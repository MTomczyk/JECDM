package interaction.trigger.rules;

import exeption.TriggerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for
 *
 * @author MTomczyk
 */
class FlagTest
{
    /**
     * Test 1.
     */
    @Test
    void shouldInteract()
    {
        {
            Flag flag = new Flag();
            String msg = null;
            try
            {
                assertFalse(flag.shouldInteract(null));
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            try
            {
                assertFalse(flag.shouldInteract(null));
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            flag.setFlag(true);

            try
            {
                assertTrue(flag.shouldInteract(null));
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            flag.setFlag(false);

            try
            {
                assertFalse(flag.shouldInteract(null));
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            assertEquals("FLAG", flag.toString());
        }

        {
            Flag flag = new Flag(true);
            String msg = null;
            try
            {
                assertTrue(flag.shouldInteract(null));
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            flag.setFlag(false);
            try
            {
                assertFalse(flag.shouldInteract(null));
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }
}