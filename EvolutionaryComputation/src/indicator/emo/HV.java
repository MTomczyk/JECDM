package indicator.emo;

import criterion.Criteria;
import indicator.AbstractPerformanceIndicator;
import indicator.IPerformanceIndicator;
import population.Specimen;
import relation.dominance.DominanceUtils;
import relation.equalevaluations.EqualEvaluationsUtils;
import space.normalization.INormalization;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A WFG implementation for the hypervolume calculation (see <a href="https://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=5766730">link</a>).
 * Note that this implementation bounds the space using a normalized hypercube [0, 1]^{the number of objectives M}.
 * The scaling (i.e., mapping specimens into a hypercube) is controlled by provided normalization objects
 * ({@link INormalization}, each for a different objective. It is assumed that all the objectives
 * are to be minimized, and the 0-vector represents the best attainable objective vector. In turn, the R-vector is the
 * upper bound, i.e., the reference point used for hypervolume calculation.
 *
 * @author MTomczyk
 */
public class HV extends AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * Policies for contribution assessment for normalized points that do not dominate the reference point.
     */
    public enum PolicyForNonDominating
    {
        /**
         * The dominated points will not contribute to total HV (recommended).
         */
        IGNORE,

        /**
         * The method will proceed following pseudocodes in <a href="https://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=5766730">link</a>.
         */
        NO_POLICY,
    }

    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * The number of objectives.
         */
        public final int _M;

        /**
         * Normalizations used to map specimens into [0-1]^{the number of objectives M} hypercube. There should be
         * exactly M such objects provided, each linked to a different objective. Note that the normalizations
         * can be null (not used) -> the evaluation vectors are explicitly used when calculating the hypervolume.
         */
        public final INormalization[] _normalizations;

        /**
         * Reference point. Provides the upper bound for the objective space.
         * The reference point is assumed to be already normalized.
         */
        public final double[] _rp;

        /**
         * If true, the data points are pre-sorted monotonically on the first objective, before calculating HV.
         * It speeds up the calculations.
         */
        public boolean _presorting;

        /**
         * Policy for contribution assessment for normalized points that do not dominate the reference point.
         */
        public PolicyForNonDominating _policyForNonDominating;

        /**
         * If true, the method first derives the non-dominated set from the input specimens set (or from the already derived
         * unique vectors; for efficiency, it is recommended to use this mode).
         */
        public boolean _deriveNonDominatedSet;

        /**
         * If true, the method first derives unique specimens (in terms of their performance vectors) from the input
         * specimens set (it is recommended to use this mode; note that the unique specimens are derived before deriving
         * non-dominated ones).
         */
        public boolean _deriveUniqueSpecimens;

        /**
         * Tolerance for double-comparisons (used when deriving unique sets).
         */
        public double _toleranceDuplicates;

        /**
         * Parameterized constructor (tuned to derive non-dominated solutions).
         *
         * @param M  the number of objectives
         * @param rp reference point; provides the upper bound for the objective space; the reference
         *           point is assumed to be already normalized
         */
        public Params(int M, double[] rp)
        {
            this(M, null, rp);
        }

        /**
         * Parameterized constructor (tuned to derive unique solutions and non-dominated ones).
         *
         * @param M              the number of objectives
         * @param normalizations normalizations used to map specimens into [0-1]^{the number of objectives M} hypercube;
         *                       there should be exactly M such objects provided, each linked to a different objective;
         *                       note that the normalizations can be null (not used) -> the evaluation vectors are
         *                       explicitly used when calculating the hypervolume
         * @param rp             reference point; provides the upper bound for the objective space; the reference
         *                       point is assumed to be already normalized
         */
        public Params(int M, INormalization[] normalizations, double[] rp)
        {
            this(M, normalizations, rp, true, PolicyForNonDominating.IGNORE, true, true, 1.0E-9);
        }

        /**
         * Parameterized constructor.
         *
         * @param M                      the number of objectives
         * @param normalizations         normalizations used to map specimens into [0-1]^{the number of objectives M} hypercube;
         *                               there should be exactly M such objects provided, each linked to a different objective;
         *                               note that the normalizations can be null (not used) -> the evaluation vectors are
         *                               explicitly used when calculating the hypervolume
         * @param rp                     reference point; provides the upper bound for the objective space; the reference
         *                               point is assumed to be already normalized
         * @param presorting             if true, the data points are pre-sorted monotonically on the first objective, before
         *                               calculating HV; it speeds up the calculations
         * @param policyForNonDominating policy for contribution assessment for normalized points that do not dominate the reference point
         * @param deriveUniqueSpecimens  if true, the method first derives unique specimens (in terms of their performance vectors)
         *                               from the input specimens set (it is recommended to use this mode; note that
         *                               the unique specimens are derived before deriving non-dominated ones)
         * @param deriveNonDominatedSet  if true, the method first derives the non-dominated set from the input specimens
         *                               set (it is recommended to use this mode)
         * @param toleranceDuplicates    tolerance for double-comparisons (used when deriving unique and non-dominated sets)
         */
        private Params(int M, INormalization[] normalizations, double[] rp, boolean presorting,
                       PolicyForNonDominating policyForNonDominating,
                       boolean deriveUniqueSpecimens,
                       boolean deriveNonDominatedSet,
                       double toleranceDuplicates)
        {
            _M = M;
            _normalizations = normalizations;
            _rp = rp;
            _presorting = presorting;
            _policyForNonDominating = policyForNonDominating;
            _deriveNonDominatedSet = deriveNonDominatedSet;
            _deriveUniqueSpecimens = deriveUniqueSpecimens;
            _toleranceDuplicates = toleranceDuplicates;
        }
    }

    /**
     * The number of objectives.
     */
    private final int _M;

    /**
     * Normalizations used to map specimens into [0-1]^{the number of objectives M} hypercube. There should be
     * exactly M such objects provided, each linked to a different objective. Note that the normalizations
     * can be null (not used) -> the evaluation vectors are explicitly used when calculating the hypervolume.
     */
    private final INormalization[] _normalizations;

    /**
     * Reference point. Provides the upper bound for the objective space.
     * The reference point is assumed to be already normalized.
     */
    private final double[] _rp;

    /**
     * If true, the data points are pre-sorted monotonically on the first objective, before calculating HV.
     * It speeds up the calculations.
     */
    private final boolean _presorting;

    /**
     * Policies for contribution assessment for normalized points that do not dominate the reference point.
     */
    private final PolicyForNonDominating _policyForNonDominating;

    /**
     * If true, the method first derives unique specimens (in terms of their performance vectors) from the input
     * specimens set (it is recommended to use this mode; note that the unique specimens are derived before deriving
     * non-dominated ones).
     */
    private final boolean _deriveUniqueSpecimens;

    /**
     * If true, the method first derives the non-dominated set from the input specimens set (or from the already derived
     * unique vectors; it is recommended to use this mode).
     */
    private final boolean _deriveNonDominatedSet;

    /**
     * Tolerance for double-comparisons (used when deriving unique sets).
     */
    private final double _toleranceDuplicates;

    /**
     * Criteria (in the normalized space -> all to be minimized)
     */
    private final Criteria _nCriteria;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public HV(Params p)
    {
        super(false);
        if (p._rp == null)
        {
            _rp = new double[p._M];
            for (int i = 0; i < p._M; i++) _rp[i] = 1.0d;
        }
        else _rp = p._rp;
        _normalizations = p._normalizations;
        _M = p._M;
        _presorting = p._presorting;
        _policyForNonDominating = p._policyForNonDominating;
        _deriveUniqueSpecimens = p._deriveUniqueSpecimens;
        _deriveNonDominatedSet = p._deriveNonDominatedSet;
        _toleranceDuplicates = p._toleranceDuplicates;
        _nCriteria = Criteria.constructCriteria("nC", _M, false);
    }


    /**
     * Method that calculated the generational distance value.
     *
     * @param population input population
     * @return performance value
     */
    @Override
    protected double evaluate(ArrayList<Specimen> population)
    {
        // create raw solution set
        double[][] S;

        // translate and normalize
        S = new double[population.size()][];
        for (int i = 0; i < population.size(); i++)
        {
            S[i] = population.get(i).getEvaluations().clone();
            if (_normalizations != null)
                for (int j = 0; j < _M; j++) S[i][j] = _normalizations[j].getNormalized(S[i][j]);
        }

        // Optionally, derive unique specimens
        if (_deriveUniqueSpecimens)
            S = EqualEvaluationsUtils.removeDuplicates(S, _toleranceDuplicates);

        // optionally derive non-dominated
        if (_deriveNonDominatedSet)
            S = DominanceUtils.getNonDominatedVectors(S, _nCriteria);

        // do optional presorting
        if (_presorting) Arrays.sort(S, (o1, o2) -> -Double.compare(o1[0], o2[0]));

        if (_policyForNonDominating.equals(PolicyForNonDominating.NO_POLICY)) return wfg(S);
        else
        {
            ArrayList<double[]> valid = new ArrayList<>(S.length);
            for (double[] s : S)
                if (DominanceUtils.isGoodAtLeastAs(s, _rp, _nCriteria)) valid.add(s);

            double[][] parsedS = new double[valid.size()][_M];
            for (int i = 0; i < valid.size(); i++) parsedS[i] = valid.get(i);

            if (parsedS.length == 0) return 0.0d;
            return wfg(parsedS);

        }
    }

    /**
     * Te main WFG procedure.
     *
     * @param S non-dominated solution set (raw/normalized)
     * @return hypervolume for S
     */
    protected double wfg(double[][] S)
    {
        if (S == null) return 0.0d;
        if (S.length == 0) return 0.0d;
        double sum = 0.0d;
        for (int k = 0; k < S.length; k++) sum += exclhv(S, k);
        return sum;
    }

    /**
     * Calculates the exclusive HV.
     *
     * @param S non-dominated solution set (raw/normalized)
     * @param k nds array index (points to the currently processed solution)
     * @return inclusive HV
     */
    protected double exclhv(double[][] S, int k)
    {
        if (S == null) return 0.0d;
        if (S.length == 0) return 0.0d;

        double[][] ls = limitset(S, k);
        double inc = inclhv(S[k]);
        if (ls == null) return inc;
        double[][] nd = DominanceUtils.getNonDominatedVectors(ls, _nCriteria);
        return inc - wfg(nd);
    }

    /**
     * Creates the limit set.
     *
     * @param S non-dominated solution set (raw/normalized)
     * @param k nds array index (points to the currently processed solution)
     * @return limit set
     */
    protected double[][] limitset(double[][] S, int k)
    {
        if (S.length - k - 1 == 0) return null;
        double[][] ql = new double[S.length - k - 1][_M];
        int idx = 0;
        for (int i = 0; i < S.length - k - 1; i++)
        {
            for (int j = 0; j < _M; j++)
            {
                if (Double.compare(S[i + k + 1][j], S[k][j]) > 0) ql[idx][j] = S[i + k + 1][j];
                else ql[idx][j] = S[k][j];
            }
            idx++;
        }
        return ql;
    }

    /**
     * Calculates the inclusive HV.
     *
     * @param s solution evaluations (normalized)
     * @return inclusive HV
     */
    protected double inclhv(double[] s)
    {
        if (_M == 1) return 0.0d;
        double inclhv = 1.0d;
        for (int i = 0; i < _M; i++) inclhv *= Math.abs(_rp[i] - s[i]);
        return inclhv;
    }


    /**
     * Returns string representation (HV)
     *
     * @return "HV"
     */
    @Override
    public String toString()
    {
        return "HV";
    }

    /**
     * Creates a cloned object in an initial state.
     * The parameters are not deep-copies (references are passed).
     *
     * @return cloning
     */
    @Override
    public IPerformanceIndicator getInstanceInInitialState()
    {
        HV.Params pHV = new Params(_M, _normalizations, _rp);
        pHV._presorting = _presorting;
        pHV._policyForNonDominating = _policyForNonDominating;
        pHV._deriveUniqueSpecimens = _deriveUniqueSpecimens;
        pHV._deriveNonDominatedSet = _deriveNonDominatedSet;
        pHV._toleranceDuplicates = _toleranceDuplicates;
        return new HV(pHV);
    }
}
