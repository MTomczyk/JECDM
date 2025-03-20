package plot;

/**
 * Simple interface used for plot-related classes to adjust the plot params when creating the plot instance
 * on the fly (e.g., {@link Plot2DFactory#getPlot(String, String, double)}).
 *
 * @author MTomczyk
 */
public interface IPlotParamsAdjuster <T extends AbstractPlot.Params>
{
    /**
     * The main method's signature.
     *
     * @param pP plot params to be adjusted
     */
    void adjust(T pP);
}
