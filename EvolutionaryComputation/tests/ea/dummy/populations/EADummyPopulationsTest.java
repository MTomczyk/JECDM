package ea.dummy.populations;

import ea.EA;
import exception.RunnerException;
import org.junit.jupiter.api.Test;
import population.Specimen;
import runner.IRunner;
import runner.Runner;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Various tests for {@link ea.dummy.populations.EADummyPopulations} class.
 *
 * @author MTomczyk
 */
class EADummyPopulationsTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        double[][][] P = new double[3][][]; // 3 generations
        P[0] = new double[][]{{2.0d, 6.0d}, {1.0d, 2.0d}, {11.0d, 6.0d}, {5.0d, 2.0d}, {-1.0d, -2.0d}};
        P[1] = new double[][]{{5.0d, 5.0d}, {2.0d, 2.0d}, {3.0d, 2.0d}, {7.0d, 4.0d}, {2.0d, 1.0d}};
        P[2] = new double[][]{{9.0d, 8.0d}, {7.0d, 3.0d}, {2.0d, 2.0d}, {1.0d, 1.0d}, {3.0d, 2.0d}};

        EA ea = new EADummyPopulations(2, P);

        Runner.Params pR = new Runner.Params(ea);
        IRunner R = new Runner(pR);

        String msg = null;
        try
        {
            R.init();
        } catch (RunnerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(ea.getSpecimensContainer());
        assertNotNull(ea.getSpecimensContainer().getPopulation());

        ArrayList<Specimen> cP = ea.getSpecimensContainer().getPopulation();
        assertEquals(5, cP.size());
        for (int i = 0; i < 5; i++)
        {
            assertEquals(2, cP.get(i).getEvaluations().length);
            assertEquals(P[0][i][0], cP.get(i).getEvaluations()[0], 0.00001d);
            assertEquals(P[0][i][1], cP.get(i).getEvaluations()[1], 0.00001d);
        }


        try
        {
            R.executeSingleGeneration(1, new int[]{4, 4});
        } catch (RunnerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        cP = ea.getSpecimensContainer().getPopulation();
        assertEquals(5, cP.size());
        for (int i = 0; i < 5; i++)
        {
            assertEquals(2, cP.get(i).getEvaluations().length);
            assertEquals(P[1][i][0], cP.get(i).getEvaluations()[0], 0.00001d);
            assertEquals(P[1][i][1], cP.get(i).getEvaluations()[1], 0.00001d);
        }

        try
        {
            R.executeSingleGeneration(2, new int[]{4, 4});
        } catch (RunnerException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        cP = ea.getSpecimensContainer().getPopulation();
        assertEquals(5, cP.size());
        for (int i = 0; i < 5; i++)
        {
            assertEquals(2, cP.get(i).getEvaluations().length);
            assertEquals(P[2][i][0], cP.get(i).getEvaluations()[0], 0.00001d);
            assertEquals(P[2][i][1], cP.get(i).getEvaluations()[1], 0.00001d);
        }

        boolean called = false;

        try
        {
            R.executeSingleGeneration(3, new int[]{4, 4});
        } catch (Exception e)
        {
            called = true;
        }

        assertTrue(called);
    }
}