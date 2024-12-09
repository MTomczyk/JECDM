package updater;

/**
 * Interface for various implementations providing new data to be processed by {@link IDataProcessor}.
 * @author MTomczyk
 */
public interface IDataSource
{
    /**
     * Should create a new data and returns it.
     *
     * @return new data
     */
    double [][] createData();

    /**
     * Can be implemented to clear some auxiliary data.
     */
    void dispose();
}
