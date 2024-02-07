package ooxml.word;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import ooxml.*;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
@Setter(AccessLevel.NONE)
public class RunProperties {
    @Nullable
    Color foreground;
    @Nullable
    Color background;
    @Nullable
    Justify alignment;
    // half-point values - i.e. real font size is half of this value (48 = 24, 47 = 23.5)
    int fontSize;
    @Nullable
    Break breakType;
}
