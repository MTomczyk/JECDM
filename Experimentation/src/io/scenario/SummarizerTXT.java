package io.scenario;

import exception.ScenarioException;
import executor.ScenariosSummarizer;
import indicator.IIndicator;
import scenario.Scenario;
import statistics.IStatistic;

import java.io.*;

/**
 * A simple implementation of {@link IScenarioSaver}. The processed data is saved in a txt file. The data stored consists
 * of chunks, each related to a different performance indicator. Each chunk begins with a line being an indicator's name.
 * The next column stores the statistic functions' names. The third row indicates the number of data lines. The remaining
 * lines are results obtained throughout generations (each generation is a separate line; the data is stored sequentially).
 * These rows provide numbers consistent with the header (column containing statistic names). The space is used as a data
 * separator, while a dot is used as a decimal separator.
 *
 * @author MTomczyk
 */
public class SummarizerTXT extends AbstractScenarioSaver implements IScenarioSaver
{
    /**
     * Used to write strings.
     */
    protected PrintWriter _writer;

    /**
     * Default constructor.
     */
    public SummarizerTXT()
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
    protected SummarizerTXT(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators, IStatistic[] statistics)
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
     * @throws ScenarioException scenario-level exception can be cast and propagated higher
     */
    @Override
    public IScenarioSaver getInstance(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators, IStatistic[] statistics) throws ScenarioException
    {
        return new SummarizerTXT(path, filename, scenario, trialIDs, indicators, statistics);
    }

    /**
     * Returns the file suffix (includes .txt)
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return ".txt";
    }

    /**
     * The implementation should create a file (and overwrite it if already exists) and instantiate the output stream.
     *
     * @throws ScenarioException scenario-level exception can be thrown (e.g., then the requested path is invalid)
     */
    @Override
    public void create() throws ScenarioException
    {
        File file = getFileAtScenarioLevel();

        try
        {
            _fileOutputStream = new FileOutputStream(file, false);
            _writer = new PrintWriter(_fileOutputStream);
        } catch (FileNotFoundException e)
        {
            throw new ScenarioException(e.toString(), this.getClass(), e, _scenario);
        }
    }

    /**
     * The implementation should close the maintained output stream.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void close() throws ScenarioException
    {
        try
        {
            if (_fileOutputStream != null) _fileOutputStream.close();
            if (_writer != null) _writer.close();
            _writer = null;
        } catch (IOException e)
        {
            throw new ScenarioException(e.toString(), this.getClass(), e, _scenario);
        }
    }

    /**
     * This extension adds the header lines to the chunk (indicator and statist functions' names).
     *
     * @param indicator   indicator
     * @param generations number of generations
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingBegins(IIndicator indicator, int generations) throws ScenarioException
    {
        super.notifyIndicatorProcessingBegins(indicator, generations);
        _writer.println(indicator.getName());
        for (int i = 0; i < _statistics.length; i++)
        {
            _writer.print(_statistics[i].getName());
            if (i < _statistics.length - 1) _writer.print(" ");
        }
        _writer.println();
        _writer.println(generations);
        _writer.flush();
    }


    /**
     * Writes the statistics values.
     *
     * @param trialResults raw trial results
     * @param statistics   statistics calculated from trial results (1:1 mapping with statistic objects stored in {@link container.scenario.AbstractScenarioDataContainer})
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void pushData(double[] trialResults, double[] statistics, int generation) throws ScenarioException
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < statistics.length; i++)
        {
            s.append(statistics[i]);
            if (i < statistics.length - 1) s.append(" ");
        }
        _writer.println(s.toString().replace(',', '.'));
        _writer.flush();
    }

}
