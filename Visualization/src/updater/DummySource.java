package updater;

/**
 * Dummy source (does nothing)
 *
 * @author MTomczyk
 */

public class DummySource extends AbstractSource implements IDataSource
{
    /**
     * Returns null
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        return null;
    }
}
