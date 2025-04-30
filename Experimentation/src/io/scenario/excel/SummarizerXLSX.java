package io.scenario.excel;

import color.Color;
import exception.ScenarioException;
import executor.ScenariosSummarizer;
import indicator.IIndicator;
import io.cross.excel.AbstractFinalStatistics;
import io.scenario.IScenarioSaver;
import io.utils.excel.Style;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import scenario.Scenario;
import statistics.IStatistic;

/**
 * Extension of {@link AbstractFinalStatistics} for xls files (2007+ Excel).
 *
 * @author MTomczyk
 */
public class SummarizerXLSX extends AbstractExcelSummarizer implements IScenarioSaver
{

    /**
     * Default constructor.
     */
    public SummarizerXLSX()
    {
        this(new Style());
    }

    /**
     * Default constructor.
     *
     * @param style provides some basic customization options
     */
    public SummarizerXLSX(Style style)
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
    public SummarizerXLSX(boolean useStreaming, int flushEvery, boolean useTempFilesCompression, Style style)
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
    protected SummarizerXLSX(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators,
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
     * @throws ScenarioException scenario-level exception can be cast and propagated higher
     */
    @Override
    public IScenarioSaver getInstance(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators, IStatistic[] statistics) throws ScenarioException
    {
        return new SummarizerXLSX(path, filename, scenario, trialIDs, indicators, statistics,
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
     * Auxiliary method for creating convergence plots.
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void createConvergencePlots()
    {
        if (_excel._useStreaming) return; // exit

        int w = 1;
        int h = 1;
        if (_excel._style._plotWidth > 1) w = _excel._style._plotWidth;
        if (_excel._style._plotHeight > 1) h = _excel._style._plotHeight;


        int leftX = 2 + _statistics.length;
        int rightX = leftX + w;

        CellRangeAddress generationsRange = new CellRangeAddress(1, _expectedNumberOfGenerations, 0, 0);
        XDDFNumericalDataSource<Double> generationsSource =
                XDDFDataSourcesFactory.fromNumericCellRange(_excel._xssfSheets[_activeSheet], generationsRange);

        XDDFSolidFillProperties fill = new XDDFSolidFillProperties();
        if (_excel._style._lineColors != null)
        {
            Color cp = _excel._style._lineColors.getColor(0);
            XDDFColor color = XDDFColor.from(cp.getRed(), cp.getGreen(), cp.getBlue());
            fill.setColor(color);
        }

        XDDFLineProperties lineProperties = new XDDFLineProperties();
        if (_excel._style._lineWidth != null)
            lineProperties.setWidth(_excel._style._lineWidth);
        lineProperties.setFillProperties(fill);

        for (int s = 0; s < _statistics.length; s++)
        {
            int topY = 2 + s * (h + 1);
            int bottomY = topY + h;

            XSSFDrawing drawing = _excel._xssfSheets[_activeSheet].createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, leftX, topY, rightX, bottomY);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Results for " + _statistics[s].getName() + " of " + _currentIndicator.getName());
            chart.setTitleOverlay(false);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("Generation");
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle(_statistics[s].getName() + " of " + _currentIndicator.getName());

            CellRangeAddress range = new CellRangeAddress(1, _expectedNumberOfGenerations, 1 + s, 1 + s);
            XDDFNumericalDataSource<Double> statisticsSource =
                    XDDFDataSourcesFactory.fromNumericCellRange(_excel._xssfSheets[_activeSheet], range);

            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
            XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(generationsSource, statisticsSource);
            chart.plot(data);
            _excel._style.applyLineStyle(chart, series, null, 0, 0);
        }
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
        return ".xlsx";
    }


}
