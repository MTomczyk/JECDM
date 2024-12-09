package interaction.refine.filters.reduction;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.RefinerException;
import org.junit.jupiter.api.Test;
import space.Vector;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link RemoveDuplicatesInOS}.
 *
 * @author MTomczyk
 */
class RemoveDuplicatesInOSTest
{

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        IReductionFilter f = new RemoveDuplicatesInOS();
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
        IReductionFilter f = new RemoveDuplicatesInOS();
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
        IReductionFilter f = new RemoveDuplicatesInOS();
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
        double[][] alternatives = new double[][]
                {
                        {2.0d, 2.0d, 2.0d}, // ok
                        {0.0d, 1.0d, 3.0d}, // ok
                        {2.0d, 2.0d, 2.0d}, // x
                        {1.0d, 1.0d, 1.0d}, // ok
                        {1.0d, 1.0d, 1.0d}, // x
                        {1.0d, 1.0d}, // ok
                        {1.0d, 1.0d}, // x
                        {0.0d, 1.0d, 3.0d}, // x
                        {0.0d, 1.0d, 2.0d, 3.0d} // ok
                };

        double[][] expected1 = new double[][]
                {
                        {2.0d, 2.0d, 2.0d}, // ok
                        {0.0d, 1.0d, 3.0d}, // ok
                        {1.0d, 1.0d, 1.0d}, // ok
                        {1.0d, 1.0d}, // ok
                        {0.0d, 1.0d, 2.0d, 3.0d} // ok
                };

        ArrayList<Alternative> As = Alternative.getAlternativeArray("A", alternatives);


        IReductionFilter f = new RemoveDuplicatesInOS();
        String msg = null;
        AbstractAlternatives<?> reduced = null;
        try
        {
            reduced = f.reduce(new DMContext(Criteria.constructCriteria("5", 2, false), null, null, null, false, 0), new Alternatives(As));
        } catch (RefinerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(reduced);

        assertEquals(reduced.size(), expected1.length);
        for (int i = 0; i < expected1.length; i++)
            assertTrue(Vector.areVectorsEqual(expected1[i], reduced.get(i).getPerformanceVector()));
    }
}