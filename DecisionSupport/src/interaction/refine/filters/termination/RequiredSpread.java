package interaction.refine.filters.termination;

import alternative.AbstractAlternatives;
import criterion.Criteria;
import criterion.Criterion;
import dmcontext.DMContext;
import exeption.RefinerException;
import space.normalization.INormalization;

import java.util.LinkedList;

/**
 * This filter calculates the spread of alternatives for each objective (delta in evaluations, absolute value).
 * The calculations are done in the normalized space (if the normalizations are provided via {@link DMContext};
 * otherwise, the calculations are done in the original objective space). If the deltas (all) are smaller than the given
 * threshold, then the alternatives set is considered too concentrated, favoring termination in this way.
 *
 * @author MTomczyk
 */
public class RequiredSpread extends AbstractTerminationFilter implements ITerminationFilter
{
    /**
     * Comparison threshold (terminate, if all spreads are smaller or equal to the threshold).
     * The array is used so that each objective can use a different threshold. Note that if
     * the array contains one element, it is used for all criteria/objectives.
     */
    private final double[] _thresholds;

    /**
     * Parameterized constructor. It assigns each objective/criterion the same threshold.
     *
     * @param threshold comparison threshold
     */
    public RequiredSpread(double threshold)
    {
        this(new double[]{threshold});
    }

    /**
     * Parameterized constructor. It assigns each objective/criterion different thresholds.
     *
     * @param thresholds comparison thresholds
     */
    public RequiredSpread(double[] thresholds)
    {
        super("Required spread");
        _thresholds = thresholds;
    }


    /**
     * The method verifying whether the reference sets construction process should be terminated. This filter calculates
     * the spread of alternatives for each objective (delta in evaluations, absolute value). The calculations are done
     * in the normalized space (if the normalizations are provided via {@link DMContext}; otherwise, the
     * calculations are done in the original objective space). If the deltas are smaller than the given threshold, then
     * the alternatives set is considered too concentrated, favoring termination in this way.
     *
     * @param dmContext current decision-making context (provides alternatives superset).
     * @return filter's indications
     * @throws RefinerException exception can be thrown and propagated higher
     */
    @Override
    public TerminationResult shouldTerminate(DMContext dmContext) throws RefinerException
    {
        validate(dmContext);
        return shouldTerminate(dmContext, dmContext.getCurrentAlternativesSuperset().getCopy());
    }

    /**
     * The method verifying whether the reference sets construction process should be terminated. This filter calculates
     * the spread of alternatives for each objective (delta in evaluations, absolute value). The calculations are done
     * in the normalized space (if the normalizations are provided via {@link DMContext}; otherwise, the
     * calculations are done in the original objective space). If the deltas are smaller than the given threshold, then
     * the alternatives set is considered too concentrated, favoring termination in this way.
     *
     * @param dmContext    decision-making context
     * @param alternatives wrapped alternatives
     * @return filter's indications
     * @throws RefinerException exception can be thrown and propagated higher
     */
    public TerminationResult shouldTerminate(DMContext dmContext, AbstractAlternatives<?> alternatives) throws RefinerException
    {
        validate(dmContext);
        if (alternatives == null)
            throw new RefinerException("The alternatives set is not provided (the array is null)", this.getClass());
        if (alternatives.isEmpty())
            return new TerminationResult(false, "Alternatives set is empty (empty set is assumed to be valid)");

        if (_thresholds == null)
            throw new RefinerException("The thresholds are not provided (the array is null)", this.getClass());
        if (_thresholds.length == 0)
            throw new RefinerException("The thresholds are not provided (the array is empty)", this.getClass());
        if ((_thresholds.length > 1) && (_thresholds.length != dmContext.getCriteria()._no))
            throw new RefinerException("The number of criteria (" + dmContext.getCriteria()._no +
                    ") differs from the number of thresholds (" + _thresholds.length + ") (and the number of thresholds is not 1)", this.getClass());

        Criteria C = dmContext.getCriteria();

        double[] min = new double[C._no];
        double[] max = new double[C._no];
        for (int c = 0; c < C._no; c++)
        {
            min[c] = Double.POSITIVE_INFINITY;
            max[c] = Double.NEGATIVE_INFINITY;
        }

        INormalization[] normalizations = dmContext.getNormalizationsCurrentOS();
        if ((normalizations != null) && (normalizations.length < C._no))
            throw new RefinerException("Not enough normalization objects were constructed (" +
                    normalizations.length + " but at least " + C._no + " are required)", this.getClass());

        for (int i = 0; i < alternatives.size(); i++)
        {
            double[] e = alternatives.get(i).getPerformanceVector();
            if (e.length < C._no)
                throw new RefinerException("The alternative " + alternatives.get(i).getName() +
                        " does not have enough evaluations (required at least " + C._no + " but has " + e.length + ")", this.getClass());
            for (int c = 0; c < C._no; c++)
            {
                double v = e[c];
                if (normalizations != null) v = normalizations[c].getNormalized(v);
                if (Double.compare(v, min[c]) < 0) min[c] = v;
                if (Double.compare(v, max[c]) > 0) max[c] = v;
            }
        }

        LinkedList<Criterion> eC = new LinkedList<>();

        for (int c = 0; c < C._no; c++)
        {
            double th = _thresholds[0];
            if (_thresholds.length > 1) th = _thresholds[c];
            if ((Double.compare(max[c], min[c]) >= 0) && (Double.compare(max[c] - min[c], th) >= 0))
            {
                eC.add(C._c[c]);
            }
        }

        if (!eC.isEmpty())
        {
            if (eC.size() == 1)
                return new TerminationResult(false, "Threshold exceeded or equal at criterion = " + eC.getFirst().getName());

            StringBuilder s = new StringBuilder();
            int index = 0;
            for (Criterion c : eC)
            {
                s.append(c.getName());
                if (index++ < eC.size() - 1) s.append(" ");
            }
            return new TerminationResult(false, "Threshold exceeded or equal at criteria = " + s);
        }

        return new TerminationResult(true, "The set is invalid (not sufficiently spread)");
    }
}
