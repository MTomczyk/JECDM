package swing.swingworkerqueue;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * It supports queueing swing workers (see {@link SwingWorkerQueue}) so that their processing is done in order
 * imposed by their execution. The workers are organized in blocks {@link ExecutionBlock}. The execution of workers
 * in a single block cannot be interlaced. However, the execution of the block (starting the first registered worker)
 * can be skipped if the block (its first worker) proves overdue.
 *
 * @author MTomczyk
 */
public class SwingWorkerQueue<T, V>
{
    /**
     * List of execution blocks.
     */
    private volatile LinkedList<ExecutionBlock<T, V>> _blocks = new LinkedList<>();

    /**
     * Matrix that represents how many blocks of different types and called by different
     * entities are stored in the queue. The first dimension corresponds to various callers.
     * The second to various block types.
     */
    private final int[][] _blocksDistribution;


    /**
     * Lock used to control synchronization.
     */
    protected volatile ReentrantLock _lock = new ReentrantLock();

    /**
     * Auxiliary flag indicating whether the queue allows for adding new execution blocks (true) or not (false).
     */
    protected volatile boolean _enableAddingExecutionBlocks = true;

    /**
     * Parameterized constructor.
     *
     * @param blocksTypes should represent the spectrum (number) of types of all potentially scheduled blocks
     * @param callerTypes should represent the spectrum (number) of all potential callers (entities)
     */
    public SwingWorkerQueue(int blocksTypes, int callerTypes)
    {
        _blocksDistribution = new int[callerTypes][blocksTypes];
    }

    /**
     * The method should be called from EDT (one thread).
     * Adds an execution block (packed workers) to the queue.
     * They are added collectively, i.e., this process cannot be
     * interlaced with the execution of some worker that already existed in the queue.
     * If the queue was empty prior to addition, the first added worker in the block is immediately executed.
     *
     * @param block workers to be scheduled
     */
    public void addAndScheduleExecutionBlock(ExecutionBlock<T, V> block)
    {
        _lock.lock();

        if ((!_enableAddingExecutionBlocks) || (block == null) || (block._workers.isEmpty())) // nothing to add
        {
            _lock.unlock();
            return;
        }

        for (QueuedSwingWorker<T, V> w : block._workers) w.setQueue(this);
        block._registeredTimestamp = System.nanoTime();
        _blocksDistribution[block._callerType][block._blockType]++;
        _blocks.add(block);

        // check immediate start condition
        if (_blocks.size() == 1) dispatchNextWorker();

        _lock.unlock();
    }

    /**
     * Auxiliary method that enables the queue (upcoming tasks will be accepted).
     */
    public void enableAddingExecutionBlocks()
    {
        _lock.lock();
        _enableAddingExecutionBlocks = true;
        _lock.unlock();
    }


    /**
     * Auxiliary method that enables the queue (upcoming tasks will be rejected).
     */
    public void disableAddingExecutionBlocks()
    {
        _lock.lock();
        _enableAddingExecutionBlocks = false;
        _lock.unlock();
    }

    /**
     * Auxiliary method that waits until the first execution block is NOT of a given caller type.
     *
     * @param callerType caller type
     */
    protected void waitUntilTheFirstBlockIsNotOfCallerType(int callerType)
    {
        if (_blocks.isEmpty()) return;
        while (true)
        {
            _lock.lock();
            if (_blocks.isEmpty())
            {
                _lock.unlock();
                return;
            }
            if (_blocks.getFirst()._callerType != callerType) break;
            _lock.unlock();
        }
    }

    /**
     * Auxiliary method that removes all execution blocks in the queue whose types match the input.
     *
     * @param callerType type (id) of blocks that are to be removed
     */
    public void removeExecutionBlocksWithCallerType(int callerType)
    {
        _lock.lock();
        if ((_blocks == null) || (_blocks.isEmpty()))
        {
            _lock.unlock();
            return;
        }

        ListIterator<ExecutionBlock<T, V>> it = _blocks.listIterator();
        ExecutionBlock<T, V> block = it.next();

        // The first element can be removed only if it is not dispatched
        if (!block._executionStarted) it.remove();

        while (it.hasNext())
        {
            block = it.next();
            if (block._callerType == callerType) it.remove();
        }

        _lock.unlock();
    }

    /**
     * Dispatches the next worker.
     */
    private void dispatchNextWorker()
    {
        _blocks.getFirst()._executionStarted = true;
        _blocks.getFirst()._workers.getFirst().execute();
    }

    /**
     * Called by the worker that completed its job to remove itself from the queue
     * and execute the next worker, if any. The method can only be executed by the current worker.
     * The method also can take into account whether the blocks are overdue.
     * Specifically, if the processing of the next block is to begin, the method checks if it is overdue.
     * and if it is not the last of its kind in the queue. If so, the whole block can be skipped.
     */
    protected void removeFirstWorkerAndExecuteNext()
    {
        _lock.lock();

        // clearing
        _blocks.getFirst()._workers.removeFirst();

        if (_blocks.getFirst()._workers.isEmpty()) // process the next block
        {
            // update distributions
            _blocksDistribution[_blocks.getFirst()._callerType][_blocks.getFirst()._blockType]--;
            _blocks.removeFirst(); // remove the empty block

            //check if the next block exists (otherwise do nothing)
            if (!_blocks.isEmpty())
            {
                // next dispatched task should be of that type (otherwise, it is not guaranteed that caller's job will ever be executed)
                int expectedCallerType = _blocks.getFirst()._callerType;

                // check overdue blocks and remove them
                Iterator<ExecutionBlock<T, V>> it = _blocks.iterator();
                ExecutionBlock<T, V> B;
                while (it.hasNext())
                {
                    B = it.next();
                    boolean overdue = B.isOverdue();
                    boolean isLast = _blocksDistribution[B._callerType][B._blockType] == 1;

                    if ((overdue) && (!isLast))
                    {
                        _blocksDistribution[B._callerType][B._blockType]--;
                        it.remove(); // remove and proceed to the next
                    }
                    else
                    {
                        moveCallersBlockFirst(expectedCallerType);
                        dispatchNextWorker();
                        break;
                    }
                }

            }
        }
        else
        {
            // the next worker in the block must be processed
            dispatchNextWorker();
        }

        _lock.unlock();
    }

    /**
     * Auxiliary method that shifts the first encountered block of a given caller type to the first position (next block to be dispatched)
     *
     * @param callerType requested caller type
     */
    private void moveCallersBlockFirst(int callerType)
    {
        Iterator<ExecutionBlock<T, V>> it = _blocks.iterator();
        ExecutionBlock<T, V> B = null;
        while (it.hasNext())
        {
            B = it.next();
            if (B._callerType == callerType)
            {
                it.remove();
                break;
            }
        }
        if (B != null) _blocks.addFirst(B);
    }


    /**
     * Cancels all swing workers and clears the queue.
     */
    public void dispose()
    {
        _lock.lock();
        if ((_blocks != null) && (!_blocks.isEmpty()))
        {
            for (ExecutionBlock<T, V> B : _blocks)
            {
                if (B._workers != null)
                    for (QueuedSwingWorker<T, V> w : B._workers) w.cancel(false);
                B._workers = null;
            }
        }
        _blocks.clear();
        _blocks = null;
        _lock.unlock();
        _lock = null;
    }

}
