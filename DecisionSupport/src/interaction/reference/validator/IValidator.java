package interaction.reference.validator;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;

/**
 * Interface for auxiliary classes responsible for validating alternatives and determining their potential for
 * constituting a reference set.
 *
 * @author MTomczyk
 */
public interface IValidator
{
    /**
     * Signature for methods verifying if the two input alternatives can constitute a pair (reference pair).
     * @param dmContext current decision-making context
     * @param a1 the first alternative
     * @param a2 the second alternative
     * @return true, if both alternatives can form a pair, false otherwise
     * @throws ReferenceSetsConstructorException exception can be thrown 
     */
    boolean isValid(DMContext dmContext, Alternative a1, Alternative a2) throws ReferenceSetsConstructorException;

    /**
     * Signature for methods verifying if input alternatives can constitute a reference set.
     * @param dmContext current decision-making context
     * @param alternatives wrapped alternatives
     * @return true, if alternatives can form a reference set, false otherwise
     * @throws ReferenceSetsConstructorException exception can be thrown 
     */
    boolean isValid(DMContext dmContext, AbstractAlternatives<?> alternatives) throws ReferenceSetsConstructorException;
}
