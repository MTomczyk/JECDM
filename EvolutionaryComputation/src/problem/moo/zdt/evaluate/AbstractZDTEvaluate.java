package problem.moo.zdt.evaluate;

import phase.IEvaluate;
import population.Specimen;

import java.util.ArrayList;

/**
 * Abstract implementation for the evaluators linked to the ZDT bundle.
 *
 * @author MTomczyk
 */
public abstract class AbstractZDTEvaluate implements IEvaluate
{
    /**
     * Auxiliary constant multiplier.
     */
    protected final double _cMul;

    /**
     * Parameterized constructor.
     *
     * @param n the number of decision variables
     */
    protected AbstractZDTEvaluate(int n)
    {
        _cMul = 9.0d / (n - 1.0d);
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
