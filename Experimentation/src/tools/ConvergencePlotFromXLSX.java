package tools;

import color.Color;
import color.palette.AbstractColorPalette;
import color.palette.DefaultPalette;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.IPlotParamsAdjuster;
import plot.ISchemeAdjuster;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;
import utils.Projection;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Auxiliary factory-like class providing efficient means for generating almost ready-for-presentation convergence
 * plots.
 *
 * @author MTomczyk
 */
public class ConvergencePlotFromXLSX
{
    /**
     * Params container (used to parameterize
     * {@link ConvergencePlotFromXLSX#getParamsContainerForConvergencePlotFromXLSX(Params)}).
     */
    public static class Params
    {
        /**
         * Title for the X-axis.
         */
        public String _xAxisTitle;

        /**
         * Title for the Y-axis.
         */
        public String _yAxisTitle;

        /**
         * Fixed range for the X-axis.
         */
        public Range _xAxisRange;

        /**
         * Fixed range for the Y-axis.
         */
        public Range _yAxisRange;

        /**
         * Rescaling factor for top margin.
         */
        public float _topMarginRescale = 1.0f;

        /**
         * Rescaling factor for right margin.
         */
        public float _rightMarginRescale = 1.0f;

        /**
         * Rescaling factor for bottom margin.
         */
        public float _bottomMarginRescale = 1.0f;

        /**
         * Rescaling factor for left margin.
         */
        public float _leftMarginRescale = 1.0f;

        /**
         * Rescaling factor for the X-axis title offset.
         */
        public float _xAxisTitleOffsetRescale = 1.0f;

        /**
         * Rescaling factor for the Y-axis title offset.
         */
        public float _yAxisTitleOffsetRescale = 1.0f;

        /**
         * Rescaling factor for axes titles font sizes.
         */
        public float _axesTitlesFontSizeRescale = 1.0f;

        /**
         * Rescaling factor for axes ticks font sizes.
         */
        public float _axesTicksFontSizeRescale = 1.0f;

        /**
         * Rescaling factor for axes font sizes.
         */
        public float _legendFontSizeRescale = 1.0f;

        /**
         * Rescaling factor for grid lines (widths).
         */
        public float _gridLinesRescale = 1.0f;

        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor.
         *
         * @param xAxisTitle                title for the X-axis
         * @param yAxisTitle                title for the Y-axis
         * @param xAxisRange                fixed range for the X-axis
         * @param yAxisRange                fixed range for the Y-axis
         * @param topMarginRescale          rescaling factor for top margin
         * @param rightMarginRescale        rescaling factor for right margin
         * @param bottomMarginRescale       rescaling factor for bottom margin
         * @param leftMarginRescale         rescaling factor for left margin
         * @param xAxisTitleOffsetRescale   rescaling factor for the X-axis title offset
         * @param yAxisTitleOffsetRescale   rescaling factor for the Y-axis title offset
         * @param axesTitlesFontSizeRescale rescaling factor for axes titles font sizes
         * @param axesTicksFontSizeRescale  rescaling factor for axes ticks font sizes
         * @param legendFontSizeRescale     rescaling factor for axes font sizes
         * @param gridLinesRescale          rescaling factor for grid lines (widths)
         */
        public Params(String xAxisTitle,
                      String yAxisTitle,
                      Range xAxisRange,
                      Range yAxisRange,
                      float topMarginRescale,
                      float rightMarginRescale,
                      float bottomMarginRescale,
                      float leftMarginRescale,
                      float xAxisTitleOffsetRescale,
                      float yAxisTitleOffsetRescale,
                      float axesTitlesFontSizeRescale,
                      float axesTicksFontSizeRescale,
                      float legendFontSizeRescale,
                      float gridLinesRescale)
        {
            _xAxisTitle = xAxisTitle;
            _yAxisTitle = yAxisTitle;
            _xAxisRange = xAxisRange;
            _yAxisRange = yAxisRange;
            _topMarginRescale = topMarginRescale;
            _rightMarginRescale = rightMarginRescale;
            _bottomMarginRescale = bottomMarginRescale;
            _leftMarginRescale = leftMarginRescale;
            _xAxisTitleOffsetRescale = xAxisTitleOffsetRescale;
            _yAxisTitleOffsetRescale = yAxisTitleOffsetRescale;
            _axesTitlesFontSizeRescale = axesTitlesFontSizeRescale;
            _axesTicksFontSizeRescale = axesTicksFontSizeRescale;
            _legendFontSizeRescale = legendFontSizeRescale;
            _gridLinesRescale = gridLinesRescale;
        }


        /**
         * Constructs a deep copy of this object.
         *
         * @return deep copy
         */
        public Params getClone()
        {
            Params p = new Params();
            p._xAxisTitle = _xAxisTitle;
            p._yAxisTitle = _yAxisTitle;
            p._xAxisRange = _xAxisRange;
            p._yAxisRange = _yAxisRange;
            p._topMarginRescale = _topMarginRescale;
            p._rightMarginRescale = _rightMarginRescale;
            p._bottomMarginRescale = _bottomMarginRescale;
            p._leftMarginRescale = _leftMarginRescale;
            p._xAxisTitleOffsetRescale = _xAxisTitleOffsetRescale;
            p._yAxisTitleOffsetRescale = _yAxisTitleOffsetRescale;
            p._axesTitlesFontSizeRescale = _axesTitlesFontSizeRescale;
            p._axesTicksFontSizeRescale = _axesTicksFontSizeRescale;
            p._legendFontSizeRescale = _legendFontSizeRescale;
            p._gridLinesRescale = _gridLinesRescale;
            return p;
        }
    }

    /**
     * Returns an instantiated params container for Plot 2D.
     * Note that further layout adjustments can be achieved by modifying the scheme object.
     *
     * @param p params container
     * @return params container for plot 2D.
     */
    public static Plot2D.Params getParamsContainerForConvergencePlotFromXLSX(Params p)
    {
        return getParamsContainerForConvergencePlotFromXLSX(p, null, null);
    }

    /**
     * Returns an instantiated params container for Plot 2D.
     * Note that further layout adjustments can be achieved by modifying the scheme object.
     *
     * @param p                  params container
     * @param schemeAdjuster     optional scheme adjuster (executed just after the default scheme parameterization is
     *                           done; can be null if not used)
     * @param plotParamsAdjuster optional plot params adjuster (executed just before the return call; can be null if not
     *                           used)
     * @return params container for plot 2D.
     */
    public static Plot2D.Params getParamsContainerForConvergencePlotFromXLSX(Params p, ISchemeAdjuster schemeAdjuster, IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster)
    {
        Plot2D.Params pP = new Plot2D.Params();
        pP._scheme = new WhiteScheme();
        pP._drawLegend = true;
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._xAxisTitle = p._xAxisTitle;
        pP._yAxisTitle = p._yAxisTitle;
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D(p._xAxisRange, p._yAxisRange);
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);

        pP._scheme.rescale(p._topMarginRescale, SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER);
        pP._scheme.rescale(p._rightMarginRescale, SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER);
        pP._scheme.rescale(p._bottomMarginRescale, SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER);
        pP._scheme.rescale(p._leftMarginRescale, SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER);

        pP._scheme.rescale(p._xAxisTitleOffsetRescale, SizeFields.AXIS_X_TITLE_OFFSET_RELATIVE_MULTIPLIER);
        pP._scheme.rescale(p._yAxisTitleOffsetRescale, SizeFields.AXIS_Y_TITLE_OFFSET_RELATIVE_MULTIPLIER);

        pP._scheme.rescale(p._gridLinesRescale, SizeFields.GRID_AUX_LINES_WIDTH_RELATIVE_MULTIPLIER);
        pP._scheme.rescale(p._gridLinesRescale, SizeFields.GRID_MAIN_LINES_WIDTH_RELATIVE_MULTIPLIER);

        pP._scheme.rescale(p._legendFontSizeRescale, SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER);

        pP._scheme.rescale(p._axesTitlesFontSizeRescale, SizeFields.AXIS_X_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
        pP._scheme.rescale(p._axesTitlesFontSizeRescale, SizeFields.AXIS_Y_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
        pP._scheme.rescale(p._axesTicksFontSizeRescale, SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
        pP._scheme.rescale(p._axesTicksFontSizeRescale, SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);

        if (schemeAdjuster != null) schemeAdjuster.adjust(pP._scheme);
        if (plotParamsAdjuster != null) plotParamsAdjuster.adjust(pP);
        return pP;
    }

    /**
     * Creates a suitably adjusted frame that visualizes a convergence plot.
     * The method also:
     * - sets axes number formats to decimals.
     * - adds a right click popup menu with "save as image" option
     *
     * @param pP               plot 2D params container obtained via
     *                         {@link ConvergencePlotFromXLSX#getParamsContainerForConvergencePlotFromXLSX(Params)}
     * @param dataSets         data sets obtained via
     *                         {@link ConvergencePlotFromXLSX#parseDataSetsFromXLSX(String, DataMatrixCoordinates,
     *                         DataSetData[], Float)}
     * @param width            frame width
     * @param widthHeightRatio width/height ratio (greater than 0)
     * @return frame
     */
    public static Frame getFrame(Plot2D.Params pP, ArrayList<IDataSet> dataSets, int width, float widthHeightRatio)
    {
        return getFrame(pP, dataSets, width, widthHeightRatio, "0.00", "0.00");
    }

    /**
     * Creates a suitably adjusted frame that visualizes a convergence plot.
     * The method also:
     * - sets axes number formats to decimals.
     * - adds a right click popup menu with "save as image" option
     *
     * @param pP                 plot 2D params container obtained via
     *                           {@link ConvergencePlotFromXLSX#getParamsContainerForConvergencePlotFromXLSX(Params)}
     * @param dataSets           data sets obtained via
     *                           {@link ConvergencePlotFromXLSX#parseDataSetsFromXLSX(String, DataMatrixCoordinates,
     *                           DataSetData[], Float)}
     * @param width              frame width
     * @param widthHeightRatio   width/height ratio (greater than 0)
     * @param xAxisDecimalFormat decimal format for X-axis ticks (e.g. "0.00")
     * @param yAxisDecimalFormat decimal format for Y-axis ticks (e.g. "0.00")
     * @return frame
     */
    public static Frame getFrame(Plot2D.Params pP,
                                 ArrayList<IDataSet> dataSets,
                                 int width,
                                 float widthHeightRatio,
                                 String xAxisDecimalFormat,
                                 String yAxisDecimalFormat)
    {
        return getFrame(pP, dataSets, width, (int) (width / widthHeightRatio), xAxisDecimalFormat, yAxisDecimalFormat);
    }

    /**
     * Creates a suitably adjusted frame that visualizes a convergence plot.
     * The method also:
     * - sets axes number formats to decimals.
     * - adds a right click popup menu with "save as image" option
     *
     * @param pP                 plot 2D params container obtained via
     *                           {@link ConvergencePlotFromXLSX#getParamsContainerForConvergencePlotFromXLSX(Params)}
     * @param dataSets           data sets obtained via
     *                           {@link ConvergencePlotFromXLSX#parseDataSetsFromXLSX(String, DataMatrixCoordinates,
     *                           DataSetData[], Float)}
     * @param width              frame width
     * @param height             frame height
     * @param xAxisDecimalFormat decimal format for X-axis ticks (e.g. "0.00"; if null, the default DecimalFormat() will
     *                           be instantiated as the formatter)
     * @param yAxisDecimalFormat decimal format for Y-axis ticks (e.g. "0.00"; if null, the default DecimalFormat() will
     *                           be instantiated as the formatter)
     * @return frame
     */
    public static Frame getFrame(Plot2D.Params pP,
                                 ArrayList<IDataSet> dataSets,
                                 int width,
                                 int height,
                                 String xAxisDecimalFormat,
                                 String yAxisDecimalFormat)
    {
        return getFrame(pP, dataSets, width, height,
                xAxisDecimalFormat == null ? new DecimalFormat() : new DecimalFormat(xAxisDecimalFormat),
                yAxisDecimalFormat == null ? new DecimalFormat() : new DecimalFormat(yAxisDecimalFormat)
        );
    }

    /**
     * Creates a suitably adjusted frame that visualizes a convergence plot.
     * The method also:
     * - sets axes number formats to decimals.
     * - adds a right click popup menu with "save as image" option
     *
     * @param pP                plot 2D params container obtained via
     *                          {@link ConvergencePlotFromXLSX#getParamsContainerForConvergencePlotFromXLSX(Params)}
     * @param dataSets          data sets obtained via
     *                          {@link ConvergencePlotFromXLSX#parseDataSetsFromXLSX(String, DataMatrixCoordinates,
     *                          DataSetData[], Float)}
     * @param width             frame width
     * @param height            frame height
     * @param xAxisNumberFormat number format for the x-axis (if null, DecimalFormat() is used)
     * @param yAxisNumberFormat number format for the y-axis (if null, DecimalFormat() is used)
     * @return frame
     */
    public static Frame getFrame(Plot2D.Params pP,
                                 ArrayList<IDataSet> dataSets,
                                 int width,
                                 int height,
                                 NumberFormat xAxisNumberFormat,
                                 NumberFormat yAxisNumberFormat)
    {
        Plot2D plot2D = new Plot2D(pP);
        Frame frame = new Frame(plot2D, width, Projection.getP(height));

        plot2D.getModel().setDataSets(dataSets);
        plot2D.getComponentsContainer().getAxes()[0].getTicksDataGetter().setNumberFormat(xAxisNumberFormat);
        plot2D.getComponentsContainer().getAxes()[1].getTicksDataGetter().setNumberFormat(yAxisNumberFormat);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot2D.getController().addRightClickPopupMenu(menu);
        return frame;
    }

    /**
     * The method for loaded a data matrix from an XLSX file (excel) and parses them into data sets suited
     * for the convergence plot. Note that the parsed Excel file is not validated (use must ensure that valid data
     * exists).
     *
     * @param path        absolute path to the file (includes prefix)
     * @param dmc         data matrix coordinates in the Excel file
     * @param dataSetData coordinates-related data for establishing data sets used by the plot (not used if null)
     * @return data table (null, if a file cannot be loaded)
     */
    public static ArrayList<IDataSet> parseDataSetsFromXLSX(String path,
                                                            DataMatrixCoordinates dmc,
                                                            DataSetData[] dataSetData)
    {
        return parseDataSetsFromXLSX(path, dmc, dataSetData, null);
    }


    /**
     * The method for loaded a data matrix from an XLSX file (excel) and parses them into data sets suited
     * for the convergence plot. Note that the parsed Excel file is not validated (use must ensure that valid data
     * exists).
     *
     * @param path                 absolute path to the file (includes prefix)
     * @param dmc                  data matrix coordinates in the Excel file
     * @param dataSetData          coordinates-related data for establishing data sets used by the plot (not used if
     *                             null)
     * @param envelopeTransparency envelope color transparency level (spanned over primary upper and lower bounds, null
     *                             if not used)
     * @return data table (null, if a file cannot be loaded)
     */
    public static ArrayList<IDataSet> parseDataSetsFromXLSX(String path,
                                                            DataMatrixCoordinates dmc,
                                                            DataSetData[] dataSetData,
                                                            Float envelopeTransparency)
    {
        return parseDataSetsFromXLSX(path, dmc, dataSetData, envelopeTransparency, 1.0f);
    }

    /**
     * The method for loaded a data matrix from an XLSX file (excel) and parses them into data sets suited
     * for the convergence plot. Note that the parsed Excel file is not validated (use must ensure that valid data
     * exists).
     *
     * @param path                 absolute path to the file (includes prefix)
     * @param dmc                  data matrix coordinates in the Excel file
     * @param dataSetData          coordinates-related data for establishing data sets used by the plot (not used if
     *                             null)
     * @param envelopeTransparency envelope color transparency level (spanned over primary upper and lower bounds, null
     *                             if not used)
     * @param lw                   data set line width
     * @return data table (null, if a file cannot be loaded)
     */
    public static ArrayList<IDataSet> parseDataSetsFromXLSX(String path,
                                                            DataMatrixCoordinates dmc,
                                                            DataSetData[] dataSetData,
                                                            Float envelopeTransparency,
                                                            float lw)
    {
        return parseDataSetsFromXLSX(path, dmc, dataSetData, envelopeTransparency, lw, new DefaultPalette());
    }

    /**
     * The method for loaded a data matrix from an XLSX file (excel) and parses them into data sets suited
     * for the convergence plot. Note that the parsed Excel file is not validated (use must ensure that valid data
     * exists).
     *
     * @param path                 absolute path to the file (includes prefix)
     * @param dmc                  data matrix coordinates in the Excel file
     * @param dataSetData          coordinates-related data for establishing data sets used by the plot (not used if
     *                             null)
     * @param envelopeTransparency envelope color transparency level (spanned over primary upper and lower bounds, null
     *                             if not used)
     * @param lw                   data set line width
     * @param colorPalette         palette providing colors for data sets to be depicted
     * @return data table (null, if a file cannot be loaded)
     */
    public static ArrayList<IDataSet> parseDataSetsFromXLSX(String path,
                                                            DataMatrixCoordinates dmc,
                                                            DataSetData[] dataSetData,
                                                            Float envelopeTransparency,
                                                            float lw,
                                                            AbstractColorPalette colorPalette)
    {
        ArrayList<IDataSet> dataSets = new ArrayList<>(dataSetData.length);
        Double[][] data = DataMatrixFromXLSX.getDoubleData(path, dmc);

        if (data == null) return null;

        for (int i = 0; i < dataSetData.length; i++)
        {
            if (dataSetData[i] == null) continue;
            boolean primaryDeviationsUsed = dataSetData[i].arePrimaryDeviationsUsed();
            float upMul = dataSetData[i]._primaryUpperMultiplier;
            float lowMul = dataSetData[i]._primaryLowerMultiplier;

            int size = 2;
            if (primaryDeviationsUsed) size = 4;

            double[][] d = new double[dmc._rows][size];
            int x = dataSetData[i]._xIndex;
            int y = dataSetData[i]._yIndex;
            int yPlusDev = 0;
            int yMinusDev = 0;

            if (primaryDeviationsUsed)
            {
                yPlusDev = dataSetData[i]._yPrimaryUpperDeviationIndex;
                yMinusDev = dataSetData[i]._yPrimaryLowerDeviationIndex;
            }

            for (int j = 0; j < dmc._rows; j++)
            {
                d[j][0] = data[j][x];
                d[j][1] = data[j][y];
                if (primaryDeviationsUsed)
                {
                    d[j][2] = data[j][y] + data[j][yPlusDev] * upMul;
                    d[j][3] = data[j][y] - data[j][yMinusDev] * lowMul;
                }
            }

            Color color = colorPalette.getColor(i);
            color.Color envelopeColor = null;
            if (envelopeTransparency != null)
                envelopeColor = color.getTransparentInstance(envelopeTransparency);
            IDataSet ds = DSFactory2D.getDSForConvergencePlot(dataSetData[i]._name, d,
                    new LineStyle(lw, new color.gradient.Color(color), 0.01f), envelopeColor);
            dataSets.add(ds);
        }

        return dataSets;
    }

}
