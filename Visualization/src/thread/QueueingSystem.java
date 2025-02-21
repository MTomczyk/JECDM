package thread;

import swing.swingworkerqueue.ExecutionBlock;

import java.util.HashMap;

/**
 * A simple wrapper for {@link QueueingSystem}.
 *
 * @author MTomczyk
 */
public class QueueingSystem<T extends Enum<T>>
{
    /**
     * Queueing system that wraps queues that can manage swing workers' execution.
     * It ensures that their execution does not overlap, i.e., workers are queued and execution
     * of a next task begins after its predecessor finished its job.
     * (see {@link swing.swingworkerqueue.ExecutionBlock}).
     */
    private final swing.swingworkerqueue.QueueingSystem<Void, Void> _queueingSystem;

    /**
     * Translates execution block type into an integer.
     */
    private final HashMap<T, Integer> _blockToIntMap;

    /**
     * If true, ignore execution of those background tasks (execution blocks) that are overdue and not relevant for
     * the processing (recommended).
     */
    private final boolean _considerOverdueForExecutionBlocks;

    /**
     * Overdue time (in nanoseconds) for background tasks (see {@link ExecutionBlock}).
     */
    private final int _overdue;

    /**
     * Parameterized constructor
     *
     * @param values                            execution block types
     * @param noUpdatersQueues                  no. updaters queues
     * @param noCallerTypes                     no. potential callers (types)
     * @param considerOverdueForExecutionBlocks if true, ignore execution of those background tasks (execution blocks) that
     *                                          are overdue and not relevant for the processing (recommended)
     * @param overdue                           overdue time (in nanoseconds) for background tasks (see {@link ExecutionBlock})
     */
    public QueueingSystem(T[] values, int noUpdatersQueues, int noCallerTypes, boolean considerOverdueForExecutionBlocks, int overdue)
    {
        _blockToIntMap = new HashMap<>(values.length);
        for (int i = 0; i < values.length; i++) _blockToIntMap.put(values[i], i);
        _considerOverdueForExecutionBlocks = considerOverdueForExecutionBlocks;
        _overdue = overdue;
        _queueingSystem = new swing.swingworkerqueue.QueueingSystem<>(noUpdatersQueues, getNoBlockTypes(), noCallerTypes);
    }

    /**
     * Returns the number of execution block types registered in the system.
     *
     * @return the number of execution block types registered in the system
     */
    private int getNoBlockTypes()
    {
        if (_blockToIntMap == null) return 0;
        else return _blockToIntMap.size();
    }

    /**
     * Auxiliary method that returns execution block type ID given the input enum constant
     *
     * @param blockType execution block type
     * @return block type ID (-1 if it is not possible to derive the value from {@link QueueingSystem#_blockToIntMap})
     */
    public int getBlockTypeID(T blockType)
    {
        if (_blockToIntMap == null) return -1;
        if (!_blockToIntMap.containsKey(blockType)) return -1;
        return _blockToIntMap.get(blockType);
    }

    /**
     * Registers workers (block) for execution to the queue, which ensures that the background tasks do not overlap,
     * and they are executed in the order in which they were registered.
     *
     * @param executionBlock block of workers to be executed one by one
     */
    public void registerWorkers(ExecutionBlock<Void, Void> executionBlock)
    {
        if (executionBlock == null) return;
        executionBlock.setConsiderOverdue(_considerOverdueForExecutionBlocks);
        executionBlock.setOverdue(_overdue);
        if (_queueingSystem != null) _queueingSystem.addAndScheduleExecutionBlock(executionBlock);
    }


    /**
     * Auxiliary method that enables adding new execution blocks to a queue associated with a caller (upcoming tasks will be accepted).
     *
     * @param callerType caller type ID
     */
    public void enableAddingExecutionBlocksToQueue(int callerType)
    {
        _queueingSystem.enableAddingExecutionBlocksToQueue(callerType);
    }

    /**
     * Auxiliary method that waits until the first execution block is NOT of a given caller type.
     *
     * @param callerType caller type
     */
    public void waitUntilTheFirstBlockIsNotOfCallerType(int callerType)
    {
        _queueingSystem.waitUntilTheFirstBlockIsNotOfCallerType(callerType);
    }

    /**
     * Auxiliary method that disables adding new execution blocks to a queue associated with a caller (upcoming tasks will be rejected).
     *
     * @param callerType caller type ID
     */
    public void disableAddingExecutionBlocksToQueue(int callerType)
    {
        _queueingSystem.disableAddingExecutionBlocksToQueue(callerType);
    }

    /**
     * Auxiliary method that removes all execution blocks in the queue whose types match the input.
     * Such blocks are expected to be located in one queue, which is determined by the method first.
     *
     * @param callerType ID of callers of blocks to be removed
     */
    public void removeExecutionBlocksWithCallerType(int callerType)
    {
        _queueingSystem.removeExecutionBlocksWithCallerType(callerType);
    }

    /**
     * Auxiliary method that removes all execution blocks in the queue whose types match the input (both conditions must be met).
     * Such blocks are expected to be located in one queue, which is determined by the method first.
     *
     * @param callerType ID of callers of blocks to be removed
     * @param blockType  ID of blocks to be removed
     */
    public void removeExecutionBlocksWithCallerAndBlockType(int callerType, int blockType)
    {
        _queueingSystem.removeExecutionBlocksWithCallerAndBlockType(callerType, blockType);
    }

    /**
     * Auxiliary method for disposing the queue.
     */
    public void dispose()
    {
        _queueingSystem.dispose();
    }
}
