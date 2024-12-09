package space;

/**
 * Basic class representing a pair (coordinate value; size) for a dimension, e.g., x and width.
 * Note that the size cannot be negative.
 *
 * @author MTomczyk
 */
public class Dimension
{
    /**
     * Coordinate value.
     */
    public double _position;

    /**
     * Size.
     */
    public double _size;


    /**
     * Default constructor.
     */
    public Dimension()
    {
        this(0.0d, 1.0d);
    }

    /**
     * Parameterized constructor.
     *
     * @param position coordinate value (e.g., x)
     * @param size     size (e.g., width)
     */
    public Dimension(double position, double size)
    {
        assert Double.compare(size, 0.0d) >= 0;
        _position = position;
        _size = size;
    }

    /**
     * Returns the sum of the position and the size (right part/coordinate).
     *
     * @return right position
     */
    public double getRightPosition()
    {
        return _position + _size;
    }
}
