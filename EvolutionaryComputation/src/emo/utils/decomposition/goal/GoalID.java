package emo.utils.decomposition.goal;

import emo.utils.decomposition.family.FamilyID;

import java.util.Objects;


/**
 * Auxiliary object that represents goal's unique ID.
 *
 * @author MTomczyk
 */

public class GoalID
{
    /**
     * Represents the index of an element in an array that stores all the goals.
     */
    protected final int _arrayIndex;

    /**
     * Family ID. Can be used when implementing a co-evolutionary method (distinguished subset of goals).
     */
    protected final FamilyID _familyID;

    /**
     * Optional (can be null) field supporting calculating unique hash codes (see {@link #hashCode()}).
     */
    protected final Integer _hashOffset;

    /**
     * Parameterized constructor.
     *
     * @param familyID  family in-array index
     * @param goalIndex represents the index of an element in an array that stores all the goals
     */
    public GoalID(FamilyID familyID, int goalIndex)
    {
        this(familyID, goalIndex, null);
    }


    /**
     * Parameterized constructor.
     *
     * @param familyID   family in-array index
     * @param goalIndex  represents the index of an element in an array that stores all the goals
     * @param hashOffset Optional (can be null) field supporting calculating unique hash codes (see {@link #hashCode()}).
     */
    public GoalID(FamilyID familyID, int goalIndex, Integer hashOffset)
    {
        _familyID = familyID;
        _arrayIndex = goalIndex;
        _hashOffset = hashOffset;
    }


    /**
     * Getter for the index of an element in an array that stores all the goals.
     *
     * @return goal index
     */
    public int getGoalArrayIndex()
    {
        return _arrayIndex;
    }


    /**
     * Getter for the index of an element in an array that stores all the families.
     *
     * @return family index
     */
    public int getFamilyArrayIndex()
    {
        return _familyID.getArrayIndex();
    }

    /**
     * Overwritten method for calculating the hash code.
     * If the offset is provided (not null; see the class field) the final hash is quickly calculated as offset + goal array index.
     * To avoid collisions, the offset for j-th family should be large enough to exceed a total number of goals of all families of id lesser than j-th.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        if (_hashOffset == null) return Objects.hash(_familyID.getArrayIndex(), _arrayIndex); // lazy init
        return _hashOffset + getGoalArrayIndex();
    }

    /**
     * Overwritten method for comparing locations.
     *
     * @param o object to be compared with
     * @return true -> IDs are the same; false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof GoalID)) return false;
        if (!_familyID.equals(((GoalID) o)._familyID)) return false;
        return _arrayIndex == ((GoalID) o).getGoalArrayIndex();
    }
}
