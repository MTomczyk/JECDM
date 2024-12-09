package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common;

import exception.PhaseException;
import phase.IEvaluate;
import population.Specimen;

import java.util.ArrayList;

/**
 * This evaluator calculates the performance as the sum of the kernel function values (the specimens' decision vectors
 * are [x1, x2] coordinates in [0, 1] ranges).
 *
 * @author MTomczyk
 */


public class ContEvaluator implements IEvaluate
{
    /**
     * Kernel functions (to be summed).
     */
    private final Kernel[] _kernels;

    /**
     * Parameterized constructor.
     *
     * @param kernels functions (to be summed).
     */
    public ContEvaluator(Kernel[] kernels)
    {
        _kernels = kernels;
    }

    /**
     * Evaluates specimens.
     *
     * @param specimens array of specimens to be evaluated
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void evaluateSpecimens(ArrayList<Specimen> specimens) throws PhaseException
    {
        for (Specimen s : specimens)
        {
            double [] x = s.getDoubleDecisionVector();
            double fitness = Utils.evaluatePoint(x[0], x[1], _kernels);
            s.setEvaluations(new double[]{fitness});
            s.setAuxScore(fitness);
        }
    }
}
