package problem.moo.zdt;

import ea.IEA;
import phase.IConstruct;
import population.Chromosome;
import population.Specimen;

import java.util.ArrayList;

/**
 * Constructs random solutions for ZDT4, 6 problems (10 decision variables). Important note: ZDT4 involves decision
 * variables with different domains. However, this implementation assumes that the variables are normalized and lie in
 * the [0, 1] range; their appropriate rescaling is performed during evaluation (i.e., x' = -5 + 10x).
 *
 * @author MTomczyk
 */
public class Construct46 implements IConstruct
{
    /**
     * Creates initial population randomly.
     *
     * @param ea evolutionary algorithm
     * @return initial population
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public ArrayList<Specimen> createInitialPopulation(IEA ea)
    {
        ArrayList<Specimen> S = new ArrayList<>(ea.getPopulationSize());
        int criteria = ea.getCriteria()._no;

        for (int s = 0; s < ea.getPopulationSize(); s++)
        {
            Specimen specimen = new Specimen(criteria);
            double[] x = ea.getR().nextDoubles(10);
            specimen.setChromosome(new Chromosome(x));
            S.add(specimen);
        }
        return S;
    }

}
