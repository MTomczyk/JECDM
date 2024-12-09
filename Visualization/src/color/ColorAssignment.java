package color;

/**
 * Represents value -> color mapping (used, e.g., in gradients).
 *
 * @author MTomczyk
 */


public class ColorAssignment
{
    /**
     * Double value.
     */
    public final float _value;

    /**
     * Assigned color.
     */
    public final Color _color;

    /**
     * Parameterized constructor.
     *
     * @param value double value
     * @param color assigned color
     */
    public ColorAssignment(float value, Color color)
    {
        _value = value;
        _color = color;
    }
}
