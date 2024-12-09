package thread;

import java.util.concurrent.CountDownLatch;

/**
 * Abstract runnable class that can be extended and used in the context of waiting for a series of threads
 * performing on the same array-like data type to end.
 *
 * @author MTomczyk
 */

public class AbstractLatchArrayProcessor implements Runnable
{
    /**
     * Count down latch.
     */
    protected CountDownLatch _latch;

    /**
     * Begin = begin index (inclusive) of the part of the data in the data array the thread is responsible to process.
     */
    protected int _begin;

    /**
     * End = end index (exclusive) of the part of the data in the data array the thread is responsible to process.
     */
    protected int _end;

    /**
     * Offset used when traversing through an array.
     */
    protected int _offset;

    /**
     * Default constructor.
     */
    public AbstractLatchArrayProcessor()
    {
        _latch = null;
        _begin = 0;
        _end = 0;
        _offset = 0;
    }

    /**
     * Parameterized constructor
     * @param latch count down latch
     * @param begin begin index (inclusive) of the part of the data in the data array the thread is responsible to process.
     * @param end  end index (exclusive) of the part of the data in the data array the thread is responsible to process.
     * @param offset offset used when traversing through an array.
     */
    public AbstractLatchArrayProcessor(CountDownLatch latch, int begin, int end, int offset)
    {
        _latch = latch;
        _begin = begin;
        _end = end;
        _offset = offset;
    }

    /**
     * Default (empty) implementation.
     */
    @Override
    public void run()
    {

    }

    /**
     * Sets params.
     * @param latch count down latch
     * @param begin begin = begin index (inclusive) of the part of the data in the data array the thread is responsible to process
     * @param end end = end index (exclusive) of the part of the data in the data array the thread is responsible to process
     * @param offset offset used when traversing through an array.
     */
    public void setParams(CountDownLatch latch, int begin, int end, int offset)
    {
        _latch = latch;
        _begin = begin;
        _end = end;
        _offset = offset;
    }

}
