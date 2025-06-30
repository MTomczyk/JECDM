package io.scenario.excel;

import exception.ScenarioException;
import executor.ScenariosSummarizer;
import indicator.IIndicator;
import io.cross.excel.AbstractExcelGenerationStatistics;
import io.scenario.IScenarioSaver;
import io.utils.excel.Style;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import scenario.Scenario;
import statistics.IStatistic;

/**
 * Extension of {@link AbstractExcelGenerationStatistics} for xls files (2007+ Excel).
 * This auxiliary summarizer fills data matrix by explicitly copying results from the trial level without performing
 * the summary (the resulting matrices are of generations x trials size; per indicator).
 *
 * @author MTomczyk
 */
public class TrialsResultsXLSX extends AbstractExcelSummarizer implements IScenarioSaver
{

    /**
     * Default constructor.
     */
    public TrialsResultsXLSX()
    {
        this(new Style());
    }

    /**
     * Default constructor.
     *
     * @param style provides some basic customization options
     */
    public TrialsResultsXLSX(Style style)
    {
        this("", "", null, null, null, null, false, 0, false, style);
    }


    /**
     * Parameterized constructor.
     *
     * @param useStreaming            if true, the data is occasionally flushed to the disk while being created; this
     *                                way, the
     *                                memory usage can be reduced; important note: in the line plots are not generated
     *                                while in the streaming mode
     * @param flushEvery              when in the streaming mode, this number determines after how many processed rows
     *                                the data is flushed
     * @param useTempFilesCompression When in the streaming mode, this field determines whether the temporary data files
     *                                created while flushing should be compressed
     * @param style                   provides some basic customization options
     */
    public TrialsResultsXLSX(boolean useStreaming, int flushEvery, boolean useTempFilesCompression, Style style)
    {
        this("", "", null, null, null, null, useStreaming, flushEvery, useTempFilesCompression, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param path                    full path to the folder where the file should be stored (without a path separator)
     * @param filename                the filename (without the suffix, e.g., extension)
     * @param scenario                currently processed scenario
     * @param trialIDs                trial IDs
     * @param indicators              performance indicators employed when assessing the performance of EAs.
     * @param statistics              statistic functions used to aggregate the data
     * @param useStreaming            if true, the data is occasionally flushed to the disk while being created; this way, the
     *                                memory usage can be reduced; important note: in the line plots are not generated while in the
     *                                streaming mode
     * @param flushEvery              when in the streaming mode, this number determines after how many processed rows the data is
     *                                flushed
     * @param useTempFilesCompression when in the streaming mode, this field determines whether the temporary data files
     *                                created while flushing should be compressed
     * @param style                   provides some basic customization options
     */
    protected TrialsResultsXLSX(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators,
                                IStatistic[] statistics, boolean useStreaming, int flushEvery, boolean useTempFilesCompression, Style style)
    {
        super(path, filename, scenario, trialIDs, indicators, statistics, style);
        _excel._doFormatting = true;
        _excel._useStreaming = useStreaming;
        _excel._flushEvery = flushEvery;
        _excel._useTempFilesCompression = useTempFilesCompression;
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
        return new TrialsResultsXLSX(path, filename, scenario, trialIDs, indicators, statistics,
                _excel._useStreaming, _excel._flushEvery, _excel._useTempFilesCompression, _excel._style);
    }

    /**
     * Creates XSSF (SXSSF) workbook.
     */
    @Override
    protected void instantiateWorkbook()
    {
        if (_excel._useStreaming) _excel.instantiateWorkbookAsSXSSF();
        else _excel.instantiateWorkbookAsXSSF();
    }

    /**
     * Auxiliary method for instantiating sheets.
     */
    @Override
    protected void instantiateSheets()
    {
        String[] names = getSheetsNamesFromIndicators();
        if (_excel._useStreaming) _excel.instantiateSheetsAsSXSSF(names);
        else _excel.instantiateSheetsAsXSSF(names);
    }

    /**
     * Auxiliary method for creating a header.
     */
    @Override
    protected void createHeader()
    {
        Row header = _excel._sheets[_activeSheet].createRow(0);

        Cell generationCell = header.createCell(0);
        if ((_excel._doFormatting) && (_excel._headerStyle != null)) generationCell.setCellStyle(_excel._headerStyle);
        generationCell.setCellValue("Generation");

        for (int i = 0; i < _trialIDs.length; i++)
        {
            Cell statCell = header.createCell(1 + i);
            if ((_excel._doFormatting) && (_excel._headerStyle != null)) statCell.setCellStyle(_excel._headerStyle);
            statCell.setCellValue(_trialIDs[i]);
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
        pushData(trialResults, generation);
    }

    /**
     * Auxiliary method for creating convergence plots.
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void createConvergencePlots()
    {
        // do nothing
    }

    /**
     * The implementation flushes the data to the actual file and closes the outputs.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void close() throws ScenarioException
    {
        super.close();
    }

    /**
     * Returns the file suffix (includes .xlsx)
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return "_trial_results.xlsx";
    }


}
