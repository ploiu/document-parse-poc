package ooxml.word;

import java.util.List;

public record Paragraph(ParagraphProperties paragraphProperties, List<Run> runs) {
}
