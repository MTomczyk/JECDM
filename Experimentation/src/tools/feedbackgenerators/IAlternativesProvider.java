package tools.feedbackgenerators;

import alternative.Alternative;
import random.IRandom;
import scenario.Scenario;

import java.util.ArrayList;

/**
 * Auxiliary interface for objects responsible for providing reference sets of alternatives given a scenario being processed.
 *
 * @author MTomczyk
 */
public interface IAlternativesProvider
{
    /**
     * The main method.
     *
     * @param scenario       scenario being currently processed
     * @param noInteractions the expected number of interactions with the DM
     * @param R              random number generator
     * @return alternatives set(s); IMPORTANT: the first dimension refers to different interaction numbers
     * (index of 0 refers to the first interaction, of 1 to the second, etc.); the second dimension represents
     * associated alternatives.
     */
    ArrayList<ArrayList<Alternative>> getAlternatives(Scenario scenario, int noInteractions, IRandom R);
}
