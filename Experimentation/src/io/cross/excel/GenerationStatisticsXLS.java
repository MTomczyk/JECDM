package io.cross.excel;

import exception.CrossedScenariosException;
import executor.CrossSummarizer;
import io.cross.ICrossSaver;
import io.utils.excel.Style;
import scenario.CrossedScenarios;

/**
 * Extension of {@link AbstractExcelGenerationStatistics} for xls files (1997-2003 Excel). Reports results attained in a
 * specific generation. Produces a file with the "_generation_" + _summarizeResultsInGeneration + "_statistics_" +
 * _level + "D.xls" suffix.
 *
 * @author MTomczyk
 */
public class GenerationStatisticsXLS extends AbstractExcelGenerationStatistics implements ICrossSaver
{
    /**
     * Parameterized constructor.
     *
     * @param generation the data summary will concern results attained in this specified generation number
     * @param level      the level of cross-analysis (should be at least 2)
     */
    public GenerationStatisticsXLS(int generation, int level)
    {
        this(generation, level, new Style());
    }

    /**
     * Parameterized constructor.
     *
     * @param generation the data summary will concern results attained in this specified generation number
     * @param level      the level of cross-analysis (should be at least 2)
     * @param style      provides some basic customization options
     */
    public GenerationStatisticsXLS(int level, int generation, Style style)
    {
        this(null, null, null, generation, level, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @param generation       generation number associated with the results to be presented
     * @param level            the level of cross-analysis (should be at least 2)
     * @param style            provides some basic customization options
     */
    protected GenerationStatisticsXLS(String path,
                                      String filename,
                                      CrossedScenarios crossedScenarios,
                                      int generation,
                                      int level,
                                      Style style)
    {
        super(path, filename, crossedScenarios, generation, level, style);
        _fullPath = getFullPath(path, filename); // overwrite previous (from super; generation was not set)
        _excel._doFormatting = false;
    }

    /**
     * Creates a new instance of the object. Intended to be used by {@link CrossSummarizer} to clone the initial object
     * instance one time per each crossed scenarios object involved (i.e., one clone will be mapped to one such
     * object).
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @return new object instance
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast 
     */
    @Override
    public ICrossSaver getInstance(String path, String filename, CrossedScenarios crossedScenarios) throws CrossedScenariosException
    {
        if (_level < 2)
            throw new CrossedScenariosException("The 1-level cross comparison is no supported", null, this.getClass(), crossedScenarios);
        return new GenerationStatisticsXLS(path, filename, crossedScenarios, _summarizeResultsInGeneration, _level, _excel._style);
    }

    /**
     * Returns the file suffix.
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return "_generation_" + _summarizeResultsInGeneration + "_statistics_" + _level + "D.xls";
    }

    /**
     * Returns saver's default name (not file suffix).
     *
     * @return saver's default name
     */
    @Override
    public String getDefaultName()
    {
        return "GENERATION " + _summarizeResultsInGeneration + " STATISTICS XLS";
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
