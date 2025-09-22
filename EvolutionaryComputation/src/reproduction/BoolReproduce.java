package reproduction;

import ea.IEA;
import exception.PhaseException;
import population.Chromosome;
import population.Parents;
import population.Specimen;
import random.IRandom;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple implementation of {@link reproduction.IReproduce} that delivers a convenient means for reproducing the
 * specimens, i.e., creating the offspring. The default assumptions are as follows:<br>
 * - The number of offspring to create equals the offspring size {@link IEA#getOffspringSize()}.<br>
 * - One offspring is created using one parents object consisting of two parent specimens ({@link Parents})
 * stored in {@link population.SpecimensContainer#getParents()}.<br>
 * - The above assumption can be altered by using different constructors or factory-like methods, e.g., the object can
 * be parameterized to produce two offspring from two parents.<br>
 * - The reproduction is delegated to an object that implements an auxiliary {@link IReproduce interface}.<br>
 * - the offspring decision boolean-vector is instantiated using the parents' vectors (booleans).<br>
 *
 * @author MTomczyk
 */
public class BoolReproduce implements IReproduce
{
    /**
     * Auxiliary interface for reproducing the specimens.
     */
    public interface IReproduce
    {
        /**
         * The main method's signature.
         *
         * @param p1 the first parent (decision boolean-vector; reference; do not modify it)
         * @param p2 the second parent (decision boolean-vector; reference; do not modify it)
         * @param R  random number generator obtained from {@link IEA}
         * @return offspring (decision boolean-vector)
         */
        boolean[] reproduce(boolean[] p1, boolean[] p2, IRandom R);
    }

    /**
     * Auxiliary interface for reproducing the specimens. This reproducer constructs two offspring from two parents.
     */
    public interface IReproduceTO
    {
        /**
         * The main method's signature.
         *
         * @param p1 the first parent (decision boolean-vector; reference; do not modify it)
         * @param p2 the second parent (decision boolean-vector; reference; do not modify it)
         * @param R  random number generator obtained from {@link IEA}
         * @return two offspring packed into a 2D array (the first offspring is the first element; the second offspring
         * is the second element; decision boolean-vectors)
         */
        boolean[][] reproduce(boolean[] p1, boolean[] p2, IRandom R);
    }

    /**
     * Auxiliary interface for reproducing the specimens. This reproducer should construct an arbitrary, but constant
     * and pre-defined, number of offspring solutions from two parents.
     */
    public interface IReproduceMO
    {
        /**
         * The main method's signature.
         *
         * @param p1 the first parent (decision boolean-vector; reference; do not modify it)
         * @param p2 the second parent (decision boolean-vector; reference; do not modify it)
         * @param R  random number generator obtained from {@link IEA}
         * @return offspring packed into a multidimensional array (offspring solutions are packed as rows; decision
         * boolean-vectors)
         */
        boolean[][] reproduce(boolean[] p1, boolean[] p2, IRandom R);

        /**
         * This method should inform about the number of offspring solutions the reproducer will construct using two
         * parents. The number should be constant.
         *
         * @return the number of offspring solutions the reproducer will construct using two parents
         */
        int getNoOffspring();
    }


    /**
     * Auxiliary object for reproducing specimens (it constructs one offspring using two parents).
     */
    private final IReproduce _reproducer;

    /**
     * Auxiliary object for reproducing specimens (it constructs two offspring using two parents).
     */
    private final IReproduceTO _reproducerTO;

    /**
     * Auxiliary objects for reproducing specimens (if provided, it constructs an arbitrary number of offspring
     * solutions using two parents). The array maps the requested number of offspring solutions to construct with a
     * dedicated reproducer.
     */
    private final HashMap<Integer, IReproduceMO> _reproducersMO;

    /**
     * Parameterized constructor. It sets the reproducer to allow only the construction of one offspring from two
     * parents.
     *
     * @param reproducer auxiliary object for reproducing specimens.
     */
    public BoolReproduce(IReproduce reproducer)
    {
        this(reproducer, null, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param reproducer    auxiliary object for reproducing specimens (constructs one offspring using two parents)
     * @param reproducerTO  auxiliary object for reproducing specimens (constructs two offspring using two parents)
     * @param reproducersMO auxiliary objects for reproducing specimens (constructs an arbitrary number of offspring
     *                      solutions using two parents); if different reproducers sharing the same
     *                      {@link IReproduceMO#getNoOffspring()} are provided, only the last one will be used (the
     *                      reproducers are supposed to differ in this regard)
     */
    private BoolReproduce(IReproduce reproducer, IReproduceTO reproducerTO, IReproduceMO[] reproducersMO)
    {
        _reproducer = reproducer;
        _reproducerTO = reproducerTO;
        if ((reproducersMO != null) && (reproducersMO.length > 0))
        {
            _reproducersMO = new HashMap<>(reproducersMO.length);
            for (IReproduceMO reproduceMO : reproducersMO)
                if (reproduceMO != null)
                    _reproducersMO.put(reproduceMO.getNoOffspring(), reproduceMO);
        } else _reproducersMO = null;
    }


    /**
     * Returns an instance parameterized to construct two offspring from two parents.
     *
     * @param reproducer auxiliary object for reproducing specimens
     * @return object instance
     */
    public static BoolReproduce getInstanceTO(IReproduceTO reproducer)
    {
        return new BoolReproduce(null, reproducer, null);
    }

    /**
     * Returns an instance parameterized to construct an arbitrary number of offspring solutions from two parents.
     *
     * @param reproducersMO auxiliary objects for reproducing specimens (constructs an arbitrary number of offspring
     *                      solutions using two parents); if different reproducers sharing the same
     *                      {@link IReproduceMO#getNoOffspring()} are provided, only the last one will be used (the
     *                      reproducers are supposed to differ in this regard)
     * @return object instance
     */
    public static BoolReproduce getInstanceMO(IReproduceMO[] reproducersMO)
    {
        return new BoolReproduce(null, null, reproducersMO);
    }

    /**
     * Returns an instance parameterized to construct a varying number of offspring solutions from two parents (using
     * different reproducers).
     *
     * @param reproducer    auxiliary object for reproducing specimens (dedicated to constructing one offspring from two
     *                      parents)
     * @param reproducerTO  auxiliary object for reproducing specimens (dedicated to constructing two offspring from two
     *                      parents)
     * @param reproducersMO auxiliary objects for reproducing specimens (constructs an arbitrary number of offspring
     *                      solutions using two parents); if different reproducers sharing the same
     *                      {@link IReproduceMO#getNoOffspring()} are provided, only the last one will be used (the
     *                      reproducers are supposed to differ in this regard)
     * @return object instance
     */
    public static BoolReproduce getInstance(IReproduce reproducer, IReproduceTO reproducerTO, IReproduceMO[] reproducersMO)
    {
        return new BoolReproduce(reproducer, reproducerTO, reproducersMO);
    }


    /**
     * The main method that creates the offspring. The reproduction is delegated to an auxiliary object implementing
     * the {@link IReproduce}, {@link IReproduceTO}, or {@link IReproduceMO} interface. The method uses this reproducer
     * that matches {@link Parents#_noOffspringToConstruct} value (determined for each Parents object). If no matching
     * reproducer was supplied when instantiating the class instance, the method will throw an exception.
     *
     * @param ea evolutionary algorithm
     * @return offspring array
     * @throws PhaseException the exception can be thrown
     */
    @Override
    public ArrayList<Specimen> createOffspring(IEA ea) throws PhaseException
    {
        ArrayList<Specimen> offspring = new ArrayList<>(ea.getSpecimensContainer().getNoExpectedOffspringToConstruct());
        for (Parents p : ea.getSpecimensContainer().getParents())
        {
            if (p._noOffspringToConstruct == 1)
            {
                if (_reproducer == null)
                    throw PhaseException.getInstanceWithSource("The number of offspring to construct = 1 for " +
                            "the Parents object being processed, " +
                            "but no dedicated reproducer is provided", this.getClass());
                Specimen s = new Specimen(ea.getCriteria()._no);
                s.setChromosome(new Chromosome(_reproducer.reproduce(
                        p._parents.get(0).getBooleanDecisionVector(),
                        p._parents.get(1).getBooleanDecisionVector(),
                        ea.getR())));
                offspring.add(s);
            } else if (p._noOffspringToConstruct == 2)
            {
                if (_reproducerTO == null)
                    throw PhaseException.getInstanceWithSource("The number of offspring to construct = 2 for " +
                            "the Parents object being processed, " +
                            "but no dedicated reproducer is provided", this.getClass());
                Specimen s1 = new Specimen(ea.getCriteria()._no);
                Specimen s2 = new Specimen(ea.getCriteria()._no);
                boolean[][] o = _reproducerTO.reproduce(
                        p._parents.get(0).getBooleanDecisionVector(),
                        p._parents.get(1).getBooleanDecisionVector(),
                        ea.getR());
                s1.setChromosome(new Chromosome(o[0]));
                s2.setChromosome(new Chromosome(o[1]));
                offspring.add(s1);
                offspring.add(s2);
            } else if ((_reproducersMO != null) && (_reproducersMO.containsKey(p._noOffspringToConstruct)))
            {
                IReproduceMO reproduceMO = _reproducersMO.get(p._noOffspringToConstruct);
                boolean[][] o = reproduceMO.reproduce(
                        p._parents.get(0).getBooleanDecisionVector(),
                        p._parents.get(1).getBooleanDecisionVector(),
                        ea.getR());
                for (boolean[] booleans : o)
                {
                    Specimen s = new Specimen(ea.getCriteria()._no);
                    s.setChromosome(new Chromosome(booleans));
                    offspring.add(s);
                }
            } else
            {
                throw PhaseException.getInstanceWithSource("The number of offspring to construct = "
                        + p._noOffspringToConstruct + " for " +
                        "the Parents object being processed, " +
                        "but no dedicated reproducer is provided", this.getClass());
            }

        }
        return offspring;
    }
}
