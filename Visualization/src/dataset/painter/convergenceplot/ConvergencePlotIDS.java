package dataset.painter.convergenceplot;

import dataset.painter.IDS;

import java.util.LinkedList;


/**
 * Extension of {@link IDS} for handling envelope-related data (converge plots).
 *
 * @author MTomczyk
 */
public class ConvergencePlotIDS extends IDS
{
    /**
     * Total number of individual envelope points (among all segments; shared for the upper and lower envelope bound).
     */
    public int _noEnvelopePoints = 0;

    /**
     * Total number of individual line points per contiguous lines (shared for the upper and lower envelope bound).
     */
    public LinkedList<Integer> _noEnvelopePointsInContiguousLines = null;

    /**
     * List of contiguous lines representing an upper bound of the envelope.
     * Data points are stored sequentially, taking n subsequent elements, where n denotes the number of attributes/dimensions.
     */
    public LinkedList<float[]> _normalizedUpperLine = null;

    /**
     * List of contiguous lines representing a lower bound of the envelope.
     * Data points are stored sequentially, taking n subsequent elements, where n denotes the number of attributes/dimensions.
     */
    public LinkedList<float[]> _normalizedLowerLine = null;

    /**
     * List of contiguous x-coordinates for the polygon representing the envelope in the rendering space.
     */
    public LinkedList<int[]> _projectedXCoords = null;

    /**
     * List of contiguous x-coordinates for the polygon representing the envelope in the rendering space.
     */
    public LinkedList<int[]> _projectedYCoords = null;

    /**
     * Can be called to restart IDS.
     */
    @Override
    public void reset()
    {
        super.reset();
        _noEnvelopePointsInContiguousLines = null;
        _projectedXCoords = null;
        _normalizedUpperLine = null;
        _projectedYCoords = null;
        _normalizedLowerLine = null;
    }
}
