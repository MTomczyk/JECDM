package emo.utils.decomposition.family;


/**
 * Object storing data on the family ID.
 *
 * @author MTomczyk
 */
public class FamilyID
{
    /**
     * Represents the index of an element in an array that stores all the families.
     */
    private final int _arrayIndex;

    /**
     * Parameterized constructor.
     *
     * @param arrayIndex index of an element in an array that stores all the families.
     */
    public FamilyID(int arrayIndex)
    {
        _arrayIndex = arrayIndex;
    }

    /**
     * Getter for the index of an element in an array that stores all the families.
     *
     * @return index of an element in an array that stores all the families
     */
    public int getArrayIndex()
    {
        return _arrayIndex;
    }

    /**
     * Overwritten method for comparing family IDs (based on comparing {@link FamilyID#_arrayIndex}).
     *
     * @param o object to be compared with
     * @return true -> IDs are the same; false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof FamilyID)) return false;
        return _arrayIndex == ((FamilyID) o).getArrayIndex();
    }

    /**
     * Overwritten method for calculating the hash code. The array index {@link FamilyID#_arrayIndex} is used as the
     * hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return _arrayIndex;
    }
}
