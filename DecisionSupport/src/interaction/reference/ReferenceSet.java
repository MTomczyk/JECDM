package interaction.reference;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import alternative.IAlternativeWrapper;
import exeption.ReferenceSetsConstructorException;
import utils.StringUtils;

import java.util.ArrayList;

/**
 * A wrapper for alternatives that are supposed to be evaluated by the decision maker.
 *
 * @author MTomczyk
 */
public class ReferenceSet
{
    /**
     * Alternatives for evaluation.
     */
    private final AbstractAlternatives<?> _alternatives;

    /**
     * Parameterized constructor (constructs a single reference solution).
     *
     * @param a the alternative to be wrapped
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    public ReferenceSet(Alternative a) throws ReferenceSetsConstructorException
    {
        _alternatives = new Alternatives(a);
        checkSet(_alternatives);
    }

    /**
     * Parameterized constructor (constructs a pair).
     *
     * @param a1 the fist wrapped alternative
     * @param a2 the second wrapped alternative
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    public ReferenceSet(Alternative a1, Alternative a2) throws ReferenceSetsConstructorException
    {
        _alternatives = new Alternatives(a1, a2);
        checkSet(_alternatives);
    }

    /**
     * Parameterized constructor (constructs a triplet).
     *
     * @param a1 the fist wrapped alternative
     * @param a2 the second wrapped alternative\
     * @param a3 the third wrapped alternative
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    public ReferenceSet(Alternative a1, Alternative a2, Alternative a3) throws ReferenceSetsConstructorException
    {
        _alternatives = new Alternatives(a1, a2, a3);
        checkSet(_alternatives);
    }

    /**
     * Parameterized constructor.
     *
     * @param alternatives wrapped alternatives
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    public ReferenceSet(ArrayList<Alternative> alternatives) throws ReferenceSetsConstructorException
    {
        _alternatives = new Alternatives(alternatives);
        checkSet(_alternatives);
    }

    /**
     * Parameterized constructor.
     *
     * @param alternatives wrapped alternatives (uses a copy)
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    public ReferenceSet(AbstractAlternatives<?> alternatives) throws ReferenceSetsConstructorException
    {
        _alternatives = alternatives.getCopy();
        checkSet(_alternatives);
    }


    /**
     * Auxiliary method for checking validity of the reference set.
     *
     * @param alternatives alternatives to be examined
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    protected void checkSet(AbstractAlternatives<?> alternatives) throws ReferenceSetsConstructorException
    {
        if (alternatives == null)
            throw new ReferenceSetsConstructorException("No alternatives are provided (the array is null)", this.getClass());
        if (alternatives.isEmpty())
            throw new ReferenceSetsConstructorException("No alternatives are provided (the array is empty)", this.getClass());
        for (IAlternativeWrapper a : alternatives)
            if (a.getAlternative() == null)
                throw new ReferenceSetsConstructorException("One of the provided alternatives is null", this.getClass());
    }

    /**
     * Getter for the wrapped alternatives.
     *
     * @return wrapped alternatives
     */
    public AbstractAlternatives<?> getAlternatives()
    {
        return _alternatives;
    }

    /**
     * Getter for the reference set size.
     *
     * @return reference set size
     */
    public int getSize()
    {
        return _alternatives.size();
    }

    /**
     * Returns the string representation.
     * @return string representation
     */
    @Override
    public String toString()
    {
        return getStringRepresentation();
    }

    /**
     * Constructs and returns a string representation of this object.
     *
     * @return string representation
     */
    public String getStringRepresentation()
    {
        return getStringRepresentation(0);
    }

    /**
     * Constructs and returns a string representation of this object.
     *
     * @param indent auxiliary indent used when constructing the string
     * @return string representation
     */
    public String getStringRepresentation(int indent)
    {
        String ind = StringUtils.getIndent(indent);
        StringBuilder sb = new StringBuilder();
        sb.append(ind).append("Alternatives = ");
        for (int i = 0; i < _alternatives.size(); i++)
        {
            sb.append(_alternatives.get(i).getName());
            if (i < _alternatives.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
