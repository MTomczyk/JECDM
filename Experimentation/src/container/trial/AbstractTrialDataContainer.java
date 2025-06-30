package container.trial;

import container.AbstractDataContainer;
import container.global.AbstractGlobalDataContainer;
import container.scenario.AbstractScenarioDataContainer;
import container.trial.initialziers.IEAInitializer;
import container.trial.initialziers.IProblemBundleInitializer;
import container.trial.initialziers.IRunnerInitializer;
import ea.EA;
import exception.TrialException;
import indicator.IIndicator;
import io.trial.BinarySaver;
import io.trial.ITrialSaver;
import io.trial.TSPerIndicator;
import problem.AbstractProblemBundle;
import random.IRandom;
import runner.IRunner;
import scenario.Scenario;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * A container class that limits the scope to a single scenario trial (i.e., the problem is selected, the algorithm,
 * etc., and the trial ID of a scenario). This abstract class provides various fields and methods that can be commonly
 * used in diverse implementations. Important note: this class is supposed to be extended, and its method is to be
 * overwritten to impose some interpretability.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public abstract class AbstractTrialDataContainer extends AbstractDataContainer
{
    /**
     * Params container
     */
    public static class Params extends TrialDataContainerFactory.Params
    {
        /**
         * The scenario data container.
         */
        public AbstractScenarioDataContainer _SDC;

        /**
         * TrialID the unique (in scenario) ID assigned to the test run.
         */
        public final int _trialID;

        /**
         * Specifies a list of objects for storing per-trial data (one file per each indicator and saver used).
         * At this point, the list should already contain a binary saver {@link io.trial.BinarySaver}.
         */
        public LinkedList<ITrialSaver> _trialSavers;

        /**
         * Random number generator associated with this trial.
         */
        public IRandom _R;

        /**
         * Abstract problem bundle (created by {@link IProblemBundleInitializer}).
         */
        public AbstractProblemBundle _problemBundle;

        /**
         * Parameterized constructor.
         *
         * @param SDC     the scenario data container
         * @param trialID trialID the unique (in scenario) ID assigned to the test run
         */
        public Params(AbstractScenarioDataContainer SDC, int trialID)
        {
            _SDC = SDC;
            _trialID = trialID;
        }
    }

    /**
     * Reference to the global data container.
     */
    protected AbstractGlobalDataContainer _GDC;

    /**
     * Reference to the global data container.
     */
    protected AbstractScenarioDataContainer _SDC;

    /**
     * The unique (in scenario) ID assigned to the test run.
     */
    protected final int _trialID;

    /**
     * Array of performance indicators.
     */
    protected IIndicator[] _indicators;

    /**
     * Main data matrix storing the results (indicators (rows) x generations (columns) ).
     */
    protected double[][] _results;

    /**
     * Instance of the evolutionary algorithm.
     */
    protected EA _EA;

    /**
     * Runner object responsible for performing the evolutionary process.
     */
    protected IRunner _runner;

    /**
     * Specifies a list of objects for storing per-trial data (one file per each indicator and saver used).
     * At this point, the list should already contain a binary saver {@link io.trial.BinarySaver}.
     */
    protected LinkedList<ITrialSaver> _trialSavers;

    /**
     * During the processing initialization, the reference trial savers (provided by the programmer) are cloned so that
     * each such saver produces one copy per indicator (per-indicator results are stored in different files).
     * These clones are stored as within this class's instances.
     */
    protected TSPerIndicator[] _trialSaversPerIndicator;

    /**
     * Object responsible for generating per-trial instances of evolutionary algorithms.
     */
    protected IEAInitializer _eaInitializer;

    /**
     * Object responsible for generating per-trial instances of runners responsible for managing the evolutionary process.
     */
    protected IRunnerInitializer _runnerInitializer;

    /**
     * Object responsible for initializing the per-scenario problem to be solved.
     */
    protected IProblemBundleInitializer _problemBundleInitializer;


    /**
     * Random number generator associated with this trial.
     */
    protected IRandom _R;

    /**
     * Parameterized constructor that also instantiates the data.
     *
     * @param p params container
     * @throws TrialException the exception can be thrown and passed to the main executor via the scenario executor
     */
    public AbstractTrialDataContainer(Params p) throws TrialException
    {
        super(p);
        _GDC = p._SDC.getGDC();
        _SDC = p._SDC;
        _trialID = p._trialID;
        _eaInitializer = p._eaInitializer;
        _runnerInitializer = p._runnerInitializer;
        _problemBundleInitializer = p._problemInitializer;
        instantiateData(p);
    }

    /**
     * The main method that calls the auxiliary ones to instantiate the trial-scenario-related data.
     *
     * @param p params container
     * @throws TrialException the exception can be thrown and passed to the main executor via the scenario executor
     */
    protected void instantiateData(Params p) throws TrialException
    {
        instantiateProblemBundle(p);
        instantiatePerformanceIndicators(p);
        instantiateReferenceTrialSavers(p);
        instantiateTrialSaversPerIndicator(p);

        instantiateEA(p._R, p);
        instantiateRunner(p);
        instantiateResults(p);

        p._validator.validateTrialSavers(_trialSavers);
        p._validator.validateEA(_EA);
        p._validator.validateRunner(_runner);
    }

    /**
     * Auxiliary method that instantiates the problem-related data
     *
     * @param p params container
     * @throws TrialException the signature allows the overwriting method to cast exceptions
     */
    protected void instantiateProblemBundle(Params p) throws TrialException
    {
        p._problemBundle = _problemBundleInitializer.instantiateProblem(p);
    }

    /**
     * The method for instantiating the performance indicators.
     *
     * @param p params container
     * @throws TrialException trial-level exception can be thrown 
     */
    private void instantiatePerformanceIndicators(Params p) throws TrialException
    {
        IIndicator[] referenceIndicators = p._SDC.getIndicators();
        _indicators = new IIndicator[referenceIndicators.length];
        for (int i = 0; i < referenceIndicators.length; i++)
            _indicators[i] = referenceIndicators[i].getInstance(p._SDC.getScenario(), p._trialID);
    }

    /**
     * The method for instantiating the trial savers.
     *
     * @param p params container
     * @throws TrialException trial-level will be thrown if the trail savers list does not contain a binary saver
     */
    private void instantiateReferenceTrialSavers(Params p) throws TrialException
    {
        _trialSavers = p._trialSavers;
        boolean contains = false;
        for (ITrialSaver ts : _trialSavers)
            if (ts instanceof BinarySaver)
            {
                contains = true;
                break;
            }
        if (!contains)
            throw new TrialException("The TDC is not supplied with a binary saver",
                    null, this.getClass(), _SDC.getScenario(), _trialID);
    }

    /**
     * The method for instantiating the trial savers (per indicator). Note: The reference trial savers (provided by the
     * programmer) are cloned so that each such saver produces one copy per indicator (per-indicator results are stored
     * in different files). These clones are stored in array, where each element directly corresponds to one saver.
     *
     * @param p params container
     * @throws TrialException trial-level exception can be thrown 
     */
    private void instantiateTrialSaversPerIndicator(Params p) throws TrialException
    {
        _trialSaversPerIndicator = new TSPerIndicator[_trialSavers.size()];
        int idx = 0;
        for (ITrialSaver trialSaver : _trialSavers)
        {
            _trialSaversPerIndicator[idx++] = new TSPerIndicator(trialSaver, _indicators, _SDC.getMainPath(),
                    _SDC.getScenario(), _trialID);
        }
    }

    /**
     * The method instantiates the evolutionary algorithm (to be overwritten)
     *
     * @param R random number generator
     * @param p params container
     * @throws TrialException trial-level exception can be thrown 
     */
    protected void instantiateEA(IRandom R, Params p) throws TrialException
    {
        _EA = _eaInitializer.instantiateEA(R, p);
    }

    /**
     * The method instantiates the runner (object responsible for conducting the evolutionary process).
     *
     * @param p params container
     * @throws TrialException trial-level exception can be thrown 
     */
    protected void instantiateRunner(Params p) throws TrialException
    {
        _runner = _runnerInitializer.instantiateRunner(_EA, p);
    }


    /**
     * The method for instantiating the result matrix.
     *
     * @param p params container
     * @throws TrialException trial-level exception can be thrown 
     */
    public void instantiateResults(Params p) throws TrialException
    {
        try
        {
            int columns = _SDC.getGenerations();
            if (_SDC.getDataStoreInterval() < _SDC.getGenerations()) columns = _SDC.getDataStoreInterval();
            _results = new double[_indicators.length][columns];
        } catch (Exception e)
        {
            throw new TrialException(e.getMessage(), this.getClass(), e, _SDC.getScenario(), _trialID);
        }
    }

    /**
     * Auxiliary method for creating trial-level results files.
     *
     * @throws TrialException trial-level exception can be thrown 
     */
    public void createResultsFiles() throws TrialException
    {
        for (TSPerIndicator ts : _trialSaversPerIndicator) ts.createResultsFiles();
    }

    /**
     * Auxiliary method for storing the results in the files.
     *
     * @param results current results matrix (rows = linked to indicators, columns = linked to generations)
     * @param offset  determines the offset in the columns (starting index)
     * @param length  determines the number of columns involved in data saving
     * @throws TrialException trial-level exception can be thrown 
     */
    public void pushResults(double[][] results, int offset, int length) throws TrialException
    {
        for (TSPerIndicator ts : _trialSaversPerIndicator)
            ts.pushResults(results, offset, length);
    }

    /**
     * Auxiliary method for closing trial-level results files (closes, e.g., FileOutputStreams).
     *
     * @throws TrialException trial-level exception can be thrown 
     */
    public void closeResultsFiles() throws TrialException
    {
        for (TSPerIndicator ts : _trialSaversPerIndicator) ts.closeResultsFiles();
    }

    /**
     * The method for clearing the already instantiated result matrix.
     *
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    public void clearResults() throws TrialException
    {
        try
        {
            for (double[] r : _results) Arrays.fill(r, 0.0d);
        } catch (Exception e)
        {
            throw new TrialException(e.getMessage(), this.getClass(), e, _SDC.getScenario(), _trialID);
        }
    }

    /**
     * Getter for the result matrix.
     *
     * @return the result matrix
     */
    public double[][] getReferenceToResults()
    {
        return _results;
    }

    /**
     * Getter for the instance of the evolutionary algorithm.
     *
     * @return the instance of the evolutionary algorithm
     */
    public EA getEA()
    {
        return _EA;
    }

    /**
     * Getter for the runner object (responsible for performing the evolution).
     *
     * @return the runner object
     */
    public IRunner getRunner()
    {
        return _runner;
    }

    /**
     * Getter for the performance indicators.
     *
     * @return performance indicators
     */
    public IIndicator[] getPerformanceIndicators()
    {
        return _indicators;
    }

    /**
     * Getter for the trial savers.
     *
     * @return trial savers
     */
    public LinkedList<ITrialSaver> getTrialSavers()
    {
        return _trialSavers;
    }

    /**
     * Returns the string representation (the same as for the wrapped {@link Scenario} object).
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _SDC.toString() + "_" + _trialID;
    }

    /**
     * Getter for the global data container.
     *
     * @return global data container
     */
    public AbstractGlobalDataContainer getGDC()
    {
        return _SDC.getGDC();
    }

    /**
     * Getter for the scenario data container
     *
     * @return scenario data container
     */
    public AbstractScenarioDataContainer getSDC()
    {
        return _SDC;
    }


    /**
     * Auxiliary method for clearing the data.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        if (_indicators != null) for (IIndicator i : _indicators) i.dispose();
        _results = null;
        _indicators = null;
        _GDC = null;
        _SDC = null;
        _EA = null;
        _runner = null;
        _trialSavers = null;
        for (TSPerIndicator ts : _trialSaversPerIndicator) ts.dispose();
        _trialSaversPerIndicator = null;
        _runnerInitializer = null;
        _eaInitializer = null;
    }

}
