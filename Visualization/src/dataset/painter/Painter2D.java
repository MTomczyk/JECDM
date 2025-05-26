package dataset.painter;

import color.Color;
import dataset.painter.arrowsutils.IArrowProjectionDataConstructor;
import dataset.painter.arrowsutils.Triangular2D;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Arrow;
import dataset.painter.style.enums.Marker;
import space.Dimension;
import utils.DrawUtils;
import utils.Projection;

import java.awt.*;
import java.util.ListIterator;


/**
 * Default implementation of {@link IPainter} for Java AWT-based 2D rendering.
 *
 * @author MTomczyk
 */
public class Painter2D extends AbstractPainter implements IPainter
{
    /**
     * Auxiliary interface for classes responsible for performing params container adjustments.
     */
    public interface IParamsAdjuster
    {
        /**
         * The main method.
         *
         * @param p params container to be adjusted
         */
        void adjust(Params p);
    }


    /**
     * Params container
     */
    public static class Params extends AbstractPainter.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param ms marker style
         * @param ls line style
         */
        public Params(MarkerStyle ms, LineStyle ls)
        {
            this(ms, ls, null);
        }


        /**
         * Parameterized constructor.
         *
         * @param ms marker style
         * @param ls line style
         * @param as arrow styles (beginning and ending)
         */
        public Params(MarkerStyle ms, LineStyle ls, ArrowStyles as)
        {
            this(ms, ls, as, false, 0.005f);
        }

        /**
         * Parameterized constructor.
         *
         * @param ms                           marker style
         * @param ls                           line style
         * @param as                           arrow styles (beginning and ending)
         * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
         *                                     each double [][] data segment as one contiguous line (when using a line style),
         *                                     the data is considered to be a series of independent lines whose coordinates
         *                                     occupy each subsequent pair of double [] vectors in the data segment
         * @param gradientLineMinSegmentLength Determines the minimal segment line used when constructing gradient line
         *                                     (discretization level, the lower the value, the greater the discretization
         *                                     but also computational resources used); the interpretation is
         *                                     implementation-dependent; default: percent value of an average screen
         *                                     dimension (in pixels)
         */
        public Params(MarkerStyle ms, LineStyle ls, ArrowStyles as, boolean treatContiguousLinesAsBroken,
                      float gradientLineMinSegmentLength)
        {
            super(ms, ls, as, treatContiguousLinesAsBroken, gradientLineMinSegmentLength);
        }
    }


    /**
     * Java awt graphics context.
     */
    protected Graphics _G;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public Painter2D(Params p)
    {
        super(p);
    }

    /**
     * The method clones the painter but ignores the rendering data.
     * The method supports data replacing.
     *
     * @return cloned painter (except for rendering data).
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public IPainter getEmptyClone()
    {
        MarkerStyle ms = null;
        if (_ms != null) ms = _ms.getClone();
        LineStyle ls = null;
        if (_ls != null) ls = _ls.getClone();
        ArrowStyles as = null;
        if (_as != null) as = _as.getClone();
        return new Painter2D(new Params(ms, ls, as, _treatContiguousLinesAsBroken, _gradientLineMinSegmentLength));
    }

    /**
     * Setter for the renderer.
     *
     * @param g renderer object
     */
    @Override
    public void setRenderer(Object g)
    {
        assert g instanceof Graphics;
        _G = ((Graphics) g).create();
    }

    /**
     * Called at the end of the rendering to release the renderer (e.g., Java AWT Graphics dispose()).
     */
    @Override
    public void releaseRenderer()
    {
        _G.dispose();
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
        projectedArray[projectedOffset] = (float) (dimensions[0]._position +
                normalizedArray[normalizedOffset + _IDS._drIdx_to_flatAttIdx[0]] * (dimensions[0]._size - 1));
        projectedArray[projectedOffset + 1] = (float) (dimensions[1]._position +
                (1.0f - normalizedArray[normalizedOffset + _IDS._drIdx_to_flatAttIdx[1]]) * (dimensions[1]._size - 1));
    }

    /**
     * Auxiliary method that initializes arrow projection data constructor.
     *
     * @param as arrow style
     * @return arrow projection data constructor
     */
    @Override
    protected IArrowProjectionDataConstructor getArrowDataConstructor(ArrowStyle as)
    {
        if ((as._arrow.equals(Arrow.TRIANGULAR_2D)) || (as._arrow.equals(Arrow.TRIANGULAR_REVERSED_2D)))
            return new Triangular2D();
        else return null;
    }

    /**
     * Used for drawing markers.
     */
    @Override
    protected void drawMarkers()
    {
        if (_ms == null) return;

        if (_IDS == null) return;
        if (_IDS._noMarkerPoints == 0) return;
        if (_IDS._projectedMarkers == null) return;

        float size = _ms.calculateRelativeSize(_GC, _PC);
        if (Float.compare(size, 0.5f) <= 0) size = 0.5f;

        if (_ms.isToBeFilled())
        {
            if (_ms._color.isMonoColor())
            {
                Color c = _ms._color.getColor(0.0f);
                _G.setColor(c);
            }

            int offset = 0;
            for (int i = 0; i < _IDS._noMarkerPoints; i++)
            {
                if (!_ms._color.isMonoColor()) _G.setColor(_IDS._markerFillGradientColors[i]);
                drawMarkerFill(offset, size);
                offset += _IDS._pSize;
            }
        }

        if (_ms.areEdgesToBeDrawn())
        {
            Graphics2D g2 = (Graphics2D) _G;
            float width = _ms._edge.calculateRelativeSize(_GC, _PC);
            g2.setStroke(DrawUtils.constructRescaledStroke(width / _ms._edge._stroke.getLineWidth(), _ms._edge._stroke));

            if (_ms._edge._color.isMonoColor())
            {
                Color c = _ms._edge._color.getColor(0.0f);
                _G.setColor(c);
            }

            int offset = 0;
            for (int i = 0; i < _IDS._noMarkerPoints; i++)
            {
                if (!_ms._edge._color.isMonoColor()) _G.setColor(_IDS._markerEdgeGradientColors[i]);
                drawMarkerEdge(offset, size);
                offset += _IDS._pSize;
            }
        }
    }

    /**
     * Used for drawing arrows.
     */
    @Override
    protected void drawArrows()
    {
        if (_IDS._noLinesWithArrows == 0) return;
        if (_as == null) return;

        drawArrowPolygon(_IDS._baIDS, _as._bas);
        drawArrowPolygon(_IDS._eaIDS, _as._eas);
    }

    /**
     * Auxiliary method for drawing arrows begins and ends (polygons).
     *
     * @param idsArrows IDS for arrows
     * @param as        arrow style
     */
    protected void drawArrowPolygon(IDSArrows idsArrows, ArrowStyle as)
    {
        if ((idsArrows != null) && (idsArrows._arrowProjectedData != null))
        {
            boolean mc = as._color.isMonoColor();
            if (mc) _G.setColor(as._color.getColor(0.0f));
            int idx = 0;
            for (int offset = 0; offset < idsArrows._arrowProjectedData.length; offset += idsArrows._stride)
            {
                if (!mc) _G.setColor(idsArrows._arrowFillColors[idx]);
                int[] x = new int[idsArrows._verts];
                int[] y = new int[idsArrows._verts];
                int off = 0;
                for (int i = 0; i < idsArrows._verts; i++)
                {
                    x[i] = Projection.getP(idsArrows._arrowProjectedData[offset + off]);
                    y[i] = Projection.getP(idsArrows._arrowProjectedData[offset + off + 1]);
                    off += 2;
                }
                idx++;
                _G.fillPolygon(x, y, idsArrows._verts);
            }
        }
    }


    /**
     * Supportive method for rendering marker (fill only).
     *
     * @param offset offset pointing to a projected point in the projected array
     * @param size   relative size
     */
    protected void drawMarkerFill(int offset, float size)
    {
        int x = Projection.getP(_IDS._projectedMarkers[offset]);
        int y = Projection.getP(_IDS._projectedMarkers[offset + 1]);
        drawMarkerFill(x, y, size);
    }

    /**
     * Auxiliary method for drawing marker fill given the coordinates
     *
     * @param x    x-coordinate
     * @param y    y-coordinate
     * @param size size
     */
    protected void drawMarkerFill(float x, float y, float size)
    {
        if (_ms == null) return;
        if (_ms._style == Marker.SQUARE) DrawUtils.drawSquare(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.CIRCLE) DrawUtils.drawCircle(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.TRIANGLE_UP) DrawUtils.drawTriangleUp(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.TRIANGLE_DOWN)
            DrawUtils.drawTriangleDown(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.TRIANGLE_LEFT)
            DrawUtils.drawTriangleLeft(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.TRIANGLE_RIGHT)
            DrawUtils.drawTriangleRight(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.PENTAGON) DrawUtils.drawPentagon(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.STAR) DrawUtils.drawStar(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.HEXAGON_HOR) DrawUtils.drawHexagonHor(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.HEXAGON_VERT) DrawUtils.drawHexagonVert(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.DIAMOND_HOR) DrawUtils.drawDiamondHor(_G, x, y, size, null, null, true, false);
        else if (_ms._style == Marker.DIAMOND_VERT) DrawUtils.drawDiamondVert(_G, x, y, size, null, null, true, false);
    }


    /**
     * Supportive method for rendering marker (edge only).
     *
     * @param offset offset pointing to a projected point in the projected array
     * @param size   relative size
     */
    protected void drawMarkerEdge(int offset, float size)
    {
        int x = Projection.getP(_IDS._projectedMarkers[offset]);
        int y = Projection.getP(_IDS._projectedMarkers[offset + 1]);
        drawMarkerEdge(x, y, size);
    }


    /**
     * Auxiliary method for drawing marker edge given the coordinates
     *
     * @param x    x-coordinate
     * @param y    y-coordinate
     * @param size size
     */
    protected void drawMarkerEdge(float x, float y, float size)
    {
        if (_ms == null) return;
        if (_ms._style == Marker.SQUARE) DrawUtils.drawSquare(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.CIRCLE) DrawUtils.drawCircle(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.TRIANGLE_UP) DrawUtils.drawTriangleUp(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.TRIANGLE_DOWN)
            DrawUtils.drawTriangleDown(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.TRIANGLE_LEFT)
            DrawUtils.drawTriangleLeft(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.TRIANGLE_RIGHT)
            DrawUtils.drawTriangleRight(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.PENTAGON) DrawUtils.drawPentagon(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.STAR) DrawUtils.drawStar(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.HEXAGON_HOR) DrawUtils.drawHexagonHor(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.HEXAGON_VERT) DrawUtils.drawHexagonVert(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.DIAMOND_HOR) DrawUtils.drawDiamondHor(_G, x, y, size, null, null, false, true);
        else if (_ms._style == Marker.DIAMOND_VERT) DrawUtils.drawDiamondVert(_G, x, y, size, null, null, false, true);
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
        if (_IDS._projectedContiguousLines == null) return;
        boolean gradient = !_ls._color.isMonoColor();
        if ((gradient) && (_IDS._lineGradientColors == null)) return;

        Graphics2D g2 = (Graphics2D) _G;
        float width = _ls.calculateRelativeSize(_GC, _PC);
        g2.setStroke(DrawUtils.constructRescaledStroke(width / _ls._stroke.getLineWidth(), _ls._stroke));

        ListIterator<Color[]> colorIt = null;
        Color[] gradientColors = null;
        if (!gradient) _G.setColor(_ls._color.getColor(0.0f));
        else colorIt = _IDS._lineGradientColors.listIterator();

        ListIterator<int[]> auxNoPointsIt = null;
        ListIterator<float[]> auxLinesIt = null;
        ListIterator<Color[]> auxLineGradientColorsIt = null;
        int[] auxNoPoint = null;
        float[] auxLines = null;
        Color[] auxLineGradientColors = null;

        if (gradient)
        {
            auxNoPointsIt = _IDS._auxProjectedLinesNoPoints.listIterator();
            auxLinesIt = _IDS._auxProjectedContiguousLines.listIterator();
            auxLineGradientColorsIt = _IDS._auxLineGradientColors.listIterator();
        }

        for (float[] lines : _IDS._projectedContiguousLines)
        {
            if (gradient)
            {
                auxNoPoint = auxNoPointsIt.next();
                auxLines = auxLinesIt.next();
                auxLineGradientColors = auxLineGradientColorsIt.next();
            }

            int auxIdx = 0;
            int auxOffset = 0;
            int auxColorIdx = 0;
            int pOffset = 0;
            int colorIdx = 0;

            int move = _IDS._pSize;
            // If the interpretation is changed, make twice bigger jumps
            if (_treatContiguousLinesAsBroken) move *= 2;

            if (gradient) gradientColors = colorIt.next();

            for (int offset = _IDS._pSize; offset < lines.length; offset += move)
            {
                if (gradient)
                {
                    _G.setColor(gradientColors[colorIdx]); // left point color is assumed to be assigned for the line segment

                    if (auxNoPoint[auxIdx] == 0) drawLine(lines, pOffset, offset);
                    else
                    {
                        // ============================================================================================
                        drawLine(lines, auxLines, pOffset, auxOffset);
                        // ============================================================================================
                        for (int p = 0; p < auxNoPoint[auxIdx] - 1; p++)
                        {
                            _G.setColor(auxLineGradientColors[auxColorIdx]);
                            drawLine(auxLines, auxOffset, auxOffset + _IDS._pSize);
                            auxColorIdx++;
                            auxOffset += _IDS._pSize;
                        }
                        // ============================================================================================
                        _G.setColor(auxLineGradientColors[auxColorIdx]);
                        drawLine(auxLines, lines, auxOffset, offset);
                        auxOffset += _IDS._pSize;
                        auxColorIdx++;
                    }
                }
                else drawLine(lines, pOffset, offset);

                pOffset += move;
                if (_treatContiguousLinesAsBroken) colorIdx += 2;
                else colorIdx++;
                auxIdx++;
            }
        }
    }


    /**
     * Draws a contiguous line.
     *
     * @param array   projected lines data
     * @param offset1 offset pointing to the first data point
     * @param offset2 offset pointing to the second data point
     */
    protected void drawLine(float[] array, int offset1, int offset2)
    {
        _G.drawLine(Projection.getP(array[offset1]), Projection.getP(array[offset1 + 1]),
                Projection.getP(array[offset2]), Projection.getP(array[offset2 + 1]));
    }


    /**
     * Draws a contiguous line. Data is selected from two arrays.
     *
     * @param array1  projected lines data storing the first point
     * @param array2  projected lines data storing the second point
     * @param offset1 offset pointing to the first data point (in the first array)
     * @param offset2 offset pointing to the second data point (in the second array)
     */
    protected void drawLine(float[] array1, float[] array2, int offset1, int offset2)
    {
        _G.drawLine(Projection.getP(array1[offset1]), Projection.getP(array1[offset1 + 1]),
                Projection.getP(array2[offset2]), Projection.getP(array2[offset2 + 1]));
    }


}
