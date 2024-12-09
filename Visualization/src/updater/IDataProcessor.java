package updater;

import java.util.LinkedList;

/**
 * Interface for classes responsible for processing data to be provided to plots.
 *
 * @author MTomczyk
 */
public interface IDataProcessor
{
    /**
     * The implementation be called to update the internal data maintained by the updater.
     *
     * @param sourceData new data to be processed
     */
    void update(double[][] sourceData);

    /**
     * Getter for the data maintained (and processed) by the updater.
     * When called, the method should construct a new list based on the maintained data and return it (to avoid concurrent modification).
     *
     * @return data
     */
    LinkedList<double[][]> getData();

    /**
     * Can be called to reset the internal data maintained by the updater.
     */
    void reset();

    /**
     * Can be called to dispose data.
     */
    void dispose();
}
