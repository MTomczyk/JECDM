package utils;

/**
 * Supportive class for representing a fixed/relative size that can be used
 * to, e.g., establish marker size, line width, etc.
 *
 * @author MTomczyk
 */

public class Size
{
    /**
     * Fixed, i.e., constant size.
     */
    private float _fixedSize = 0.0f;

    /**
     * Auxiliary multiplier used when determining the relative size.
     */
    private float _relativeSizeMultiplier = 1.0f;

    /**
     * Flag indicating whether the relative or fixed size should be used.
     */
    private boolean _useRelativeSize;

    /**
     * Actual size calculated either as a fixed or relative size (depends on the flag set).
     */
    public float _actualSize = 0.0f;

    /**
     * Calculates the actual size.
     *
     * @param relativeObjectSize Size of the object relative to which the actual size is to be calculated
     *                           (irrelevant if the actual size is fixed).
     */
    public void computeActualSize(float relativeObjectSize)
    {
        if (_useRelativeSize) _actualSize = relativeObjectSize * _relativeSizeMultiplier;
        else _actualSize = _fixedSize;
    }

    /**
     * Setter for the fixed size.
     *
     * @param fixedSize fixed size
     */
    public void setFixedSize(float fixedSize)
    {
        _fixedSize = fixedSize;
    }

    /**
     * Setter for the relative size multiplier.
     *
     * @param relativeSizeMultiplier relative size multiplier
     */
    public void setRelativeSizeMultiplier(float relativeSizeMultiplier)
    {
        _relativeSizeMultiplier = relativeSizeMultiplier;
    }

    /**
     * Setter for the "use relative size" flag.
     *
     * @param useRelativeSize "use relative size" flag
     */
    public void setUseRelativeSize(boolean useRelativeSize)
    {
        _useRelativeSize = useRelativeSize;
    }
}
