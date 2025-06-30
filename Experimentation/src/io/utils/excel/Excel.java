package io.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Auxiliary container-like class wrapping data linked to an Excel file (workbook, sheets, etc.)
 *
 * @author MTomczyk
 */
public class Excel
{
    /**
     * The workbook object.
     */
    public Workbook _workbook;

    /**
     * The workbook object (viewed as HSSF).
     */
    public HSSFWorkbook _hssfWorkbook;

    /**
     * The workbook object (viewed as XSSF).
     */
    public XSSFWorkbook _xssfWorkbook;

    /**
     * The workbook object (viewed as SXSSF).
     */
    public SXSSFWorkbook _sxssfWorkbook;

    /**
     * Excel file sheets.
     */
    public Sheet[] _sheets;

    /**
     * Excel file sheets (viewed as HSSF).
     */
    public HSSFSheet[] _hssfSheets;

    /**
     * Excel file sheets (viewed as XSSF).
     */
    public XSSFSheet[] _xssfSheets;

    /**
     * Excel file sheets (viewed as SXSSF).
     */
    public SXSSFSheet[] _sxssfSheets;

    /**
     * Provides basic style options.
     */
    public Style _style;

    /**
     * Header style.
     */
    public CellStyle _headerStyle;

    /**
     * Content style (all rows excluding the header).
     */
    public CellStyle _contentStyle;

    /**
     * Header font.
     */
    public Font _headerFont;

    /**
     * Content font (all rows excluding the header).
     */
    public Font _contentFont;

    /**
     * If true, additional formatting is applied to cells.
     * The flag is set to true only for XLSX files.
     */
    public boolean _doFormatting = false;

    /**
     * If true, the data is occasionally flushed to the disk while being created; this way, the memory usage can be reduced.
     * Important note: in the line plots are not generated while in the streaming mode.
     */
    public boolean _useStreaming;

    /**
     * When in the streaming mode, this number determines after how many processed rows the data is flushed.
     */
    public int _flushEvery;

    /**
     * When in the streaming mode, this field determines whether the temporary data files created while flushing should
     * be compressed.
     */
    public boolean _useTempFilesCompression;

    /**
     * Default constructor.
     */
    public Excel()
    {
        this(new Style());
    }

    /**
     * Parameterized constructor.
     *
     * @param style provides basic customization options
     */
    public Excel(Style style)
    {
        _style = style;
        if (_style == null) _style = new Style();
    }

    /**
     * Instantiates the workbook (as HSSF).
     */
    public void instantiateWorkbookAsHSSF()
    {
        _hssfWorkbook = new HSSFWorkbook();
        _workbook = _hssfWorkbook;
    }

    /**
     * Instantiates the workbook (as XSSF).
     */
    public void instantiateWorkbookAsXSSF()
    {

        _xssfWorkbook = new XSSFWorkbook();
        _workbook = _xssfWorkbook;
    }


    /**
     * Instantiates the workbook (as SXSSF; uses the object's current parameterization).
     */
    public void instantiateWorkbookAsSXSSF()
    {

        _sxssfWorkbook = new SXSSFWorkbook(_flushEvery);
        _sxssfWorkbook.setCompressTempFiles(_useTempFilesCompression);
        _workbook = _sxssfWorkbook;
    }

    /**
     * Instantiates sheets (as HSSF).
     *
     * @param names sheets' names
     */
    public void instantiateSheetsAsHSSF(String[] names)
    {
        _hssfSheets = new HSSFSheet[names.length];
        _sheets = new Sheet[names.length];
        for (int i = 0; i < names.length; i++)
        {
            _hssfSheets[i] = _hssfWorkbook.createSheet(names[i]);
            _sheets[i] = _hssfSheets[i];
        }
    }

    /**
     * Instantiates sheets (as XSSF).
     *
     * @param names sheets' names
     */
    public void instantiateSheetsAsXSSF(String[] names)
    {
        _xssfSheets = new XSSFSheet[names.length];
        _sheets = new Sheet[names.length];
        for (int i = 0; i < names.length; i++)
        {
            _xssfSheets[i] = _xssfWorkbook.createSheet(names[i]);
            _sheets[i] = _xssfSheets[i];
        }
    }

    /**
     * Instantiates sheets (as SXSSF).
     *
     * @param names sheets' names
     */
    public void instantiateSheetsAsSXSSF(String[] names)
    {
        _sxssfSheets = new SXSSFSheet[names.length];
        _sheets = new Sheet[names.length];
        for (int i = 0; i < names.length; i++)
        {
            _sxssfSheets[i] = _sxssfWorkbook.createSheet(names[i]);
            _sheets[i] = _hssfSheets[i];
        }
    }

    /**
     * Auxiliary method for instantiating a header font based on the input style and workbook used.
     * Note that if the workbook or the style objects are not instantiated, the method terminates.
     */
    private void instantiateHeaderFont()
    {
        if (_workbook == null) return;
        if (_style == null) return;
        _headerFont = _workbook.createFont();
        if (_style._headerFontName != null) _headerFont.setFontName(_style._headerFontName);
        if (_style._headerFontHeightInPoints != null)
            _headerFont.setFontHeightInPoints(_style._headerFontHeightInPoints);
        _headerFont.setBold(_style._headerFontBold);
    }

    /**
     * Auxiliary method for creating a header style based on the input style, workbook used, and font style.
     * Note that if the workbook or the style objects are not instantiated, the method terminates.
     */
    public void instantiateHeaderStyle()
    {
        if (_workbook == null) return;
        if (_style == null) return;
        _headerStyle = _workbook.createCellStyle();
        if (_style._headerFillForegroundColor != null)
            _headerStyle.setFillForegroundColor(_style._headerFillForegroundColor);
        if (_style._headerFillPattern != null) _headerStyle.setFillPattern(_style._headerFillPattern);
        if (_style._headerVerticalAlignment != null) _headerStyle.setVerticalAlignment(_style._headerVerticalAlignment);
        if (_style._headerHorizontalAlignment != null) _headerStyle.setAlignment(_style._headerHorizontalAlignment);
        instantiateHeaderFont();
        _headerStyle.setFont(_headerFont);
    }

    /**
     * Auxiliary method for creating a content font based on the input style and workbook used.
     * Note that if the workbook or the style objects are not instantiated, the method terminates.
     */
    private void instantiateContentFont()
    {
        if (_workbook == null) return;
        if (_style == null) return;
        _contentFont = _workbook.createFont();
        if (_style._contentFontName != null) _contentFont.setFontName(_style._contentFontName);
        if (_style._contentFontHeightInPoints != null)
            _contentFont.setFontHeightInPoints(_style._contentFontHeightInPoints);
        _contentFont.setBold(_style._contentFontBold);
    }


    /**
     * Auxiliary method for creating a content style based on the input style, workbook used, and font style.
     * Note that if the workbook or the style objects are not instantiated, the method terminates.
     */
    public void instantiateContentStyle()
    {
        if (_workbook == null) return;
        if (_style == null) return;
        _contentStyle = _workbook.createCellStyle();
        instantiateContentFont();
        _contentStyle.setFont(_contentFont);
    }

    /**
     * Flushes the data to disk, closes the workbook, and nulls the references.
     *
     * @param fileOutputStream file output stream used to write data (will be closed by the method)
     * @throws IOException the IO exception can be thrown 
     */
    public void close(FileOutputStream fileOutputStream) throws IOException
    {
        _workbook.write(fileOutputStream);
        _workbook.close();
        fileOutputStream.close();

        _workbook = null;
        _hssfWorkbook = null;
        _xssfWorkbook = null;
        _sxssfWorkbook = null;
        _sheets = null;
        _hssfSheets = null;
        _xssfSheets = null;
        _sxssfSheets = null;
        _style = null;
        _headerStyle = null;
        _contentStyle = null;
        _headerFont = null;
        _contentFont = null;
    }
}
