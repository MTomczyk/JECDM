package interaction.refine.filters.reduction;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import exeption.RefinerException;

/**
 * Abstract implementation of {@link IReductionFilter}. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
public class AbstractReductionFilter implements IReductionFilter
{
    /**
     * Filter name.
     */
    protected final String _name;

    /**
     * Parameterized constructor.
     *
     * @param name                 filter name
     */
    public AbstractReductionFilter(String name)
    {
        _name = name;
    }

    /**
     * Default implementation passes all alternatives (via new array).
     *
     * @param dmContext          current decision-making context
     * @param processedSet upper (super) set
     * @return input alternatives wrapped via a new array
     * @throws RefinerException exception can be thrown and propagated higher
     */
    @Override
    public AbstractAlternatives<?> reduce(DMContext dmContext, AbstractAlternatives<?> processedSet) throws RefinerException
    {
        validate(dmContext, processedSet);
        return processedSet.getCopy();
    }

    /**
     * Auxiliary method for performing basic validation.
     *
     * @param dmContext          current decision-making context
     * @param alternatives processed superset of alternatives
     * @throws RefinerException exception can be thrown and propagated higher
     */
    protected void validate(DMContext dmContext, AbstractAlternatives<?> alternatives) throws RefinerException
    {
        if (dmContext == null)
            throw new RefinerException("The decision-making context is not provided", this.getClass());
        if (dmContext.getCriteria() == null)
            throw new RefinerException("The criteria are not provided", this.getClass());
        if (alternatives == null)
            throw new RefinerException("The alternatives set is not provided (the array is null)", this.getClass());
    }

    /**
     * Returns the filter's string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _name;
    }

}
