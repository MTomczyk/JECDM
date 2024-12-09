package statistics.distribution.bucket;

import org.junit.jupiter.api.Test;
import space.Range;
import statistics.distribution.bucket.transform.LinearlyThresholded;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Several tests for {@link BucketCoordsTransform} class.
 *
 * @author MTomczyk
 */
class BucketCoordsTransformTest
{

    /**
     * Test #1.
     */
    @Test
    void getBucketCoords1()
    {
        BucketCoordsTransform bc = new BucketCoordsTransform(1, 5, Range.getNormalRange(), new LinearlyThresholded());

        double[] check = new double[]{-0.0000001d, 0.0d, 0.1d, 0.3d, 0.5d, 0.7d, 0.9f, 1.0d, 1.00000001d};
        Integer[] exp = new Integer[]{null, 0, 0, 1, 2, 3, 4, null, null};

        for (int i = 0; i < check.length; i++)
        {
            int[] c = bc.getBucketCoords(new double[]{check[i]});
            if (exp[i] == null) assertNull(c);
            else
            {
                assertEquals(1, c.length);
                assertEquals(exp[i], c[0]);
            }
        }
    }


    /**
     * Test #2.
     */
    @Test
    void getBucketCoords2()
    {
        BucketCoordsTransform bc = new BucketCoordsTransform(1, 5, new Range(0.0d, 2.0d), new LinearlyThresholded());

        double[] check = new double[]{-0.0000001d, 0.0d, 0.1d, 0.3d, 0.5d, 0.7d, 0.9f, 1.0d, 1.00000001d};
        Integer[] exp = new Integer[]{null, 0, 0, 1, 2, 3, 4, null, null};

        for (int i = 0; i < check.length; i++)
        {
            int[] c = bc.getBucketCoords(new double[]{check[i] * 2});
            if (exp[i] == null) assertNull(c);
            else
            {
                assertEquals(1, c.length);
                assertEquals(exp[i], c[0]);
            }
        }
    }


    /**
     * Test #3.
     */
    @Test
    void getBucketCoords3()
    {
        BucketCoordsTransform bc = new BucketCoordsTransform(1, 1, new Range(0.0d, 2.0d), new LinearlyThresholded());

        double[] check = new double[]{-0.0000001d, 0.0d, 0.1d, 0.3d, 0.5d, 0.7d, 0.9f, 1.0d, 1.00000001d};
        Integer[] exp = new Integer[]{null, 0, 0, 0, 0, 0, 0, null, null};

        for (int i = 0; i < check.length; i++)
        {
            int[] c = bc.getBucketCoords(new double[]{check[i] * 2});
            if (exp[i] == null) assertNull(c);
            else
            {
                assertEquals(1, c.length);
                assertEquals(exp[i], c[0]);
            }
        }
    }


    /**
     * Test #4.
     */
    @Test
    void getBucketCoords4()
    {
        BucketCoordsTransform bc = new BucketCoordsTransform(1, 2, new Range(0.0d, 2.0d), new LinearlyThresholded());

        double[] check = new double[]{-0.0000001d, 0.0d, 0.1d, 0.3d, 0.5d, 0.7d, 0.9f, 1.0d, 1.00000001d};
        Integer[] exp = new Integer[]{null, 0, 0, 0, 1, 1, 1, null, null};

        for (int i = 0; i < check.length; i++)
        {
            int[] c = bc.getBucketCoords(new double[]{check[i] * 2});
            if (exp[i] == null) assertNull(c);
            else
            {
                assertEquals(1, c.length);
                assertEquals(exp[i], c[0]);
            }
        }
    }


    /**
     * Test #6.
     */
    @SuppressWarnings("DataFlowIssue")
    @Test
    void getBucketCoords6()
    {
        BucketCoordsTransform bc = new BucketCoordsTransform(2, new int[]{5, 2},
                new Range[]{Range.getNormalRange(), new Range(1.0d, 2.0d)}, new LinearlyThresholded());

        double[][] check = new double[][]{
                new double[]{-0.0000001d, 1.5d},
                new double[]{0.0d, 0.0d},
                new double[]{0.1d, 1.0d},
                new double[]{0.3d, 1.25d},
                new double[]{0.5d, 1.5d},
                new double[]{0.7d, 1.75d},
                new double[]{0.9f, 2.0d},
                new double[]{1.0d, 3.0d},
                new double[]{1.00000001d, 1.5d}
        };

        Integer[][] exp = new Integer[][]{
                null,
                null,
                new Integer[]{0, 0},
                new Integer[]{1, 0},
                new Integer[]{2, 1},
                new Integer[]{3, 1},
                null,
                null,
                null
        };

        for (int i = 0; i < check.length; i++)
        {
            int[] c = bc.getBucketCoords(check[i]);
            if (exp[i] == null) assertNull(c);
            else
            {
                assertEquals(2, c.length);
                assertEquals(exp[i][0], c[0]);
                assertEquals(exp[i][1], c[1]);
            }
        }
    }
}