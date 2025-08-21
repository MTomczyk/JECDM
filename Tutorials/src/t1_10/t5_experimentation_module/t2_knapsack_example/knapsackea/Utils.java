package t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea;

import ea.EA;
import phase.PhasesBundle;
import random.IRandom;
import selection.Tournament;

/**
 * Provides some auxiliary functionalities
 *
 * @author MTomczyk
 */

public class Utils
{
    /**
     * Auxiliary method that creates an evolutionary algorithm for solving the knapsack problem.
     *
     * @param populationSize population size (equals the offspring size)
     * @param capacity       knapsack capacity
     * @param data           knapsack data
     * @param repairMode     if true, infeasible solutions will be immediately repaired by removing items from the
     *                       knapsack
     *                       in the increasing order of their value/size ratios until the feasibility is restored;
     *                       if false, the algorithm works in "penalty mode"
     * @param R              random number generator
     * @return instance of {@link EA}.
     */
    @SuppressWarnings("DuplicatedCode")
    public static KnapsackEA getKnapsackEA(int populationSize, Data data, double capacity, boolean repairMode, IRandom R)
    {
        KnapsackEABundle.Params pB = new KnapsackEABundle.Params(data, capacity, repairMode, R);
        Tournament.Params pTournament = new Tournament.Params(2);
        pTournament._preferenceDirection = true;
        pB._select = new Tournament(pTournament);
        KnapsackEABundle bundle = new KnapsackEABundle(pB);
        EA.Params pEA = new EA.Params(bundle._name, pB._criteria);
        pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(bundle._phasesBundle);
        pEA._id = 0;
        pEA._R = R;
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        return new KnapsackEA(pEA);
    }
}
