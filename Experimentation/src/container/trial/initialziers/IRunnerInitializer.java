package container.trial.initialziers;

import container.trial.AbstractTrialDataContainer;
import ea.EA;
import exception.TrialException;
import runner.IRunner;

/**
 * Interface for objects responsible for generating per-trial instances of runners objects reponsible for executing
 * the evolutionary process.
 *
 * @author MTomczyk
 */
public interface IRunnerInitializer
{
    /**
     * Creates and returns an instance of per-trial runner reponsible for executing the evolutionary process.
     *
     * @param ea instance of the evolutionary algorithm to be processed
     * @param p  params container
     * @return per-trial runner
     * @throws TrialException trial-level exception can be thrown 
     */
    IRunner instantiateRunner(EA ea, AbstractTrialDataContainer.Params p) throws TrialException;
}
