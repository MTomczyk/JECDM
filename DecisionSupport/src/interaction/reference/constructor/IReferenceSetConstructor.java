package interaction.reference.constructor;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;

import java.util.LinkedList;

/**
 * Interface for classes responsible for preparing a reference set (or sets) given the input filtered alternatives.
 *
 * @author MTomczyk
 */
public interface IReferenceSetConstructor
{
    /**
     * Implemented method should notify about the reference set size the main construction procedure will create.
     * The preference elicitation module will trigger the termination if there are not enough refined alternatives to
     * produce the reference set of the specified size.
     *
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @return expected size of the reference set
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    int getExpectedSize(AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException;

    /**
     * Main method for constructing a reference set (or sets). The method is supposed to return null if no valid
     * reference sets can be constructed (the elements of the output list cannot be null)
     *
     * @param dmContext                  current decision-making context
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @return constructed reference set (or sets); should return null if no such sets were generated (the elements cannot be null)
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException;

    /**
     * Should return a string representation.
     *
     * @return string representation
     */
    String toString();
}
