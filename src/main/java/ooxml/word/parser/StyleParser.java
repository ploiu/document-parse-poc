package ooxml.word.parser;

import ooxml.word.DocumentStyles;
import ooxml.word.Style;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class StyleParser {
    private final XPath xpath = XPathFactory.newInstance().newXPath();

    public DocumentStyles parseStyles(File styleFile) {
        throw new UnsupportedOperationException();
    }

    private Style parseDefaultStyles(org.w3c.dom.Document doc) {
        throw new UnsupportedOperationException();
    }

    private Style parseStyle(Node node) {
        throw new UnsupportedOperationException();
    }
}
