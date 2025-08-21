package phase;

import ea.AbstractPhasesEA;
import ea.EA;
import ea.IEA;
import exception.PhaseException;

/**
 * Class representing a single step of an EA.
 *
 * @author MTomczyk
 */
public interface IPhase
{
    /**
     * Executes the phase.
     *
     * @param ea evolutionary algorithm
     * @return report on the executed action
     * @throws PhaseException the exception can be thrown 
     */
    PhaseReport perform(AbstractPhasesEA ea) throws PhaseException;

    /**
     * Returns the phase unique name
     *
     * @return name
     */
    String getName();
}
