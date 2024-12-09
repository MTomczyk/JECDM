package plot.heatmap;

import color.Color;
import color.gradient.Gradient;
import com.jogamp.opengl.GL2;
import component.AbstractVBOComponent;
import container.PlotContainer;
import dataset.painter.style.BucketStyle;
import dataset.painter.style.enums.Bucket;
import drmanager.DisplayRangesManager;
import gl.VBOManager;
import gl.vboutils.BufferData;
import gl.vboutils.Cube;
import gl.vboutils.OffsetStride;
import plot.heatmap.utils.Coords;
import space.Dimension;
import space.normalization.INormalization;
import space.normalization.minmax.AbstractMinMaxNormalization;

/**
 * Additional layer that can be used to display a heatmap (VBO component).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Heatmap3DLayer extends AbstractVBOComponent
{
    /**
     * Heatmap model.
     */
    protected HeatmapLayerModel _HM;

    /**
     * If true, the fourth (alpha) channel is used.
     */
    private final boolean _useAlphaChannel;

    /**
     * Color stride.
     */
    private int _colorStride = 3;

    /**
     * Style used when rendering the buckets.
     */
    public BucketStyle _bucketStyle;

    /**
     * VBO manager responsible for drawing edges.
     */
    private VBOManager _edges = null;

    /**
     * Parameterized constructor.
     *
     * @param p  params container
     * @param PC plot container
     */
    public Heatmap3DLayer(Heatmap3D.Params p, PlotContainer PC)
    {
        super("Heatmap 3D layer", PC);
        _HM = new HeatmapLayerModel(p._xDiv, p._yDiv, p._zDiv,
                new AbstractMinMaxNormalization[]{p._xBucketCoordsNormalizer, p._yBucketCoordsNormalizer, p._zBucketCoordsNormalizer},
                3, p._gradient, p._heatmapDisplayRange);
        _useAlphaChannel = p._useAlphaChannel;
        if (_useAlphaChannel) _colorStride = 4;
        _bucketStyle = p._bucketStyle;
    }

    /**
     * Can be called to draw object.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void draw(GL2 gl)
    {
        Integer left = null;
        Integer right = null;

        if ((_vbo != null) || (_edges != null))
        {
            left = _HM.getLeftIndicesBound();
            right = _HM.getRightIndicesBound();
            if (left > right)
            {
                left = null;
                right = null;
            }
        }

        int maskedOffset = _HM.getNoMaskedBucketsPriorToLeftIndexBound();
        int maskedSub = _HM.getNoMaskedBucketsBetweenIndexBounds();

        if ((_vbo != null) && (left != null))
        {
            if (_bucketStyle._style == Bucket.POINT_3D)
            {
                if (_bucketStyle._relativeSize != null)
                    gl.glPointSize(_bucketStyle._relativeSize.getSize(_GC, _PC, _bucketStyle._size));
                else gl.glPointSize(_bucketStyle._size);

                int[] offset = new int[]{left - maskedOffset};
                int[] indices = new int[]{(right - left - maskedSub + 1)};
                _vbo.render(gl, indices, offset);
            }
            else
            {
                int[] offset = new int[]{(left - maskedOffset) * 6 * 2 * 3};
                int[] indices = new int[]{(right - left - maskedSub + 1) * 6 * 2 * 3};
                _vbo.render(gl, indices, offset);
            }
        }

        if ((_edges != null) && (left != null) && (_bucketStyle != null) && (_bucketStyle._edgeStyle != null))
        {
            if (_bucketStyle._edgeStyle._color.isMonoColor())
            {
                Color c = _bucketStyle._edgeStyle._color.getColor(0.0f);
                gl.glColor4f(c._r, c._g, c._b, c._a);
            }

            if (_bucketStyle._edgeStyle._relativeSize != null)
                gl.glLineWidth(_bucketStyle._edgeStyle._relativeSize.getSize(_GC, _PC, _bucketStyle._edgeStyle._size));
            else gl.glLineWidth(_bucketStyle._edgeStyle._size);

            int[] offset = new int[]{(left - maskedOffset + 1) * 12 * 2};
            int[] indices = new int[]{(right - left - maskedSub + 1) * 12 * 2};

            _edges.render(gl, indices, offset);
        }
    }


    /**
     * Auxiliary method that checks whether there is no valid data to process.
     *
     * @return true = processing should be skipped, false otherwise
     */
    private boolean checkPrematureTermination()
    {
        if (_HM.getSortedCoords() == null) return true;
        if (_HM.getSortedValues() == null) return true;
        if (_HM.getSortedCoords().length < 1) return true;
        if (_HM.getSortedValues().length < 1) return true;
        if (_bucketStyle == null) return true;
        if ((!_bucketStyle._drawEdges) && (!_bucketStyle._fillBuckets)) return true;
        assert _HM.getSortedCoords().length == _HM.getSortedValues().length;
        return false;
    }


    /**
     * Can be called to instantiate VBO buffers on the GPU.
     *
     * @param gl open gl rendering context
     */

    @Override
    public void executeInitialDataTransfer(GL2 gl)
    {
        super.executeInitialDataTransfer(gl);
        if (_edges != null) _edges.initialDataTransfer(gl);
    }

    /**
     * Can be called to instantiate VBO buffers (but the data is not yet transferred to GPU).
     */
    @Override
    public void createBuffers()
    {
        if (checkPrematureTermination()) return;

        // fills
        if (_bucketStyle._fillBuckets)
        {
            if (_bucketStyle._style == Bucket.CUBE_3D)
            {
                BufferData bdFill = constructFillCubeBufferData();
                _vbo = bdFill.createVBOManager(GL2.GL_TRIANGLES, 3, _colorStride);
            }
            else // POINTS
            {
                BufferData bdFill = constructFillPointBufferData();
                _vbo = bdFill.createVBOManager(GL2.GL_POINTS, 3, _colorStride);
            }
        }

        if ((_bucketStyle != null) && (_bucketStyle._style != Bucket.POINT_3D) && // not point
                (_bucketStyle._drawEdges) && (_bucketStyle._edgeStyle != null))
        {
            BufferData bdEdge = constructEdgeBufferData();
            _edges = bdEdge.createVBOManager(GL2.GL_LINES, 3, _colorStride);
        }
    }

    /**
     * Auxiliary method that constructs buffer data (style = cube).
     *
     * @return buffer data.
     */
    private BufferData constructFillCubeBufferData()
    {
        int n = _HM.getNoNotMaskedBuckets();
        float[] v = new float[n * 8 * 3];
        int[] ii = null;
        short[] is = null;
        boolean useInts = n * 8 > Short.MAX_VALUE;
        if (useInts) ii = new int[n * 6 * 2 * 3];
        else is = new short[n * 6 * 2 * 3];
        float[] c = new float[n * 8 * _colorStride];

        Dimension[] D = _PC.getDrawingArea().getRenderingData().getCopyOfProjectionBounds();
        Gradient g = _HM.getHeatmapGradient();

        DisplayRangesManager DRM = _HM.getHeatmapDRM();
        DisplayRangesManager.DisplayRange heatmapDR = _HM.getHeatmapDisplayRange();
        INormalization xNormalizer = DRM.getDisplayRangeForXAxis().getNormalizer();
        INormalization yNormalizer = DRM.getDisplayRangeForYAxis().getNormalizer();
        INormalization zNormalizer = DRM.getDisplayRangeForZAxis().getNormalizer();

        OffsetStride os = new OffsetStride();
        os._vaStride = 24;
        os._iaStride = 36;
        os._iStride = 8;
        os._caStride = 8 * _colorStride;

        float[] xL = _HM.getXBucketCoordsTicksDataGetter().getTicksLocations().clone();
        float[] yL = _HM.getYBucketCoordsTicksDataGetter().getTicksLocations().clone();
        float[] zL = _HM.getZBucketCoordsTicksDataGetter().getTicksLocations().clone();

        Coords co;
        double val;
        double normVal;
        Color cl;
        if (g == null) cl = Color.RED;
        else cl = null;

        for (int i = 0; i < _HM.getSortedValues().length; i++)
        {
            co = _HM.getSortedCoords()[i];
            if (_HM.isMasked(co._x, co._y, co._z)) continue;

            val = _HM.getSortedValues()[i];
            normVal = heatmapDR.getNormalizer().getNormalized(val);

            float cx = (float) (D[0]._position + D[0]._size * (xNormalizer.getNormalized(xL[co._x + 1])
                    + xNormalizer.getNormalized(xL[co._x])) / 2.0d);
            float cy = (float) (D[1]._position + D[1]._size * (yNormalizer.getNormalized(yL[co._y + 1])
                    + yNormalizer.getNormalized(yL[co._y])) / 2.0f);
            float cz = (float) (D[2]._position + D[2]._size * (1.0d -
                    (zNormalizer.getNormalized(zL[co._z + 1]) + zNormalizer.getNormalized(zL[co._z])) / 2.0f));

            if (g != null) cl = g.getColor((float) normVal);

            float hw = (float) (D[0]._size * (xNormalizer.getNormalized(xL[co._x + 1]) - xNormalizer.getNormalized(xL[co._x]))) / 2.0f;
            float hh = (float) (D[1]._size * (yNormalizer.getNormalized(yL[co._y + 1]) - yNormalizer.getNormalized(yL[co._y]))) / 2.0f;
            float hd = (float) (D[2]._size * (zNormalizer.getNormalized(zL[co._z + 1]) - zNormalizer.getNormalized(zL[co._z]))) / 2.0f;

            Cube.fillCubeFillData(cx, cy, cz, hw, hh, hd, v, ii, is, c, os, cl, _useAlphaChannel);
            os.applyStrides();
        }

        if (useInts) return new BufferData(v, ii, c);
        else return new BufferData(v, is, c);
    }

    /**
     * Auxiliary method that constructs buffer data (style = point).
     *
     * @return buffer data.
     */
    private BufferData constructFillPointBufferData()
    {
        int n = _HM.getNoNotMaskedBuckets();

        float[] v = new float[n * 3];
        int[] ii = null;
        short[] is = null;
        boolean useInts = n > Short.MAX_VALUE;
        if (useInts) ii = new int[n];
        else is = new short[n];
        float[] c = new float[n * _colorStride];

        Dimension[] D = _PC.getDrawingArea().getRenderingData().getCopyOfProjectionBounds();
        Gradient g = _HM.getHeatmapGradient();
        DisplayRangesManager.DisplayRange heatmapDR = _HM.getHeatmapDisplayRange();

        int vOffset = 0;
        int cOffset = 0;
        int iOffset = 0;
        int cStride = _colorStride;

        float[] xL = _HM.getXBucketCoordsTicksDataGetter().getTicksLocations();
        float[] yL = _HM.getYBucketCoordsTicksDataGetter().getTicksLocations();
        float[] zL = _HM.getZBucketCoordsTicksDataGetter().getTicksLocations();

        Coords co;
        double val;
        double normVal;
        Color cl;
        if (g == null) cl = Color.RED;
        else cl = null;

        for (int i = 0; i < _HM.getSortedValues().length; i++)
        {
            co = _HM.getSortedCoords()[i];
            if (_HM.isMasked(co._x, co._y, co._z)) continue;

            val = _HM.getSortedValues()[i];
            normVal = heatmapDR.getNormalizer().getNormalized(val);

            float cx = (float) (D[0]._position + D[0]._size * (xL[co._x + 1] + xL[co._x]) / 2.0d);
            float cy = (float) (D[1]._position + D[1]._size * (yL[co._y + 1] + yL[co._y]) / 2.0f);
            float cz = (float) (D[2]._position + D[2]._size * (1.0d - (zL[co._z + 1] + zL[co._z]) / 2.0f));

            if (g != null) cl = g.getColor((float) normVal);

            v[vOffset] = cx;
            v[vOffset + 1] = cy;
            v[vOffset + 2] = cz;

            if (useInts) ii[iOffset] = iOffset;
            else is[iOffset] = (short) iOffset;

            c[cOffset] = cl._r;
            c[cOffset + 1] = cl._g;
            c[cOffset + 2] = cl._b;
            if (_useAlphaChannel) c[cOffset + 3] = cl._a;

            vOffset += 3;
            iOffset++;
            cOffset += cStride;
        }

        if (useInts) return new BufferData(v, ii, c);
        else return new BufferData(v, is, c);
    }

    /**
     * Auxiliary method that constructs buffer data.
     *
     * @return buffer data.
     */
    private BufferData constructEdgeBufferData()
    {
        int n = _HM.getNoNotMaskedBuckets();

        float[] v = new float[n * 8 * 3];
        int[] ii = null;
        short[] is = null;
        boolean useInts = n * 8 > Short.MAX_VALUE;
        if (useInts) ii = new int[n * 12 * 2];
        else is = new short[n * 12 * 2];
        float[] c = null;
        //noinspection DataFlowIssue
        if (!_bucketStyle._edgeStyle._color.isMonoColor()) c = new float[n * 8 * _colorStride];

        Dimension[] D = _PC.getDrawingArea().getRenderingData().getCopyOfProjectionBounds();
        Gradient g = _HM.getHeatmapGradient();
        int gradientID = _bucketStyle._edgeStyle._drID;
        DisplayRangesManager.DisplayRange gradientDisplayRange = _HM.getHeatmapDRM().getDisplayRange(gradientID);

        OffsetStride os = new OffsetStride();
        os._vaStride = 24;
        os._iaStride = 24;
        os._iStride = 8;
        os._caStride = 8 * _colorStride;

        float[] xL = _HM.getXBucketCoordsTicksDataGetter().getTicksLocations();
        float[] yL = _HM.getYBucketCoordsTicksDataGetter().getTicksLocations();
        float[] zL = _HM.getZBucketCoordsTicksDataGetter().getTicksLocations();

        Coords co;
        double val;
        double normVal;
        Color cl;
        if (g == null) cl = _bucketStyle._edgeStyle._color.getColor(0.0f);
        else cl = null;

        for (int i = 0; i < _HM.getSortedValues().length; i++)
        {
            co = _HM.getSortedCoords()[i];
            if (_HM.isMasked(co._x, co._y, co._z)) continue;

            if (g != null)
            {
                val = 0.0f;
                if (gradientID == 0) val = (xL[co._x + 1] + xL[co._x]) / 2.0d;
                else if (gradientID == 1) val = (yL[co._y + 1] + yL[co._y]) / 2.0f;
                else if (gradientID == 2) val = (zL[co._z + 1] + zL[co._y]) / 2.0f;
                else if (gradientID == 3) val = _HM.getSortedValues()[i];
                normVal = gradientDisplayRange.getNormalizer().getNormalized(val);
                cl = g.getColor((float) normVal);
            }

            float cx = (float) (D[0]._position + D[0]._size * (xL[co._x + 1] + xL[co._x]) / 2.0d);
            float cy = (float) (D[1]._position + D[1]._size * (yL[co._y + 1] + yL[co._y]) / 2.0f);
            float cz = (float) (D[2]._position + D[2]._size * (1.0d - (zL[co._z + 1] + zL[co._z]) / 2.0f));

            float hw = (float) (D[0]._size * (xL[co._x + 1] - xL[co._x])) / 2.0f;
            float hh = (float) (D[1]._size * (yL[co._y + 1] - yL[co._y])) / 2.0f;
            float hd = (float) (D[2]._size * (zL[co._z + 1] - zL[co._z])) / 2.0f;

            Cube.fillCubeEdgesData(cx, cy, cz, hw, hh, hd, v, ii, is, c, os, cl, _useAlphaChannel);
            os.applyStrides();
        }

        if (useInts) return new BufferData(v, ii, c);
        else return new BufferData(v, is, c);
    }

    /**
     * The method returns true if some of the wrapped buffers need to be sent to the GPU for the first time.
     *
     * @return true, if the update is required, false = otherwise.
     */
    @Override
    public boolean isInitialUpdateRequested()
    {
        if ((_vbo != null) && (_vbo.isInitializationRequested())) return true;
        return (_edges != null) && (_edges.isInitializationRequested());
    }

    /**
     * Can be called to update the VBO that is already instantiated in GPU.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void executeUpdate(GL2 gl)
    {
        // must be explicitly overwritten to indicate that this buffer can be changing
        if (_vbo != null) _vbo.updateData(gl);
        if (_edges != null) _edges.updateData(gl);
    }

    /**
     * The method returns true if some of the wrapped buffers were updated and the data needs to be sent to GPU.
     *
     * @return true, if the update is required, false = otherwise.
     */
    @Override
    public boolean isUpdateRequested()
    {
        if ((_vbo != null) && (_vbo.isUpdateRequested())) return true;
        return (_edges != null) && (_edges.isUpdateRequested());
    }

    /**
     * Can be called to update data in the VBO that has already been instantiated and sent to GPU  (but the updated is not yet transferred to GPU).
     */
    @Override
    public void updateBuffers()
    {
        if (checkPrematureTermination()) return;

        if (_vbo == null) // lazy init -> change to create
        {
            createBuffers();
            return;
        }

        if ((_bucketStyle != null) && (_bucketStyle._fillBuckets))
        {
            if (_bucketStyle._style == Bucket.CUBE_3D)
            {
                BufferData bdFill = constructFillCubeBufferData();
                _vbo.initDataUpdate(bdFill, 3, _colorStride);
            }
            else // POINTS
            {
                BufferData bdFill = constructFillPointBufferData();
                _vbo.initDataUpdate(bdFill, 3, _colorStride);
            }
        }

        if ((_bucketStyle != null) && (_bucketStyle._style != Bucket.POINT_3D) &&
                (_bucketStyle._drawEdges) && (_bucketStyle._edgeStyle != null))
        {
            BufferData bdEdge = constructEdgeBufferData();
            _edges.initDataUpdate(bdEdge, 3, _colorStride);
        }
    }

    /**
     * Can be called to remove GL-related data.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void dispose(GL2 gl)
    {
        super.dispose(gl);
        if (_edges != null) _edges.dispose(gl);
        _edges = null;
    }


    /**
     * Can be called to clear memory.
     */
    @Override
    @SuppressWarnings("DuplicatedCode")
    public void dispose()
    {
        super.dispose();
        _HM.dispose(); // _HM should not be disposed by the drawing area
    }

    /**
     * Can be called to check whether the buffer objects have not been created yet.
     *
     * @return true, buffer objects have not been created yet.
     */
    @Override
    public boolean areVBOsNull()
    {
        if ((_bucketStyle != null) && (_bucketStyle._fillBuckets) && (_vbo == null)) return true;
        if ((_bucketStyle != null) && (_bucketStyle._drawEdges))
        {
            return _edges == null;
        }
        return false;
    }
}
