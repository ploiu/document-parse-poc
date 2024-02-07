import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.lingala.zip4j.ZipFile;
import ooxml.Break;
import ooxml.Color;
import ooxml.Justify;
import ooxml.word.Paragraph;
import ooxml.word.ParagraphProperties;
import ooxml.word.Run;
import ooxml.word.RunProperties;
import ooxml.word.parser.DocumentParser;
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
            var document = new DocumentParser().parse(documentFile);
            var mapper = new ObjectMapper().registerModule(new Jdk8Module());
            System.out.println(mapper.writeValueAsString(document));
        }

    }
}
