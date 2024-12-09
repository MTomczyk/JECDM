package interaction.reference.constructor;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;
import interaction.reference.validator.IValidator;

import java.util.LinkedList;

/**
 * Abstract implementation of {@link IReferenceSetConstructor} for constructing pairs of alternatives.
 *
 * @author MTomczyk
 */
class AbstractPairsConstructor implements IReferenceSetConstructor
{
    /**
     * Constructor's name (string representation).
     */
    protected final String _name;

    /**
     * Determines how many pairs are to be generated.
     */
    protected final int _pairs;

    /**
     * Validators used when analysing the e.g., validity of candidate pairs (all must be satisfied).
     */
    protected final IValidator[] _validators;

    /**
     * Parameterized constructor
     *
     * @param name       constructor's name (string representation)
     * @param validators validators used when analysing the e.g., validity of candidate pairs
     * @param pairs      how many pairs are to be generated
     */
    protected AbstractPairsConstructor(String name, IValidator[] validators, int pairs)
    {
        _name = name;
        _pairs = pairs;
        _validators = validators;
    }

    /**
     * Main method for constructing a reference set (or sets).
     *
     * @param dmContext            current decision-making context
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @return constructed reference set (or sets)
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
    {
        if (filteredAlternatives.size() < 2)
            throw new ReferenceSetsConstructorException("There are not enough filtered alternatives (should be at least 2)", this.getClass());
        LinkedList<ReferenceSet> sets = new LinkedList<>();
        for (int p = 0; p < _pairs; p++)
        {
            ReferenceSet RS = constructSet(dmContext, filteredAlternatives, p);
            if (RS != null) sets.add(RS);
        }

        if (sets.isEmpty()) return null;
        return sets;
    }

    /**
     * A method for constructing reference sets (should be overwritten). This default implementation just takes the
     * first two filtered alternatives to create the reference set.
     *
     * @param dmContext            current decision-making context
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @param p                    iteration number
     * @return constructed reference set (or sets)
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    protected ReferenceSet constructSet(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives, int p) throws ReferenceSetsConstructorException
    {
        return new ReferenceSet(filteredAlternatives.get(0), filteredAlternatives.get(1));
    }

    /**
     * Implemented method should notify about the reference set size the main construction procedure will create.
     * This implementation always returns 2.
     *
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @return this implementation always returns 2
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public int getExpectedSize(AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
    {
        return 2;
    }

    /**
     * Auxiliary method for validating the candidate pair of alternatives (given the validators conditions).
     *
     * @param dmContext current decision-making context
     * @param a1        the first alternative
     * @param a2        the second alternative
     * @return true if the pair is valid, false otherwise
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    protected boolean validatePair(DMContext dmContext, Alternative a1, Alternative a2) throws ReferenceSetsConstructorException
    {
        if (_validators == null) return true;
        for (IValidator c : _validators)
            if ((c != null) && (!c.isValid(dmContext, a1, a2))) return false;
        return true;
    }

    /**
     * Returns a string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _name;
    }
}
