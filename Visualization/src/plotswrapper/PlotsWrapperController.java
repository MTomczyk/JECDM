package plotswrapper;

import container.Notification;
import listeners.MousePressedLooseFocus;
import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;
import swing.keyboard.KeyUtils;
import swing.swingworkerqueue.ExecutionBlock;
import swing.swingworkerqueue.QueuedSwingWorker;
import swing.swingworkerqueue.QueueingSystem;
import thread.swingtimer.reporters.AbstractPlotsWrapperReporter;
import thread.swingworker.Animator;
import thread.swingworker.BlockTypes;
import thread.swingworker.Interactor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 * Controller for the {@link AbstractPlotsWrapper} class.
 *
 * @author MTomczyk
 */
public class PlotsWrapperController
{
    /**
     * Reference to plots wrapper.
     */
    protected final AbstractPlotsWrapper _plotsWrapper;

    /**
     * Reference to plots wrapper model.
     */
    protected PlotsWrapperModel _M;

    /**
     * Mouse click -> loose focus on all plots (listener -> action).
     * The goal is to lose focus on all plots anytime something that is not a plot is clicked.
     */
    protected MousePressedLooseFocus _mousePressedLooseFocus;

    /**
     * ID of an active plot.
     */
    protected Integer _activePlotID = null;

    /**
     * List of all reporters (notify on the wrapper performance).
     */
    protected LinkedList<AbstractPlotsWrapperReporter> _reporters = new LinkedList<>();

    /**
     * If true, calls for repaint are triggered using a timer-like object (recommended).
     */
    protected boolean _useAnimator;

    /**
     * This field indicates how frequently the rendering call should be executed when using the animator.
     */
    protected int _animatorFPS;

    /**
     * If true, the interactor will be used (see {@link Interactor}).
     */
    protected boolean _useInteractor;

    /**
     * This field indicates how frequently the interactor will notify the active interaction listener about the current timestamp.
     */
    protected int _interactorFPS;

    /**
     * General timer object for calling general plots wrapper repaint at a specified frequency.
     */
    protected Animator _animator;

    /**
     * Global timer (swing worker background tasks) that notifies the active plot's
     * interact listener about time passed.
     */
    protected Interactor _interactor;

    /**
     * If true, ignore execution of those background tasks (swing workers) that are overdue and not relevant for
     * the processing (recommended).
     */
    protected boolean _considerOverdueForSwingWorkers = true;

    /**
     * Overdue time (in nanoseconds) for background tasks (see {@link QueuedSwingWorker}).
     */
    protected int _overdue = 0;

    /**
     * Represents the number of updaters queues in the queueing system {@link PlotsWrapperController#_queueingSystem}.
     * Each queue consumes one thread from the  swing worker thread pool, and it is assumed that
     * each plot is linked to just one queue (but one queue can have multiple linkages).
     * Therefore, using a value greater than one if there is just one plot does not make sense.
     * However, in the case of maintaining multiple plots, setting this number to a greater
     * value may introduce better parallelization. Still, however, the number should be set
     * to some reasonable value, e.g., of four, as it is capped by (1) no. cores in the system,
     * (2) swing worker thread pool size of 10.
     */
    protected int _noUpdatersQueues = 1;

    /**
     * Queueing system that wraps queues that can manage swing workers' execution.
     * It ensures that their execution does not overlap, i.e., workers are queued and execution
     * of a next task begins after its predecessor finished its job.
     * (see {@link swing.swingworkerqueue.ExecutionBlock}.
     */
    protected QueueingSystem<Void, Void> _queueingSystem;

    /**
     * Parameterized constructor.
     *
     * @param plotsWrapper reference to plots wrapper.
     */
    public PlotsWrapperController(AbstractPlotsWrapper plotsWrapper)
    {
        _plotsWrapper = plotsWrapper;
    }

    /**
     * Setter for the plots wrapper model.
     *
     * @param M plots model wrapper
     */
    public void setPlotsWrapperModel(PlotsWrapperModel M)
    {
        _M = M;
    }

    /**
     * Starts all plot-related timers (also calls the same method for descendants (wrappers -> plots)).
     */
    public void startBackgroundThreads()
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper controller: start background threads method called");

        if (_interactor != null) _interactor.resumeTimer();
        if (_animator != null) _animator.resumeTimer();

        for (AbstractPlotWrapper w : _M._wrappers)
            if (w != null) w.getController().startBackgroundThreads();

        for (AbstractPlotsWrapperReporter r : _reporters) r.startTimer();
    }

    /**
     * Stops all plot-related background threads (also calls the same method for descendants (wrappers -> plots)).
     */
    public void stopBackgroundThreads()
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper controller: stop background threads method called");

        if (_interactor != null) _interactor.pauseTimer();
        if (_animator != null) _animator.pauseTimer();

        for (AbstractPlotWrapper w : _M._wrappers)
            if (w != null) w.getController().stopBackgroundThreads();

        for (AbstractPlotsWrapperReporter r : _reporters)
            r.stopTimer();
    }

    /**
     * Registers workers (block) for execution to the queue, which ensures that the background tasks do not overlap,
     * and they are executed in the order in which they were registered.
     *
     * @param executionBlock block of workers to be executed one by one
     */
    public void registerWorkers(ExecutionBlock<Void, Void> executionBlock)
    {
        if (executionBlock == null) return;
        executionBlock.setConsiderOverdue(_considerOverdueForSwingWorkers);
        executionBlock.setOverdue(_overdue);
        _queueingSystem.addAndScheduleExecutionBlock(executionBlock);
    }

    /**
     * Upon termination, this method is called to cancel all ongoing background threads and dispose the queue.
     */
    public void disposeAllQueuedWorkersAndQueue()
    {
        _queueingSystem.dispose();
        _queueingSystem = null;
    }

    /**
     * Instantiates background threads (also calls the same method for descendants (wrappers -> plots)).
     */
    public void instantiateBackgroundThreads()
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper controller: instantiate background threads method called");

        _queueingSystem = new QueueingSystem<>(_noUpdatersQueues,
                BlockTypes.NO_TYPES, _M._wrappers.length);

        if (_useInteractor)
        {
            _interactor = new Interactor(_M._GC, _interactorFPS);
            _interactor.execute();
        }

        if (_useAnimator)
        {
            _animator = new Animator(_M._GC, _animatorFPS);
            _animator.execute();
        }

        for (AbstractPlotWrapper w : _M._wrappers)
        {
            if (w == null) continue;
            w.getController().instantiateBackgroundThreads();
        }
    }

    /**
     * Auxiliary method for registering all key bindings.
     */
    public void registerKeyBindings()
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper controller: register key bindings method called");

        JPanel P = _M._plotsWrapper;

        int[] rKC = KeyUtils.REGULAR_KEYS_CODES;
        int[] sKC = KeyUtils.SPECIAL_KEYS_CODES;

        for (int[] a : new int[][]{rKC, sKC})
        {
            for (int code : a)
            {
                P.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(code, 0, false), KeyEvent.getKeyText(code));
                P.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(code, 0, true), "released_" + KeyEvent.getKeyText(code));

                P.getActionMap().put(KeyEvent.getKeyText(code), new AbstractAction()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if (_activePlotID == null) return;
                        Integer idx = getPlotIDtoInArrayID(_activePlotID);
                        if (idx == null) return;
                        _M._wrappers[idx].getModel().getPlot().getController().getInteractListener().notifyKeyPressed(code);
                    }
                });

                P.getActionMap().put("released_" + KeyEvent.getKeyText(code), new AbstractAction()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if (_activePlotID == null) return;
                        Integer idx = getPlotIDtoInArrayID(_activePlotID);
                        if (idx == null) return;
                        _M._wrappers[idx].getModel().getPlot().getController().getInteractListener().notifyKeyReleased(code);
                    }
                });

            }
        }
    }

    /**
     * Instantiates and registers listeners (also calls the same method for descendants (wrappers -> plots)).
     */
    public void instantiateListeners()
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper controller: instantiate listeners method called");

        _mousePressedLooseFocus = new MousePressedLooseFocus(_M._GC);
        _plotsWrapper.addMouseListener(_mousePressedLooseFocus);

        for (AbstractPlotWrapper w : _M._wrappers)
        {
            if (w == null) continue;
            w.getController().instantiateListeners();
        }
    }

    /**
     * Unregisters listeners (also calls the same method for descendants (wrappers -> plots)).
     */
    public void unregisterListeners()
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper controller: unregister listeners method called");

        _plotsWrapper.removeMouseListener(_mousePressedLooseFocus);

        for (AbstractPlotWrapper w : _M._wrappers)
        {
            if (w == null) continue;
            w.getController().unregisterListeners();

        }
    }

    /**
     * Requests focus on the first valid (not null) plot.
     */
    public void requestFocusOnTheFirstValidPlot()
    {
        Notification.printNotification(_M._GC, null, "Plot container controller: request focus on the first valid plot method called");

        for (AbstractPlotWrapper w : _M._wrappers)
        {
            if (w == null) continue;
            requestFocusOn(w.getModel().getPlotID());
            break;
        }
    }

    /**
     * Sets off the focus for all plots.
     * The method also notifies all {@link listeners.interact.AbstractInteractListener}
     * that all keys and mouse buttons have been released.
     */
    public void looseFocusToAllPlots()
    {
        Notification.printNotification(_M._GC, null, "Plot container controller: loose focus to all method called");

        for (AbstractPlotWrapper w : _M._wrappers)
        {
            if (w == null) continue;
            w.getModel().getPlot().getController().setFocused(false);
            w.getModel().getPlot().getController().getInteractListener().notifyAllKeysReleased();
            w.getModel().getPlot().getController().getInteractListener().notifyAllMouseButtonsReleased();
        }

        _interactor.setActivePlot(null);
        _activePlotID = null;
    }


    /**
     * Gets in-array id for a plot with provided id (stored in -> in-array id mapping).
     *
     * @param plotID plot id
     * @return in-array id
     */
    public Integer getPlotIDtoInArrayID(int plotID)
    {
        if (_M._wrappers == null) return null;

        Integer idx = null;
        for (int i = 0; i < _M._wrappers.length; i++)
        {
            if (_M._wrappers[i] != null)
                if (_M._wrappers[i].getModel().getPlotID() == plotID)
                {
                    idx = i;
                    break;
                }
        }
        return idx;
    }

    /**
     * Sets which plot is focused.
     *
     * @param plotID od of a plot that gained focus
     */
    public void requestFocusOn(int plotID)
    {
        if (_M._wrappers == null) return;
        Integer idx = getPlotIDtoInArrayID(plotID);
        if (idx == null) return;

        Notification.printNotification(_M._GC, null, "Plot container controller: focus requested (plot ID = " + plotID + ", in-array index = " + idx + ")");

        for (int i = 0; i < _M._wrappers.length; i++)
        {
            if (i != idx)
            {
                if (_M._wrappers[i] == null) continue;
                _M._wrappers[i].getModel().getPlot().getController().setFocused(false);
                _M._wrappers[i].getModel().getPlot().getController().getInteractListener().notifyAllKeysReleased();
                _M._wrappers[i].getModel().getPlot().getController().getInteractListener().notifyAllMouseButtonsReleased();
            }
        }

        _M._wrappers[idx].getModel().getPlot().getController().setFocused(true);
        if (_interactor != null) _interactor.setActivePlot(_M._wrappers[idx].getModel().getPlot());
        _activePlotID = plotID;
    }

    /**
     * Can be called to add a reporter (internal timer is instantiated here; object notifies on the wrapper performance).
     *
     * @param reporter reporter object
     */
    public void addReporter(AbstractPlotsWrapperReporter reporter)
    {
        reporter.instantiateTimer();
        _reporters.add(reporter);
    }

    /**
     * Getter for the active plot ID (if null -> no plot is active).
     *
     * @return active plot ID
     */
    public Integer getActivePlotID()
    {
        return _activePlotID;
    }

    /**
     * Getter for the active plot.
     *
     * @return active plot
     */
    public AbstractPlot getActivePlot()
    {
        if (_activePlotID == null) return null;
        Integer aID = getPlotIDtoInArrayID(_activePlotID);
        if (aID == null) return null;
        return _M._wrappers[aID].getModel().getPlot();
    }


    /**
     * Upon termination, this method is called to cancel all ongoing background threads.
     */
    public void disposeAllBackgroundThreads()
    {
        // queue
        disposeAllQueuedWorkersAndQueue();

        // animator
        if (_animator != null)
        {
            _animator.deactivate();
            _animator.cancel(true);
            _animator = null;
        }

        // interactions
        if (_interactor != null)
        {
            _interactor.deactivate();
            _interactor.cancel(true);
            _interactor = null;
        }

        // reporters
        for (AbstractPlotsWrapperReporter r : _reporters) r.dispose();
    }

    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_M._GC, null, "Plots wrapper controller: dispose method called");
        _M = null;
        _mousePressedLooseFocus = null;
        _reporters = null;
    }

}
