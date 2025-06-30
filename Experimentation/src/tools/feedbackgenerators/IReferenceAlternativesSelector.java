package tools.feedbackgenerators;

import alternative.Alternative;
import random.IRandom;
import scenario.Scenario;

import java.util.ArrayList;

/**
 * Auxiliary interface for objects pointing alternatives (reference alternatives) to be used to form a feedback.
 *
 * @author MTomczyk
 */
public interface IReferenceAlternativesSelector
{
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
    int[][] getAlternativesIndices(Scenario scenario, int t, IRandom R, ArrayList<ArrayList<Alternative>> sortedAlternatives);
}
