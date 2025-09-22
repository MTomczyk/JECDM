package reproduction;

import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;
import reproduction.valuecheck.IValueCheck;

/**
 * Abstract class assisting in performing standard reproduction using two parents' decision vectors to construct one
 * offspring. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
abstract class AbstractStandardSOReproducer extends AbstractStandardReproducer
{
    /**
     * Crossover operator (designed to produce one offspring solution from one Parents object).
     */
    protected final ICrossover _c;

    /**
     * Parameterized constructor.
     *
     * @param c  crossover operator
     * @param m  mutation operator (can be null; not used then)
     * @param vc object for checking if the resulting variable are in valid bounds (can be null; not used then)
     */
    public AbstractStandardSOReproducer(ICrossover c, IMutate m, IValueCheck vc)
    {
        super(m, vc);
        _c = c;
    }
}
