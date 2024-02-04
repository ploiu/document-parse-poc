import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.lingala.zip4j.ZipFile;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Main {
    static ClassLoader loader = Main.class.getClassLoader();

    public static void main(String[] args) throws Exception {
        var inputFile = loader.getResource("test.docx");
        var splitPath = inputFile.getPath().split("/");
        var fileName = splitPath[splitPath.length - 1];
        var path = inputFile.getPath().replace("/" + fileName, "");
        try (var zipFile = new ZipFile(inputFile.getPath())) {
            zipFile.extractAll(path);
            var documentFile = new File(loader.getResource("word/document.xml").getPath());
            var paragraphs = buildParagraphs(documentFile);
            var mapper = new ObjectMapper().registerModule(new Jdk8Module());
            System.out.println(mapper.writeValueAsString(paragraphs));
        }

    }

    static List<Paragraph> buildParagraphs(File inputFile) throws Exception {
        var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        var doc = builder.parse(inputFile);
        doc.normalizeDocument();
        doc.normalizeDocument();
        var xpath = XPathFactory.newInstance().newXPath();
        var graphs = (NodeList) xpath.evaluate("/document/body/p", doc, XPathConstants.NODESET);
        var paragraphs = new LinkedList<Paragraph>();
        for (int i = 0; i < graphs.getLength(); i++) {
            paragraphs.add(createParagraph(graphs.item(i)));
        }
        return paragraphs;
    }

    static Paragraph createParagraph(Node paragraphNode) throws Exception {
        if (!paragraphNode.getNodeName().equals("w:p")) {
            throw new UnsupportedOperationException("not a p element");
        }
        var xpath = XPathFactory.newInstance().newXPath();
        var propertiesNode = (Node) xpath.evaluate("pPr", paragraphNode, XPathConstants.NODE);
        var alignment = xpath.evaluate("string(jc/@val)", propertiesNode);
        var background = xpath.evaluate("string(shd/@fill)", propertiesNode);
        var paragraphProperties = ParagraphProperties.builder()
                .background(new Color(background));
        if (!alignment.isBlank()) {
            paragraphProperties.alignment(Justify.valueOf(alignment.toUpperCase()));
        }
        var runNodes = (NodeList) xpath.evaluate("r", paragraphNode, XPathConstants.NODESET);
        var runs = new LinkedList<Run>();
        for (int i = 0; i < runNodes.getLength(); i++) {
            runs.add(createRun(runNodes.item(i)));
        }
        return new Paragraph(paragraphProperties.build(), runs);
    }

    static Run createRun(Node runNode) throws Exception {
        if (!runNode.getNodeName().equals("w:r")) {
            throw new UnsupportedOperationException("not a r element");
        }
        var xpath = XPathFactory.newInstance().newXPath();
        @Nullable var propertiesNode = (Node) xpath.evaluate("rPr", runNode, XPathConstants.NODE);
        var runPropertiesBuilder = RunProperties.builder();
        var breakType = xpath.evaluate("string(br/@type)", runNode);
        if (!breakType.isBlank()) {
            runPropertiesBuilder.breakType(Break.valueOf(breakType.toUpperCase()));
        }
        if (propertiesNode != null) {
            var foreground = xpath.evaluate("string(color/@val)", propertiesNode);
            var background = xpath.evaluate("string(shd/@fill)", propertiesNode);
            var alignment = xpath.evaluate("string(jc/@val)", propertiesNode);
            var fontSize = xpath.evaluate("number(sz/@val)", propertiesNode);
            if (!alignment.isBlank()) {
                runPropertiesBuilder.alignment(Justify.valueOf(alignment.toUpperCase()));
            }
            if (fontSize != null && !"NaN".equalsIgnoreCase(fontSize)) {
                runPropertiesBuilder.fontSize(Integer.parseInt(fontSize));
            }
            if (!foreground.isBlank()) {
                runPropertiesBuilder.foreground(new Color(foreground));
            }
            if (!background.isBlank()) {
                runPropertiesBuilder.background(new Color(background));
            }

        }
        var text = Optional.ofNullable(xpath.evaluate("t/text()", runNode));
        return new Run(runPropertiesBuilder.build(), text);
    }

}
