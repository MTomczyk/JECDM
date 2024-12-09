package indicator.emo;

import org.junit.jupiter.api.Test;
import population.Specimen;
import space.distance.Chebyshev;
import space.distance.Euclidean;
import space.distance.IDistance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for the IGD performance indicator {@link IGD}.
 *
 * @author MTomczyk
 */
class IGDTest
{
    /**
     * Test 1.
     */
    @Test
    void evaluate1()
    {
        IDistance distance = new Chebyshev();
        double[][] rp = new double[][]{{0.0d, 4.0d}, {1.0d, 2.0d}, {2.0d, 1.0d}, {5.0d, 0.0d}};
        double[][] evals = new double[][]{{1.0d, 5.0d}, {2.0d, 3.0d}, {3.0d, 1.0d}, {3.0d, 2.0d}, {5.0d, 1.0d}};
        ArrayList<Specimen> specimens = new ArrayList<>();
        for (double[] e : evals) specimens.add(new Specimen(e));
        IGD gd = new IGD(distance, rp);
        double performance = gd.evaluate(specimens);
        assertEquals(1.0d, performance, 0.000001d);
    }

    /**
     * Test 2.
     */
    @Test
    void evaluate2()
    {
        IDistance distance = new Chebyshev();
        double[][] rp = new double[][]{{0.0d, 4.0d}, {1.0d, 2.0d}, {2.0d, 1.0d}, {5.0d, 0.0d}};
        double[][] evals = new double[][]{{2.0d, 2.0d}, {3.0d, 1.0d}, {3.0d, 2.0d}, {4.0d, 3.0d}};
        ArrayList<Specimen> specimens = new ArrayList<>();
        for (double[] e : evals) specimens.add(new Specimen(e));
        IGD gd = new IGD(distance, rp);
        double performance = gd.evaluate(specimens);
        assertEquals(6.0d / 4.0d, performance, 0.000001d);
    }

    /**
     * Test 2.
     */
    @Test
    void evaluate3()
    {
        IDistance distance = new Euclidean();
        double[][] rp = new double[][]{{0.0d, 4.0d}, {1.0d, 2.0d}, {2.0d, 1.0d}, {5.0d, 0.0d}};
        double[][] evals = new double[][]{{2.0d, 2.0d}, {3.0d, 1.0d}, {3.0d, 2.0d}, {4.0d, 3.0d}};
        ArrayList<Specimen> specimens = new ArrayList<>();
        for (double[] e : evals) specimens.add(new Specimen(e));
        IGD gd = new IGD(distance, rp);
        double performance = gd.evaluate(specimens);
        assertEquals((2.0d + 2.0d * Math.sqrt(2.0d) + Math.sqrt(5.0d)) / 4.0d, performance, 0.000001d);
    }
}