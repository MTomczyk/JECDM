package interaction.refine.filters.reduction;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import exeption.RefinerException;
import relation.equalevaluations.EqualEvaluationsUtils;
import utils.Constants;

/**
 * Implementation of {@link IReductionFilter} for removing duplicated alternatives.
 * The comparison is based only on inspecting their evaluations in the objective space.
 *
 * @author MTomczyk
 */
public class RemoveDuplicatesInOS extends AbstractReductionFilter implements IReductionFilter
{
    /**
     * Tolerance used when comparing doubles.
     */
    private final double _tolerance;

    /**
     * Default constructor.
     */
    public RemoveDuplicatesInOS()
    {
        this(Constants.EPSILON);
    }

    /**
     * Parameterized constructor.
     *
     * @param tolerance tolerance used when comparing doubles (comparison is done in the original (not normalized) space
     */
    public RemoveDuplicatesInOS(double tolerance)
    {
        super("Remove duplicates in the objective space");
        _tolerance = tolerance;
    }

    /**
     * The main method that filters out duplicated alternatives (comparison is based on inspecting evaluations, i.e., performance vectors only).
     *
     * @param dmContext          current decision-making context
     * @param processedSet upper (super) set
     * @return alternatives that passed the filter tests (are valid and can be processed further), it should be a subset of the input set (recommended to make it a different object than the input)
     * @throws RefinerException the exception can be thrown 
     */
    @Override
    public AbstractAlternatives<?> reduce(DMContext dmContext, AbstractAlternatives<?> processedSet) throws RefinerException
    {
        validate(dmContext, processedSet);
        if (processedSet.isEmpty()) return processedSet.getCopy();
        return EqualEvaluationsUtils.removeDuplicates(processedSet, _tolerance);
    }
}
