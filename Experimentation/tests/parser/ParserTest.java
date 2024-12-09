package parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link Parser}.
 *
 * @author MTomczyk
 */
class ParserTest
{
    /**
     * Test 1.
     */
    @Test
    void parse1()
    {
        String[] args = new String[]{"THR EADS=10"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNull(r._scenarioDisablingConditions);
        assertNull(r._disabledTrials);
        assertEquals(0, r._invalidArgs.size());
    }

    /**
     * Test 2.
     */
    @Test
    void parse2()
    {
        String[] args = new String[]{"THR EADS=10", "WRONG"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNull(r._scenarioDisablingConditions);
        assertNull(r._disabledTrials);
        assertEquals(1, r._invalidArgs.size());
        assertEquals("WRONG", r._invalidArgs.getFirst());
    }

    /**
     * Test 3.
     */
    @Test
    void parse3()
    {
        String[] args = new String[]{"THR EADS=ABCD", "WRONG"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertNull(r._noThreads);
        assertNull(r._scenarioDisablingConditions);
        assertNull(r._disabledTrials);
        assertEquals(2, r._invalidArgs.size());
        assertEquals("THREADS=ABCD", r._invalidArgs.getFirst());
        assertEquals("WRONG", r._invalidArgs.get(1));
    }

    /**
     * Test 4.
     */
    @Test
    void parse4()
    {
        String[] args = new String[]{"THR EADS=10", "WRONG", "DISABLED_TRIALS=[]"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNull(r._scenarioDisablingConditions);
        assertNull(r._disabledTrials);
        assertEquals(1, r._invalidArgs.size());
        assertEquals("WRONG", r._invalidArgs.getFirst());
    }

    /**
     * Test 5.
     */
    @Test
    void parse5()
    {
        String[] args = new String[]{"THR EADS=10", "WRONG", "DISABLED_ TRIALS=[1,2,ABCD,  10-12,10-1]"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNull(r._scenarioDisablingConditions);
        assertNotNull(r._disabledTrials);
        assertTrue(r._disabledTrials.contains(1));
        assertTrue(r._disabledTrials.contains(2));
        assertTrue(r._disabledTrials.contains(10));
        assertTrue(r._disabledTrials.contains(11));
        assertTrue(r._disabledTrials.contains(12));
        assertEquals(1, r._invalidArgs.size());
        assertEquals("WRONG", r._invalidArgs.getFirst());
    }

    /**
     * Test 6.
     */
    @Test
    void parse6()
    {
        String[] args = new String[]{"THR EADS=10", "WRONG", "DISABLED_ TRIALS=[1,2,ABCD,  10-12,10-1]",
                "DISAB_ SC"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNull(r._scenarioDisablingConditions);
        assertNotNull(r._disabledTrials);
        assertTrue(r._disabledTrials.contains(1));
        assertTrue(r._disabledTrials.contains(2));
        assertTrue(r._disabledTrials.contains(10));
        assertTrue(r._disabledTrials.contains(11));
        assertTrue(r._disabledTrials.contains(12));
        assertEquals(2, r._invalidArgs.size());
        assertEquals("WRONG", r._invalidArgs.getFirst());
        assertEquals("DISAB_SC", r._invalidArgs.get(1));
    }

    /**
     * Test 7.
     */
    @Test
    void parse7()
    {
        String[] args = new String[]{"THR EADS=10", "WRONG", "DISABLED_ TRIALS=[1,2,ABCD,  10-12,10-1]",
                "DISAB_ SC", "DISABLED_SCENARIOS=[]"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNull(r._scenarioDisablingConditions);
        assertNotNull(r._disabledTrials);
        assertTrue(r._disabledTrials.contains(1));
        assertTrue(r._disabledTrials.contains(2));
        assertTrue(r._disabledTrials.contains(10));
        assertTrue(r._disabledTrials.contains(11));
        assertTrue(r._disabledTrials.contains(12));
        assertEquals(2, r._invalidArgs.size());
        assertEquals("WRONG", r._invalidArgs.getFirst());
        assertEquals("DISAB_SC", r._invalidArgs.get(1));
    }

    /**
     * Test 8.
     */
    @Test
    void parse8()
    {
        String[] args = new String[]{"THR EADS=10", "WRONG", "DISABLED_ TRIALS=[1,2,ABCD,  10-12,10-1]",
                "DISAB_ SC", "DISABLED_SCENARIOS=[ABCD]"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNull(r._scenarioDisablingConditions);
        assertNotNull(r._disabledTrials);
        assertTrue(r._disabledTrials.contains(1));
        assertTrue(r._disabledTrials.contains(2));
        assertTrue(r._disabledTrials.contains(10));
        assertTrue(r._disabledTrials.contains(11));
        assertTrue(r._disabledTrials.contains(12));
        assertEquals(2, r._invalidArgs.size());
        assertEquals("WRONG", r._invalidArgs.getFirst());
        assertEquals("DISAB_SC", r._invalidArgs.get(1));
    }

    /**
     * Test 9.
     */
    @Test
    void parse9()
    {
        String[] args = new String[]{"THR EADS=10", "WRONG", "DISABLED_ TRIALS=[1,2,ABCD,  10-12,10-1]",
                "DISAB_ SC", "DISABLED_SCENARIOS=[(AAA)]"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNull(r._scenarioDisablingConditions);
        assertNotNull(r._disabledTrials);
        assertTrue(r._disabledTrials.contains(1));
        assertTrue(r._disabledTrials.contains(2));
        assertTrue(r._disabledTrials.contains(10));
        assertTrue(r._disabledTrials.contains(11));
        assertTrue(r._disabledTrials.contains(12));
        assertEquals(2, r._invalidArgs.size());
        assertEquals("WRONG", r._invalidArgs.getFirst());
        assertEquals("DISAB_SC", r._invalidArgs.get(1));
    }

    /**
     * Test 10.
     */
    @Test
    void parse10()
    {
        String[] args = new String[]{"THR EADS=10", "WRONG", "DISABLED_ TRIALS=[1,2,ABCD,  10-12,10-1]",
                "DISAB_ SC", "DISABLED_SCENARIOS=[(PROBLEM:DTLZ2)]"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNotNull(r._scenarioDisablingConditions);
        assertEquals(1, r._scenarioDisablingConditions.size());
        assertEquals("PROBLEM_DTLZ2", r._scenarioDisablingConditions.getFirst().toString());
        assertNotNull(r._disabledTrials);
        assertTrue(r._disabledTrials.contains(1));
        assertTrue(r._disabledTrials.contains(2));
        assertTrue(r._disabledTrials.contains(10));
        assertTrue(r._disabledTrials.contains(11));
        assertTrue(r._disabledTrials.contains(12));
        assertEquals(2, r._invalidArgs.size());
        assertEquals("WRONG", r._invalidArgs.getFirst());
        assertEquals("DISAB_SC", r._invalidArgs.get(1));
    }

    /**
     * Test 11.
     */
    @Test
    void parse11()
    {
        String[] args = new String[]{"THR EADS=10", "WRONG", "DISABLED_ TRIALS=[1,2,ABCD,  10-12,10-1]",
                "DISAB_ SC", "DISABLED_SCENARIOS=[(PROBLEM:DTLZ2;ABCD:DEFG), (XYZ:123)]"};
        Parser parser = new Parser();
        Parser.Result r = parser.parse(args);
        String[] log = r.generateLogLines();
        for (String s : log) System.out.println(s);

        assertEquals(10, r._noThreads);
        assertNotNull(r._scenarioDisablingConditions);
        assertEquals(2, r._scenarioDisablingConditions.size());
        assertEquals("PROBLEM_DTLZ2_ABCD_DEFG", r._scenarioDisablingConditions.getFirst().toString());
        assertEquals("XYZ_123", r._scenarioDisablingConditions.get(1).toString());
        assertNotNull(r._disabledTrials);
        assertTrue(r._disabledTrials.contains(1));
        assertTrue(r._disabledTrials.contains(2));
        assertTrue(r._disabledTrials.contains(10));
        assertTrue(r._disabledTrials.contains(11));
        assertTrue(r._disabledTrials.contains(12));
        assertEquals(2, r._invalidArgs.size());
        assertEquals("WRONG", r._invalidArgs.getFirst());
        assertEquals("DISAB_SC", r._invalidArgs.get(1));
    }
}