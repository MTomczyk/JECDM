package scenario;

/**
 * Class representing a characteristic/property/feature/key of an experimental scenario.
 * E.g., the key be the optimization problem, algorithm, the number of objectives, etc.
 * (key != the value, e.g., "DTLZ2", "NSGA-II", or "3). Important note: it is assumed
 * that the key label is stored in the upper case.
 *
 * @author MTomczyk
 */
public class Key
{
    /**
     * Key order (ID).
     */
    private int _order;

    /**
     * Key/label/name.
     */
    private final String _label;

    /**
     * Key/label/name (abbreviated).
     */
    private final String _abb;

    /**
     * Parameterized constructor.
     *
     * @param name key/label/name
     * @param abb  key/label/name (abbreviated)
     */
    protected Key(String name, String abb)
    {
        _label = name.toUpperCase();
        _abb = abb.toUpperCase();
    }

    /**
     * Setter for the key order (ID).
     *
     * @param order key order (ID)
     */
    public void setOrder(int order)
    {
        _order = order;
    }

    /**
     * Getter for the key order (ID).
     *
     * @return key order (ID)
     */
    public int getOrder()
    {
        return _order;
    }

    /**
     * Return the key's name.
     *
     * @return key's name
     */
    public String getLabel()
    {
        return _label;
    }

    /**
     * Returns the key's abbreviation.
     *
     * @return key's abbreviation
     */
    public String getAbbreviation()
    {
        return _abb;
    }

    /**
     * Returns the string representation.
     *
     * @return string representation.
     */
    @Override
    public String toString()
    {
        return getLabel();
    }

    /**
     * Checks whether the key is equal to other key (based on the names' comparison).
     *
     * @param o the other object
     * @return true = objects are equal (their names); false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Key ok)) return false;
        return _label.equals(ok.getLabel());
    }

    /**
     * Assumes that the order is the hash code.
     *
     * @return the hash code (order).
     */
    @Override
    public int hashCode()
    {
        return _order;
    }

    /**
     * Auxiliary method for determining key abbreviation.
     *
     * @param key     the input key
     * @param abbrevs the array of keys' abbreviations (can be null; then, the first (up to) three characters from the input key are used to construct the abbreviation)
     * @param index   index in the abbreviation array
     * @return key's abbreviation
     */
    public static String getKeyAbbreviation(String key, String[] abbrevs, int index)
    {
        if ((abbrevs == null) ||
                (abbrevs.length <= index) ||
                (abbrevs[index] == null))
            return key.substring(0, Math.min(3, key.length()));
        else return abbrevs[index];
    }
}
