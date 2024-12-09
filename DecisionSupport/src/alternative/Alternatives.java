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
     * @param alternative alternative to be wrapped.
     */
    public Alternatives(Alternative alternative)
    {
        super(alternative);
    }


    /**
     * Parameterized constructor. Wraps two alternatives (in the given order).
     *
     * @param alternative1 alternative to be wrapped.
     * @param alternative2 alternative to be wrapped.
     */
    public Alternatives(Alternative alternative1, Alternative alternative2)
    {
        super(alternative1, alternative2);
    }

    /**
     * Parameterized constructor. Wraps three alternatives (in the given order).
     *
     * @param alternative1 alternative to be wrapped.
     * @param alternative2 alternative to be wrapped.
     * @param alternative3 alternative to be wrapped.
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
}
