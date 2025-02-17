package tools;

/**
 * Auxiliary class providing bounds for data matrix contained in an Excel file.
 *
 * @author MTomczyk
 */
public class DataMatrixCoordinates
{
    /**
     * Index of the left -- starting -- column of the data table (starts from 0).
     */
    public int _leftColumn;

    /**
     * Index of the top -- starting -- row of the data table (starts from 0).
     */
    public int _topRow;

    /**
     * The number of columns to read.
     */
    public int _columns;

    /**
     * The number of rows to read.
     */
    public int _rows;

    /**
     * Sheet index (starts from 0).
     */
    public int _sheet;

    /**
     * Default constructor.
     */
    public DataMatrixCoordinates()
    {
        this(0, 0, 0, 0, 0);
    }

    /**
     * Parameterized constructor.
     *
     * @param columns the number of columns to read
     * @param rows    the number of rows to read
     * @param sheet   sheet index (starts from 0)
     */
    public DataMatrixCoordinates(int columns, int rows, int sheet)
    {
        this(0, 0, columns, rows, sheet);
    }

    /**
     * Parameterized constructor.
     *
     * @param leftColumn index of the left -- starting -- column of the data table (starts from 0)
     * @param topRow     index of the top -- starting -- row of the data table (starts from 0)
     * @param columns    the number of columns to read
     * @param rows       the number of rows to read
     * @param sheet      sheet index (starts from 0)
     */
    public DataMatrixCoordinates(int leftColumn, int topRow, int columns, int rows, int sheet)
    {
        _leftColumn = leftColumn;
        _topRow = topRow;
        _columns = columns;
        _rows = rows;
        _sheet = sheet;
    }
}
