package interaction.reference.validator;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import exeption.RefinerException;

/**
 * This validator is founded on {@link interaction.refine.filters.termination.RequiredSpread}. It checks if all
 * alternatives are encapsulated in a small, rectangular part of the normalized objective space. If so, the
 * alternatives cannot form a reference set. Otherwise, they can.
 *
 * @author MTomczyk
 */
public class RequiredSpread extends AbstractValidator implements IValidator
{
    /**
     * Wrapped required spread object.
     */
    private final interaction.refine.filters.termination.RequiredSpread _RS;

    /**
     * Parameterized constructor. It assigns each objective/criterion the same threshold.
     *
     * @param threshold comparison threshold
     */
    public RequiredSpread(double threshold)
    {
        this(new double[]{threshold});
    }

    /**
     * Parameterized constructor. It assigns each objective/criterion different thresholds.
     *
     * @param thresholds comparison thresholds
     */
    public RequiredSpread(double[] thresholds)
    {
        _RS = new interaction.refine.filters.termination.RequiredSpread(thresholds);
    }

    /**
     * Returns true.
     *
     * @param dmContext          current decision-making context
     * @param alternatives wrapped alternatives
     * @return true
     * @throws ReferenceSetsConstructorException exception can be thrown and propagated higher
     */
    @Override
    public boolean isValid(DMContext dmContext, AbstractAlternatives<?> alternatives) throws ReferenceSetsConstructorException
    {
        if ((alternatives != null) && (alternatives.isEmpty()))
            throw new ReferenceSetsConstructorException("The alternatives set is not provided (the array is empty)", this.getClass());
        try
        {
            return !_RS.shouldTerminate(dmContext, alternatives)._shouldTerminate;
        } catch (RefinerException e)
        {
            throw new ReferenceSetsConstructorException("Error occurred when refining the alternatives " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }
}
