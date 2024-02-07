package ooxml.word.parser;

import ooxml.Color;
import ooxml.Justify;
import ooxml.word.Paragraph;
import ooxml.word.ParagraphProperties;
import ooxml.word.Run;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.LinkedList;

public class ParagraphParser {
    private final RunParser runParser = new RunParser();

    Paragraph createParagraph(Node paragraphNode) throws Exception {
        if (!paragraphNode.getNodeName().equals("w:p")) {
            throw new UnsupportedOperationException("not a p element");
        }
        var xpath = XPathFactory.newInstance().newXPath();
        var propertiesNode = (Node) xpath.evaluate("pPr", paragraphNode, XPathConstants.NODE);
        var alignment = xpath.evaluate("string(jc/@val)", propertiesNode);
        var background = xpath.evaluate("string(shd/@fill)", propertiesNode);
        var styleType = xpath.evaluate("string(pStyle/@val)", propertiesNode);
        var paragraphProperties = ParagraphProperties.builder()
                .background(new Color(background))
                .styleType(styleType);
        if (!alignment.isBlank()) {
            paragraphProperties.alignment(Justify.valueOf(alignment.toUpperCase()));
        }
        var runNodes = (NodeList) xpath.evaluate("r", paragraphNode, XPathConstants.NODESET);
        var runs = new LinkedList<Run>();
        for (int i = 0; i < runNodes.getLength(); i++) {
            runs.add(runParser.createRun(runNodes.item(i)));
        }
        return new Paragraph(paragraphProperties.build(), runs);
    }

}
