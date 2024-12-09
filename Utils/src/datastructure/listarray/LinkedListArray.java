package datastructure.listarray;

import java.util.LinkedList;

/**
 * Data structure representing linked list array.
 * It allows instantiating and adding new elements. The new arrays are constructed and added to the list when needed.
 *
 * @author MTomczyk
 */

public class LinkedListArray
{
    /**
     * The number of stored elements (starts from 0).
     */
    private int _noStoredElements;

    /**
     * In-list fixed array size.
     */
    private final int _arraySize;

    /**
     * In-array current element id (points where a new value should be stored).
     */
    private int _inArrayCurrentIndex;

    /**
     * Currently processed array.
     */
    private double[] _currentArray;

    /**
     * Linked list array.
     */
    private LinkedList<double[]> _la;

    /**
     * Parameterized constructor.
     *
     * @param arraySize in-list fixed array size
     */
    public LinkedListArray(int arraySize)
    {
        assert arraySize > 0;
        _arraySize = arraySize;
        reset();
    }

    /**
     * Restarts data.
     */
    public void reset()
    {
        clearData();
        _la = new LinkedList<>();
        _currentArray = new double[_arraySize];
        _la.add(_currentArray);
    }

    /**
     * Clears data.
     */
    public void clearData()
    {
        _noStoredElements = 0;
        _inArrayCurrentIndex = 0;
        _la = null;
        _currentArray = null;
    }

    /**
     * Adds a new value.
     *
     * @param v value to be added
     */
    public void addValue(double v)
    {
        if (_inArrayCurrentIndex >= _arraySize) // construct new list
        {
            _currentArray = new double[_arraySize];
            _la.add(_currentArray);
            _inArrayCurrentIndex = 0;
        }

        _currentArray[_inArrayCurrentIndex++] = v;
        _noStoredElements++;
    }

    /**
     * Getter for the linked list array
     *
     * @return linked list array.
     */
    public LinkedList<double[]> getLinkedListArray()
    {
        return _la;
    }

    /**
     * Getter for the NO. stored elements.
     *
     * @return no stored elements.
     */
    public int getNoStoredElements()
    {
        return _noStoredElements;
    }

    /**
     * Transforms the contained data into a single (flat) array and returns (can be computationally expensive).
     *
     * @return new array of doubles (stored data).
     */
    public double[] getTransformedToArray()
    {
        double[] a = new double[_noStoredElements];
        int pnt = 0;
        int taken = 0;
        if (_noStoredElements == 0) return a;

        for (double[] arr : _la)
            for (double v : arr)
            {
                a[pnt++] = v;
                taken++;
                if (taken == _noStoredElements) return a;
            }
        return a;
    }
}
