package ea.dummy.populations;

import alternative.Alternative;
import ea.EA;
import ea.EATimestamp;
import phase.IPhase;
import phase.PhasesIDs;
import population.Specimen;
import population.SpecimenID;

import java.util.ArrayList;

/**
 * Dummy EA implementation that, instead of evolving populations of solutions, sets a current population
 * to some a priori provided specimen list.
 *
 * @author MTomczyk
 */


public class EADummyPopulations extends EA
{
    /**
     * Fixed populations (generation -> population member).
     */
    protected ArrayList<ArrayList<Specimen>> _populations;

    /**
     * Parameterized constructor.
     *
     * @param criteria no criteria considered
     * @param evals    evaluation vectors (1 dimension -> generations; 2 dimension -> population member; 3 dimension -> specimen's evaluations).
     */
    public EADummyPopulations(int criteria, double[][][] evals)
    {
        _name = "Dummy EA with fixed populations";
        _criteria = null;
        _id = -1;
        _populationSize = -1;
        _offspringSize = -1;
        _osManager = null;
        _computeExecutionTimes = false;
        _computePhasesExecutionTimes = false;
        _executionTime = 0.0d;
        _phasesExecutionTimes = null;
        _currentTimestamp = new EATimestamp(0, 0);

        _populations = new ArrayList<>(evals.length);
        for (int g = 0; g < evals.length; g++)
        {
            ArrayList<Specimen> S = new ArrayList<>(evals[g].length);
            for (int n = 0; n < evals[g].length; n++)
            {
                Alternative A = new Alternative("A_" + g + "_" + n, evals[g][n]);
                Specimen s = new Specimen(criteria, new SpecimenID(0, g, 0, n));
                s.setAlternative(A);
                S.add(s);
            }
            _populations.add(S);
        }

        _phases = new IPhase[PhasesIDs._phaseNames.length];
        _phases[PhasesIDs.PHASE_INIT_STARTS] = null;
        _phases[PhasesIDs.PHASE_CONSTRUCT_INITIAL_POPULATION] = new SetInitialPopulation(_populations.get(0));
        _phases[PhasesIDs.PHASE_ASSIGN_SPECIMENS_IDS] = null;
        _phases[PhasesIDs.PHASE_EVALUATE] = null;
        _phases[PhasesIDs.PHASE_SORT] = null;
        _phases[PhasesIDs.PHASE_PREPARE_STEP] = null;
        _phases[PhasesIDs.PHASE_CONSTRUCT_MATING_POOL] = null;
        _phases[PhasesIDs.PHASE_SELECT_PARENTS] = null;
        _phases[PhasesIDs.PHASE_REPRODUCE] = null;
        _phases[PhasesIDs.PHASE_MERGE] = null;
        _phases[PhasesIDs.PHASE_REMOVE] = null;
        _phases[PhasesIDs.PHASE_FINALIZE_STEP] = new DummyFinalizeStepSetPopulations(_populations);
        _phases[PhasesIDs.PHASE_UPDATE_OS] = null;
    }


}
