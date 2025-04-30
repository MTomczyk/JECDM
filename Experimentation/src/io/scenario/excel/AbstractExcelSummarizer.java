package io.scenario.excel;

import exception.ScenarioException;
import indicator.IIndicator;
import io.scenario.IScenarioSaver;
import io.utils.excel.Style;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import scenario.Scenario;
import statistics.IStatistic;

/**
 * This abstract implementation of {@link IScenarioSaver} creates scenario-level Excel files.
 *
 * @author MTomczyk
 */
public abstract class AbstractExcelSummarizer extends AbstractExcelSaver implements IScenarioSaver
{

    /**
     * Parameterized constructor.
     *
     * @param path       full path to the folder where the file should be stored (without a path separator)
     * @param filename   the filename (without the suffix, e.g., extension)
     * @param scenario   currently processed scenario
     * @param trialIDs   trial IDs
     * @param indicators performance indicators employed when assessing the performance of EAs.
     * @param statistics statistic functions used to aggregate the data
     * @param style      provides some basic customization options
     */
    public AbstractExcelSummarizer(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators,
                                   IStatistic[] statistics, Style style)
    {
        super(path, filename, scenario, trialIDs, indicators, statistics, style);
    }


    /**
     * This extension creates a new sheet using the workbook object.
     *
     * @param indicator   indicator
     * @param generations number of generations
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingBegins(IIndicator indicator, int generations) throws ScenarioException
    {
        super.notifyIndicatorProcessingBegins(indicator, generations);
        createHeader();
    }


    /**
     * Auxiliary method for creating a header.
     */
    protected void createHeader()
    {
        Row header = _excel._sheets[_activeSheet].createRow(0);

        Cell generationCell = header.createCell(0);
        if ((_excel._doFormatting) && (_excel._headerStyle != null)) generationCell.setCellStyle(_excel._headerStyle);
        generationCell.setCellValue("Generation");

        for (int i = 0; i < _statistics.length; i++)
        {
            Cell statCell = header.createCell(1 + i);
            if ((_excel._doFormatting) && (_excel._headerStyle != null)) statCell.setCellStyle(_excel._headerStyle);
            statCell.setCellValue(_statistics[i].getName());
        }
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
        pushData(statistics, generation);
    }

    /**
     * Writes the data values (row).
     *
     * @param data       data values
     * @param generation current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    protected void pushData(double[] data, int generation) throws ScenarioException
    {
        Row row = _excel._sheets[_activeSheet].createRow(1 + generation);
        Cell cell = row.createCell(0);
        if (_excel._contentStyle != null) cell.setCellStyle(_excel._contentStyle);
        cell.setCellValue(generation);

        for (int s = 0; s < data.length; s++)
        {
            cell = row.createCell(1 + s);
            if (_excel._contentStyle != null) cell.setCellStyle(_excel._contentStyle);
            cell.setCellValue(data[s]);
        }
    }
}
