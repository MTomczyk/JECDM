package scenario;

import condition.ScenarioDisablingConditions;
import exception.GlobalException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link CrossedScenariosGenerator}.
 *
 * @author MTomczyk
 */
class CrossedScenariosGeneratorTest
{

    /**
     * Test 1.
     */
    @Test
    void generateCrossedScenarios1()
    {
        String[] keys = new String[]{"K1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "c"},
                {"1", "2", "3"},
                {"x", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];
        Scenarios scenarios = null;

        String msg = null;

        try
        {
            for (int i = 0; i < 3; i++) kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        CrossedSetting crossedSetting = new CrossedSetting(new String[]{"k1", "K2"},
                new String[][]{new String[]{"A", "b", "C"},
                        new String[]{"1", "2", "3"}}, null);
        try
        {
            crossedSetting.instantiateSetting(scenarios);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(crossedSetting);

        CrossedScenariosGenerator generator = new CrossedScenariosGenerator();
        CrossedScenarios[] CS = null;
        try
        {
            CS = generator.generateCrossedScenarios(scenarios, new CrossedSetting[]{crossedSetting});
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(CS);

        assertEquals(3, CS.length);

        for (int i = 0; i < 3; i++)
        {
            assertEquals(1, CS[i].getFixedKeyValue().length);
            assertEquals(2, CS[i].getLevel());
            assertEquals(2, CS[i].getComparedKeyValues().length);
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().toString());
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().getAbbreviation());
            assertEquals(3, CS[i].getComparedKeyValues()[0].getValues().length);
            assertEquals("A", CS[i].getComparedKeyValues()[0].getValues()[0].getValue());
            assertEquals("B", CS[i].getComparedKeyValues()[0].getValues()[1].getValue());
            assertEquals("C", CS[i].getComparedKeyValues()[0].getValues()[2].getValue());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().toString());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().getAbbreviation());
            assertEquals(3, CS[i].getComparedKeyValues()[1].getValues().length);
            assertEquals("1", CS[i].getComparedKeyValues()[1].getValues()[0].getValue());
            assertEquals("2", CS[i].getComparedKeyValues()[1].getValues()[1].getValue());
            assertEquals("3", CS[i].getComparedKeyValues()[1].getValues()[2].getValue());
        }

        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("X", CS[0].getFixedKeyValue()[0].getValue().getValue());

        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Y", CS[1].getFixedKeyValue()[0].getValue().getValue());

        assertEquals("K3", CS[2].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[2].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Z", CS[2].getFixedKeyValue()[0].getValue().getValue());

        String[][] refScenarios = new String[][]
                {
                        {"K1_A_K2_1_K3_X", "K1_A_K2_2_K3_X", "K1_A_K2_3_K3_X",
                                "K1_B_K2_1_K3_X", "K1_B_K2_2_K3_X", "K1_B_K2_3_K3_X",
                                "K1_C_K2_1_K3_X", "K1_C_K2_2_K3_X", "K1_C_K2_3_K3_X",
                        },
                        {"K1_A_K2_1_K3_Y", "K1_A_K2_2_K3_Y", "K1_A_K2_3_K3_Y",
                                "K1_B_K2_1_K3_Y", "K1_B_K2_2_K3_Y", "K1_B_K2_3_K3_Y",
                                "K1_C_K2_1_K3_Y", "K1_C_K2_2_K3_Y", "K1_C_K2_3_K3_Y",
                        },
                        {"K1_A_K2_1_K3_Z", "K1_A_K2_2_K3_Z", "K1_A_K2_3_K3_Z",
                                "K1_B_K2_1_K3_Z", "K1_B_K2_2_K3_Z", "K1_B_K2_3_K3_Z",
                                "K1_C_K2_1_K3_Z", "K1_C_K2_2_K3_Z", "K1_C_K2_3_K3_Z",
                        },
                };

        for (int i = 0; i < 3; i++)
        {

            Set<String> expected = new HashSet<>();
            Collections.addAll(expected, refScenarios[i]);
            for (Scenario s : CS[i].getReferenceScenarios())
                assertTrue(expected.contains(s.toString()));
            assertEquals(refScenarios[i].length, CS[i].getReferenceScenarios().length);
        }

        assertEquals("FIXED_K3_X_COMPARED_K1_K2", CS[0].getStringRepresentation());
        assertEquals("FIXED_K3_Y_COMPARED_K1_K2", CS[1].getStringRepresentation());
        assertEquals("FIXED_K3_Z_COMPARED_K1_K2", CS[2].getStringRepresentation());
    }

    /**
     * Test 2.
     */
    @Test
    void generateCrossedScenarios2()
    {
        String[] keys = new String[]{"K1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "c"},
                {"1", "2", "3"},
                {"x", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];
        Scenarios scenarios = null;

        String msg = null;

        try
        {
            for (int i = 0; i < 3; i++) kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        CrossedSetting crossedSetting = new CrossedSetting(new String[]{"k1", "K2"},
                new String[][]{new String[]{"A", "c"},
                        new String[]{"1", "2"}}, null);
        try
        {
            crossedSetting.instantiateSetting(scenarios);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(crossedSetting);

        CrossedScenariosGenerator generator = new CrossedScenariosGenerator();
        CrossedScenarios[] CS = null;
        try
        {
            CS = generator.generateCrossedScenarios(scenarios, new CrossedSetting[]{crossedSetting});
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(CS);

        assertEquals(3, CS.length);

        for (int i = 0; i < 3; i++)
        {
            assertEquals(1, CS[i].getFixedKeyValue().length);
            assertEquals(2, CS[i].getLevel());
            assertEquals(2, CS[i].getComparedKeyValues().length);
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().toString());
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[0].getValues().length);
            assertEquals("A", CS[i].getComparedKeyValues()[0].getValues()[0].getValue());
            assertEquals("C", CS[i].getComparedKeyValues()[0].getValues()[1].getValue());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().toString());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[1].getValues().length);
            assertEquals("1", CS[i].getComparedKeyValues()[1].getValues()[0].getValue());
            assertEquals("2", CS[i].getComparedKeyValues()[1].getValues()[1].getValue());
        }

        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("X", CS[0].getFixedKeyValue()[0].getValue().getValue());

        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Y", CS[1].getFixedKeyValue()[0].getValue().getValue());

        assertEquals("K3", CS[2].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[2].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Z", CS[2].getFixedKeyValue()[0].getValue().getValue());

        String[][] refScenarios = new String[][]
                {
                        {"K1_A_K2_1_K3_X", "K1_A_K2_2_K3_X",
                                "K1_C_K2_1_K3_X", "K1_C_K2_2_K3_X",
                        },
                        {"K1_A_K2_1_K3_Y", "K1_A_K2_2_K3_Y",
                                "K1_C_K2_1_K3_Y", "K1_C_K2_2_K3_Y",
                        },
                        {"K1_A_K2_1_K3_Z", "K1_A_K2_2_K3_Z",
                                "K1_C_K2_1_K3_Z", "K1_C_K2_2_K3_Z",
                        },
                };

        for (int i = 0; i < 3; i++)
        {
            Set<String> expected = new HashSet<>();
            Collections.addAll(expected, refScenarios[i]);
            for (Scenario s : CS[i].getReferenceScenarios())
                assertTrue(expected.contains(s.toString()));
            assertEquals(refScenarios[i].length, CS[i].getReferenceScenarios().length);
        }

        assertEquals("FIXED_K3_X_COMPARED_K1_K2", CS[0].getStringRepresentation());
        assertEquals("FIXED_K3_Y_COMPARED_K1_K2", CS[1].getStringRepresentation());
        assertEquals("FIXED_K3_Z_COMPARED_K1_K2", CS[2].getStringRepresentation());
    }


    /**
     * Test 3.
     */
    @Test
    void generateCrossedScenarios3()
    {
        String[] keys = new String[]{"K1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "c"},
                {"1", "2", "3"},
                {"x", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];
        Scenarios scenarios = null;

        String msg = null;

        try
        {
            for (int i = 0; i < 3; i++) kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        CrossedSetting crossedSetting1 = new CrossedSetting(new String[]{"k1", "K2"},
                new String[][]{new String[]{"A", "c"},
                        new String[]{"1", "2"}}, null);
        CrossedSetting crossedSetting2 = new CrossedSetting(new String[]{"k1", "K2"},
                new String[][]{new String[]{"A", "c"},
                        new String[]{"1", "2"}}, null);
        try
        {
            crossedSetting1.instantiateSetting(scenarios);
            crossedSetting2.instantiateSetting(scenarios);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(crossedSetting1);
        assertNotNull(crossedSetting2);

        CrossedScenariosGenerator generator = new CrossedScenariosGenerator();
        CrossedScenarios[] CS = null;
        try
        {
            CS = generator.generateCrossedScenarios(scenarios, new CrossedSetting[]{crossedSetting1, crossedSetting2});
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(CS);

        assertEquals(3, CS.length);

        for (int i = 0; i < 3; i++)
        {
            assertEquals(1, CS[i].getFixedKeyValue().length);
            assertEquals(2, CS[i].getLevel());
            assertEquals(2, CS[i].getComparedKeyValues().length);
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().toString());
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[0].getValues().length);
            assertEquals("A", CS[i].getComparedKeyValues()[0].getValues()[0].getValue());
            assertEquals("C", CS[i].getComparedKeyValues()[0].getValues()[1].getValue());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().toString());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[1].getValues().length);
            assertEquals("1", CS[i].getComparedKeyValues()[1].getValues()[0].getValue());
            assertEquals("2", CS[i].getComparedKeyValues()[1].getValues()[1].getValue());
        }

        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("X", CS[0].getFixedKeyValue()[0].getValue().getValue());

        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Y", CS[1].getFixedKeyValue()[0].getValue().getValue());

        assertEquals("K3", CS[2].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[2].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Z", CS[2].getFixedKeyValue()[0].getValue().getValue());

        String[][] refScenarios = new String[][]
                {
                        {"K1_A_K2_1_K3_X", "K1_A_K2_2_K3_X",
                                "K1_C_K2_1_K3_X", "K1_C_K2_2_K3_X",
                        },
                        {"K1_A_K2_1_K3_Y", "K1_A_K2_2_K3_Y",
                                "K1_C_K2_1_K3_Y", "K1_C_K2_2_K3_Y",
                        },
                        {"K1_A_K2_1_K3_Z", "K1_A_K2_2_K3_Z",
                                "K1_C_K2_1_K3_Z", "K1_C_K2_2_K3_Z",
                        },
                };

        for (int i = 0; i < 3; i++)
        {
            Set<String> expected = new HashSet<>();
            Collections.addAll(expected, refScenarios[i]);
            for (Scenario s : CS[i].getReferenceScenarios())
                assertTrue(expected.contains(s.toString()));
            assertEquals(refScenarios[i].length, CS[i].getReferenceScenarios().length);
        }

        assertEquals("FIXED_K3_X_COMPARED_K1_K2", CS[0].getStringRepresentation());
        assertEquals("FIXED_K3_Y_COMPARED_K1_K2", CS[1].getStringRepresentation());
        assertEquals("FIXED_K3_Z_COMPARED_K1_K2", CS[2].getStringRepresentation());
    }


    /**
     * Test 4.
     */
    @Test
    void generateCrossedScenarios4()
    {
        String[] keys = new String[]{"K1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "c"},
                {"1", "2", "3"},
                {"x", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];
        Scenarios scenarios = null;

        String msg = null;

        try
        {
            for (int i = 0; i < 3; i++) kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        CrossedSetting crossedSetting1 = new CrossedSetting(new String[]{"k1", "K2"},
                new String[][]{new String[]{"A", "c"},
                        new String[]{"1", "2"}}, null);
        CrossedSetting crossedSetting2 = new CrossedSetting(new String[]{"k2", "K3"},
                new String[][]{new String[]{"1", "2"},
                        new String[]{"X", "Z"}}, null);
        CrossedSetting crossedSetting3 = new CrossedSetting(new String[]{"k1", "K2"},
                new String[][]{new String[]{"A", "c"},
                        new String[]{"1", "2"}}, null);
        try
        {
            crossedSetting1.instantiateSetting(scenarios);
            crossedSetting2.instantiateSetting(scenarios);
            crossedSetting3.instantiateSetting(scenarios);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(crossedSetting1);
        assertNotNull(crossedSetting2);
        assertNotNull(crossedSetting3);

        CrossedScenariosGenerator generator = new CrossedScenariosGenerator();
        CrossedScenarios[] CS = null;
        try
        {
            CS = generator.generateCrossedScenarios(scenarios, new CrossedSetting[]{crossedSetting1, crossedSetting2,
                    crossedSetting3});
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(CS);

        assertEquals(6, CS.length);

        for (int i = 0; i < 3; i++)
        {
            assertEquals(1, CS[i].getFixedKeyValue().length);
            assertEquals(2, CS[i].getLevel());
            assertEquals(2, CS[i].getComparedKeyValues().length);
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().toString());
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[0].getValues().length);
            assertEquals("A", CS[i].getComparedKeyValues()[0].getValues()[0].getValue());
            assertEquals("C", CS[i].getComparedKeyValues()[0].getValues()[1].getValue());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().toString());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[1].getValues().length);
            assertEquals("1", CS[i].getComparedKeyValues()[1].getValues()[0].getValue());
            assertEquals("2", CS[i].getComparedKeyValues()[1].getValues()[1].getValue());
        }

        for (int i = 3; i < 6; i++)
        {
            assertEquals(1, CS[i].getFixedKeyValue().length);
            assertEquals(2, CS[i].getLevel());
            assertEquals(2, CS[i].getComparedKeyValues().length);
            assertEquals("K2", CS[i].getComparedKeyValues()[0].getKey().toString());
            assertEquals("K2", CS[i].getComparedKeyValues()[0].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[0].getValues().length);
            assertEquals("1", CS[i].getComparedKeyValues()[0].getValues()[0].getValue());
            assertEquals("2", CS[i].getComparedKeyValues()[0].getValues()[1].getValue());
            assertEquals("K3", CS[i].getComparedKeyValues()[1].getKey().toString());
            assertEquals("K3", CS[i].getComparedKeyValues()[1].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[1].getValues().length);
            assertEquals("X", CS[i].getComparedKeyValues()[1].getValues()[0].getValue());
            assertEquals("Z", CS[i].getComparedKeyValues()[1].getValues()[1].getValue());
        }


        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("X", CS[0].getFixedKeyValue()[0].getValue().getValue());
        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Y", CS[1].getFixedKeyValue()[0].getValue().getValue());
        assertEquals("K3", CS[2].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[2].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Z", CS[2].getFixedKeyValue()[0].getValue().getValue());

        assertEquals("K1", CS[3].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K1", CS[3].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("A", CS[3].getFixedKeyValue()[0].getValue().getValue());
        assertEquals("K1", CS[4].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K1", CS[4].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("B", CS[4].getFixedKeyValue()[0].getValue().getValue());
        assertEquals("K1", CS[5].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K1", CS[5].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("C", CS[5].getFixedKeyValue()[0].getValue().getValue());

        int[][] exp = new int[][]
                {
                        {0, 0},
                        {1, 0},
                        {0, 1},
                        {1, 1}
                };
        for (int i = 0; i < 6; i++)
        {
            assertEquals(exp.length, CS[i].getPossibleRealizations().length);
            for (int j = 0; j < exp.length; j++)
            {
                assertEquals(exp[j].length, CS[i].getPossibleRealizations()[j].length);
                for (int k = 0; k < exp[j].length; k++) assertEquals(exp[j][k], CS[i].getPossibleRealizations()[j][k]);
            }
        }


        String[][] refScenarios = new String[][]
                {
                        {"K1_A_K2_1_K3_X", "K1_C_K2_1_K3_X", "K1_A_K2_2_K3_X", "K1_C_K2_2_K3_X",},
                        {"K1_A_K2_1_K3_Y", "K1_C_K2_1_K3_Y", "K1_A_K2_2_K3_Y", "K1_C_K2_2_K3_Y",},
                        {"K1_A_K2_1_K3_Z", "K1_C_K2_1_K3_Z", "K1_A_K2_2_K3_Z", "K1_C_K2_2_K3_Z",},
                        {"K1_A_K2_1_K3_X", "K1_A_K2_2_K3_X", "K1_A_K2_1_K3_Z", "K1_A_K2_2_K3_Z",},
                        {"K1_B_K2_1_K3_X", "K1_B_K2_2_K3_X", "K1_B_K2_1_K3_Z", "K1_B_K2_2_K3_Z",},
                        {"K1_C_K2_1_K3_X", "K1_C_K2_2_K3_X", "K1_C_K2_1_K3_Z", "K1_C_K2_2_K3_Z",}
                };

        for (int i = 0; i < 6; i++)
        {
            Set<String> expected = new HashSet<>();
            Collections.addAll(expected, refScenarios[i]);
            for (Scenario s : CS[i].getReferenceScenarios())
                assertTrue(expected.contains(s.toString()));
            assertEquals(refScenarios[i].length, CS[i].getReferenceScenarios().length);

            for (int j = 0; j < 4; j++)
                assertEquals(refScenarios[i][j], CS[i].getReferenceScenariosSorted()[j].toString());
        }

        assertEquals("FIXED_K3_X_COMPARED_K1_K2", CS[0].getStringRepresentation());
        assertEquals("FIXED_K3_Y_COMPARED_K1_K2", CS[1].getStringRepresentation());
        assertEquals("FIXED_K3_Z_COMPARED_K1_K2", CS[2].getStringRepresentation());
        assertEquals("FIXED_K1_A_COMPARED_K2_K3", CS[3].getStringRepresentation());
        assertEquals("FIXED_K1_B_COMPARED_K2_K3", CS[4].getStringRepresentation());
        assertEquals("FIXED_K1_C_COMPARED_K2_K3", CS[5].getStringRepresentation());
    }

    /**
     * Test 5.
     */
    @Test
    void generateCrossedScenarios5()
    {
        String[] keys = new String[]{"K1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "c"},
                {"1", "2", "3"},
                {"x", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];
        Scenarios scenarios = null;

        String msg = null;

        try
        {
            for (int i = 0; i < 3; i++) kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        CrossedSetting crossedSetting1 = new CrossedSetting(new String[]{"k1", "K2"},
                new String[][]{new String[]{"A", "c"},
                        new String[]{"1", "2"}},
                new ScenarioDisablingConditions[]{
                        new ScenarioDisablingConditions("K3", "X")
                });
        CrossedSetting crossedSetting2 = new CrossedSetting(new String[]{"k2", "K3"},
                new String[][]{new String[]{"1", "2"},
                        new String[]{"X", "Z"}},
                new ScenarioDisablingConditions[]{
                        new ScenarioDisablingConditions("K1", "B")
                });
        try
        {
            crossedSetting1.instantiateSetting(scenarios);
            crossedSetting2.instantiateSetting(scenarios);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(crossedSetting1);
        assertNotNull(crossedSetting2);

        CrossedScenariosGenerator generator = new CrossedScenariosGenerator();
        CrossedScenarios[] CS = null;
        try
        {
            CS = generator.generateCrossedScenarios(scenarios, new CrossedSetting[]{crossedSetting1, crossedSetting2});
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(CS);

        assertEquals(4, CS.length);

        for (int i = 0; i < 2; i++)
        {
            assertEquals(1, CS[i].getFixedKeyValue().length);
            assertEquals(2, CS[i].getLevel());
            assertEquals(2, CS[i].getComparedKeyValues().length);
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().toString());
            assertEquals("K1", CS[i].getComparedKeyValues()[0].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[0].getValues().length);
            assertEquals("A", CS[i].getComparedKeyValues()[0].getValues()[0].getValue());
            assertEquals("C", CS[i].getComparedKeyValues()[0].getValues()[1].getValue());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().toString());
            assertEquals("K2", CS[i].getComparedKeyValues()[1].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[1].getValues().length);
            assertEquals("1", CS[i].getComparedKeyValues()[1].getValues()[0].getValue());
            assertEquals("2", CS[i].getComparedKeyValues()[1].getValues()[1].getValue());
        }

        for (int i = 2; i < 4; i++)
        {
            assertEquals(1, CS[i].getFixedKeyValue().length);
            assertEquals(2, CS[i].getLevel());
            assertEquals(2, CS[i].getComparedKeyValues().length);
            assertEquals("K2", CS[i].getComparedKeyValues()[0].getKey().toString());
            assertEquals("K2", CS[i].getComparedKeyValues()[0].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[0].getValues().length);
            assertEquals("1", CS[i].getComparedKeyValues()[0].getValues()[0].getValue());
            assertEquals("2", CS[i].getComparedKeyValues()[0].getValues()[1].getValue());
            assertEquals("K3", CS[i].getComparedKeyValues()[1].getKey().toString());
            assertEquals("K3", CS[i].getComparedKeyValues()[1].getKey().getAbbreviation());
            assertEquals(2, CS[i].getComparedKeyValues()[1].getValues().length);
            assertEquals("X", CS[i].getComparedKeyValues()[1].getValues()[0].getValue());
            assertEquals("Z", CS[i].getComparedKeyValues()[1].getValues()[1].getValue());
        }


        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[0].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Y", CS[0].getFixedKeyValue()[0].getValue().getValue());
        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K3", CS[1].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("Z", CS[1].getFixedKeyValue()[0].getValue().getValue());

        assertEquals("K1", CS[2].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K1", CS[2].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("A", CS[2].getFixedKeyValue()[0].getValue().getValue());
        assertEquals("K1", CS[3].getFixedKeyValue()[0].getKey().toString());
        assertEquals("K1", CS[3].getFixedKeyValue()[0].getKey().getAbbreviation());
        assertEquals("C", CS[3].getFixedKeyValue()[0].getValue().getValue());


        String[][] refScenarios = new String[][]
                {
                        {"K1_A_K2_1_K3_Y", "K1_A_K2_2_K3_Y",
                                "K1_C_K2_1_K3_Y", "K1_C_K2_2_K3_Y",
                        },
                        {"K1_A_K2_1_K3_Z", "K1_A_K2_2_K3_Z",
                                "K1_C_K2_1_K3_Z", "K1_C_K2_2_K3_Z",
                        },

                        {
                                "K1_A_K2_1_K3_X", "K1_A_K2_1_K3_Z",
                                "K1_A_K2_2_K3_X", "K1_A_K2_2_K3_Z",

                        },
                        {
                                "K1_C_K2_1_K3_X", "K1_C_K2_1_K3_Z",
                                "K1_C_K2_2_K3_X", "K1_C_K2_2_K3_Z",

                        }
                };

        for (int i = 0; i < 4; i++)
        {
            Set<String> expected = new HashSet<>();
            Collections.addAll(expected, refScenarios[i]);
            for (Scenario s : CS[i].getReferenceScenarios())
                assertTrue(expected.contains(s.toString()));
            assertEquals(refScenarios[i].length, CS[i].getReferenceScenarios().length);
        }

        assertEquals("FIXED_K3_Y_COMPARED_K1_K2", CS[0].getStringRepresentation());
        assertEquals("FIXED_K3_Z_COMPARED_K1_K2", CS[1].getStringRepresentation());
        assertEquals("FIXED_K1_A_COMPARED_K2_K3", CS[2].getStringRepresentation());
        assertEquals("FIXED_K1_C_COMPARED_K2_K3", CS[3].getStringRepresentation());
    }


    /**
     * Test 6.
     */
    @Test
    void generateCrossedScenarios6()
    {
        String[] keys = new String[]{"K1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "c"},
                {"1", "2", "3"},
                {"x", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];
        Scenarios scenarios = null;

        String msg = null;

        try
        {
            for (int i = 0; i < 3; i++) kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        CrossedSetting crossedSetting1 = new CrossedSetting(new String[]{"k1", "K2"},
                new String[][]{new String[]{"A", "c"},
                        new String[]{"1", "2"}},
                new ScenarioDisablingConditions[]{
                        new ScenarioDisablingConditions("K3", "X"),
                        new ScenarioDisablingConditions("K3", "Y"),
                        new ScenarioDisablingConditions("K3", "Z")
                });
        CrossedSetting crossedSetting2 = new CrossedSetting(new String[]{"k2", "K3"},
                new String[][]{new String[]{"1", "2"},
                        new String[]{"X", "Z"}},
                new ScenarioDisablingConditions[]{
                        new ScenarioDisablingConditions("K1", "A"),
                        new ScenarioDisablingConditions("K1", "B"),
                        new ScenarioDisablingConditions("K1", "C")
                });
        try
        {
            crossedSetting1.instantiateSetting(scenarios);
            crossedSetting2.instantiateSetting(scenarios);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(crossedSetting1);
        assertNotNull(crossedSetting2);

        CrossedScenariosGenerator generator = new CrossedScenariosGenerator();
        CrossedScenarios[] CS = null;
        try
        {
            CS = generator.generateCrossedScenarios(scenarios, new CrossedSetting[]{crossedSetting1, crossedSetting2});
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNull(CS);
    }

    /**
     * Test 7.
     */
    @Test
    void generateCrossedScenarios7()
    {
        String[] keys = new String[]{"K1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "c"},
                {"1", "2", "3"},
                {"x", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];
        Scenarios scenarios = null;

        String msg = null;

        try
        {
            for (int i = 0; i < 3; i++) kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        CrossedSetting crossedSetting = new CrossedSetting(new String[]{"k1", "K2", "K3"},
                new String[][]{new String[]{"A", "b", "C"},
                        new String[]{"1", "2", "3"}, new String[]{"x", "y", "z"}}, null);
        try
        {
            crossedSetting.instantiateSetting(scenarios);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(crossedSetting);

        CrossedScenariosGenerator generator = new CrossedScenariosGenerator();
        CrossedScenarios[] CS = null;
        try
        {
            CS = generator.generateCrossedScenarios(scenarios, new CrossedSetting[]{crossedSetting});
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(CS);

        assertEquals(1, CS.length);

        assertEquals(0, CS[0].getFixedKeyValue().length);
        assertEquals(3, CS[0].getLevel());
        assertEquals(3, CS[0].getComparedKeyValues().length);
        assertEquals("K1", CS[0].getComparedKeyValues()[0].getKey().toString());
        assertEquals("K1", CS[0].getComparedKeyValues()[0].getKey().getAbbreviation());
        assertEquals(3, CS[0].getComparedKeyValues()[0].getValues().length);
        assertEquals("A", CS[0].getComparedKeyValues()[0].getValues()[0].getValue());
        assertEquals("B", CS[0].getComparedKeyValues()[0].getValues()[1].getValue());
        assertEquals("C", CS[0].getComparedKeyValues()[0].getValues()[2].getValue());
        assertEquals("K2", CS[0].getComparedKeyValues()[1].getKey().toString());
        assertEquals("K2", CS[0].getComparedKeyValues()[1].getKey().getAbbreviation());
        assertEquals(3, CS[0].getComparedKeyValues()[1].getValues().length);
        assertEquals("1", CS[0].getComparedKeyValues()[1].getValues()[0].getValue());
        assertEquals("2", CS[0].getComparedKeyValues()[1].getValues()[1].getValue());
        assertEquals("3", CS[0].getComparedKeyValues()[1].getValues()[2].getValue());
        assertEquals(3, CS[0].getComparedKeyValues()[2].getValues().length);
        assertEquals("K3", CS[0].getComparedKeyValues()[2].getKey().toString());
        assertEquals("K3", CS[0].getComparedKeyValues()[2].getKey().getAbbreviation());
        assertEquals("X", CS[0].getComparedKeyValues()[2].getValues()[0].getValue());
        assertEquals("Y", CS[0].getComparedKeyValues()[2].getValues()[1].getValue());
        assertEquals("Z", CS[0].getComparedKeyValues()[2].getValues()[2].getValue());


        String[] refScenarios = new String[]
                {"K1_A_K2_1_K3_X", "K1_B_K2_1_K3_X", "K1_C_K2_1_K3_X",
                        "K1_A_K2_2_K3_X", "K1_B_K2_2_K3_X", "K1_C_K2_2_K3_X",
                        "K1_A_K2_3_K3_X", "K1_B_K2_3_K3_X", "K1_C_K2_3_K3_X",
                        "K1_A_K2_1_K3_Y", "K1_B_K2_1_K3_Y", "K1_C_K2_1_K3_Y",
                        "K1_A_K2_2_K3_Y", "K1_B_K2_2_K3_Y", "K1_C_K2_2_K3_Y",
                        "K1_A_K2_3_K3_Y", "K1_B_K2_3_K3_Y", "K1_C_K2_3_K3_Y",
                        "K1_A_K2_1_K3_Z", "K1_B_K2_1_K3_Z", "K1_C_K2_1_K3_Z",
                        "K1_A_K2_2_K3_Z", "K1_B_K2_2_K3_Z", "K1_C_K2_2_K3_Z",
                        "K1_A_K2_3_K3_Z", "K1_B_K2_3_K3_Z", "K1_C_K2_3_K3_Z",
                };

        Set<String> expected = new HashSet<>();
        Collections.addAll(expected, refScenarios);
        for (Scenario s : CS[0].getReferenceScenarios())
            assertTrue(expected.contains(s.toString()));
        assertEquals(refScenarios.length, CS[0].getReferenceScenarios().length);

        for (int s = 0; s < refScenarios.length; s++)
            assertEquals(CS[0].getReferenceScenariosSorted()[s].toString(), refScenarios[s]);

        assertEquals("FIXED_NONE_COMPARED_K1_K2_K3", CS[0].getStringRepresentation());
    }

    /**
     * Test 8.
     */
    @Test
    void generateCrossedScenarios8()
    {
        String[] keys = new String[]{"K1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "c"},
                {"1", "2", "3"},
                {"x", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];
        Scenarios scenarios = null;

        String msg = null;

        try
        {
            for (int i = 0; i < 3; i++) kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        CrossedSetting crossedSetting = new CrossedSetting(new String[]{"K3", "k1", "K2",},
                new String[][]{new String[]{"z", "x", "y"},
                        new String[]{"A", "C", "b"}, new String[]{"1", "3", "2"}}, null);
        try
        {
            crossedSetting.instantiateSetting(scenarios);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(crossedSetting);

        CrossedScenariosGenerator generator = new CrossedScenariosGenerator();
        CrossedScenarios[] CS = null;
        try
        {
            CS = generator.generateCrossedScenarios(scenarios, new CrossedSetting[]{crossedSetting});
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(CS);

        assertEquals(1, CS.length);

        assertEquals(0, CS[0].getFixedKeyValue().length);
        assertEquals(3, CS[0].getLevel());
        assertEquals(3, CS[0].getComparedKeyValues().length);
        assertEquals("K3", CS[0].getComparedKeyValues()[0].getKey().toString());
        assertEquals("K3", CS[0].getComparedKeyValues()[0].getKey().getAbbreviation());
        assertEquals(3, CS[0].getComparedKeyValues()[0].getValues().length);
        assertEquals("Z", CS[0].getComparedKeyValues()[0].getValues()[0].getValue());
        assertEquals("X", CS[0].getComparedKeyValues()[0].getValues()[1].getValue());
        assertEquals("Y", CS[0].getComparedKeyValues()[0].getValues()[2].getValue());
        assertEquals(3, CS[0].getComparedKeyValues()[1].getValues().length);
        assertEquals("K1", CS[0].getComparedKeyValues()[1].getKey().toString());
        assertEquals("K1", CS[0].getComparedKeyValues()[1].getKey().getAbbreviation());
        assertEquals("A", CS[0].getComparedKeyValues()[1].getValues()[0].getValue());
        assertEquals("C", CS[0].getComparedKeyValues()[1].getValues()[1].getValue());
        assertEquals("B", CS[0].getComparedKeyValues()[1].getValues()[2].getValue());
        assertEquals(3, CS[0].getComparedKeyValues()[2].getValues().length);
        assertEquals("K2", CS[0].getComparedKeyValues()[2].getKey().toString());
        assertEquals("K2", CS[0].getComparedKeyValues()[2].getKey().getAbbreviation());
        assertEquals("1", CS[0].getComparedKeyValues()[2].getValues()[0].getValue());
        assertEquals("3", CS[0].getComparedKeyValues()[2].getValues()[1].getValue());
        assertEquals("2", CS[0].getComparedKeyValues()[2].getValues()[2].getValue());


        String[] refScenarios = new String[]
                {
                        "K1_A_K2_1_K3_Z",   "K1_A_K2_1_K3_X",   "K1_A_K2_1_K3_Y",
                        "K1_C_K2_1_K3_Z",   "K1_C_K2_1_K3_X",   "K1_C_K2_1_K3_Y",
                        "K1_B_K2_1_K3_Z",   "K1_B_K2_1_K3_X",   "K1_B_K2_1_K3_Y",

                        "K1_A_K2_3_K3_Z",   "K1_A_K2_3_K3_X",   "K1_A_K2_3_K3_Y",
                        "K1_C_K2_3_K3_Z",   "K1_C_K2_3_K3_X",   "K1_C_K2_3_K3_Y",
                        "K1_B_K2_3_K3_Z",   "K1_B_K2_3_K3_X",   "K1_B_K2_3_K3_Y",

                        "K1_A_K2_2_K3_Z",   "K1_A_K2_2_K3_X",   "K1_A_K2_2_K3_Y",
                        "K1_C_K2_2_K3_Z",   "K1_C_K2_2_K3_X",   "K1_C_K2_2_K3_Y",
                        "K1_B_K2_2_K3_Z",   "K1_B_K2_2_K3_X",   "K1_B_K2_2_K3_Y",
                };

        Set<String> expected = new HashSet<>();
        Collections.addAll(expected, refScenarios);
        for (Scenario s : CS[0].getReferenceScenarios())
            assertTrue(expected.contains(s.toString()));
        assertEquals(refScenarios.length, CS[0].getReferenceScenarios().length);

        for (int s = 0; s < refScenarios.length; s++)
            assertEquals(CS[0].getReferenceScenariosSorted()[s].toString(), refScenarios[s]);

        assertEquals("FIXED_NONE_COMPARED_K3_K1_K2", CS[0].getStringRepresentation());
    }
}