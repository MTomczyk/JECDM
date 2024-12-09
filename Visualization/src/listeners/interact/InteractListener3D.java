package listeners.interact;


import container.GlobalContainer;
import container.PlotContainer;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Implementation of {@link AbstractInteractListener} for 3D plots ({@link plot.Plot3D}).
 *
 * @author MTomczyk
 */

public class InteractListener3D extends AbstractInteractListener implements MouseMotionListener, MouseListener
{

    /**
     * Parameterized constructor.
     *
     * @param GC                             global container (shared object; stores references, provides various functionalities)
     * @param PC                             stores references to plot-related objects and provides various functionalities
     */
    public InteractListener3D(GlobalContainer GC, PlotContainer PC)
    {
        super(GC, PC);
    }


    /**
     * Auxiliary method instantiating the object responsible for executing the interactions events.
     */
    @Override
    protected void instantiateExecutor()
    {
        _executor = new Executor3D(_GC, _PC, _projection, 1.0f, 180.0f);
    }

    /**
     * Initializes the default projection.
     */
    protected void initDefaultProjection()
    {
        _projection.setDefaultProjection(new float[2], new float[2], new float[]{0.0f, 0.0f, 2.0f});
        resetProjection();
    }
}
