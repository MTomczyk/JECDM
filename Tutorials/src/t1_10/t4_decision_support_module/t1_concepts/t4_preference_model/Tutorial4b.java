package t1_10.t4_decision_support_module.t1_concepts.t4_preference_model;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import exeption.PreferenceModelException;
import model.IPreferenceModel;
import model.evaluator.AbstractEvaluator;
import model.evaluator.EvaluationResult;
import model.evaluator.IEvaluator;
import model.internals.value.scalarizing.LNorm;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import java.util.ArrayList;

/**
 * This tutorial focuses on the {@link IPreferenceModel} interface ({@link model.definitions.LNorm}).
 *
 * @author MTomczyk
 */
public class Tutorial4b
{
    /**
     * Let's create our own evaluator, which calculates the final evaluation as the average of scores each internal model provides.
     */
    public static class Average extends AbstractEvaluator<LNorm> implements IEvaluator<LNorm>
    {
        /**
         * Main evaluation phase (calculates the average score).
         *
         * @param ER           evaluation result to be filled
         * @param alternatives alternatives to be evaluated
         * @param models       internal models used for evaluation
         * @throws PreferenceModelException the exception can be thrown and propagated higher
         */
        @Override
        protected void mainEvaluationPhase(EvaluationResult ER, AbstractAlternatives<?> alternatives, ArrayList<LNorm> models) throws PreferenceModelException
        {
            double[] e = new double[alternatives.size()];
            for (int i = 0; i < alternatives.size(); i++)
            {
                Alternative a = alternatives.get(i);
                for (LNorm lNorm : models) e[i] += lNorm.evaluate(a);
                e[i] /= models.size();
            }
            ER._evaluations = e;
        }
    }


    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        IPreferenceModel<LNorm> model = new model.definitions.LNorm(new Average());

        //  Use two internal model (this time, provide no normalizations, we will simulate their update later):
        LNorm lnorm1 = new LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY);
        LNorm lnorm2 = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);
        ArrayList<LNorm> internals = new ArrayList<>();
        internals.add(lnorm1);
        internals.add(lnorm2);

        // Normalizations:
        INormalization[] normalizations = new INormalization[]{new Linear(0.0d, 4.0d), new Linear(0.0d, 2.0d)};

        // Set internal models:
        model.setInternalModels(internals);

        // Simulate the update (typically, it is done automatically by the system using the context's normalizations
        // (only when the _osChanged flag is true).
        for (LNorm lNorm : model.getInternalModels())
            lNorm.setNormalizations(normalizations);

        int no = 11;
        double[][] e = new double[11][2];
        for (int i = 0; i < no; i++)
        {
            e[i][0] = (double) i / (no - 1);
            e[i][1] = 1.0d - e[i][0];
            e[i][0] *= 4.0d; // upscale to original space
            e[i][1] *= 2.0d; // upscale to original space
        }
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        try
        {
            System.out.println("Model = " + model.getName());
            System.out.println("Less is better = " + model.isLessPreferred());
            EvaluationResult result = model.evaluateAlternatives(new Alternatives(alternatives));
            System.out.println(result.toString());

        } catch (PreferenceModelException ex)
        {
            throw new RuntimeException(ex);
        }

    }
}
