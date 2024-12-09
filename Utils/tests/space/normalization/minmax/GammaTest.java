package space.normalization.minmax;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Several tests for {@link Gamma} class.
 *
 * @author MTomczyk
 */
class GammaTest
{
    /**
     * Test 1.
     */
    @Test
    void getNormalized1()
    {
        AbstractMinMaxNormalization n = new Gamma(-1.0d, 1.0d, 5.0d);
        double[] V = new double[]{-1.0d, -0.5d, 0.0d, 0.5d, 1.0d};
        double[] E = new double[]{0.0d, 0.000976563, 0.03125, 0.237304688, 1.0d};
        for (int i = 0; i < V.length; i++)
        {
            assertEquals(E[i], n.getNormalized(V[i]), 0.0001f);
            assertEquals(V[i], n.getUnnormalized(n.getNormalized(V[i])), 0.0001f);
        }
    }
}