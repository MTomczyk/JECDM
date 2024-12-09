package statistics.movingaverage;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Several tests for the {@link MovingAverageDouble} class.
 *
 * @author MTomczyk
 */
class MovingAverageLongTest
{

    /**
     * Test 1.
     */
    @Test
    void getAverage1()
    {
        MovingAverageLong MA = new MovingAverageLong(10);
        assertNull(MA.getAverage());

        long [] ds = new long[200];
        for (int i = 0; i < 200; i++) ds[i] = i + 1;
        double [] exp = new double[200];
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j <= i; j++) exp[i] += ds[j];
            exp[i] /= (i + 1);
        }
        for (int i = 10; i < 200; i++)
        {
            for (int j = i - 9; j <= i; j++) exp[i] += ds[j];
            exp[i] /= 10;
        }

        for (int i = 0; i < 200; i++)
        {
            MA.addData(ds[i]);
            assertEquals(exp[i], MA.getAverage(), 0.0001d);
        }
    }

    /**
     * Test 2.
     */
    @Test
    void getAverage2()
    {
        MovingAverageLong MA = new MovingAverageLong(1);
        assertNull(MA.getAverage());

        long [] ds = new long[200];
        for (int i = 0; i < 200; i++) ds[i] = i + 1;
        double [] exp = new double[200];
        for (int i = 0; i < 200; i++) exp[i] = ds[i];

        for (int i = 0; i < 200; i++)
        {
            MA.addData(ds[i]);
            assertEquals(exp[i], MA.getAverage(), 0.0001d);
        }
    }

    /**
     * Test 3.
     */
    @Test
    void getAverage3()
    {
        MovingAverageLong MA = new MovingAverageLong(10);
        assertNull(MA.getAverage());

        IRandom R = new MersenneTwister64(System.currentTimeMillis());

        long [] ds = new long[1000];
        for (int i = 0; i < 1000; i++) ds[i] = R.nextInt(100);
        double [] exp = new double[1000];
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j <= i; j++) exp[i] += ds[j];
            exp[i] /= (i + 1);
        }
        for (int i = 10; i < 1000; i++)
        {
            for (int j = i - 9; j <= i; j++) exp[i] += ds[j];
            exp[i] /= 10;
        }

        for (int i = 0; i < 1000; i++)
        {
            MA.addData(ds[i]);
            assertEquals(exp[i], MA.getAverage(), 0.0001d);
        }

        MA.reset();

        for (int i = 0; i < 1000; i++)
        {
            MA.addData(ds[i]);
            assertEquals(exp[i], MA.getAverage(), 0.0001d);
        }
    }


}