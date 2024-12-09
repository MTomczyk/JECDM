package interaction.reference.validator;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import org.junit.jupiter.api.Test;
import space.Range;
import space.os.ObjectiveSpace;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link RequiredSpread}.
 *
 * @author MTomczyk
 */
class RequiredSpreadTest
{

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        RequiredSpread rs = new RequiredSpread(1.0d);
        String msg = null;
        try
        {
            rs.isValid(null, null);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Error occurred when refining the alternatives (handler = interaction.refine.filters.termination.RequiredSpread, reason = The decision-making context is not provided)", msg);
    }


    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        DMContext dmContext = new DMContext(null, null, null, null, false, 0);
        RequiredSpread rs = new RequiredSpread(1.0d);
        String msg = null;
        try
        {
            rs.isValid(dmContext, null);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Error occurred when refining the alternatives (handler = interaction.refine.filters.termination.RequiredSpread, reason = The alternatives set is not provided (the array is null))", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false),
                null, null, null, false, 0);
        RequiredSpread rs = new RequiredSpread(1.0d);
        String msg = null;
        try
        {
            rs.isValid(dmContext, new Alternatives(new ArrayList<>()));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The alternatives set is not provided (the array is empty)", msg);
    }


    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[2]));
        DMContext dmContext = new DMContext(null, null, new Alternatives(A), null, false, 0);
        RequiredSpread rs = new RequiredSpread(1.0d);
        String msg = null;
        try
        {
            rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Error occurred when refining the alternatives (handler = interaction.refine.filters.termination.RequiredSpread, reason = The criteria are not provided)", msg);
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[2]));
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false),
                null, new Alternatives(A), null, false, 0);
        RequiredSpread rs = new RequiredSpread(null);
        String msg = null;
        try
        {
            rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Error occurred when refining the alternatives (handler = interaction.refine.filters.termination.RequiredSpread, reason = The thresholds are not provided (the array is null))", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[2]));
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false),
                null, new Alternatives(A), null, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[0]);
        String msg = null;
        try
        {
            rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Error occurred when refining the alternatives (handler = interaction.refine.filters.termination.RequiredSpread, reason = The thresholds are not provided (the array is empty))", msg);
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[2]));
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 3, false),
                null, new Alternatives(A), null, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[2]);
        String msg = null;
        try
        {
            rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Error occurred when refining the alternatives (handler = interaction.refine.filters.termination.RequiredSpread, reason = The number of criteria (3) differs from the number of thresholds (2) (and the number of thresholds is not 1))", msg);
    }

    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[1]));
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false),
                null, new Alternatives(A), null, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[2]);
        String msg = null;
        try
        {
            rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Error occurred when refining the alternatives (handler = interaction.refine.filters.termination.RequiredSpread, reason = The alternative A1 does not have enough evaluations (required at least 2 but has 1))", msg);
    }

    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[2]));
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, new Alternatives(A), null, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[2]);
        String msg = null;
        Boolean result = null;
        try
        {
            result = rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(result);
        assertTrue(result);
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[]{1.0d, 2.0d}));
        A.add(new Alternative("A2", new double[]{2.0d, 1.0d}));

        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, new Alternatives(A), null, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[]{0.5d, 0.5d});
        String msg = null;
        Boolean result = null;
        try
        {
            result = rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(result);
        assertTrue(result);
    }

    /**
     * Test 11.
     */
    @Test
    void test11()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[]{1.0d, 2.0d}));
        A.add(new Alternative("A2", new double[]{2.0d, 1.0d}));

        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, new Alternatives(A), null, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[]{1.0000001d, 1.0000001d});
        String msg = null;
        Boolean result = null;
        try
        {
            result = rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(result);
        assertFalse(result);
    }


    /**
     * Test 12.
     */
    @Test
    void test12()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[]{1.0d, 2.0d}));
        A.add(new Alternative("A2", new double[]{2.0d, 3.1d}));

        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, new Alternatives(A), null, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[]{1.0000001d, 1.0000001d});
        String msg = null;
        Boolean result = null;
        try
        {
            result = rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(result);
        assertTrue(result);
    }

    /**
     * Test 13.
     */
    @Test
    void test13()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[]{1.0d, 2.0d}));
        A.add(new Alternative("A2", new double[]{2.0d, 1.1d}));

        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{Range.getNormalRange()}, new boolean[]{false});
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null,
                new Alternatives(A), OS, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[]{1.0000001d, 1.0000001d});
        String msg = null;
        try
        {
            rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Error occurred when refining the alternatives (handler = interaction.refine.filters.termination.RequiredSpread, reason = Not enough normalization objects were constructed (1 but at least 2 are required))", msg);
    }

    /**
     * Test 14.
     */
    @Test
    void test14()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[]{1.0d, 2.0d}));
        A.add(new Alternative("A2", new double[]{2.0d, 3.1d}));

        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{Range.getNormalRange(), Range.getNormalRange()}, new boolean[]{false, false});
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, new Alternatives(A), OS, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[]{1.0000001d, 1.0000001d});
        String msg = null;
        Boolean result = null;
        try
        {
            result = rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(result);
        assertTrue(result);
    }

    /**
     * Test 15.
     */
    @Test
    void test15()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[]{1.0d, 2.0d}));
        A.add(new Alternative("A2", new double[]{2.0d, 3.1d}));

        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{Range.getNormalRange(), new Range(0.0d, 2.0d)}, new boolean[]{false, false});
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, new Alternatives(A), OS, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[]{1.0000001d, 1.0000001d});
        String msg = null;
        Boolean result = null;
        try
        {
            result = rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(result);
        assertFalse(result);
    }

    /**
     * Test 16.
     */
    @Test
    void test16()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[]{1.0d, 2.0d}));
        A.add(new Alternative("A2", new double[]{2.0d, 1.0d}));

        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{
                new Range(0.0d, 2.0d), new Range(0.0d, 4.0d)}, new boolean[]{false, false});
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, new Alternatives(A), OS, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[]{0.5d - 0.0001d, 0.25d - 0.0001d});
        String msg = null;
        Boolean result = null;
        try
        {
            result = rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(result);
        assertTrue(result);
    }

    /**
     * Test 17.
     */
    @Test
    void test17()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[]{1.0d, 2.0d}));
        A.add(new Alternative("A2", new double[]{2.0d, 1.0d}));

        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{
                new Range(0.0d, 2.0d), new Range(0.0d, 4.0d)}, new boolean[]{false, false});
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, new Alternatives(A), OS, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[]{0.5d + 0.0001d, 0.25d - 0.0001d});
        String msg = null;
        Boolean result = null;
        try
        {
            result = rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(result);
        assertTrue(result);
    }

    /**
     * Test 18.
     */
    @Test
    void test18()
    {
        ArrayList<Alternative> A = new ArrayList<>();
        A.add(new Alternative("A1", new double[]{1.0d, 2.0d}));
        A.add(new Alternative("A2", new double[]{2.0d, 1.0d}));

        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{
                new Range(0.0d, 2.0d), new Range(0.0d, 4.0d)}, new boolean[]{false, false});
        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, new Alternatives(A), OS, false, 0);
        RequiredSpread rs = new RequiredSpread(new double[]{0.5d + 0.0001d, 0.25d + 0.0001d});
        String msg = null;
        Boolean result = null;
        try
        {
            result = rs.isValid(dmContext, new Alternatives(A));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(result);
        assertFalse(result);
    }
}