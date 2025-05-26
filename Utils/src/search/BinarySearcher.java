package search;

/**
 * Object supporting performing a binary search.
 *
 * @author MTomczyk
 */

public class BinarySearcher
{
    /**
     * Lower index.
     */
    private int _li;

    /**
     * Upper index.
     */
    private int _ui;

    /**
     * Current index.
     */
    private int _ci;

    /**
     * Parameterized constructor. Sets the default bounds for indices to 0, 1.
     */
    public BinarySearcher()
    {
        this(0, 1);
    }


    /**
     * Parameterized constructor. Bounds for indices to be examined have to be provided.
     *
     * @param li lower bound
     * @param ui upper bound
     */
    public BinarySearcher(int li, int ui)
    {
        setData(li, ui);
    }

    /**
     * Sets bounds.
     *
     * @param li lower bound
     * @param ui upper bound
     */
    public void setData(int li, int ui)
    {
        _li = li;
        _ui = ui;
    }

    /**
     * Setter for the index (use with extra caution).
     *
     * @param ci new index
     */
    public void setIndex(int ci)
    {
        _ci = ci;
    }

    /**
     * Generates and returns the next index to be examined.
     *
     * @return new index to be examined
     */
    public int generateIndex()
    {
        if (_li == _ui)
        {
            _ci = _li;
            return _ci;
        }

        _ci = _li + (_ui - _li) / 2;
        return _ci;
    }

    /**
     * Updates lower bound on demand.
     */
    public void goRight()
    {
        if (_li != _ui) _li = _ci + 1;
    }

    /**
     * Updates upper bound on demand.
     */
    public void goLeft()
    {
        if (_li != _ui) _ui = _ci - 1;
    }


}
