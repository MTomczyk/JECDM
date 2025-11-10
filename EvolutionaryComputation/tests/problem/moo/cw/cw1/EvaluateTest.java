package problem.moo.cw.cw1;

import org.junit.jupiter.api.Test;
import print.PrintUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link Evaluate}.
 *
 * @author MTomczyk
 */
class EvaluateTest
{

    /**
     * Tests boundary solutions.
     */
    @Test
    void evaluate()
    {

        Evaluate E = new Evaluate();
        // Min f1
        double[] f1 = E.evaluate(new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d});
        PrintUtils.printVectorOfDoubles(f1, 5);

        double[] f2 = E.evaluate(new double[]{0.0d, 1.0d, 1.0d, 0.0d, 0.0d});
        PrintUtils.printVectorOfDoubles(f2, 5);

        double[] f3 = f1.clone();
        double[] x = new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d};
        for (double x1 : new double[]{0.0d, 1.0d})
            for (double x2 : new double[]{0.0d, 1.0d})
                for (double x3 : new double[]{0.0d, 1.0d})
                    for (double x4 : new double[]{0.0d, 1.0d})
                        for (double x5 : new double[]{0.0d, 1.0d})
                        {
                            double[] xt = new double[]{x1, x2, x3, x4, x5};
                            double[] f = E.evaluate(xt);
                            if (Double.compare(f[2], f3[2]) < 0)
                            {
                                f3 = f;
                                x = xt;
                            }
                        }
        PrintUtils.printVectorOfDoubles(f3, 5);
        PrintUtils.printVectorOfDoubles(x, 5);

        double [] u = new double[]{f1[0], f2[1], f3[2]};
        double [] n = new double[]{Math.max(f2[0], f3[0]), Math.max(f1[1], f3[1]), Math.max(f1[2], f2[2])};
        System.out.println("Utopia:");
        PrintUtils.printVectorOfDoubles(u, 9);
        System.out.println("Nadir:");
        PrintUtils.printVectorOfDoubles(n, 9);

    }
}