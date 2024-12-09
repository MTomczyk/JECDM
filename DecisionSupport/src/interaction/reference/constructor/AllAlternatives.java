package interaction.reference.constructor;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;

import java.util.LinkedList;

/**
 * Implementation of {@link IReferenceSetConstructor} for constructing a single, general set that wraps all the filtered alternatives.
 *
 * @author MTomczyk
 */
public class AllAlternatives implements IReferenceSetConstructor
{
    /**
     * The method returns back the size of the input filtered alternatives set.
     *
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @return size of the input filtered alternatives set
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public int getExpectedSize(AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
    {
        if (filteredAlternatives == null)
            throw new ReferenceSetsConstructorException("The filtered alternatives array is null", this.getClass());
        return filteredAlternatives.size();
    }

    /**
     * The method constructs a single, general set that wraps all the filtered alternatives.
     *
     * @param dmContext                  current decision-making context
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @return wrapped input filtered alternatives
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
    {
        LinkedList<ReferenceSet> r = new LinkedList<>();
        r.add(new ReferenceSet(filteredAlternatives));
        return r;
    }

    /**
     * Returns a string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "All alternatives";
    }
}
