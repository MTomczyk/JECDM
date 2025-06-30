package phase;

import ea.EA;
import exception.PhaseException;
import population.Specimen;
import population.SpecimenID;
import population.SpecimensContainer;

import java.util.ArrayList;

/**
 * Default "Assign Specimens IDs" phase. The ID (for a specimen) is constructed using its in-array index,
 * the EA id ({@link EA#getID()}), current generation ({@link EA#getCurrentGeneration()}), and current steady-state
 * repeat ({@link EA#getCurrentSteadyStateRepeat()}). The specimens stored in {@link SpecimensContainer#getPopulation()}
 * and/or {@link SpecimensContainer#getOffspring()} are assigned IDs (depends on the {@link SpecimensContainer#isPopulationRequiringIDAssignment()}
 * and {@link SpecimensContainer#isOffspringRequiringIDAssignment()} flags).
 *
 * @author MTomczyk
 */
public class AssignSpecimensIDs extends AbstractAssignSpecimenIDs implements IPhase
{
    /**
     * Default constructor.
     */
    public AssignSpecimensIDs()
    {
        this("Assign Specimens IDs");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AssignSpecimensIDs(String name)
    {
        super(name);
    }

    /**
     * Phase's main action. Assigns IDs to specimens. The ID (for a specimen) is constructed using its in-array index,
     * the EA id ({@link EA#getID()}), current generation ({@link EA#getCurrentGeneration()}), and current steady-state
     * repeat ({@link EA#getCurrentSteadyStateRepeat()}). The specimens stored in {@link SpecimensContainer#getPopulation()}
     * and/or {@link SpecimensContainer#getOffspring()} are assigned IDs (depends on the {@link SpecimensContainer#isPopulationRequiringIDAssignment()}
     * and {@link SpecimensContainer#isOffspringRequiringIDAssignment()} flags).
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        if (ea.getSpecimensContainer().isPopulationRequiringIDAssignment())
            assignIDs(ea, ea.getSpecimensContainer().getPopulation());
        if (ea.getSpecimensContainer().isOffspringRequiringIDAssignment())
            assignIDs(ea, ea.getSpecimensContainer().getOffspring());
    }

    /**
     * Auxiliary method. Instantiates specimens IDs.
     *
     * @param ea        evolutionary algorithm
     * @param specimens array of specimens to be assigned ID
     */
    private void assignIDs(EA ea, ArrayList<Specimen> specimens)
    {
        for (int i = 0; i < specimens.size(); i++)
        {
            specimens.get(i).setID(
                    new SpecimenID(ea.getID(),
                            ea.getCurrentGeneration(),
                            ea.getCurrentSteadyStateRepeat(),
                            i));
        }
    }
}
