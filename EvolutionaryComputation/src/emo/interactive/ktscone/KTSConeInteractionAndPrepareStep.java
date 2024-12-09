package emo.interactive.ktscone;

import emo.interactive.AbstractInteractAndPrepareStep;
import emo.interactive.utils.dmcontext.IDMCParamsConstructor;
import os.IOSChangeListener;
import phase.IPhase;
import system.ds.DecisionSupportSystem;

/**
 * Direct extension of {@link AbstractInteractAndPrepareStep} for handling interactions and model updates.
 *
 * @author MTomczyk
 */


public class KTSConeInteractionAndPrepareStep extends AbstractInteractAndPrepareStep implements IPhase, IOSChangeListener
{
    /**
     * Parameterized constructor.
     *
     * @param DSS                  decision support system
     * @param dmContextParamsConstructor decision-making context params constructor
     */
    public KTSConeInteractionAndPrepareStep(DecisionSupportSystem DSS, IDMCParamsConstructor dmContextParamsConstructor)
    {
        super("KTSCone: Interact and prepare step", DSS, dmContextParamsConstructor);
    }
}
