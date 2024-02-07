package ooxml.word.parser;

import ooxml.Break;
import ooxml.Color;
import ooxml.Justify;
import ooxml.word.Run;
import ooxml.word.RunProperties;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.Optional;

public class RunParser {

    public Run createRun(Node runNode) throws Exception {
        if (!runNode.getNodeName().equals("w:r")) {
            throw new UnsupportedOperationException("not an r element");
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
