package interaction.refine.filters.reduction;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import exeption.RefinerException;

/**
 * Interface for objects responsible for reducing the superset.
 *
 * @author MTomczyk
 */
public interface IReductionFilter
{
    /**
     * The main method that filters out not valid alternatives from the super set.
     *
     * @param dmContext          current decision-making context
     * @param processedSet upper (super) set
     * @return alternatives that passed the filter tests (are valid and can be processed further), it should be a subset of the input set (recommended to make it a different object than the input)
     * @throws RefinerException the exception can be thrown 
     */
    AbstractAlternatives<?> reduce(DMContext dmContext, AbstractAlternatives<?> processedSet) throws RefinerException;
}
