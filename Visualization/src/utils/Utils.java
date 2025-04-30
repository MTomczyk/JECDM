package utils;

import color.Color;

/**
 * Provides various utility functions.
 *
 * @author MTomczyk
 */
public class Utils
{
    /**
     * Returns a color object from a color represented as int (packed as ARGB, each channel uses one 8 bits)
     *
     * @param color color represented as int
     * @return color object
     */
    public static Color getColor(int color)
    {
        int alpha = (color >> 24) & 0xff;
        int red = (color >> 16) & 0xff;
        int green = (color >> 8) & 0xff;
        int blue = (color) & 0xff;
        return new Color(red, green, blue, alpha);
    }

    /**
     * Returns a color represented as int (packed as ARGB, each channel uses one 8 bits) from a color object.
     *
     * @param color color represented as int
     * @return color object
     */
    public static int getColor(Color color)
    {
        int argb = 0;
        argb += (color.getAlpha() << 24); // alpha
        argb += (color.getBlue() & 0xff); // blue
        argb += (color.getGreen() << 8); // green
        argb += (color.getRed() << 16); // red
        return argb;
    }
}
