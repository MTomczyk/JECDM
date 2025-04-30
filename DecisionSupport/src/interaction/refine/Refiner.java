package interaction.refine;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import exeption.RefinerException;
import interaction.Status;
import interaction.reference.ReferenceSetsConstructor;
import interaction.refine.filters.reduction.IReductionFilter;
import interaction.refine.filters.reduction.RemoveDominated;
import interaction.refine.filters.reduction.RemoveDuplicatesInOS;
import interaction.refine.filters.termination.ITerminationFilter;
import interaction.refine.filters.termination.RequiredSpread;
import interaction.refine.filters.termination.TerminationResult;

import java.util.LinkedList;

/**
 * The class can indicate premature termination or reduce the input set given selected conditions. Finally, a subset of
 * the input superset will serve as a basis for reference set construction (see {@link ReferenceSetsConstructor}).
 *
 * @author MTomczyk
 */
public class Refiner
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Optional termination filters (can be null = not used).
         */
        public LinkedList<ITerminationFilter> _terminationFilters;

        /**
         * Optional reduction filters (can be null = not used). Note that the order matters: the filters will be
         * executed in the given order, and the output of one filter will be the input for the next one.
         */
        public LinkedList<IReductionFilter> _reductionFilters;

        /**
         * Creates default object instance (default parameterization). It first instantiates the termination filters
         * using {@link RequiredSpread} (threshold of 0.0001 is used). Next, reduction filters involve
         * {@link RemoveDuplicatesInOS} and {@link RemoveDominated} (in the provided order).
         *
         * @return default object instance
         */
        public static Params getDefault()
        {
            return getDefault(1.0E-4);
        }


        /**
         * Creates default object instance (default parameterization). It first instantiates the termination filters
         * using {@link RequiredSpread}. Next, reduction filters involve {@link RemoveDuplicatesInOS} and
         * {@link RemoveDominated} (in the provided order).
         *
         * @param thTermination the threshold for the termination filter
         * @return default object instance
         */
        public static Params getDefault(double thTermination)
        {
            Params p = new Params();
            p._terminationFilters = new LinkedList<>();
            p._terminationFilters.add(new RequiredSpread(thTermination));
            p._reductionFilters = new LinkedList<>();
            p._reductionFilters.add(new RemoveDuplicatesInOS());
            p._reductionFilters.add(new RemoveDominated());
            return p;
        }
    }

    /**
     * Optional termination filters (can be null = not used).
     */
    private final LinkedList<ITerminationFilter> _terminationFilters;

    /**
     * Optional reduction filters (can be null = not used). Note that the order matters: the filters will be
     * executed in the given order, and the output of one filter will be the input for the next one.
     */
    private final LinkedList<IReductionFilter> _reductionFilters;


    /**
     * Creates default object instance. It first instantiates the termination filters using {@link RequiredSpread}
     * (threshold of 0.001). Next, reduction filters involve {@link RemoveDuplicatesInOS} and
     * {@link RemoveDominated} (in the provided order).
     *
     * @return default object instance (null, if the creation process failed)
     */
    public static Refiner getDefault()
    {
        return getDefault(0.001d);
    }

    /**
     * Creates default object instance. It first instantiates the termination filters using {@link RequiredSpread}.
     * Next, reduction filters involve {@link RemoveDuplicatesInOS} and {@link RemoveDominated} (in the
     * provided order).
     *
     * @param thTermination the threshold for the termination filter
     * @return default object instance
     */
    public static Refiner getDefault(double thTermination)
    {
        return new Refiner(Params.getDefault(thTermination));
    }


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public Refiner(Params p)
    {
        _terminationFilters = p._terminationFilters;
        _reductionFilters = p._reductionFilters;
    }

    /**
     * Auxiliary method that can be called to validate some essential fields.
     *
     * @throws RefinerException the exception will be thrown if the validation fails
     */
    public void validate() throws RefinerException
    {
        if (_terminationFilters != null)
            for (ITerminationFilter tf : _terminationFilters)
                if (tf == null)
                    throw new RefinerException("One of the provided termination filters is null", this.getClass());
        if (_reductionFilters != null)
            for (IReductionFilter rf : _reductionFilters)
                if (rf == null)
                    throw new RefinerException("One of the provided reduction filters is null", this.getClass());
    }

    /**
     * The main method for constructing refined set of alternatives (wrapped via {@link interaction.reference.Result#_referenceSetsContainer}).
     *
     * @param dmContext current decision-making context
     * @return refined set of alternatives (wrapped via {@link interaction.reference.Result#_referenceSetsContainer}).
     * @throws RefinerException the exception can be thrown and propagated higher
     */
    public Result refine(DMContext dmContext) throws RefinerException
    {
        validate(dmContext);

        Result result = new Result(dmContext);

        // termination filter
        if (checkTerminationConditions(result, dmContext))
        {
            result._refinedAlternatives = null;
            return result;
        }

        // perform reduction
        result._refinedAlternatives = doReduction(result, dmContext, dmContext.getCurrentAlternativesSuperset().getCopy());
        result._status = Status.PROCESS_ENDED_SUCCESSFULLY;

        return result;
    }

    /**
     * Auxiliary method for performing basic validation.
     *
     * @param dmContext current decision-making context
     * @throws RefinerException exception can be thrown and propagated higher
     */
    protected void validate(DMContext dmContext) throws RefinerException
    {
        if (dmContext == null)
            throw new RefinerException("The decision-making context is not provided", this.getClass());
        if (dmContext.getCriteria() == null)
            throw new RefinerException("The criteria are not provided", this.getClass());
        if (dmContext.getCurrentAlternativesSuperset() == null)
            throw new RefinerException("The alternatives set is not provided (the array is null)", this.getClass());
    }

    /**
     * Supportive method performing the reduction step.
     *
     * @param result       result object to be filled
     * @param dmContext          current decision-making context
     * @param alternatives input alternatives set (superset)
     * @return output alternatives (reduced superset)
     * @throws RefinerException the exception can be thrown and propagated higher
     */
    private AbstractAlternatives<?> doReduction(Result result, DMContext dmContext, AbstractAlternatives<?> alternatives) throws RefinerException
    {
        // reduction attempt
        if (_reductionFilters != null)
        {
            int initialSize = alternatives.size();
            long pT = System.nanoTime();
            for (IReductionFilter rf : _reductionFilters) alternatives = rf.reduce(dmContext, alternatives);
            result._reductionFiltersProcessingTime = (System.nanoTime() - pT) / 1000000;
            result._reductionSize = initialSize - alternatives.size();
        }

        return alternatives;
    }

    /**
     * Supportive method that performs the termination step (via filters).
     *
     * @param result       result object to be filled
     * @param dmContext          current decision-making context
     * @return if true, the process should be terminated; false otherwise
     * @throws RefinerException the exception can be thrown and propagated higher
     */
    private boolean checkTerminationConditions(Result result, DMContext dmContext) throws RefinerException
    {
        long pT = System.nanoTime();

        // termination filters
        if (_terminationFilters != null)
        {
            for (ITerminationFilter tf : _terminationFilters)
            {
                TerminationResult tr = tf.shouldTerminate(dmContext);
                if (tr._shouldTerminate)
                {
                    result._status = Status.TERMINATED_DUE_TO_TERMINATION_FILTER;
                    result._terminatedDueToTerminationFilter = true;
                    result._terminatedDueToTerminationFilterMessage = tr._message;
                }
            }

        }
        result._terminationFiltersProcessingTime = (System.nanoTime() - pT) / 1000000;
        return result._terminatedDueToTerminationFilter;
    }
}
