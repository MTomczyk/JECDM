package indicator.initializers;

import exception.TrialException;
import indicator.IIndicator;
import scenario.Scenario;

/**
 * Interface for indicator's initializers (getInstance() of {@link indicator.IIndicator} delegates to the initializer).
 *
 * @author MTomczyk
 */
public interface IIndicatorInitializer
{
    /**
     * Main method for creating object instance (getInstance() of {@link indicator.IIndicator} delegates to the initializer).
     *
     * @param scenario scenario being currently processed
     * @param trialID  ID of a trial being processed
     * @return new instance (clear, unprocessed) of the indicator
     * @throws TrialException trial-level exception can be thrown 
     */
    IIndicator getInstance(Scenario scenario, int trialID) throws TrialException;
}
