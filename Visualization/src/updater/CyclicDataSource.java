package updater;

/**
 * Simple implementation of {@link IDataSource}. It handles a fixed, provided by the constructor, data bundle
 * in the form of 3d double matrix: [data set number][entries][attributes]. Each time the data collection is required,
 * a next data package from the bundle is provided. If the data limit would be exceeded, the counter goes back to 0-row.
 *
 * @author MTomczyk
 */
public class CyclicDataSource extends AbstractSource implements IDataSource
{
    /**
     * Data to be provided.
     */
    private final double[][][] _data;

    /**
     * Current index.
     */
    private int _index = 0;

    /**
     * Parameterized constructor (uses just one data set).
     *
     * @param data data bundle ([entries][attributes])
     */
    public CyclicDataSource(double[][] data)
    {
        this(new double[][][]{data});
    }

    /**
     * Parameterized constructor.
     *
     * @param data data bundle ([data set number][entries][attributes])
     */
    public CyclicDataSource(double[][][] data)
    {
        _data = data;
    }

    /**
     * Creates new data and returns it.
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        if (_data == null) return null;
        if (_data.length == 0) return null;

        double[][] d = _data[_index];
        _index++;
        if (_index > _data.length - 1) _index = 0;

        return d;
    }
}
