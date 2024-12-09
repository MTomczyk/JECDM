package plotwrapper;

import container.Notification;
import plot.AbstractPlot;
import scheme.AbstractScheme;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper-like class for the {@link AbstractPlot} object (plot to be displayed). Can be extended and customized (e.g., by adding buttons etc.)
 *
 * @author MTomczyk
 */
public abstract class AbstractPlotWrapper extends JPanel
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Plot to be displayed.
         */
        public AbstractPlot _plot;

        /**
         * Parameterized constructor.
         *
         * @param plot plot to be displayed
         */
        public Params(AbstractPlot plot)
        {
            _plot = plot;
        }
    }

    /**
     * MVC model: plot wrapper controller.
     */
    protected PlotWrapperController _C;

    /**
     * MVC model: plot wrapper model.
     */
    protected PlotWrapperModel _M;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    protected AbstractPlotWrapper(Params p)
    {
        instantiateModelAndController(p);
        instantiateGUI(p);
        instantiateLayout(p);
    }

    /**
     * Instantiates the model and the controller.
     *
     * @param p params container
     */
    protected void instantiateModelAndController(Params p)
    {
        _M = getModelInstance(p);
        _C = getControllerInstance(p);
        _M.setPlotWrapperController(_C);
        _C.setPlotWrapperModel(_M);
        _M.setPlot(p._plot);
    }

    /**
     * Method for creating the controller instance.
     *
     * @param p params container
     * @return plot wrapper controller
     */
    protected PlotWrapperController getControllerInstance(Params p)
    {
        return new PlotWrapperController(this);
    }

    /**
     * Method for creating the model instance.
     *
     * @param p params container
     * @return plot wrapper model
     */
    protected PlotWrapperModel getModelInstance(Params p)
    {
        return new PlotWrapperModel(this);
    }

    /**
     * Can be overwritten to instantiate some additional GUI elements.
     *
     * @param p params container
     */
    protected void instantiateGUI(Params p)
    {

    }

    /**
     * Instantiates the layout.
     *
     * @param p params container
     */
    protected void instantiateLayout(Params p)
    {
        setLayout(new BorderLayout());
        add(_M._plot, BorderLayout.CENTER);
    }

    /**
     * Returns the model (MVC).
     *
     * @return the model
     */
    public PlotWrapperModel getModel()
    {
        return _M;
    }

    /**
     * Returns the controller (MVC).
     *
     * @return the controller
     */
    public PlotWrapperController getController()
    {
        return _C;
    }

    /**
     * Can be called to update plot appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc.); each plot uses a deep copy object as new scheme and updates its look; can also be null -> each plot uses its own scheme object provided when instantiated
     */
    public void updateScheme(AbstractScheme scheme)
    {
        Notification.printNotification(_M._GC, null, "Plot wrapper [id = " + _M.getPlotID() + "]: update scheme method called");
        _M._plot.updateScheme(scheme);
    }

    /**
     * Can be called to update the layout (typically called on the window resized event).
     */
    public void updateLayout()
    {
        Notification.printNotification(_M._GC, null, "Plot wrapper [id = " + _M.getPlotID() + "]: update layout method called");
        setPreferredSize(getSize());
        _M._plot.updateLayout();
    }

    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_M._GC, null, "Plot wrapper [id = " + _M.getPlotID() + "]: dispose method called");
        _C.dispose();
        _M.dispose();
        _C = null;
        _M = null;
    }
}
