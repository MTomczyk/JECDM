package emo.interactive.iemod;

import ea.EA;
import emo.interactive.AbstractInteractAndPrepareStep;
import emo.interactive.utils.dmcontext.IDMCParamsConstructor;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import model.internals.value.AbstractValueInternalModel;
import os.IOSChangeListener;
import phase.IPhase;
import phase.PhaseReport;
import system.dm.DM;
import system.ds.DecisionSupportSystem;
import system.ds.Report;

import java.util.LinkedList;

/**
 * Default IEMO/D "prepare step" phase. It first runs the DSS process, but only if it is the first steady state repeat
 * in the generation. If the preference elicitation was triggered and the models were constructed,
 * the new goals are constructed and supplied to {@link MOEADGoalsManager}. Then, the phase constructs goals update
 * sequence. Additionally, this phase implements {@link IOSChangeListener} to update models/goals in response to changes
 * in the known objective space.
 *
 * @author MTomczyk
 */

public class IEMODInteractAndPrepareStep<T extends AbstractValueInternalModel> extends AbstractInteractAndPrepareStep implements IPhase, IOSChangeListener
{
    /**
     * MOEA/D goal manager.
     */
    protected final MOEADGoalsManager _goalManager;

    /**
     * Goals updater.
     */
    private final IEMODGoalsUpdater<T> _goalsUpdater;

    /**
     * Parameterized constructor.
     *
     * @param goalManager          MOEA/D goal manager
     * @param DSS                  decision support system
     * @param goalsUpdater         goals updater
     * @param dmParamsConstructor  decision-making context params constructor
     */
    public IEMODInteractAndPrepareStep(MOEADGoalsManager goalManager,
                                       DecisionSupportSystem DSS,
                                       IEMODGoalsUpdater<T> goalsUpdater,
                                       IDMCParamsConstructor dmParamsConstructor)
    {
        super("IEMO/D: Interact and prepare step", DSS, dmParamsConstructor);
        _goalsUpdater = goalsUpdater;
        _goalManager = goalManager;
    }


    /**
     * Phase's main action.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        super.action(ea, report);
        if (ea.getCurrentSteadyStateRepeat() == 0) // only in the first steady-state repeat
            _goalManager.determineUpdatesSequence(ea.getR());
    }


    /**
     * An auxiliary method can be overwritten to perform the internal data update (to be overwritten).
     *
     * @param report report on the most recent DSS execute process call
     * @param dm     list of DMs for which some new internal models were constructed
     * @param ea     reference to the EA
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    protected void doInternalUpdate(Report report, LinkedList<DM> dm, EA ea) throws PhaseException
    {
        try
        {
            _goalsUpdater.updateGoals(ea);
        } catch (PhaseException e)
        {
            throw new PhaseException("Error occurred when updating goals " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

}
