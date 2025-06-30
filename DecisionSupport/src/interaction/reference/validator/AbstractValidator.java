package interaction.reference.validator;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;

import java.util.ArrayList;

/**
 * Abstract implementation of {@link IValidator}. Its implemented methods are always favor creating a reference set.
 *
 * @author MTomczyk
 */
public class AbstractValidator implements IValidator
{
    /**
     * Returns true (delegates to {@link AbstractValidator#isValid(DMContext, AbstractAlternatives)}.
     * @param dmContext current decision-making context
     * @param a1 the first alternative
     * @param a2 the second alternative
     * @return true
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */

    @Override
    public boolean isValid(DMContext dmContext, Alternative a1, Alternative a2) throws ReferenceSetsConstructorException
    {
        ArrayList<Alternative> alternatives = new ArrayList<>();
        alternatives.add(a1);
        alternatives.add(a2);
        return isValid(dmContext, new Alternatives(alternatives));
    }

    /**
     * Returns true.
     * @param dmContext current decision-making context
     * @param alternatives wrapped alternatives
     * @return true
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    @Override
    public boolean isValid(DMContext dmContext, AbstractAlternatives<?> alternatives) throws ReferenceSetsConstructorException
    {
        return true;
    }
}
