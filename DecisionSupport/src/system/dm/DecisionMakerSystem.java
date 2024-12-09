package system.dm;

import dmcontext.DMContext;
import exeption.DecisionMakerSystemException;
import exeption.HistoryException;
import exeption.ModelSystemException;
import history.History;
import history.PreferenceInformationWrapper;
import model.internals.AbstractInternalModel;
import preference.IPreferenceInformation;
import system.model.ModelSystem;

import java.util.LinkedList;

/**
 * A class representing a decision-maker-level processing. It is centrally focused on updating the
 * assumed preference model(s), given the preference examples provided by the decision maker,
 * and handling potential inconsistencies.
 *
 * @author MTomczyk
 */
public class DecisionMakerSystem
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * The decision maker's identifier.
         */
        public DM _DM;

        /**
         * Model systems associated with the decision maker.
         */
        public ModelSystem<? extends AbstractInternalModel>[] _modelSystems;

        /**
         * If true, the timestamps (date and time) will be added when, e.g., registering new preference examples.
         */
        public boolean _addTimestamps = true;
    }

    /**
     * The decision maker's identifier.
     */
    private final DM _DM;

    /**
     * Model systems associated with the decision maker.
     */
    private final ModelSystem<?>[] _modelSystems;

    /**
     * Current decision-making context.
     */
    private DMContext _dmContext;

    /**
     * Represents the history of preference elicitation associated with the decision maker.
     */
    private final History _history;

    /**
     * If true, the timestamps (date and time) will be added when, e.g., registering new preference examples.
     */
    private final boolean _addTimestamps;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     * @throws DecisionMakerSystemException decision maker system exception can be thrown and propagated higher
     */
    public DecisionMakerSystem(Params p) throws DecisionMakerSystemException
    {
        if (p._DM.getName() == null)
            throw new DecisionMakerSystemException("The name of the decision maker is not provided", this.getClass());
        if (p._modelSystems == null)
            throw new DecisionMakerSystemException("The model(s) associated with the decision maker is (are) not provided (the array is null)", this.getClass());
        if (p._modelSystems.length == 0)
            throw new DecisionMakerSystemException("The model(s) associated with the decision maker is (are) not provided (the array is empty)", this.getClass());
        for (ModelSystem<?> ms : p._modelSystems)
            if (ms == null)
                throw new DecisionMakerSystemException("One of the models associated with the decision maker is not provided (is null)", this.getClass());

        _DM = p._DM;
        _modelSystems = p._modelSystems;
        _dmContext = null;
        _addTimestamps = p._addTimestamps;
        _history = new History("History for " + _DM.getName());
    }

    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext decision-making context
     * @throws DecisionMakerSystemException decision maker system exception can be thrown and propagated higher
     */
    public void registerDecisionMakingContext(DMContext dmContext) throws DecisionMakerSystemException
    {
        try
        {
            _dmContext = dmContext;
            for (ModelSystem<?> ms : _modelSystems) ms.registerDecisionMakingContext(dmContext);
        } catch (ModelSystemException e)
        {
            throw new DecisionMakerSystemException("Error occurred when registering decision-making context " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Method for notifying that the preference elicitation begins.
     *
     * @throws DecisionMakerSystemException decision maker system exception can be thrown and propagated higher
     */
    public void notifyPreferenceElicitationBegins() throws DecisionMakerSystemException
    {
        try
        {
            for (ModelSystem<?> ms : _modelSystems) ms.notifyPreferenceElicitationBegins();
        } catch (ModelSystemException e)
        {
            throw new DecisionMakerSystemException("Notification about the beginning of the preference elicitation failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Method for notifying on the most recent preference information provided by the decision maker.
     *
     * @param preferenceInformation preference information provided
     * @throws DecisionMakerSystemException decision maker system exception can be thrown and propagated higher
     */
    public void notifyAboutTheMostRecentPreferenceInformation(IPreferenceInformation preferenceInformation) throws DecisionMakerSystemException
    {
        if (preferenceInformation == null)
            throw new DecisionMakerSystemException("The provided preference example is null", this.getClass());
        LinkedList<IPreferenceInformation> pi = new LinkedList<>();
        pi.add(preferenceInformation);
        notifyAboutTheMostRecentPreferenceInformation(pi);
    }

    /**
     * Method for notifying on the most recent preference information provided by the decision maker.
     *
     * @param preferenceInformation preference information provided
     * @throws DecisionMakerSystemException decision maker system exception can be thrown and propagated higher
     */
    public void notifyAboutTheMostRecentPreferenceInformation(LinkedList<IPreferenceInformation> preferenceInformation) throws DecisionMakerSystemException
    {
        if (preferenceInformation == null)
            throw new DecisionMakerSystemException("The preference information is not provided (the array is null)", this.getClass());
        if (preferenceInformation.isEmpty())
            throw new DecisionMakerSystemException("The preference information is not provided (the array is empty)", this.getClass());
        for (IPreferenceInformation pi : preferenceInformation)
            if (pi == null)
                throw new DecisionMakerSystemException("One of the provided preference examples is null", this.getClass());

        try
        {
            LinkedList<PreferenceInformationWrapper> wrappers =
                    _history.registerPreferenceInformation(preferenceInformation,
                            _dmContext.getCurrentIteration(), _addTimestamps);
            for (ModelSystem<?> ms : _modelSystems)
                ms.notifyAboutMostRecentPreferenceInformation(wrappers);

        } catch (ModelSystemException e)
        {
            throw new DecisionMakerSystemException("Notification about the most recent preference information failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        } catch (HistoryException e)
        {
            throw new DecisionMakerSystemException("Could not register the newest preference examples " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }


    /**
     * Method for notifying that the preference elicitation failed (e.g., when there were no reasonable alternatives for comparison).
     *
     * @throws DecisionMakerSystemException decision maker system exception can be thrown and propagated higher
     */
    public void notifyPreferenceElicitationFailed() throws DecisionMakerSystemException
    {
        try
        {
            for (ModelSystem<?> ms : _modelSystems) ms.notifyPreferenceElicitationFailed();
        } catch (ModelSystemException e)
        {
            throw new DecisionMakerSystemException("Notification about a failure during preference elicitation failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Method for notifying that the preference elicitation ends.
     *
     * @throws DecisionMakerSystemException decision maker system exception can be thrown and propagated higher
     */
    public void notifyPreferenceElicitationEnds() throws DecisionMakerSystemException
    {
        try
        {
            for (ModelSystem<?> ms : _modelSystems) ms.notifyPreferenceElicitationEnds();
        } catch (ModelSystemException e)
        {
            throw new DecisionMakerSystemException("Notification about an end of the preference elicitation step failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * The main method for updating the model given the history of preference elicitation.
     *
     * @return report on the process
     * @throws DecisionMakerSystemException decision maker system exception can be thrown and propagated higher
     */
    public Report updateModels() throws DecisionMakerSystemException
    {
        Report report = new Report(_dmContext);
        long startTime = System.nanoTime();
        report._reportsOnModelUpdates = new LinkedList<>();

        for (ModelSystem<?> ms : _modelSystems)
        {
            try
            {
                system.model.Report<?> r = ms.updateModel();
                report._reportsOnModelUpdates.add(r);
            } catch (ModelSystemException e)
            {
                throw new DecisionMakerSystemException("Could not update the model of " + ms + " " + e.getDetailedReasonMessage(), this.getClass(), e);
            }
        }

        report._processingTime = (System.nanoTime() - startTime) / 1000000;
        return report;
    }


    /**
     * Auxiliary method that can be used to unregister the current decision-making context {@link DMContext}.
     *
     * @throws DecisionMakerSystemException decision maker system exception can be thrown and propagated higher
     */
    public void unregisterDecisionMakingContext() throws DecisionMakerSystemException
    {
        try
        {
            _dmContext = null;
            for (ModelSystem<?> ms : _modelSystems) ms.unregisterDecisionMakingContext();

        } catch (ModelSystemException e)
        {
            throw new DecisionMakerSystemException("Error occurred when unregistering decision-making context " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Getter for the preference elicitation history.
     *
     * @return preference elicitation history
     */
    public History getHistory()
    {
        return _history;
    }

    /**
     * Checks equality of this system with another (based on comparing IDs of their associated decision makers
     *
     * @param o other object
     * @return true, if this object equals another; false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DecisionMakerSystem that = (DecisionMakerSystem) o;
        return _DM.equals(that._DM);
    }

    /**
     * Getter for the decision maker (identifier, provides auxiliary data).
     *
     * @return decision maker object
     */
    public DM getDM()
    {
        return _DM;
    }


    /**
     * Returns the hash code (passes the hash code of the decision maker (id)).
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return _DM.hashCode();
    }

    /**
     * Getter for the model systems.
     *
     * @return model systems
     */
    public ModelSystem<?>[] getModelSystems()
    {
        return _modelSystems;
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "Decision maker system (" + _DM.getName() + ")";
    }
}
