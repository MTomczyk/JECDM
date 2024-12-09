package io.cross.excel.utils;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Supportive class that provides various statistics-related functionalities (mainly for {@link io.cross.excel.AbstractFinalStatistics}).
 *
 * @author MTomczyk
 */
public class RankerUtils
{
    /**
     * Auxiliary class used by {@link RankerUtils#calculateAttainedRanks(double[][], boolean, double)}.
     */
    protected static class RankAux
    {
        /**
         * Score attained in a given trial.
         */
        protected final double _score;

        /**
         * Index pointing to the scenario.
         */
        protected final int _originalIndex;

        /**
         * Parameterized constructor.
         *
         * @param score         score attained in a given trial
         * @param originalIndex index pointing to the scenario
         */
        public RankAux(double score, int originalIndex)
        {
            _score = score;
            _originalIndex = originalIndex;
        }
    }

    /**
     * Auxiliary method for calculating attained ranks.
     *
     * @param trialResults per-trial results
     * @param isLessBetter flag indicating the preference direction
     * @param tolerance    tolerance level (delta used when comparing doubles, the difference smaller/equal than the tolerance implies equality)

     * @return attained ranks matrix
     */
    public static double[][] calculateAttainedRanks(double[][] trialResults,
                                                    boolean isLessBetter,
                                                    double tolerance)
    {
        int trials = 0;
        int ranks = 0;
        for (double[] d : trialResults)
            if (d != null)
            {
                ranks++;
                if (trials == 0) trials = d.length;
            }
        int[] map1 = new int[trialResults.length];
        int[] map2 = new int[ranks];
        int idx = 0;
        for (int t = 0; t < trialResults.length; t++)
            if (trialResults[t] != null)
            {
                map1[t] = idx;
                map2[idx] = t;
                idx++;
            }

        // result matrix [scenario x possible ranks]
        double[][] rMatrix = new double[trialResults.length][ranks];

        // fill the data
        for (int t = 0; t < trials; t++)
        {
            ArrayList<RankAux> aux = new ArrayList<>(trialResults.length);
            for (int i = 0; i < trialResults.length; i++)
                if (trialResults[i] != null) aux.add(new RankAux(trialResults[i][t], i));
            if (!isLessBetter) aux.sort((o1, o2) -> -Double.compare(o1._score, o2._score));
            else aux.sort(Comparator.comparingDouble(o -> o._score));

            // retrieve ranks (account for equality)
            for (int index = 0; index < aux.size(); index++)
            {
                double cV = aux.get(index)._score;
                int nIndex = index;
                if (index < aux.size() - 1)
                {
                    while (Double.compare(Math.abs(aux.get(nIndex + 1)._score - cV), tolerance) <= 0)
                    {
                        nIndex++;
                        if (nIndex == aux.size() - 1) break;
                    }
                }

                int div = nIndex - index + 1;

                for (int j = index; j < nIndex + 1; j++)
                    for (int k = index; k < nIndex + 1; k++)
                        rMatrix[aux.get(k)._originalIndex][j] += 1.0d / div;
                index = nIndex;
            }
        }

        // replace with null not used
        //for (int i = 0; i < trialResults.length; i++)
        //     if (trialResults[i] == null) rMatrix[i] = null;

        return rMatrix;
    }


    /**
     * The elements of two vectors are compared pairwise. The method counts the number of pairwise wins attained by the
     * first vector. The input vectors should be of the same length.
     *
     * @param v1           the first vector
     * @param v2           the second vector
     * @param isLessBetter if true, smaller values are preferred; false otherwise
     * @param tolerance    tolerance level (delta used when comparing doubles, the difference smaller/equal than the tolerance implies equality)
     * @return the number of pairwise wins for the first vector.
     */
    public static int countWins(double[] v1, double[] v2, boolean isLessBetter, double tolerance)
    {
        int wins = 0;
        for (int i = 0; i < v1.length; i++)
        {
            if (Double.compare(Math.abs(v1[i] - v2[i]), tolerance) <= 0) continue;
            if ((isLessBetter) && (Double.compare(v1[i], v2[i]) < 0)) wins++;
            if (!(isLessBetter) && (Double.compare(v1[i], v2[i]) > 0)) wins++;
        }
        return wins;
    }

    /**
     * The elements of two vectors are compared pairwise. The method counts the number of pairwise ties. The input vectors should be of the same length.
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @param tolerance    tolerance level (delta used when comparing doubles, the difference smaller/equal than the tolerance implies equality)

     * @return the number of pairwise wins for the first vector.
     */
    public static int countTies(double[] v1, double[] v2, double tolerance)
    {
        int ties = 0;
        for (int i = 0; i < v1.length; i++)
        {
            if (Double.compare(Math.abs(v1[i] - v2[i]), tolerance) <= 0) ties++;
        }
        return ties;
    }


}
