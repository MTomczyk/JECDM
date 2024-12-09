package listeners.interact;


import container.GlobalContainer;
import container.PlotContainer;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Default implementation of {@link AbstractInteractListener}.
 * Reports event if the debug mode is on in {@link container.GlobalContainer}.
 *
 * @author MTomczyk
 */

public class InteractListener extends AbstractInteractListener implements MouseMotionListener, MouseListener
{
    /**
     * Parameterized constructor.
     *
     * @param GC global container (shared object; stores references, provides various functionalities)
     * @param PC stores references to plot-related objects and provides various functionalities
     */
    public InteractListener(GlobalContainer GC, PlotContainer PC)
    {
        super(GC, PC);
    }
}
