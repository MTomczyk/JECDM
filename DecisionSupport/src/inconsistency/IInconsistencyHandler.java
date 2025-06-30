package inconsistency;

import dmcontext.DMContext;
import exeption.InconsistencyHandlerException;
import history.History;
import history.PreferenceInformationWrapper;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.internals.AbstractInternalModel;

import java.util.LinkedList;

/**
 * Interface for classes responsible for handling inconsistency when constructing the model given preference information
 * (reintroduce consistency).
 *
 * @author MTomczyk
 */
public interface IInconsistencyHandler<T extends AbstractInternalModel>
{
    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext current decision-making context
     * @throws InconsistencyHandlerException the exception can be thrown 
     */
    void registerDecisionMakingContext(DMContext dmContext) throws InconsistencyHandlerException;

    /**
     * Auxiliary method that can be used to unregister the current decision-making context {@link DMContext}.
     */
    void unregisterDecisionMakingContext();

    /**
     * Signature for the method responsible for reintroducing consistency.
     *
     * @param modelBundle           bundle result of the attempt to construct models (see {@link Report}),
     *                              it is assumed that this bundle indicates inconsistency ({@link Report#_inconsistencyDetected}).
     * @param constructor           object used to construct the model given the preference information stored in the history object (see {@link History}).
     * @param preferenceInformation current preference information (copied from the model system) that can be modified to reintroduce consistency (altered set should be stored in the report);
     *                              the list is derived via {@link History#getPreferenceInformationCopy()}, thus it is valid (e.g., preference statements are ordered from the oldest to the newest)
     * @return the method should reintroduce consistency (e.g., by altering the constructor definition or history
     * of preference elicitation); the result should be provided via the report object
     * @throws InconsistencyHandlerException the preference model exception can be thrown 
     */
    inconsistency.Report<T> reintroduceConsistency(Report<T> modelBundle,
                                                   IConstructor<T> constructor,
                                                   LinkedList<PreferenceInformationWrapper> preferenceInformation) throws InconsistencyHandlerException;
}
