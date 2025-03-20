package updater;

import java.util.LinkedList;

/**
 * Abstract class implementation of {@link IDataProcessor} interface.
 *
 * @author MTomczyk
 */


abstract public class AbstractDataProcessor implements IDataProcessor
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * If true: current data captures all data collected throughout subsequent calls; false = represents only
         * the most recently submitted data.
         */
        public boolean _cumulative;

        /**
         * Limit for the accumulated data arrays. In the case of exceeding the threshold, the oldest entry is removed.
         * In the case when the flag "interlace nulls" is true, the last two oldest entries are
         * removed (without checking if a single removal would be enough to satisfy the threshold limit)
         */
        public int _cumulativeLimit;

        /**
         * If true: The list elements will be interlaced by nulls (i.e., one double[][], one null, and so on). This flag
         * may be helpful when the intention is to illustrate line segments (nulls are considered breaks in lines).
         */
        public boolean _interlaceNulls;

        /**
         * Default constructor.
         */
        public Params()
        {
            this(true);
        }

        /**
         * Parameterized constructor.
         *
         * @param cumulative if true: current data captures all data collected throughout subsequent calls; false = represents only the most recently submitted data
         */
        public Params(boolean cumulative)
        {
            this(cumulative, Integer.MAX_VALUE, false);
        }

        /**
         * Parameterized constructor.
         *
         * @param cumulative      if true: current data captures all data collected throughout subsequent calls; false = represents only the most recently submitted data
         * @param cumulativeLimit limit for the accumulated data arrays. In the case of exceeding the threshold, the oldest
         *                        entry is removed; in the case when the flag "interlace nulls" is true,
         *                        the last two oldest entries are removed (without checking if a single removal would be
         *                        enough to satisfy the threshold limit)
         * @param interlaceNulls  if true: The list elements will be interlaced by nulls (i.e., one double[][], one null, and so on); this flag may be helpful when the intention is to illustrate line segments (nulls are considered breaks in lines)
         */
        public Params(boolean cumulative, int cumulativeLimit, boolean interlaceNulls)
        {
            _cumulative = cumulative;
            _interlaceNulls = interlaceNulls;
            _cumulativeLimit = cumulativeLimit;
        }
    }

    /**
     * If true: current data captures all data collected throughout the optimization process; false = only data for the current time-stamp is used.
     */
    protected final boolean _cumulative;

    /**
     * Limit for the accumulated data arrays. In the case of exceeding the threshold, the oldest entry is removed. In the
     * case when the flag "interlace nulls" is true, the last two oldest entries are removed.
     */
    private final int _cumulativeLimit;

    /**
     * If true: The list elements will be interlaced by nulls (i.e., one double[][], one null, and so on). This flag
     * may be helpful when the intention is to illustrate line segments (nulls are considered breaks in lines).
     */
    protected final boolean _interlaceNulls;

    /**
     * Cumulated data (used only when the "cumulative flag" is set to true).
     */
    protected LinkedList<double[][]> _cumulatedData;

    /**
     * The most recently generated data to be visualized.
     */
    protected double[][] _currentData;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractDataProcessor(Params p)
    {
        _cumulative = p._cumulative;
        _interlaceNulls = p._interlaceNulls;
        if (_cumulative) _cumulatedData = new LinkedList<>();
        _cumulativeLimit = p._cumulativeLimit;
    }


    /**
     * Can be called to update the internal data maintained by the updater.
     *
     * @param sourceData new data to be processed
     */
    @Override
    public void update(double[][] sourceData)
    {
        double[][] processed = getProcessedData(sourceData);
        if (_cumulative)
        {
            _cumulatedData.add(processed);
            if (_interlaceNulls) _cumulatedData.add(null);
            if (_cumulatedData.size() > _cumulativeLimit)
            {
                _cumulatedData.removeFirst();
                if (_interlaceNulls) _cumulatedData.removeFirst();
            }

        }
        _currentData = processed;
    }

    /**
     * Default implementation = source data is immediately returned, i.e., it is considered as the already processed data.
     * Should be overwritten when intending to serve some more complex purposes.
     *
     * @param sourceData source data
     * @return processed data
     */
    protected double[][] getProcessedData(double[][] sourceData)
    {
        return sourceData;
    }

    /**
     * Getter for the data maintained (and processed) by the updater.
     * When called, the method should construct a new list based on the maintained data and return it (to avoid concurrent modification).
     *
     * @return data
     */
    @Override
    public LinkedList<double[][]> getData()
    {
        if (_cumulative) return new LinkedList<>(_cumulatedData);

        LinkedList<double[][]> wrapped = new LinkedList<>();
        wrapped.add(_currentData);
        if (_interlaceNulls) wrapped.add(null);
        return wrapped;
    }

    /**
     * Can be called to reset the internal data maintained by the updater.
     */
    @Override
    public void reset()
    {
        _currentData = null;
        _cumulatedData.clear();
    }

    /**
     * Can be called to dispose data.
     */
    @Override
    public void dispose()
    {
        _cumulatedData = null;
        _currentData = null;
    }

}
