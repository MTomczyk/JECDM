package alternative;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Abstract generic class wrapping an array of alternatives (or their wrappers)
 *
 * @author MTomczyk
 */
public class AbstractAlternatives<T extends IAlternativeWrapper> implements Iterable<T>
{
    /**
     * Dedicated iterator.
     */
    private class ArrayIterator implements Iterator<T>
    {
        /**
         * Current index.
         */
        private int index = 0;

        /**
         * Checks if it has next element.
         * @return true, if there is next element; false otherwise
         */
        public boolean hasNext()
        {
            return index < _alternatives.size();
        }

        /**
         * Returns the next element.
         * @return the next element
         */
        public T next()
        {
            return _alternatives.get(index++);
        }
    }

    /**
     * Wrapped array.
     */
    protected final ArrayList<T> _alternatives;

    /**
     * Parameterized constructor. If the input is null, an empty array is created.
     *
     * @param alternatives array of alternatives to be wrapped
     */
    public AbstractAlternatives(ArrayList<T> alternatives)
    {
        if (alternatives == null) _alternatives = new ArrayList<>();
        else _alternatives = alternatives;
    }

    /**
     * Parameterized constructor. Wraps one alternative.
     *
     * @param alternative alternative to be wrapped.
     */
    public AbstractAlternatives(T alternative)
    {
        _alternatives = new ArrayList<>(1);
        _alternatives.add(alternative);
    }


    /**
     * Parameterized constructor. Wraps two alternatives (in the given order).
     *
     * @param alternative1 alternative to be wrapped.
     * @param alternative2 alternative to be wrapped.
     */
    public AbstractAlternatives(T alternative1, T alternative2)
    {
        _alternatives = new ArrayList<>(2);
        _alternatives.add(alternative1);
        _alternatives.add(alternative2);
    }

    /**
     * Parameterized constructor. Wraps three alternatives (in the given order).
     *
     * @param alternative1 alternative to be wrapped.
     * @param alternative2 alternative to be wrapped.
     * @param alternative3 alternative to be wrapped.
     */
    public AbstractAlternatives(T alternative1, T alternative2, T alternative3)
    {
        _alternatives = new ArrayList<>(3);
        _alternatives.add(alternative1);
        _alternatives.add(alternative2);
        _alternatives.add(alternative3);
    }

    /**
     * Returns the number of alternatives.
     *
     * @return the number of alternatives
     */
    public int size()
    {
        return _alternatives.size();
    }

    /**
     * Returns the k-th alternative (to be overwritten).
     *
     * @param index index
     * @return k-th alternative
     */
    public Alternative get(int index)
    {
        return _alternatives.get(index).getAlternative();
    }

    /**
     * Creates a copy of the array of alternatives (elements are copied by reference).
     *
     * @return new array of alternatives
     */
    public AbstractAlternatives<T> getCopy()
    {
        ArrayList<T> alternatives = new ArrayList<>(_alternatives.size());
        alternatives.addAll(_alternatives);
        return new AbstractAlternatives<>(alternatives);
    }


    /**
     * Checks if the alternatives array is empty.
     * @return true, if there are no alternatives stored; false otherwise.
     */
    public boolean isEmpty()
    {
        return _alternatives.isEmpty();
    }

    /**
     * Returns the wrapped array's iterator.
     *
     * @return iterator.
     */
    @Override
    public Iterator<T> iterator()
    {
        return new ArrayIterator();
    }

}
