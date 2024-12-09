package scenario;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ScenariosGenerator} class.
 *
 * @author MTomczyk
 */
class ScenariosGeneratorTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        String msg = null;
        try
        {
            ScenariosGenerator.getScenarios(null, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The key-values are not specified (the array is null)", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        String msg = null;
        try
        {
            ScenariosGenerator.getScenarios(new KeyValues[]{}, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The key-values are not specified (the array is empty)", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        String msg = null;
        try
        {
            ScenariosGenerator.getScenarios(new KeyValues[1], null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("One of the provided key-values is not specified (is null)", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        String msg = null;
        try
        {
            ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstance("TEST1", "T1", new String[]{"1", "2", "3"}, null),
                    KeyValues.getInstance("TEST1", "T1", new String[]{"1", "2", "3"}, null)
            }, null, new int[1]);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The number of provided custom orders is of a different size than the key-values array length", msg);
    }


    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        String msg = null;
        try
        {
            ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstance("TEST1", "T1", new String[]{"1", "2", "3"}, null),
                    KeyValues.getInstance("TEST1", "T1", new String[]{"1", "2", "3"}, null)
            }, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The key = TEST1 is not unique", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        String msg = null;
        try
        {
            ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstance("TEST1", "T1", new String[]{"1", "2", "3"}, null),
                    KeyValues.getInstance("TEST2", "T1", new String[]{"1", "2", "3"}, null)
            }, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The key abbreviation = T1 is not unique", msg);
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        String msg = null;
        try
        {
            ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstance("TEST1!", "T1", new String[]{"1", "2", "3"}, null),
                    KeyValues.getInstance("TEST2", "T2", new String[]{"1", "2", "3"}, null)
            }, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The key = TEST1! contains forbidden characters", msg);
    }


    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        String msg = null;
        try
        {
            ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstance("TEST1", "T1!", new String[]{"1", "2", "3"}, null),
                    KeyValues.getInstance("TEST2", "T2", new String[]{"1", "2", "3"}, null)
            }, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The key abbreviation = T1! contains forbidden characters", msg);
    }

    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        String msg = null;
        try
        {
            ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstance("TEST1", "T1", new String[]{"1!", "2", "3"}, null),
                    KeyValues.getInstance("TEST2", "T2", new String[]{"1", "2", "3"}, null)
            }, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The value = 1! of the key = TEST1 contains forbidden characters", msg);
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        String msg = null;
        try
        {
            Set<Character> characters = new HashSet<>();
            characters.add('!');
            ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstance("TEST1", "T1", new String[]{"1!", "2", "3"}, null),
                    KeyValues.getInstance("TEST2", "T2", new String[]{"1", "2", "3"}, null)
            }, characters, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 11.
     */
    @Test
    void test11()
    {
        String msg = null;
        try
        {
            Scenarios scenarios = ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstance("TEST1", "T1", new String[]{"1", "2", "3"}, null),
                    KeyValues.getInstance("TEST2", "T2", new String[]{"a", "b", "c"}, null),
                    KeyValues.getInstance("TEST3", "T3", new String[]{"X", "Y", "Z"}, null)
            }, null, null);

            assertEquals(27, scenarios.getScenarios().length);
            String[] exp = new String[]{
                    "TEST1_1_TEST2_A_TEST3_X",
                    "TEST1_1_TEST2_A_TEST3_Y",
                    "TEST1_1_TEST2_A_TEST3_Z",
                    "TEST1_1_TEST2_B_TEST3_X",
                    "TEST1_1_TEST2_B_TEST3_Y",
                    "TEST1_1_TEST2_B_TEST3_Z",
                    "TEST1_1_TEST2_C_TEST3_X",
                    "TEST1_1_TEST2_C_TEST3_Y",
                    "TEST1_1_TEST2_C_TEST3_Z",

                    "TEST1_2_TEST2_A_TEST3_X",
                    "TEST1_2_TEST2_A_TEST3_Y",
                    "TEST1_2_TEST2_A_TEST3_Z",
                    "TEST1_2_TEST2_B_TEST3_X",
                    "TEST1_2_TEST2_B_TEST3_Y",
                    "TEST1_2_TEST2_B_TEST3_Z",
                    "TEST1_2_TEST2_C_TEST3_X",
                    "TEST1_2_TEST2_C_TEST3_Y",
                    "TEST1_2_TEST2_C_TEST3_Z",

                    "TEST1_3_TEST2_A_TEST3_X",
                    "TEST1_3_TEST2_A_TEST3_Y",
                    "TEST1_3_TEST2_A_TEST3_Z",
                    "TEST1_3_TEST2_B_TEST3_X",
                    "TEST1_3_TEST2_B_TEST3_Y",
                    "TEST1_3_TEST2_B_TEST3_Z",
                    "TEST1_3_TEST2_C_TEST3_X",
                    "TEST1_3_TEST2_C_TEST3_Y",
                    "TEST1_3_TEST2_C_TEST3_Z",
            };

            for (int i = 0; i < 27; i++)
            {
                assertEquals(exp[i], scenarios.getScenarios()[i].toString());
                assertFalse(scenarios.getScenarios()[i].isDisabled());
            }

        } catch (Exception e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
    }

    /**
     * Test 12.
     */
    @Test
    void test12()
    {
        String msg = null;
        try
        {
            Scenarios scenarios = ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstance("TEST1", "T1", new String[]{"1", "2", "3"}, new boolean[]{true, false, false}),
                    KeyValues.getInstance("TEST2", "T2", new String[]{"a", "b", "c"}, null),
                    KeyValues.getInstance("TEST3", "T3", new String[]{"X", "Y", "Z"}, new boolean[]{false, true, true})
            }, null, null);

            assertEquals(27, scenarios.getScenarios().length);
            String[] exp = new String[]{
                    "TEST1_1_TEST2_A_TEST3_X",
                    "TEST1_1_TEST2_A_TEST3_Y",
                    "TEST1_1_TEST2_A_TEST3_Z",
                    "TEST1_1_TEST2_B_TEST3_X",
                    "TEST1_1_TEST2_B_TEST3_Y",
                    "TEST1_1_TEST2_B_TEST3_Z",
                    "TEST1_1_TEST2_C_TEST3_X",
                    "TEST1_1_TEST2_C_TEST3_Y",
                    "TEST1_1_TEST2_C_TEST3_Z",

                    "TEST1_2_TEST2_A_TEST3_X",
                    "TEST1_2_TEST2_A_TEST3_Y",
                    "TEST1_2_TEST2_A_TEST3_Z",
                    "TEST1_2_TEST2_B_TEST3_X",
                    "TEST1_2_TEST2_B_TEST3_Y",
                    "TEST1_2_TEST2_B_TEST3_Z",
                    "TEST1_2_TEST2_C_TEST3_X",
                    "TEST1_2_TEST2_C_TEST3_Y",
                    "TEST1_2_TEST2_C_TEST3_Z",

                    "TEST1_3_TEST2_A_TEST3_X",
                    "TEST1_3_TEST2_A_TEST3_Y",
                    "TEST1_3_TEST2_A_TEST3_Z",
                    "TEST1_3_TEST2_B_TEST3_X",
                    "TEST1_3_TEST2_B_TEST3_Y",
                    "TEST1_3_TEST2_B_TEST3_Z",
                    "TEST1_3_TEST2_C_TEST3_X",
                    "TEST1_3_TEST2_C_TEST3_Y",
                    "TEST1_3_TEST2_C_TEST3_Z",
            };

            boolean[] expDisabled = new boolean[]{
                    true, true, true, true, true, true, true, true, true,
                    false, true, true, false, true, true, false, true, true,
                    false, true, true, false, true, true, false, true, true,
            };

            for (int i = 0; i < 27; i++)
            {
                assertEquals(exp[i], scenarios.getScenarios()[i].toString());
                assertEquals(expDisabled[i], scenarios.getScenarios()[i].isDisabled());
            }

        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }


    /**
     * Test 13.
     */
    @Test
    void test13()
    {
        String msg = null;
        try
        {
            Scenarios scenarios = ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstanceForObjectives(new String[]{"3", "4"}, null),
                    KeyValues.getInstanceForAlgorithms(new String[]{"NSGAII", "NSGAIII"}, null),
                    KeyValues.getInstanceForProblems(new String[]{"DTLZ2"}, null),
                    KeyValues.getInstance("TEST", "T", new String[]{"XYZ"}, null)
            }, null, null);

            assertEquals(4, scenarios.getScenarios().length);

            String[] exp = new String[]{
                    "PROBLEM_DTLZ2_OBJECTIVES_3_TEST_XYZ_ALGORITHM_NSGAII",
                    "PROBLEM_DTLZ2_OBJECTIVES_3_TEST_XYZ_ALGORITHM_NSGAIII",
                    "PROBLEM_DTLZ2_OBJECTIVES_4_TEST_XYZ_ALGORITHM_NSGAII",
                    "PROBLEM_DTLZ2_OBJECTIVES_4_TEST_XYZ_ALGORITHM_NSGAIII"
            };

            for (int i = 0; i < 4; i++)
            {
                assertEquals(exp[i], scenarios.getScenarios()[i].toString());
                assertFalse(scenarios.getScenarios()[i].isDisabled());
            }

            exp = new String[]{
                    "PRO_DTLZ2_OBJ_3_T_XYZ_ALG_NSGAII",
                    "PRO_DTLZ2_OBJ_3_T_XYZ_ALG_NSGAIII",
                    "PRO_DTLZ2_OBJ_4_T_XYZ_ALG_NSGAII",
                    "PRO_DTLZ2_OBJ_4_T_XYZ_ALG_NSGAIII"
            };

            for (int i = 0; i < 4; i++)
            {
                assertEquals(exp[i], scenarios.getScenarios()[i].getStringRepresentationAbbreviated());
                assertFalse(scenarios.getScenarios()[i].isDisabled());
            }


        } catch (Exception e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
    }


    /**
     * Test 14.
     */
    @Test
    void test14()
    {
        String msg = null;
        try
        {
            Scenarios scenarios = ScenariosGenerator.getScenarios(new KeyValues[]{
                    KeyValues.getInstanceForObjectives(new String[]{"3", "4"}, null),
                    KeyValues.getInstanceForAlgorithms(new String[]{"NSGAII", "NSGAIII"}, null),
                    KeyValues.getInstanceForProblems(new String[]{"DTLZ2"}, null),
                    KeyValues.getInstance("TEST", "T", new String[]{"XYZ"}, null)
            }, null, new int[]{3, 2, 1, 0});

            assertEquals(4, scenarios.getScenarios().length);

            String[] exp = new String[]{
                    "TEST_XYZ_PROBLEM_DTLZ2_ALGORITHM_NSGAII_OBJECTIVES_3",
                    "TEST_XYZ_PROBLEM_DTLZ2_ALGORITHM_NSGAII_OBJECTIVES_4",
                    "TEST_XYZ_PROBLEM_DTLZ2_ALGORITHM_NSGAIII_OBJECTIVES_3",
                    "TEST_XYZ_PROBLEM_DTLZ2_ALGORITHM_NSGAIII_OBJECTIVES_4",
            };

            for (int i = 0; i < 4; i++)
            {
                assertEquals(exp[i], scenarios.getScenarios()[i].toString());
                assertFalse(scenarios.getScenarios()[i].isDisabled());
            }

            exp = new String[]{
                    "T_XYZ_PRO_DTLZ2_ALG_NSGAII_OBJ_3",
                    "T_XYZ_PRO_DTLZ2_ALG_NSGAII_OBJ_4",
                    "T_XYZ_PRO_DTLZ2_ALG_NSGAIII_OBJ_3",
                    "T_XYZ_PRO_DTLZ2_ALG_NSGAIII_OBJ_4",
            };

            for (int i = 0; i < 4; i++)
            {
                assertEquals(exp[i], scenarios.getScenarios()[i].getStringRepresentationAbbreviated());
                assertFalse(scenarios.getScenarios()[i].isDisabled());
            }


        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

}