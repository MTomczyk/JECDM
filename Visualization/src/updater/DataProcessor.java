package updater;

/**
 * Default implementation of the data updater interface {@link IDataProcessor}.
 * It does not process the data. It just stores the source data.
 *
 * @author MTomczyk
 */


public class DataProcessor extends AbstractDataProcessor implements IDataProcessor
{
    /**
     * Default constructor.
     */
    public DataProcessor()
    {
        this(false);
    }

    /**
     * Parameterized constructor.
     *
     * @param cumulative if true: current data captures all data collected throughout subsequent calls; false = represents only the most recently submitted data.
     */
    public DataProcessor(boolean cumulative)
    {
        this(cumulative, false);
    }

    /**
     * Parameterized constructor.
     *
     * @param cumulative     if true: current data captures all data collected throughout subsequent calls; false = represents only the most recently submitted data.
     * @param interlaceNulls if true: The list elements will be interlaced by nulls (i.e., one double[][], one null, and so on); this flag may be helpful when the intention is to illustrate line segments (nulls are considered breaks in lines)
     */
    public DataProcessor(boolean cumulative, boolean interlaceNulls)
    {
        super(new Params(cumulative, interlaceNulls));
    }


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public DataProcessor(Params p)
    {
        super(p);
    }
}
