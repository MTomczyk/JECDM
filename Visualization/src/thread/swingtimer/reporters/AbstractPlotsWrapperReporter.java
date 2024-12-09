package thread.swingtimer.reporters;

import container.GlobalContainer;
import plotswrapper.AbstractPlotsWrapper;
import thread.swingtimer.AbstractTimer;

/**
 * Abstract class for objects periodically reporting the status of {@link AbstractPlotsWrapper} objects.
 *
 * @author MTomczyk
 */

public class AbstractPlotsWrapperReporter extends AbstractTimer
{
    /**
     * Parameterized constructor.
     *
     * @param GC  global container (shared object; stores references, provides various functionalities)
     * @param fps frames per second (action execution frequency).
     */
    public AbstractPlotsWrapperReporter(GlobalContainer GC, int fps)
    {
        super(GC, fps, false, 0);
    }
}
