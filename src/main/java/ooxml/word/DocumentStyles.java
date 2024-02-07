package ooxml.word;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
public class DocumentStyles {
    /** document-level styles */
    private Style defaultStyle;
    @Setter(AccessLevel.NONE)
    private final Map<String, Style> styles = new HashMap<>();

    public void add(String name, Style style) {
        styles.put(name, style);
    }
}
