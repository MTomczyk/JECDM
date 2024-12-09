package phase;

import ea.EA;
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
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    PhaseReport perform(EA ea) throws PhaseException;

    /**
     * Returns the phase name
     *
     * @return name
     */
    String getName();

    /**
     * Returns phase unique identifier (see {@link PhasesIDs}).
     * @return ID
     */
    int getID();
}
