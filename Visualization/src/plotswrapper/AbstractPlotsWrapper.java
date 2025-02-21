package plotswrapper;

import container.Notification;
import frame.Frame;
import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;
import plotwrapper.PlotWrapper;
import scheme.AbstractScheme;
import thread.swingworker.Interactor;

import javax.swing.*;

/**
 * Main panel displayed on the frame ({@link Frame}).
 * It encapsulates and organizes plots to be displayed ({@link AbstractPlot},
 * plots are wrapped using {@link AbstractPlotWrapper}).
 *
 * @author MTomczyk
 */
public abstract class AbstractPlotsWrapper extends JPanel
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Plots to be displayed (wrapped).
         */
        public final AbstractPlotWrapper[] _wrappers;

        /**
         * If true, calls for repaint are triggered using a timer-like object (recommended).
         */
        public boolean _useAnimator = true;

        /**
         * This filed indicates how frequently the rendering call should be executed when using the animator.
         */
        public int _animatorFPS = 240;

        /**
         * If true, the interactor will be used (see {@link Interactor}).
         */
        public boolean _useInteractor = true;

        /**
         * This field indicates how frequently the interactor will notify the active interaction listener about the current timestamp.
         */
        public int _interactorFPS = 240;

        /**
         * If true, ignore execution of those background tasks (execution blocks) that are overdue and not relevant for
         * the processing (recommended).
         */
        public boolean _considerOverdueForExecutionBlocks = true;

        /**
         * Overdue time (in nanoseconds) for background tasks (see {@link swing.swingworkerqueue.ExecutionBlock}).
         */
        public int _overdue = (int) (1.0E9 / 60.0d);

        /**
         * Represents the number of updaters queues in the queueing system {@link PlotsWrapperController#_queueingSystem}.
         * Each queue consumes one thread from the swing worker thread pool, and it is assumed that
         * each plot is linked to just one queue (but one queue can have multiple linkages).
         * Therefore, using a value greater than one if there is just one plot does not make sense.
         * However, in the case of maintaining multiple plots, setting this number to a greater
         * value may introduce better parallelization. Still, however, the number should be set
         * to some reasonable value, e.g., of four, as it is capped by (1) no. cores in the system,
         * (2) swing worker thread pool size of 10.
         */
        public int _noUpdatersQueues = 1;

        /**
         * Parameterized constructor.
         *
         * @param plotWrappers plots to be displayed (wrapped).
         */
        public Params(AbstractPlotWrapper[] plotWrappers)
        {
            _wrappers = plotWrappers;
        }

        /**
         * Parameterized constructor.
         *
         * @param plots plots to be displayed (default wrapper is used).
         */
        public Params(AbstractPlot[] plots)
        {
            _wrappers = new AbstractPlotWrapper[plots.length];
            for (int i = 0; i < plots.length; i++)
            {
                if (plots[i] == null) continue;
                _wrappers[i] = new PlotWrapper(plots[i]);
            }
        }
    }

    /**
     * MVC model: plots wrapper controller.
     */
    protected PlotsWrapperController _C;

    /**
     * MVC model: plots wrapper model.
     */
    protected PlotsWrapperModel _M;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    protected AbstractPlotsWrapper(Params p)
    {
        instantiateModelAndController(p);
        instantiateGUI(p);
        instantiateLayout(p);
    }

    /**
     * Instantiates the model and the controller.
     *
     * @param p params container
     */
    protected void instantiateModelAndController(Params p)
    {
        _M = getModelInstance(p);

        _C = getControllerInstance(p);
        _C._useAnimator = p._useAnimator;
        _C._animatorFPS = p._animatorFPS;
        _C._useInteractor = p._useInteractor;
        _C._interactorFPS = p._interactorFPS;
        _C._noUpdatersQueues = p._noUpdatersQueues;
        _C._overdue = p._overdue;
        _C._considerOverdueForExecutionBlocks = p._considerOverdueForExecutionBlocks;

        _M.setPlotsWrapperController(_C);
        _C.setPlotsWrapperModel(_M);
    }

    /**
     * Method for creating the controller instance.
     *
     * @param p params container
     * @return plots wrapper controller
     */
    protected PlotsWrapperController getControllerInstance(Params p)
    {
        return new PlotsWrapperController(this);
    }

    /**
     * Method for creating the model instance.
     *
     * @param p params container
     * @return plots wrapper model
     */
    protected PlotsWrapperModel getModelInstance(Params p)
    {
        return new PlotsWrapperModel(this, p._wrappers);
    }

    /**
     * Can be overwritten to instantiate some additional GUI elements.
     *
     * @param p params container
     */
    protected void instantiateGUI(Params p)
    {

    }

    /**
     * Instantiates the layout (to be overwritten).
     *
     * @param p params container
     */
    protected void instantiateLayout(Params p)
    {

    }

    /**
     * Getter for the model (MVC).
     *
     * @return the model
     */
    public PlotsWrapperModel getModel()
    {
        return _M;
    }

    /**
     * Getter for the controller (MVC).
     *
     * @return the model
     */
    public PlotsWrapperController getController()
    {
        return _C;
    }

    /**
     * Can be called to update plot appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc.); each plot uses a deep copy object as a
     *               new scheme and updates its look; can also be null -> each plot uses its own scheme object provided
     *               when instantiated
     */
    public void updateScheme(AbstractScheme scheme)
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper: update scheme method called");
        if (_M._wrappers != null)
        {
            for (AbstractPlotWrapper w : _M._wrappers)
            {
                if (w == null) continue;
                if (scheme == null) w.updateScheme(null);
                else w.updateScheme(scheme.getClone());
            }
        }
    }

    /**
     * Can be called to update the layout (typically called on the main frame window resized event).
     */
    public void updateLayout()
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper: update layout method called");
        setPreferredSize(getSize());
        if (_M._wrappers != null)
        {
            for (AbstractPlotWrapper w : _M._wrappers)
                if (w != null) w.updateLayout();
        }
    }


    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper: dispose method called");
        _M.notifyTerminating();
        _C.disposeAllBackgroundThreads();
        _C.dispose();
        _M.dispose();
        _C = null;
        _M = null;
    }

}
