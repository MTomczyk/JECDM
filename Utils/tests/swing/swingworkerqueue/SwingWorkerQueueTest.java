package swing.swingworkerqueue;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests queueing the workers
 *
 * @author MTomczyk
 */
class SwingWorkerQueueTest
{
    /**
     * Auxiliary worker
     */
    public static class Worker extends QueuedSwingWorker<String, Object>
    {
        /**
         * Sleep time
         */
        private final int _sleep;

        /**
         * ID
         */
        private final int _id;

        /**
         * To be updated.
         */
        private final LinkedList<Integer> _finishedOrder;

        /**
         * Parameterized constructor
         *
         * @param id            worker id
         * @param sleep         sleep time
         * @param finishedOrder reference to the debugging list
         */
        public Worker(int id,int sleep, LinkedList<Integer> finishedOrder)
        {
            _id = id;
            _sleep = sleep;
            _finishedOrder = finishedOrder;
        }

        /**
         * To be overwritten.
         *
         * @return report on execution
         */
        @Override
        protected String doInBackground()
        {
            try
            {
                Thread.sleep(_sleep);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            _finishedOrder.add(_id);
            notifyTermination();

            return String.valueOf(_id);
        }
    }

    /**
     * Finished order
     */
    protected LinkedList<Integer> _finishedOrder;

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        int sleep = 1;
        int T = 50;
        _finishedOrder = new LinkedList<>();

        long pTime = System.currentTimeMillis();
        SwingWorkerQueue<String, Object> queue = new SwingWorkerQueue<>(1, 1);
        for (int i = 0; i < T; i++)
        {
            QueuedSwingWorker<String, Object> worker = new Worker(i, sleep, _finishedOrder);
            queue.addAndScheduleExecutionBlock(new ExecutionBlock<>(0, 0,worker));
        }
        System.out.println("Spawned in " + (System.currentTimeMillis() - pTime));

        try // wait till the background thread finishes
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("==========================================");
        for (Integer i : _finishedOrder)
            System.out.println(i);

        for (int i = 0; i < T; i++) assertEquals(i, _finishedOrder.get(i));
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        int sleep = 100;
        int T = 10;
        _finishedOrder = new LinkedList<>();

        long pTime = System.currentTimeMillis();
        SwingWorkerQueue<String, Object> queue = new SwingWorkerQueue<>(1, 1);
        for (int i = 0; i < T; i++)
        {
            QueuedSwingWorker<String, Object> worker = new Worker(i,  sleep, _finishedOrder);
            ExecutionBlock<String, Object> B = new ExecutionBlock<>(0, 0, worker);
            B.setConsiderOverdue(true);
            B.setOverdue(1000000000);
            queue.addAndScheduleExecutionBlock(B);
        }
        System.out.println("Spawned in " + (System.currentTimeMillis() - pTime));

        try // wait till the background thread finishes
        {
            Thread.sleep(10000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("==========================================");
        for (Integer i : _finishedOrder)
            System.out.println(i);

        for (int i = 0; i < T; i++) assertEquals(i, _finishedOrder.get(i));
    }


    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        int sleep = 100;
        int T = 10;
        _finishedOrder = new LinkedList<>();

        long pTime = System.currentTimeMillis();
        SwingWorkerQueue<String, Object> queue = new SwingWorkerQueue<>(1, 1);
        for (int i = 0; i < T; i++)
        {
            QueuedSwingWorker<String, Object> worker = new Worker(i,  sleep, _finishedOrder);
            ExecutionBlock<String, Object> B = new ExecutionBlock<>(0, 0, worker);
            B.setConsiderOverdue(true);
            B.setOverdue(0); // almost all should be skipped
            queue.addAndScheduleExecutionBlock(B);
        }
        System.out.println("Spawned in " + (System.currentTimeMillis() - pTime));

        try // wait till the background thread finishes
        {
            Thread.sleep(4000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("==========================================");
        for (Integer i : _finishedOrder)
            System.out.println(i);

        assertEquals(2, _finishedOrder.size());
        assertEquals(0, _finishedOrder.get(0));
        assertEquals(9, _finishedOrder.get(1));
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        int sleep = 100;
        int T = 10;
        _finishedOrder = new LinkedList<>();

        long pTime = System.currentTimeMillis();
        SwingWorkerQueue<String, Object> queue = new SwingWorkerQueue<>(2, 1);
        for (int i = 0; i < T; i++)
        {
            QueuedSwingWorker<String, Object> worker = new Worker(i,  sleep, _finishedOrder);
            ExecutionBlock<String, Object> B = new ExecutionBlock<>(i % 2, 0, worker);
            B.setConsiderOverdue(true);
            B.setOverdue(0); // almost all should be skipped
            queue.addAndScheduleExecutionBlock(B);
        }
        System.out.println("Spawned in " + (System.currentTimeMillis() - pTime));

        try // wait till the background thread finishes
        {
            Thread.sleep(4000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("==========================================");
        for (Integer i : _finishedOrder)
            System.out.println(i);

        assertEquals(3, _finishedOrder.size());
        assertEquals(0, _finishedOrder.get(0));
        assertEquals(8, _finishedOrder.get(1));
        assertEquals(9, _finishedOrder.get(2));
    }
}