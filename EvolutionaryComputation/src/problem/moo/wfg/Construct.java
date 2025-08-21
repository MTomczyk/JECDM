package problem.moo.wfg;

import ea.IEA;
import phase.IConstruct;
import population.Chromosome;
import population.Specimen;

import java.util.ArrayList;

/**
 * Constructs random solutions for WFG problems.
 *
 * @author MTomczyk
 */


public class Construct implements IConstruct
{
    /**
     * The number of position-related parameters.
     */
    protected final int _k;

    /**
     * The number of distance-related parameters.
     */
    protected final int _l;

    /**
     * Parameterized constructor.
     * @param k       the number of position-related parameters
     * @param l       the number of distance-related parameters
     */
    public Construct(int k, int l)
    {
        _k = k;
        _l = l;
    }

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
            double[] x = new double[_k + _l];
            for (int i = 0; i < _k; i++) x[i] = ea.getR().nextDouble();
            for (int i = _k; i < _k + _l; i++) x[i] = ea.getR().nextDouble();
            for (int i = 0; i < _k + _l; i++) x[i] *= 2.0d * ((double)i + 1.0d); // different domains
            specimen.setChromosome(new Chromosome(x));
            S.add(specimen);
        }
        return S;
    }

}
