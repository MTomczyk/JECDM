package dataset.painter.style.size;

import container.GlobalContainer;
import container.PlotContainer;

import java.awt.*;

/**
 * Calculates relative value as a percent of "min {drawing area bounds (in pixels))}".
 *
 * @author MTomczyk
 */


public class RelativeToMinDrawingAreaBoundsSize implements IRelativeSize
{
    /**
     * If true, the drawing area recalling factors (offscreen/onscreen renders proportions) are taken into
     * account when determining the relative value.
     */
    private final boolean _considerRescalingFactor;

    /**
     * Default constructor.
     */
    public RelativeToMinDrawingAreaBoundsSize()
    {
        this(true);
    }

    /**
     * Parameterized constructor.
     *
     * @param considerRescalingFactor If true, the drawing area recalling factors (offscreen/onscreen renders proportions) are taken into account when determining the relative value.
     */
    public RelativeToMinDrawingAreaBoundsSize(boolean considerRescalingFactor)
    {
        _considerRescalingFactor = considerRescalingFactor;
    }

    /**
     * Calculates relative value.
     *
     * @param GC             global container
     * @param PC             plot container
     * @param referenceValue input (fixed) size based on which the relative value will be calculated (e.g., some percent value)
     * @return relative value
     */
    public float getSize(GlobalContainer GC, PlotContainer PC, float referenceValue)
    {
        Rectangle area = PC.getDrawingArea().getPrimaryDrawingArea();
        Float rescale = null;

        float min = (float) area.width;
        if (_considerRescalingFactor) rescale = PC.getDrawingArea().getRenderingData().getRescallingFactorX();

        if (area.height < min)
        {
            min = (float) area.height;
            if (_considerRescalingFactor) rescale = PC.getDrawingArea().getRenderingData().getRescallingFactorY();
        }

        if (rescale != null) return min * referenceValue * rescale / 100.0f;
        else return min * referenceValue / 100.0f;
    }

    /**
     * Constructs object's clone.
     *
     * @return deep copy
     */
    @Override
    public IRelativeSize getClone()
    {
        return new RelativeToMinDrawingAreaBoundsSize();
    }
}
