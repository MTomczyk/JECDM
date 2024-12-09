package os;

import ea.EA;
import exception.PhaseException;
import space.os.ObjectiveSpace;

/**
 * IOSChangeListener implementation: dummy, for the printing purposes.
 *
 * @author MTomczyk
 */
public class DummyPrintListener implements IOSChangeListener
{
    /**
     * Dummy action: prints info on objective space.
     * @param ea evolutionary algorithm
     * @param os objective space (updated)
     * @param prevOS objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {
        System.out.println(" --- Objective space has changed! ----");
        os.print();
        System.out.println(" ===================================== ");
    }
}
