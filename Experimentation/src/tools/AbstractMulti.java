package tools;

/**
 * Abstract class for {@link MultiColumn} and {@link MultiRow}.
 */
public sealed abstract class AbstractMulti permits MultiRow, MultiColumn
{
    /**
     * Starting row index (starting from 0, points to a row in the already derived from Excel data matrix).
     */
    public final int _row;

    /**
     * Starting column index (starting from 0, points to a column in the already derived from Excel data matrix).
     */
    public final int _column;

    /**
     * No. columns to merge.
     */
    public final int _span;

    /**
     * Auxiliary argument, e.g., "c" for the multicolumn.
     */
    public final String _arg;

    /**
     * Parameterized constructor.
     *
     * @param row     starting row index
     * @param column  starting column index
     * @param columns no. columns to merge.
     * @param pos     pos command, e.g., "c"
     */
    public AbstractMulti(int row, int column, int columns, String pos)
    {
        _row = row;
        _column = column;
        _span = columns;
        _arg = pos;
    }
}
