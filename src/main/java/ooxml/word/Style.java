package ooxml.word;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@Setter(AccessLevel.NONE)
public class Style {
    private String name;
    private String basedOn;
    private ParagraphProperties paragraphProperties;
    private RunProperties runProperties;
}
