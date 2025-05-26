package plot;

import drmanager.DisplayRangesManager;

/**
 * Auxiliary interface for classes responsible for constructing instances of {@link plot.Plot2D}.
 *
 * @author MTomczyk
 */
public interface IPlot2DConstructor
{
    /**
     * The main method.
     *
     * @param pDRM optionally supplied params container for the display ranges manager to be used in plot 2D
     * @return plot 2D
     */
    Plot2D getPlot(DisplayRangesManager.Params pDRM);
}
