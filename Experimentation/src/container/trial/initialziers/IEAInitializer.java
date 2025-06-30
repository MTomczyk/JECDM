package container.trial.initialziers;

import container.trial.AbstractTrialDataContainer;
import ea.EA;
import exception.TrialException;
import random.IRandom;

/**
 * Interface for objects responsible for generating per-trial instances of Evolutionary Algorithms.
 *
 * @author MTomczyk
 */
public interface IEAInitializer
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
    EA instantiateEA(IRandom R, AbstractTrialDataContainer.Params p) throws TrialException;
}
