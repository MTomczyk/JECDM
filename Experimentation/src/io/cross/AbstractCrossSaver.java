package io.cross;


import container.scenario.AbstractScenarioDataContainer;
import exception.CrossedScenariosException;
import exception.ScenarioException;
import executor.CrossSummarizer;
import indicator.IIndicator;
import io.AbstractIO;
import scenario.CrossedScenarios;
import scenario.Scenario;
import statistics.IStatistic;
import unified.UnifiedIndicators;
import unified.UnifiedStatistics;

import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * Abstract implementation of {@link ICrossSaver}. Provides common fields/functionalities.
 *
 * @author MTomczyk
 */
public abstract class AbstractCrossSaver extends AbstractIO implements ICrossSaver
{
    /**
     * File output stream object that can be used to store the data.
     */
    protected FileOutputStream _fileOutputStream;

    /**
     * Unified indicators (unified across all crossed scenarios).
     */
    protected UnifiedStatistics _uStatistics;

    /**
     * Unified statistics (unified across all crossed scenarios).
     */
    protected UnifiedIndicators _uIndicators;

    /**
     * Field storing data on the current scenario's expected last generation number.
     */
    protected int _lastGeneration;

    /**
     * Auxiliary field storing the generations limit reported when notifying the scenario.
     */
    protected Integer[] _generationsPerScenario;

    /**
     * Auxiliary matrix indicating whether an indicator (the second dimension) was used in a scenario (the first dimension)
     */
    protected boolean[][] _indicatorUsedPerScenario;

    /**
     * Auxiliary matrix indicating whether a statistic function (the third dimension) was used along with an indicator
     * (the second dimension) when processing a scenario (the first dimension).
     */
    protected boolean[][][] _statisticUsedPerScenarioIndicator;

    /**
     * Auxiliary field for storing the maximum number of generations considered within the scope being processed.
     */
    protected Integer _maxGenerations;

    /**
     * Statistic functions being currently used.
     */
    protected IStatistic[] _currentStatistics;

    /**
     * Reference to the scenario data container being currently processed.
     */
    protected AbstractScenarioDataContainer _currentSDC;

    /**
     * Scenario being currently processed.
     */
    protected Scenario _currentScenario;

    /**
     * Index of a scenario being currently processed.
     */
    protected int _currentScenarioIndex;

    /**
     * Index of a indicator being currently processed.
     */
    protected int _currentIndicatorIndex;


    /**
     * Parameterized constructor.
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @param level            the level of cross-analysis (should be at least 2)
     */
    public AbstractCrossSaver(String path, String filename, CrossedScenarios crossedScenarios, int level)
    {
        super(path, filename, crossedScenarios, level);
    }

    /**
     * Creates a new instance of the object. Intended to be used by {@link CrossSummarizer} to clone the
     * initial object instance one time per each crossed scenarios object involved (i.e., one clone will be mapped
     * to one such object).
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @return new object instance
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    @Override
    public ICrossSaver getInstance(String path, String filename, CrossedScenarios crossedScenarios) throws CrossedScenariosException
    {
        throw new CrossedScenariosException("The \"get instance\" method is not implemented", this.getClass(), crossedScenarios);
    }


    /**
     * Method for notifying the object about unified indicators and statistics (unified across all crossed scenarios).
     *
     * @param indicators unified indicators
     * @param statistics unified statistics
     */
    @Override
    public void notifyAboutUnifiedData(UnifiedIndicators indicators, UnifiedStatistics statistics)
    {
        _uIndicators = indicators;
        _uStatistics = statistics;
    }


    /**
     * Returns the level (dimensionality) the saver can operate with.
     *
     * @return the dedicated level
     */
    @Override
    public int getDedicatedLevel()
    {
        return 2;
    }

    /**
     * Returns a suffix intended to be added to the file name (including the file extension).
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return null;
    }

    /**
     * Creates and returns the full path based on the folder path, filename, and saver's suffix (e.g., extension).
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @return full path
     */
    @Override
    protected String getFullPath(String path, String filename)
    {
        return super.getFullPath(path, filename) + getFileSuffix();
    }

    /**
     * The implementation should create a file (and overwrite it if already exists) and instantiate the output stream.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown (e.g., then the requested path is invalid)
     */
    @Override
    public void create() throws CrossedScenariosException
    {
        throw new CrossedScenariosException("The \"create\" method is not implemented", this.getClass(), _crossedScenarios);
    }


    /**
     * Method for notifying the savers that the processing begins (prior to executing any scenario).
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    @Override
    public void notifyProcessingBegins() throws CrossedScenariosException
    {
        int noScenarios = _crossedScenarios.getReferenceScenariosSorted().length;
        int noInds = _uIndicators._entities.size();
        int noStats = _uStatistics._entities.size();
        _generationsPerScenario = new Integer[noScenarios];
        _indicatorUsedPerScenario = new boolean[noScenarios][noInds];
        _statisticUsedPerScenarioIndicator = new boolean[noScenarios][noInds][noStats];
        Arrays.fill(_generationsPerScenario, null);
    }

    /**
     * Method for notifying the savers that the processing begins.
     *
     * @param scenario scenario that is to be processed
     * @param SDC      scenario data container linked to the scenario being currently processed (read-only)
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    @Override
    public void notifyScenarioProcessingBegins(Scenario scenario, AbstractScenarioDataContainer SDC) throws CrossedScenariosException
    {
        _currentScenario = scenario;
        _currentScenarioIndex = _crossedScenarios.getReferenceScenariosSortedMap().get(scenario.toString());
        _currentStatistics = SDC.getStatisticFunctions();
        _currentSDC = SDC;
        _generationsPerScenario[_currentScenarioIndex] = SDC.getGenerations();
        _lastGeneration = SDC.getGenerations() - 1;
    }

    /**
     * A method for notifying the saver that the processing of indicator-related data begins.
     *
     * @param indicator indicator
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingBegins(IIndicator indicator) throws CrossedScenariosException
    {
        int scID = _crossedScenarios.getReferenceScenariosSortedMap().get(_currentSDC.getScenario().toString());
        int indID = _uIndicators._entitiesMap.get(indicator.getName());
        _currentIndicatorIndex = indID;
        _indicatorUsedPerScenario[scID][indID] = true;
        for (IStatistic s : _currentStatistics)
        {
            int statID = _uStatistics._entitiesMap.get(s.toString());
            _statisticUsedPerScenarioIndicator[scID][indID][statID] = true;
        }
    }

    /**
     * The main method for pushing data to be stored. The data provided is assumed to come from all trials concerning
     * one generation and one indicator (the generation number is not provided; it is assumed that all generations per
     * indicator are provided sequentially). IMPORTANT NOTE: The data supplied is supposed to be organized. The double
     * vectors from different indicators cannot be interlaced. As mentioned above, these vectors will also come in the
     * ascending order of generations, and each will completely cover the results obtained within a generation.
     *
     * @param trialResults trial results
     * @param statistics   statistics calculated from trial results (1:1 mapping with statistic objects stored in {@link container.scenario.AbstractScenarioDataContainer})
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void pushData(double[] trialResults, double[] statistics, int generation) throws ScenarioException
    {
        throw new ScenarioException("The \"push data\" method is not implemented", this.getClass(), _scenario);
    }

    /**
     * A method for notifying the saver that the processing of indicator-related data ends.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingEnds() throws CrossedScenariosException
    {
        throw new CrossedScenariosException("The \"notify indicator processing ends\" method is not implemented", this.getClass(), _crossedScenarios);
    }


    /**
     * Method for notifying the savers that the processing ends.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    @Override
    public void notifyScenarioProcessingEnds() throws CrossedScenariosException
    {
        throw new CrossedScenariosException("The \"notify scenario processing ends\" method is not implemented", this.getClass(), _crossedScenarios);
    }

    /**
     * Method for notifying the savers that the processing begins (after all scenarios are processed).
     */
    @Override
    public void notifyProcessingEnds() throws CrossedScenariosException
    {
        throw new CrossedScenariosException("The \"notify processing ends\" method is not implemented", this.getClass(), _crossedScenarios);
    }

    /**
     * The implementation should close the maintained output stream.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown
     */
    @Override
    public void close() throws CrossedScenariosException
    {
        throw new CrossedScenariosException("The \"close\" method is not implemented", this.getClass(), _crossedScenarios);
    }

    /**
     * Can be implemented to notify {@link CrossSummarizer} that the saver's instance (creation) should be skipped given
     * the crossed scenarios setting.
     *
     * @param crossedScenarios crossed scenarios
     * @return true, if the instance should be skipped; false otherwise
     */
    @Override
    public boolean shouldBeSkipped(CrossedScenarios crossedScenarios)
    {
        return false;
    }
}
