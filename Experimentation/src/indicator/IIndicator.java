package indicator;

import ea.EA;
import exception.TrialException;
import scenario.Scenario;

/**
 * Interface for classes responsible for assessing the performance of an EA given the current state (population, generation, etc.)
 * Note that the indicators are (by default) established at the scenario level.
 *
 * @author MTomczyk
 */
public interface IIndicator
{
    /**
     * Each indicator must implement this factory-like method. After establishing the indicator at the scenario level,
     * each dispatched trial executor receives a copy. This allows each instance to maintain its own trial-dependent data.
     * Effectively, this method intends to clone the reference object in its initial state.
     *
     * @param scenario scenario being currently processed
     * @param trialID  ID of a trial being processed
     * @return new instance (clear, unprocessed) of the indicator
     * @throws TrialException trial-level exception can be thrown 
     */
    IIndicator getInstance(Scenario scenario, int trialID) throws TrialException;

    /**
     * The main method for performance evaluation.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    double evaluate(EA ea) throws TrialException;

    /**
     * Method for identifying preference direction.
     *
     * @return preference direction (if true, less is preferred; if true, more is preferred)
     */
    boolean isLessBetter();

    /**
     * Returns the indicator's name. Note that the name should obey the same naming rules as the titles of the scenarios' keys and values.
     *
     * @return the indicator's name
     */
    String getName();

    /**
     * The method for clearing the data.
     */
    void dispose();

    /**
     * The implementation must overwrite the toString() method.
     */
    @Override
    String toString();
}
