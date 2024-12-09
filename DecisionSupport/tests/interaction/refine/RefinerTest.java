package interaction.refine;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.RefinerException;
import interaction.Status;
import interaction.refine.filters.reduction.RemoveDominated;
import interaction.refine.filters.reduction.RemoveDuplicatesInOS;
import interaction.refine.filters.termination.RequiredSpread;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link Refiner}.
 *
 * @author MTomczyk
 */
class RefinerTest
{

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        Refiner.Params p = new Refiner.Params();
        p._terminationFilters = new LinkedList<>();
        p._terminationFilters.add(null);
        String msg = null;
        Refiner r = new Refiner(p);
        try
        {
            r.validate();
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }

        assertEquals("One of the provided termination filters is null", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        Refiner.Params p = new Refiner.Params();
        p._terminationFilters = new LinkedList<>();
        p._terminationFilters.add(new RequiredSpread(0.01d));
        p._reductionFilters = new LinkedList<>();
        p._reductionFilters.add(null);
        String msg = null;
        Refiner r = new Refiner(p);
        try
        {
            r.validate();
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }

        assertEquals("One of the provided reduction filters is null", msg);
    }


    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        Refiner.Params p = new Refiner.Params();
        p._terminationFilters = new LinkedList<>();
        p._terminationFilters.add(new RequiredSpread(0.01d));
        p._reductionFilters = new LinkedList<>();
        p._reductionFilters.add(new RemoveDuplicatesInOS());
        p._reductionFilters.add(new RemoveDominated());

        String msg = null;
        Refiner r = new Refiner(p);
        try
        {
            r.validate();
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(r);

        try
        {
            r.refine(null);
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision-making context is not provided", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        Refiner.Params p = new Refiner.Params();
        p._terminationFilters = new LinkedList<>();
        p._terminationFilters.add(new RequiredSpread(0.01d));
        p._reductionFilters = new LinkedList<>();
        p._reductionFilters.add(new RemoveDuplicatesInOS());
        p._reductionFilters.add(new RemoveDominated());


        String msg = null;
        Refiner r = new Refiner(p);
        try
        {
            r.validate();
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(r);

        try
        {
            r.refine(new DMContext(null, null, null, null, false, 0));
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The criteria are not provided", msg);
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        Refiner.Params p = new Refiner.Params();
        p._terminationFilters = new LinkedList<>();
        p._terminationFilters.add(new RequiredSpread(0.01d));
        p._reductionFilters = new LinkedList<>();
        p._reductionFilters.add(new RemoveDuplicatesInOS());
        p._reductionFilters.add(new RemoveDominated());


        String msg = null;
        Refiner r = new Refiner(p);
        try
        {
            r.validate();
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(r);

        try
        {
            r.refine(new DMContext(Criteria.constructCriteria("C", 2, false), null,null, null, false, 0));
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The alternatives set is not provided (the array is null)", msg);
    }


    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        Refiner.Params p = new Refiner.Params();
        p._terminationFilters = new LinkedList<>();
        p._terminationFilters.add(new RequiredSpread(10.0d));
        p._reductionFilters = new LinkedList<>();
        p._reductionFilters.add(new RemoveDuplicatesInOS());
        p._reductionFilters.add(new RemoveDominated());

        String msg = null;
        Refiner r = new Refiner(p);
        try
        {
            r.validate();
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(r);

        double[][] evals = new double[][]{
                {0.0d, 0.0d},
                {1.0d, 1.0d},
        };

        Result res = null;
        try
        {
            res = r.refine(new DMContext(Criteria.constructCriteria("C", 2, false), null,
                    new Alternatives(Alternative.getAlternativeArray("A", evals)), null, false, 0));
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(res);

        assertEquals(Status.TERMINATED_DUE_TO_TERMINATION_FILTER, res._status);
        assertTrue(res._terminatedDueToTerminationFilter);
        assertEquals("The set is invalid (not sufficiently spread)", res._terminatedDueToTerminationFilterMessage);
        assertTrue(res._terminationFiltersProcessingTime >= 0);
        assertEquals(0, res._reductionSize);
        assertTrue(res._reductionFiltersProcessingTime >= 0);
        assertEquals(0, res._iteration);
    }


    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        Refiner.Params p = new Refiner.Params();
        p._terminationFilters = new LinkedList<>();
        p._terminationFilters.add(new RequiredSpread(0.0001d));
        p._reductionFilters = new LinkedList<>();
        p._reductionFilters.add(new RemoveDuplicatesInOS());
        p._reductionFilters.add(new RemoveDominated());

        String msg = null;
        Refiner r = new Refiner(p);
        try
        {
            r.validate();
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(r);

        double[][] evals = new double[][]{
                {0.0d, 11.0d},
                {1.0d, 5.0d},
                {3.0d, 3.0d},
                {3.0d, 7.0d},
                {7.0d, 7.0d},
                {11.0d, 1.0d},
                {5.0d, 2.0d},
                {5.0d, 2.0d},
                {9.0d, 9.0d},
                {3.0d, 7.0d},
                {1.0d, 5.0d}
        };

        Result res = null;
        try
        {
            res = r.refine(new DMContext(Criteria.constructCriteria("C", 2, false), null,
                    new Alternatives(Alternative.getAlternativeArray("A", evals)), null, false, 1));
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(res);

        assertEquals(Status.PROCESS_ENDED_SUCCESSFULLY, res._status);
        assertFalse(res._terminatedDueToTerminationFilter);
        assertNull(res._terminatedDueToTerminationFilterMessage);
        assertTrue(res._terminationFiltersProcessingTime >= 0);
        assertEquals(6, res._reductionSize);
        assertTrue(res._terminationFiltersProcessingTime >= 0);
        String[] expNames = new String[]{"A0", "A1", "A2", "A5", "A6"};
        assertNotNull(res._refinedAlternatives);
        assertEquals(expNames.length, res._refinedAlternatives.size());
        for (int i = 0; i < expNames.length; i++)
            assertEquals(expNames[i], res._refinedAlternatives.get(i).getName());
        assertEquals(1, res._iteration);
    }


    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        Refiner.Params p = new Refiner.Params();
        p._terminationFilters = new LinkedList<>();
        p._terminationFilters.add(new RequiredSpread(0.0001d));
        p._reductionFilters = new LinkedList<>();
        p._reductionFilters.add(new RemoveDuplicatesInOS());
        p._reductionFilters.add(new RemoveDominated());


        String msg = null;
        Refiner r = new Refiner(p);
        try
        {
            r.validate();
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(r);

        double[][] evals = new double[][]{
                {0.0d, 11.0d},
                {1.0d, 5.0d},
                {3.0d, 3.0d},
                {3.0d, 7.0d},
                {7.0d, 7.0d},
                {11.0d, 1.0d},
                {5.0d, 2.0d},
                {5.0d, 2.0d},
                {9.0d, 9.0d},
                {3.0d, 7.0d},
                {1.0d, 5.0d}
        };

        Result res = null;
        try
        {
            res = r.refine(new DMContext(Criteria.constructCriteria("C", 2, false), null,
                    new Alternatives(Alternative.getAlternativeArray("A", evals)), null, false, 0));
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(res);
        res.printStringRepresentation();
        assertEquals(Status.PROCESS_ENDED_SUCCESSFULLY, res._status);
        assertFalse(res._terminatedDueToTerminationFilter);
        assertNull(res._terminatedDueToTerminationFilterMessage);
        assertTrue(res._terminationFiltersProcessingTime >= 0);
        assertEquals(6, res._reductionSize);
        assertTrue(res._terminationFiltersProcessingTime >= 0);
        String[] expNames = new String[]{"A0", "A1", "A2", "A5", "A6"};
        assertNotNull(res._refinedAlternatives);
        assertEquals(expNames.length, res._refinedAlternatives.size());
        for (int i = 0; i < expNames.length; i++)
            assertEquals(expNames[i], res._refinedAlternatives.get(i).getName());
        assertEquals(0, res._iteration);
    }
}