package thread;


import java.util.concurrent.CountDownLatch;

/**
 * Interface for classes constructing a series of {@link AbstractLatchArrayProcessor} instances.
 *
 * @author MTomczyk
 */

public interface LatchArrayProcessorFactory
{
    /**
     * Gets the runnable instance.
     * @param latch count down latch
     * @param begin begin index (inclusive) of the part of the data in the data array the thread is responsible to process.
     * @param end  end index (exclusive) of the part of the data in the data array the thread is responsible to process.
     * @param offset offset used when traversing through an array.
     * @return runnable instance
     */
    AbstractLatchArrayProcessor getInstance(CountDownLatch latch, int begin, int end, int offset);
}
