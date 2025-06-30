package tools.feedbackgenerators;

import alternative.Alternative;
import random.IRandom;
import scenario.Scenario;

import java.util.ArrayList;

/**
 * Standard implementation of {@link IReferenceAlternativesSelector}. It constructs indices pointing to pairs of
 * alternatives by generating first equally spaced double values from the bound [0, 1], adjusting them according to
 * the gamma normalization (i.e., [0, 1]^gamma), and then linearly projecting them into the size of the alternatives
 * sets, i.e., [0,1]^gamma into [0, no. alternatives]. Hence, if gamma = 1, equally spaced alternatives from the input
 * set are picked to form reference alternative sets. If gamma is greater than one, the procedure is pushed toward
 * picking more preferred alternatives. If gamma is less than one, less-preferred options are selected.
 *
 * @author MTomczyk
 */
public class GammaIndicesForPCs implements IReferenceAlternativesSelector
{
    /**
     * Parameterized constructor.
     *
     * @param gamma gamma value
     */
    public GammaIndicesForPCs(double gamma)
    {
        _gamma = Math.max(0.0d, gamma);
    }

    /**
     * Gamma value.
     */
    private final double _gamma;

    /**
     * The main method for constructing reference sets.
     *
     * @param scenario           scenario being currently processed
     * @param t                  trial being currently processed
     * @param R                  random number generator
     * @param sortedAlternatives input alternatives set; the first dimension is associated with the interaction number
     *                           (index of 0 = the first interaction; 1 = the second; etc.); sorted = per-interaction
     *                           alternatives sets are already sorted according to the DM's model from the most
     *                           to the least preferred
     * @return matrix of indices pointing to alternatives to be used to form reference sets; the first dimension is
     * associated with the interaction number (index of 0 = the first interaction; 1 = the second; etc.); the second
     * are the per-interaction alternatives indices
     */
    @Override
    public int[][] getAlternativesIndices(Scenario scenario, int t, IRandom R, ArrayList<ArrayList<Alternative>> sortedAlternatives)
    {
        int pcs = sortedAlternatives.size();
        int noAlternatives = sortedAlternatives.get(0).size();
        int[][] idxAt = new int[pcs][2];
        int pc = 0;
        int total = pcs * 2;
        for (int i = 0; i < total; i += 2)
        {
            double v = Math.pow((total - 1 - i) / (double) (total - 1), _gamma);
            idxAt[pc][0] = (int) (v * (noAlternatives - 1) + 0.5d);
            v = Math.pow((total - 1 - (i + 1)) / (double) (total - 1), _gamma);
            idxAt[pc][1] = (int) (v * (noAlternatives - 1) + 0.5d);
            pc++;
        }
        return idxAt;
    }
}
