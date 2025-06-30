package container.trial.initialziers;

import container.trial.AbstractTrialDataContainer;
import ea.EA;
import exception.TrialException;
import runner.IRunner;
import runner.Runner;

/**
 * Default implementation of {@link IRunnerInitializer}.
 *
 * @author MTomczyk
 */
public class DefaultRunnerInitializer implements IRunnerInitializer
{
    /**
     * Creates and returns an instance of per-trial runner reponsible for executing the evolutionary process.
     *
     * @param ea instance of the evolutionary algorithm to be processed
     * @param p  params container
     * @return per-trial runner
     * @throws TrialException trial-level exception can be thrown 
     */
    @Override
    public IRunner instantiateRunner(EA ea, AbstractTrialDataContainer.Params p) throws TrialException
    {
        try
        {
            EA[] eas = new EA[]{ea};
            int steadyStateRepeats = p._SDC.getSteadyStateRepeats();
            Runner.Params pR = new Runner.Params(eas, steadyStateRepeats);
            pR._visualization = null;
            pR._displayMode = null;
            pR._updaterMode = null;
            return new Runner(pR);
        } catch (Exception e)
        {
            throw new TrialException(e.getMessage(), this.getClass(), e, p._SDC.getScenario(), p._trialID);
        }
    }
}
