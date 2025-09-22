package phase;

import ea.AbstractPhasesEA;
import exception.PhaseException;

/**
 * Default "Init Ends" phase (sets the ``requires evaluation/ID assignment'' flags to false).
 *
 * @author MTomczyk
 */


public class InitEnds extends AbstractInitEndsPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "INIT_ENDS").
     */
    public InitEnds()
    {
        this("INIT_ENDS");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public InitEnds(String name)
    {
        super(name);
    }

    /**
     * Phase main action (sets the ``requires evaluation/ID assignment'' flags to false).
     *
     * @param ea evolutionary algorithm
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
