package tools.ivemo.heatmap;

import space.Range;

import java.util.Arrays;

/**
 * Abstract class representing IVEMO.Heatmap module for visualizing the robust performance of an EMOA.
 *
 * @author MTomczyk
 */

public abstract class AbstractHeatmap
{
    /**
     * Params container
     */
    public static class Params
    {
        /**
         * If true, the tool prints some notifications (e.g., currently processed test run).
         */
        public boolean _notify = true;

        /**
         * X-axis display range used when dividing the objective space into buckets on the x-axis.
         */
        public Range _xAxisDisplayRange = null;

        /**
         * Number of divisions on the x-axis (used to construct buckets).
         */
        public int _xAxisDivisions = 100;

        /**
         * X-axis display range used when dividing the objective space into buckets on the x-axis.
         */
        public Range _yAxisDisplayRange = null;

        /**
         * Number of divisions on the y-axis (used to construct buckets).
         */
        public int _yAxisDivisions = 100;
    }


    /**
     * The number of dimensions (colorbar-specific dimension is ignored; hence = 2 for 2D visualization or 3 for 3D).
     */
    protected int _dimensions;

    /**
     * If true, the tool prints some notifications (e.g., currently processed test run).
     */
    protected boolean _notify;

    /**
     * X-axis display range used when dividing the objective space into buckets on the x-axis.
     */
    protected Range _xAxisDisplayRange;

    /**
     * Number of divisions on the x-axis (used to construct buckets).
     */
    protected int _xAxisDivisions;

    /**
     * X-axis display range used when dividing the objective space into buckets on the x-axis.
     */
    protected Range _yAxisDisplayRange;

    /**
     * Number of divisions on the y-axis (used to construct buckets).
     */
    protected int _yAxisDivisions;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractHeatmap(Params p)
    {
        instantiateDimensionality();
        _notify = p._notify;
        _xAxisDivisions = p._xAxisDivisions;
        _xAxisDisplayRange = p._xAxisDisplayRange;
        _yAxisDivisions = p._yAxisDivisions;
        _yAxisDisplayRange = p._yAxisDisplayRange;
    }

    /**
     * Auxiliary method that instantiates the dimensionality of the objective space.
     */
    public void instantiateDimensionality()
    {
        _dimensions = 2;
    }


    /**
     * Heatmap global data (to be visualized). If 2D heatmap is generated, the 2D matrix is stored as first (and only) element of this matrix.
     * Note that the dimensions order is (according to index no) Z, Y, X.
     */
    protected double[][][] _data = null;

    /**
     * Auxiliary method for printing messages.
     *
     * @param msg message to be printed
     */
    protected void notify(String msg)
    {
        System.out.println(msg);
    }

    /**
     * Clears final data, i.e., instantiates elements as Double.NEGATIVE_INFINITY.
     */
    protected void clearData()
    {
        for (double[][] datum : _data)
            for (double[] doubles : datum)
                Arrays.fill(doubles, Double.NEGATIVE_INFINITY);
    }
}

