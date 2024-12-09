package t1_10.t4_decision_support_module.t1_concepts.t4_preference_model;

import alternative.Alternative;
import alternative.Alternatives;
import exeption.PreferenceModelException;
import model.IPreferenceModel;
import model.evaluator.EvaluationResult;
import model.evaluator.IEvaluator;
import model.evaluator.RepresentativeModel;
import model.internals.value.scalarizing.LNorm;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import java.util.ArrayList;

/**
 * This tutorial focuses on the {@link model.IPreferenceModel} interface ({@link model.definitions.LNorm}).
 *
 * @author MTomczyk
 */
public class Tutorial4a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create the model evaluator: representative = alternative will be evaluated as imposed by the first internal
        // model stored (remaining ones will be ignored)
        IEvaluator<LNorm> representative = new RepresentativeModel<>();

        // Create the model definition:
        IPreferenceModel<LNorm> model = new model.definitions.LNorm(representative);

        // Create the internal model (parameterization):
        LNorm lnorm = new LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY,
                new INormalization[]{new Linear(0.0d, 4.0d), new Linear(0.0d, 2.0d)});

        // Set the internal model (single model):
        model.setInternalModel(lnorm);

        // Create the artificial data (in the normalized space first, but the code then does the ``upscaling'' (so that
        // the used normalization objects will be relevant).
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

        // Evaluate the alternatives and print the result:
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
