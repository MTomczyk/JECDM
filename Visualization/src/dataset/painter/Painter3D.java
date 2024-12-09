package dataset.painter;

import color.Color;
import com.jogamp.opengl.GL2;
import dataset.painter.glutils.*;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Line;
import dataset.painter.style.enums.Marker;
import gl.IVBOComponent;
import gl.VBOManager;
import gl.vboutils.BufferData;
import gl.vboutils.Icosphere;
import space.Dimension;
import thread.swingworker.EventTypes;

import java.util.LinkedList;
import java.util.ListIterator;


/**
 * Default implementation of {@link IPainter} for OpenGL-based 3D rendering.
 *
 * @author MTomczyk
 */
public class Painter3D extends AbstractPainter implements IPainter, IVBOComponent
{
    /**
     * Reference to the projection data (additional reference is kept to avoid casting).
     */
    private IDS3D _3dProjection;

    /**
     * VBO for markers (fill).
     */
    protected VBOManager _markerFills;

    /**
     * VBO for markers (edges).
     */
    protected VBOManager _markerEdges;

    /**
     * VBO for markers (fill).One marker for one contiguous line.
     */
    protected VBOManager[] _lines;

    /**
     * If true, the fourth channel is used when defining the colors. False otherwise.
     */
    private final boolean _useAlpha;

    /**
     * Color array stride.
     */
    private final int _colorStride;

    /**
     * Parameterized constructor.
     *
     * @param ms       marker style
     * @param ls       line style
     * @param useAlpha if true, the fourth channel is used when defining the colors
     */
    public Painter3D(MarkerStyle ms, LineStyle ls, boolean useAlpha)
    {
        super(ms, ls);
        _useAlpha = useAlpha;
        if (useAlpha) _colorStride = 4;
        else _colorStride = 3;
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
        return new Painter3D(ms, ls, _useAlpha);
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
    protected void fillProjectedPoint(float[] projectedArray, int projectedOffset,
                                      float[] normalizedArray, int normalizedOffset,
                                      Dimension[] dimensions, int pSize)
    {
        projectedArray[projectedOffset] = (float) (dimensions[0]._position + normalizedArray[normalizedOffset +
                _IDS._drIdx_to_flatAttIdx[0]] * (dimensions[0]._size));
        projectedArray[projectedOffset + 1] = (float) (dimensions[1]._position + normalizedArray[normalizedOffset +
                _IDS._drIdx_to_flatAttIdx[1]] * (dimensions[1]._size));
        projectedArray[projectedOffset + 2] = (float) (dimensions[2]._position + // swap on Z
                (1.0f - normalizedArray[normalizedOffset + _IDS._drIdx_to_flatAttIdx[2]]) * (dimensions[2]._size));
    }

    /**
     * Auxiliary method for constructing the projection data.
     */
    @Override
    protected void instantiateProjections()
    {
        _3dProjection = new IDS3D(); // shared reference
        _IDS = _3dProjection;
    }

    /**
     * IDS = Internal Data Set Structures = data structures optimized for rendering.
     * Can be called to construct third-level IDS (e.g., data for VBOs when rendering in 3D)
     *
     * @param eventType event type that triggered the method execution
     */
    @Override
    public void updateThirdLevelIDS(EventTypes eventType)
    {
        long startTime = 0;
        if (_measureRecalculateIDSTimes[2]) startTime = System.nanoTime();

        if ((eventType.equals(EventTypes.ON_DATA_CHANGED)) || (eventType.equals(EventTypes.ON_DEMAND)))
        {
            instantiateMarkerFillData();
            instantiateMarkerEdgeEdge();
            instantiateLineData();
            if (eventType.equals(EventTypes.ON_DATA_CHANGED)) createBuffers();
            else updateBuffers();
        }


        if (_measureRecalculateIDSTimes[2]) _IDSRecalculationTimes[2].addData(System.nanoTime() - startTime);
    }

    /**
     * Auxiliary method instantiating data related to marker fills.
     */
    protected void instantiateMarkerFillData()
    {
        if (_3dProjection == null) return;
        if (_3dProjection._noMarkerPoints <= 0) return;
        if (_ms == null) return;
        if (!_ms.isToBeFilled()) return;

        int totalIndices;
        boolean intIndices = false;
        float[] v;
        int[] ii = null;
        short[] is = null;
        float[] c = null;

        if (_ms._style.equals(Marker.CUBE_3D))
        {
            // no walls * no triangles * no points
            totalIndices = _3dProjection._noMarkerPoints * 6 * 2 * 3;
            if (_3dProjection._noMarkerPoints * 8 > Short.MAX_VALUE) intIndices = true;
            v = new float[_3dProjection._noMarkerPoints * 8 * 3]; // no points per cube * no dimensions
            if (intIndices) ii = new int[totalIndices];
            else is = new short[totalIndices];
            if (!_ms._color.isMonoColor()) c = new float[_3dProjection._noMarkerPoints * 8 * _colorStride];
            Cube.fillCubeFillData(v, ii, is, c, _useAlpha, _3dProjection, _ms);
        }
        else if ((_ms._style.equals(Marker.TETRAHEDRON_UP_3D)) || (_ms._style.equals(Marker.TETRAHEDRON_DOWN_3D)) ||
                (_ms._style.equals(Marker.TETRAHEDRON_LEFT_3D)) || (_ms._style.equals(Marker.TETRAHEDRON_RIGHT_3D))
                || (_ms._style.equals(Marker.TETRAHEDRON_FRONT_3D)) || (_ms._style.equals(Marker.TETRAHEDRON_BACK_3D)))
        {
            // no triangles * no points
            totalIndices = _3dProjection._noMarkerPoints * 4 * 3;
            if (_3dProjection._noMarkerPoints * 4 > Short.MAX_VALUE) intIndices = true;
            v = new float[_3dProjection._noMarkerPoints * 4 * 3]; // no points per cube * no dimensions
            if (intIndices) ii = new int[totalIndices];
            else is = new short[totalIndices];
            if (!_ms._color.isMonoColor()) c = new float[_3dProjection._noMarkerPoints * 4 * _colorStride];
            Tetrahedron.fillTetrahedronFillData(v, ii, is, c, _useAlpha, _3dProjection, _ms);
        }
        else if ((_ms._style.equals(Marker.SPHERE_LOW_POLY_3D)) || (_ms._style.equals(Marker.SPHERE_MEDIUM_POLY_3D)) ||
                (_ms._style.equals(Marker.SPHERE_HIGH_POLY_3D)))
        {
            int polyLevel = 0;
            if (_ms._style.equals(Marker.SPHERE_MEDIUM_POLY_3D)) polyLevel = 1;
            else if (_ms._style.equals(Marker.SPHERE_HIGH_POLY_3D)) polyLevel = 2;

            int vs = _3dProjection._noMarkerPoints * Icosphere._NO_VERTICES[polyLevel];
            totalIndices = _3dProjection._noMarkerPoints * Icosphere._NO_INDICES_FILL[polyLevel];
            if (vs > Short.MAX_VALUE) intIndices = true;
            v = new float[vs * 3]; // no points per cube * no dimensions
            if (intIndices) ii = new int[totalIndices];
            else is = new short[totalIndices];
            if (!_ms._color.isMonoColor()) c = new float[vs * _colorStride];
            dataset.painter.glutils.Icosphere.fillIcosphereFillData(v, ii, is, c, _useAlpha, _3dProjection, _ms, polyLevel);
        }
        else // points by default
        {
            totalIndices = _3dProjection._noMarkerPoints;
            if (_3dProjection._noMarkerPoints > Short.MAX_VALUE) intIndices = true;
            v = new float[totalIndices * 3];
            if (intIndices) ii = new int[totalIndices];
            else is = new short[totalIndices];
            if (!_ms._color.isMonoColor()) c = new float[totalIndices * _colorStride];
            Point.fillPointFillData(v, ii, is, c, _useAlpha, _3dProjection);
        }

        // create buffer
        if (intIndices) _3dProjection._markersFillsBuffer = new BufferData(v, ii, c);
        else _3dProjection._markersFillsBuffer = new BufferData(v, is, c);
    }

    /**
     * Auxiliary method instantiating data related to marker fills.
     */
    @SuppressWarnings("DuplicatedCode")
    protected void instantiateMarkerEdgeEdge()
    {
        if (_3dProjection == null) return;
        if (_3dProjection._noMarkerPoints <= 0) return;
        if (_ms == null) return;
        if (!_ms.areEdgesToBeDrawn()) return;

        int totalIndices;
        boolean intIndices = false;
        float[] v = null;
        int[] ii = null;
        short[] is = null;
        float[] c = null;

        // point style do not use edges
        if (_ms._style.equals(Marker.CUBE_3D))
        {
            // no walls * no triangles * no points
            totalIndices = _3dProjection._noMarkerPoints * 12 * 2;
            if (_3dProjection._noMarkerPoints * 8 > Short.MAX_VALUE) intIndices = true;
            v = new float[_3dProjection._noMarkerPoints * 8 * 3]; // no points per cube * no dimensions
            if (intIndices) ii = new int[totalIndices];
            else is = new short[totalIndices];
            if (!_ms._edge._color.isMonoColor()) c = new float[_3dProjection._noMarkerPoints * 8 * _colorStride];
            Cube.fillCubeEdgesData(v, ii, is, c, _useAlpha, _3dProjection, _ms);
        }
        else if ((_ms._style.equals(Marker.TETRAHEDRON_UP_3D)) || (_ms._style.equals(Marker.TETRAHEDRON_DOWN_3D)) ||
                (_ms._style.equals(Marker.TETRAHEDRON_LEFT_3D)) || (_ms._style.equals(Marker.TETRAHEDRON_RIGHT_3D))
                || (_ms._style.equals(Marker.TETRAHEDRON_FRONT_3D)) || (_ms._style.equals(Marker.TETRAHEDRON_BACK_3D)))
        {
            // no triangles * no points
            totalIndices = _3dProjection._noMarkerPoints * 6 * 2;
            if (_3dProjection._noMarkerPoints * 8 > Short.MAX_VALUE) intIndices = true;
            v = new float[_3dProjection._noMarkerPoints * 8 * 3]; // no points per cube * no dimensions
            if (intIndices) ii = new int[totalIndices];
            else is = new short[totalIndices];
            if (!_ms._edge._color.isMonoColor()) c = new float[_3dProjection._noMarkerPoints * 8 * _colorStride];
            Tetrahedron.fillTetrahedronEdgesData(v, ii, is, c, _useAlpha, _3dProjection, _ms);
        }
        else if ((_ms._style.equals(Marker.SPHERE_LOW_POLY_3D)) || (_ms._style.equals(Marker.SPHERE_MEDIUM_POLY_3D)) ||
                (_ms._style.equals(Marker.SPHERE_HIGH_POLY_3D)))
        {
            int polyLevel = 0;
            if (_ms._style.equals(Marker.SPHERE_MEDIUM_POLY_3D)) polyLevel = 1;
            else if (_ms._style.equals(Marker.SPHERE_HIGH_POLY_3D)) polyLevel = 2;
            int vs = _3dProjection._noMarkerPoints * Icosphere._NO_VERTICES[polyLevel];
            totalIndices = _3dProjection._noMarkerPoints * Icosphere._NO_INDICES_EDGES[polyLevel];
            if (vs > Short.MAX_VALUE) intIndices = true;

            v = new float[vs * 3]; // no points per cube * no dimensions
            if (intIndices) ii = new int[totalIndices];
            else is = new short[totalIndices];
            if (!_ms._edge._color.isMonoColor()) c = new float[vs * _colorStride];
            dataset.painter.glutils.Icosphere.fillIcosphereEdgesData(v, ii, is, c, _useAlpha, _3dProjection, _ms, polyLevel);
        }

        // create buffer
        if (v != null)
        {
            if (intIndices) _3dProjection._markersEdgesBuffer = new BufferData(v, ii, c);
            else _3dProjection._markersEdgesBuffer = new BufferData(v, is, c);
        }
        else _3dProjection._markersEdgesBuffer = null;
    }

    /**
     * Auxiliary method instantiating data related to marker fills.
     */
    @SuppressWarnings("DuplicatedCode")
    protected void instantiateLineData()
    {
        if (_3dProjection == null) return;
        if (_3dProjection._noLinePoints <= 0) return;
        if (_ls == null) return;
        if (!_ls.isDrawable()) return;

        boolean gradient = !_ls._color.isMonoColor();

        ListIterator<Integer> noPointsIt = _IDS._noLinePointsInContiguousLines.listIterator();
        ListIterator<Color[]> colorsIterator = null;
        ListIterator<int[]> noAuxPointsIt = null;
        ListIterator<float[]> auxLinesIt = null;
        ListIterator<Color[]> auxColorsIt = null;

        if (gradient)
        {
            colorsIterator = _IDS._lineGradientColors.listIterator();
            noAuxPointsIt = _IDS._auxProjectedLinesNoPoints.listIterator();
            auxLinesIt = _IDS._auxProjectedContiguousLines.listIterator();
            auxColorsIt = _IDS._auxLineGradientColors.listIterator();
        }

        LinkedList<BufferData> buffers = new LinkedList<>();

        float lineRadius = _ls._size / 2.0f;

        for (float[] cLine : _IDS._projectedContiguousLines)
        {
            BufferData bd;
            if (_ls._style.equals(Line.POLY_QUAD)) bd = PolyLine.getPolyQuadLineData(cLine, noPointsIt, colorsIterator,
                    noAuxPointsIt, auxLinesIt, auxColorsIt, gradient, _colorStride, _useAlpha, lineRadius);
            else if (_ls._style.equals(Line.POLY_OCTO)) bd = PolyLine.getPolyOctoLineData(cLine, noPointsIt,
                    colorsIterator, noAuxPointsIt, auxLinesIt, auxColorsIt, gradient, _colorStride, _useAlpha, lineRadius);
            else bd = RegularLine.getRegularLineData(cLine, noPointsIt, colorsIterator, noAuxPointsIt, auxLinesIt,
                        auxColorsIt, gradient, _colorStride, _useAlpha);

            buffers.add(bd);
        }

        _3dProjection._linesBuffer = new BufferData[buffers.size()];
        int pnt = 0;
        for (BufferData bd : buffers) _3dProjection._linesBuffer[pnt++] = bd;
    }

    /**
     * Can be called to instantiate VBO buffers (but the data is not yet transferred to GPU).
     */
    @Override
    public void createBuffers()
    {
        if (_3dProjection._markersFillsBuffer != null)
        {
            _markerFills = _3dProjection._markersFillsBuffer.createVBOManager(
                    getMarkerFillsRenderingMode(_ms), 3, _colorStride);
        }
        if (_3dProjection._markersEdgesBuffer != null)
            _markerEdges = _3dProjection._markersEdgesBuffer.createVBOManager(
                    getMarkerEdgesRenderingMode(_ms), 3, _colorStride);

        if (_3dProjection._linesBuffer != null)
        {
            _lines = new VBOManager[_3dProjection._linesBuffer.length];
            int renderingMode = getLinesRenderingMode(_ls);
            for (int i = 0; i < _3dProjection._linesBuffer.length; i++)
                _lines[i] = _3dProjection._linesBuffer[i].createVBOManager(renderingMode, 3, _colorStride);
        }
    }

    /**
     * Can be called to instantiate VBO buffers on the GPU.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void executeInitialDataTransfer(GL2 gl)
    {
        if (_markerFills != null) _markerFills.initialDataTransfer(gl);
        if (_markerEdges != null) _markerEdges.initialDataTransfer(gl);
        if (_lines != null) for (VBOManager l : _lines) l.initialDataTransfer(gl);
    }


    /**
     * Can be called to update data in the VBO that has already been instantiated and sent to GPU  (but the updated is not yet transferred to GPU).
     */
    @Override
    public void updateBuffers()
    {
        if (_3dProjection._markersFillsBuffer != null)
            _markerFills.initDataUpdate(_3dProjection._markersFillsBuffer, 3, _colorStride);
        if (_3dProjection._markersEdgesBuffer != null)
            _markerEdges.initDataUpdate(_3dProjection._markersEdgesBuffer, 3, _colorStride);
        if (_3dProjection._linesBuffer != null)
            for (int i = 0; i < _3dProjection._linesBuffer.length; i++)
                _lines[i].initDataUpdate(_3dProjection._linesBuffer[i], 3, _colorStride);
    }


    /**
     * Auxiliary method returning the rendering mode for marker fill (GL_LINES,etc.) based on the marker style.
     *
     * @param ms marker style
     * @return rendering mode
     */
    protected int getMarkerFillsRenderingMode(MarkerStyle ms)
    {
        if (ms._style.equals(Marker.CUBE_3D)) return GL2.GL_TRIANGLES;
        if (ms._style.equals(Marker.TETRAHEDRON_UP_3D)) return GL2.GL_TRIANGLES;
        if (ms._style.equals(Marker.TETRAHEDRON_DOWN_3D)) return GL2.GL_TRIANGLES;
        if (ms._style.equals(Marker.TETRAHEDRON_LEFT_3D)) return GL2.GL_TRIANGLES;
        if (ms._style.equals(Marker.TETRAHEDRON_RIGHT_3D)) return GL2.GL_TRIANGLES;
        if (ms._style.equals(Marker.TETRAHEDRON_FRONT_3D)) return GL2.GL_TRIANGLES;
        if (ms._style.equals(Marker.TETRAHEDRON_BACK_3D)) return GL2.GL_TRIANGLES;
        if (ms._style.equals(Marker.SPHERE_LOW_POLY_3D)) return GL2.GL_TRIANGLES;
        if (ms._style.equals(Marker.SPHERE_MEDIUM_POLY_3D)) return GL2.GL_TRIANGLES;
        if (ms._style.equals(Marker.SPHERE_HIGH_POLY_3D)) return GL2.GL_TRIANGLES;
        return GL2.GL_POINTS;
    }

    /**
     * Auxiliary method returning the rendering mode for marker edges (GL_LINES,etc.) based on the marker style.
     *
     * @param ms marker style
     * @return rendering mode
     */
    protected int getMarkerEdgesRenderingMode(MarkerStyle ms)
    {
        if (ms._style.equals(Marker.CUBE_3D)) return GL2.GL_LINES;
        if (ms._style.equals(Marker.TETRAHEDRON_UP_3D)) return GL2.GL_LINES;
        if (ms._style.equals(Marker.TETRAHEDRON_DOWN_3D)) return GL2.GL_LINES;
        if (ms._style.equals(Marker.TETRAHEDRON_LEFT_3D)) return GL2.GL_LINES;
        if (ms._style.equals(Marker.TETRAHEDRON_RIGHT_3D)) return GL2.GL_LINES;
        if (ms._style.equals(Marker.TETRAHEDRON_FRONT_3D)) return GL2.GL_LINES;
        if (ms._style.equals(Marker.TETRAHEDRON_BACK_3D)) return GL2.GL_LINES;
        if (ms._style.equals(Marker.SPHERE_LOW_POLY_3D)) return GL2.GL_LINES;
        if (ms._style.equals(Marker.SPHERE_MEDIUM_POLY_3D)) return GL2.GL_LINES;
        if (ms._style.equals(Marker.SPHERE_HIGH_POLY_3D)) return GL2.GL_LINES;
        return -1;
    }

    /**
     * Auxiliary method returning the rendering mode for lines (GL_LINES,etc.) based on the line style used.
     *
     * @param ls marker style
     * @return rendering mode
     */
    protected int getLinesRenderingMode(LineStyle ls)
    {
        if (ls._style.equals(Line.POLY_QUAD)) return GL2.GL_TRIANGLES;
        if (ls._style.equals(Line.POLY_OCTO)) return GL2.GL_TRIANGLES;
        if (ls._style.equals(Line.REGULAR)) return GL2.GL_LINE_STRIP;
        return -1;
    }


    /**
     * Setter for the renderer.
     *
     * @param g renderer object
     */
    @Override
    public void setRenderer(Object g)
    {
        // do not use
    }

    /**
     * Called at the end of the rendering to release the renderer (e.g., Java AWT Graphics dispose()).
     */
    @Override
    public void releaseRenderer()
    {
        // do not use
    }


    /**
     * Used for drawing lines.
     */
    @Override
    protected void drawLines()
    {
        // do not use
    }


    /**
     * Used for drawing lines.
     */
    @Override
    protected void drawMarkers()
    {
        // do not use
    }

    /**
     * Can be called to draw an object.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void draw(GL2 gl)
    {
        if (_ds.isRenderingSkipped()) return;

        if (_lines != null)
        {
            if (_ls._color.isMonoColor()) setConstantColor(gl, _ls._color.getColor(0.0f));
            if (_ls._style.equals(Line.REGULAR))
            {
                if (_ls._relativeSize != null) gl.glLineWidth(_ls._relativeSize.getSize(_GC, _PC, _ls._size));
                else gl.glLineWidth(_ls._size);
            }
            for (VBOManager l : _lines) l.render(gl);
        }
        if (_markerFills != null)
        {
            if (_ms._color.isMonoColor()) setConstantColor(gl, _ms._color.getColor(0.0f));
            if (_ms._style == Marker.POINT_3D)
            {
                if (_ms._relativeSize != null) gl.glPointSize(_ms._relativeSize.getSize(_GC, _PC, _ms._size));
                else gl.glPointSize(_ms._size);
            }
            _markerFills.render(gl);
        }
        if (_markerEdges != null)
        {
            if (_ms._edge._color.isMonoColor()) setConstantColor(gl, _ms._edge._color.getColor(0.0f));
            gl.glLineWidth(_ms._edge._size);
            _markerEdges.render(gl);
        }
    }

    /**
     * Auxiliary method setting constant (non-gradient) color.
     *
     * @param gl current OpenGL rendering context
     * @param c  gradient color
     */
    protected void setConstantColor(GL2 gl, Color c)
    {
        if (_useAlpha) gl.glColor4f(c._r, c._g, c._b, c._a);
        else gl.glColor3f(c._r, c._g, c._b);
    }

    /**
     * The method returns true if some of the wrapped buffers need to be sent to the GPU for the first time.
     *
     * @return true, if the update is required, false = otherwise.
     */
    @Override
    public boolean isInitialUpdateRequested()
    {
        if (_markerFills != null) return _markerFills.isInitializationRequested();
        if (_markerEdges != null) return _markerEdges.isInitializationRequested();
        if (_lines != null) for (VBOManager l : _lines) if (l.isInitializationRequested()) return true;
        return false;
    }


    /**
     * The method returns true if some of the wrapped buffers were updated and the data needs to be sent to GPU.
     *
     * @return true, if the update is required, false = otherwise.
     */
    @Override
    public boolean isUpdateRequested()
    {
        if ((_markerFills != null) && (_markerFills.isUpdateRequested())) return true;
        if ((_markerEdges != null) && (_markerEdges.isUpdateRequested())) return true;
        if (_lines != null) for (VBOManager l : _lines) if (l.isUpdateRequested()) return true;
        return false;
    }

    /**
     * Can be called to update the VBO that is already instantiated in GPU.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void executeUpdate(GL2 gl)
    {
        if (_markerFills != null) _markerFills.updateData(gl);
        if (_markerEdges != null) _markerEdges.updateData(gl);
        if (_lines != null) for (VBOManager l : _lines) l.updateData(gl);
    }


    /**
     * Can be called to remove GL-related data.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void dispose(GL2 gl)
    {
        if (_markerFills != null) _markerFills.dispose(gl);
        if (_markerEdges != null) _markerEdges.dispose(gl);
        if (_lines != null) for (VBOManager l : _lines) l.dispose(gl);
        _markerFills = null;
        _markerEdges = null;
        _lines = null;
    }

    /**
     * Can be called to check whether the buffer objects have not been created yet.
     *
     * @return true, buffer objects have not been created yet.
     */
    @Override
    public boolean areVBOsNull()
    {
        if ((_ms != null) && (_ms.isToBeFilled()) && (_markerFills == null)) return true;
        if ((_ms != null) && (_ms.areEdgesToBeDrawn()) && (_markerEdges == null)) return true;
        return (_ls != null) && (_ls.isDrawable()) && (_lines == null);
    }

}
