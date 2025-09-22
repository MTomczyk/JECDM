package emo.interactive.nemo;

import ea.IEA;
import emo.interactive.AbstractInteractAndPrepareStep;
import emo.interactive.utils.dmcontext.IDMCParamsConstructor;
import exception.PhaseException;
import os.IOSChangeListener;
import phase.IPhase;
import space.os.ObjectiveSpace;
import system.dm.DecisionMakerSystem;
import system.ds.DecisionSupportSystem;

/**
 * "Interact and prepare" step for NEMO algorithms.
 *
 * @author MTomczyk
 */

public class NEMOInteractAndPrepareStep extends AbstractInteractAndPrepareStep implements IPhase, IOSChangeListener
{
    /**
     * Parameterized constructor.
     *
     * @param name                       phase name
     * @param DSS                        decision support system
     * @param dmContextParamsConstructor decision-making context params constructor
     */
    public NEMOInteractAndPrepareStep(String name, DecisionSupportSystem DSS, IDMCParamsConstructor dmContextParamsConstructor)
    {
        super(name, DSS, dmContextParamsConstructor);
        _DMS = DSS.getDecisionMakersSystems()[0];
    }

    /**
     * Decision maker's system.
     */
    protected final DecisionMakerSystem _DMS;


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
        // Switching the mode
        if (_DMS.getHistory().getNoPreferenceExamples() != 0) super.action(ea, os, prevOS);
    }
}
