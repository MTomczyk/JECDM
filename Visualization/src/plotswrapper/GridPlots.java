package plotswrapper;

import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;

import java.awt.*;

/**
 * Plots wrapper that organizes the plots using the grid layout.
 *
 * @author MTomczyk
 */
public class GridPlots extends AbstractPlotsWrapper
{
    /**
     * Params container.
     */
    public static class Params extends AbstractPlotsWrapper.Params
    {
        /**
         * No. rows.
         */
        public int _rows;

        /**
         * No. columns.
         */
        public int _columns;

        /**
         * Parameterized constructor.
         *
         * @param wrappers plots to be displayed; they are read rows by rows and their number should equal rows x columns, but nulls are accepted (ignored)
         * @param rows     number of rows
         * @param columns  number of columns
         */
        public Params(AbstractPlotWrapper[] wrappers, int rows, int columns)
        {
            this(wrappers, rows, columns, 1);
        }

        /**
         * Parameterized constructor.
         *
         * @param plots   plots to be displayed (default wrapper is used); they are read rows by rows and their number should equal rows x columns, but nulls are accepted (ignored)
         * @param rows    number of rows
         * @param columns number of columns
         */
        public Params(AbstractPlot[] plots, int rows, int columns)
        {
            this(plots, rows, columns, 1);
        }

        /**
         * Parameterized constructor.
         *
         * @param wrappers         plots to be displayed; they are read rows by rows and their number should equal rows x columns, but nulls are accepted (ignored)
         * @param rows             number of rows
         * @param columns          number of columns
         * @param noUpdatersQueues represents the number of updaters queues in the queueing system {@link PlotsWrapperController#_queueingSystem}
         */
        public Params(AbstractPlotWrapper[] wrappers, int rows, int columns, int noUpdatersQueues)
        {
            super(wrappers);
            _rows = rows;
            _columns = columns;
            _noUpdatersQueues = noUpdatersQueues;
        }

        /**
         * Parameterized constructor.
         *
         * @param plots            plots to be displayed (default wrapper is used); they are read rows by rows and their number should equal rows x columns, but nulls are accepted (ignored)
         * @param rows             number of rows
         * @param columns          number of columns
         * @param noUpdatersQueues represents the number of updaters queues in the queueing system {@link PlotsWrapperController#_queueingSystem}
         */
        public Params(AbstractPlot[] plots, int rows, int columns, int noUpdatersQueues)
        {
            super(plots);
            _rows = rows;
            _columns = columns;
            _noUpdatersQueues = noUpdatersQueues;
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param wrappers plots encapsulated using the wrapper object.
     * @param rows     number of rows
     * @param columns  number of columns
     */
    public GridPlots(AbstractPlotWrapper[] wrappers, int rows, int columns)
    {
        this(wrappers, rows, columns, 1);
    }

    /**
     * Parameterized constructor.
     *
     * @param plots   plots to be displayed (default wrapped is used)
     * @param rows    number of rows
     * @param columns number of columns
     */
    public GridPlots(AbstractPlot[] plots, int rows, int columns)
    {
        this(plots, rows, columns, 1);
    }

    /**
     * Parameterized constructor.
     *
     * @param wrappers         plots encapsulated using the wrapper object.
     * @param rows             number of rows
     * @param columns          number of columns
     * @param noUpdatersQueues represents the number of updaters queues in the queueing system {@link PlotsWrapperController#_queueingSystem}.
     */
    public GridPlots(AbstractPlotWrapper[] wrappers, int rows, int columns, int noUpdatersQueues)
    {
        this(new Params(wrappers, rows, columns, noUpdatersQueues));
    }

    /**
     * Parameterized constructor.
     *
     * @param plots            plots to be displayed (default wrapped is used)
     * @param rows             number of rows
     * @param columns          number of columns
     * @param noUpdatersQueues represents the number of updaters queues in the queueing system {@link PlotsWrapperController#_queueingSystem}.
     */
    public GridPlots(AbstractPlot[] plots, int rows, int columns, int noUpdatersQueues)
    {
        this(new Params(plots, rows, columns, noUpdatersQueues));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public GridPlots(Params p)
    {
        super(p);
    }

    /**
     * Instantiates the layout.
     *
     * @param p params container
     */
    @Override
    protected void instantiateLayout(AbstractPlotsWrapper.Params p)
    {
        assert p instanceof Params;
        Params pp = (Params) p;

        setLayout(new GridLayout(pp._rows, pp._columns));
        int cnt = 0;
        for (int r = 0; r < pp._rows; r++)
            for (int c = 0; c < pp._columns; c++)
            {
                if (cnt == p._wrappers.length) break;
                AbstractPlotWrapper plotWrapper = p._wrappers[cnt++];
                if (plotWrapper == null) continue;
                add(plotWrapper);
            }
    }
}
