package layoutmanager;

import container.GlobalContainer;
import plot.AbstractPlot;
import scheme.AbstractScheme;

import java.awt.*;

/**
 * Supporting interface for classes that help organize plot components.
 *
 * @author MTomczyk
 */
public interface ILayoutManager
{
    /**
     * Adds elements to the plot (JPanel) in the correct order.
     *
     * @param plot the plot
     */
    void addElementsInCorrectOrder(AbstractPlot plot);

    /**
     * Used to properly set elements' dimensions.
     *
     * @param g current graphics context
     * @param x plot main panel x-coordinate
     * @param y plot main panel y-coordinate
     * @param w plot main panel width
     * @param h plot main panel height
     */
    void positionElements(Graphics g, int x, int y, int w, int h);

    /**
     * Used to properly set legend's dimensions.
     *
     * @param g current graphics context
     * @param x plot main panel x-coordinate
     * @param y plot main panel y-coordinate
     * @param w plot main panel width
     * @param h plot main panel height
     */
    void updateLegend(Graphics g, int x, int y, int w, int h);

    /**
     * Can be used to set the global container.
     *
     * @param GC global container: allows accessing various components of the main frame
     */
    void establishGlobalContainer(GlobalContainer GC);

    /**
     * Update scheme.
     *
     * @param scheme scheme object
     */
    void updateScheme(AbstractScheme scheme);

    /**
     * Can be called to clear data.
     */
    void dispose();
}
