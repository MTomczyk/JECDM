package y2025.ERS.common;

import alternative.Alternative;
import exeption.PreferenceModelException;
import history.PreferenceInformationWrapper;
import model.IPreferenceModel;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.internals.value.scalarizing.LNorm;
import preference.indirect.PairwiseComparison;
import random.IRandom;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Provides common functionalities.
 *
 * @author MTomczyk
 */
public class Common
{
    /**
     * Creates and returns an auxiliary alpha to index mapping.
     *
     * @return alpha to index mapping
     */
    public static HashMap<Double, Integer> getMapAlphaIndex()
    {
        HashMap<Double, Integer> mapAlphaIndex = new HashMap<>();
        mapAlphaIndex.put(1.0d, 0);
        mapAlphaIndex.put(5.0d, 1);
        mapAlphaIndex.put(Double.POSITIVE_INFINITY, 2);
        return mapAlphaIndex;
    }

    /**
     * Creates and returns an auxiliary objectives to index mapping.
     *
     * @return objectives to index mapping
     */
    public static HashMap<Integer, Integer> getMapObjectivesIndex()
    {
        HashMap<Integer, Integer> mapObjectivesIndex = new HashMap<>();
        mapObjectivesIndex.put(2, 0);
        mapObjectivesIndex.put(3, 1);
        mapObjectivesIndex.put(4, 2);
        mapObjectivesIndex.put(5, 3);
        return mapObjectivesIndex;
    }

    /**
     * Converts per-trial data on pairwise comparisons into relevant preference information objects
     *
     * @param trialPCs reference data
     * @param noPCs    no pairwise comparisons to construct
     * @return preference information objects
     * @throws PreferenceModelException exception can be thrown and propagated higher
     */
    public static LinkedList<PreferenceInformationWrapper> getPreferenceInformation(PCsDataContainer.TrialPCs trialPCs, int noPCs) throws PreferenceModelException
    {
        IPreferenceModel<LNorm> model = new model.definitions.LNorm(new LNorm(trialPCs._dmW, trialPCs._dmA, null));
        LinkedList<PreferenceInformationWrapper> pi = new LinkedList<>();
        for (int i = 0; i < noPCs; i++)
        {
            Alternative A1 = new Alternative("A1_" + i, trialPCs._referenceEvaluations[i][0]);
            Alternative A2 = new Alternative("A2_" + i, trialPCs._referenceEvaluations[i][1]);
            double e1 = model.evaluate(A1);
            double e2 = model.evaluate(A2);
            int comp = Double.compare(e1, e2);
            if (comp == 0) throw new PreferenceModelException("Alternatives should not be evaluated equally", null);
            if (comp < 0)
                pi.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(A1, A2)));
            else pi.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(A2, A1)));
        }
        return pi;
    }

    /**
     * Creates and returns random initial models.
     *
     * @param N     the number of models to construct
     * @param M     the number of objectives
     * @param alpha alpha value
     * @param R     random number generator
     * @return random initial models
     */
    public static LNorm[] getInitialRandomModels(int N, int M, double alpha, IRandom R)
    {
        LNorm[] initialModels = new LNorm[N];
        IRandomModel<LNorm> randomModel = new LNormGenerator(M, alpha);
        for (int i = 0; i < N; i++) initialModels[i] = randomModel.generateModel(R);
        return initialModels;
    }
}
