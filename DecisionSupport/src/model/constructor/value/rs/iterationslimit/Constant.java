package model.constructor.value.rs.iterationslimit;

import dmcontext.DMContext;
import history.PreferenceInformationWrapper;
import model.constructor.Report;
import model.internals.value.AbstractValueInternalModel;

import java.util.LinkedList;

/**
 * Default implementation of {@link IIterationsLimit} that returns a constant limit.
 *
 * @author MTomczyk
 */
public class Constant implements IIterationsLimit
{
    /**
     * Limit for the number of iterations.
     */
    public final int _limit;

    /**
     * Parameterized constructor.
     *
     * @param limit limit for the number of iterations
     */
    public Constant(int limit)
    {
        _limit = limit;
    }

    /**
     * Determines the limit for the number of iterations.
     *
     * @param dmContext             current decision-making context
     * @param preferenceInformation current preference information
     * @param report                current report being built (can be already partially filled by the initializeStep method of {@link model.constructor.value.rs.AbstractRejectionSampling})
     * @param N                     the number of feasible models to sample
     * @return the number of iterations (should be at least 0)
     */
    @Override
    public <T extends AbstractValueInternalModel> int getIterations(DMContext dmContext, LinkedList<PreferenceInformationWrapper> preferenceInformation, Report<T> report, int N)
    {
        return _limit;
    }
}
