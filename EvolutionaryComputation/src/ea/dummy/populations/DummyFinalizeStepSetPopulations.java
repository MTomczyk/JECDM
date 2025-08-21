package ea.dummy.populations;

import ea.AbstractPhasesEA;
import exception.PhaseException;
import phase.FinalizeStep;
import phase.IPhase;
import phase.PhaseReport;
import population.Specimen;

import java.util.ArrayList;

/**
 * Default "finalize step" phase. It sets the current population with the one provided as an input.
 *
 * @author MTomczyk
 */


public class DummyFinalizeStepSetPopulations extends FinalizeStep implements IPhase
{
    /**
     * Fixed populations.
     */
    private final ArrayList<ArrayList<Specimen>> _populations;

    /**
     * Default constructor.
     *
     * @param populations fixed populations
     */
    public DummyFinalizeStepSetPopulations(ArrayList<ArrayList<Specimen>> populations)
    {
        super();
        _populations = populations;
    }

    /**
     * Phase main action. Replaces EA's current population with one of the fixed ones (as imposed by the generation number).
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(AbstractPhasesEA ea, PhaseReport report) throws PhaseException
    {
        super.action(ea, report);
        ea.getSpecimensContainer().setPopulation(_populations.get(ea.getCurrentGeneration()));
    }
}
