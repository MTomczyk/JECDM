package scheme.referencevalue;


import plot.AbstractPlot;

/**
 * Implementation of the {@link IReferenceValueGetter} interface.
 * Returns the reference value defined as min { "plot.width" , "plot.height" }.
 *
 * @author MTomczyk
 */

public class MinPlotDimension implements IReferenceValueGetter
{
    /**
     * The reference to the main panel (plot).
     */
    private final AbstractPlot _plot;

    /**
     * Parameterized constructor.
     *
     * @param plot the reference to the main panel (plot)
     */
    public MinPlotDimension(AbstractPlot plot)
    {
        _plot = plot;
    }

    /**
     * Returns the reference value defined as min { "panel.width" , "panel.height" }.
     *
     * @return reference value
     */
    @Override
    public float getReferenceValue()
    {
        return Math.min(_plot.getWidth(), _plot.getHeight());
    }
}
