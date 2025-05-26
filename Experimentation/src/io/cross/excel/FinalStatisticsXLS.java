package io.cross.excel;

import exception.CrossedScenariosException;
import executor.CrossSummarizer;
import io.cross.ICrossSaver;
import io.utils.excel.Style;
import scenario.CrossedScenarios;

/**
 * Extension of {@link AbstractFinalStatistics} for xls files (1997-2003 Excel).
 *
 * @author MTomczyk
 */
public class FinalStatisticsXLS extends AbstractFinalStatistics implements ICrossSaver
{
    /**
     * Parameterized constructor.
     *
     * @param level the level of cross-analysis (should be at least 2)
     */
    public FinalStatisticsXLS(int level)
    {
        this(level, new Style());
    }

    /**
     * Parameterized constructor.
     *
     * @param level the level of cross-analysis (should be at least 2)
     * @param style provides some basic customization options
     */
    public FinalStatisticsXLS(int level, Style style)
    {
        this(null, null, null, level, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @param level            the level of cross-analysis (should be at least 2)
     * @param style            provides some basic customization options
     */
    protected FinalStatisticsXLS(String path, String filename, CrossedScenarios crossedScenarios, int level, Style style)
    {
        super(path, filename, crossedScenarios, level, style);
        _excel._doFormatting = false;
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
        return new FinalStatisticsXLS(path, filename, crossedScenarios, _level, _excel._style);
    }

    /**
     * Returns the file suffix (_finalStatistics + level + D.xls)
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return "_finalStatistics" + _level + "D.xls";
    }

    /**
     * Returns saver's default name (not file suffix).
     *
     * @return saver's default name
     */
    @Override
    public String getDefaultName()
    {
        return "FINAL STATISTICS XLSX";
    }

    /**
     * Creates HSSF workbook.
     */
    @Override
    protected void instantiateWorkbook()
    {
        _excel.instantiateWorkbookAsHSSF();
    }

    /**
     * Auxiliary method for instantiating sheets.
     */
    @Override
    protected void instantiateSheets()
    {
        if (_uIndicators == null) return;
        _excel.instantiateSheetsAsHSSF(getSheetsNamesFromUIndicators());
    }


}
