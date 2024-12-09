package dataset.painter.style.size;


import container.GlobalContainer;
import container.PlotContainer;

/**
 * Interface for classes responsible for establishing relative values that can be used by the plots to determine, e.g.,
 * marker size/line relative size.
 *
 * @author MTomczyk
 */
public interface IRelativeSize
{
    /**
     * Calculates the relative size
     *
     * @param GC        global container
     * @param PC        plot container
     * @param referenceValue input (fixed) size based on which the relative value will be calculated (e.g., some percent value)
     * @return relative size
     */
    float getSize(GlobalContainer GC, PlotContainer PC, float referenceValue);

    /**
     * Constructs object's clone.
     *
     * @return deep copy
     */
    IRelativeSize getClone();
}
