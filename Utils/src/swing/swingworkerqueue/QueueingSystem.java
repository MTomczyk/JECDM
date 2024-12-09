package swing.swingworkerqueue;

import thread.Partitioner;

import java.util.ArrayList;

/**
 * This class wraps {@link SwingWorkerQueue}. It handles multiple queues and assumes that
 * one worker from each can be executed at the same time. The class maps potential caller
 * ids into queue number and, thus, handles processing. The more queues are in the system,
 * the greater the parallelization.
 *
 * @author MTomczyk
 */
public class QueueingSystem<T, V>
{
    /**
     * Represents the number of queues.
     * Each queue consumes one thread from swing worker thread pool, and it is assumed that
     * each caller is linked to just one queue (but one queue can have multiple linkages).
     * Therefore, using a value greater than one if just one caller does not make sense.
     * However, in the case of maintaining multiple callers, setting this number to a greater
     * value may introduce better parallelization. Still, however, the number should set
     * to some reasonable value, e.g., of four, as it is capped by (1) no. cores in the system,
     * (2) swing worker thread pool size of 10.
     */
    protected int _noQueues;

    /**
     * Queues that manage swing workers' execution. It ensures that their execution does not overlap,
     * i.e., workers are queued, and execution of next task begins after its predecessor finished its job
     * (see {@link swing.swingworkerqueue.ExecutionBlock}).
     */
    protected ArrayList<SwingWorkerQueue<T, V>> _queues;

    /**
     * Caller id to queue mapping.
     */
    protected int[] _callerIDtoQueue;

    /**
     * Transforms original caller number into in-queue caller number (subtracts the offset).
     */
    protected int[] _callerIDOffset;

    /**
     * No. unique caller IDs assigned to the queue.
     */
    protected int[] _queueCallers;

    /**
     * Parameterized constructor.
     *
     * @param noQueues    determines the total number of queues in the system
     * @param blockTypes  determines the total number of block types [0, B] in the system
     * @param callerTypes determines the total number of caller types [0, C] in the system
     */
    public QueueingSystem(int noQueues, int blockTypes, int callerTypes)
    {
        // generate partitions and the mapping
        int[] p = Partitioner.generatePartitions(callerTypes, noQueues);
        // if the divisibility is poor, the no queues may be set to a lesser value
        _noQueues = Partitioner.getValidNoThreads(p, noQueues);

        _callerIDtoQueue = new int[callerTypes];
        _callerIDOffset = new int[callerTypes];
        _queueCallers = new int[_noQueues];

        int cO = 0;
        for (int qID = 0; qID < _noQueues; qID++)
        {
            int left = p[2 * qID];
            int right = p[2 * qID + 1];
            assert left != -1;
            assert right != -1;
            for (int i = left; i < right; i++)
            {
                _callerIDtoQueue[i] = qID;
                _callerIDOffset[i] = cO;
            }
            _queueCallers[qID] = right - left;
            cO += _queueCallers[qID];
        }

        _queues = new ArrayList<>(_noQueues);
        for (int i = 0; i < _noQueues; i++)
            _queues.add(new SwingWorkerQueue<>(blockTypes, _queueCallers[i]));
    }

    /**
     * The method should be called from EDT (one thread).
     * Adds an execution block (packed workers) to the queue mapped to the caller id.
     *
     * @param block workers to be scheduled
     */
    public void addAndScheduleExecutionBlock(ExecutionBlock<T, V> block)
    {
        int callerType = block._callerType;
        int queue = _callerIDtoQueue[callerType];
        int offset = _callerIDOffset[callerType];
        _queues.get(queue).addAndScheduleExecutionBlock( // wrap and pass
                block.getClone(block._blockType, block._callerType - offset));
    }

    /**
     * Cancels all dispatched workers and clears the data.
     */
    public void dispose()
    {
        for (SwingWorkerQueue<T, V> q : _queues) q.dispose();
        _queues = null;
        _queueCallers = null;
        _callerIDtoQueue = null;
        _callerIDOffset = null;
    }
}
