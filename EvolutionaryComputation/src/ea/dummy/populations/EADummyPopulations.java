package ea.dummy.populations;

import alternative.Alternative;
import ea.EA;
import ea.IEA;
import phase.PhasesBundle;
import population.Specimen;
import population.SpecimenID;

import java.util.ArrayList;

/**
 * Dummy EA implementation that, instead of evolving populations of solutions, sets a current population
 * to some a priori provided specimen list.
 *
 * @author MTomczyk
 */

public class EADummyPopulations extends EA implements IEA
{
    /**
     * Dummy extension of the params container
     */
    private static class Params extends EA.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param populations population matrix
         */
        public Params(ArrayList<ArrayList<Specimen>> populations)
        {
            super("Dummy EA with fixed populations", -1, null, false, null);
            _populationSize = -1;
            _offspringSize = -1;
            _osManager = null;
            _computeExecutionTimes = false;
            _computePhasesExecutionTimes = false;

            PhasesBundle phasesBundle = PhasesBundle.getNulledInstance();
            phasesBundle._initStarts = null;
            phasesBundle._constructInitialPopulation = new SetInitialPopulation(populations.get(0));
            phasesBundle._assignSpecimenIDs = null;
            phasesBundle._evaluate = null;
            phasesBundle._sort = null;
            phasesBundle._prepareStep = null;
            phasesBundle._constructMatingPool = null;
            phasesBundle._selectParents = null;
            phasesBundle._reproduce = null;
            phasesBundle._merge = null;
            phasesBundle._remove = null;
            phasesBundle._finalizeStep = new DummyFinalizeStepSetPopulations(populations);
            phasesBundle._updateOS = null;
            _phases = PhasesBundle.getPhasesAssignmentsFromBundle(phasesBundle);
        }
    }

    /**
     * Fixed populations (generation -> population member).
     */
    protected ArrayList<ArrayList<Specimen>> _populations;

    /**
     * Parameterized constructor (private).
     *
     * @param populations populations matrix
     */
    private EADummyPopulations(ArrayList<ArrayList<Specimen>> populations)
    {
        super(new Params(populations));
        _populations = populations;
    }

    /**
     * Parameterized constructor.
     *
     * @param criteria no criteria considered
     * @param evals    evaluation vectors (1 dimension -> generations; 2 dimension -> population member; 3 dimension ->
     *                 specimen's evaluations).
     */
    public EADummyPopulations(int criteria, double[][][] evals)
    {
        this(getPopulations(criteria, evals));
    }

    /**
     * Auxiliary method for constructing the population matrix.
     *
     * @param criteria no criteria considered
     * @param evals    evaluation vectors (1 dimension -> generations; 2 dimension -> population member; 3 dimension ->
     *                 specimen's evaluations).
     * @return population matrix
     */
    private static ArrayList<ArrayList<Specimen>> getPopulations(int criteria, double[][][] evals)
    {
        ArrayList<ArrayList<Specimen>> populations = new ArrayList<>(evals.length);
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
            populations.add(S);
        }
        return populations;
    }


}
