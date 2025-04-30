package utils;

import problem.Problem;
import problem.moo.ReferencePointsFactory;
import random.IRandom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Auxiliary container-like class that generates and stored reference Pareto optimal points for test problems.
 *
 * @author MTomczyk
 */
public class ReferencePointsOnPareto
{
    /**
     * Per problem and objectives (dimensionality) data.
     */
    public static class ObjectivesData
    {
        /**
         * The number of objectives.
         */
        public final int _M;

        /**
         * Reference points.
         */
        public final double[][] _rps;

        /**
         * Parameterized constructor.
         *
         * @param M   the number of objectives
         * @param rps reference points
         */
        public ObjectivesData(int M, double[][] rps)
        {
            _M = M;
            _rps = rps;
        }
    }

    /**
     * Per-problem data.
     */
    public static class ProblemData
    {
        /**
         * Encapsulated data.
         */
        private final HashMap<Integer, ObjectivesData> _data;

        /**
         * Patter for representing the problem(s).
         */
        private final Pattern _pattern;

        /**
         * Parameterized constructor.
         *
         * @param pattern patter for representing the problem(s) (case-insensitive)
         * @param data    data
         */
        public ProblemData(String pattern, HashMap<Integer, ObjectivesData> data)
        {
            this(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE), data);
        }

        /**
         * Parameterized constructor.
         *
         * @param pattern patter for representing the problem(s)
         * @param data    data
         */
        public ProblemData(Pattern pattern, HashMap<Integer, ObjectivesData> data)
        {
            _data = data;
            _pattern = pattern;
        }

        /**
         * Returns reference points associated with a specified number of objectives.
         *
         * @param M the number of objectives
         * @return reference points (null, if the data cannot be retrieved)
         */
        public double[][] getReferencePoints(int M)
        {
            if (_data == null) return null;
            if (_data.isEmpty()) return null;
            if (!_data.containsKey(M)) return null;
            return _data.get(M)._rps;
        }
    }

    /**
     * Problem-related data.
     */
    private final ProblemData[] _problemData;

    /**
     * Parameterized constructor.
     *
     * @param problemData per problem(s) data.
     */
    private ReferencePointsOnPareto(ProblemData[] problemData)
    {
        _problemData = problemData;
    }

    /**
     * Returns a default class instance that generates random reference points for DTLZ and WFG test problems
     * (their Pareto fronts).
     *
     * @param n  a fixed number of random points to generate for each problem
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return class instance (null if the input data is invalid)
     */
    public static ReferencePointsOnPareto getDefault(int n, int lm, int um, IRandom R)
    {
        if (um <= lm) return null;
        int[] nn = new int[um - lm];
        Arrays.fill(nn, n);
        return getDefault(nn, lm, um, R);
    }

    /**
     * Returns a default class instance that generates random reference points for DTLZ and WFG test problems
     * (their Pareto fronts).
     *
     * @param n  a fixed number of random points to generate for each problem (one entry for each subsequent m-configuration, e.g., if lm = 2 and um = 6, n should be a 4-element vector)
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return class instance (null if the input data is invalid)
     */
    public static ReferencePointsOnPareto getDefault(int[] n, int lm, int um, IRandom R)
    {
        if (n == null) return null;
        if (n.length == 0) return null;
        for (Integer i : n) if (i < 1) return null;
        if (um <= lm) return null;

        return new ReferencePointsOnPareto(new ProblemData[]{
                getForDTLZ1(n, lm, um, R),
                getForDTLZ2_4(n, lm, um, R),
                getForDTLZ5_6(n, lm, um, R),
                getForDTLZ7(n, lm, um, R),
                getForWFG1(n, lm, um, R),
                getForWFG2(n, lm, um, R),
                getForWFG3(n, lm, um, R),
                getForWFG4_9(n, lm, um, R)
        });
    }

    /**
     * The main method for retrieving reference points.
     *
     * @param problem problem ID (e.g., "DTLZ2")
     * @param M       the number of objectives considered
     * @return reference points (the object is not cloned, i.e., reference is returned; returns null if no proper data can be found)
     */
    public double[][] getReferencePointsOnPF(String problem, int M)
    {
        if (_problemData == null) return null;
        for (ProblemData problemData : _problemData)
        {
            Matcher matcher = problemData._pattern.matcher(problem);
            boolean match = matcher.find();
            if (match) return problemData.getReferencePoints(M);
        }
        return null;
    }

    /**
     * Returns DTLZ1-related reference points.
     *
     * @param n  a fixed number of random points to generate (one entry for each subsequent m-configuration, e.g., if lm = 2 and um = 6, n should be a 4-element vector)
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return DTLZ1-related data
     */
    private static ProblemData getForDTLZ1(int[] n, int lm, int um, IRandom R)
    {
        HashMap<Integer, ObjectivesData> data = new HashMap<>(um - lm);
        for (int m = lm; m < um; m++)
            data.put(m, new ObjectivesData(m, ReferencePointsFactory.getRandomReferencePoints(Problem.DTLZ1, n[m - lm], m, R)));
        return new ProblemData("DTLZ1", data);
    }

    /**
     * Returns DTLZ2-4-related reference points.
     *
     * @param n  a fixed number of random points to generate (one entry for each subsequent m-configuration, e.g., if lm = 2 and um = 6, n should be a 4-element vector)
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return DTLZ2-4-related data
     */
    private static ProblemData getForDTLZ2_4(int[] n, int lm, int um, IRandom R)
    {
        HashMap<Integer, ObjectivesData> data = new HashMap<>(um - lm);
        for (int m = lm; m < um; m++)
            data.put(m, new ObjectivesData(m, ReferencePointsFactory.getUniformRandomRPsOnConcaveSphere(n[m - lm], m, R)));
        return new ProblemData("DTLZ[2-4]", data);
    }

    /**
     * Returns DTLZ5-6-related reference points.
     *
     * @param n  a fixed number of random points to generate (one entry for each subsequent m-configuration, e.g., if lm = 2 and um = 6, n should be a 4-element vector)
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return DTLZ5-6-related data
     */
    private static ProblemData getForDTLZ5_6(int[] n, int lm, int um, IRandom R)
    {
        HashMap<Integer, ObjectivesData> data = new HashMap<>(um - lm);
        for (int m = lm; m < um; m++)
            data.put(m, new ObjectivesData(m, ReferencePointsFactory.getRandomReferencePoints(Problem.DTLZ5, n[m - lm], m, R)));
        return new ProblemData("DTLZ[5-6]", data);
    }

    /**
     * Returns DTLZ7-related reference points.
     *
     * @param n  a fixed number of random points to generate (one entry for each subsequent m-configuration, e.g., if lm = 2 and um = 6, n should be a 4-element vector)
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return DTLZ7-related data
     */
    private static ProblemData getForDTLZ7(int[] n, int lm, int um, IRandom R)
    {
        HashMap<Integer, ObjectivesData> data = new HashMap<>(um - lm);
        for (int m = lm; m < um; m++)
            data.put(m, new ObjectivesData(m, ReferencePointsFactory.getRandomReferencePoints(Problem.DTLZ7, n[m - lm], m, R)));
        return new ProblemData("DTLZ7", data);
    }


    /**
     * Returns WFG1-related reference points.
     *
     * @param n  a fixed number of random points to generate (one entry for each subsequent m-configuration, e.g., if lm = 2 and um = 6, n should be a 4-element vector)
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return WFG1-related data
     */
    private static ProblemData getForWFG1(int[] n, int lm, int um, IRandom R)
    {
        HashMap<Integer, ObjectivesData> data = new HashMap<>(um - lm);
        for (int m = lm; m < um; m++)
            data.put(m, new ObjectivesData(m, ReferencePointsFactory.getRandomReferencePoints(Problem.WFG1, n[m - lm], m, R)));
        return new ProblemData("WFG1|WFG1ALPHA02|WFG1ALPHA025|WFG1ALPHA05", data);
    }

    /**
     * Returns WFG2-related reference points.
     *
     * @param n  a fixed number of random points to generate (one entry for each subsequent m-configuration, e.g., if lm = 2 and um = 6, n should be a 4-element vector)
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return WFG2-related data
     */
    private static ProblemData getForWFG2(int[] n, int lm, int um, IRandom R)
    {
        HashMap<Integer, ObjectivesData> data = new HashMap<>(um - lm);
        for (int m = lm; m < um; m++)
            data.put(m, new ObjectivesData(m, ReferencePointsFactory.getRandomReferencePoints(Problem.WFG2, n[m - lm], m, R)));
        return new ProblemData("WFG2", data);
    }

    /**
     * Returns WFG3-related reference points.
     *
     * @param n  a fixed number of random points to generate (one entry for each subsequent m-configuration, e.g., if lm = 2 and um = 6, n should be a 4-element vector)
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return WFG3-related data
     */
    private static ProblemData getForWFG3(int[] n, int lm, int um, IRandom R)
    {
        HashMap<Integer, ObjectivesData> data = new HashMap<>(um - lm);
        for (int m = lm; m < um; m++)
            data.put(m, new ObjectivesData(m, ReferencePointsFactory.getRandomReferencePoints(Problem.WFG3, n[m - lm], m, R)));
        return new ProblemData("WFG3", data);
    }

    /**
     * Returns WFG4-9-related reference points.
     *
     * @param n  a fixed number of random points to generate (one entry for each subsequent m-configuration, e.g., if lm = 2 and um = 6, n should be a 4-element vector)
     * @param lm lower limit for the number of objectives considered (inclusive)
     * @param um lower limit for the number of objectives considered (exclusive)
     * @param R  random number generator
     * @return DTLZ4-9-related data
     */
    private static ProblemData getForWFG4_9(int[] n, int lm, int um, IRandom R)
    {
        HashMap<Integer, ObjectivesData> data = new HashMap<>(um - lm);
        for (int m = lm; m < um; m++)
            data.put(m, new ObjectivesData(m, ReferencePointsFactory.getRandomReferencePoints(Problem.WFG4, n[m - lm], m, R)));
        return new ProblemData("WFG[4-9]", data);
    }
}
