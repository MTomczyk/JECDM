package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Auxiliary class for encapsulating data on generations limit imposed on evolutionary algorithms
 * when executing experiments. The per-problem data on generation limits is contained in instances of {@link ProblemLimit}.
 * The central method of this call is {@link GenerationsLimits#getLimit(String, int)}. When called, the input ID is
 * compared with supplied patterns for regular expression to derive suitable data on generation limits.
 *
 * @author MTomczyk
 */
public class GenerationsLimits
{
    /**
     * Simple interface for specifying formulas for generations limits.
     */
    public interface ILimit
    {
        /**
         * The main method.
         *
         * @param problem problem ID (string representation)
         * @param M       the number of objectives involved
         * @return generations limit
         */
        int getLimit(String problem, int M);
    }

    /**
     * Implementation of {@link ILimit} that defines the limit for the number of generations as base + (M - 1) *
     * perObjective, where the base is a baseline number of generations, M denotes the number of objectives considered,
     * and mul determines additional generations per objective.
     */
    public static class Linear implements ILimit
    {
        /**
         * Baseline limit.
         */
        private final int _base;

        /**
         * Per-objective limit added.
         */
        private final int _perObjective;

        /**
         * Parameterized constructor
         *
         * @param base         base value
         * @param perObjective per-objective value
         */
        public Linear(int base, int perObjective)
        {
            _base = base;
            _perObjective = perObjective;
        }

        /**
         * Returns the limit for the number of generations as base + (M - 1) * perObjective, where the base is
         * a baseline number of generations, M denotes the number of objectives considered, and mul determines
         * additional generations per objective.
         *
         * @param problem baseline limit
         * @param M       per-objective limit added
         * @return the generation limit
         */
        @Override
        public int getLimit(String problem, int M)
        {
            return _base + (M - 2) * _perObjective;
        }
    }

    /**
     * Class representing per-problem generations limit data.
     */
    public static class ProblemLimit
    {
        /**
         * Patter for regular expression. It should represent problem(s) identifiers.
         */
        private final Pattern _pattern;

        /**
         * Formula for the generations limit.
         */
        private final ILimit _limit;

        /**
         * Parameterized constructor. Uses the {@link Linear} formula as for the generations limit.
         *
         * @param pattern      problem ID pattern (case-insensitive)
         * @param base         baseline limit
         * @param perObjective per-objective limit added
         */
        public ProblemLimit(String pattern, int base, int perObjective)
        {
            this(pattern, new Linear(base, perObjective));
        }

        /**
         * Parameterized constructor. Uses the {@link Linear} formula as for the generations limit.
         *
         * @param pattern      problem ID pattern
         * @param base         baseline limit
         * @param perObjective per-objective limit added
         */
        public ProblemLimit(Pattern pattern, int base, int perObjective)
        {
            this(pattern, new Linear(base, perObjective));
        }

        /**
         * Parameterized constructor. Uses the {@link Linear} formula as for the generations limit.
         *
         * @param pattern problem ID pattern (case-insensitive)
         * @param limit   formula for the generations limit
         */
        public ProblemLimit(String pattern, ILimit limit)
        {
            _pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            _limit = limit;
        }

        /**
         * Parameterized constructor. Uses the {@link Linear} formula as for the generations limit.
         *
         * @param pattern problem ID pattern
         * @param limit   formula for the generations limit
         */
        public ProblemLimit(Pattern pattern, ILimit limit)
        {
            _pattern = pattern;
            _limit = limit;
        }

        /**
         * Returns the limit for the number of generations expressed as: base value + (M - 1) * mul.
         *
         * @param problem problem ID
         * @param M       the number of generations considered in the problem
         * @return generations limit
         */
        public int getLimit(String problem, int M)
        {
            return _limit.getLimit(problem, M);
        }
    }

    /**
     * Data on problem limits.
     */
    private final ProblemLimit[] _problemLimits;

    /**
     * Parameterized constructor.
     *
     * @param problemLimits data on generations limits imposed
     */
    private GenerationsLimits(ProblemLimit[] problemLimits)
    {
        _problemLimits = problemLimits;
    }

    /**
     * Method for constructing an instance of this class. The per-problem data on generations limits is supplied
     * explicitly via this constructor
     *
     * @param problemLimits data on generations limits
     * @return class instance (null, if the input is invalid, i.e., is null or has nulled entries)
     */
    public static GenerationsLimits getInstance(ProblemLimit[] problemLimits)
    {
        if (problemLimits == null) return null;
        if (problemLimits.length == 0) return null;
        for (ProblemLimit p : problemLimits)
        {
            if (p == null) return null;
            if (p._pattern == null) return null;
        }

        return new GenerationsLimits(problemLimits);
    }

    /**
     * Returns generations limit associated with a specified problem (problem string ID, e.g., "DTLZ2").
     * A suitable problem data object is identified by sequentially checking the internal problem data objects
     * (instances of {@link ProblemLimit}) and comparing the input problem ID with their patterns. The first match
     * triggered is used to determine the generations limit ({@link ProblemLimit#getLimit(String, int)}).
     *
     * @param problem problem string ID
     * @param M       the number of generations considered in the problem
     * @return generation limit (null, if no data for the input string is specified)
     */
    public Integer getLimit(String problem, int M)
    {
        if (_problemLimits == null) return null;
        for (ProblemLimit problemLimit : _problemLimits)
        {
            Matcher matcher = problemLimit._pattern.matcher(problem);
            boolean match = matcher.find();
            if (match) return problemLimit.getLimit(problem, M);
        }
        return null;
    }


}
