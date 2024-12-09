package ea.dummy.populations;

import ea.EA;
import phase.ConstructInitialPopulation;
import phase.PhaseReport;
import population.Specimen;
import population.SpecimensContainer;

import java.util.ArrayList;


/**
 * Dummy initial population constructor (sets population to a fixed specimen set).
 *
 * @author MTomczyk
 */


public class SetInitialPopulation extends ConstructInitialPopulation
{
    /**
     * Initial population to be set.
     */
    private final ArrayList<Specimen> _initialPopulation;

    /**
     * Parameterized constructor
     *
     * @param initialPopulation initial (fixed) population
     */
    public SetInitialPopulation(ArrayList<Specimen> initialPopulation)
    {
        super("Dummy phase (set initial population)");
        _initialPopulation = initialPopulation;
    }


    /**
     * Phase main action. Creates initial population.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     */
    @Override
    public void action(EA ea, PhaseReport report)
    {
        ea.setSpecimensContainer(new SpecimensContainer(_initialPopulation));
    }
}
