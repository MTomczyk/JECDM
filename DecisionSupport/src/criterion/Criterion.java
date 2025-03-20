package criterion;

/**
 * Class representing a criterion.
 *
 * @author MTomczyk
 */

public class Criterion
{
    /**
     * Criterion name.
     */
    private final String _name;
    /**
     * Criterion type, gain = true (the more, the better); false otherwise.
     */
    private final boolean _gain;

    /**
     * The identification number.
     */
    private final int _id;

    /**
     * Parameterized constructor.
     *
     * @param name criterion name
     * @param gain criterion type
     * @param id   the identification number
     */
    protected Criterion(String name, boolean gain, int id)
    {
        _name = name;
        _gain = gain;
        _id = id;
    }


    /**
     * Getter for name.
     *
     * @return criterion name
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Getter for criterion type.
     *
     * @return criterion type
     */
    public boolean isGain()
    {
        return _gain;
    }


    /**
     * Getter for criterion id.
     *
     * @return criterion if
     */
    public int getID()
    {
        return _id;
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _name;
    }

    /**
     * Returns the hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return _id;
    }
}
