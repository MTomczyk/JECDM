package space.scalarfunction;

import org.junit.jupiter.api.Test;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Various tests for the {@link PBI} class.
 *
 * @author MTomczyk
 */
class PBITest
{

    /**
     * Tests evaluations.
     */
    @Test
    void evaluate()
    {
        {
            {
                PBI pbi = new PBI(new double[]{2.0d, 2.0d}, 1.0d);
                double[] e = new double[]{0.0d, 2.0d};
                double r = pbi.evaluate(e);
                assertEquals(2.0d * Math.sqrt(2.0d), r, 0.000001d);
                assertTrue(pbi.isLessMorePreferred());
            }
            {
                PBI pbi = new PBI(new double[]{2.0d, 2.0d}, 2.0d);
                double[] e = new double[]{0.0d, 2.0d};
                double r = pbi.evaluate(e);
                assertEquals(3.0d * Math.sqrt(2.0d), r, 0.000001d);
            }

            {
                PBI pbi = new PBI(new double[]{2.0d, 2.0d}, 100.0d);
                double[] e = new double[]{2.0d, 2.0d};
                double r = pbi.evaluate(e);
                assertEquals(2.0d * Math.sqrt(2.0d), r, 0.000001d);
            }

            {
                PBI pbi = new PBI(new double[]{2.0d, 2.0d}, 0.5d);
                double[] e = new double[]{4.0d, 0.0d};
                double r = pbi.evaluate(e);
                assertEquals(3.0d * Math.sqrt(2.0d), r, 0.000001d);
            }
        }

        {
            INormalization[] N = new INormalization[]
                    {
                            new Linear(0.0d, 1.0d),
                            new Linear(1.0d, 3.0d)
                    };
            {
                PBI pbi = new PBI(new double[]{2.0d, 2.0d}, 1.0d, N);
                double[] e = new double[]{0.0d, 5.0d};
                double r = pbi.evaluate(e);
                assertEquals(2.0d * Math.sqrt(2.0d), r, 0.000001d);
            }
            {
                PBI pbi = new PBI(new double[]{2.0d, 2.0d}, 2.0d, N);
                double[] e = new double[]{0.0d, 5.0d};
                double r = pbi.evaluate(e);
                assertEquals(3.0d * Math.sqrt(2.0d), r, 0.000001d);
            }

            {
                PBI pbi = new PBI(new double[]{2.0d, 2.0d}, 100.0d, N);
                double[] e = new double[]{2.0d, 5.0d};
                double r = pbi.evaluate(e);
                assertEquals(2.0d * Math.sqrt(2.0d), r, 0.000001d);
            }

            {
                PBI pbi = new PBI(new double[]{2.0d, 2.0d}, 0.5d, N);
                double[] e = new double[]{4.0d, 1.0d};
                double r = pbi.evaluate(e);
                assertEquals(3.0d * Math.sqrt(2.0d), r, 0.000001d);
            }
        }
    }
}