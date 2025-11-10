package problem.moo.cw.cw1;

import ea.IEA;
import phase.IConstruct;
import population.Chromosome;
import population.Specimen;

import java.util.ArrayList;

/**
 * Constructs random solutions for the Crash-worthiness problem (Liao, X., Li, Q., Yang, X. et al. Multiobjective
 * optimization for crash safety design of vehicles using stepwise regression model. Struct Multidisc Optim 35, 561–569
 * (2008). <a href="https://doi.org/10.1007/s00158-007-0163-x">.LINK</a>). Note that the problem assumes that there are
 * 5 continuous decision variables in [1;3] bounds. This implementation assumes using normalized variables in [0; 1]
 * bounds. The linear rescaling is done when evaluating the solutions (see {@link Evaluate}).
 *
 * @author MTomczyk
 */
public class Construct implements IConstruct
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
            double[] x = ea.getR().nextDoubles(5);
            specimen.setChromosome(new Chromosome(x));
            S.add(specimen);
        }
        return S;
    }

}
