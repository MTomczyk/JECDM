package emo.interactive;

import dmcontext.DMContext;
import ea.AbstractPhasesEA;
import ea.IEA;
import emo.interactive.utils.dmcontext.IDMCParamsConstructor;
import exception.PhaseException;
import exeption.DecisionSupportSystemException;
import interaction.Status;
import model.internals.AbstractInternalModel;
import os.IOSChangeListener;
import phase.AbstractPrepareStepPhase;
import phase.IPhase;
import phase.PhaseReport;
import space.os.ObjectiveSpace;
import system.dm.DM;
import system.ds.DecisionSupportSystem;
import system.ds.Report;

import java.util.EnumSet;
import java.util.LinkedList;

/**
 * Default implementation of a "prepare step" dedicated to interactive methods. The call for
 * {@link DecisionSupportSystem#executeProcess(DMContext.Params)}
 * is executed only when the current steady state repeat equals zero (see
 * {@link AbstractInteractAndPrepareStep#action(AbstractPhasesEA, PhaseReport)})
 *
 * @author MTomczyk
 */
public class AbstractInteractAndPrepareStep extends AbstractPrepareStepPhase implements IPhase, IOSChangeListener
{
    /**
     * Decision support system.
     */
    protected final DecisionSupportSystem _DSS;

    /**
     * Decision-making context params constructor.
     */
    protected final IDMCParamsConstructor _dmContextParamsConstructor;

    /**
     * Parameterized constructor.
     *
     * @param name                phase name
     * @param DSS                 decision support system
     * @param dmParamsConstructor decision-making context params constructor
     */
    public AbstractInteractAndPrepareStep(String name, DecisionSupportSystem DSS,
                                          IDMCParamsConstructor dmParamsConstructor)
    {
        super(name);
        _DSS = DSS;
        _dmContextParamsConstructor = dmParamsConstructor;
    }

    /**
     * Phase's main action.
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown
     */
    @Override
    public void action(AbstractPhasesEA ea, PhaseReport report) throws PhaseException
    {
        if (ea.getCurrentSteadyStateRepeat() == 0)
        {
            DMContext.Params pDMC = _dmContextParamsConstructor.getDMCParams(ea);
            pDMC._reasons = EnumSet.of(DMContext.Reason.REGULAR_ITERATION);
            Report dssReport;
            try
            {
                dssReport = _DSS.executeProcess(pDMC);
                if (dssReport._elicitationReport._interactionStatus == Status.PROCESS_ENDED_SUCCESSFULLY)
                    doInternalUpdate(dssReport, whichDMModelsChanged(dssReport._updateReport), ea);

            } catch (DecisionSupportSystemException e)
            {
                throw new PhaseException("Exception occurred when executing the phase = " + _name + " " +
                        e.getDetailedReasonMessage(), this.getClass(), e);
            }
        }
    }

    /**
     * An auxiliary method can be overwritten to perform the internal data update (to be overwritten).
     *
     * @param report report on the most recent DSS execute process call
     * @param dm     list of DMs for which some new internal models were constructed
     * @param ea     reference to the EA
     * @throws PhaseException the exception can be thrown
     */
    protected void doInternalUpdate(Report report, LinkedList<DM> dm, IEA ea) throws PhaseException
    {

    }

    /**
     * Action to be performed when there is a change in the objective space.
     *
     * @param ea     evolutionary algorithm
     * @param os     objective space (updated)
     * @param prevOS objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown
     */
    @Override
    public void action(IEA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {
        DMContext.Params pDMC = _dmContextParamsConstructor.getDMCParams(ea);
        pDMC._reasons = EnumSet.of(DMContext.Reason.OS_CHANGED);
        try
        {
            Report report = _DSS.executeModelUpdateProcessAndWrapReport(pDMC);
            doInternalUpdate(report, whichDMModelsChanged(report._updateReport), ea);

        } catch (DecisionSupportSystemException e)
        {
            throw new PhaseException("Exception occurred when executing the IOSChangeListener.action() " +
                    e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Auxiliary method for checking which DMs' internal models have changed. The verification process examines the
     * success rate in preserving the already existing models. Some new models have been generated if the rate is
     * smaller
     * than 1. Thus, there is a need for data updates. The method returns a list of DMs for which such a need exists.
     *
     * @param report report on the last call of
     *               {@link system.modules.updater.ModelsUpdaterModule#executeProcess(DMContext)}
     * @return true, if the internal model(s) should be updated
     */
    protected LinkedList<DM> whichDMModelsChanged(system.modules.updater.Report report)
    {
        LinkedList<DM> dms = new LinkedList<>();
        for (DM dm : report._DMs)
        {
            for (system.model.Report<? extends AbstractInternalModel> r : report._modelsUpdatesReports.get(dm)._reportsOnModelUpdates)
            {
                model.constructor.Report<? extends AbstractInternalModel> modelReport = r._report;
                if (Double.compare(modelReport._successRateInPreserving, 1.0d) < 0)
                {
                    dms.add(dm);
                    break;
                }
            }
        }
        return dms;
    }

}
