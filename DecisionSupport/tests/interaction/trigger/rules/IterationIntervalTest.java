package interaction.trigger.rules;

import dmcontext.DMContext;
import exeption.TriggerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link IterationInterval}.
 *
 * @author MTomczyk
 */
class IterationIntervalTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        IterationInterval II = new IterationInterval(1);
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertTrue(II.shouldInteract(dmContext));
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

        }
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        IterationInterval II = new IterationInterval(2);
        boolean[] exp = new boolean[]{true, false, true, false, true, false, true, false, true, false};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        IterationInterval II = new IterationInterval(3);
        boolean[] exp = new boolean[]{true, false, false, true, false, false, true, false, false, true};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        IterationInterval II = new IterationInterval(1, 1);
        boolean[] exp = new boolean[]{false, true, true, true, true, true, true, true, true, true};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());
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
        IterationInterval II = new IterationInterval(1, 2);
        boolean[] exp = new boolean[]{false, true, false, true, false, true, false, true, false, true};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        IterationInterval II = new IterationInterval(1, 3);
        boolean[] exp = new boolean[]{false, true, false, false, true, false, false, true, false, false};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        IterationInterval II = new IterationInterval(2, 1);
        boolean[] exp = new boolean[]{false, false, true, true, true, true, true, true, true, true};
        int acc = 0;

        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        IterationInterval II = new IterationInterval(2, 2);
        boolean[] exp = new boolean[]{false, false, true, false, true, false, true, false, true, false};
        int acc = 0;

        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 9
     */
    @Test
    void test9()
    {
        IterationInterval II = new IterationInterval(2, 3);
        boolean[] exp = new boolean[]{false, false, true, false, false, true, false, false, true, false};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        IterationInterval II = new IterationInterval(5, 4);
        boolean[] exp = new boolean[]{false, false, false, false, false, true, false, false, false, true};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 11.
     */
    @SuppressWarnings("ConstantValue")
    @Test
    void test11()
    {
        IterationInterval II = new IterationInterval(1);
        boolean[] exp = new boolean[]{true, true, true, true, true, true, true, true, true, true};
        boolean[] postpone = new boolean[]{true, true, true, true, true, true, true, true, true, true};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (postpone[i]) II.postpone(null);
                if ((exp[i]) && (!postpone[i]))
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());
            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 12.
     */
    @Test
    void test12()
    {
        IterationInterval II = new IterationInterval(2);
        //boolean[] exp = new boolean[]{true, false, true, false, true, false, true, false, true, false};
        boolean[] exp = new boolean[]{true, true, true, true, true, true, true, false, true, true};
        boolean[] postpone = new boolean[]{true, true, true, false, false, false, false, false, true, false};
        int acc = 0;

        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (postpone[i]) II.postpone(null);
                if ((exp[i]) && (!postpone[i]))
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 13.
     */
    @Test
    void test13()
    {
        IterationInterval II = new IterationInterval(2, 3);
        //boolean[] exp = new boolean[]{false, false, true, false, false, true, false, false, true, false};
        boolean[] exp = new boolean[]{false, false, true, false, false, true, true, true, true, false};
        boolean[] postpone = new boolean[]{false, false, false, false, false, true, true, false, false, false};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (postpone[i]) II.postpone(null);
                if ((exp[i]) && (!postpone[i]))
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 14.
     */
    @Test
    void test14()
    {
        IterationInterval II = new IterationInterval(2, 3, 2);
        //boolean[] exp = new boolean[]{false, false, true, false, false, true, false, false, true, false};
        boolean[] exp = new boolean[]{false, false, true, false, false, true, true, true, false, false};
        boolean[] postpone = new boolean[]{false, false, false, false, false, true, true, false, false, false};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (postpone[i]) II.postpone(null);
                if ((exp[i]) && (!postpone[i]))
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    if (acc < 2) acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 15.
     */
    @Test
    void test15()
    {
        IterationInterval II = new IterationInterval(2, 3, 1);
        //boolean[] exp = new boolean[]{false, false, true, false, false, true, false, false, true, false};
        boolean[] exp = new boolean[]{false, false, true, false, false, false, false, false, false, false};
        boolean[] postpone = new boolean[]{false, false, false, false, false, true, true, false, false, false};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (postpone[i]) II.postpone(null);
                if ((exp[i]) && (!postpone[i]))
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    II.notifyPreferenceElicitationEnds(dmContext);
                    if (acc < 1) acc++;
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 16.
     */
    @Test
    void test16()
    {
        IterationInterval II = new IterationInterval(1, 2, 3);
        boolean[] exp = new boolean[]{false, true, false, true, false, true, false, true, false, false};
        int acc = 0;
        for (int i = 0; i < 10; i++)
        {
            String msg = null;
            try
            {
                DMContext dmContext = new DMContext(null, null, null, null, false, i);
                assertEquals(exp[i], II.shouldInteract(dmContext));
                if (exp[i])
                {
                    II.notifyPreferenceElicitationBegins(dmContext);
                    if (i == 3) II.notifyPreferenceElicitationFailed(dmContext);
                    else acc++;
                    II.notifyPreferenceElicitationEnds(dmContext);
                }
                assertEquals(acc, II.getNumberOfSuccessfullyConductedInteractions());

            } catch (TriggerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

}