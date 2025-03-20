package color.palette;

/**
 * Default color palette
 *
 * @author MTomczyk
 */
public class DefaultPalette extends AbstractColorPalette
{
    /**
     * Static array of palette colors.
     */
    public static final color.Color[] _colors = new color.Color[]
            {
                    new color.Color(31, 119, 180, 255),
                    new color.Color(255, 127, 14, 255),
                    new color.Color(44, 160, 44, 255),
                    new color.Color(214, 39, 40, 255),
                    new color.Color(148, 103, 189, 255),
                    new color.Color(140, 86, 75, 255),
                    new color.Color(227, 119, 194, 255),
                    new color.Color(127, 127, 127, 255),
                    new color.Color(188, 189, 34, 255),
                    new color.Color(23, 190, 207, 255),
            };

    /**
     * Default constructor.
     */
    public DefaultPalette()
    {
        super(_colors);
    }
}
