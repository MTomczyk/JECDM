package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common;

import ea.EA;
import population.Chromosome;
import population.Parents;
import population.Specimen;
import reproduction.IReproduce;
import reproduction.operators.crossover.SBX;
import reproduction.operators.mutation.PM;
import reproduction.valuecheck.Wrap;
import space.Range;

import java.util.ArrayList;

/**
 * The implementation of IReproduce interface. It is assumed that the constructed size equals ea.getOffspringSize().
 * Further, the number of parents per offspring is two, and the number of parent objects stored in the specimen container
 * equals the offspring size. The offspring specimens are constructed by applying an SBX operator to parents'
 * double decision vectors, followed by applying a polynomial mutation (with probability of 1 / the number of items).
 * Both operators used a distribution index of 10.
 *
 * @author MTomczyk
 */
public class ContReproducer implements IReproduce
{
    /**
     * SBX operator.
     */
    private final SBX _SBX;

    /**
     * Polynomial mutation operator
     */
    private final PM _PM;

    /**
     * Parameterized constructor.
     *
     */
    public ContReproducer()
    {
        // instantiate the crossover and mutation operators

        // SBX operator is used (distribution index of 10)
        SBX.Params pSBX = new SBX.Params(1.0d, 10.0d);

        // bounds for the double decision variables (if null, the default [0, 1] bounds are used if the value check
        // object is used (see below).
        pSBX._doubleBounds = new Range[]{Range.getNormalRange(), Range.getNormalRange()};
        // Used to correct infeasible variable values. Incorrect values are wrapped around the boundaries.
        // Can be null -> not used.
        pSBX._valueCheck = new Wrap();

        _SBX = new SBX(pSBX);

        PM.Params pM = new PM.Params(1.0d / 2.0d, 10.0d);
        pM._valueCheck = new Wrap();
        pM._doubleBounds = pSBX._doubleBounds; // share the objects
        _PM = new PM(pM);
    }

    /**
     * Creates an array of offspring specimens (of size equal to ``offspring size'').
     *
     * @param ea evolutionary algorithm
     * @return offspring specimens
     */
    @Override
    public ArrayList<Specimen> createOffspring(EA ea)
    {
        int os = ea.getOffspringSize(); // get the offspring size
        ArrayList<Specimen> offspring = new ArrayList<>(os);
        for (int i = 0; i < os; i++)
        {
            Parents parents = ea.getSpecimensContainer().getParents().get(i);
            Specimen p1 = parents._parents.get(0);
            Specimen p2 = parents._parents.get(1);
            double[] bv1 = p1.getDoubleDecisionVector();
            double[] bv2 = p2.getDoubleDecisionVector();
            double[] ob = _SBX.crossover(bv1, bv2, ea.getR());
            _PM.mutate(ob, ea.getR());

            // create the specimen
            Chromosome chromosome = new Chromosome(ob);
            Specimen specimen = new Specimen(2);
            specimen.setChromosome(chromosome);
            offspring.add(specimen);
        }
        return offspring;
    }
}
