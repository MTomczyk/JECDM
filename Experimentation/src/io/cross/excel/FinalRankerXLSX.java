package io.cross.excel;

import exception.CrossedScenariosException;
import executor.CrossSummarizer;
import io.cross.ICrossSaver;
import io.utils.excel.Style;
import scenario.CrossedScenarios;
import statistics.tests.ITest;

/**
 * Extension of {@link AbstractExcelGenerationRanker} for xls files (2007+ Excel). Reports results attained in
 * the last generations. Produces a file with the "_generation_final_ranker_key_" + getFileSuffixMiddlePart() +
 * _level + "D.xlsx" suffix.
 *
 * @author MTomczyk
 */
public class FinalRankerXLSX extends AbstractExcelGenerationRanker implements ICrossSaver
{
    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param level       the level of cross-analysis (should be at least 1)
     */
    public FinalRankerXLSX(String selectedKey, int level)
    {
        this(selectedKey, null, level, _defaultTolerance);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param level       the level of cross-analysis (should be at least 1)
     * @param tolerance   Delta (tolerance) used when comparing doubles)
     */
    public FinalRankerXLSX(String selectedKey, int level, double tolerance)
    {
        this(selectedKey, null, level, tolerance, new Style());
    }


    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param tests       statistical tests used when comparing the key-values (e.g., algorithms)
     * @param level       the level of cross-analysis (should be at least 1)
     */
    public FinalRankerXLSX(String selectedKey, ITest[] tests, int level)
    {
        this(selectedKey, tests, level, _defaultTolerance);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param tests       statistical tests used when comparing the key-values (e.g., algorithms)
     * @param level       the level of cross-analysis (should be at least 1)
     * @param tolerance   Delta (tolerance) used when comparing doubles)
     */
    public FinalRankerXLSX(String selectedKey, ITest[] tests, int level, double tolerance)
    {
        this(selectedKey, tests, level, tolerance, new Style());
    }


    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param level       the level of cross-analysis (should be at least 1)
     * @param style       provides some basic customization options
     */
    public FinalRankerXLSX(String selectedKey, int level, Style style)
    {
        this(selectedKey, level, _defaultTolerance, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param level       the level of cross-analysis (should be at least 1)
     * @param tolerance   Delta (tolerance) used when comparing doubles)
     * @param style       provides some basic customization options
     */
    public FinalRankerXLSX(String selectedKey, int level, double tolerance, Style style)
    {
        this(selectedKey, null, level, tolerance, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param tests       statistical tests used when comparing the key-values (e.g., algorithms)
     * @param level       the level of cross-analysis (should be at least 1)
     * @param style       provides some basic customization options
     */
    public FinalRankerXLSX(String selectedKey, ITest[] tests, int level, Style style)
    {
        this(selectedKey, tests, level, _defaultTolerance, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param selectedKey key selected for a direct comparison
     * @param tests       statistical tests used when comparing the key-values (e.g., algorithms)
     * @param level       the level of cross-analysis (should be at least 1)
     * @param tolerance   Delta (tolerance) used when comparing doubles)
     * @param style       provides some basic customization options
     */
    public FinalRankerXLSX(String selectedKey, ITest[] tests, int level, double tolerance, Style style)
    {
        this(null, null, null, selectedKey, tests, level, tolerance, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @param selectedKey      key selected for a direct comparison
     * @param tests            statistical tests used when comparing the key-values (e.g., algorithms)
     * @param level            the level of cross-analysis (should be at least 1)
     * @param tolerance        Delta (tolerance) used when comparing doubles)
     * @param style            provides some basic customization options
     */
    protected FinalRankerXLSX(String path, String filename, CrossedScenarios crossedScenarios, String selectedKey, ITest[] tests,
                              int level, double tolerance, Style style)
    {
        super(path, filename, crossedScenarios, selectedKey, tests, level, tolerance, style);
        _excel._doFormatting = true;
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
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be cast 
     */
    @Override
    public ICrossSaver getInstance(String path, String filename, CrossedScenarios crossedScenarios) throws CrossedScenariosException
    {
        validate(crossedScenarios, _tests);
        return new FinalRankerXLSX(path, filename, crossedScenarios, _selectedKey, _tests, _level, _tolerance, _excel._style);
    }

    /**
     * Returns the file suffix.
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return "_generation_final_ranker_key_" + getFileSuffixMiddlePart() + _level + "D.xlsx";
    }

    /**
     * Returns saver's default name (not file suffix).
     *
     * @return saver's default name
     */
    @Override
    public String getDefaultName()
    {
        return "GENERATION FINAL RANKER XLSX";
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
