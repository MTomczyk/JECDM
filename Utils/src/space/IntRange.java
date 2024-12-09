package space;


/**
 * Class representing a range (integer bounds).
 *
 * @author MTomczyk
 */


public class IntRange
{
    /**
     * Left boundary.
     */
    private int _left;
    /**
     * Right boundary.
     */
    private int _right;

    /**
     * If true, left > right case is allowed.
     */
    private final boolean _canCanBeInvalid;

    /**
     * Default constructor: sets ranges to: left = 0.0, right =  1.0.
     */
    public IntRange()
    {
        this(0, 1, false);
    }

    /**
     * Parameterized constructor.
     *
     * @param left  left boundary
     * @param right right boundary
     */
    public IntRange(int left, int right)
    {
        this(left, right, false);
    }

    /**
     * Parameterized constructor.
     *
     * @param left         left boundary
     * @param right        right boundary
     * @param canBeInvalid if true, left > right case is allowed
     */
    public IntRange(int left, int right, boolean canBeInvalid)
    {
        _canCanBeInvalid = canBeInvalid;
        assert canBeInvalid || left <= right;
        _left = left;
        _right = right;
    }

    /**
     * Method verifies if a point (value) is in range.
     *
     * @param v         considered value
     * @param inclusive true = inclusive / closed interval; false = otherwise
     * @return true = point is in range; false = otherwise
     */
    public boolean isInRange(int v, boolean inclusive)
    {
        if (inclusive) return !((v < _left) || (v > _right));
        else return !((v <= _left) || (v >= _right));
    }

    /**
     * Calculates the interval.
     *
     * @return interval
     */
    public int getInterval()
    {
        return _right - _left;
    }

    /**
     * Constructs a deep copy of the object.
     *
     * @return deep copy
     */
    public IntRange getClone()
    {
        return new IntRange(_left, _right, _canCanBeInvalid);
    }


    /**
     * Checks if this object has the same boundaries as the other one.
     *
     * @param r the other object
     * @return true = bounds are the same; false = otherwise
     */
    public boolean isEqual(IntRange r)
    {
        return (_left == r._left) && (_right == r._right);
    }

    /**
     * Setter for the left boundary.
     *
     * @param left left boundary
     */
    public void setLeft(int left)
    {
        assert _canCanBeInvalid || left <= _right;
        _left = left;
    }

    /**
     * Getter for the left boundary.
     *
     * @return left boundary
     */
    public int getLeft()
    {
        return _left;
    }

    /**
     * Setter for the right boundary.
     *
     * @param right right boundary
     */
    public void setRight(int right)
    {
        assert _canCanBeInvalid || right >= _left;
        _right = right;
    }

    /**
     * Getter for the right boundary.
     *
     * @return right boundary
     */
    public int getRight()
    {
        return _right;
    }

    /**
     * Setter for both boundaries.
     *
     * @param left  left boundary
     * @param right right boundary
     */
    public void setValues(int left, int right)
    {
        assert !_canCanBeInvalid || left <= right;
        _left = left;
        _right = right;
    }
}
