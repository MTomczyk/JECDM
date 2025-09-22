package reproduction;

import reproduction.operators.mutation.IMutate;
import reproduction.valuecheck.IValueCheck;

/**
 * Abstract class assisting in performing standard reproduction using two parents' decision vectors. Provides common
 * fields and functionalities.
 *
 * @author MTomczyk
 */
abstract class AbstractStandardReproducer
{
    /**
     * Mutation operator (can be null; not used then).
     */
    protected final IMutate _m;

    /**
     * Object for checking if the resulting variable are in valid bounds (can be null; not used then).
     */
    protected final IValueCheck _vc;

    /**
     * Parameterized constructor.
     *
     * @param m  mutation operator (can be null; not used then)
     * @param vc object for checking if the resulting variable are in valid bounds (can be null; not used then)
     */
    public AbstractStandardReproducer(IMutate m, IValueCheck vc)
    {
        _m = m;
        _vc = vc;
    }
}
