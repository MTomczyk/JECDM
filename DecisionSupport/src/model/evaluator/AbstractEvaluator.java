package model.evaluator;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import exeption.PreferenceModelException;
import model.internals.IInternalModel;

import java.util.ArrayList;

/**
 * Abstract implementation of {@link IEvaluator} (provides common fields and functionalities).
 *
 * @author MTomczyk
 */
public class AbstractEvaluator<T extends IInternalModel> implements IEvaluator<T>
{
    /**
     * Alternatives superset.
     */
    protected AbstractAlternatives<?> _alternativesSuperset = null;

    /**
     * Auxiliary method that can be used to register alternatives for evaluation. Note that it is supposed that the
     * alternatives registered via this method constitute a superset (or at least equal set) to the alternatives
     * evaluated via {@link IEvaluator#evaluateAlternatives(AbstractAlternatives, ArrayList)}.
     *
     * @param alternativesSuperset alternatives to be evaluated
     * @throws PreferenceModelException the exception can be thrown 
     */
    @Override
    public void registerAlternativesSuperset(AbstractAlternatives<?>  alternativesSuperset) throws PreferenceModelException
    {
        if (alternativesSuperset == null) throw new PreferenceModelException("The input alternatives superset is null", this.getClass());
        _alternativesSuperset = alternativesSuperset;
    }

    /**
     * Auxiliary method that can be used to unregister the alternatives' superset.
     */
    @Override
    public void unregisterAlternativesSuperset()
    {
        _alternativesSuperset = null;
    }

    /**
     * The main method for evaluating an alternative. Note that it is preferred to use {@link IEvaluator#evaluateAlternatives(AbstractAlternatives, ArrayList)}}
     * as it may speed up calculations, may be the only implemented method, or can provide additional evaluation results
     * (implementation dependent).
     *
     * @param alternative the alternative to be evaluated
     * @param models      internal models used for evaluation
     * @return attained score
     * @throws PreferenceModelException the exception can be thrown 
     */
    @Override
    public double evaluate(Alternative alternative, ArrayList<T> models) throws PreferenceModelException
    {
        return 0;
    }

    /**
     * The main method for evaluating an alternative. This method is divided into sequentially executed pre
     * ({@link AbstractEvaluator#preEvaluationPhase(AbstractAlternatives, ArrayList)}),
     * main ({@link AbstractEvaluator#mainEvaluationPhase(EvaluationResult, AbstractAlternatives, ArrayList)}),
     * and post-phases ({@link AbstractEvaluator#postEvaluationPhase(EvaluationResult, AbstractAlternatives, ArrayList)}),
     * which can be suitably overwritten to serve various purposes.
     *
     * @param alternatives alternatives to be evaluated
     * @param models       internal models used for evaluation
     * @return attained scores (each element corresponds to a different alternative, 1:1 connection)
     * @throws PreferenceModelException the exception can be thrown 
     */
    @Override
    public EvaluationResult evaluateAlternatives(AbstractAlternatives<?> alternatives, ArrayList<T> models) throws PreferenceModelException
    {
        EvaluationResult ER = preEvaluationPhase(alternatives, models);
        mainEvaluationPhase(ER, alternatives, models);
        postEvaluationPhase(ER, alternatives, models);
        return ER;
    }


    /**
     * Pre-evaluation phase.
     *
     * @param alternatives alternatives to be evaluated
     * @param models       internal models used for evaluation
     * @return attained scores (each element corresponds to a different alternative, 1:1 connection)
     * @throws PreferenceModelException the exception can be thrown 
     */
    protected EvaluationResult preEvaluationPhase(AbstractAlternatives<?> alternatives, ArrayList<T> models) throws PreferenceModelException
    {
        EvaluationResult ER = new EvaluationResult();
        ER._startTime = System.nanoTime();
        return ER;
    }

    /**
     * Main evaluation phase.
     *
     * @param ER evaluation result to be filled
     * @param alternatives alternatives to be evaluated
     * @param models       internal models used for evaluation
     * @throws PreferenceModelException the exception can be thrown 
     */
    protected void mainEvaluationPhase(EvaluationResult ER, AbstractAlternatives<?> alternatives, ArrayList<T> models) throws PreferenceModelException
    {
        double[] e = new double[alternatives.size()];
        for (int i = 0; i < alternatives.size(); i++) e[i] = evaluate(alternatives.get(i), models);
        ER._evaluations = e;
    }

    /**
     * Post evaluation phase
     *
     * @param ER evaluation result to be filled
     * @param alternatives alternatives to be evaluated
     * @param models       internal models used for evaluation
     * @return attained scores (each element corresponds to a different alternative, 1:1 connection)
     * @throws PreferenceModelException the exception can be thrown 
     */
    protected EvaluationResult postEvaluationPhase(EvaluationResult ER, AbstractAlternatives<?> alternatives, ArrayList<T> models) throws PreferenceModelException
    {
        ER._elapsedTime = (System.nanoTime() - ER._startTime) / 1000000;
        return ER;
    }
}
