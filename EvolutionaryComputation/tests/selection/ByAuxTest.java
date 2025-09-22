package selection;

import alternative.Alternative;
import org.junit.jupiter.api.Test;
import population.Specimen;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ByAux}.
 *
 * @author MTomczyk
 */
class ByAuxTest
{
    /**
     * Tests the comparator.
     */
    @Test
    void compare()
    {
        double[][] tests = new double[][]{
                {0.0d, 0.0d}, {1.0d, 0.0d}, {0.0d, 1.0d},
                {0.0d, 0.0d}, {1.0d, 0.0d}, {0.0d, 1.0d},
        };
        boolean[] pd = new boolean[]{true, true, true, false, false, false};
        int[] exp = new int[]{0, 1, -1, 0, -1, 1};

        for (int i = 0; i < tests.length; i++)
        {
            Alternative A = new Alternative("A", null, new double[]{tests[i][0]});
            Alternative B = new Alternative("A", null, new double[]{tests[i][1]});
            Specimen S1 = new Specimen();
            S1.setAlternative(A);
            Specimen S2 = new Specimen();
            S2.setAlternative(B);
            ByAux byAux = new ByAux(pd[i]);
            assertEquals(exp[i], byAux.compare(S1, S2));
        }
    }
}