package component.pane;

import color.Color;
import com.jogamp.opengl.GL2;
import component.AbstractVBOComponent;
import component.axis.ticksupdater.FromFixedInterval;
import component.axis.ticksupdater.ITicksDataGetter;
import container.PlotContainer;
import drmanager.DisplayRangesManager;
import gl.IVBOComponent;
import gl.VBOManager;
import gl.vboutils.BufferData;
import listeners.auxiliary.IDisplayRangesChangedListener;
import scheme.AbstractScheme;
import scheme.enums.Align;
import scheme.enums.ColorFields;
import space.Dimension;
import space.Range;

/**
 * Supportive object for drawing cube pane (walls).
 *
 * @author MTomczyk
 */

public class Pane extends AbstractVBOComponent implements IVBOComponent, IDisplayRangesChangedListener
{
    /**
     * Gridlines linked to the first valid dimension, represented as VBO.
     */
    private VBOManager _gridFirst;

    /**
     * Gridlines linked to the second valid dimension, represented as VBO.
     */
    private VBOManager _gridSecond;

    /**
     * Line color (gridlines color).
     */
    private Color _lineColor = null;

    /**
     * Display range ID associated with one of the plane's dimensions.
     */
    private int _associatedFirstDisplayRangeID = 0;

    /**
     * Display range ID associated with one of the plane's dimensions.
     */
    private int _associatedSecondDisplayRangeID = 1;

    /**
     * Display range ID that is not associated with any of the plane's dimensions.
     */
    private int _notAssociatedDisplayRangeID = 2;

    /**
     * Auxiliary object for getting ticks location (equivalent to grid lines locations); for display range associated with the first available dimension.
     */
    private ITicksDataGetter _firstTicksDataGetter;

    /**
     * Auxiliary object for getting ticks location (equivalent to grid lines locations); for display range associated with the second available dimension.
     */
    private ITicksDataGetter _secondTicksDataGetter;

    /**
     * Parameterized constructor.
     *
     * @param name  component name
     * @param PC    p
     * @param align panel alignment (TOP, BOTTOM, LEFT, RIGHT, FRONT, BACK)
     */
    public Pane(String name, PlotContainer PC, Align align)
    {
        super(name, PC);
        _align = align;
        instantiateAuxFields();
        _firstTicksDataGetter = new FromFixedInterval(Range.getNormalRange(), 11);
        _secondTicksDataGetter = new FromFixedInterval(Range.getNormalRange(), 11);
    }

    /**
     * Auxiliary method instantiating supportive alignment-related fields.
     */
    private void instantiateAuxFields()
    {
        switch (_align)
        {
            case LEFT, RIGHT ->
            {
                _notAssociatedDisplayRangeID = 0;
                _associatedFirstDisplayRangeID = 1; // Y-axis
                _associatedSecondDisplayRangeID = 2; // Z-axis
            }
            case FRONT, BACK ->
            {
                _associatedFirstDisplayRangeID = 0; // X-axis
                _associatedSecondDisplayRangeID = 1; // Y-axis
                _notAssociatedDisplayRangeID = 2;
            }
            default -> // as bottom or top
            {
                _associatedFirstDisplayRangeID = 0; // X-axis
                _notAssociatedDisplayRangeID = 1;
                _associatedSecondDisplayRangeID = 2; // Z-axis
            }
        }
    }

    /**
     * Calculates and returns auxiliary transformation data used when determining pane/gridlines coordinates.
     *
     * @return 5-element tuple [left bound of the first dimension, right bound of the first dimension,
     * left bound of the second dimension, right bound of the second dimension, additional shift on the not used dimension].
     */
    private float[] getAuxTransformationData()
    {
        Dimension[] projectionBounds = _PC.getDrawingArea().getRenderingData().getCopyOfProjectionBounds();

        float l1 = (float) projectionBounds[_associatedFirstDisplayRangeID]._position;
        float r1 = (float) projectionBounds[_associatedFirstDisplayRangeID].getRightPosition();
        float l2 = (float) projectionBounds[_associatedSecondDisplayRangeID]._position;
        float r2 = (float) projectionBounds[_associatedSecondDisplayRangeID].getRightPosition();

        float shift = (float) projectionBounds[_notAssociatedDisplayRangeID]._position; // left / bottom / back
        if ((_align == Align.RIGHT) || (_align == Align.TOP) || (_align == Align.FRONT))
            shift = (float) projectionBounds[_notAssociatedDisplayRangeID].getRightPosition();

        return new float[]{l1, r1, l2, r2, shift};
    }


    /**
     * Can be called to instantiate VBO buffers (but the data is not yet transferred to GPU).
     */
    @Override
    public void createBuffers()
    {
        float[] T = getAuxTransformationData();

        // panes
        {
            float[] vertexes = new float[12];
            vertexes[_associatedFirstDisplayRangeID] = T[0];
            vertexes[_associatedSecondDisplayRangeID] = T[2];
            vertexes[_notAssociatedDisplayRangeID] = T[4];
            vertexes[3 + _associatedFirstDisplayRangeID] = T[0];
            vertexes[3 + _associatedSecondDisplayRangeID] = T[3];
            vertexes[3 + _notAssociatedDisplayRangeID] = T[4];
            vertexes[6 + _associatedFirstDisplayRangeID] = T[1];
            vertexes[6 + _associatedSecondDisplayRangeID] = T[3];
            vertexes[6 + _notAssociatedDisplayRangeID] = T[4];
            vertexes[9 + _associatedFirstDisplayRangeID] = T[1];
            vertexes[9 + _associatedSecondDisplayRangeID] = T[2];
            vertexes[9 + _notAssociatedDisplayRangeID] = T[4];
            short[] indices = new short[]{0, 1, 2, 0, 2, 3};
            _vbo = new VBOManager(vertexes, indices, null, GL2.GL_TRIANGLES, 3, 3);
        }

        // grids
        BufferData bd = getGridData(_associatedFirstDisplayRangeID, _associatedSecondDisplayRangeID,
                T[0], T[1], T[2], T[3], T[4], _firstTicksDataGetter);
        _gridFirst = new VBOManager(bd._vertices, bd._indicesShort, null, GL2.GL_LINES, 3, 3);
        bd = getGridData(_associatedSecondDisplayRangeID, _associatedFirstDisplayRangeID,
                T[2], T[3], T[0], T[1], T[4], _secondTicksDataGetter);
        _gridSecond = new VBOManager(bd._vertices, bd._indicesShort, null, GL2.GL_LINES, 3, 3);
    }

    /**
     * Auxiliary method helping to retrieve buffer data for gridlines.
     *
     * @param ID1 id of one of the active dimensions
     * @param ID2 id of the other active dimensions
     * @param left additional transformation data
     * @param right additional transformation data
     * @param bottom additional transformation data
     * @param top additional transformation data
     * @param shift additional transformation data
     * @param tdg selected tick data getter
     * @return buffer data for VBO
     */
    private BufferData getGridData(int ID1, int ID2, float left, float right, float bottom,
                                   float top, float shift, ITicksDataGetter tdg)
    {
        int ticksNo = tdg.getNoTicks();
        assert ticksNo > 0;
        float[] ticksLoc = tdg.getTicksLocations().clone();
        int validTicksNo = 0;
        for (float f : ticksLoc)
            if ((Float.compare(f, 0.0f) >= 0) && (Float.compare(f, 1.0f) <= 0)) validTicksNo++;

        // flip z axis
        if (ID1 == 2)
        {
            for (int i = 0; i < ticksLoc.length; i++)
                ticksLoc[i] = 1.0f - ticksLoc[i];
        }

        float[] vertexes = new float[validTicksNo * 2 * 3];
        int pnt = -1;

        float delta = right - left;
        for (int i = 0; i < ticksNo; i++)
        {
            if ((Float.compare(ticksLoc[i], 0.0f) < 0) || (Float.compare(ticksLoc[i], 1.0f) > 0)) continue;
            pnt++;
            vertexes[6 * pnt + ID1] = left + ticksLoc[i] * delta;
            vertexes[6 * pnt + _notAssociatedDisplayRangeID] = shift;
            vertexes[6 * pnt + ID2] = bottom;
            vertexes[6 * pnt + 3 + ID1] = left + ticksLoc[i] * delta;
            vertexes[6 * pnt + 3 + _notAssociatedDisplayRangeID] = shift;
            vertexes[6 * pnt + 3 + ID2] = top;
        }
        short[] indices = new short[validTicksNo * 2];
        for (short i = 0; i < validTicksNo * 2; i++) indices[i] = i;
        return new BufferData(vertexes, indices, null);
    }


    /**
     * The method returns true if either of the gridlines objects requires an update.
     *
     * @return true, if the update is required, false = otherwise.
     */
    @Override
    public boolean isUpdateRequested()
    {
        if ((_gridFirst != null) && (_gridFirst.isUpdateRequested())) return true;
        return (_gridSecond != null) && (_gridSecond.isUpdateRequested());
    }



    /**
     * Can be called to update data in the VBO that has already been instantiated and sent to GPU  (but the updated is not yet transferred to GPU).
     */
    @Override
    public void updateBuffers()
    {
        float[] T = getAuxTransformationData();
        BufferData bd = getGridData(_associatedFirstDisplayRangeID, _associatedSecondDisplayRangeID,
                T[0], T[1], T[2], T[3], T[4], _firstTicksDataGetter);
        _gridFirst.initDataUpdate(bd._vertices, bd._indicesShort, bd._colors, 3, 3);
        bd = getGridData(_associatedSecondDisplayRangeID, _associatedFirstDisplayRangeID,
                T[2], T[3], T[0], T[1], T[4], _secondTicksDataGetter);
        _gridSecond.initDataUpdate(bd._vertices, bd._indicesShort, bd._colors, 3, 3);
    }

    /**
     * Can be called to update the VBO that is already instantiated in GPU.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void executeUpdate(GL2 gl)
    {
        if (_gridFirst != null) _gridFirst.updateData(gl);
        if (_gridSecond != null) _gridSecond.updateData(gl);
    }

    /**
     * Can be called to instantiate VBO buffers.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void executeInitialDataTransfer(GL2 gl)
    {
        if (_vbo != null) _vbo.initialDataTransfer(gl);
        if (_gridFirst != null) _gridFirst.initialDataTransfer(gl);
        if (_gridSecond != null) _gridSecond.initialDataTransfer(gl);
    }


    /**
     * Supportive object for drawing the pane.
     *
     * @param gl allows performing OpenGL rendering
     */
    @Override
    public void draw(GL2 gl)
    {
        if ((_backgroundColor != null) && (_vbo != null)) // do not draw if no color
        {
            gl.glColor4f(_backgroundColor._r, _backgroundColor._g, _backgroundColor._b, _backgroundColor._a);
            if (_vbo != null) _vbo.render(gl);
        }

        if (_lineColor != null)
        {
            gl.glColor4f(_lineColor._r, _lineColor._g, _lineColor._b, _lineColor._a);
            if (_gridFirst != null) _gridFirst.render(gl);
            if (_gridSecond != null) _gridSecond.render(gl);
        }

    }

    /**
     * Supportive method updating element's scheme.
     *
     * @param scheme scheme
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        super.updateScheme(scheme);
        _backgroundColor = scheme.getColors(_surpassedColors, ColorFields.PANE_3D_BACKGROUND);
        _lineColor = scheme.getColors(_surpassedColors, ColorFields.PANE_3D_LINE);
    }

    /**
     * Can be called to flush data.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void dispose(GL2 gl)
    {
        if (_vbo != null)
        {
            _vbo.dispose(gl);
            _vbo = null;
        }

        if (_gridFirst != null)
        {
            _gridFirst.dispose(gl);
            _gridFirst = null;
        }

        if (_gridSecond != null)
        {
            _gridSecond.dispose(gl);
            _gridSecond = null;
        }
    }

    /**
     * Setter for the new tick data getter associated with the X-dimension.
     * The method properly identifies whether the first or the second ticks data getter is linked to the questioned axis
     * and makes a replacement. If the x-dimension is not relevant to this pane, the assignment is not made.
     *
     * @param xTicksDataGetter new ticks data getter for the X-axis
     */
    public void setXTicksDataGetter(ITicksDataGetter xTicksDataGetter)
    {
        if (xTicksDataGetter == null) return;
        if (_notAssociatedDisplayRangeID == 0) return;
        if (_associatedFirstDisplayRangeID == 0) _firstTicksDataGetter = xTicksDataGetter;
        else _secondTicksDataGetter = xTicksDataGetter;
    }

    /**
     * Setter for the new tick data getter associated with the Y-dimension.
     * The method properly identifies whether the first or the second ticks data getter is linked to the questioned axis
     * and makes a replacement. If the y-dimension is not relevant to this pane, the assignment is not made.
     *
     * @param yTicksDataGetter new ticks data getter for the Y-axis
     */
    public void setYTicksDataGetter(ITicksDataGetter yTicksDataGetter)
    {
        if (yTicksDataGetter == null) return;
        if (_notAssociatedDisplayRangeID == 1) return;
        if (_associatedFirstDisplayRangeID == 1) _firstTicksDataGetter = yTicksDataGetter;
        else _secondTicksDataGetter = yTicksDataGetter;
    }

    /**
     * Setter for the new tick data getter associated with the Z-dimension.
     * The method properly identifies whether the first or the second ticks data getter is linked to the questioned axis
     * and makes a replacement. If the z-dimension is not relevant to this pane, the assignment is not made.
     *
     * @param zTicksDataGetter new ticks data getter for the Z-axis
     */
    public void setZTicksDataGetter(ITicksDataGetter zTicksDataGetter)
    {
        if (zTicksDataGetter == null) return;
        if (_notAssociatedDisplayRangeID == 2) return;
        if (_associatedFirstDisplayRangeID == 2) _firstTicksDataGetter = zTicksDataGetter;
        else _secondTicksDataGetter = zTicksDataGetter;
    }

    /**
     * Returns the x-dimensions tick data getter. If the pane is not linked to the x-dimension, the method returns null.
     * Note: this tick data getter is employed to determine grid lines locations.
     *
     * @return ticks data getter of the pane, linked to the x-dimension
     */
    public ITicksDataGetter getXTicksDataGetter()
    {
        if (_associatedFirstDisplayRangeID == 0) return _firstTicksDataGetter;
        else if (_associatedSecondDisplayRangeID == 0) return _secondTicksDataGetter;
        return null;
    }

    /**
     * Returns the x-dimensions tick data getter. If the pane is not linked to the x-dimension, the method returns null.
     * Note: this tick data getter is employed to determine grid lines locations.
     *
     * @return ticks data getter of the pane, linked to the x-dimension
     */
    public ITicksDataGetter getYTicksDataGetter()
    {
        if (_associatedFirstDisplayRangeID == 1) return _firstTicksDataGetter;
        else if (_associatedSecondDisplayRangeID == 1) return _secondTicksDataGetter;
        return null;
    }

    /**
     * Returns the z-dimensions tick data getter. If the pane is not linked to the z-dimension, the method returns null.
     * Note: this tick data getter is employed to determine grid lines locations.
     *
     * @return ticks data getter of the pane, linked to the z-dimension
     */
    public ITicksDataGetter getZTicksDataGetter()
    {
        if (_associatedFirstDisplayRangeID == 2) return _firstTicksDataGetter;
        else if (_associatedSecondDisplayRangeID == 2) return _secondTicksDataGetter;
        return null;
    }



    /**
     * The method returns true if some of the wrapped buffers need to be sent to the GPU for the first time.
     *
     * @return true, if the update is required, false = otherwise.
     */
    @Override
    public boolean isInitialUpdateRequested()
    {
        if (_vbo != null) return _vbo.isInitializationRequested();
        if (_gridFirst != null) return _gridFirst.isInitializationRequested();
        if (_gridSecond != null) return _gridSecond.isInitializationRequested();
        return false;
    }

    /**
     * Method notifying on changes in display ranges.
     *
     * @param drm    display ranges manager
     * @param report report on the last update of display ranges
     */
    @Override
    public void displayRangesChanged(DisplayRangesManager drm, DisplayRangesManager.Report report)
    {
        updateBuffers();
    }

    /**
     * Can be called to check whether the buffer objects have not been created yet.
     *
     * @return true, buffer objects have not been created yet.
     */
    @Override
    public boolean areVBOsNull()
    {
        if (super.areVBOsNull()) return true;
        if (_gridFirst == null) return true;
        else return _gridSecond == null;
    }
}
