package thread;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Auxiliary class providing various static methods for executing implementations of {@link AbstractLatchArrayProcessor}.
 *
 * @author MTomczyk
 */

public class LatchArrayProcessorRunner
{
    /**
     * Executes instances of {@link AbstractLatchArrayProcessor} and waits until they are done.
     *
     * @param noThreads upper limit (expected) for the number of threads
     * @param dataLen   length of the array-like data
     * @param factory   object generating a series of {@link AbstractLatchArrayProcessor} instances
     * @return list of constructed runnables
     */
    public static LinkedList<AbstractLatchArrayProcessor> runAndWait(int noThreads, int dataLen, LatchArrayProcessorFactory factory)
    {
        // SPLIT COMPUTATION INTO THREADS
        int[] p = Partitioner.generatePartitions(dataLen, noThreads);
        int threads = Partitioner.getValidNoThreads(p, noThreads);

        CountDownLatch latch = new CountDownLatch(threads);
        Executor e = Executors.newFixedThreadPool(threads);
        LinkedList<AbstractLatchArrayProcessor> dataProcessors = new LinkedList<>();

        for (int i = 0; i < threads; i++) dataProcessors.add(factory.getInstance(latch, p[2 * i], p[2 * i + 1], 1));
        for (AbstractLatchArrayProcessor DP : dataProcessors) e.execute(DP);

        try
        {
            latch.await();
        } catch (InterruptedException ex)
        {
            throw new RuntimeException(ex);
        }

        return dataProcessors;
    }
}
