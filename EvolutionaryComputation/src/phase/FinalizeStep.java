package phase;

import ea.AbstractPhasesEA;
import ea.EA;
import exception.PhaseException;

/**
 * Default "finalize step" phase. It sets all the population/offspring requires evaluation/ID assignment
 * flags to false.
 *
 * @author MTomczyk
 */


public class FinalizeStep extends AbstractFinalizeStepPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "FINALIZE_STEP").
     */
    public FinalizeStep()
    {
        this("FINALIZE_STEP");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public FinalizeStep(String name)
    {
        super(name);
    }

    /**
     * Phase main action (sets the "requires evaluation/ID assignments" flags to false).
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(AbstractPhasesEA ea, PhaseReport report) throws PhaseException
    {
        ea.getSpecimensContainer().setPopulationRequiresEvaluation(false);
        ea.getSpecimensContainer().setOffspringRequiresEvaluation(false);
        ea.getSpecimensContainer().setPopulationRequiresIDAssignment(false);
        ea.getSpecimensContainer().setOffspringRequiresIDAssignment(false);
    }
}
