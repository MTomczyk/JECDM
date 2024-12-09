package updater;

/**
 * Abstract implementation of {@link IDataSource}
 *
 * @author MTomczyk
 */
public class AbstractSource implements IDataSource
{
    /**
     * Creates new data and returns it.
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        return new double[0][];
    }

    /**
     * Clears data.
     */
    @Override
    public void dispose()
    {

    }
}
