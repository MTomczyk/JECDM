package interaction.refine.filters.termination;

import dmcontext.DMContext;
import exeption.RefinerException;

/**
 * Abstract implementation of {@link ITerminationFilter}. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
public abstract class AbstractTerminationFilter implements ITerminationFilter
{
    /**
     * Filter name.
     */
    protected final String _name;

    /**
     * Parameterized constructor.
     *
     * @param name filter name
     */
    public AbstractTerminationFilter(String name)
    {
        _name = name;
    }

    /**
     * The method verifying whether the reference sets construction process should be terminated.
     *
     * @param dmContext current decision-making context
     * @return filter's indications
     * @throws RefinerException the exception can be thrown 
     */
    @Override
    public TerminationResult shouldTerminate(DMContext dmContext) throws RefinerException
    {
        return new TerminationResult(false, "Abstract implementation used");
    }

    /**
     * Auxiliary method for performing basic validation.
     *
     * @param dmContext current decision-making context
     * @throws RefinerException the exception can be thrown 
     */
    protected void validate(DMContext dmContext) throws RefinerException
    {
        if (dmContext == null)
            throw new RefinerException("The decision-making context is not provided", this.getClass());
        if (dmContext.getCurrentAlternativesSuperset() == null)
            throw new RefinerException("The alternatives set is not provided (the array is null)", this.getClass());
        if (dmContext.getCriteria() == null)
            throw new RefinerException("The criteria are not provided", this.getClass());
    }

    /**
     * Returns the filter's string representation.
     *
     * @return string representation
     */
    public String toString()
    {
        return _name;
    }
}
