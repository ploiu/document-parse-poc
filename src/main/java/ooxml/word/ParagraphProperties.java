package ooxml.word;

import lombok.*;
import ooxml.Color;
import ooxml.Justify;


@Data
@Builder
@Setter(AccessLevel.NONE)
public class ParagraphProperties {
    private Justify alignment;
    private Color background;
    private String styleType;
}
