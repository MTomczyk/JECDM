package problem.moo.zdt;

import ea.IEA;
import phase.IConstruct;
import population.Chromosome;
import population.Specimen;

import java.util.ArrayList;

/**
 * Constructs random solutions for ZDT1-3 problems (30 decision variables).
 *
 * @author MTomczyk
 */
public class Construct123 implements IConstruct
{
    /**
     * Creates initial population randomly.
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
            double[] x = ea.getR().nextDoubles(30);
            specimen.setChromosome(new Chromosome(x));
            S.add(specimen);
        }
        return S;
    }

}
