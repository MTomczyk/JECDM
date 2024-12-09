package swing.swingworkerqueue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link QueueingSystem} class.
 *
 * @author MTomczyk
 */
class QueueingSystemTest
{

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        QueueingSystem<Void, Void> qs = new QueueingSystem<>(1, 5, 5);
        assertEquals(1, qs._queues.size());
        assertEquals(5, qs._callerIDtoQueue.length);
        assertEquals(0, qs._callerIDtoQueue[0]);
        assertEquals(0, qs._callerIDtoQueue[1]);
        assertEquals(0, qs._callerIDtoQueue[2]);
        assertEquals(0, qs._callerIDtoQueue[3]);
        assertEquals(0, qs._callerIDtoQueue[4]);
        assertEquals(5, qs._callerIDOffset.length);
        assertEquals(0, qs._callerIDOffset[0]);
        assertEquals(0, qs._callerIDOffset[1]);
        assertEquals(0, qs._callerIDOffset[2]);
        assertEquals(0, qs._callerIDOffset[3]);
        assertEquals(0, qs._callerIDOffset[4]);
        assertEquals(1, qs._queueCallers.length);
        assertEquals(5, qs._queueCallers[0]);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        QueueingSystem<Void, Void> qs = new QueueingSystem<>(2, 5, 5);
        assertEquals(2, qs._queues.size());
        assertEquals(5, qs._callerIDtoQueue.length);
        assertEquals(0, qs._callerIDtoQueue[0]);
        assertEquals(0, qs._callerIDtoQueue[1]);
        assertEquals(0, qs._callerIDtoQueue[2]);
        assertEquals(1, qs._callerIDtoQueue[3]);
        assertEquals(1, qs._callerIDtoQueue[4]);
        assertEquals(5, qs._callerIDOffset.length);
        assertEquals(0, qs._callerIDOffset[0]);
        assertEquals(0, qs._callerIDOffset[1]);
        assertEquals(0, qs._callerIDOffset[2]);
        assertEquals(3, qs._callerIDOffset[3]);
        assertEquals(3, qs._callerIDOffset[4]);
        assertEquals(2, qs._queueCallers.length);
        assertEquals(3, qs._queueCallers[0]);
        assertEquals(2, qs._queueCallers[1]);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        QueueingSystem<Void, Void> qs = new QueueingSystem<>(5, 5, 5);
        assertEquals(5, qs._queues.size());
        assertEquals(5, qs._callerIDtoQueue.length);
        assertEquals(0, qs._callerIDtoQueue[0]);
        assertEquals(1, qs._callerIDtoQueue[1]);
        assertEquals(2, qs._callerIDtoQueue[2]);
        assertEquals(3, qs._callerIDtoQueue[3]);
        assertEquals(4, qs._callerIDtoQueue[4]);
        assertEquals(5, qs._callerIDOffset.length);
        assertEquals(0, qs._callerIDOffset[0]);
        assertEquals(1, qs._callerIDOffset[1]);
        assertEquals(2, qs._callerIDOffset[2]);
        assertEquals(3, qs._callerIDOffset[3]);
        assertEquals(4, qs._callerIDOffset[4]);
        assertEquals(5, qs._queueCallers.length);
        assertEquals(1, qs._queueCallers[0]);
        assertEquals(1, qs._queueCallers[1]);
        assertEquals(1, qs._queueCallers[2]);
        assertEquals(1, qs._queueCallers[3]);
        assertEquals(1, qs._queueCallers[4]);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        QueueingSystem<Void, Void> qs = new QueueingSystem<>(5, 5, 1);
        assertEquals(1, qs._queues.size());
        assertEquals(1, qs._callerIDtoQueue.length);
        assertEquals(0, qs._callerIDtoQueue[0]);
        assertEquals(1, qs._callerIDOffset.length);
        assertEquals(0, qs._callerIDOffset[0]);
        assertEquals(1, qs._queueCallers.length);
        assertEquals(1, qs._queueCallers[0]);
    }
}