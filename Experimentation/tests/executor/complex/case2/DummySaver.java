package executor.complex.case2;

import exception.ScenarioException;
import executor.ScenariosSummarizer;
import indicator.IIndicator;
import io.scenario.AbstractScenarioSaver;
import io.scenario.IScenarioSaver;
import scenario.Scenario;
import statistics.IStatistic;


/**
 * Dummy saver throwing an exception when attempting to create a file.
 *
 * @author MTomczyk
 */
class DummySaver extends AbstractScenarioSaver implements IScenarioSaver
{

    /**
     * Parameterized constructor.
     */
    public DummySaver()
    {
        this("", "", null, null, null, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param path       full path to the folder where the file should be stored (without a path separator)
     * @param filename   the filename (without the suffix, e.g., extension)
     * @param scenario   currently processed scenario
     * @param trialIDs   trial IDs
     * @param indicators performance indicators employed when assessing the performance of EAs.
     * @param statistics statistic functions used to aggregate the data
     */
    public DummySaver(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators, IStatistic[] statistics)
    {
        super(path, filename, scenario, trialIDs, indicators, statistics);
    }

    /**
     * Creates a new instance of the object. Intended to be used by {@link ScenariosSummarizer} to clone the
     * initial object instance one time per each scenario involved (i.e., one clone will be mapped to one scenario).
     *
     * @param path       full path to the folder where the file should be stored (without a path separator)
     * @param filename   the filename (without the suffix, e.g., extension)
     * @param scenario   scenario being currently processed
     * @param trialIDs   trial IDs
     * @param indicators performance indicators employed when assessing the performance of EAs.
     * @param statistics statistic functions used to aggregate the data
     * @return new object instance
     * @throws ScenarioException scenario-level exception can be cast 
     */
    @Override
    public IScenarioSaver getInstance(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators, IStatistic[] statistics) throws ScenarioException
    {
        return new DummySaver(path, filename, scenario, trialIDs, indicators, statistics);
    }

    @Override
    public String getFileSuffix()
    {
        return ".dummy";
    }

    /**
     * The implementation should create a file (and overwrite it if already exists) and instantiate the output stream.
     *
     * @throws ScenarioException scenario-level exception can be thrown (e.g., then the requested path is invalid)
     */
    @Override
    public void create() throws ScenarioException
    {
      throw new ScenarioException("Dummy exception (create)",  null, this.getClass(), _scenario);
    }

    /**
     * The implementation should close the maintained output stream.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void close() throws ScenarioException
    {

    }
}
