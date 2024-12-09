package component.axis.ticksupdater;

import container.PlotContainer;
import drmanager.DisplayRangesManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Extension of {@link AbstractTicksDataGetter}.
 * Ticks data (labels) is derived from the display range.
 * Ticks locations are fixed and equally spaced.
 *
 * @author MTomczyk
 */

public class FromDisplayRange extends AbstractTicksDataGetter implements ITicksDataGetter
{
    /**
     * Reference to the display range object maintained by {@link DisplayRangesManager}.
     */
    protected DisplayRangesManager.DisplayRange _DR;

    /**
     * Reference to the plot container.
     */
    protected PlotContainer _PC;

    /**
     * Parameterized constructor.
     *
     * @param DR      reference to the display range object maintained by {@link DisplayRangesManager}
     * @param noTicks number of ticks
     */
    public FromDisplayRange(DisplayRangesManager.DisplayRange DR, int noTicks)
    {
        this(DR, noTicks, new DecimalFormat("0.##E0"));
    }

    /**
     * Parameterized constructor.
     *
     * @param DR     reference to the display range object maintained by {@link DisplayRangesManager}
     * @param labels pre-defined fixed tick labels
     */
    public FromDisplayRange(DisplayRangesManager.DisplayRange DR, String[] labels)
    {
        this(DR, labels, labels.length);
    }

    /**
     * Parameterized constructor.
     *
     * @param DR      reference to the display range object maintained by {@link DisplayRangesManager}
     * @param labels  pre-defined fixed tick labels
     * @param noTicks number of ticks
     */
    public FromDisplayRange(DisplayRangesManager.DisplayRange DR, String[] labels, int noTicks)
    {
        super(noTicks);
        _DR = DR;
        _predefinedLabels = labels;
        createDefaultLocations();
    }

    /**
     * Parameterized constructor.
     *
     * @param DR        reference to the display range object maintained by {@link DisplayRangesManager}
     * @param noTicks   number of ticks
     * @param format format used when translating doubles into string; can be null -> not used.
     */
    public FromDisplayRange(DisplayRangesManager.DisplayRange DR, int noTicks, NumberFormat format)
    {
        super(noTicks);
        _DR = DR;
        _format = format;
        createDefaultLocations();
    }

    /**
     * Getter for the normalized ticks locations [0, 1]. They are linearly interpolated along the axis.
     */
    @Override
    public float[] getTicksLocations()
    {
        float[] locSource = super.getTicksLocations();
        if (_forcedUnnormalizedLocations != null)
        {
            fillAuxForcedData();
            locSource = _forcedNAuxData;
        }
        return locSource;
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
        return getLabelsFromNormalizer(_DR.getNormalizer());
    }

    /**
     * Supportive method for filling auxiliary forced data.
     * It processed forced unnormalized tick locations and fills an auxiliary vector with normalized tick locations.
     */
    @Override
    protected void fillAuxForcedData()
    {
        for (int i = 0; i < _noTicks; i++)
            _forcedNAuxData[i] = (float) _DR.getNormalizer().getNormalized(_forcedUnnormalizedLocations[i]);
    }
}
