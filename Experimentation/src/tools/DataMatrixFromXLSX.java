package tools;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

/**
 * This auxiliary class allows for loading data matrices from an XLSX file (excel).
 *
 * @author MTomczyk
 */
public class DataMatrixFromXLSX
{
    /**
     * The method for loading a data matrix from an XLSX file (excel).
     * Note that the parsed Excel file is not validated (use must ensure that valid data exists).
     *
     * @param path absolute path to the file (includes prefix)
     * @param dmc  data matrix coordinates in the Excel file
     * @return data table (cells; null, if a file cannot be loaded)
     */
    public static Cell[][] getCellsData(String path, DataMatrixCoordinates dmc)
    {
        File file = new File(path);
        try (XSSFWorkbook workbook = new XSSFWorkbook(file))
        {
            return getCellsData(workbook, dmc);

        } catch (IOException | InvalidFormatException e)
        {
            System.out.println("Could not load the file: " + path + "(" + e.getMessage() + ")");
            return null;
        }
    }

    /**
     * The method for loading a data matrix from an XLSX file (excel).
     * Note that the parsed Excel file is not validated (use must ensure that valid data exists).
     *
     * @param workbook workbook file
     * @param dmc      data matrix coordinates in the Excel file
     * @return data table (null, if a file cannot be loaded)
     */
    public static Cell[][] getCellsData(XSSFWorkbook workbook, DataMatrixCoordinates dmc)
    {
        Cell[][] cells = new Cell[dmc._rows][dmc._columns];
        Row row;
        XSSFSheet sh = workbook.getSheetAt(dmc._sheet);
        for (int r = 0; r < dmc._rows; r++)
        {
            row = sh.getRow(dmc._topRow + r);
            if (row == null) continue;
            for (int c = 0; c < dmc._columns; c++)
            {
                Cell cell = row.getCell(dmc._leftColumn + c);
                if (cell == null) continue;
                cells[r][c] = cell;
            }
        }
        return cells;
    }


    /**
     * The method for loaded a data matrix from an XLSX file (excel).
     * Note that the parsed Excel file is not validated (use must ensure that valid data exists).
     *
     * @param path absolute path to the file (includes prefix)
     * @param dmc  data matrix coordinates in the Excel file
     * @return data table (null, if a file cannot be loaded)
     */
    public static Double[][] getDoubleData(String path, DataMatrixCoordinates dmc)
    {
        Cell[][] cells = getCellsData(path, dmc);
        if (cells == null) return null;
        return getDoubleData(cells);
    }

    /**
     * The method for loaded a data matrix from an XLSX file (excel).
     * Note that the parsed Excel file is not validated (use must ensure that valid data exists).
     *
     * @param cells cells derived from an Excel file
     * @return data table (null, if a file cannot be loaded; entry is null if the corresponding cell is not numeric)
     */
    public static Double[][] getDoubleData(Cell[][] cells)
    {
        Double[][] data = new Double[cells.length][cells[0].length];
        for (int r = 0; r < cells.length; r++)
        {
            if (cells[r] == null) continue;
            for (int c = 0; c < cells[0].length; c++)
            {
                if (cells[r][c] == null) continue;
                if (cells[r][c].getCellType() == CellType.NUMERIC) data[r][c] = cells[r][c].getNumericCellValue();
                else if (cells[r][c].getCellType() == CellType.FORMULA) data[r][c] = cells[r][c].getNumericCellValue();
                else data[r][c] = null;
            }
        }
        return data;
    }

    /**
     * The method for loaded a data matrix from an XLSX file (excel).
     * Note that the parsed Excel file is not validated (use must ensure that valid data exists).
     *
     * @param path absolute path to the file (includes prefix)
     * @param dmc  data matrix coordinates in the Excel file
     * @return data table (null, if a file cannot be loaded)
     */
    public static String[][] getStringData(String path, DataMatrixCoordinates dmc)
    {
        Cell[][] cells = getCellsData(path, dmc);
        if (cells == null) return null;
        return getStringData(cells);
    }

    /**
     * The method for loaded a data matrix from an XLSX file (excel).
     * Note that the parsed Excel file is not validated (use must ensure that valid data exists).
     *
     * @param cells cells derived from an Excel file
     * @return data table (null, if a file cannot be loaded)
     */
    public static String[][] getStringData(Cell[][] cells)
    {
        String[][] data = new String[cells.length][cells[0].length];

        for (int r = 0; r < cells.length; r++)
        {
            if (cells[r] == null) continue;
            for (int c = 0; c < cells[0].length; c++)
            {
                if (cells[r][c] == null) continue;
                if (cells[r][c].getCellType() == CellType.BLANK) data[r][c] = " ";
                else if (cells[r][c].getCellType() == CellType.NUMERIC)
                    data[r][c] = String.valueOf(cells[r][c].getNumericCellValue());
                else if (cells[r][c].getCellType() == CellType.FORMULA)
                {
                    if (cells[r][c].getCachedFormulaResultType().equals(CellType.STRING))
                        data[r][c] = cells[r][c].getStringCellValue();
                    else
                    {
                        double value = cells[r][c].getNumericCellValue();
                        data[r][c] = String.valueOf(value);
                    }
                }
                else if (cells[r][c].getCellType() == CellType.STRING) data[r][c] = cells[r][c].getStringCellValue();
            }
        }
        return data;
    }
}
