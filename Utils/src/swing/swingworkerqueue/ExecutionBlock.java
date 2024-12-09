package swing.swingworkerqueue;

import java.util.LinkedList;

/**
 * Block of swing workers whose execution should not be interlaced with other block's executions.
 * A block can be considered overdue if its first worker, when, executed, is overdue and the block is not last of its
 * type in the queue. If so, the block can be removed. If, the block execution began, it cannot be terminated due
 * to the overdue condition.
 *
 * @author MTomczyk
 */


public class ExecutionBlock<T, V>
{
    /**
     * Queue worker type (should be provided).
     */
    protected volatile int _blockType;

    /**
     * Queue the called/creator type
     */
    protected volatile int _callerType;

    /**
     * Timestamp determining when the block (first worker) was registered).
     */
    protected volatile long _registeredTimestamp;

    /**
     * Delta time specifying overdue (when the worker can be considered overdue, i.e., can be skipped).
     * By default, it is set to infinite (no overdue/deadline).
     */
    protected volatile long _overdue = Long.MAX_VALUE;

    /**
     * Determines whether the worker can ever be considered overdue.
     */
    protected volatile boolean _considerOverdue = false;

    /**
     * List of workers to be executed.
     */
    protected volatile LinkedList<QueuedSwingWorker<T, V>> _workers;

    /**
     * If true, the block cannot be considered overdue.
     */
    protected volatile boolean _executionStarted = false;

    /**
     * Constructs a clone with modified block type and caller type.
     *
     * @param blockType  new block type
     * @param callerType new caller type
     * @return cloned execution block
     */
    public ExecutionBlock<T, V> getClone(int blockType, int callerType)
    {
        ExecutionBlock<T, V> block = new ExecutionBlock<>(blockType, callerType, _workers);
        block._registeredTimestamp = _registeredTimestamp;
        block._overdue = _overdue;
        block._considerOverdue = _considerOverdue;
        block._executionStarted = _executionStarted;
        return block;
    }


    /**
     * Parameterized constructor.
     *
     * @param blockType  block type
     * @param callerType caller type
     * @param worker     worker to be executed
     */
    public ExecutionBlock(int blockType, int callerType, QueuedSwingWorker<T, V> worker)
    {
        LinkedList<QueuedSwingWorker<T, V>> workers = new LinkedList<>();
        workers.add(worker);
        setData(blockType, callerType, workers);
    }

    /**
     * Parameterized constructor.
     *
     * @param blockType  block type
     * @param callerType caller type
     * @param workers    workers to be executed
     */
    public ExecutionBlock(int blockType, int callerType, LinkedList<QueuedSwingWorker<T, V>> workers)
    {
        setData(blockType, callerType, workers);
    }

    /**
     * Checks if the block is overdue (checks (1) the time condition and (2) the execution not yet begun condition).
     *
     * @return true -> can be considered overdue, false otherwise
     */
    public boolean isOverdue()
    {
        if (!_considerOverdue) return false;
        if (System.nanoTime() - _registeredTimestamp < _overdue) return false;
        return !_executionStarted;
    }

    /**
     * Data setter.
     *
     * @param blockType  block type
     * @param callerType caller type
     * @param workers    workers to be executed
     */
    private void setData(int blockType, int callerType, LinkedList<QueuedSwingWorker<T, V>> workers)
    {
        _blockType = blockType;
        _callerType = callerType;
        _workers = workers;
    }

    /**
     * Setter for the overdue threshold.
     *
     * @param overdue overdue threshold
     */
    public void setOverdue(long overdue)
    {
        _overdue = overdue;
    }

    /**
     * Setter for the ''consider overdue'' flag (determines whether the worker can ever be considered overdue).
     *
     * @param considerOverdue consider overdue flag
     */
    public void setConsiderOverdue(boolean considerOverdue)
    {
        _considerOverdue = considerOverdue;
    }
}
