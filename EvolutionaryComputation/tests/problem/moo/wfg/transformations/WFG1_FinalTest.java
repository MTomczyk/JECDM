package problem.moo.wfg.transformations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Some tests for {@link WFG1_Final} class.
 *
 * @author MTomczyk
 */
class WFG1_FinalTest
{
    @Test
    void applyTransformation()
    {
        ITransformation t = new WFG1_Final(3, 4);
        double[] r = { 0.2, 0.4, 0.6, 0.1, 0.7, 0.9, 0.1, 0.6 };
        double[] v = t.applyTransformation(r);

        double[] e = { 0.333333333, 0.314285714, 0.553846154 };

        for (int i = 0; i < 3; i++)
        {
            String a = String.format("%.5f", e[i]);
            String b = String.format("%.5f", v[i]);
            assertEquals(a, b);

        }
    }
}