package t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.common;

import ea.EA;
import phase.PhasesBundle;
import population.Specimen;
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
     * Auxiliary method for printing specimen data.
     *
     * @param s                    specimen
     * @param printDecisionVectors if true, the decision vectors will be printed
     */
    @SuppressWarnings("DuplicatedCode")
    public static void printSpecimenData(Specimen s, boolean printDecisionVectors)
    {
        String s1 = "Specimen = " + s.getID().toString() + ": [value = " + s.getEvaluations()[0] + "; size = " +
                s.getEvaluations()[1] + "; aux = " + s.getAlternative().getAuxScore() +
                "] ";
        String s2 = "";
        if (printDecisionVectors)
        {
            StringBuilder sb = new StringBuilder();
            boolean[] bv = s.getBooleanDecisionVector();
            for (boolean b : bv)
                if (b) sb.append("1");
                else sb.append("0");
            s2 = sb.toString();
        }
        System.out.println(s1 + s2);
    }

    /**
     * Auxiliary method that creates an evolutionary algorithm for solving the knapsack problem.
     *
     * @param populationSize   population size (equals the offspring size)
     * @param R                random number generator
     * @param knapsackCapacity knapsack capacity
     * @param repairMode       if true, infeasible solutions will be immediately repaired by removing items from the knapsack
     *                         in the increasing order of their value/size ratios until the feasibility is restored;
     *                         if false, the algorithm works in "penalty mode"
     * @return instance of {@link EA}.
     */
    @SuppressWarnings("DuplicatedCode")
    public static EA getKnapsackEA(int populationSize, IRandom R, int knapsackCapacity, boolean repairMode)
    {
        // Create the bundle params container:
        KnapsackEABundle.Params pB = new KnapsackEABundle.Params(knapsackCapacity, repairMode);

        // Use a tournament selection (the winner is selected based on the aux value; greater values are preferred)
        Tournament.Params pTournament = new Tournament.Params(2, populationSize);
        pTournament._preferenceDirection = true; //  greater values are preferred
        pB._select = new Tournament(pTournament);

        // Create the parameterized bundle:
        KnapsackEABundle bundle = new KnapsackEABundle(pB);

        // Create the EA params container:
        EA.Params pEA = new EA.Params(bundle._name, pB._criteria);
        // Transfer all phases from the bundle to EA:
        PhasesBundle.copyPhasesFromBundleToEA(pEA, bundle._phasesBundle);
        // Set EA id:
        pEA._id = 0;
        // Set the random number generator (accessible from the ea object)"
        pEA._R = R;
        // Set the population size (and offspring size):
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize; // (equals the population size)
        return new EA(pEA);
    }
}
