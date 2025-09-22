package reproduction;

import reproduction.operators.crossover.ICrossoverMO;
import reproduction.operators.mutation.IMutate;
import reproduction.valuecheck.IValueCheck;

/**
 * Abstract class assisting in performing standard reproduction using two parents' decision vectors to construct an
 * arbitrary number, but constant and pre-defined, of offspring solutions. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
abstract class AbstractStandardMOReproducer extends AbstractStandardReproducer
{
    /**
     * Crossover operator (designed to produce an arbitrary number, but constant and pre-defined, of offspring solutions
     * from one Parents object).
     */
    protected final ICrossoverMO _c;

    /**
     * Parameterized constructor.
     *
     * @param c  crossover operator (designed to produce an arbitrary number, but constant and pre-defined, of offspring
     *           solutions from one Parents object)
     * @param m  mutation operator (can be null; not used then)
     * @param vc object for checking if the resulting variable are in valid bounds (can be null; not used then)
     */
    public AbstractStandardMOReproducer(ICrossoverMO c, IMutate m, IValueCheck vc)
    {
        super(m, vc);
        _c = c;
    }

    /**
     * This method should inform about the number of offspring solutions the reproducer will construct using two
     * parents. The number should be constant.
     *
     * @return the number of offspring solutions the reproducer will construct using two parents
     */
    public int getNoOffspring()
    {
        return _c.getNoOffspring();
    }
}
