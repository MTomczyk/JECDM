package gl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import gl.vboutils.BufferData;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;


/**
 * Wrapper for various VBO-related operations.
 *
 * @author MTomczyk
 */
public class VBOManager
{
    /**
     * Supportive class container capturing essential data and performing basic operations.
     */
    public static class Data
    {
        /**
         * Used to store info on the size of the allocated data (bytes).
         */
        private long[] _allocatedData = new long[3];

        /**
         * Determines the dimensionality of vertices. E.g., if 2D rendering is the case, use offset = 2.
         */
        private int _vertexStride = 3;

        /**
         * Determines the dimensionality of color point. If 4, alpha channel is used, 3 otherwise.
         */
        private int _colorStride = 3;

        /**
         * Number of indices.
         */
        private int _noIndices;

        /**
         * Float buffer (for vertices).
         */
        protected FloatBuffer _vB;

        /**
         * Float buffer (for colors).
         */
        protected FloatBuffer _cB;

        /**
         * Short buffer (for indices).
         */
        private ShortBuffer _iBs;

        /**
         * Integer buffer (for indices).
         */
        private IntBuffer _iBi;

        /**
         * If true, indices are expressed using ints. Otherwise, they are shorts.
         */
        private boolean _useIntIndices;

        /**
         * Auxiliary flag indicating whether the data must be reallocated when updating.
         */
        protected boolean _dataMustBeReallocated = false;

        /**
         * Flag indicating whether the initialization is required.
         */
        private boolean _initializeRequest = false;

        /**
         * Flag indicating whether the update call is required.
         */
        private boolean _dataUpdateRequest = false;

        /**
         * Auxiliary method initializing buffers. Indices are represented as ints.
         *
         * @param vertices vertices
         * @param indices  (int) indices
         * @param colors   colors (can be null -> color array is not used)
         */
        public void instantiateBuffers(float[] vertices, int[] indices, float[] colors)
        {
            initVertices(vertices);
            if (indices != null) initIndices(indices);
            if (colors != null) initColors(colors);
            _useIntIndices = true;
        }

        /**
         * Auxiliary method initializing buffers. Indices are represented as shorts.
         *
         * @param vertices vertices
         * @param indices  (short) indices
         * @param colors   colors (can be null -> color array is not used)
         */
        public void instantiateBuffers(float[] vertices, short[] indices, float[] colors)
        {
            initVertices(vertices);
            if (indices != null) initIndices(indices);
            if (colors != null) initColors(colors);
            _useIntIndices = false;
        }


        /**
         * Initializes vertex buffer.
         *
         * @param vertices vertex data
         */
        private void initVertices(float[] vertices)
        {
            _vB = FloatBuffer.allocate(vertices.length);
            _vB.put(vertices);
            _vB.flip();
        }

        /**
         * Initializes color buffer.
         *
         * @param colors color data
         */
        private void initColors(float[] colors)
        {
            _cB = FloatBuffer.allocate(colors.length);
            _cB.put(colors);
            _cB.flip();
        }

        /**
         * Initializes indices buffer (integers).
         *
         * @param indices indices data
         */
        private void initIndices(int[] indices)
        {
            _iBi = IntBuffer.allocate(indices.length);
            _iBi.put(indices);
            _iBi.flip();
        }

        /**
         * Initializes indices buffer (shorts).
         *
         * @param indices indices data
         */
        private void initIndices(short[] indices)
        {
            _iBs = ShortBuffer.allocate(indices.length);
            _iBs.put(indices);
            _iBs.flip();
        }

        /**
         * Supportive method determining whether the data need to be reallocated when triggering data update.
         * The decision is based on comparing the already allocated data size with the required one.
         *
         * @param oldData (old) current data container.
         * @return true = data must be reallocated, false otherwise
         */
        public boolean determineRealloc(Data oldData)
        {
            for (int i = 0; i < 3; i++)
                if (_allocatedData[i] != oldData._allocatedData[i]) return true;
            return false;
        }

        /**
         * Releases memory.
         */
        public void dispose()
        {
            _allocatedData = null;

            if (_vB != null)
            {
                _vB.clear();
                _vB = null;
            }

            if (_iBi != null)
            {
                _iBi.clear();
                _iBi = null;
            }

            if (_iBs != null)
            {
                _iBs.clear();
                _iBs = null;
            }

            if (_cB != null)
            {
                _cB.clear();
                _cB = null;
            }
        }
    }

    /*
     * Array ids (vao).
     */
    //protected int[] _arrayIds = null;

    /**
     * Buffer ids (vbo).
     */
    protected int[] _bufferIds = null;


    /**
     * Eg., GL_LINES, GL_QUADS, etc.
     */
    protected int _drawingMode;

    /**
     * Supportive container capturing essential data.
     */
    protected Data _data = null;

    /**
     * Parameterized constructor. NOTE THAT it only stores the arrays and creates buffers (but not transfers data to GPU).
     *
     * @param vertices     vertices
     * @param indices      indices (shorts)
     * @param colors       vertex colors (can be null -> color attribute not passed)
     * @param drawingMode  e.g., GL2.GL_LINES, GL2.GL_QUADS, etc.
     * @param vertexStride determines the dimensionality of vertices; e.g., if the 2D rendering is the case, use offset = 2
     * @param colorStride  determines the dimensionality of a color point; if 4, alpha channel is used, 3 otherwise
     */
    public VBOManager(float[] vertices, short[] indices, float[] colors, int drawingMode, int vertexStride, int colorStride)
    {
        _drawingMode = drawingMode;
        _data = getShortDataInstance(vertices, indices, colors);
        _data._dataMustBeReallocated = false;
        _data._dataUpdateRequest = false;
        _data._initializeRequest = true;
        _data._vertexStride = vertexStride;
        _data._colorStride = colorStride;
    }


    /**
     * Supportive method for creating (short) data container.
     *
     * @param vertices vertices
     * @param indices  indices (shorts)
     * @param colors   vertex colors (can be null -> color attribute not passed)
     * @return (short) data container
     */
    private Data getShortDataInstance(float[] vertices, short[] indices, float[] colors)
    {
        Data D = new Data();
        D._useIntIndices = false;
        D._noIndices = indices.length;
        D._allocatedData = new long[3];
        D._allocatedData[0] = (long) vertices.length * Buffers.SIZEOF_FLOAT;
        D._allocatedData[1] = (long) indices.length * Buffers.SIZEOF_SHORT;
        if (colors != null) D._allocatedData[2] = (long) colors.length * Buffers.SIZEOF_FLOAT;
        D.instantiateBuffers(vertices, indices, colors);
        return D;
    }

    /**
     * Parameterized constructor. NOTE THAT it only stores the arrays and creates buffers (but not transfers data to GPU).
     *
     * @param vertices     vertices
     * @param indices      indices (ints)
     * @param colors       vertex colors (can be null -> color attribute not passed)
     * @param drawingMode  e.g., GL2.GL_LINES, GL2.GL_QUADS, etc.
     * @param vertexStride determines the dimensionality of vertices. E.g., if the 2D rendering is the case, use offset = 2
     * @param colorStride  determines the dimensionality of color point. If 4, alpha channel is used, 3 otherwise.
     */
    public VBOManager(float[] vertices, int[] indices, float[] colors, int drawingMode, int vertexStride, int colorStride)
    {
        _drawingMode = drawingMode;
        _data = getIntDataInstance(vertices, indices, colors);
        _data._dataMustBeReallocated = false;
        _data._dataUpdateRequest = false;
        _data._initializeRequest = true;
        _data._vertexStride = vertexStride;
        _data._colorStride = colorStride;
    }


    /**
     * Supportive method for creating (int) data container.
     *
     * @param vertices vertices
     * @param indices  indices (ints)
     * @param colors   vertex colors (can be null -> color attribute not passed)
     * @return (short) data container
     */
    private Data getIntDataInstance(float[] vertices, int[] indices, float[] colors)
    {
        Data D = new Data();
        D._useIntIndices = true;
        D._noIndices = indices.length;
        D._allocatedData = new long[3];
        D._allocatedData[0] = (long) vertices.length * Buffers.SIZEOF_FLOAT;
        D._allocatedData[1] = (long) indices.length * Buffers.SIZEOF_INT;
        if (colors != null) D._allocatedData[2] = (long) colors.length * Buffers.SIZEOF_FLOAT;
        D.instantiateBuffers(vertices, indices, colors);
        return D;
    }

    /**
     * Transfers data to GPU.
     *
     * @param gl open-gl rendering context
     */
    public void initialDataTransfer(GL2 gl)
    {
        if (_bufferIds == null) // lazy init of ids
        {
            //_arrayIds = new int[1];
            _bufferIds = new int[3];
            // System.out.println("OpenGL: " + (gl.glGetString(GL_VERSION)));
            //gl.glGenVertexArrays(1, _arrayIds, 0);
            gl.glGenBuffers(3, _bufferIds, 0);
        }

        //gl.glBindVertexArray(_arrayIds[0]);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, _bufferIds[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, _data._allocatedData[0], _data._vB, GL2.GL_STATIC_DRAW);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, _bufferIds[1]);
        if (_data._useIntIndices) gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, _data._allocatedData[1], _data._iBi, GL2.GL_STATIC_DRAW);
        else gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, _data._allocatedData[1], _data._iBs, GL2.GL_STATIC_DRAW);

        if (_data._cB != null)
        {
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, _bufferIds[2]);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, _data._allocatedData[2], _data._cB, GL2.GL_STATIC_DRAW);
        }

        _data._initializeRequest = false;
        _data._dataMustBeReallocated = false;
        _data._dataUpdateRequest = false;

    //    gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    //    gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);

        //gl.glBindVertexArray(0);
    }

    /**
     * Performs rendering.
     *
     * @param gl open GL rendering context
     */
    public void render(GL2 gl)
    {
        render(gl, null, null);
    }

    /**
     * Performs rendering (method to be parameterized).
     *
     * @param gl        open GL rendering context
     * @param noIndices if null, the whole index array is used for rendering, otherwise -> noIndices array triggers series
     *                  of rendering calls, where the number of indices to used is determined by elements of noIndices
     * @param offsets   if null, the whole index array is used for rendering, otherwise -> noIndices array triggers series
     *                  of rendering calls, where the offsets for rendering are determined by this input array
     */
    public void render(GL2 gl, int[] noIndices, int[] offsets)
    {
        if (_bufferIds == null) return; // no buffer ids

        //gl.glBindVertexArray(_arrayIds[0]);

        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, _bufferIds[0]);
        gl.glVertexPointer(_data._vertexStride, GL2.GL_FLOAT, 0, 0);

        if (_data._cB != null)
        {
            gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, _bufferIds[2]);
            gl.glColorPointer(_data._colorStride, GL2.GL_FLOAT, 0, 0);
        }

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, _bufferIds[1]);

        if ((noIndices != null) && (offsets != null))
        {
            for (int d = 0; d < noIndices.length; d++)
            {
                if (offsets[d] < 0) continue;
                if (_data._useIntIndices) gl.glDrawElements(_drawingMode, noIndices[d], GL2.GL_UNSIGNED_INT, (long) offsets[d] * Buffers.SIZEOF_INT);
                else gl.glDrawElements(_drawingMode, noIndices[d], GL2.GL_UNSIGNED_SHORT, (long) offsets[d] * Buffers.SIZEOF_SHORT);
            }
        }
        else
        {
            if (_data._useIntIndices) gl.glDrawElements(_drawingMode, _data._noIndices, GL2.GL_UNSIGNED_INT, 0);
            else gl.glDrawElements(_drawingMode, _data._noIndices, GL2.GL_UNSIGNED_SHORT, 0);

        }

        if (_data._cB != null) gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
        //gl.glBindVertexArray(0);
    }

    /**
     * Prepares the data to be updated in VBO.
     *
     * @param bufferData buffer data (vertices, indices, colors)
     * @param vertexStride vertex stride for the vertex array
     * @param colorStride color stride for the optional color array
     */
    public void initDataUpdate(BufferData bufferData, int vertexStride, int colorStride)
    {
        if (bufferData._indicesInt != null)
            initDataUpdate(bufferData._vertices, bufferData._indicesInt, bufferData._colors, vertexStride,colorStride);
        else initDataUpdate(bufferData._vertices, bufferData._indicesShort, bufferData._colors, vertexStride,colorStride);
    }

    /**
     * Prepares the data to be updated in VBO.
     *
     * @param vertices vertices
     * @param indices  indices (shorts)
     * @param colors   vertex colors (can be null -> color attribute not passed)
     * @param vertexStride vertex stride for the vertex array
     * @param colorStride color stride for the optional color array
     */
    public void initDataUpdate(float[] vertices, short[] indices, float[] colors, int vertexStride, int colorStride)
    {
        Data newData = getShortDataInstance(vertices, indices, colors);
        newData._vertexStride = vertexStride;
        newData._colorStride = colorStride;
        newData._dataMustBeReallocated = newData.determineRealloc(_data);
        newData._initializeRequest = false;
        newData._dataUpdateRequest = true;
        _data = newData;
    }

    /**
     * Prepares the data to be updated in VBO.
     *
     * @param vertices vertices
     * @param indices  indices (ints)
     * @param colors   vertex colors (can be null -> color attribute not passed)
     * @param vertexStride vertex stride for the vertex array
     * @param colorStride color stride for the optional color array
     */
    public void initDataUpdate(float[] vertices, int[] indices, float[] colors, int vertexStride, int colorStride)
    {
        Data newData = getIntDataInstance(vertices, indices, colors);
        newData._vertexStride = vertexStride;
        newData._colorStride = colorStride;
        newData._dataMustBeReallocated = newData.determineRealloc(_data);
        newData._initializeRequest = false;
        newData._dataUpdateRequest = true;
        _data = newData;
    }

    /**
     * Supportive method updating data in already existing buffers.
     *
     * @param gl open gl drawing context
     */
    public void updateData(GL2 gl)
    {
        if (_bufferIds == null)
        {
            //_arrayIds = new int[1];
            _bufferIds = new int[3];
            //gl.glGenVertexArrays(1, _arrayIds, 0);
            gl.glGenBuffers(3, _bufferIds, 0);
            _data._dataMustBeReallocated = true;
        }

        //gl.glBindVertexArray(_arrayIds[0]);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, _bufferIds[0]);

        if (_data._dataMustBeReallocated)
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, _data._allocatedData[0], null, GL2.GL_DYNAMIC_DRAW);
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, _data._allocatedData[0], _data._vB);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, _bufferIds[1]);
        if (_data._dataMustBeReallocated)
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, _data._allocatedData[1], null, GL2.GL_DYNAMIC_DRAW);
        if (_data._useIntIndices)
            gl.glBufferSubData(GL2.GL_ELEMENT_ARRAY_BUFFER, 0, _data._allocatedData[1], _data._iBi);
        else
            gl.glBufferSubData(GL2.GL_ELEMENT_ARRAY_BUFFER, 0, _data._allocatedData[1], _data._iBs);

        if (_data._cB != null)
        {
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, _bufferIds[2]);
            if (_data._dataMustBeReallocated)
                gl.glBufferData(GL2.GL_ARRAY_BUFFER, _data._allocatedData[2], null, GL2.GL_DYNAMIC_DRAW);
            gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, _data._allocatedData[2], _data._cB);
        }

        _data._dataUpdateRequest = false;
        _data._dataMustBeReallocated = false;
        _data._initializeRequest = false;

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
        //gl.glBindVertexArray(0);
    }

    /**
     * Deletes allocated memory.
     *
     * @param gl open gl drawing context
     */
    public void dispose(GL2 gl)
    {
        //if (_arrayIds != null) gl.glDeleteVertexArrays(1, _arrayIds, 0);
        if (_bufferIds != null) gl.glDeleteBuffers(3, _bufferIds, 0);
        _data.dispose();
    }

    /**
     * Method for determining whether the initialization using the OpenGL rendering context is required.
     *
     * @return true =  initialization using the open gl rendering context is required, false otherwise
     */
    public boolean isInitializationRequested()
    {
        return _data._initializeRequest;
    }

    /**
     * Method for determining whether the data update using the OpenGL rendering context is required.
     *
     * @return true =  data update using the open gl rendering context is required, false otherwise
     */
    public boolean isUpdateRequested()
    {
        return _data._dataUpdateRequest;
    }
}
