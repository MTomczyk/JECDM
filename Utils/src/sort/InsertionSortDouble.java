package sort;

/**
 * Supportive method for sorting doubles (insertion sort; increasing order).
 *
 * @author MTomczyk
 */

public class InsertionSortDouble
{
    /**
     * Array that is kept sorted.
     */
    public double[] _data = null;

    /**
     * If true, the array is sorted in increasing order. False: decreasing order.
     */
    private boolean _increasing;

    /**
     * Pointer (index) to the last sorted element.
     */
    private int _pointer = -1;

    /**
     * Parameterized constructor.
     *
     * @param n          the number of elements must be provided a priori (it can be lesser than the total number of values
     *                   inserted, the not n-best value will be ignored)
     * @param increasing if true, the values are sorted in the increasing order; false = otherwise.
     */
    public void init(int n, boolean increasing)
    {
        assert n >= 1;
        _data = new double[n];
        _pointer = -1;
        _increasing = increasing;
    }

    /**
     * Adds sequentially new elements and sorts the array.
     *
     * @param values array of values to be added
     */
    public void add(double[] values)
    {
        for (double v : values) add(v);
    }

    /**
     * Adds a new element and sorts the array.
     *
     * @param value value to be inserted
     */
    public void add(double value)
    {
        if (_pointer >= _data.length - 1)
        {
            if ((_increasing) && (value < _data[_pointer])) _data[_pointer] = value;
            else if ((!_increasing) && (value > _data[_pointer])) _data[_pointer] = value;
            else return;
        } else
        {
            _pointer++;
            _data[_pointer] = value;
        }

        if (_pointer == 0) return;

        for (int i = _pointer; i > 0; i--)
        {
            if (
                    ((_increasing) && (_data[i - 1] > _data[i])) ||
                            ((!_increasing) && (_data[i - 1] < _data[i])))
            {
                _data[i] = _data[i - 1];
                _data[i - 1] = value;
            } else break;
        }

    }
}
