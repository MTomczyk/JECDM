package plotswrapper;

import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;

import java.awt.*;

/**
 * Plots wrapper that organizes the plots using the grid bag layout (which is, by default, customized to perform as
 * grid layout).
 *
 * @author MTomczyk
 */
public class GridPlots extends AbstractPlotsWrapper
{
    /**
     * Auxiliary interface for classes responsible for adjusting grid bag constraints being processed
     * (and associated with corresponding plot).
     */
    public interface IConstraintsAdjuster
    {
        /**
         * The main method for adjusting grid bag constraints being built.
         * It is assumed that the input gbc is already instantiated as follows: <br>
         * 1) gbc.gridx = c; (current column index) <br>
         * 2) gbc.gridy = r; (current row index) <br>
         * 3) gbc.weightx = 1.0d; <br>
         * 4) gbc.weighty = 1.0d; <br>
         * 5) gbc.fill = GridBagConstraints.BOTH;<br>
         *
         * @param gbc grid bag constraints
         * @param r   row index (starting from 0)
         * @param c   column index (starting from 0)
         * @param i   plot index (starting from zero)
         */
        void adjust(GridBagConstraints gbc, int r, int c, int i);
    }

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
         * Grid bag constraints adjuster. Auxiliary object responsible for adjusting grid bag constraints being processed
         * (and associated with corresponding plot).
         */
        public IConstraintsAdjuster _ca;

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
         * @param wrappers         plots to be displayed; they are read rows by rows and their number should equal rows x
         *                         columns, but nulls are accepted (ignored)
         * @param rows             number of rows
         * @param columns          number of columns
         * @param noUpdatersQueues represents the number of updaters queues in the queueing system
         *                         {@link PlotsWrapperController#_queueingSystem}
         */
        public Params(AbstractPlotWrapper[] wrappers, int rows, int columns, int noUpdatersQueues)
        {
            this(wrappers, rows, columns, noUpdatersQueues, null);
        }

        /**
         * Parameterized constructor.
         *
         * @param wrappers         plots to be displayed; they are read rows by rows and their number should equal rows x
         *                         columns, but nulls are accepted (ignored)
         * @param rows             number of rows
         * @param columns          number of columns
         * @param noUpdatersQueues represents the number of updaters queues in the queueing system
         *                         {@link PlotsWrapperController#_queueingSystem}
         * @param ca               grid bag constraints adjuster. Auxiliary object responsible for adjusting grid bag constraints
         *                         being processed (and associated with corresponding plot)
         */
        public Params(AbstractPlotWrapper[] wrappers, int rows, int columns, int noUpdatersQueues, IConstraintsAdjuster ca)
        {
            super(wrappers);
            _rows = rows;
            _columns = columns;
            _noUpdatersQueues = noUpdatersQueues;
            _ca = ca;
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
            this(plots, rows, columns, noUpdatersQueues, null);
        }

        /**
         * Parameterized constructor.
         *
         * @param plots            plots to be displayed (default wrapper is used); they are read rows by rows and their number should equal rows x columns, but nulls are accepted (ignored)
         * @param rows             number of rows
         * @param columns          number of columns
         * @param noUpdatersQueues represents the number of updaters queues in the queueing system {@link PlotsWrapperController#_queueingSystem}
         * @param ca               grid bag constraints adjuster. Auxiliary object responsible for adjusting grid bag constraints
         *                         being processed (and associated with corresponding plot)
         */
        public Params(AbstractPlot[] plots, int rows, int columns, int noUpdatersQueues, IConstraintsAdjuster ca)
        {
            super(plots);
            _rows = rows;
            _columns = columns;
            _noUpdatersQueues = noUpdatersQueues;
            _ca = ca;
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
        setLayout(new GridBagLayout());

        int cnt = 0;
        for (int r = 0; r < pp._rows; r++)
            for (int c = 0; c < pp._columns; c++)
            {
                if (cnt == p._wrappers.length) break;
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = c;
                gbc.gridy = r;
                gbc.weightx = 1.0d;
                gbc.weighty = 1.0d;
                gbc.fill = GridBagConstraints.BOTH;
                if (pp._ca != null) pp._ca.adjust(gbc, r, c, cnt);
                AbstractPlotWrapper plotWrapper = p._wrappers[cnt++];
                if (plotWrapper == null) continue;
                add(plotWrapper, gbc);
            }
    }
}
