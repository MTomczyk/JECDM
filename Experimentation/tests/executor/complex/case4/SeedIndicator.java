package executor.complex.case4;

import ea.EA;
import exception.TrialException;
import indicator.IIndicator;
import scenario.Scenario;

/**
 * Returns RNG's seed.
 *
 * @author MTomczyk
 */
class SeedIndicator implements IIndicator
{
    /**
     * Each indicator must implement this factory-like method. After establishing the indicator at the scenario level,
     * each dispatched trial executor receives a copy. This allows each instance to maintain its own trial-dependent data.
     * Effectively, this method intends to clone the reference object in its initial state.
     *
     * @param scenario scenario being currently processed
     * @param trialID  ID of a trial being processed
     * @return new instance (clear, unprocessed) of the indicator
     * @throws TrialException trial-level exception can be thrown and propagated higher
     */
    @Override
    public IIndicator getInstance(Scenario scenario, int trialID) throws TrialException
    {
        return new SeedIndicator();
    }

    /**
     * The main method for performance evaluation.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    @Override
    public double evaluate(EA ea) throws TrialException
    {
        return ea.getR().getSeed();
    }

    /**
     * Method for identifying preference direction.
     *
     * @return preference direction (if true, less is preferred; if true, more is preferred)
     */
    @Override
    public boolean isLessBetter()
    {
        return false;
    }

    /**
     * Returns the indicator's name. Note that the name should obey the same naming rules as the titles of the scenarios' keys and values.
     *
     * @return the indicator's name
     */
    @Override
    public String getName()
    {
        return "SEED";
    }

    /**
     * The method for clearing the data.
     */
    @Override
    public void dispose()
    {

    }
}
