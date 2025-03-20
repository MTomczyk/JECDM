package phase;

import exception.PhaseException;
import population.Specimen;

import java.util.ArrayList;

/**
 * A simple implementation of {@link phase.IEvaluate} that delivers a convenient means for evaluating specimens.
 * The specimens are assumed to be evaluated based on their decision double-vectors. The evaluation is delegated to
 * an implementation of an auxiliary IEvaluate interface.
 *
 * @author MTomczyk
 */
public class DoubleEvaluate implements IEvaluate
{
    /**
     * Supportive interface for evaluating specimens based on their decision double-vectors.
     */
    public interface IEvaluate
    {
        /**
         * The main method's signature.
         *
         * @param v input decision vector (doubles)
         * @return evaluations
         */
        double[] evaluate(double[] v);
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
    public DoubleEvaluate(IEvaluate evaluator)
    {
        _evaluator = evaluator;
    }

    /**
     * The method for evaluating specimens based on their decision vectors (doubles). The evaluation is delegated to
     * an object implementing the supportive {@link IEvaluate} interface.
     *
     * @param specimens array of specimens to be evaluated
     * @throws PhaseException an exception can be thrown and propagated higher
     */
    @Override
    public void evaluateSpecimens(ArrayList<Specimen> specimens) throws PhaseException
    {
        for (Specimen s : specimens) s.setEvaluations(_evaluator.evaluate(s.getDoubleDecisionVector()));
    }
}
