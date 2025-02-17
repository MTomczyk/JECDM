package tools;

/**
 * Data representing which cells should be merged (multirow:  \multirow{nrows}{width}{text} command).
 */
public final class MultiRow extends AbstractMulti
{
    /**
     * Parameterized constructor.
     *
     * @param row    starting row index
     * @param column starting column index
     * @param rows   no. rows to merge
     * @param width    width command, e.g., "*"
     */
    public MultiRow(int row, int column, int rows, String width)
    {
        super(row, column, rows, width);
    }

    /**
     * Returns a deep copy of the object
     *
     * @return deep copy
     */
    public MultiRow getClone()
    {
        return new MultiRow(_row, _column, _span, _arg);
    }
}
