package color.gradient;

import color.ColorAssignment;

import java.util.ArrayList;

/**
 * Class representing a color (encapsulates the gradient class).
 *
 * @author MTomczyk
 */

public class Color extends Gradient
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
    public static final color.Color DARK = new color.Color(49, 51, 54);

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
     * Parameterized constructor.
     *
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     */
    public Color(int r, int g, int b)
    {
        this(r, g, b, 255);
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
        _points = new ArrayList<>(1);
        _points.add(new ColorAssignment(0.0f, new color.Color(r, g, b, a)));
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
        this(r, g, b, 1.0f);
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
        _points = new ArrayList<>(1);
        _points.add(new ColorAssignment(0.0f, new color.Color(r, g, b, a)));
    }

    /**
     * Parameterized constructor.
     *
     * @param color mono-color
     */
    public Color(color.Color color)
    {
        _points = new ArrayList<>(1);
        _points.add(new ColorAssignment(0.0f, color));
    }

    /**
     * Checks if the object represents just a single color (always true for this class).
     *
     * @return true = there is no gradient; false = otherwise
     */
    public boolean isMonoColor()
    {
        return true;
    }

    /**
     * Returns the default color (input does not matter).
     *
     * @param value input value (does not matter)
     * @return default color
     */
    public color.Color getColor(float value)
    {
        return _points.get(0)._color;
    }


    /**
     * Overwrites the parent method (does nothing).
     *
     * @param ca color assignment
     */
    @Override
    public void add(ColorAssignment ca)
    {

    }
}
