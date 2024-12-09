package updater;

/**
 * Represents association: data source -> data processors.
 * This mapping implies that one data source can be used by multiple processors.
 */
public class SourceToProcessors
{
    /**
     * IDs (in-array indices) of associated data processors.
     */
    protected final int[] _processorID;

    /**
     * Parameterized constructor.
     *
     * @param processorID index of the associated data processor.
     */
    public SourceToProcessors(int processorID)
    {
        this(new int[]{processorID});
    }

    /**
     * Parameterized constructor.
     *
     * @param processorsIDs (in-array indices) of associated data processors.
     */
    public SourceToProcessors(int[] processorsIDs)
    {
        _processorID = processorsIDs;
    }

}
