package model.constructor.value.rs.iterationslimit;

import dmcontext.DMContext;
import history.PreferenceInformationWrapper;
import model.constructor.Report;
import model.internals.value.AbstractValueInternalModel;

import java.util.LinkedList;

/**
 * Simple interface for dynamically determining the limit for the number of iterations (improvement attempts) for
 * {@link model.constructor.value.rs.ers.ERS}.
 *
 * @author MTomczyk
 */
public interface IIterationsLimit
{
    /**
     * Determines the limit for the number of iterations.
     *
     * @param dmContext             current decision-making context
     * @param preferenceInformation current preference information
     * @param report                current report being built (can be already partially filled by the initializeStep method of {@link model.constructor.value.rs.AbstractRejectionSampling})
     * @param N                     the number of feasible models to sample
     * @param <T>                   preference model definition
     * @return the number of iterations (should be at least 0)
     */
    <T extends AbstractValueInternalModel> int getIterations(DMContext dmContext, LinkedList<PreferenceInformationWrapper> preferenceInformation, Report<T> report, int N);
}
