package component.axis.ticksupdater;

import space.normalization.INormalization;
import space.normalization.minmax.AbstractMinMaxNormalization;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Default implementation of {@link ITicksDataGetter}.
 *
 * @author MTomczyk
 */

public abstract class AbstractTicksDataGetter implements ITicksDataGetter
{
    /**
     * Number of ticks.
     */
    protected int _noTicks;

    /**
     * Labels to be displayed.
     */
    protected String[] _labels;

    /**
     * Predefined labels (stored/fixed).
     */
    protected String[] _predefinedLabels;

    /**
     * Ticks locations (ticks locations [0, 1]. They linearly interpolated along the axis).
     */
    protected float[] _defaultLocations;

    /**
     * Ticks locations in the normalized [0, 1] space. If not null, it surpasses the default locations.
     */
    protected float[] _forcedNormalizedLocations = null;

    /**
     * Ticks locations in the unnormalized (original) space. If not null, it surpasses the default locations
     * and {@link AbstractTicksDataGetter#_forcedNormalizedLocations}.
     */
    protected float[] _forcedUnnormalizedLocations = null;

    /**
     * Supportive field boosting memory management in the case when {@link AbstractTicksDataGetter#_forcedUnnormalizedLocations} is used.
     */
    protected float[] _forcedNAuxData = null;

    /**
     * Number format used when translating doubles into a string. Can be null -> not used.
     */
    protected NumberFormat _format;


    /**
     * Parameterized constructor.
     *
     * @param noTicks axis no ticks (>= 2)
     */
    public AbstractTicksDataGetter(int noTicks)
    {
        this(noTicks, new DecimalFormat("0.##E0"));
    }

    /**
     * Parameterized constructor.
     *
     * @param noTicks axis no ticks (>= 2)
     * @param format  number format used when translating doubles into a string. Can be null -> not used
     */
    public AbstractTicksDataGetter(int noTicks, NumberFormat format)
    {
        assert noTicks >= 1;
        _noTicks = noTicks;
        _format = format;
    }

    /**
     * Getter for the normalized ticks locations. They are linearly interpolated along the axis.
     * The ticks are expected to be in [0, 1]. If, however, the point are outside this bound (e.g., on the user's demand),
     * it is the responsibility of a receiver to exclude improper bounds. Note that the method only returns the reference.
     * Use clone() if changes are necessary.
     *
     * @return normalized ticks locations
     */
    public float[] getTicksLocations()
    {
        float[] locSource = _defaultLocations;
        if (_forcedNormalizedLocations != null) locSource = _forcedNormalizedLocations;
        if (_forcedUnnormalizedLocations != null) locSource = _forcedNAuxData;
        return locSource;
    }

    /**
     * Method for retrieving new labels for axis.
     *
     * @return new labels for axis ticks
     */
    public String[] getLabels()
    {
        return _labels;
    }

    /**
     * Getter for the number of ticks.
     *
     * @return number of ticks
     */
    public int getNoTicks()
    {
        return _noTicks;
    }

    /**
     * Setter for the number of ticks.
     * The method also re-calculates default tick locations.
     *
     * @param noTicks no. ticks
     */
    @Override
    public void setNoTicks(int noTicks)
    {
        _noTicks = noTicks;
        createDefaultLocations();
    }

    /**
     * Auxiliary method for instantiating ticks default locations.
     */
    protected void createDefaultLocations()
    {
        _defaultLocations = new float[_noTicks];
        float dl = 1.0f / (_noTicks - 1);
        for (int i = 0; i < _noTicks - 1; i++)
            _defaultLocations[i] = i * dl;
        _defaultLocations[_noTicks - 1] = 1.0f;
    }

    /**
     * Setter for the predefined tick labels.
     * These labels are intended to surpass labels that can be automatically generated.
     *
     * @param predefinedTickLabels predefined tick labels
     */
    public void setPredefinedTickLabels(String[] predefinedTickLabels)
    {
        _predefinedLabels = predefinedTickLabels;
    }

    /**
     * Auxiliary method for constructing and returning tick labels based on stored ticks locations and provided normalizer.
     *
     * @param normalizer normalization function
     * @return tick labels
     */
    protected String[] getLabelsFromNormalizer(AbstractMinMaxNormalization normalizer)
    {
        float[] locs = getTicksLocations().clone();
        _labels = new String[Math.min(locs.length, _noTicks)];
        for (int i = 0; i < Math.min(locs.length, _noTicks); i++)
        {
            double unv = normalizer.getUnnormalized(locs[i]);
            if (_format == null) _labels[i] = String.valueOf(unv);
            else _labels[i] = _format.format(unv);
        }
        return _labels;
    }

    /**
     * Setter for the tick locations in the normalized [0, 1] space. If not null, it surpasses the default locations.
     *
     * @param forcedNormalizedLocations forced normalized locations
     */
    @Override
    public void setForcedNormalizedLocations(float[] forcedNormalizedLocations)
    {
        _forcedNormalizedLocations = forcedNormalizedLocations;
    }

    /**
     * Setter for the tick locations in the unnormalized (original) space. If not null, it surpasses the default locations
     * and {@link AbstractTicksDataGetter#_forcedNormalizedLocations}.
     *
     * @param forcedUnnormalizedLocations forced locations in the original space
     */
    @Override
    public void setForcedUnnormalizedLocations(float[] forcedUnnormalizedLocations)
    {
        _forcedUnnormalizedLocations = forcedUnnormalizedLocations;
        initAuxForcedData();
        fillAuxForcedData();
    }

    /**
     * Creates auxiliary field for storing forced unnormalized data.
     */
    protected void initAuxForcedData()
    {
        if (_forcedUnnormalizedLocations == null)
        {
            _forcedNAuxData = null;
            _noTicks = 0;
        }
        else if ((_forcedNAuxData == null) || (_forcedNAuxData.length != _forcedUnnormalizedLocations.length))
        {
            _forcedNAuxData = new float[_forcedUnnormalizedLocations.length];
            _noTicks = _forcedUnnormalizedLocations.length;
        }
    }


    /**
     * Creates the ticks' locations in the original space using the provided normalizer.
     * The method processes default ticks locations evenly distributed along the [0-1] interval and passes them
     * through the normalizer to construct forced tick locations in the original space.
     *
     * @param normalizer normalizer used to construct tick locations
     */
    @Override
    public void createForcedUnnormalizedLocationsUsingNormalizer(INormalization normalizer)
    {
        _forcedUnnormalizedLocations = new float[_defaultLocations.length];
        for (int i = 0; i < _defaultLocations.length; i++)
            _forcedUnnormalizedLocations[i] = (float) normalizer.getNormalized(_defaultLocations[i]);
        initAuxForcedData();
        fillAuxForcedData();
    }

    /**
     * Supportive method for filling auxiliary forced data.
     * It processed forced unnormalized ticks locations and fills an auxiliary vector with normalized ticks locations.
     */
    protected void fillAuxForcedData()
    {
        System.arraycopy(_forcedUnnormalizedLocations, 0, _forcedNAuxData, 0, _forcedUnnormalizedLocations.length);
    }

    /**
     * Checks if tick normalized localization is in [0,1] range.
     *
     * @param loc tick normalized localization
     * @return true if tick normalized localization is in [0,1] range, false otherwise
     */
    public static boolean isTickLockNormal(float loc)
    {
        return ((Float.compare(loc, 0.0f) >= 0) && (Float.compare(loc, 1.0f) <= 0));
    }

    /**
     * Setter for the number format used when translating doubles into a string.
     *
     * @param format number format used when translating doubles into a string
     */
    public void setNumberFormat(NumberFormat format)
    {
        _format = format;
    }
}
