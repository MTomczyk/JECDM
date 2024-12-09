package problem.moo.dtlz.evaluate;

import phase.IEvaluate;
import population.Specimen;

import java.util.ArrayList;

/**
 * Abstract class that supports evaluation of specimens as imposed by DTLZ benchmarks.
 *
 * @author MTomczyk
 */


public abstract class DTLZEvaluate implements IEvaluate
{
    /**
     * The number of position-related variables (the number of objectives = _P + 1)
     */
    protected final int _P;

    /**
     * The number of distance-related variables.
     */
    protected final int _D;

    /**
     * Optional rescaling vector for testing (should be null if the original DTLZ problem is to be used).
     * If used, every i-th objective function value is rescaled by i-th scaling factor provided.
     */
    protected double[] _rescale;

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives
     * @param D the number of distance-related parameters
     */
    public DTLZEvaluate(int M, int D)
    {
        this(M, D, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param M       the number of objectives
     * @param D       the number of distance-related parameters
     * @param rescale Optional rescaling vector for testing (should be null if the original DTLZ problem is to be used). If used, every i-th objective function value is rescaled by i-th scaling factor provided.
     */
    public DTLZEvaluate(int M, int D, double[] rescale)
    {
        _P = M - 1;
        _D = D;
        _rescale = rescale;
    }

    /**
     * Evaluates specimens.
     *
     * @param specimens array of specimens to be evaluated
     */
    @Override
    public void evaluateSpecimens(ArrayList<Specimen> specimens)
    {
        for (Specimen specimen : specimens)
        {
            double[] x = specimen.getDoubleDecisionVector();
            double[] f = evaluate(x);
            specimen.setEvaluations(f);
        }
    }

    /**
     * Supportive method for calculating the objective vector.
     *
     * @param x decision vector
     * @return objective vector
     */
    protected double[] evaluate(double[] x)
    {
        return null;
    }
}
