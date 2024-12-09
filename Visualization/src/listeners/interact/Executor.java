package listeners.interact;

import container.GlobalContainer;
import container.PlotContainer;

/**
 * Default implementation of {@link AbstractExecutor}. Does not perform any interactions.
 *
 * @author MTomczyk
 */

public class Executor extends AbstractExecutor
{
    /**
     * Parameterized constructor.
     *
     * @param GC     global container (shared object; stores references, provides various functionalities)
     * @param PC     plot container
     * @param P      reference to the projection data kept by {@link AbstractInteractListener}
     * @param tSpeed translation speed (change in vector per second)
     * @param rSpeed rotation speed (change in angles per percent of panel dimensions passed by the mouse)
     */
    public Executor(GlobalContainer GC, PlotContainer PC, Projection P, float tSpeed, float rSpeed)
    {
        super(GC, PC, P, tSpeed, rSpeed);
    }
}

