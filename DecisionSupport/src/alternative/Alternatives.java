package alternative;

import java.util.ArrayList;

/**
 * Default wrapper for the alternatives array.
 *
 * @author MTomczyk
 */
public class Alternatives extends AbstractAlternatives<Alternative>
{
    /**
     * Parameterized constructor. If the input is null, an empty array is created.
     *
     * @param alternatives array of alternatives to be wrapped
     */
    public Alternatives(ArrayList<Alternative> alternatives)
    {
        super(alternatives);
    }

    /**
     * Parameterized constructor. Wraps one alternative.
     *
     * @param alternative alternative to be wrapped
     */
    public Alternatives(Alternative alternative)
    {
        super(alternative);
    }


    /**
     * Parameterized constructor. Wraps two alternatives (in the given order).
     *
     * @param alternative1 alternative to be wrapped
     * @param alternative2 alternative to be wrapped
     */
    public Alternatives(Alternative alternative1, Alternative alternative2)
    {
        super(alternative1, alternative2);
    }

    /**
     * Parameterized constructor. Wraps three alternatives (in the given order).
     *
     * @param alternative1 alternative to be wrapped
     * @param alternative2 alternative to be wrapped
     * @param alternative3 alternative to be wrapped
     */
    public Alternatives(Alternative alternative1, Alternative alternative2, Alternative alternative3)
    {
        super(alternative1, alternative2, alternative3);
    }

    /**
     * Returns the k-th alternative.
     *
     * @param index index
     * @return k-th alternative
     */
    @Override
    public Alternative get(int index)
    {
        return _alternatives.get(index);
    }


    /**
     * Creates an array of length of n of alternatives (objects).
     *
     * @param prefixName common prefix for names of all alternatives; e.g., if prefixName = "A", then the names are
     *                   "A0", "A1", "A2", and so on
     * @param n          the number of alternatives to create
     * @param criteria   the number of criteria
     * @return a vector of length n of alternatives
     */
    public static Alternatives getAlternativeArray(String prefixName, int n, int criteria)
    {
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray(prefixName, n, criteria);
        return alternatives != null ? new Alternatives(alternatives) : null;
    }

    /**
     * Creates an array of length of n of alternatives (objects).
     *
     * @param prefixName            common prefix for names of all alternatives; e.g., if prefixName = "A", then the
     *                              names are "A" + suffixStartingCounter, "A" + (suffixStartingCounter + 1),  "A" +
     *                              (suffixStartingCounter + 2), and so on.
     * @param n                     the number of alternatives to create (at least 0)
     * @param criteria              the number of criteria
     * @param suffixStartingCounter suffix starting counter for the names of alternatives (e.g., if = 1, the first
     *                              alternative will be named "A1", and so on)
     * @return a vector of length n of alternatives (null, if the input data is invalid)
     */
    public static Alternatives getAlternativeArray(String prefixName, int n, int criteria, int suffixStartingCounter)
    {
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray(prefixName, n, criteria, suffixStartingCounter);
        return alternatives != null ? new Alternatives(alternatives) : null;
    }

    /**
     * Creates an alternatives array using an evaluation matrix (each row is linked to a different alternative).
     *
     * @param prefixName common prefix for names of all alternatives; e.g., if prefixName = "A", then the names are
     *                   "A0", "A1", "A2", and so on.
     * @param e          evaluation matrix
     * @return a vector of length n of alternatives
     */
    public static Alternatives getAlternativeArray(String prefixName, double[][] e)
    {
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray(prefixName, e);
        return alternatives != null ? new Alternatives(alternatives) : null;
    }

    /**
     * Creates an alternatives array using an evaluation matrix (each row is linked to a different alternative).
     *
     * @param prefixName           common prefix for names of all alternatives; e.g., if prefixName = "A", then the
     *                             names are "A" + suffixStartingCounter, "A" + (suffixStartingCounter + 1),  "A" +
     *                             (suffixStartingCounter + 2), and so on.
     * @param e                    evaluation matrix
     * @param suffixStarterCounter suffix starting counter for the names of alternatives (e.g., if = 1, the first
     *                             alternative will be named "A1", and so on)
     * @return a vector of length n of alternatives (null, if the input data is invalid, e.g., when e == null)
     */
    public static Alternatives getAlternativeArray(String prefixName, double[][] e, int suffixStarterCounter)
    {
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray(prefixName, e, suffixStarterCounter);
        return alternatives != null ? new Alternatives(alternatives) : null;
    }
}
