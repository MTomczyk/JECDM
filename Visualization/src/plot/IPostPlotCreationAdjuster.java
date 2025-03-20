package plot;

/**
 * Simple interface used for plot-related classes to adjust the plot after its construction on the fly (e.g.,
 * {@link Plot2DFactory#getPlot(String, String, double)}).
 *
 * @author MTomczyk
 */
public interface IPostPlotCreationAdjuster<T extends AbstractPlot>
{
    /**
     * The main method's signature.
     *
     * @param plot plot to be adjusted
     */
    void adjust(T plot);
}
