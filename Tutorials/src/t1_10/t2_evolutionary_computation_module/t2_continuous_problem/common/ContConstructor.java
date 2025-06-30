package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common;

import ea.EA;
import exception.PhaseException;
import phase.IConstruct;
import population.Chromosome;
import population.Specimen;

import java.util.ArrayList;

/**
 * This constructor creates random specimens whose decision vectors are set as random two-element double vectors
 * (numbers in [0, 1])
 *
 * @author MTomczyk
 */


public class ContConstructor implements IConstruct
{
    /**
     * Creates initial population (array of specimens) of size ``population size''.
     * The population consists of random solutions  to the 2-variable continuous problem.
     *
     * @param ea evolutionary algorithm
     * @return specimen array
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public ArrayList<Specimen> createInitialPopulation(EA ea) throws PhaseException
    {
        int ps = ea.getPopulationSize(); // number of specimens = population size
        ArrayList<Specimen> specimens = new ArrayList<>(ps); // instantiate the array
        for (int i = 0; i < ps; i++) // iterate the required number of times
        {
            Specimen s = new Specimen(1); // 1 criterion: value function
            Chromosome c = new Chromosome(new double[]{ea.getR().nextDouble(), ea.getR().nextDouble()}); // random
            // x1 and x2 coordinates in [0, 1] bounds.
            s.setChromosome(c); // set specimen's chromosome
            specimens.add(s); // store the specimen
        }
        return specimens;
    }
}
