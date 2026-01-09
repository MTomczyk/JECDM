package tools;

import exception.Exception;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * Provides various utility methods related to reading/writing Excel files.
 *
 * @author MTomczyk
 */
public class ExcelUtils
{
    /**
     * Auxiliary method that returns an integer value from a cell that is NUMERIC (checks if it is an integer; if not an
     * exception is cast) or STRING (attempts to cast to int; throws an exception if the attempt fails).
     *
     * @param cell input cell
     * @return string cell value
     * @throws Exception an exception is thrown when the input is null or the cell format is not supported
     * @deprecated work in progress; do not use
     */
    public static int getIntegerFromCell(Cell cell) throws Exception
    {
        if (cell == null) throw Exception.getInstanceWithSource("The input cell is null", ExcelUtils.class);
        else if (cell.getCellType() == CellType.BLANK)
            throw Exception.getInstanceWithSource("The input cell is blank", ExcelUtils.class);
        else if (cell.getCellType() == CellType.NUMERIC)
        {
            double v = cell.getNumericCellValue();
            if ((Double.compare(v, Math.floor(v)) == 0) && (!Double.isInfinite(v))) return (int) v;
            throw Exception.getInstanceWithSource("The input cell number is not integer", ExcelUtils.class);
        }
        else if (cell.getCellType() == CellType.STRING)
        {
            try
            {
                return Integer.parseInt(cell.getStringCellValue());
            } catch (NumberFormatException e)
            {
                throw Exception.getInstanceWithSource("The input cell string cannot be cast to integer", ExcelUtils.class);
            }
        }
        throw Exception.getInstanceWithSource("The input cell format is not supported (" + cell.getCellType() +
                ")", ExcelUtils.class);
    }

    /**
     * Auxiliary method that returns a string value from a cell that is BLANK (returns an empty string);
     * NUMERIC (returns double converted to string) or STRING (explicitly returns the string value).
     *
     * @param cell                        input cell
     * @param roundIntegerDoubleToInteger if true, a cell representing an integer will be correctly converted into
     *                                    an integer (i.e., without the fractional part, e.g., from 40.0 to 40)
     * @return string cell value
     * @throws Exception an exception is thrown when the input is null or the cell format is not supported
     * @deprecated work in progress; do not use
     */
    public static String getStringFromCell(Cell cell, boolean roundIntegerDoubleToInteger) throws Exception
    {
        if (cell == null) throw Exception.getInstanceWithSource("The input cell is null", ExcelUtils.class);
        else if (cell.getCellType() == CellType.BLANK) return "";
        else if (cell.getCellType() == CellType.NUMERIC)
        {
            double v = cell.getNumericCellValue();
            if ((Double.compare(v, Math.floor(v)) == 0) && (!Double.isInfinite(v)))
                return String.valueOf((int) v);
            return String.valueOf(v);
        }
        else if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        throw Exception.getInstanceWithSource("The input cell format is not supported (" + cell.getCellType() +
                ")", ExcelUtils.class);
    }
}
