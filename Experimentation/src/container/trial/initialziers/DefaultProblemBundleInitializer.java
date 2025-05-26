package container.trial.initialziers;

import container.trial.AbstractTrialDataContainer;
import exception.TrialException;
import problem.AbstractProblemBundle;

/**
 * Default implementation of {@link IProblemBundleInitializer}.
 *
 * @author MTomczyk
 */
public class DefaultProblemBundleInitializer implements IProblemBundleInitializer
{
    /**
     * Instantiates the problem to be solved.
     *
     * @param p params container
     * @throws TrialException the signature allows the overwriting method to cast exceptions
     */
    @Override
    public AbstractProblemBundle instantiateProblem(AbstractTrialDataContainer.Params p) throws TrialException
    {
        try
        {
            int objectives = p._SDC.getObjectives();
            String problem = p._SDC.getProblemID();
            if (problem == null) return null;
            return ProblemBundleGetter.getProblemBundle(problem, objectives);

        } catch (Exception e)
        {
            throw new TrialException(e.getMessage(), null, this.getClass(), p._SDC.getScenario(), p._trialID);
        }
    }
}
