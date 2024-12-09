package dataset;

import java.util.LinkedList;

/**
 * Container-like class representing data for rendering.
 *
 * @author MTomczyk
 */
public class Data
{
    /**
     * Raw data. A linked list is used as the data may be collected by receiving various batches, but all stored
     * arrays are interpreted as contiguous. Each element of the double array is a point to be visualized.
     * Note that the elements of the linked list can be null. If so, the lines are broken at these points (this is
     * the interpretation of the default painter). Nulls in stored arrays have no meaning (are skipped).
     */
    private final LinkedList<double[][]> _data;


    /**
     * Parameterized constructor.
     *
     * @param data raw data points
     */
    public Data(double[][] data)
    {
        _data = new LinkedList<>();
        _data.add(data);
    }

    /**
     * Parameterized constructor.
     *
     * @param data         data points
     */
    public Data(LinkedList<double[][]> data)
    {
        _data = data;
    }

    /**
     * Getter for the raw data. A linked list is used as the data may be collected by receiving various batches,
     * but all stored arrays are interpreted as contiguous. Each element of the double array is a point to be visualized.
     * Note that the elements of the linked list can be null. If so, the lines are broken at these points.
     * Nulls in stored arrays have no meaning (are skipped).
     *
     * @return data
     */
    public LinkedList<double[][]> getData()
    {
        return _data;
    }

}
