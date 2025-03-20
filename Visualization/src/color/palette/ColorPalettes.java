package color.palette;

import color.Color;

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
     * @param i colo index (at least 0; if the index exceeds array length, the index is derived using modulo operation (cyclic access)
     * @return color from the color cycle (null if the index is negative)
     */
    public static Color getFromDefaultPalette(int i)
    {
        if (i < 0) return null;
        if (i >= DefaultPalette._colors.length) return DefaultPalette._colors[i % DefaultPalette._colors.length];
        else return DefaultPalette._colors[i];
    }
}
