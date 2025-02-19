package plotswrapper;

import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;
import plotwrapper.PlotWrapper;

import javax.swing.*;
import java.awt.*;


/**
 * Plot container that organizes two plots horizontally.
 * This implementation differs from {@link TwoPlotsHorizontally} by separating the plots with JSplitPane.
 *
 * @author MTomczyk
 */
public class TwoPlotsHorizontallyWithSplit extends AbstractPlotsWrapper
{
    /**
     * Params container.
     */
    public static class Params extends AbstractPlotsWrapper.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param leftPlot  left plot (default wrapper is used)
         * @param rightPlot right plot (default wrapper is used)
         */
        public Params(AbstractPlot leftPlot, AbstractPlot rightPlot)
        {
            this(new PlotWrapper(leftPlot), new PlotWrapper(rightPlot));
        }

        /**
         * Parameterized constructor.
         *
         * @param leftWrapper  left plot wrapper
         * @param rightWrapper right plot wrapper
         */
        public Params(AbstractPlotWrapper leftWrapper, AbstractPlotWrapper rightWrapper)
        {
            super(new AbstractPlotWrapper[]{leftWrapper, rightWrapper});
        }
    }

    /**
     * Split pane.
     */
    private JSplitPane _splitPane;

    /**
     * Parameterized constructor.
     *
     * @param leftPlot  left plot (default wrapper is used)
     * @param rightPlot rightPlot (default wrapper is used)
     */
    public TwoPlotsHorizontallyWithSplit(AbstractPlot leftPlot, AbstractPlot rightPlot)
    {
        this(new PlotWrapper(leftPlot), new PlotWrapper(rightPlot));
    }

    /**
     * Parameterized constructor.
     *
     * @param leftWrapper  left plot wrapper
     * @param rightWrapper right plot wrapper
     */
    public TwoPlotsHorizontallyWithSplit(AbstractPlotWrapper leftWrapper, AbstractPlotWrapper rightWrapper)
    {
        this(new Params(leftWrapper, rightWrapper));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public TwoPlotsHorizontallyWithSplit(Params p)
    {
        super(p);
    }

    /**
     * Can be overwritten to instantiate some additional GUI elements.
     *
     * @param p params container
     */
    @Override
    protected void instantiateGUI(AbstractPlotsWrapper.Params p)
    {
        _splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        _splitPane.setOneTouchExpandable(true);
        _splitPane.setResizeWeight(0.5d);
        _splitPane.setDividerLocation(0.5d);
        _splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt ->
                // Invoke later needed to call the updates after the layout managers are updated
                SwingUtilities.invokeLater(() -> {
                    getModel()._GC.getFrame().updateLayout();
                    getModel().updatePlotsIDSsOnScreenResize();
                }));
    }

    /**
     * Instantiates the layout (to be overwritten).
     *
     * @param p params container
     */
    @Override
    protected void instantiateLayout(AbstractPlotsWrapper.Params p)
    {
        _splitPane.setLeftComponent(p._wrappers[0]);
        _splitPane.setRightComponent(p._wrappers[1]);
        _splitPane.setResizeWeight(0.5d);
        _splitPane.setDividerLocation(0.5d);
        setLayout(new BorderLayout());
        add(_splitPane);
    }

    /**
     * Auxiliary method for hiding a right panel.
     */
    public void hideRightPane()
    {
        _splitPane.setDividerLocation(1.0f);
    }

    /**
     * Auxiliary method for hiding a left panel.
     */
    public void hideLeftPane()
    {
        _splitPane.setDividerLocation(0.0f);
    }

    /**
     * Auxiliary method for equally spacing panels.
     */
    public void equallySpacePanel()
    {
        _splitPane.setDividerLocation(0.5f);
    }
}
