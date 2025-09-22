package criterion;

import utils.ArrayUtils;

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
     * @param criterion criterion
     */
    protected Criteria(Criterion criterion)
    {
        _c = new Criterion[]{criterion};
        _no = 1;
    }

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
     * Creates a criteria object (consisting of a single criterion).
     *
     * @param name criterion name
     * @param gain criterion type (gain = true (the more, the better); false otherwise)
     * @return a vector of length n of criteria
     */
    public static Criteria constructCriterion(String name, boolean gain)
    {
        return new Criteria(new Criterion(name, gain, 0));
    }

    /**
     * Creates a criteria object.
     *
     * @param prefix common prefix for criteria names; e.g., if prefixName = "C", then the names are "C0", "C1", "C2",
     *               and so on
     * @param n      the number of objects (criteria) to create
     * @param gain   criteria type (gain = true (the more, the better); false otherwise)
     * @return a vector of length n of criteria (null, if the input data is invalid)
     */
    public static Criteria constructCriteria(String prefix, int n, boolean gain)
    {
        if (n < 0) return null;
        return constructCriteria(prefix, ArrayUtils.getBooleanArray(n, gain));
    }

    /**
     * Creates a criteria object.
     *
     * @param prefix                common prefix for names of all criteria; e.g., if prefixName = "C", then the
     *                              names are "C" + suffixStartingCounter, "C" + (suffixStartingCounter + 1),  "C" +
     *                              (suffixStartingCounter + 2), and so on
     * @param n                     the number of objects (criteria) to create
     * @param gain                  criteria type (gain = true (the more, the better); false otherwise)
     * @param suffixStartingCounter suffix starting counter for the names of alternatives (e.g., if = 1, the first
     *                              alternative will be named "A1", and so on)
     * @return a vector of length n of criteria (null, if the input data is invalid)
     */
    public static Criteria constructCriteria(String prefix, int n, boolean gain, int suffixStartingCounter)
    {
        if (n < 0) return null;
        return constructCriteria(prefix, ArrayUtils.getBooleanArray(n, gain), suffixStartingCounter);
    }

    /**
     * Creates a criteria object.
     *
     * @param prefix common prefix for criteria names; e.g., if prefixName = "C", then the names are "C0", "C1", "C2",
     *               and so on
     * @param gain   criteria type (gain = true (the more, the better); false otherwise)
     * @return a vector of length n of criteria (null, if the input data is invalid)
     */
    public static Criteria constructCriteria(String prefix, boolean[] gain)
    {
        if (gain == null) return null;
        return constructCriteria(prefix, gain, 0);
    }

    /**
     * Creates a criteria object.
     *
     * @param prefix                common prefix for names of all criteria; e.g., if prefixName = "C", then the
     *                              names are "C" + suffixStartingCounter, "C" + (suffixStartingCounter + 1),  "C" +
     *                              (suffixStartingCounter + 2), and so on
     * @param gain                  criteria type (gain = true (the more, the better); false otherwise)
     * @param suffixStartingCounter suffix starting counter for the names of alternatives (e.g., if = 1, the first
     *                              alternative will be named "A1", and so on)
     * @return a vector of length n of criteria (null, if the input data is invalid)
     */
    public static Criteria constructCriteria(String prefix, boolean[] gain, int suffixStartingCounter)
    {
        if (gain == null) return null;
        Criterion[] c = new Criterion[gain.length];
        int idx = 0;
        for (int i = suffixStartingCounter; i < gain.length + suffixStartingCounter; i++)
            c[idx] = new Criterion(String.format("%s%d", prefix, i), gain[idx++], i);
        return new Criteria(c);
    }

    /**
     * Creates a criteria object.
     *
     * @param names criteria names (array length must equal the gains array length)
     * @param gains criteria ``gain'' flags (array length must equal the names array length; gain = true (the more, the
     *              better); false otherwise)
     * @return a vector of length n of criteria (null, if the input data is invalid)
     */
    public static Criteria constructCriteria(String[] names, boolean[] gains)
    {
        if (names == null) return null;
        if (gains == null) return null;
        if (names.length != gains.length) return null;

        Criterion[] c = new Criterion[names.length];
        for (int i = 0; i < names.length; i++)
            c[i] = new Criterion(names[i], gains[i], i);
        return new Criteria(c);
    }

    /**
     * Creates a criteria object from a single criterion.
     *
     * @param C criterion to be wrapped
     * @return a criterion object (null, if the input data is invalid)
     */
    public static Criteria constructCriteria(Criterion C)
    {
        if (C == null) return null;
        Criterion[] c = new Criterion[] {C};
        return new Criteria(c);
    }

    /**
     * Auxiliary method for constructing boolean array indicating criteria types (true = gain is preferred; false
     * otherwise). The method constructs and returns a new object each time it is called.
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
     * Returns the string representation (criteria names separated by a comma).
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
     * Returns the string representation (criteria names separated by a comma).
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return getStringRepresentation();
    }
}
