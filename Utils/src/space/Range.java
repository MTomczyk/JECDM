package space;

/**
 * Class representing a range (double bounds).
 *
 * @author MTomczyk
 */


public class Range
{
    /**
     * Left limit (allowed to be not greater than the right).
     */
    private double _left;

    /**
     * Right limit (allowed to be not smaller than the right).
     */
    private double _right;

    /**
     * If true, left > right case is allowed.
     */
    private final boolean _canCanBeInvalid;


    /**
     * Returns object instance representing an [0, 1] interval.
     *
     * @return object instance
     */
    public static Range getNormalRange()
    {
        return new Range();
    }

    /**
     * Returns object instance representing an [0, right bound] interval.
     *
     * @param right right bound (should be greater than the left bound)
     * @return object instance
     */
    public static Range get0R(double right)
    {
        return new Range(0.0d, right);
    }

    /**
     * Default constructor: sets ranges to: left = 0.0, right =  1.0.
     */
    public Range()
    {
        this(0.0d, 1.0d, false);
    }


    /**
     * Parameterized constructor.
     *
     * @param left  left boundary
     * @param right right boundary
     */
    public Range(double left, double right)
    {
        this(left, right, false);
    }

    /**
     * Parameterized constructor.
     *
     * @param left         left boundary
     * @param right        right boundary
     * @param canBeInvalid if true, the range can be invalid (i.e., left > right); false otherwise
     */
    public Range(double left, double right, boolean canBeInvalid)
    {
        assert canBeInvalid || left <= right;
        _left = left;
        _right = right;
        _canCanBeInvalid = canBeInvalid;
    }

    /**
     * Constructs an array of default ranges (0, 1).
     *
     * @param n the length of an array
     * @return the constructed array
     */
    public static Range[] getDefaultRanges(int n)
    {
        return getDefaultRanges(n, 1.0d);
    }

    /**
     * Constructs an array of default ranges (0, r).
     *
     * @param n the length of an array
     * @param r right limit
     * @return the constructed array
     */
    public static Range[] getDefaultRanges(int n, double r)
    {
        Range[] dr = new Range[n];
        for (int i = 0; i < n; i++) dr[i] = new Range(0.0d, r, false);
        return dr;
    }

    /**
     * Method verifies if a point (value) is in range (inclusive/closed interval).
     *
     * @param v considered value
     * @return true = point is in range; false = otherwise
     */
    public boolean isInRange(double v)
    {
        return isInRange(v, true);
    }

    /**
     * Method verifies if a point (value) is in range.
     *
     * @param v         considered value
     * @param inclusive true = inclusive / closed interval; false = otherwise
     * @return true = point is in range; false = otherwise
     */
    public boolean isInRange(double v, boolean inclusive)
    {
        if (inclusive)
        {
            if (Double.compare(v, _left) < 0) return false;
            return Double.compare(v, _right) <= 0;
        }
        else
        {
            if (Double.compare(v, _left) <= 0) return false;
            return Double.compare(v, _right) < 0;
        }
    }

    /**
     * Calculates the interval.
     *
     * @return interval
     */
    public double getInterval()
    {
        return _right - _left;
    }

    /**
     * Constructs a deep copy of the object.
     *
     * @return deep copy
     */
    public Range getClone()
    {
        return new Range(_left, _right, _canCanBeInvalid);
    }


    /**
     * Checks if this object has the same boundaries as the other one.
     *
     * @param r the other object
     * @return true = bounds are the same; false = otherwise
     */
    public boolean isEqual(Range r)
    {
        if (Double.compare(_left, r._left) != 0) return false;
        return Double.compare(_right, r._right) == 0;
    }

    /**
     * Setter for the left boundary.
     *
     * @param left left boundary
     */
    public void setLeft(double left)
    {
        assert _canCanBeInvalid || left <= _right;
        _left = left;
    }

    /**
     * Getter for the left boundary.
     *
     * @return left boundary
     */
    public double getLeft()
    {
        return _left;
    }

    /**
     * Setter for the right boundary.
     *
     * @param right right boundary
     */
    public void setRight(double right)
    {
        assert _canCanBeInvalid || right >= _left;
        _right = right;
    }

    /**
     * Getter for the right boundary.
     *
     * @return right boundary
     */
    public double getRight()
    {
        return _right;
    }

    /**
     * Setter for both boundaries.
     *
     * @param left  left boundary
     * @param right right boundary
     */
    public void setValues(double left, double right)
    {
        assert _canCanBeInvalid || left <= right;
        _left = left;
        _right = right;
    }

    /**
     * Checks if the currently stored is invalid (left > right).
     *
     * @return if true -> data is invalid, false otherwise
     */
    public boolean isInvalid()
    {
        return Double.compare(_left, _right) > 0;
    }

    /**
     * To string method.
     *
     * @return string representing the range
     */
    @Override
    public String toString()
    {
        return "[" + _left + " " + _right + "]";
    }
}
