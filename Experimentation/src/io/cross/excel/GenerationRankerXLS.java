package io.cross.excel;

import exception.CrossedScenariosException;
import executor.CrossSummarizer;
import io.cross.ICrossSaver;
import io.utils.excel.Style;
import scenario.CrossedScenarios;
import statistics.tests.ITest;

/**
 * Extension of {@link AbstractExcelGenerationRanker} for xls files (1997-2003 Excel). Reports results attained in a
 * specific generation. Produces a file with the "_generation_" + _summarizeResultsInGeneration + "_ranker_key_" +
 * getFileSuffixMiddlePart() + _level + "D.xls" suffix.
 *
 * @author MTomczyk
 */
public class GenerationRankerXLS extends AbstractExcelGenerationRanker implements ICrossSaver
{
    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param generation  the data summary will concern results attained in this specified generation number
     * @param level       the level of cross-analysis (should be at least 1)
     */
    public GenerationRankerXLS(String selectedKey, int generation, int level)
    {
        this(selectedKey, null, generation, level, _defaultTolerance);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param generation  the data summary will concern results attained in this specified generation number
     * @param level       the level of cross-analysis (should be at least 1)
     * @param tolerance   Delta (tolerance) used when comparing doubles)
     */
    public GenerationRankerXLS(String selectedKey, int generation, int level, double tolerance)
    {
        this(selectedKey, null, generation, level, tolerance, new Style());
    }


    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param tests       statistical tests used when comparing the key-values (e.g., algorithms)
     * @param generation  the data summary will concern results attained in this specified generation number
     * @param level       the level of cross-analysis (should be at least 1)
     */
    public GenerationRankerXLS(String selectedKey, ITest[] tests, int generation, int level)
    {
        this(selectedKey, tests, generation, level, _defaultTolerance);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param tests       statistical tests used when comparing the key-values (e.g., algorithms)
     * @param generation  the data summary will concern results attained in this specified generation number
     * @param level       the level of cross-analysis (should be at least 1)
     * @param tolerance   Delta (tolerance) used when comparing doubles)
     */
    public GenerationRankerXLS(String selectedKey, ITest[] tests, int generation, int level, double tolerance)
    {
        this(selectedKey, tests, generation, level, tolerance, new Style());
    }


    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param generation  the data summary will concern results attained in this specified generation number
     * @param level       the level of cross-analysis (should be at least 1)
     * @param style       provides some basic customization options
     */
    public GenerationRankerXLS(String selectedKey, int generation, int level, Style style)
    {
        this(selectedKey, generation, level, _defaultTolerance, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param generation  the data summary will concern results attained in this specified generation number
     * @param level       the level of cross-analysis (should be at least 1)
     * @param tolerance   Delta (tolerance) used when comparing doubles)
     * @param style       provides some basic customization options
     */
    public GenerationRankerXLS(String selectedKey, int generation, int level, double tolerance, Style style)
    {
        this(selectedKey, null, generation, level, tolerance, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param tests       statistical tests used when comparing the key-values (e.g., algorithms)
     * @param generation  the data summary will concern results attained in this specified generation number
     * @param level       the level of cross-analysis (should be at least 1)
     * @param style       provides some basic customization options
     */
    public GenerationRankerXLS(String selectedKey, ITest[] tests, int generation, int level, Style style)
    {
        this(selectedKey, tests, generation, level, _defaultTolerance, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param tests       statistical tests used when comparing the key-values (e.g., algorithms)
     * @param generation  the data summary will concern results attained in this specified generation number
     * @param level       the level of cross-analysis (should be at least 1)
     * @param tolerance   Delta (tolerance) used when comparing doubles)
     * @param style       provides some basic customization options
     */
    public GenerationRankerXLS(String selectedKey, ITest[] tests, int generation, int level, double tolerance, Style style)
    {
        this(null, null, null, selectedKey, tests, generation, level, tolerance, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @param selectedKey      key selected for a direct comparison
     * @param tests            statistical tests used when comparing the key-values (e.g., algorithms)
     * @param generation       generation number associated with the results to be presented
     * @param level            the level of cross-analysis (should be at least 1)
     * @param tolerance        Delta (tolerance) used when comparing doubles)
     * @param style            provides some basic customization options
     */
    protected GenerationRankerXLS(String path,
                                  String filename,
                                  CrossedScenarios crossedScenarios,
                                  String selectedKey,
                                  ITest[] tests,
                                  int generation,
                                  int level,
                                  double tolerance,
                                  Style style)
    {
        super(path, filename, crossedScenarios, selectedKey, tests,
                generation, level, tolerance, style);
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
        validate(crossedScenarios, _tests);
        return new GenerationRankerXLS(path, filename, crossedScenarios, _selectedKey, _tests,
                _summarizeResultsInGeneration, _level, _tolerance, _excel._style);
    }

    /**
     * Returns the file suffix.
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return "_generation_" + _summarizeResultsInGeneration + "_ranker_key_" + getFileSuffixMiddlePart() + _level + "D.xls";
    }

    /**
     * Returns saver's default name (not file suffix).
     *
     * @return saver's default name
     */
    @Override
    public String getDefaultName()
    {
        return "GENERATION " + _summarizeResultsInGeneration + " RANKER XLS";
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
