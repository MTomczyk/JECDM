package component.axis.ticksupdater;

import space.normalization.INormalization;

import java.text.NumberFormat;

/**
 * Interface for classes responsible for providing ticks data: providing locations, labels, and no. ticks.
 *
 * @author MTomczyk
 */
public interface ITicksDataGetter
{
    /**
     * Getter for the number of ticks.
     *
     * @return number of ticks
     */
    int getNoTicks();

    /**
     * Setter for the number of ticks.
     *
     * @param noTicks no. ticks
     */
    void setNoTicks(int noTicks);


    /**
     * Getter for the normalized ticks locations [0, 1]. They are linearly interpolated along the axis.
     *
     * @return normalized ticks locations
     */
    float[] getTicksLocations();

    /**
     * Method for retrieving new labels for axis.
     *
     * @return new labels for axis ticks
     */
    String[] getLabels();

    /**
     * Setter for the predefined tick labels.
     * These labels are intended to surpass labels that can be automatically generated.
     *
     * @param predefinedTickLabels predefined tick labels
     */
    void setPredefinedTickLabels(String[] predefinedTickLabels);

    /**
     * Setter for the tick locations in the normalized [0, 1] space. If not null, it surpasses the default locations.
     *
     * @param forcedNormalizedLocations forced normalized locations
     */
    void setForcedNormalizedLocations(float[] forcedNormalizedLocations);

    /**
     * Setter for the tick locations in the unnormalized (original) space. If not null, it surpasses the default locations
     * and {@link AbstractTicksDataGetter#_forcedNormalizedLocations}.
     *
     * @param forcedUnnormalizedLocations forced locations in the original space
     */
    void setForcedUnnormalizedLocations(float[] forcedUnnormalizedLocations);

    /**
     * Creates the tick locations in the original space using the provided normalizer.
     * The method processes default ticks locations evenly distributed along the [0-1] interval and passes them
     * through the normalizer to construct forced tick locations in the original space.
     *
     * @param normalizer normalizer used to construct tick locations
     */
    void createForcedUnnormalizedLocationsUsingNormalizer(INormalization normalizer);

    /**
     * Setter for the number format used when translating doubles into a string.
     *
     * @param numberFormat number format used when translating doubles into a string
     */
    void setNumberFormat(NumberFormat numberFormat);
}

