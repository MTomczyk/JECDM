package tools.feedbackgenerators;

import scenario.Scenario;

/**
 * Auxiliary interface for classes responsible for providing the number of interactions to be simulated for scenarios.
 *
 * @author MTomczyk
 */
public interface INoInteractionsProvider
{
    /**
     * The main method. Returns the number of interactions given an input scenario definition.
     *
     * @param scenario scenario definition
     * @return the number of interactions (at least 0)
     */
    int getNoInteractions(Scenario scenario);
}
