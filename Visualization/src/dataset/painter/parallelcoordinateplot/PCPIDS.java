package dataset.painter.parallelcoordinateplot;

import dataset.painter.IDS;


/**
 * Extension of {@link IDS} for handling auxiliary data for displaying parallel coordinate lines.
 *
 * @author MTomczyk
 */
public class PCPIDS extends IDS
{
    /**
     * Projected X-coordinates.
     */
    public float[] _pX = null;

    /**
     * Normalized x-axis ticks locations.
     */
    public float [] _pT = null;


    /**
     * Can be called to restart IDS.
     */
    @Override
    public void reset()
    {
        super.reset();
        _pX = null;
        _pT = null;
    }
}
