package component.axis.ticksupdater;

import space.Range;
import space.normalization.minmax.AbstractMinMaxNormalization;
import space.normalization.minmax.Linear;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Extension of {@link AbstractTicksDataGetter}.
 * Ticks data (labels) is derived from the pre-provided fixed interval.
 * Ticks locations are fixed and equally spaced.
 * Labels are constructed as interpolated numbers or can be pre-defined.
 *
 * @author MTomczyk
 */

public class FromFixedInterval extends AbstractTicksDataGetter implements ITicksDataGetter
{
    /**
     * Normalizer parameterized based on the provided input interval.
     */
    protected AbstractMinMaxNormalization _normalizer;


    /**
     * Parameterized constructor.
     *
     * @param interval fixed interval
     * @param noTicks  number of ticks
     */
    public FromFixedInterval(Range interval, int noTicks)
    {
        this(interval, noTicks, new DecimalFormat("0.##E0"));
    }

    /**
     * Parameterized constructor.
     *
     * @param interval fixed interval
     * @param labels   pre-defined fixed tick labels
     */
    public FromFixedInterval(Range interval, String[] labels)
    {
        this(interval, labels, labels.length);
    }

    /**
     * Parameterized constructor.
     *
     * @param interval fixed interval
     * @param labels   pre-defined fixed tick labels
     * @param noTicks  number of ticks
     */
    public FromFixedInterval(Range interval, String[] labels, int noTicks)
    {
        super(noTicks);
        _predefinedLabels = labels;
        _normalizer = new Linear(interval.getLeft(), interval.getRight());
        createDefaultLocations();
    }

    /**
     * Parameterized constructor.
     *
     * @param interval  fixed interval
     * @param noTicks   number of ticks
     * @param format format used when translating doubles into string; can be null -> not used.
     */
    public FromFixedInterval(Range interval, int noTicks, NumberFormat format)
    {
        super(noTicks);
        _normalizer = new Linear(interval.getLeft(), interval.getRight());
        _format = format;
        createDefaultLocations();
    }

    /**
     * Creates and returns axis ticks labels.
     *
     * @return new labels for axis ticks
     */
    @Override
    public String[] getLabels()
    {
        if (_predefinedLabels != null) return _predefinedLabels;
        return getLabelsFromNormalizer(_normalizer);
    }


    /**
     * Supportive method for filling auxiliary forced data.
     * It processed forced unnormalized tick locations and fills an auxiliary vector with normalized tick locations.
     */
    @Override
    protected void fillAuxForcedData()
    {
        for (int i = 0; i < _noTicks; i++)
            _forcedNAuxData[i] = (float) _normalizer.getNormalized(_forcedUnnormalizedLocations[i]);
    }
}
