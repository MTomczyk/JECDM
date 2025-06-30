package io.cross.excel;

import exception.CrossedScenariosException;
import executor.CrossSummarizer;
import io.cross.ICrossSaver;
import io.utils.excel.Style;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import scenario.CrossedScenarios;
import scenario.Scenario;

/**
 * Extension of {@link AbstractExcelConvergence} for xls files (2007+ Excel). Produces a file with the
 * "_convergence_" + _level + "D.xlsx" suffix.
 *
 * @author MTomczyk
 */
public class ConvergenceXLSX extends AbstractExcelConvergence implements ICrossSaver
{
    /**
     * Parameterized constructor.
     *
     * @param level the level of cross-analysis (should be at least 1)
     */
    public ConvergenceXLSX(int level)
    {
        this(level, new Style());
    }

    /**
     * Parameterized constructor.
     *
     * @param level the level of cross-analysis (should be at least 1)
     * @param style provides some basic customization options
     */
    public ConvergenceXLSX(int level, Style style)
    {
        this(level, false, 0, false, style);
    }


    /**
     * Parameterized constructor.
     *
     * @param level                   the level of cross-analysis (should be at least 1)
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
    public ConvergenceXLSX(int level, boolean useStreaming, int flushEvery, boolean useTempFilesCompression, Style style)
    {
        this(null, null, null, level, useStreaming, flushEvery, useTempFilesCompression, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param path                    full path to the folder where the file should be stored (without a path separator)
     * @param filename                the filename (without the suffix, e.g., extension)
     * @param crossedScenarios        crossed scenarios being currently processed
     * @param level                   the level of cross-analysis (should be at least 1
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
    protected ConvergenceXLSX(String path, String filename, CrossedScenarios crossedScenarios, int level,
                              boolean useStreaming, int flushEvery, boolean useTempFilesCompression, Style style)
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
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be cast 
     */
    @Override
    public ICrossSaver getInstance(String path, String filename, CrossedScenarios crossedScenarios) throws CrossedScenariosException
    {
        return new ConvergenceXLSX(path, filename, crossedScenarios, _level, _excel._useStreaming, _excel._flushEvery,
                _excel._useTempFilesCompression, _excel._style);
    }

    /**
     * Returns the file suffix.
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return "_convergence_" + _level + "D.xlsx";
    }

    /**
     * Returns saver's default name (not file suffix).
     *
     * @return saver's default name
     */
    @Override
    public String getDefaultName()
    {
        return "CONVERGENCE XLSX";
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

    /**
     * Method for notifying the savers that the processing begins (after all scenarios are processed).
     */
    @Override
    public void notifyProcessingEnds() throws CrossedScenariosException
    {
        super.notifyProcessingEnds();
        createConvergencePlots();
    }


    /**
     * Auxiliary method for creating convergence plots.
     */
    @SuppressWarnings("DuplicatedCode")
    private void createConvergencePlots()
    {
        if (_excel._useStreaming) return;

        int cx = _excel._style._tableMarginX + getExpectedTableWidth() + 2;
        int cw = _excel._style._plotWidth;
        int ch = _excel._style._plotHeight;
        int firstRow = _excel._style._tableMarginY + getExpectedTableHeight();

        // to be overwritten
        for (int i = 0; i < _excel._sheets.length; i++)
        {
            XSSFSheet sheet = _excel._xssfSheets[i];
            int chartIndex = 0;
            for (int j = 0; j < _crossedScenarios.getReferenceScenariosSorted().length; j++)
            {
                Scenario rS = _crossedScenarios.getReferenceScenarios()[j];
                int scenarioIndex = _crossedScenarios.getReferenceScenariosSortedMap().get(rS.toString());
                if (_generationsPerScenario[scenarioIndex] == null) continue;
                if (_generationsPerScenario[scenarioIndex] == 0) continue;
                if (!_indicatorUsedPerScenario[scenarioIndex][i]) continue;
                int lastRow = firstRow + _generationsPerScenario[scenarioIndex] - 1;

                // generations
                CellRangeAddress generationsRange = new CellRangeAddress(firstRow, lastRow, _excel._style._tableMarginX, _excel._style._tableMarginX);
                XDDFNumericalDataSource<Double> xSource = XDDFDataSourcesFactory.fromNumericCellRange(sheet, generationsRange);

                int cy = _excel._style._tableMarginY + chartIndex * (ch + 1);
                int baseColumn = getBaseColumn(rS);
                chartIndex++;

                XSSFDrawing drawing = sheet.createDrawingPatriarch();
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, cx, cy, cx + cw, cy + ch);
                XSSFChart chart = drawing.createChart(anchor);

                StringBuilder label = new StringBuilder();
                for (int kv = 0; kv < rS.getKeyValues().length; kv++)
                {
                    label.append(rS.getKeyValues()[kv].getKey().toString());
                    label.append("=");
                    label.append(rS.getKeyValues()[kv].getValue().toString());
                    if (kv < rS.getKeyValues().length - 1) label.append(" ");
                }

                chart.setTitleText("Results for " + label);
                chart.setTitleOverlay(false);
                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                bottomAxis.setTitle("Generation");
                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                leftAxis.setTitle("Statistics");

                int seriesIndex = -1;
                for (int s = 0; s < _uStatistics._entities.size(); s++)
                {
                    if (!_statisticUsedPerScenarioIndicator[scenarioIndex][i][s]) continue;
                    seriesIndex++;

                    int colIndex = _uStatistics._entitiesMap.get(_uStatistics._entities.get(s).toString());
                    int col = _excel._style._tableMarginX + baseColumn + colIndex;

                    CellRangeAddress range = new CellRangeAddress(firstRow, lastRow, col, col);
                    XDDFNumericalDataSource<Double> ySource = XDDFDataSourcesFactory.fromNumericCellRange(sheet, range);
                    XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
                    XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(xSource, ySource);
                    chart.plot(data);
                    _excel._style.applyLineStyle(chart, series, _uStatistics._entities.get(s).toString(), s, seriesIndex);
                }

                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.TOP_RIGHT);
                legend.setOverlay(false);
            }
        }
    }

}
