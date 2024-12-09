package container.trial.initialziers;

import container.trial.AbstractTrialDataContainer;
import exception.TrialException;
import problem.AbstractProblemBundle;

/**
 * Object reponsible for determining the per-scenario problem to be solved.
 *
 * @author MTomczyk
 */
public interface IProblemBundleInitializer
{
    /**
     * Instantiates the problem to be solved.
     *
     * @param p          params container
     * @return instantiated problem bundle
     * @throws TrialException the signature allows the overwriting method to cast exceptions
     */
    AbstractProblemBundle instantiateProblem(AbstractTrialDataContainer.Params p) throws TrialException;
}
