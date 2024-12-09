package interaction.refine.filters.reduction;


import alternative.AbstractAlternatives;
import alternative.Alternatives;
import dmcontext.DMContext;
import exeption.RefinerException;
import relation.dominance.DominanceUtils;

/**
 * Implementation of {@link IReductionFilter} for removing dominated alternatives.
 *
 * @author MTomczyk
 */
public class RemoveDominated extends AbstractReductionFilter implements IReductionFilter
{
    /**
     * Default constructor.
     */
    public RemoveDominated()
    {
        super("Remove duplicated");
    }


    /**
     * The main method that filters out duplicated alternatives (comparison is based on inspecting evaluations, i.e., performance vectors only).
     *
     * @param dmContext          current decision-making context
     * @param processedSet upper (super) set
     * @return alternatives that passed the filter tests (are valid and can be processed further), it should be a subset of the input set (recommended to make it a different object than the input)
     * @throws RefinerException exception can be thrown and propagated higher
     */
    @Override
    public AbstractAlternatives<?> reduce(DMContext dmContext, AbstractAlternatives<?> processedSet) throws RefinerException
    {
        validate(dmContext, processedSet);
        if (processedSet.isEmpty()) processedSet.getCopy();
        return new Alternatives(DominanceUtils.getNonDominatedAlternatives(processedSet, dmContext.getCriteria()));
    }
}
