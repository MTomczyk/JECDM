package runner;

import ea.EA;
import ea.EATimestamp;
import exception.RunnerException;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import runner.threads.DisplayAndWaitThread;
import visualization.IVisualization;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Default (abstract) implementation of the {@link runner.IRunner} interface (responsible for executing EAs).
 *
 * @author MTomczyk
 */

public abstract class AbstractRunner implements IRunner
{
    /**
     * Params container.
     */
    public static class Params
    {

        /**
         * Evolutionary algorithm(s).
         */
        public EA[] _eas;

        /**
         * The number of steady-state iterations.
         * Each element represents the number of steady-state repeats for a different EA (1:1 mapping).
         * Hence, the array length equals the number of EAs.
         */
        public int[] _steadyStateRepeats;

        /**
         * Optional object responsible for performing visualization (can be null -> visualization-related functionality is ignored).
         */
        public IVisualization _visualization;

        /**
         * Display Mode flag (see flags descriptions).
         */
        public DisplayMode _displayMode = DisplayMode.AT_THE_END;

        /**
         * Updaters mode flag (see flags descriptions).
         */
        public UpdaterMode _updaterMode = UpdaterMode.AFTER_GENERATION;

        /**
         * Parameterized constructor.
         *
         * @param eas                evolutionary algorithms
         * @param steadyStateRepeats the number of steady-state iterations
         */
        public Params(EA[] eas, int steadyStateRepeats)
        {
            this(eas, steadyStateRepeats, null);
        }

        /**
         * Parameterized constructor.
         *
         * @param eas                evolutionary algorithms
         * @param steadyStateRepeats the number of steady-state iterations (the same for each evolutionary algorithm)
         * @param visualization      optional visualization (plot); can be null -> not displayed
         */
        public Params(EA[] eas, int steadyStateRepeats, IVisualization visualization)
        {
            _eas = eas;
            _steadyStateRepeats = new int[eas.length];
            for (int i = 0; i < eas.length; i++) _steadyStateRepeats[i] = steadyStateRepeats;
            _visualization = visualization;
        }

        /**
         * Parameterized constructor.
         *
         * @param eas                evolutionary algorithms
         * @param steadyStateRepeats the number of steady-state iterations; each element represents the number of
         *                           steady-state repeats for a different EA (1:1 mapping); hence, the array length
         *                           should equal the number of EAs
         * @param visualization      optional visualization (plot); can be null -> not displayed
         */
        public Params(EA[] eas, int[] steadyStateRepeats, IVisualization visualization)
        {
            _eas = eas;
            _steadyStateRepeats = steadyStateRepeats;
            _visualization = visualization;
        }
    }


    /**
     * Evolutionary algorithm(s).
     */
    protected final EA[] _eas;

    /**
     * The number of steady-state iterations.
     * Each element represents the number of steady-state repeats for a different EA (1:1 mapping).
     * Hence, the array length equals the number of EAs.
     */
    protected final int[] _steadyStateRepeats;

    /**
     * Optional object(s) responsible for performing visualization (can be null -> visualization-related functionality is ignored).
     */
    protected final IVisualization _visualization;

    /**
     * Display Mode flag (see flags descriptions).
     */
    protected final DisplayMode _displayMode;

    /**
     * Run updaters mode (see flags descriptions).
     */
    protected final UpdaterMode _updatersMode;

    /**
     * Cyclic barrier synchronizing plot displaying with executing EAs.
     */
    protected CyclicBarrier _displayAndWaitBarrier;

    /**
     * Executes a thread displaying the visualization modules and waiting till they are displayed.
     */
    protected Executor _displayAndWaitExecutor;

    /**
     * Instantiates a thread that displays the plot and waits till they are displayed before executing EAs (matters only if plots are supposed to be displayed from the beginning).
     */
    protected DisplayAndWaitThread _displayAndWaitThread;


    /**
     * Parameterized constructor.
     *
     * @param p params contained
     */
    public AbstractRunner(Params p)
    {
        _eas = p._eas;
        _steadyStateRepeats = p._steadyStateRepeats;
        assert _eas.length == _steadyStateRepeats.length;
        _visualization = p._visualization;
        _displayMode = p._displayMode;
        _updatersMode = p._updaterMode;
        instantiateThreads();
    }

    /**
     * Auxiliary method for instantiating threads.
     */
    protected void instantiateThreads()
    {
        instantiateDisplayAndWaitThread();
    }

    /**
     * Instantiates a thread that displays the plot and waits till it is displayed (window opened) before executing EAs
     * (matters only if plots are supposed to be displayed from the beginning).
     */
    protected void instantiateDisplayAndWaitThread()
    {
        if (_visualization == null) return;

        if (_displayMode == DisplayMode.FROM_THE_BEGINNING)
        {
            _displayAndWaitBarrier = new CyclicBarrier(2);
            _displayAndWaitThread = new DisplayAndWaitThread(_displayAndWaitBarrier, _visualization);
            _displayAndWaitExecutor = Executors.newFixedThreadPool(1);
        }
        else
        {
            _displayAndWaitBarrier = null;
            _displayAndWaitThread = null;
            _displayAndWaitExecutor = null;
        }
    }

    /**
     * Performs the evolution (from scratch, i.e., calls inits etc.) for the specified number of generations.
     *
     * @param generations specified number of generations (the same for each evolutionary algorithm)
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    @Override
    public void executeEvolution(int generations) throws RunnerException
    {
        try
        {
            int[] g = new int[_eas.length];
            for (int i = 0; i < _eas.length; i++) g[i] = generations;
            executeEvolution(g);
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("execute evolution", e);
        }
    }


    /**
     * Performs the evolution (from scratch, i.e., calls inits etc.) for the specified number of generations.
     *
     * @param generations specified number of generations; each element corresponds to a different evolutionary algorithm
     *                    (1:1) mapping; hence, the array length should equal the number of algorithms
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    @Override
    public void executeEvolution(int[] generations) throws RunnerException
    {
        try
        {
            if (generations.length != _eas.length)
                throw new RunnerException("The number of generation limits is different from the number of evolutionary algorithms", this.getClass());

            init(); // Generation 0 is for creating the initial population

            int maxGenerations = -1;
            for (Integer g : generations) if (g > maxGenerations) maxGenerations = g;
            for (int g = 1; g < maxGenerations; g++) executeSingleGeneration(g, generations);
            stop();
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("execute evolution", e);
        }
    }

    /**
     * Initializes EAs. Default implementation: the method runs pre, main, and post-init phases (in the given order).
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    @Override
    public void init() throws RunnerException
    {
        try
        {
            preInitPhase();
            mainInitPhase();
            postInitPhase();
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("init", e);
        }
    }

    /**
     * Pre init phase (executed before the main init phase); default implementation can potentially start the visualization module.
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void preInitPhase() throws RunnerException
    {
        try
        {
            if (_visualization == null) return;
            if (_displayMode != DisplayMode.FROM_THE_BEGINNING) return;

            _displayAndWaitBarrier.reset();
            _displayAndWaitExecutor.execute(_displayAndWaitThread);

            try
            {
                _displayAndWaitBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e)
            {
                wrapException("pre init phase", e);
            }
        } catch (Exception e)
        {
            wrapException("pre init phase", e);
        }
    }


    /**
     * Main init step.
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void mainInitPhase() throws RunnerException
    {
        try
        {
            for (EA ea : _eas) ea.init();
        } catch (Exception e)
        {
            wrapException("main init phase", e);
        }
    }

    /**
     * Post-init phase (executed after the main init phase);
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void postInitPhase() throws RunnerException
    {
        try
        {
            if (_visualization == null) return;
            if (_updatersMode.equals(UpdaterMode.AFTER_GENERATION)) _visualization.updateData();
        } catch (Exception e)
        {
            wrapException("post init phase", e);
        }
    }

    /**
     * Performs a single generation of EAs. Default implementation: the method runs pre, main, and post-init phases (in the given order).
     *
     * @param generation       current generation number
     * @param generationLimits limits for the number of generations an EA is allowed run (one element per each EA, 1:1
     *                         mapping; note that the generation counter starts from 0); this field excludes those EAs
     *                         who have reached their generation limit; can be null -> not used
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    @Override
    public void executeSingleGeneration(int generation, int[] generationLimits) throws RunnerException
    {
        try
        {
            preExecuteSingleGenerationPhase(generation, generationLimits);
            mainExecuteSingleGenerationPhase(generation, generationLimits);
            postExecuteSingleGenerationPhase(generation, generationLimits);
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("execute single generation", e);
        }

    }

    /**
     * Pre-"execute single generation phase" (executed before the main step phase).
     *
     * @param generation       current generation number
     * @param generationLimits limits for the number of generations an EA is allowed run (one element per each EA, 1:1
     *                         mapping; note that the generation counter starts from 0); this field excludes those EAs
     *                         who have reached their generation limit; can be null -> not used
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */

    protected void preExecuteSingleGenerationPhase(int generation, int[] generationLimits) throws RunnerException
    {

    }


    /**
     * Main "execute single generation" phase.
     *
     * @param generation       current generation number
     * @param generationLimits limits for the number of generations an EA is allowed run (one element per each EA, 1:1
     *                         mapping; note that the generation counter starts from 0); this field excludes those EAs
     *                         who have reached their generation limit.
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void mainExecuteSingleGenerationPhase(int generation, int[] generationLimits) throws RunnerException
    {
        try
        {
            for (int eaID = 0; eaID < _eas.length; eaID++)
            {
                if ((generationLimits != null) && (generation >= generationLimits[eaID]))
                {
                    continue; // generation limit reached
                }
                EA ea = _eas[eaID];
                for (int r = 0; r < _steadyStateRepeats[eaID]; r++)
                {
                    executeSingleSteadyStateRepeat(ea, generation, r);
                }
            }
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("main execute single generation phase", e);
        }
    }

    /**
     * Post-"execute single generation" phase.
     *
     * @param generation       current generation number
     * @param generationLimits limits for the number of generations an EA is allowed run (one element per each EA, 1:1
     *                         mapping; note that the generation counter starts from 0) this field excludes those EAs
     *                         who have reached their generation limit; can be null -> not used
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void postExecuteSingleGenerationPhase(int generation, int[] generationLimits) throws RunnerException
    {
        try
        {
            if (_visualization == null) return;
            if (_updatersMode.equals(UpdaterMode.AFTER_GENERATION)) _visualization.updateData();
        } catch (Exception e)
        {
            wrapException("post execute single generation phase", e);
        }
    }

    /**
     * Executes a single steady-state repeat of an EA. Default implementation: the method runs pre, main, and post-init phases (in the given order).
     *
     * @param ea                evolutionary algorithm whose single steady-state repeat is to be executed
     * @param generation        current generation number
     * @param steadyStateRepeat current steady-state repeat number
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    @Override
    public void executeSingleSteadyStateRepeat(EA ea, int generation, int steadyStateRepeat) throws RunnerException
    {
        try
        {
            preExecuteSingleSteadyStateRepeat(ea, generation, steadyStateRepeat);
            mainExecuteSingleSteadyStateRepeat(ea, generation, steadyStateRepeat);
            postExecuteSingleSteadyStateRepeat(ea, generation, steadyStateRepeat);
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("execute single steady state repeat", e);
        }
    }

    /**
     * Pre-"execute single steady-state repeat phase" of an EA (executed before the main step phase).
     *
     * @param ea                evolutionary algorithm whose single steady-state repeat is to be executed
     * @param generation        current generation number
     * @param steadyStateRepeat current steady-state repeat number
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void preExecuteSingleSteadyStateRepeat(EA ea, int generation, int steadyStateRepeat) throws RunnerException
    {

    }

    /**
     * Main "execute single steady-state repeat" of an EA phase.
     *
     * @param ea                evolutionary algorithm whose single steady-state repeat is to be executed
     * @param generation        current generation number
     * @param steadyStateRepeat current steady-state repeat number
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void mainExecuteSingleSteadyStateRepeat(EA ea, int generation, int steadyStateRepeat) throws RunnerException
    {
        try
        {
            ea.step(new EATimestamp(generation, steadyStateRepeat));
        } catch (Exception e)
        {
            wrapException("main execute single steady state repeat", e);
        }

    }

    /**
     * Post-"execute single steady-state repeat" of an EA phase.
     *
     * @param ea                evolutionary algorithm whose single steady-state repeat is to be executed
     * @param generation        current generation number
     * @param steadyStateRepeat current steady-state repeat number
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void postExecuteSingleSteadyStateRepeat(EA ea, int generation, int steadyStateRepeat) throws RunnerException
    {
        try
        {
            if (_visualization == null) return;
            if (_updatersMode.equals(UpdaterMode.AFTER_STEADY_STATE_REPEAT)) _visualization.updateData();
        } catch (Exception e)
        {
            wrapException("post execute single steady state repeat", e);
        }
    }


    /**
     * Stops simulations. Default implementation: the method runs pre, main, and post-init phases (in the given order).
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    @Override
    public void stop() throws RunnerException
    {
        try
        {
            preStopPhase();
            mainStopPhase();
            postStopPhase();
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("stop", e);
        }
    }

    /**
     * Pre-stop phase (executed before the main stop phase).
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void preStopPhase() throws RunnerException
    {

    }

    /**
     * Main stop phase. Can potentially display visualization.
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void mainStopPhase() throws RunnerException
    {
        try
        {
            if (_visualization == null) return;
            if (_updatersMode == UpdaterMode.AT_THE_END) _visualization.updateData();
            if (_displayMode == DisplayMode.AT_THE_END)
            {
                _visualization.init();
                _visualization.display();
            }
        } catch (Exception e)
        {
            wrapException("main stop phase", e);
        }
    }

    /**
     * Post-stop phase (executed after the main stop phase).
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    protected void postStopPhase() throws RunnerException
    {

    }

    /**
     * Optional method for terminating the execution and clearing data.
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    @Override
    public void dispose() throws RunnerException
    {
        try
        {
            if (_visualization != null) _visualization.dispose();
            _displayAndWaitExecutor = null;
            _displayAndWaitBarrier = null;
        } catch (Exception e)
        {
            wrapException("dispose", e);
        }
    }

    /**
     * Auxiliary method for wrapping (and casting) the general exception.
     *
     * @param method name of the method that triggered the exception
     * @param e      exception
     * @throws RunnerException wrapped exception
     */
    protected void wrapException(String method, Exception e) throws RunnerException
    {
        throw new RunnerException("Exception occurred when executing the '" + method + "' method (reason = " +
                e.getMessage() + ")", this.getClass(), e);
    }

    /**
     * Sets the number of steady-state repeats for a given EA (pointed by index). The method terminates
     * if the internal steadyStateRepeats is null or the pointer is invalid.
     * @param steadyStateRepeats new steady-state repeats number
     * @param index index pointing to the EA
     */
    @Override
    public void setSteadyStateRepeatsFor(int steadyStateRepeats, int index)
    {
        if (_steadyStateRepeats == null) return;
        if (index >= _steadyStateRepeats.length) return;
        if (index < 0) return;
        _steadyStateRepeats[index] = steadyStateRepeats;
    }
}
