package phase;

import ea.EA;
import exception.PhaseException;
import population.Specimen;

import java.util.ArrayList;

/**
 * Default "remove" phase (preserves the first "population size" of solutions in the current population; new population array
 * is constructed in this process).
 *
 * @author MTomczyk
 */

public class Remove extends AbstractRemovePhase implements IPhase
{
    /**
     * Default constructor.
     */
    public Remove()
    {
        this("Remove");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public Remove(String name)
    {
        super(name);
    }

    /**
     * Phase main action (preserves ``population size'' first specimens stored in the population array; new population array
     * is constructed in this process).
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        ArrayList<Specimen> survivalists = new ArrayList<>(ea.getPopulationSize());
        for (int i = 0; i < ea.getPopulationSize(); i++)
            survivalists.add(ea.getSpecimensContainer().getPopulation().get(i));
        ea.getSpecimensContainer().setPopulation(survivalists);
    }
}
