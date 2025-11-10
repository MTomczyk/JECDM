package problem.moo.zdt;

import ea.IEA;
import phase.IConstruct;
import population.Chromosome;
import population.Gene;
import population.Specimen;

import java.util.ArrayList;

/**
 * Constructs random solutions for ZDT4, 6 problems (10 decision variables). Important note: the decision variables for
 * this problem are bit sequences. They are modeled as boolean arrays and stored within separate genes ({@link Gene}
 * within the top-level specimen's chromosome).
 *
 * @author MTomczyk
 */
public class Construct5 implements IConstruct
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
            Gene[] genes = new Gene[11];
            genes[0] = new Gene(ea.getR().nextBooleans(30));
            for (int i = 1; i < 11; i++) genes[i] = new Gene(ea.getR().nextBooleans(5));
            Specimen specimen = new Specimen(criteria);
            specimen.setChromosome(new Chromosome(genes));
            S.add(specimen);
        }

        return S;
    }

}
