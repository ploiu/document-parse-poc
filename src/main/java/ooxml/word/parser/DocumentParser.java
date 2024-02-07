package ooxml.word.parser;

import ooxml.Color;
import ooxml.word.Document;
import ooxml.word.Paragraph;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DocumentParser {
    private final ParagraphParser paragraphParser = new ParagraphParser();
    private final XPath xpath = XPathFactory.newInstance().newXPath();

    public Document parse(File file) throws Exception {
        var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        var doc = builder.parse(file);
        doc.normalizeDocument();
        var paragraphs = buildParagraphs(doc);
        // parse styles
        var color = xpath.evaluate("string(document/background/@color)", doc);
        var documentBuilder = Document.builder()
                .paragraphs(paragraphs)
                .background(new Color(color));
        return documentBuilder.build();
    }

    private List<Paragraph> buildParagraphs(org.w3c.dom.Document doc) throws Exception {
        var graphs = (NodeList) xpath.evaluate("/document/body/p", doc, XPathConstants.NODESET);
        var paragraphs = new LinkedList<Paragraph>();
        for (int i = 0; i < graphs.getLength(); i++) {
            paragraphs.add(paragraphParser.createParagraph(graphs.item(i)));
        }
        return paragraphs;
    }
}
