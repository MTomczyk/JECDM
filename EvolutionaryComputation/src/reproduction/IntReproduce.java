package reproduction;

import ea.EA;
import exception.PhaseException;
import population.Chromosome;
import population.Parents;
import population.Specimen;
import random.IRandom;

import java.util.ArrayList;

/**
 * A simple implementation of {@link reproduction.IReproduce} that delivers a convenient means for reproducing the
 * specimens, i.e., creating the offspring. The default assumptions are as follows:
 * - The number of offspring to create equals the offspring size {@link EA#getOffspringSize()}.
 * - One offspring is created using one parents object consisting of two parent specimens ({@link Parents})
 * stored in {@link population.SpecimensContainer#getParents()}.
 * - The reproduction is delegated to an object that implements an auxiliary {@link IReproduce interface}.
 * - the offspring decision int-vector is instantiated using the parents' vectors (integers)
 *
 * @author MTomczyk
 */
public class IntReproduce implements IReproduce
{
    /**
     * Auxiliary interface for reproducing the specimens.
     */
    public interface IReproduce
    {
        /**
         * The main method's signature.
         *
         * @param p1 the first parent (decision int-vector)
         * @param p2 the second parent (decision int-vector)
         * @param R  random number generator obtained from {@link EA}
         * @return offspring (decision int-vector)
         */
        int[] reproduce(int[] p1, int[] p2, IRandom R);
    }

    /**
     * Auxiliary object for reproducing specimens.
     */
    private final IReproduce _reproducer;

    /**
     * Parameterized constructor.
     *
     * @param reproducer auxiliary object for reproducing specimens.
     */
    public IntReproduce(IReproduce reproducer)
    {
        _reproducer = reproducer;
    }

    /**
     * The main method that creates the offspring. The reproduction is delegated to an auxiliary object implementing
     * the {@link IReproduce} interface.
     *
     * @param ea evolutionary algorithm
     * @return offspring array
     * @throws PhaseException an exception can be thrown and propagated higher
     */
    @Override
    public ArrayList<Specimen> createOffspring(EA ea) throws PhaseException
    {
        ArrayList<Specimen> offspring = new ArrayList<>(ea.getOffspringSize());
        for (Parents p : ea.getSpecimensContainer().getParents())
        {
            Specimen s = new Specimen(ea.getCriteria()._no);
            s.setChromosome(new Chromosome(_reproducer.reproduce(
                    p._parents.get(0).getIntDecisionVector(),
                    p._parents.get(1).getIntDecisionVector(),
                    ea.getR())));
            offspring.add(s);
        }
        return offspring;
    }
}
