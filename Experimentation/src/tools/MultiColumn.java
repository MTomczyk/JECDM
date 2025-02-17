package tools;

/**
 * Data representing which cells should be merged (multicolumn: \multicolumn{ncols}{pos}{text} command).
 */
public final class MultiColumn extends AbstractMulti
{
    /**
     * Parameterized constructor.
     *
     * @param row     starting row index
     * @param column  starting column index
     * @param columns no. columns to merge.
     * @param pos     pos command, e.g., "c"
     */
    public MultiColumn(int row, int column, int columns, String pos)
    {
        super(row, column, columns, pos);
    }

    /**
     * Returns a deep copy of the object
     *
     * @return deep copy
     */
    public MultiColumn getClone()
    {
        return new MultiColumn(_row, _column, _span, _arg);
    }
}
