package system.dm;

/**
 * This class represents a DM and also plays a role of a unique identified (used, e.g., for hashing).
 *
 * @author MTomczyk
 */


public class DM
{
    /**
     * Unique ID (integer).
     */
    private final int _id;

    /**
     * Unique name.
     */
    private final String _name;

    /**
     * Parameterized constructor.
     *
     * @param id   unique id
     * @param name unique name
     */
    public DM(int id, String name)
    {
        _id = id;
        _name = name;
    }

    /**
     * Getter for the decision maker's name.
     *
     * @return the decision maker's name
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Checks equality with other object (based on comparing ID).
     *
     * @param o other object
     * @return true, if this object equals another; false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DM dm = (DM) o;
        return _id == dm._id;
    }

    /**
     * Returns the hash code (base on the ID).
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return _id;
    }

    /**
     * Returns the string representation (name)
     *
     * @return returns the string representation (name)
     */
    public String toString()
    {
        return _name;
    }
}
