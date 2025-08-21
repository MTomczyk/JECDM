package os;

import ea.AbstractPhasesEA;
import ea.IEA;
import exception.PhaseException;
import space.os.ObjectiveSpace;

/**
 * Listener for the triggered change in the objective space.
 *
 * @author MTomczyk
 */
public interface IOSChangeListener
{
    /**
     * Action to be performed when there is a change in the objective space.
     * @param ea evolutionary algorithm
     * @param os objective space (updated)
     * @param prevOS  previous objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown 
     */
    void action(IEA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException;
}
