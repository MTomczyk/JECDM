package color;

/**
 * Provides palettes: list of colors that can be used when establishing default colors (e.g., marker colors).
 *
 * @author MTomczyk
 */

public class ColorPalettes
{
    /**
     * Color getter.
     *
     * @param i index
     * @return color from the color cycle
     */
    public static Color getFromDefaultPalette(int i)
    {
        return _colors[i % _colors.length];
    }

    /**
     * List of colors.
     */
    public static final Color[] _colors = new Color[]
            {
                    new Color(31, 119, 180, 255),
                    new Color(255, 127, 14, 255),
                    new Color(44, 160, 44, 255),
                    new Color(214, 39, 40, 255),
                    new Color(148, 103, 189, 255),
                    new Color(140, 86, 75, 255),
                    new Color(227, 119, 194, 255),
                    new Color(127, 127, 127, 255),
                    new Color(188, 189, 34, 255),
                    new Color(23, 190, 207, 255),
            };
}
