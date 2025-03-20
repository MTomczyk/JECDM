package color.palette;

import color.Color;

/**
 * Class representing a color palette (series of colors organized in an array with cyclical access).
 *
 * @author MTomczyk
 */
public class AbstractColorPalette
{
    /**
     * Colors.
     */
    protected final Color[] _colors;

    /**
     * Parameterized constructor.
     *
     * @param colors palette (colors)
     */
    protected AbstractColorPalette(Color[] colors)
    {
        _colors = colors;
    }

    /**
     * Color getter.
     *
     * @param i colo index (at least 0; if the index exceeds array length, the index is derived using modulo operation (cyclic access)
     * @return color from the color cycle (null if the index is negative)
     */
    public Color getColor(int i)
    {
        if (i < 0) return null;
        if (i >= DefaultPalette._colors.length) return DefaultPalette._colors[i % DefaultPalette._colors.length];
        else return DefaultPalette._colors[i];
    }
}
