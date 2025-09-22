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
 * - The reproduction is delegated to an object that implements an auxiliary {@link IReproduce interface}.
 * - the offspring decision chromosome is instantiated using the parents' chromosomes.<br>
 *
 * @author MTomczyk
 */
public class ChromosomeReproduce implements IReproduce
{
    /**
     * Auxiliary interface for reproducing the specimens.
     */
    public interface IReproduce
    {
        /**
         * The main method's signature.
         *
         * @param c1 the first parent (chromosome; reference; do not modify it)
         * @param c2 the second parent (chromosome; reference; do not modify it)
         * @param R  random number generator obtained from {@link IEA}
         * @return offspring (decision chromosome)
         */
        Chromosome reproduce(Chromosome c1, Chromosome c2, IRandom R);
    }

    /**
     * Auxiliary interface for reproducing the specimens. This reproducer constructs two offspring from two parents.
     */
    public interface IReproduceTO
    {
        /**
         * The main method's signature.
         *
         * @param p1 the first parent (chromosome; reference; do not modify it)
         * @param p2 the second parent (chromosome; reference; do not modify it)
         * @param R  random number generator obtained from {@link IEA}
         * @return two offspring packed into an array (the first offspring is the first element; the second offspring
         * is the second element; chromosomes)
         */
        Chromosome[] reproduce(Chromosome p1, Chromosome p2, IRandom R);
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
         * @param p1 the first parent (chromosome; reference; do not modify it)
         * @param p2 the second parent (chromosome; reference; do not modify it)
         * @param R  random number generator obtained from {@link IEA}
         * @return offspring packed into a multidimensional array (offspring solutions are packed as rows; chromosomes)
         */
        Chromosome[] reproduce(Chromosome p1, Chromosome p2, IRandom R);

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
    public ChromosomeReproduce(IReproduce reproducer)
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
    private ChromosomeReproduce(IReproduce reproducer, IReproduceTO reproducerTO, IReproduceMO[] reproducersMO)
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
    public static ChromosomeReproduce getInstanceTO(IReproduceTO reproducer)
    {
        return new ChromosomeReproduce(null, reproducer, null);
    }

    /**
     * Returns an instance parameterized to construct an arbitrary number of offspring solutions from two parents.
     *
     * @param reproducersMO auxiliary objects for reproducing specimens (constructs an arbitrary number of offspring
     *                      solutions using two parents); if different reproducers sharing the same
     *                      {@link BoolReproduce.IReproduceMO#getNoOffspring()} are provided, only the last one will be
     *                      used (the
     *                      reproducers are supposed to differ in this regard)
     * @return object instance
     */
    public static ChromosomeReproduce getInstanceMO(IReproduceMO[] reproducersMO)
    {
        return new ChromosomeReproduce(null, null, reproducersMO);
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
     *                      {@link BoolReproduce.IReproduceMO#getNoOffspring()} are provided, only the last one will be
     *                      used (the
     *                      reproducers are supposed to differ in this regard)
     * @return object instance
     */
    public static ChromosomeReproduce getInstance(IReproduce reproducer, IReproduceTO reproducerTO, IReproduceMO[] reproducersMO)
    {
        return new ChromosomeReproduce(reproducer, reproducerTO, reproducersMO);
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
                s.setChromosome(_reproducer.reproduce(
                        p._parents.get(0).getChromosome(),
                        p._parents.get(1).getChromosome(),
                        ea.getR()));
                offspring.add(s);
            } else if (p._noOffspringToConstruct == 2)
            {
                if (_reproducerTO == null)
                    throw PhaseException.getInstanceWithSource("The number of offspring to construct = 2 for " +
                            "the Parents object being processed, " +
                            "but no dedicated reproducer is provided", this.getClass());

                Specimen s1 = new Specimen(ea.getCriteria()._no);
                Specimen s2 = new Specimen(ea.getCriteria()._no);
                Chromosome[] o = _reproducerTO.reproduce(
                        p._parents.get(0).getChromosome(),
                        p._parents.get(1).getChromosome(),
                        ea.getR());
                s1.setChromosome(o[0]);
                s2.setChromosome(o[1]);
                offspring.add(s1);
                offspring.add(s2);
            } else if ((_reproducersMO != null) && (_reproducersMO.containsKey(p._noOffspringToConstruct)))
            {
                ChromosomeReproduce.IReproduceMO reproduceMO = _reproducersMO.get(p._noOffspringToConstruct);
                Chromosome[] o = reproduceMO.reproduce(
                        p._parents.get(0).getChromosome(),
                        p._parents.get(1).getChromosome(),
                        ea.getR());
                for (Chromosome chromosome : o)
                {
                    Specimen s = new Specimen(ea.getCriteria()._no);
                    s.setChromosome(chromosome);
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
