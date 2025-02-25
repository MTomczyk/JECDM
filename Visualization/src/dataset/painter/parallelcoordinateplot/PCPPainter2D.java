package dataset.painter.parallelcoordinateplot;

import component.axis.swing.AbstractAxis;
import dataset.Data;
import dataset.painter.IDS;
import dataset.painter.IPainter;
import dataset.painter.Painter2D;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import listeners.FrameListener;
import space.Dimension;
import thread.swingworker.EventTypes;
import utils.DrawUtils;
import utils.Projection;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.util.ListIterator;


/**
 * Implementation of {@link IPainter} for painting parallel coordinate lines.
 * Works similarly to the default abstract implementation {@link dataset.painter.AbstractPainter}.
 * The major differences are linked to painting procedures.
 * Instead of considering a point explicitly as 2D point to be rendered, the M-dimensional point
 * is decomposed and drawn as M points in the 2D space (for-loop).
 *
 * @author MTomczyk
 */
public class PCPPainter2D extends Painter2D implements IPainter
{
    /**
     * Reference to the projection data (additional reference is kept to avoid casting).
     */
    private PCPIDS _pcpProjection;

    /**
     * Considered dimensionality (the number of parallel y-axes).
     */
    private final int _dimensions;

    /**
     * Parameterized constructor.
     *
     * @param ms marker style
     * @param ls line style
     * @param dimensions the number of parallel coordinate axes
     */
    public PCPPainter2D(MarkerStyle ms, LineStyle ls, int dimensions)
    {
        super(ms, ls);
        _dimensions = dimensions;
    }

    /**
     * Auxiliary method setting the minimum number of line data points required to draw a line (2 by default)
     */
    @Override
    protected void instantiateMinNoLinePointsRequired()
    {
        _minNoLinePointsRequired = 1;
    }


    /**
     * The method clones the painter but ignores the rendering data.
     * The method supports data replacing.
     *
     * @return cloned painter (except for rendering data).
     */
    @Override
    public IPainter getEmptyClone()
    {
        MarkerStyle ms = null;
        if (_ms != null) ms = _ms.getClone();
        LineStyle ls = null;
        if (_ls != null) ls = _ls.getClone();
        return new PCPPainter2D(ms, ls, _dimensions);
    }


    /**
     * Auxiliary method for constructing the projection data.
     */
    @Override
    protected void instantiateProjections()
    {
        _pcpProjection = new PCPIDS(); // shared reference
        _IDS = _pcpProjection;
    }

    /**
     * Auxiliary method for determining a size of a single projected entry (e.g., a point).
     * By default, the size equals the dimensionality of the drawing area.
     * Nonetheless, the method can be overwritten to suitably customize the processing.
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     * @return size of a projected entry
     */
    @Override
    protected int getSizeOfAProjectedEntry(Dimension[] dimensions)
    {
        // no projected points (only y + custom) equals the number of attributes that are supposed to be values for each dimension
        return _IDS._noAttributes;
    }

    /**
     * IDS = Internal Data Set Structures = data structures optimized for rendering.
     * Data ({@link Data}) is suitably processed and transformed into projection data {@link IDS} that is
     * easy-to-be-rendered. Second level IDS corresponds to normalization of data points as imposed by the drawing area coordinates.
     * The method should be called when the drawing area changes (see the top-level {@link FrameListener#componentResized(ComponentEvent)} ).
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     * @param eventType  event type that triggered the method execution
     */
    @Override
    public void updateSecondLevelIDS(Dimension[] dimensions, EventTypes eventType)
    {
        super.updateSecondLevelIDS(dimensions, eventType);
        createXProjectionData(dimensions);
    }

    /**
     * Auxiliary method creating x locations for parallel lines (projections).
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     */
    protected void createXProjectionData(Dimension[] dimensions)
    {
        // fill pX
        AbstractAxis xaxis = _PC.getPlot().getComponentsContainer().getAxes()[0];
        _pcpProjection._pT = xaxis.getTicksDataGetter().getTicksLocations().clone();
        _pcpProjection._pX = new float[_pcpProjection._pT.length];
        for (int i = 0; i < _pcpProjection._pT.length; i++)
            _pcpProjection._pX[i] = (float) (dimensions[0]._position + _pcpProjection._pT[i] * (dimensions[0]._size - 1));
    }


    /**
     * Calculates the normalized point (in the display space) and accordingly fills the normalized data array.
     *
     * @param projectedArray   projection array to be filled
     * @param projectedOffset  offset index in the projection array for the projected point
     * @param normalizedArray  normalized array (data source)
     * @param normalizedOffset offset index in the normalized array for the source point
     * @param dimensions       dimensions of the rendering space
     * @param pSize            size of a projected entry
     */
    @Override
    protected void fillProjectedPoint(float[] projectedArray, int projectedOffset,
                                      float[] normalizedArray, int normalizedOffset,
                                      Dimension[] dimensions, int pSize)
    {
        // M-dimensional data
        for (int i = 0; i < _dimensions; i++)
        {
            float p = (float) (dimensions[1]._position + (1.0f - normalizedArray[normalizedOffset + i]) * (dimensions[1]._size - 1));
            projectedArray[projectedOffset + i] = p;
        }

    }

    /**
     * Supportive method for rendering marker (fill only).
     *
     * @param offset offset pointing to a projected point in the projected array
     * @param size   relative size
     */
    @Override
    protected void drawMarkerFill(int offset, float size)
    {
        for (int a = 0; a < _dimensions; a++)  // _IDS._pSize
        {
            int x = Projection.getP(_pcpProjection._pX[a]);
            int y = Projection.getP(_pcpProjection._projectedMarkers[offset + a]);
            drawMarkerFill(x, y, size);
        }
    }


    /**
     * Supportive method for rendering marker (edge only).
     *
     * @param offset offset pointing to a projected point in the projected array
     * @param size   relative size
     */
    @Override
    protected void drawMarkerEdge(int offset, float size)
    {
        for (int a = 0; a < _dimensions; a++) // _IDS._pSize
        {
            int x = Projection.getP(_pcpProjection._pX[a]);
            int y = Projection.getP(_pcpProjection._projectedMarkers[offset + a]);
            drawMarkerEdge(x, y, size);
        }
    }

    /**
     * The method does nothing in this implementation.
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     */
    @Override
    protected void fillGradientLinesAuxData(Dimension[] dimensions)
    {

    }

    /**
     * Used for drawing lines.
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void drawLines()
    {
        if (_ls == null) return;
        if (!_ls.isDrawable()) return;
        if (_pcpProjection._projectedContiguousLines == null) return;

        Graphics2D g2 = (Graphics2D) _G;
        float width = _ls.calculateRelativeSize(_GC, _PC);
        g2.setStroke(DrawUtils.constructRescaledStroke(width / _ls._stroke.getLineWidth(), _ls._stroke));

        boolean gradient = !_ls._color.isMonoColor();

        ListIterator<color.Color[]> colorIt = null;
        color.Color[] gradientColors = null;
        if (!gradient) _G.setColor(_ls._color.getColor(0.0f));
        else colorIt = _pcpProjection._lineGradientColors.listIterator();

        for (float[] lines : _pcpProjection._projectedContiguousLines)
        {
            int colorIdx = 0;

            if (gradient) gradientColors = colorIt.next();

            for (int offset = 0; offset < lines.length; offset += _IDS._pSize)
            {
                if (gradient)
                {
                    _G.setColor(gradientColors[colorIdx]); // left point color is assumed to be assigned for the line segment
                    drawLine(lines, offset);
                }
                else drawLine(lines, offset);
                colorIdx++;
            }
        }
    }

    /**
     * Draws a contiguous line.
     *
     * @param array  projected lines data
     * @param offset offset pointing to the first data point
     */
    private void drawLine(float[] array, int offset)
    {
        for (int i = 0; i < _dimensions - 1; i++)
        {
            _G.drawLine(Projection.getP(_pcpProjection._pX[i]), Projection.getP(array[offset + i]),
                    Projection.getP(_pcpProjection._pX[i + 1]), Projection.getP(array[offset + i + 1]));
        }
    }
}
