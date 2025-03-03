package criterion;

/**
 * Wrapper for criteria objects ({@link Criterion}).
 *
 * @author MTomczyk
 */
public class Criteria
{
    /**
     * Criteria array.
     */
    public final Criterion[] _c;

    /**
     * The number of criteria.
     */
    public final int _no;

    /**
     * Parameterized constructor.
     *
     * @param criteria criteria array
     */
    protected Criteria(Criterion[] criteria)
    {
        _c = criteria;
        _no = criteria.length;
    }

    /**
     * Creates a criteria array.
     *
     * @param prefixName common prefix for names of all criteria. E.g., if prefixName = "C", then the names are "C0", "C1", "C2", and so on.
     * @param n          the number of objects (criteria) to create.
     * @param gain       criteria type
     * @return a vector of length n of criteria.
     */
    public static Criteria constructCriteria(String prefixName, int n, boolean gain)
    {
        Criterion[] c = new Criterion[n];
        for (int i = 0; i < n; i++)
            c[i] = new Criterion(String.format("%s%d", prefixName, i), gain, i);
        return new Criteria(c);
    }

    /**
     * Creates a criteria array.
     *
     * @param names criteria names (array length must equal the gains array length)
     * @param gains criteria ``gain'' flags (array length must equal the names array length)
     * @return a vector of length n of criteria.
     */
    public static Criteria constructCriteria(String[] names, boolean[] gains)
    {
        Criterion[] c = new Criterion[names.length];
        for (int i = 0; i < names.length; i++)
            c[i] = new Criterion(names[i], gains[i], i);
        return new Criteria(c);
    }

    /**
     * Auxiliary method for constructing boolean array indicating criteria types (true = gain is preferred; false otherwise).
     * The method constructs and returns a new object each time it is called.
     *
     * @return criteria types array (null if the number of criteria is 0 or the data is invalid)
     */
    public boolean[] getCriteriaTypes()
    {
        if (_no == 0) return null;
        boolean[] types = new boolean[_no];
        for (int i = 0; i < _no; i++) if (_c[i].isGain()) types[i] = true;
        return types;
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    public String getStringRepresentation()
    {
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < _no; c++)
        {
            sb.append(_c[c].toString());
            if (c < _no - 1) sb.append(", ");
        }
        return sb.toString();
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    public String toString()
    {
        return getStringRepresentation();
    }
}
