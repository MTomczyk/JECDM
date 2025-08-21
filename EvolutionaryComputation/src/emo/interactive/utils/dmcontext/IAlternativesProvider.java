package emo.interactive.utils.dmcontext;

import alternative.AbstractAlternatives;
import ea.EA;
import ea.IEA;

/**
 * Interface for classes responsible for delivering alternatives to {@link AbstractDMCParamsConstructor}.
 *
 * @author MTomczyk
 */
public interface IAlternativesProvider
{
    /**
     * The main method.
     * @param ea evolutionary algorithm
     * @return alternatives superset to be used within {@link dmcontext.DMContext} being built.
     */
    AbstractAlternatives<?> getAlternatives(IEA ea);
}
