package io.utils.excel;

import color.Color;
import color.ColorPalettes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;


/**
 * Provides some basic customization options.
 */
public class Style
{
    /**
     * Fill foreground color (for header; null = not used).
     */
    public Short _headerFillForegroundColor = IndexedColors.GREY_25_PERCENT.getIndex();

    /**
     * Fill pattern type (for header; null = not used).
     */
    public FillPatternType _headerFillPattern = FillPatternType.SOLID_FOREGROUND;

    /**
     * Font name (for header; null = not used).
     */
    public String _headerFontName = "Arial";

    /**
     * Font height in points (for header; null = not used)
     */
    public Short _headerFontHeightInPoints = 12;

    /**
     * Header vertical alignment.
     */
    public VerticalAlignment _headerVerticalAlignment = VerticalAlignment.CENTER;

    /**
     * Header horizontal alignment.
     */
    public HorizontalAlignment _headerHorizontalAlignment = HorizontalAlignment.CENTER;

    /**
     * Use bold font (for header).
     */
    public boolean _headerFontBold = true;

    /**
     * Font name (for all the content rows (excludes the header); null = not used).
     */
    public String _contentFontName = "Arial";

    /**
     * Font height in points (for all the content rows (excludes the header); null = not used)
     */
    public Short _contentFontHeightInPoints = 10;

    /**
     * Use bold font (for all the content rows (excludes the header)).
     */
    public boolean _contentFontBold = false;


    /**
     * Can be used to alter the column's width (null = not used).
     */
    public Integer _columnWidth = 18;

    /**
     * Auxiliary field that can be used to determine the margin (in cells) for tables (columns-axis).
     */
    public int _tableMarginX = 0;

    /**
     * Auxiliary field that can be used to determine the margin (in cells) for tables (rows-axis).
     */
    public int _tableMarginY = 0;

    /**
     * Color used for table major fill color.
     */
    public short _tableFillForegroundColor = IndexedColors.WHITE.getIndex();

    /**
     * Color used for table major fill color (extra color used when separating different blocks)).
     */
    public short _tableFillChessboardColor = IndexedColors.GREY_25_PERCENT.getIndex();

    /**
     * Color used for table borders.
     */
    public short _tableBorderColor = IndexedColors.BLACK.getIndex();

    /**
     * Border style used for tables
     */
    public BorderStyle _tableBorderStyle = BorderStyle.THIN;


    /**
     * Plot width (in cells, should be greater or equal to 1).
     */
    public int _plotWidth = 10;

    /**
     * Plot width (in cells, should be greater or equal to 1).
     */
    public int _plotHeight = 15;

    /**
     * Line width (for line plots; null = not used).
     */
    public Double _lineWidth = 2.0d;

    /**
     * Line colors (for line plots; null = not used).
     */
    public Color[] _lineColors = ColorPalettes._colors;

    /**
     * If true, the lines illustrated in charts are smoothened.
     */
    public boolean _createSmoothLines = false;

    /**
     * Applies style to a line chart data series.
     *
     * @param chart  chart
     * @param series series
     * @param title  series' title
     * @param colorIndex  color index
     * @param seriesIndex  series index
     */
    public void applyLineStyle(XSSFChart chart,
                               XDDFLineChartData.Series series,
                               String title,
                               int colorIndex,
                               int seriesIndex)
    {
        series.setSmooth(_createSmoothLines);
        series.setMarkerStyle(MarkerStyle.NONE);
        if (title != null) series.setTitle(title);

        CTSolidColorFillProperties fillProp = CTSolidColorFillProperties.Factory.newInstance();
        if (_lineColors != null)
        {
            int color = colorIndex % _lineColors.length;
            CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
            rgb.setVal(new byte[]{
                    (byte) _lineColors[color].getRed(),
                    (byte) _lineColors[color].getGreen(),
                    (byte) _lineColors[color].getBlue()});
            fillProp.setSrgbClr(rgb);
        }

        CTLineProperties lineProp = CTLineProperties.Factory.newInstance();
        lineProp.setSolidFill(fillProp);
        CTShapeProperties ctShapeProperties = CTShapeProperties.Factory.newInstance();
        ctShapeProperties.setLn(lineProp);

        chart.getCTChart().getPlotArea().getLineChartList().get(seriesIndex).getSerList().get(0).setSpPr(ctShapeProperties);
    }


}
