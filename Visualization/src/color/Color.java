package color;

/**
 * Class representing a color (RGBA).
 * The class extends java.awt.Color.
 *
 * @author MTomczyk
 */


public class Color extends java.awt.Color
{
    /**
     * Red color.
     */
    public static final Color RED = new Color(255, 0, 0);

    /**
     * Green color.
     */
    public static final Color GREEN = new Color(0, 255, 0);

    /**
     * Blue color.
     */
    public static final Color BLUE = new Color(0, 0, 255);

    /**
     * Cyan color.
     */
    public static final Color CYAN = new Color(0, 255, 255);

    /**
     * Magenta color.
     */
    public static final Color MAGENTA = new Color(255, 0, 255);

    /**
     * Yellow color.
     */
    public static final Color YELLOW = new Color(255, 255, 0);

    /**
     * Black color.
     */
    public static final Color BLACK = new Color(0, 0, 0);

    /**
     * Dark color.
     */
    public static final Color DARK = new Color(49, 51, 54);

    /**
     * White color.
     */
    public static final Color WHITE = new Color(255, 255, 255);

    /**
     * Light gray color (75%).
     */
    public static final Color GRAY_75 = new Color(183, 183, 183);

    /**
     * Gray color (50%).
     */
    public static final Color GRAY_50 = new Color(122, 122, 122);

    /**
     * Dark gray color (dark 25%).
     */
    public static final Color GRAY_25 = new Color(61, 61, 61);

    /**
     * Float representation of the red component.
     */
    public final float _r;

    /**
     * Float representation of the green component.
     */
    public final float _g;

    /**
     * Float representation of the blue component.
     */
    public final float _b;

    /**
     * Float representation of the alpha component.
     */
    public final float _a;

    /**
     * Parameterized constructor.
     *
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     */
    public Color(int r, int g, int b)
    {
        super(r, g, b, 255);
        _r = r / 255.0f;
        _g = g / 255.0f;
        _b = b / 255.0f;
        _a = 1.0f;
    }

    /**
     * Parameterized constructor.
     *
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @param a alpha component (0-255)
     */
    public Color(int r, int g, int b, int a)
    {
        super(r, g, b, a);
        _r = r / 255.0f;
        _g = g / 255.0f;
        _b = b / 255.0f;
        _a = a / 255.0f;
    }

    /**
     * Parameterized constructor.
     *
     * @param r red component (0.0f-1.0f)
     * @param g green component (0.0f-1.0f)
     * @param b blue component (0.0f-1.0f)
     */
    public Color(float r, float g, float b)
    {
        super(r, g, b, 1.0f);
        _r = r;
        _g = g;
        _b = b;
        _a = 1.0f;
    }

    /**
     * Parameterized constructor.
     *
     * @param r red component (0.0f-1.0f)
     * @param g green component (0.0f-1.0f)
     * @param b blue component (0.0f-1.0f)
     * @param a alpha component (0.0f-1.0f)
     */
    public Color(float r, float g, float b, float a)
    {
        super(r, g, b, a);
        _r = r;
        _g = g;
        _b = b;
        _a = a;
    }

    /**
     * Returns a transparent instance of the color (revises the alpha channel value).
     *
     * @param a new value for the alpha channel (0-255).
     * @return revised color
     */
    public Color getTransparentInstance(int a)
    {
        return getTransparentInstance((float) a / 255.0f);
    }

    /**
     * Returns a transparent instance of the color (revises the alpha channel value).
     *
     * @param a new value for the alpha channel (0.0f-1.0f).
     * @return revised color
     */
    public Color getTransparentInstance(float a)
    {
        return new Color(_r, _g, _b, a);
    }

    /**
     * Checks if the object represents the same color as another object.
     *
     * @param o other object (should be an instance of this class)
     * @return true = colors are the same; false = otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof Color color)) return false;
        if (getRed() != color.getRed()) return false;
        if (getGreen() != color.getGreen()) return false;
        if (getBlue() != color.getBlue()) return false;
        return (getAlpha() == color.getAlpha());
    }

    /**
     * Constructs object's deep copy.
     *
     * @return object's deep copy.
     */
    public Color getClone()
    {
        return new Color(getRed(), getGreen(), getBlue(), getAlpha());
    }
}
