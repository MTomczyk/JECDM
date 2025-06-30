package container.trial.initialziers;

import container.trial.AbstractTrialDataContainer;
import ea.EA;
import exception.TrialException;
import random.IRandom;

/**
 * Default implementation of {@link IInstanceGetter}.
 *
 * @author MTomczyk
 */
public class DefaultEAInitializer implements IEAInitializer
{
    /**
     * Creates and returns an instance of per-trial evolutionary algorithm.
     *
     * @param R random number generator passed directly from GDC; this is a remainder that one, single instance of
     *          a generator should be used throughout the experimentation
     * @param p params container
     * @return per-trial evolutionary algorithm
     * @throws TrialException trial-level exception can be thrown 
     */
    @Override
    public EA instantiateEA(IRandom R, AbstractTrialDataContainer.Params p) throws TrialException
    {
        return null;
    }
}
