package plot.heatmap.utils;


import datastructure.graph.bst.INodeValue;
import valuewrapper.DoubleWrapper;

/**
 * Supportive class storing info on a single box (heatmap entry). The supervising method sorts boxes according to their
 * values and passes such a sorted array into VBO. This way, rendering boxes whose values fall into specified ranges is facilitated.
 *
 * @author MTomczyk
 */
public class Coords extends DoubleWrapper implements INodeValue
{
    /**
     * X coordinate.
     */
    public final int _x;

    /**
     * Y coordinate.
     */
    public final int _y;

    /**
     * Z coordinate.
     */
    public final int _z;

    /**
     * Parameterized constructor.
     *
     * @param x     x coordinate
     * @param y     y coordinate
     * @param z     z coordinate
     * @param value stored value
     */
    public Coords(int x, int y, int z, double value)
    {
        super(value);
        _x = x;
        _y = y;
        _z = z;
    }

    /**
     * Method for comparing two nodes. Used when building the tree.
     *
     * @param otherNode other node the current node is to be compared with
     * @return 0 = nodes are equal, 1 the current node is associated with a strictly greater value, -1 the current node is considered strictly smaller.
     */
    @Override
    public int compare(INodeValue otherNode)
    {
        return Double.compare(_value, otherNode.getValue());
    }
}
