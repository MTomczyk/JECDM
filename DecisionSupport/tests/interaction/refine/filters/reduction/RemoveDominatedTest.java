package interaction.refine.filters.reduction;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.RefinerException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link RemoveDominated}.
 *
 * @author MTomczyk
 */
class RemoveDominatedTest
{

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        IReductionFilter f = new RemoveDominated();
        String msg = null;
        try
        {
            f.reduce(null, null);
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision-making context is not provided", msg);
    }


    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        IReductionFilter f = new RemoveDominated();
        String msg = null;
        try
        {
            f.reduce(new DMContext(null, null, null, null, false, 0), null);
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The criteria are not provided", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        IReductionFilter f = new RemoveDominated();
        String msg = null;
        try
        {
            f.reduce(new DMContext(Criteria.constructCriteria("C", 2, false), null, null, null, false, 0), null);
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The alternatives set is not provided (the array is null)", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        double[][] input = new double[][]
                {
                        {2.0d, 2.0d, 2.0d},
                        {0.0d, 1.0d, 3.0d},
                        {3.0d, 1.0d, 0.0d},
                        {1.0d, 1.0d, 1.0d},
                };
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", input);


        IReductionFilter f = new RemoveDominated();
        AbstractAlternatives<?> nonDom = null;
        String msg = null;
        try
        {
            nonDom = f.reduce(new DMContext(Criteria.constructCriteria("C", 3, false), null, null, null, false, 0), new Alternatives(alternatives));
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(nonDom);

        assertEquals(3, nonDom.size());
        assertEquals(0.0d, nonDom.get(0).getPerformanceVector()[0], 0.0000001d);
        assertEquals(1.0d, nonDom.get(0).getPerformanceVector()[1], 0.0000001d);
        assertEquals(3.0d, nonDom.get(0).getPerformanceVector()[2], 0.0000001d);
        assertEquals(3.0d, nonDom.get(1).getPerformanceVector()[0], 0.0000001d);
        assertEquals(1.0d, nonDom.get(1).getPerformanceVector()[1], 0.0000001d);
        assertEquals(0.0d, nonDom.get(1).getPerformanceVector()[2], 0.0000001d);
        assertEquals(1.0d, nonDom.get(2).getPerformanceVector()[0], 0.0000001d);
        assertEquals(1.0d, nonDom.get(2).getPerformanceVector()[1], 0.0000001d);
        assertEquals(1.0d, nonDom.get(2).getPerformanceVector()[2], 0.0000001d);
    }

}