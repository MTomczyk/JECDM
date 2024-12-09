package updater;

import dataset.IDataSet;

/**
 * Represents association: data processors -> data sources.
 * This mapping implies that multiple plots can use indications from the updater.
 */
public class ProcessorToPlots
{
    /**
     * Reference data set objects used when constructing new ones.
     * They provide info on the displayed data sets styles and the painter used.
     * Each reference set is linked to one plot id in the ''_plotIDs'' array.
     */
    protected final IDataSet [] _referenceDataSets;

    /**
     * Plot ids linked with the data processor.
     */
    protected final int[] _plotIDs;

    /**
     * Parameterized constructor.
     *
     * @param plotID linked plot ID
     * @param referenceDataSet  reference data sets object used when constructing new ones
     */
    public ProcessorToPlots(int plotID, IDataSet referenceDataSet)
    {
        this(new int[]{plotID}, new IDataSet[]{referenceDataSet});
    }

    /**
     * Parameterized constructor.
     *
     * @param plotIDs linked plot ids
     * @param referenceDataSet they provide info on the displayed data sets styles and the painter used; each reference
     *                         set is linked to one plot id in the ''_plotIDs'' array
     */
    public ProcessorToPlots(int[] plotIDs, IDataSet [] referenceDataSet)
    {
        _plotIDs = plotIDs;
        _referenceDataSets = referenceDataSet;
    }
}
