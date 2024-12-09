package emo.interactive.ktscone.cdemo;

import java.util.LinkedList;

/**
 * Provides auxiliary functions for the CDEMO algorithm.
 *
 * @author MTomczyk
 */
public class Utils
{
    /**
     * Auxiliary class paring an index and evaluation.
     * Associated with {@link Utils#getConeFronts(double[])}.
     */
    static class IndexEval
    {
        /**
         * Index.
         */
        public final int _index;

        /**
         * Evaluation
         */
        public final double _eval;

        /**
         * Parameterized constructor.
         *
         * @param index index
         * @param eval  evaluation
         */
        public IndexEval(int index, double eval)
        {
            _index = index;
            _eval = eval;
        }
    }

    /**
     * Auxiliary method for assigning solutions into fronts (cones), based on their input evaluations  (0 = all cones, 1 = one
     * cone is infeasible, etc.).
     *
     * @param evaluations input evaluations; the i-th element is supposed to be associated with the i-th population member
     * @return indices partitioned into fronts; the first list element consists of the best performers, etc
     */
    public static LinkedList<LinkedList<Integer>> getConeFronts(double[] evaluations)
    {
        LinkedList<LinkedList<Integer>> fronts = new LinkedList<>();

        LinkedList<IndexEval> evals = new LinkedList<>();
        for (int i = 0; i < evaluations.length; i++) evals.add(new IndexEval(i, evaluations[i]));

        while (!evals.isEmpty())
        {
            LinkedList<Integer> front = new LinkedList<>();
            LinkedList<IndexEval> passedEvals = new LinkedList<>();
            double min = Double.POSITIVE_INFINITY;
            for (IndexEval ie : evals) if (Double.compare(ie._eval, min) < 0) min = ie._eval;

            int iM = (int) (min + 0.1d);
            for (IndexEval ie : evals)
            {
                if ((int) (ie._eval + 0.1d) == iM) front.add(ie._index);
                else passedEvals.add(ie);
            }

            fronts.add(front);
            evals = passedEvals;
        }

        return fronts;
    }
}
