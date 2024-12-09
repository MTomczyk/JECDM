package thread;

import java.util.concurrent.CyclicBarrier;

/**
 * Abstract runnable class that can be extended and used in the context of waiting for a series of threads
 * performing on the same array-like data type to end.
 *
 * @author MTomczyk
 */

public abstract class AbstractCyclicBarrierArrayProcessor extends AbstractCyclicBarrier implements Runnable
{
    /**
     * Begin = begin index (inclusive) of the part of the data in the data array the thread is responsible to process.
     */
    protected int _begin;

    /**
     * End = end index (exclusive) of the part of the data in the data array the thread is responsible to process.
     */
    protected int _end;

    /**
     * Offset used when traversing through array.
     */
    protected int _offset;

    /**
     * Default constructor.
     */
    public AbstractCyclicBarrierArrayProcessor()
    {
        super();
        _begin = 0;
        _end = 0;
        _offset = 0;
    }

    /**
     * Parameterized constructor
     *
     * @param barrier reference to cyclic barrier
     * @param begin   begin index (inclusive) of the part of the data in the data array the thread is responsible to process.
     * @param end     end index (exclusive) of the part of the data in the data array the thread is responsible to process.
     * @param offset  offset used when traversing through an array.
     */
    public AbstractCyclicBarrierArrayProcessor(CyclicBarrier barrier, int begin, int end, int offset)
    {
        super();
        _barrier = barrier;
        _begin = begin;
        _end = end;
        _offset = offset;
    }


    /**
     * Sets params.
     *
     * @param begin  begin = begin index (inclusive) of the part of the data in the data array the thread is responsible to process
     * @param end    end = end index (exclusive) of the part of the data in the data array the thread is responsible to process
     * @param offset offset used when traversing through an array.
     */
    public void setParams(int begin, int end, int offset)
    {
        _begin = begin;
        _end = end;
        _offset = offset;
    }

    /**
     * Auxiliary method terminating the execution at the start.
     *
     * @return true: terminate the main function, false otherwise
     */
    @Override
    protected boolean prematureStop()
    {
        if ((_begin < 0) || (_end < 0))
        {
            endBarrier();
            return true;
        }
        return false;
    }
}
