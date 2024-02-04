import lombok.*;


@Data
@Builder
@Setter(AccessLevel.NONE)
public class ParagraphProperties {
    private Justify alignment;
    private Color background;
}
