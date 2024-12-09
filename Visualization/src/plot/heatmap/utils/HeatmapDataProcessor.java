package plot.heatmap.utils;


import datastructure.graph.bst.BST;
import datastructure.graph.bst.TreeNode;

/**
 * Supportive processing class that prepares input (sorted) data to be displayed on heatmaps
 * (bucket coords sorted according to their values).
 *
 * @author MTomczyk
 */

public class HeatmapDataProcessor
{
    /**
     * Result container (sorted values + debug fields).
     */
    public static class SortedValues
    {
        /**
         * Array of sorted values (bucket values).
         */
        public double[] _sortedValues = null;

        /**
         * Array of x-coordinate sorted buckets sorted according to their values.
         */
        public int[] _sortedX_debug = null;

        /**
         * Array of y-coordinate sorted buckets sorted according to their values.
         */
        public int[] _sortedY_debug = null;

        /**
         * Array of z-coordinate sorted buckets sorted according to their values.
         */
        public int[] _sortedZ_debug = null;
    }

    /**
     * Processed data for 2D heatmaps.
     *
     * @param xDiv No. divisions on the plot x-axis.
     * @param yDiv No. divisions on the plot y-axis.
     * @param data Data matrix (2D or 3D; if 2D, it is wrapped as data[1] entry).
     * @return processed data (coords sorted according to their values).
     */
    public Coords[] getCoords2D(int xDiv, int yDiv, double[][] data)
    {
        return processData(xDiv, yDiv, null, new double[][][]{data});
    }

    /**
     * Processed data for 3D heatmaps.
     *
     * @param xDiv No. divisions on the plot x-axis.
     * @param yDiv No. divisions on the plot y-axis.
     * @param zDiv No. divisions on the plot z-axis.
     * @param data Data matrix (2D or 3D; if 2D, it is wrapped as data[1] entry).
     * @return processed data (coords sorted according to their values).
     */
    public Coords[] getCoords3D(int xDiv, int yDiv, int zDiv, double[][][] data)
    {
        return processData(xDiv, yDiv, zDiv, data);
    }

    /**
     * Main method for data processing. Values equal to Double.NEGATIVE_INFINITY are skipped (considered as no entry).
     *
     * @param xDiv No. divisions on the plot x-axis.
     * @param yDiv No. divisions on the plot y-axis.
     * @param zDiv No. divisions on the plot z-axis.
     * @param data Data matrix (2D or 3D; if 2D, it is wrapped as data[1] entry).
     * @return processed data (coords sorted according to their values).
     */
    private Coords[] processData(int xDiv, int yDiv, Integer zDiv, double[][][] data)
    {
        int zdim = data.length;
        int ydim = data[0].length;
        int xdim = data[0][0].length;

        int expectedNoBoxes = xdim * ydim * zdim;

        int noEntries = 0;

        // SORTING STEP ===============================================================================================
        BST bst = new BST(expectedNoBoxes);

        int zlim = 1;
        if (zDiv != null) zlim = zDiv;

        for (int i = 0; i < Math.min(zdim, zlim); i++)
        {
            if (data[i] == null) continue;
            for (int j = 0; j < Math.min(data[i].length, yDiv); j++)
            {
                if (data[i][j] == null) continue;
                noEntries += Math.min(data[i][j].length, xDiv);
                for (int k = 0; k < Math.min(data[i][j].length, xDiv); k++)
                {
                    if (Double.compare(data[i][j][k], Double.NEGATIVE_INFINITY) == 0)
                    {
                        noEntries--;
                        continue;
                    }
                    bst.insert(new Coords(k, j, i, data[i][j][k]));
                }
            }
        }

        if (noEntries == 0) return null;
        Coords[] sC = new Coords[noEntries];

        TreeNode currentNode = bst.getMinNode();
        int pnt = 0;
        sC[pnt++] = (Coords) currentNode.getNodeValue();
        currentNode = bst.getInorderSuccessor(currentNode);

        while (currentNode != null)
        {
            sC[pnt++] = (Coords) currentNode.getNodeValue();
            currentNode = bst.getInorderSuccessor(currentNode);
        }

        bst.releaseReferences();
        return sC;
    }


    /**
     * Supportive method for getting sorted-values array (supports efficient data filtering).
     *
     * @param sortedCoords sorted coords
     * @param dimensions   no. dimensions (2 or 3)
     * @return sorted values result container
     */
    public SortedValues getSortedValues(Coords[] sortedCoords, int dimensions)
    {
        return getSortedValues(sortedCoords, dimensions, false);
    }

    /**
     * Supportive method for getting sorted-values array (supports efficient data filtering).
     *
     * @param sortedCoords sorted coords
     * @param dimensions   no. dimensions (2 or 3)
     * @param debug        if true -> sorted debug arrays are additionally constructed
     * @return sorted values result container
     */
    public SortedValues getSortedValues(Coords[] sortedCoords, int dimensions, boolean debug)
    {
        SortedValues S = new SortedValues();
        if (sortedCoords == null) return S;

        int size = sortedCoords.length;

        if ((S._sortedValues == null) || (S._sortedValues.length != size))
        {
            S._sortedValues = new double[size];
            if (debug)
            {
                S._sortedX_debug = new int[size];
                S._sortedY_debug = new int[size];
                if (dimensions == 3) S._sortedZ_debug = new int[size];
            }
        }

        for (int i = 0; i < size; i++)
        {
            Coords coord = sortedCoords[i];
            S._sortedValues[i] = coord.getValue();

            if (debug)
            {
                S._sortedX_debug[i] = coord._x;
                S._sortedY_debug[i] = coord._y;
                if (dimensions == 3) S._sortedZ_debug[i] = coord._z;
            }
        }
        return S;
    }

}
