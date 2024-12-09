package emo.utils.front;

import alternative.AbstractAlternatives;
import population.Specimen;
import relation.dominance.Dominance;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class implementing the fast non-dominated sorting algorithm.
 *
 * @author MTomczyk
 */

public class FNDSorting
{
    /**
     * Dominance relation used for comparing alternatives.
     */
    private final Dominance _relation;

    /**
     * Parameterized constructor.
     *
     * @param relation dominance relation used for comparing alternatives.
     */
    public FNDSorting(Dominance relation)
    {
        _relation = relation;
    }

    /**
     * Assign ALL alternatives into non-dominated fronts. The output is a list of lists (represents different levels) of
     * integers (ids of alternatives in the input array).
     *
     * @param alternatives wrapper for the array of alternatives
     * @return list of lists (represents different levels) of integers (ids of alternatives in the input array).
     */
    public LinkedList<LinkedList<Integer>> getFrontAssignments(AbstractAlternatives<?> alternatives)
    {
        return getFrontAssignments(alternatives, alternatives.size());
    }

    /**
     * Assign alternatives into non-dominated fronts. The output is a list of lists (represents different levels) of
     * integers (ids of alternatives in the input array).
     *
     * @param alternatives wrapper for the array of alternatives
     * @param stopAfter    the assignment process is stopped after the number of already assigned alternatives in
     *                     constructed fronts exceeds this value (breaks can be triggered only after completion of a whole
     *                     front, not during this process). It can be set, e.g., to "offspring size" in NSGA-II algorithm
     *                     to construct a sufficient number of fronts.
     * @return list of lists (represents different levels) of integers (ids of alternatives in the input array).
     */
    public LinkedList<LinkedList<Integer>> getFrontAssignments(AbstractAlternatives<?> alternatives, int stopAfter)
    {
        if (alternatives.isEmpty()) return new LinkedList<>();
        if (stopAfter == 0) return new LinkedList<>();

        LinkedList<LinkedList<Integer>> fronts = new LinkedList<>();
        ArrayList<LinkedList<Integer>> S = new ArrayList<>(alternatives.size()); // WHO IS DOMINATED BY THE SOLUTION

        ArrayList<Integer> n = new ArrayList<>(alternatives.size()); // HOW MANY TIMES AN ALT IS DOMINATED

        for (int i = 0; i < alternatives.size(); i++)
        {
            n.add(0);
            LinkedList<Integer> s = new LinkedList<>();
            S.add(s);
        }

        LinkedList<Integer> f = new LinkedList<>();

        int assigned = 0;

        // Prepare counter and sets
        for (int i = 0; i < alternatives.size(); i++)
        {
            for (int j = 0; j < alternatives.size(); j++)
            {
                if (i == j) continue;

                boolean dA = _relation.isHolding(alternatives.get(i), alternatives.get(j));
                boolean dB = _relation.isHolding(alternatives.get(j), alternatives.get(i));

                if (dA) S.get(i).add(j);
                else if (dB) n.set(i, n.get(i) + 1);
            }

            if (n.get(i) == 0) f.add(i);
        }

        fronts.add(f);
        assigned += f.size();

        while ((assigned <= alternatives.size()) && (assigned < stopAfter))
        {
            LinkedList<Integer> l = fronts.getLast();

            f = new LinkedList<>();
            for (Integer p : l)
            {
                for (Integer q : S.get(p)) // WHO IS DOMINATED BY S
                {
                    int val = n.get(q) - 1;
                    n.set(q, val);
                    if (val == 0) f.add(q);
                }
            }

            if (!f.isEmpty())
            {
                fronts.add(f);
                assigned += f.size();
            }
            else break;
        }
        return fronts;
    }

    /**
     * Auxiliary method for constructing an array of alternatives' front levels based on the result of the
     * fast non-dominated sorting. If the array's element is null, the alternative has not been assigned.
     *
     * @param alternatives wrapper for array of alternatives
     * @param fronts       result of non-dominated sorting
     * @return array of alternatives' front levels
     */
    public static Integer[] getFrontLevels(AbstractAlternatives<?> alternatives, LinkedList<LinkedList<Integer>> fronts)
    {
        Integer[] lv = new Integer[alternatives.size()];

        int l = 0;
        for (LinkedList<Integer> front : fronts)
        {
            for (Integer i : front) lv[i] = l;
            l++;
        }

        return lv;
    }

    /**
     * Encapsulates the last non-dominated front whose members (some of them) can be passed to the next generation (ambiguous)
     */
    public static class AmbiguousFront
    {
        /**
         * The number of fronts that are already passed (except of the ambiguous front).
         */
        public final int _passedFronts;

        /**
         * Members that were passed to the next population without hesitation (no ambiguity).
         */
        public final int _passedMembers;

        /**
         * Ambiguous front (integers pointing to various elements of the current population).
         */
        public final LinkedList<Integer> _front;

        /**
         * Parameterized constructor.
         *
         * @param passedFronts  no. fronts already passed to the next generation
         * @param passedMembers members that were passed to the next population without hesitation (no ambiguity)
         * @param front         ambiguous front (integers pointing to various elements of the current population).
         */
        public AmbiguousFront(int passedFronts, int passedMembers, LinkedList<Integer> front)
        {
            _passedFronts = passedFronts;
            _passedMembers = passedMembers;
            _front = front;
        }
    }

    /**
     * The method passes all the members of the first non-dominated fronts for which there is no ambiguity that they
     * should be promoted. If the members in a front being processed, when added to the already passed ones, would exceed
     * the population size, the front is called ambiguous and the data on that front is returned. Note that the
     * method additionally assigns the passed specimens their auxiliary scores that are assumed to be equal to their
     * front numbers (0 = the first non-dominated front, 1 = the second, etc.)
     *
     * @param newPopulation     array representing a new population (to be filled by the method with promoted specimens)
     * @param currentPopulation current population (provides specimens)
     * @param fronts            non dominated fronts obtained via {@link FNDSorting#getFrontAssignments(AbstractAlternatives, int)}
     * @param populationSize    population size determines the limit for the number of specimens that can be passed to the next generation
     * @return data on the ambitious front, if only complete fronts are needed to fill the required number of specimen slots, the method returns null
     */
    public static AmbiguousFront fillNewPopulationWithCertainFronts(ArrayList<Specimen> newPopulation,
                                                                    ArrayList<Specimen> currentPopulation,
                                                                    LinkedList<LinkedList<Integer>> fronts,
                                                                    int populationSize)
    {
        if (populationSize == 0) return null;
        int passed = 0;
        int frontLevel = 0;

        for (LinkedList<Integer> front : fronts)
        {
            if (passed + front.size() <= populationSize)
            {
                for (Integer idx : front)
                {
                    Specimen specimen = currentPopulation.get(idx);
                    specimen.getAlternative().setAuxScore(frontLevel);
                    newPopulation.add(specimen);
                }
                passed += front.size();
                frontLevel++;
                if (passed == populationSize) return null; // filled completely
            }
            else return new AmbiguousFront(frontLevel, passed, front);
        }

        return null;
    }

}
