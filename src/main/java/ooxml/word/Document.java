package ooxml.word;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import ooxml.Color;

import java.util.List;

@Data
@Builder
@Setter(AccessLevel.NONE)
public class Document {
    private List<Paragraph> paragraphs;
    private DocumentStyles styles;
    private Color background;
}
