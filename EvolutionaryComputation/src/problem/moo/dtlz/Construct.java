package problem.moo.dtlz;

import ea.EA;
import phase.IConstruct;
import population.Chromosome;
import population.Specimen;

import java.util.ArrayList;

/**
 * Constructs random solutions for DTLZ problems.
 *
 * @author MTomczyk
 */
public class Construct implements IConstruct
{
    /**
     * The number of position-related variables (the number of objectives = _P + 1).
     */
    protected final int _P;

    /**
     * The number of distance-related variables.
     */
    protected final int _D;

    /**
     * Parameterized constructor.
     * @param M the number of objectives
     * @param D the number of distance-related parameters
     */
    public Construct(int M, int D)
    {
        _P = M - 1;
        _D = D;
    }

    /**
     * Creates initial population randomly.
     * @param ea evolutionary algorithm
     * @return initial population
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public ArrayList<Specimen> createInitialPopulation(EA ea)
    {
        ArrayList<Specimen> S = new ArrayList<>(ea.getPopulationSize());
        int criteria = ea.getCriteria()._no;

        for (int s = 0; s < ea.getPopulationSize(); s++)
        {
            Specimen specimen = new Specimen(criteria);
            double[] x = new double[_P + _D];
            for (int i = 0; i < _P; i++) x[i] = ea.getR().nextDouble();
            for (int i = _P; i < _P + _D; i++) x[i] = ea.getR().nextDouble();
            specimen.setChromosome(new Chromosome(x));
            S.add(specimen);
        }
        return S;
    }

}
