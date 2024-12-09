package interaction.trigger;

import dmcontext.DMContext;
import exeption.TriggerException;
import interaction.trigger.rules.Flag;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.IterationInterval;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link InteractionTrigger}.
 *
 * @author MTomczyk
 */
class InteractionTriggerTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        InteractionTrigger IT = new InteractionTrigger((IRule) null);
        String msg = null;
        try
        {
            IT.validate();
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("No rules are provided (the array is null)", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        InteractionTrigger IT = new InteractionTrigger(new LinkedList<>());
        String msg = null;
        try
        {
            IT.validate();
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("No rules are provided (the array is empty)", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        LinkedList<IRule> rules = new LinkedList<>();
        rules.add(null);
        InteractionTrigger IT = new InteractionTrigger(rules);
        String msg = null;
        try
        {
            IT.validate();
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("One of the provided rules is null", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        IterationInterval II1 = new IterationInterval(2);
        IterationInterval II2 = new IterationInterval(1, 2);
        LinkedList<IRule> rules = new LinkedList<>();
        rules.add(II1);
        rules.add(II2);
        InteractionTrigger IT = new InteractionTrigger(rules);

        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                Result result = IT.shouldTriggerTheInteractions(dmContext);
                assertTrue(result._shouldInteract);
                assertEquals(1, result._callers.size());
                if (i % 2 == 0) assertEquals("ITERATION_INTERVAL_0_2", result._callers.get(0));
                else assertEquals("ITERATION_INTERVAL_1_2", result._callers.get(0));
                assertEquals(i, result._iteration);
                assertTrue(result._processingTime >= 0);
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        LinkedList<IRule> rules = new LinkedList<>();
        rules.add(new IterationInterval(2));
        rules.add(new Flag(true));
        InteractionTrigger IT = new InteractionTrigger(rules);

        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                Result result = IT.shouldTriggerTheInteractions(dmContext);
                assertTrue(result._shouldInteract);
                if (i % 2 == 0)
                {
                    assertEquals(2, result._callers.size());
                    assertEquals("ITERATION_INTERVAL_0_2", result._callers.get(0));
                    assertEquals("FLAG_TRUE", result._callers.get(1));
                }
                else
                {
                    assertEquals(1, result._callers.size());
                    assertEquals("FLAG_TRUE", result._callers.get(0));
                }

                assertEquals(i, result._iteration);
                assertTrue(result._processingTime >= 0);
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }
}