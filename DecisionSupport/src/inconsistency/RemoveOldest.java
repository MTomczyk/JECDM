package inconsistency;

import exeption.ConstructorException;
import exeption.InconsistencyHandlerException;
import history.History;
import history.PreferenceInformationWrapper;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.internals.AbstractInternalModel;

import java.util.LinkedList;

/**
 * This method tries to reintroduce consistency by sequentially removing the oldest preference examples and constructing the model
 * until the latter is successfully accomplished. Next, it attempts to recover as many removed preferences as possible in the same manner,
 * starting from the newest to the oldest (they are added to the front of the list to preserve the order). See
 * similar approaches in <a href="https://doi.org/10.1109/TEVC.2014.2303783">here</a> and
 * <a href="https://doi.org/10.1016/j.ejor.2015.10.027">here</a>.
 *
 * @author MTomczyk
 */
public class RemoveOldest<T extends AbstractInternalModel> extends AbstractInconsistencyHandler<T> implements IInconsistencyHandler<T>
{
    /**
     * Default constructor.
     */
    public RemoveOldest()
    {
        this(false);
    }

    /**
     * Parameterized constructor.
     *
     * @param storeAllStates if true, all the model bundles (states) generated during consistency reintroduction
     *                       are collected and returned in a report.
     */
    public RemoveOldest(boolean storeAllStates)
    {
        super("Remove oldest", storeAllStates);
    }

    /**
     * The method for reintroducing consistency.
     *
     * @param modelBundle           bundle result of the attempt to construct models (see {@link Report}),
     *                              it is assumed that this bundle indicates inconsistency ({@link Report#_inconsistencyDetected}).
     * @param constructor           object used to construct the model given the preference information stored in the history object (see {@link History}).
     * @param preferenceInformation current preference information (copied from the model system) that can be modified to reintroduce consistency (altered set should be stored in the report);
     *                              the list is derived via {@link History#getPreferenceInformationCopy()}, thus it is valid (e.g., preference statements are ordered from the oldest to the newest)
     * @return the method should try to reintroduce consistency (e.g., by altering the constructor definition or history
     * of preference elicitation); the result should be provided via the report object
     * @throws InconsistencyHandlerException the preference model exception can be thrown 
     */
    @Override
    public inconsistency.Report<T> reintroduceConsistency(Report<T> modelBundle,
                                                          IConstructor<T> constructor,
                                                          LinkedList<PreferenceInformationWrapper> preferenceInformation) throws InconsistencyHandlerException
    {
        doBasicValidation(modelBundle, preferenceInformation);

        inconsistency.Report<T> report = new inconsistency.Report<>(_dmContext);
        long startTime = System.nanoTime();
        report._attempts = 0;
        report._states = new LinkedList<>();
        if (_storeAllStates)
        {
            report._states.add(new State<>(modelBundle, preferenceInformation, 0));
            preferenceInformation = new LinkedList<>(preferenceInformation); // create copy (the reference is already in the first state)
        }

        report._consistentState = null;

        int currentAttempt = 0;
        Report<T> currentReport = modelBundle;
        LinkedList<PreferenceInformationWrapper> removedPreferenceExamples = new LinkedList<>();

        boolean firstWasCausing = false;

        // first phase
        while (currentReport._inconsistencyDetected)
        {
            notifyAboutAttemptBeginning(constructor);
            constructor.clearModels(); // clear models here

            currentAttempt++;
            removedPreferenceExamples.add(preferenceInformation.getFirst());
            notifyAboutRemovedPreferenceInformation(constructor, preferenceInformation.getFirst());
            preferenceInformation.removeFirst();

            currentReport = getCandidateBundle(constructor, preferenceInformation, report, currentAttempt);
            if ((currentAttempt == 1) && (!currentReport._inconsistencyDetected)) firstWasCausing = true;
            notifyAboutAttemptEnding(constructor);
        }

        report._consistentState = new State<>(currentReport, new LinkedList<>(preferenceInformation), currentAttempt);

        if ((removedPreferenceExamples.size() > 1) ||
                ((!removedPreferenceExamples.isEmpty()) && (!firstWasCausing)))
        {
            int toRecover = removedPreferenceExamples.size();
            for (int i = 0; i < toRecover; i++)
            {
                notifyAboutAttemptBeginning(constructor);
                currentAttempt++;

                PreferenceInformationWrapper candidate = removedPreferenceExamples.getLast();
                removedPreferenceExamples.removeLast();

                LinkedList<PreferenceInformationWrapper> candidateHistoryCopy = new LinkedList<>(preferenceInformation);
                notifyAboutAddedPreferenceInformation(constructor, candidate);
                candidateHistoryCopy.addFirst(candidate);

                Report<T> candidateBundle = getCandidateBundle(constructor, candidateHistoryCopy, report, currentAttempt);

                if (!candidateBundle._inconsistencyDetected)
                {
                    preferenceInformation = candidateHistoryCopy;
                    currentReport = candidateBundle;
                }

                notifyAboutAttemptEnding(constructor);
            }
        }

        report._attempts = currentAttempt;
        report._consistentState = new State<>(currentReport, new LinkedList<>(preferenceInformation), currentAttempt);
        report._processingTime = (double) (System.nanoTime() - startTime) / 1000000.0d;
        return report;
    }

    /**
     * Auxiliary method for notifying about the removed preference example.
     *
     * @param constructor constructor object
     * @param removed     removed preference example
     * @throws InconsistencyHandlerException exception can be thrown 
     */
    private void notifyAboutRemovedPreferenceInformation(IConstructor<T> constructor, PreferenceInformationWrapper removed) throws InconsistencyHandlerException
    {
        LinkedList<PreferenceInformationWrapper> forNotification = new LinkedList<>();
        forNotification.add(removed);
        try
        {
            constructor.notifyAboutRemovedPreferenceInformation(forNotification);
        } catch (ConstructorException e)
        {
            throw new InconsistencyHandlerException("Could not notify about removed preference examples " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }


    /**
     * Auxiliary method for notifying about the added preference example.
     *
     * @param constructor constructor object
     * @param added       added preference example
     * @throws InconsistencyHandlerException exception can be thrown 
     */
    private void notifyAboutAddedPreferenceInformation(IConstructor<T> constructor, PreferenceInformationWrapper added) throws InconsistencyHandlerException
    {

        LinkedList<PreferenceInformationWrapper> forNotification = new LinkedList<>();
        forNotification.add(added);
        try
        {
            constructor.notifyAboutAddedPreferenceInformation(forNotification);
        } catch (ConstructorException e)
        {
            throw new InconsistencyHandlerException("Could not notify about added preference examples " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }


}
