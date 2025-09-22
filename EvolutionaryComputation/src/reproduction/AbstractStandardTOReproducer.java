package reproduction;

import reproduction.operators.crossover.ICrossoverTO;
import reproduction.operators.mutation.IMutate;
import reproduction.valuecheck.IValueCheck;

/**
 * Abstract class assisting in performing standard reproduction using two parents' decision vectors to construct two
 * offspring. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
abstract class AbstractStandardTOReproducer extends AbstractStandardReproducer
{
    /**
     * Crossover operator (designed to produce two offspring solutions from one Parents object).
     */
    protected final ICrossoverTO _c;

    /**
     * Parameterized constructor.
     *
     * @param c crossover operator (designed to produce two offspring solutions from one Parents object)
     * @param m   mutation operator (can be null; not used then)
     * @param vc  object for checking if the resulting variable are in valid bounds (can be null; not used then)
     */
    public AbstractStandardTOReproducer(ICrossoverTO c, IMutate m, IValueCheck vc)
    {
        super(m, vc);
        _c = c;
    }
}
