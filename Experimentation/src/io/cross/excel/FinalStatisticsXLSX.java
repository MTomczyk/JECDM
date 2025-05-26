package io.cross.excel;

import exception.CrossedScenariosException;
import executor.CrossSummarizer;
import io.cross.ICrossSaver;
import io.utils.excel.Style;
import scenario.CrossedScenarios;

/**
 * Extension of {@link AbstractFinalStatistics} for xls files (2007+ Excel).
 *
 * @author MTomczyk
 */
public class FinalStatisticsXLSX extends AbstractFinalStatistics implements ICrossSaver
{
    /**
     * Default constructor.
     *
     * @param level the level of cross-analysis (should be at least 2)
     */
    public FinalStatisticsXLSX(int level)
    {
        this(level, false, 0, false, new Style());
    }

    /**
     * Default constructor.
     *
     * @param level the level of cross-analysis (should be at least 2)
     * @param style provides some basic customization options
     */
    public FinalStatisticsXLSX(int level, Style style)
    {
        this(level, false, 0, false, style);
    }


    /**
     * Parameterized constructor.
     *
     * @param level                   the level of cross-analysis (should be at least 2)
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
    public FinalStatisticsXLSX(int level, boolean useStreaming, int flushEvery, boolean useTempFilesCompression, Style style)
    {
        this(null, null, null, level, useStreaming, flushEvery, useTempFilesCompression, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param path                    full path to the folder where the file should be stored (without a path separator)
     * @param filename                the filename (without the suffix, e.g., extension)
     * @param crossedScenarios        crossed scenarios being currently processed
     * @param level                   the level of cross-analysis (should be at least 2)
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
    protected FinalStatisticsXLSX(String path, String filename, CrossedScenarios crossedScenarios, int level, boolean useStreaming, int flushEvery, boolean useTempFilesCompression, Style style)
    {
        super(path, filename, crossedScenarios, level, style);
        _excel._doFormatting = true;
        _excel._useStreaming = useStreaming;
        _excel._flushEvery = flushEvery;
        _excel._useTempFilesCompression = useTempFilesCompression;
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
        if (_level < 2)
            throw new CrossedScenariosException("The 1-level cross comparison is no supported", null, this.getClass(), crossedScenarios);
        return new FinalStatisticsXLSX(path, filename, crossedScenarios, _level, _excel._useStreaming,
                _excel._flushEvery, _excel._useTempFilesCompression, _excel._style);
    }

    /**
     * Returns the file suffix (_finalStatistics + level + D.xlsx)
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return "_finalStatistics" + _level + "D.xlsx";
    }

    /**
     * Returns saver's default name (not file suffix).
     *
     * @return saver's default name
     */
    @Override
    public String getDefaultName()
    {
        return "FINAL STATISTICS XLS";
    }

    /**
     * Creates HSSF workbook.
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
        if (_uIndicators == null) return;
        if (_excel._useStreaming) _excel.instantiateSheetsAsSXSSF(getSheetsNamesFromUIndicators());
        else _excel.instantiateSheetsAsXSSF(getSheetsNamesFromUIndicators());
    }
}
