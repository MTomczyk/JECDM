package interaction.trigger.rules;

import dmcontext.DMContext;
import exeption.TriggerException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link TimeInterval}.
 *
 * @author MTomczyk
 */
class TimeIntervalTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        TimeInterval TI = new TimeInterval(3000, 5000);

        DMContext dmContext = new DMContext(null, null, null, null, false, 0);
        String msg = null;

        try
        {
            TI.shouldInteract(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The system starting timestamp is not provided", msg);

    }


    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        LocalDateTime startingTime = LocalDateTime.now();

        TimeInterval TI = new TimeInterval(3000, 5000);
        String msg = null;

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 0);
            assertFalse(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);


        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 1);
            assertFalse(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            Thread.sleep(3500);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 2);
            assertTrue(TI.shouldInteract(dmContext));
            //         TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 1);
            TI.shouldInteract(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The most recent timestamp is not stored (call notifyPreferenceElicitationEnds)", msg);
        msg = null;

        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 1);
            TI.notifyPreferenceElicitationEnds(dmContext);
        }

        try
        {
            Thread.sleep(4000);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 2);
            assertFalse(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            throw new RuntimeException(e);
        }
        assertNull(msg);

        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 2);
            assertTrue(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            throw new RuntimeException(e);
        }
        assertNull(msg);
    }


    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        LocalDateTime startingTime = LocalDateTime.now();

        TimeInterval TI = new TimeInterval(100, 2000);
        String msg = null;

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 0);
            assertFalse(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            Thread.sleep(1500);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 0);
            assertTrue(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            Thread.sleep(1500);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 0);
            assertFalse(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            Thread.sleep(1500);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null,
                    startingTime, null,
                    null, false, 0);
            assertTrue(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            Thread.sleep(1500);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null, startingTime, null, null, false, 0);
            assertFalse(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);


        try
        {
            Thread.sleep(1500);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null, startingTime, null, null, false, 0);
            assertTrue(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            Thread.sleep(3500);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMContext dmContext = new DMContext(null, startingTime, null, null, false, 0);
            assertTrue(TI.shouldInteract(dmContext));
            TI.notifyPreferenceElicitationEnds(dmContext);
        } catch (TriggerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }
}