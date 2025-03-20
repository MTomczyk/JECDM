package phase;

import exception.PhaseException;
import population.Specimen;

import java.util.ArrayList;

/**
 * A simple implementation of {@link phase.IEvaluate} that delivers a convenient means for evaluating specimens.
 * The specimens are assumed to be evaluated based on their decision int-vectors. The evaluation is delegated to
 * an implementation of an auxiliary IEvaluate interface.
 *
 * @author MTomczyk
 */
public class IntEvaluate implements IEvaluate
{
    /**
     * Supportive interface for evaluating specimens based on their decision int-vectors.
     */
    public interface IEvaluate
    {
        /**
         * The main method's signature.
         *
         * @param v input decision vector (integers)
         * @return evaluations
         */
        double[] evaluate(int[] v);
    }

    /**
     * Object responsible for evaluating initial decision vectors.
     */
    private final IEvaluate _evaluator;

    /**
     * Parameterized constructor.
     *
     * @param evaluator object responsible for evaluating initial decision vectors
     */
    public IntEvaluate(IEvaluate evaluator)
    {
        _evaluator = evaluator;
    }

    /**
     * The method for evaluating specimens based on their decision vectors (integers). The evaluation is delegated to
     * an object implementing the supportive {@link IEvaluate} interface.
     *
     * @param specimens array of specimens to be evaluated
     * @throws PhaseException an exception can be thrown and propagated higher
     */
    @Override
    public void evaluateSpecimens(ArrayList<Specimen> specimens) throws PhaseException
    {
        for (Specimen s : specimens) s.setEvaluations(_evaluator.evaluate(s.getIntDecisionVector()));
    }
}
