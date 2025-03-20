package color.gradient;

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
    public static color.gradient.Color getFromDefaultPalette(int i)
    {
        return new color.gradient.Color(color.palette.ColorPalettes.getFromDefaultPalette(i));
    }

}
