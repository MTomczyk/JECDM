package layoutmanager;

import container.ComponentsContainer;
import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import plot.AbstractPlot;
import scheme.AbstractScheme;

import java.awt.*;

/**
 * Abstract layout manager class to be extended.
 *
 * @author MTomczyk
 */


public class AbstractLayoutManager implements ILayoutManager
{
    /**
     * Global container: allows accessing various components of the main frame.
     */
    protected GlobalContainer _GC;

    /**
     * Plot container: allows accessing various plot components.
     */
    protected PlotContainer _PC;

    /**
     * Container for various plot components.
     */
    protected ComponentsContainer _CC;

    /**
     * Parameterized constructor.
     *
     * @param PC plot container: allows easy access to various plot-related components/functionalities
     * @param CC container for various plot components
     */
    public AbstractLayoutManager(PlotContainer PC, ComponentsContainer CC)
    {
        _PC = PC;
        _CC = CC;
    }

    /**
     * Adds elements to the plot (JPanel) in the correct order.
     *
     * @param plot the plot
     */
    @Override
    public void addElementsInCorrectOrder(AbstractPlot plot)
    {

    }

    /**
     * Used to properly set elements' dimensions.
     *
     * @param g current rendering context
     * @param x plot main panel x-coordinate
     * @param y plot main panel y-coordinate
     * @param w plot main panel width
     * @param h plot main panel height
     */
    @Override
    public void positionElements(Graphics g, int x, int y, int w, int h)
    {

    }

    /**
     * Used to properly set legend's dimensions.
     *
     * @param g current graphics context
     * @param x plot main panel x-coordinate
     * @param y plot main panel y-coordinate
     * @param w plot main panel width
     * @param h plot main panel height
     */
    public void updateLegend(Graphics g, int x, int y, int w, int h)
    {

    }

    /**
     * Can be used to set the global container.
     *
     * @param GC global container: allows accessing various components of the main frame
     */
    @Override
    public void establishGlobalContainer(GlobalContainer GC)
    {
        _GC = GC;
        Notification.printNotification(_GC, _PC, "Base layout manager [id = " + PlotContainer.getID(_PC) + "]: global container is set");
    }

    /**
     * Updates the scheme of all plot components.
     *
     * @param scheme scheme object
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {

    }

    /**
     * Can be called to clear data.
     */
    @Override
    public void dispose()
    {


    }
}
