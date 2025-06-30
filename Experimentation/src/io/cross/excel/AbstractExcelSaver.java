package io.cross.excel;

import exception.CrossedScenariosException;
import io.cross.AbstractCrossSaver;
import io.cross.ICrossSaver;
import io.utils.excel.Excel;
import io.utils.excel.Style;
import io.utils.excel.Table;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import scenario.CrossedScenarios;
import scenario.KeyValues;
import scenario.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * This abstract implementation of {@link io.cross.ICrossSaver} creates crossed-scenario-level summary Excel files.
 *
 * @author MTomczyk
 */
public abstract class AbstractExcelSaver extends AbstractCrossSaver implements ICrossSaver
{
    /**
     * Container for excel-related objects.
     */
    protected Excel _excel = null;

    /**
     * Tables to be provided in Excel files.
     */
    protected Table[] _tables;

    /**
     * Index to the sheet being active.
     */
    protected int _activeSheet = 0;

    /**
     * Index to the base row being active.
     */
    protected int _activeBaseRow = 0;

    /**
     * Index to the base column being active.
     */
    protected int _activeBaseColumn = 0;


    /**
     * Auxiliary field storing data on the number of rows occupied by one compared key-value.
     */
    protected int[] _takenRows;

    /**
     * Auxiliary field storing data on the number of components occupied by one compared key-value.
     */
    protected int[] _takenColumns;

    /**
     * Auxiliary map that allows associating specified columns with some keys (strings).
     */
    protected HashMap<String, Integer> _auxBaseColumnsMap;


    /**
     * Parameterized constructor.
     *
     * @param path                         full path to the folder where the file should be stored (without a path separator)
     * @param filename                     the filename (without the suffix, e.g., extension)
     * @param crossedScenarios             crossed scenarios being currently processed
     * @param style                        provides some basic customization options
     * @param summarizeResultsInGeneration if not null, the data summary will concern results attained in this specified
     *                                     generation number (instead of the last generation)
     * @param level                        the level of cross-analysis (should be at least 2)
     */
    public AbstractExcelSaver(String path,
                              String filename,
                              CrossedScenarios crossedScenarios,
                              Style style,
                              Integer summarizeResultsInGeneration,
                              int level)
    {
        super(path, filename, crossedScenarios, summarizeResultsInGeneration, level);
        instantiateExcel(style);
    }

    /**
     * Auxiliary method for instantiating the Excel object.
     *
     * @param style provides basic customization options
     */
    protected void instantiateExcel(Style style)
    {
        _excel = new Excel(style);
    }


    /**
     * The implementation creates the workbook object
     *
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be thrown (e.g., then the requested path is invalid)
     */
    @Override
    public void create() throws CrossedScenariosException
    {
        File file = getFileAtCrossedScenariosLevel();

        try
        {
            _fileOutputStream = new FileOutputStream(file, false);
        } catch (FileNotFoundException e)
        {
            throw new CrossedScenariosException(e.toString(), this.getClass(), e, _crossedScenarios);
        }

        instantiateWorkbook();
        instantiateStyles();
    }


    /**
     * Auxiliary method for instantiating the workbook. To be overwritten.
     */
    protected void instantiateWorkbook()
    {

    }

    /**
     * Auxiliary method for instantiating sheets. To be overwritten.
     */
    protected void instantiateSheets()
    {

    }


    /**
     * Instantiates style-related objects.
     */
    protected void instantiateStyles()
    {
        _excel.instantiateHeaderStyle();
        _excel.instantiateContentStyle();
    }


    /**
     * The implementation flushes the data to the actual file and closes the outputs.
     *
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be thrown 
     */
    @Override
    public void close() throws CrossedScenariosException
    {
        if (_excel._workbook != null)
        {
            try
            {
                _excel.close(_fileOutputStream);
            } catch (IOException e)
            {
                throw new CrossedScenariosException("Unable to close the workbook (reason = " + e.getMessage() + ")", this.getClass(), e, _crossedScenarios);
            }
        }
    }

    /**
     * Auxiliary method for constructing sheets' names from unified indicator's names.
     *
     * @return sheets' names
     */
    protected String[] getSheetsNamesFromUIndicators()
    {
        String[] names = new String[_uIndicators._entities.size()];
        for (int i = 0; i < _uIndicators._entities.size(); i++) names[i] = _uIndicators._entities.get(i).getName();
        return names;
    }

    /**
     * Auxiliary method for creating table and instantiating its main area.
     *
     * @param sheet parent sheet
     * @param x     x-coordinate (left-top corner, in cells)
     * @param y     x-coordinate (left-top corner, in cells)
     * @param w     width ( in cells)
     * @param h     height (in cells)
     * @return created table
     */
    protected Table createTable(Sheet sheet, int x, int y, int w, int h)
    {
        Table table = new Table(x, y, w, h, sheet);
        if (_excel._doFormatting)
        {
            CellStyle newStyle = _excel._workbook.createCellStyle();
            newStyle.cloneStyleFrom(_excel._contentStyle);
            table.applyCellStyle(newStyle);
            table.makeBlank(0, 0, w, h);
            table.applyBackground(0, 0, w, h, _excel._style._tableFillForegroundColor);
            table.applyBorder(0, 0, w, h, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
        }
        return table;
    }

    /**
     * Auxiliary method for creating table's main, top-left block.
     *
     * @param table table
     * @param label block label
     * @param w     width (in cells)
     * @param h     height (in cells)
     */
    protected void createLeftTopMainBlock(Table table, String label, int w, int h)
    {
        table._cell[0][0].setCellValue(label);
        if (_excel._doFormatting)
        {
            table.applyCellStyleToCells(0, 0, w, h, _excel._headerStyle);
            table.applyBorder(0, 0, w, h, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
            table.mergeCells(0, 0, w, h);
        }
    }

    /**
     * Auxiliary method for creating table's label + values rows at specified coordinates x and y (in cells). The label
     * is provided in the first row with a width determined by the number of values, while the values are provided in
     * the next row.
     *
     * @param table  table
     * @param x      x-coordinate (left-top; in cells)
     * @param y      y-coordinate (left-top; in cells)
     * @param label  label tobe displayed in the first row
     * @param values values to be displayed in the second row
     */
    protected void createLabelValuesRows(Table table, int x, int y, String label, Value[] values)
    {
        table._cell[y][x].setCellValue(label);
        if (_excel._doFormatting)
        {
            table.applyCellStyleToCells(x, y, values.length, 1, _excel._headerStyle);
            table.applyBorder(x, y, values.length, 1, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
            table.mergeCells(x, y, values.length, 1);
        }

        for (int k = 0; k < values.length; k++)
        {
            table._cell[y + 1][x + k].setCellValue(values[k].getValue());
            if (_excel._doFormatting)
            {
                table.applyBorder(x + k, y + 1, 1, 1, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                table.applyAlignment(x + k, y + 1, 1, 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            }
        }
    }

    /**
     * Auxiliary method for creating a left-sided block displaying all realizations of key-values in the table.
     *
     * @param table table
     * @param kvs   key-values
     * @param x     x-coordinate (left-top; in cells)
     * @param y     y-coordinate (left-top; in cells)
     */
    protected void createLeftCombinationsBlock(Table table, KeyValues[] kvs, int x, int y)
    {
        int repeats = 1;
        int levels = kvs.length;

        for (int i = 0; i < levels; i++)
        {
            int shiftX = x + 2 * i;
            int keyHeight = 1;
            for (int k = i; k < levels; k++) keyHeight *= kvs[k].getValues().length;
            int valueHeight = 1;
            for (int k = i + 1; k < levels; k++) valueHeight *= kvs[k].getValues().length;

            for (int r = 0; r < repeats; r++)
            {
                int baseY = y + r * keyHeight;
                table._cell[baseY][shiftX].setCellValue(kvs[i].getKey().toString());

                if (_excel._doFormatting)
                {
                    table.applyCellStyleToCells(shiftX, baseY, 1, keyHeight, _excel._headerStyle);
                    table.applyBorder(shiftX, baseY, 1, keyHeight, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                    table.mergeCells(shiftX, baseY, 1, keyHeight);
                }

                for (int v = 0; v < kvs[i].getValues().length; v++)
                {
                    table._cell[baseY + v * valueHeight][shiftX + 1].setCellValue(kvs[i].getValues()[v].getValue());

                    if (_excel._doFormatting)
                    {
                        table.applyAlignment(shiftX + 1, baseY + v * valueHeight, 1, valueHeight, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
                        table.applyBorder(shiftX + 1, baseY + v * valueHeight, 1, valueHeight, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                        if (valueHeight > 1) table.mergeCells(shiftX + 1, baseY + v * valueHeight, 1, valueHeight);
                    }
                }
            }

            repeats *= kvs[i].getValues().length;
        }

    }

    /**
     * Instantiates taken rows based on the provided keys (read from right to left).
     *
     * @param keys selected keys
     */
    protected void instantiateTakenRows(String[] keys)
    {
        _takenRows = new int[keys.length];
        int accu = 1;
        for (int j = keys.length - 1; j >= 0; j--)
        {
            _takenRows[j] = accu;
            int index = _crossedScenarios.getComparedKeysMap().get(keys[j]);
            accu *= _crossedScenarios.getComparedKeyValues()[index].getValues().length;
        }
    }


    /**
     * Auxiliary method for setting column widths (for columns in specified range) for all sheets
     *
     * @param leftColumn  left bound
     * @param rightColumn right bound
     */
    protected void setColumnWidths(int leftColumn, int rightColumn)
    {
        if (_excel._style._columnWidth == null) return;
        for (Sheet sheet : _excel._sheets)
            for (int j = leftColumn; j < rightColumn; j++) sheet.setColumnWidth(j, _excel._style._columnWidth * 256);
    }
}
