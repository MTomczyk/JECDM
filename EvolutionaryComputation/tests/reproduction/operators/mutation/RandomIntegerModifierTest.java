package reproduction.operators.mutation;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link RandomIntegerModifier}.
 *
 * @author MTomczyk
 */
class RandomIntegerModifierTest
{
    /**
     * Tests {@link RandomIntegerModifier#mutate(int[], IRandom)}
     */
    @Test
    void mutate()
    {
        IRandom R = new MersenneTwister64(0);
        {
            int[] o = new int[]{5, 4, 2, 1};
            RandomIntegerModifier RIM = new RandomIntegerModifier(1.0d, 2, 2);
            RIM.mutate(o, R);
            assertEquals(7, o[0]);
            assertEquals(6, o[1]);
            assertEquals(4, o[2]);
            assertEquals(3, o[3]);
        }

        for (int i = 0; i < 1000; i++)
        {
            int lb = -5 + R.nextInt(10);
            int ub = lb + R.nextInt(5);
            int[] o = R.nextInts(100, 10);
            int[] or = o.clone();
            double prob = R.nextDouble();
            RandomIntegerModifier RIM = new RandomIntegerModifier(prob, lb, ub);
            if (ub < lb) ub = lb;
            RIM.mutate(o, R);
            for (int j = 0; j < 100; j++)
                assertTrue((or[j] == o[j]) || ((o[j] >= or[j] + lb) && (o[j] <= or[j] + ub)));
        }
    }
}