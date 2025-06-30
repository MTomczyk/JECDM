package phase;

import ea.EA;
import exception.PhaseException;
import population.Specimen;
import population.SpecimensContainer;

import java.util.ArrayList;

/**
 * Default "Construct Initial Population" phase.
 * The default (implicit) assumptions are as follows:
 * - The method delegates the construction of specimens to {@link IConstruct}.
 * - The created specimen array will be stored in {@link population.SpecimensContainer#setPopulation(ArrayList)}
 * (accessible via {@link EA#getSpecimensContainer()}).
 * - The action method sets the {@link SpecimensContainer#setPopulationRequiresEvaluation(boolean)} and
 * {@link SpecimensContainer#setPopulationRequiresIDAssignment(boolean)} flags to true.
 *
 * @author MTomczyk
 */


public class ConstructInitialPopulation extends AbstractConstructInitialPopulationPhase implements IPhase
{
    /**
     * Object constructing the initial population.
     */
    protected final IConstruct _constructor;

    /**
     * Empty constructor for extending classes.
     *
     * @param name phase name
     */
    protected ConstructInitialPopulation(String name)
    {
        this(name, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param constructor responsible for generating the initial population
     */
    public ConstructInitialPopulation(IConstruct constructor)
    {
        this("Construct Initial Population", constructor);
    }

    /**
     * Parameterized constructor.
     *
     * @param name        name of the phase
     * @param constructor responsible for generating the initial population
     */
    public ConstructInitialPopulation(String name, IConstruct constructor)
    {
        super(name);
        _constructor = constructor;
    }

    /**
     * Phase main action. Creates the initial population.
     * The default (implicit) assumptions are as follows:
     * - The method delegates the construction of specimens to {@link IConstruct}.
     * - The created specimen array will be stored in {@link population.SpecimensContainer#setPopulation(ArrayList)}
     * (accessible via {@link EA#getSpecimensContainer()}).
     * - The action method sets the {@link SpecimensContainer#setPopulationRequiresEvaluation(boolean)} and
     * {@link SpecimensContainer#setPopulationRequiresIDAssignment(boolean)} flags to true.

     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        ArrayList<Specimen> specimen = _constructor.createInitialPopulation(ea);
        ea.setSpecimensContainer(new SpecimensContainer(specimen));
        ea.getSpecimensContainer().setPopulationRequiresEvaluation(true);
        ea.getSpecimensContainer().setPopulationRequiresIDAssignment(true);
    }
}
