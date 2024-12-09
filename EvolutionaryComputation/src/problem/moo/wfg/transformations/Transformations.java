package problem.moo.wfg.transformations;


import org.apache.commons.math4.legacy.stat.StatUtils;

/**
 * Provides various static methods for WFG transformations.
 */
public class Transformations
{
    /**
     * Executes: polynomial bias.
     *
     * @param y     input value
     * @param alpha alpha bias level
     * @return biased value
     */
    protected static double b_poly(double y, double alpha)
    {
        assert Double.compare(alpha, 0.0d) > 0;
        double result = Math.pow(y, alpha);
        assert Double.compare(result, 0) >= 0;
        return result;
    }

    /**
     * Executes: flat bias.
     *
     * @param y input value
     * @param A technical parameter #1
     * @param B technical parameter #2
     * @param C technical parameter #3
     * @return biased value
     */
    protected static double b_flat(double y, double A, double B, double C)
    {
        assert Double.compare(A, 0.0d) >= 0;
        assert Double.compare(A, 1.0d) <= 0;
        assert Double.compare(B, 0.0d) >= 0;
        assert Double.compare(B, 1.0d) <= 0;
        assert Double.compare(C, 0.0d) >= 0;
        assert Double.compare(C, 1.0d) <= 0;
        assert Double.compare(B, C) < 0;

        if (Double.compare(B, 0.0d) == 0)
        {
            assert Double.compare(A, 0.0d) == 0;
            assert Double.compare(C, 1.0d) != 0;
        }
        if (Double.compare(C, 1.0d) == 0)
        {
            assert Double.compare(A, 1.0d) == 0;
            assert Double.compare(B, 0.0d) != 0;
        }

        double result = A;
        {
            double[] s = {0.0d, Math.floor(y - B)};
            double mul = A * (B - y) / B;
            result += (StatUtils.min(s) * mul);
        }
        {
            double[] s = {0.0d, Math.floor(C - y)};
            double mul = (1.0d - A) * (y - C) / (1.0d - C);
            result -= (StatUtils.min(s) * mul);
        }

        // WFG calculations are prone to numerical errors
        if (Double.compare(result, 0.0d) < 0) result = 0.0d;
        return result;
    }

    /**
     * Executes: param dependent bias.
     *
     * @param y input value
     * @param A technical parameter #1
     * @param B technical parameter #2
     * @param C technical parameter #3
     * @param u technical parameter #4
     * @return biased value
     */
    protected static double b_paramDependant(double y, double A, double B, double C, double u)
    {
        assert Double.compare(A, 0.0d) > 0;
        assert Double.compare(0.0d, B) < 0;
        assert Double.compare(B, C) < 0;
        double v = A - (1.0d - 2.0d * u) * Math.abs(Math.floor(0.5d - u) + A);
        double result = Math.pow(y, B + (C - B) * v);
        assert Double.compare(result, 0) >= 0;
        return result;
    }

    /**
     * Executes: shift linear.
     *
     * @param y input value
     * @param A technical parameter #1
     * @return biased value
     */
    protected static double s_linear(double y, double A)
    {
        assert Double.compare(A, 0.0d) > 0;
        assert Double.compare(A, 1.0d) < 0;
        double nom = Math.abs(y - A);
        double denom = Math.abs(Math.floor(A - y) + A);
        double result = nom / denom;
        assert Double.compare(result, 0) >= 0;
        return result;
    }

    /**
     * Executes: shift deceptive.
     *
     * @param y input value
     * @param A technical parameter #1
     * @param B technical parameter #2
     * @param C technical parameter #3
     * @return biased value
     */
    protected static double s_deceptive(double y, double A, double B, double C)
    {
        assert Double.compare(A, 0.0d) > 0;
        assert Double.compare(A, 1.0d) < 1;
        assert Double.compare(B, 0.0d) > 0;
        assert Double.compare(B, 1.0d) < 1;
        assert Double.compare(C, 0.0d) > 0;
        assert Double.compare(C, 1.0d) < 1;
        assert Double.compare(A - B, 0.0d) > 0;
        assert Double.compare(A + B, 1.0d) < 1;

        double result = 1.0d;

        double mul = (Math.abs(y - A) - B);
        double sum = 0.0d;
        {
            double nom = Math.floor(y - A + B) * (1.0d - C + (A - B) / B);
            double denom = A - B;
            sum += nom / denom;
        }
        {
            double nom = Math.floor(A + B - y) * (1.0d - C + (1.0d - A - B) / B);
            double denom = 1.0d - A - B;
            sum += nom / denom;
        }
        sum += 1.0d / B;
        mul *= sum;
        result += mul;


        assert Double.compare(result, 0) >= 0;
        return result;
    }

    /**
     * Executes: shift multimodal.
     *
     * @param y input value
     * @param A technical parameter #1
     * @param B technical parameter #2
     * @param C technical parameter #3
     * @return biased value
     */
    public static double s_multi(double y, int A, double B, double C)
    {
        assert Double.compare(B, 0.0d) >= 0;
        assert Double.compare((4.0d * A + 2.0d) * Math.PI, 4.0d * B) >= 0;
        assert Double.compare(C, 0.0d) > 0;
        assert Double.compare(C, 1.0d) < 1;

        double arg = (Math.abs(y - C)) / (2.0d * (Math.floor(C - y) + C));

        double ca = (4.0d * A + 2.0d) * Math.PI * (0.5d - arg);
        double nom = 1.0d + Math.cos(ca) + 4.0d * B * Math.pow(arg, 2.0d);
        double denom = B + 2.0d;

        double result = nom / denom;

        assert Double.compare(result, 0) >= 0;
        return result;
    }

    /**
     * Executes: reduction sum.
     *
     * @param y input values
     * @param w input weights
     * @return biased value
     */
    protected static double r_sum(double[] y, double[] w)
    {
        assert y.length - w.length == 0;
        for (double aY : y)
        {
            assert Double.compare(aY, 0.0d) >= 0;
            assert Double.compare(aY, 1.0d) <= 1;
        }

        double result = 0.0d;
        double sum = 0.0d;
        for (int i = 0; i < y.length; i++)
        {
            sum += w[i];
            result += w[i] * y[i];
        }
        result /= sum;
        assert result >= 0.0d;
        return result;
    }

    /**
     * Executes: reduction nonseparability.
     *
     * @param y input values
     * @param A technical parameter #1
     * @return biased value
     */
    protected static double r_nonseparability(double[] y, int A)
    {
        assert A >= 1;
        assert y.length % A == 0;

        double numerator = 0.0d;

        for (int j = 0; j < y.length; j++)
        {
            numerator += y[j];
            for (int k = 0; k <= A - 2; k++)
                numerator += Math.abs(y[j] - y[(j + k + 1) % y.length]);
        }
        double tmp = Math.ceil(A / 2.0d);
        double denominator = y.length * tmp * (1.0d + 2.0d * A - 2.0d * tmp) / A;
        double result = numerator / denominator;
        assert result >= 0.0d;
        return result;
    }

}
