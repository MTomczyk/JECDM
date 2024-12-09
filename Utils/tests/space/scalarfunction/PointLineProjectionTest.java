package space.scalarfunction;

import org.junit.jupiter.api.Test;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for the {@link PBI} class.
 *
 * @author MTomczyk
 */
class PointLineProjectionTest
{

    /**
     * Tests evaluations.
     */
    @Test
    void evaluate()
    {
        {
            {
                PointLineProjection plp = new PointLineProjection(new double[]{2.0d, 2.0d});
                double[] e = new double[]{0.0d, 2.0d};
                double r = plp.evaluate(e);
                assertEquals(Math.sqrt(2.0d), r, 0.000001d);
            }
            {
                PointLineProjection plp = new PointLineProjection(new double[]{2.0d, 2.0d});
                double[] e = new double[]{2.0d, 2.0d};
                double r = plp.evaluate(e);
                assertEquals(0.0d, r, 0.000001d);
            }

            {
                PointLineProjection plp = new PointLineProjection(new double[]{2.0d, 2.0d});
                double[] e = new double[]{4.0d, 0.0d};
                double r = plp.evaluate(e);
                assertEquals(2.0d * Math.sqrt(2.0d), r, 0.000001d);
            }
        }

        {
            INormalization[] N = new INormalization[]
                    {
                            new Linear(0.0d, 1.0d),
                            new Linear(1.0d, 3.0d)
                    };
            {
                PointLineProjection plp = new PointLineProjection(new double[]{2.0d, 2.0d}, N);
                double[] e = new double[]{0.0d, 5.0d};
                double r = plp.evaluate(e);
                assertEquals(Math.sqrt(2.0d), r, 0.000001d);
            }

            {
                PointLineProjection plp = new PointLineProjection(new double[]{2.0d, 2.0d}, N);
                double[] e = new double[]{2.0d, 5.0d};
                double r = plp.evaluate(e);
                assertEquals(0.0d, r, 0.000001d);
            }

            {
                PointLineProjection plp = new PointLineProjection(new double[]{2.0d, 2.0d}, N);
                double[] e = new double[]{4.0d, 1.0d};
                double r = plp.evaluate(e);
                assertEquals(2.0d * Math.sqrt(2.0d), r, 0.000001d);
            }
        }
    }
}