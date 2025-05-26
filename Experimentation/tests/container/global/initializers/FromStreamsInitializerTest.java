package container.global.initializers;

import exception.GlobalException;
import exception.ScenarioException;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.L32_X64_MIX;
import random.MersenneTwister32;
import random.XoRoShiRo128PP;
import scenario.KeyValue;
import scenario.Scenario;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link FromStreamsInitializer}.
 *
 * @author MTomczyk
 */
class FromStreamsInitializerTest
{
    /**
     * Performs various validation tests.
     */
    @Test
    void test1()
    {
        {
            FromStreamsInitializer fsi = new FromStreamsInitializer(
                    FromStreamsInitializer.Mode.JUMPABLE,
                    FromStreamsInitializer.InitializationStage.GLOBAL,
                    () -> null);

            String msg = null;
            try
            {
                fsi.requestStreamsCreationDuringSDCInit(new Scenario(new KeyValue[]{}, 0), 1);
            } catch (ScenarioException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            try
            {
                fsi.requestStreamsCreationDuringGDCInit(1, 1);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNotNull(msg);
            assertEquals("The reference RNG is not supplied", msg);
        }
        {
            FromStreamsInitializer fsi = new FromStreamsInitializer(
                    FromStreamsInitializer.Mode.JUMPABLE,
                    FromStreamsInitializer.InitializationStage.GLOBAL,
                    () -> new MersenneTwister32(0));

            String msg = null;
            try
            {
                fsi.requestStreamsCreationDuringSDCInit(new Scenario(new KeyValue[]{}, 0), 1);
            } catch (ScenarioException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            try
            {
                fsi.requestStreamsCreationDuringGDCInit(1, 1);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNotNull(msg);
            assertEquals("The reference RNG is not jumpable (MT)", msg);
        }
        {
            FromStreamsInitializer fsi = new FromStreamsInitializer(
                    FromStreamsInitializer.Mode.SPLITTABLE,
                    FromStreamsInitializer.InitializationStage.GLOBAL,
                    () -> new MersenneTwister32(0));

            String msg = null;
            try
            {
                fsi.requestStreamsCreationDuringSDCInit(new Scenario(new KeyValue[]{}, 0), 1);
            } catch (ScenarioException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            try
            {
                fsi.requestStreamsCreationDuringGDCInit(1, 1);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNotNull(msg);
            assertEquals("The reference RNG is not splittable (MT)", msg);

            try
            {
                fsi.getRNG(new Scenario(new KeyValue[]{}, 0), 0, 1);
            } catch (ScenarioException e)
            {
                msg = e.getMessage();
            }
            assertNotNull(msg);
            assertEquals("RNGs are not available (the array is null or empty)", msg);
        }
    }

    /**
     * Performs various validation tests.
     */
    @Test
    void test2()
    {
        {
            FromStreamsInitializer fsi = new FromStreamsInitializer(
                    FromStreamsInitializer.Mode.SPLITTABLE,
                    FromStreamsInitializer.InitializationStage.GLOBAL,
                    () -> new XoRoShiRo128PP(0));

            String msg = null;
            try
            {
                fsi.requestStreamsCreationDuringSDCInit(new Scenario(new KeyValue[]{}, 0), 1);
            } catch (ScenarioException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            try
            {
                fsi.requestStreamsCreationDuringGDCInit(1, 1);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNotNull(msg);
            assertEquals("The reference RNG is not splittable (XO_RO_SHI_RO_128_PP)", msg);
        }
        {
            FromStreamsInitializer fsi = new FromStreamsInitializer(
                    FromStreamsInitializer.Mode.JUMPABLE,
                    FromStreamsInitializer.InitializationStage.GLOBAL,
                    () -> new XoRoShiRo128PP(0));

            String msg = null;
            try
            {
                fsi.requestStreamsCreationDuringSDCInit(new Scenario(new KeyValue[]{}, 0), 1);
            } catch (ScenarioException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            try
            {
                fsi.requestStreamsCreationDuringGDCInit(5, 5);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);


            assertEquals(25, fsi._streams.size());

            for (int s = 0; s < 5; s++)
            {
                Scenario scenario = new Scenario(new KeyValue[]{}, s);
                for (int t = 0; t < 5; t++)
                {
                    IRandom R = null;
                    try
                    {
                        R = fsi.getRNG(scenario, t, 5);
                    } catch (ScenarioException e)
                    {
                        msg = e.getMessage();
                    }
                    assertNull(msg);
                    assertNotNull(R);
                    assertInstanceOf(XoRoShiRo128PP.class, R);
                }
            }

            IRandom R = null;
            try
            {
                R = fsi.getRNG(new Scenario(new KeyValue[]{}, 6), 6, 5);
            } catch (ScenarioException e)
            {
                msg = e.getMessage();
            }
            assertNull(R);
            assertNotNull(msg);
            assertEquals("There is not enough streams (available = 25; requested index = 36)", msg);
        }
        {
            FromStreamsInitializer fsi = new FromStreamsInitializer(
                    FromStreamsInitializer.Mode.JUMPABLE,
                    FromStreamsInitializer.InitializationStage.SCENARIO,
                    () -> new XoRoShiRo128PP(0));

            String msg = null;
            try
            {
                fsi.requestStreamsCreationDuringGDCInit(5, 5);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            for (int s = 0; s < 5; s++)
            {
                Scenario scenario = new Scenario(new KeyValue[]{}, s);
                msg = null;
                try
                {
                    fsi.requestStreamsCreationDuringSDCInit(scenario, 5);
                } catch (ScenarioException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);

                assertEquals(5, fsi._streams.size());

                for (int t = 0; t < 5; t++)
                {
                    IRandom R = null;
                    try
                    {
                        R = fsi.getRNG(scenario, t, 5);
                    } catch (ScenarioException e)
                    {
                        msg = e.getMessage();
                    }
                    assertNull(msg);
                    assertNotNull(R);
                    assertInstanceOf(XoRoShiRo128PP.class, R);
                }

                IRandom R = null;
                try
                {
                    R = fsi.getRNG(scenario, 5, 5);
                } catch (ScenarioException e)
                {
                    msg = e.getMessage();
                }
                assertNull(R);
                assertNotNull(msg);
                assertEquals("There is not enough streams (available = 5; requested index = 5)", msg);
            }
        }
    }

}