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
     * @param cumulative     if true: current data captures all data collected throughout subsequent calls; false =
     *                       represents only the most recently submitted data.
     * @param interlaceNulls if true: The list elements will be interlaced by nulls (i.e., one double[][], one null,
     *                       and so on); this flag may be helpful when the intention is to illustrate line segments
     *                       (nulls are considered breaks in lines)
     */
    public DataProcessor(boolean cumulative, boolean interlaceNulls)
    {
        this(cumulative, Integer.MAX_VALUE, interlaceNulls);
    }

    /**
     * Parameterized constructor.
     *
     * @param cumulative      if true: current data captures all data collected throughout subsequent calls; false =
     *                        represents only the most recently submitted data.
     * @param cumulativeLimit limit for the accumulated data arrays. In the case of exceeding the threshold, the oldest
     *                        entry is removed; in the case when the flag "interlace nulls" is true,
     *                        the last two oldest entries are removed (without checking if a single removal would be
     *                        enough to satisfy the threshold limit)
     */
    public DataProcessor(boolean cumulative, int cumulativeLimit)
    {
        this(cumulative, cumulativeLimit, false);
    }


    /**
     * Parameterized constructor.
     *
     * @param cumulative      if true: current data captures all data collected throughout subsequent calls; false =
     *                        represents only the most recently submitted data.
     * @param cumulativeLimit limit for the accumulated data arrays. In the case of exceeding the threshold, the oldest
     *                        entry is removed; in the case when the flag "interlace nulls" is true,
     *                        the last two oldest entries are removed (without checking if a single removal would be
     *                        enough to satisfy the threshold limit)
     * @param interlaceNulls  if true: The list elements will be interlaced by nulls (i.e., one double[][], one null,
     *                        and so on); this flag may be helpful when the intention is to illustrate line segments
     *                        (nulls are considered breaks in lines)
     */
    public DataProcessor(boolean cumulative, int cumulativeLimit, boolean interlaceNulls)
    {
        super(new Params(cumulative, cumulativeLimit, interlaceNulls));
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
