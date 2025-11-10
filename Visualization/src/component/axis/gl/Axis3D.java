package component.axis.gl;

import color.Color;
import com.jogamp.opengl.GL2;
import component.AbstractVBOComponent;
import component.axis.DirectionVectors;
import component.axis.ticksupdater.AbstractTicksDataGetter;
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
import scheme.enums.FontFields;
import scheme.enums.SizeFields;
import space.Range;
import utils.Font3DProcessor;

import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

/**
 * Implementation of a 3D axis for {@link plot.Plot3D}.
 *
 * @author MTomczyk
 */


public class Axis3D extends AbstractVBOComponent implements IVBOComponent, IDisplayRangesChangedListener
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Axis main title. Can be null -> not displayed.
         */
        public String _title = null;

        /**
         * Component name.
         */
        public String _name;

        /**
         * Plot container
         */
        public PlotContainer _PC;

        /**
         * Axis align
         */
        public Align _align;

        /**
         * Auxiliary object for getting ticks location (equivalent to grid lines locations); for display range
         * associated with the first available dimension.
         */
        public ITicksDataGetter _ticksDataGetter;

        /**
         * Parameterized constructor.
         *
         * @param name component name
         * @param PC   plot container: allows accessing various plot components
         */
        public Params(String name, PlotContainer PC)
        {
            _name = name;
            _PC = PC;
        }
    }

    /**
     * Axis title.
     */
    public String _title;

    /**
     * Line color (gridlines color).
     */
    private Color _lineColor = null;

    /**
     * Tick length.
     */
    private float _tickLength = 0.1f;

    /**
     * Shift along the tick that indicates the label position.
     */
    private float _tickLabelOffset = 0.2f;

    /**
     * Shift along the tick that indicates the title position.
     */
    private float _titleOffset = 0.3f;

    /**
     * Font quality upscaling factor (the higher, the higher the upscaling level; thus better quality).
     */
    private float _fontQualityUpscaling = 1.0f;

    /**
     * Tick label fonts.
     */
    private Font3DProcessor[] _tickLabelFonts;

    /**
     * title font.
     */
    private final Font3DProcessor _titleFont;

    /**
     * Color field used to determine axis color.
     */
    private ColorFields _colorField;

    /**
     * Size field used to determine tick length.
     */
    private SizeFields _tickLengthField;

    /**
     * Size field used to determine tick label offset.
     */
    private SizeFields _tickLabelOffsetField;

    /**
     * Size field used to determine title offset.
     */
    private SizeFields _titleOffsetField;

    /**
     * Size field used to determine tick label font scale.
     */
    private SizeFields _tickLabelFontScaleField;

    /**
     * Size field used to determine the title font scale.
     */
    private SizeFields _titleFontScaleField;

    /**
     * Font field used to determine the tick label font.
     */
    private FontFields _tickLabelFontField;

    /**
     * Font field used to determine the title font.
     */
    private FontFields _titleFontField;

    /**
     * Font field used to determine the tick label font color.
     */
    private ColorFields _tickLabelFontColorField;

    /**
     * Font field used to determine the label font color.
     */
    private ColorFields _titleFontColorField;

    /**
     * Display range ID associated with one of the plane's dimensions.
     */
    private int _associatedDisplayRangeID = 0;

    /**
     * Auxiliary object for getting ticks location (equivalent to grid lines locations); for display range associated
     * with the first available dimension.
     */
    private ITicksDataGetter _ticksDataGetter;

    /**
     * Auxiliary field storing, e.g., ticks relative coordinates.
     */
    private DirectionVectors _D;

    /**
     * Auxiliary field storing tick labels to be rendered.
     */
    private String[] _tickLabels;

    /**
     * Tick line width (null, if not used)
     */
    private Float _tickLineWidth;

    /**
     * Main line width (null, if not used)
     */
    private Float _mainLineWidth;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public Axis3D(Params p)
    {
        super(p._name, p._PC);
        _title = p._title;
        _align = p._align;
        _ticksDataGetter = p._ticksDataGetter;
        if (_ticksDataGetter == null) _ticksDataGetter = new FromFixedInterval(Range.getNormalRange(), 5);
        _tickLabelFonts = new Font3DProcessor[_ticksDataGetter.getNoTicks()];
        for (int i = 0; i < _tickLabelFonts.length; i++) _tickLabelFonts[i] = new Font3DProcessor();
        _titleFont = new Font3DProcessor();
        instantiateAuxFields();
    }

    /**
     * Returns a display range ID associated with a given axis alignment.
     *
     * @param align axis alignment
     * @return display range id (0 = X, 1 = Y, 2 = Z)
     */
    public static int getAssociatedDisplayRangeID(Align align)
    {
        if ((align.equals(Align.FRONT_TOP)) || (align.equals(Align.FRONT_BOTTOM)) ||
                (align.equals(Align.BACK_TOP)) || (align.equals(Align.BACK_BOTTOM))) return 0;
        else if ((align.equals(Align.FRONT_LEFT)) || (align.equals(Align.FRONT_RIGHT)) ||
                (align.equals(Align.BACK_LEFT)) || (align.equals(Align.BACK_RIGHT))) return 1;
        if ((align.equals(Align.LEFT_BOTTOM)) || (align.equals(Align.LEFT_TOP)) ||
                (align.equals(Align.RIGHT_BOTTOM)) || (align.equals(Align.RIGHT_TOP))) return 2;
        return 0;
    }

    /**
     * Auxiliary method6 instantiating supportive alignment-related fields.
     */
    private void instantiateAuxFields()
    {
        if ((!_align.equals(Align.LEFT_BOTTOM)) && (!_align.equals(Align.LEFT_TOP)) &&
                (!_align.equals(Align.RIGHT_BOTTOM)) && (!_align.equals(Align.RIGHT_TOP)) &&
                (!_align.equals(Align.FRONT_LEFT)) && (!_align.equals(Align.FRONT_RIGHT)) &&
                (!_align.equals(Align.FRONT_TOP)) && (!_align.equals(Align.FRONT_BOTTOM)) &&
                (!_align.equals(Align.BACK_LEFT)) && (!_align.equals(Align.BACK_RIGHT)) &&
                (!_align.equals(Align.BACK_BOTTOM)) && (!_align.equals(Align.BACK_TOP)))
            _align = Align.LEFT_BOTTOM;

        _associatedDisplayRangeID = getAssociatedDisplayRangeID(_align);

        if (_associatedDisplayRangeID == 0)
        {
            _colorField = ColorFields.AXIS3D_X;
            _tickLengthField = SizeFields.AXIS3D_X_TICK_SIZE;
            _tickLabelOffsetField = SizeFields.AXIS3D_X_TICK_LABEL_OFFSET;
            _titleOffsetField = SizeFields.AXIS3D_X_TITLE_OFFSET;
            _tickLabelFontScaleField = SizeFields.AXIS3D_X_TICK_LABEL_FONT_SIZE_SCALE;
            _titleFontScaleField = SizeFields.AXIS3D_X_TITLE_FONT_SIZE_SCALE;
            _tickLabelFontField = FontFields.AXIS3D_X_TICK_LABEL;
            _titleFontField = FontFields.AXIS3D_X_TITLE;
            _tickLabelFontColorField = ColorFields.AXIS3D_X_TICK_LABEL_FONT;
            _titleFontColorField = ColorFields.AXIS3D_X_TITLE_FONT;
        } else if (_associatedDisplayRangeID == 1)
        {
            _colorField = ColorFields.AXIS3D_Y;
            _tickLengthField = SizeFields.AXIS3D_Y_TICK_SIZE;
            _tickLabelOffsetField = SizeFields.AXIS3D_Y_TICK_LABEL_OFFSET;
            _titleOffsetField = SizeFields.AXIS3D_Y_TITLE_OFFSET;
            _tickLabelFontScaleField = SizeFields.AXIS3D_Y_TICK_LABEL_FONT_SIZE_SCALE;
            _titleFontScaleField = SizeFields.AXIS3D_Y_TITLE_FONT_SIZE_SCALE;
            _tickLabelFontField = FontFields.AXIS3D_Y_TICK_LABEL;
            _titleFontField = FontFields.AXIS3D_Y_TITLE;
            _tickLabelFontColorField = ColorFields.AXIS3D_Y_TICK_LABEL_FONT;
            _titleFontColorField = ColorFields.AXIS3D_Y_TITLE_FONT;
        } else if (_associatedDisplayRangeID == 2)
        {
            _colorField = ColorFields.AXIS3D_Z;
            _tickLengthField = SizeFields.AXIS3D_Z_TICK_SIZE;
            _tickLabelOffsetField = SizeFields.AXIS3D_Z_TICK_LABEL_OFFSET;
            _titleOffsetField = SizeFields.AXIS3D_Z_TITLE_OFFSET;
            _tickLabelFontScaleField = SizeFields.AXIS3D_Z_TICK_LABEL_FONT_SIZE_SCALE;
            _titleFontScaleField = SizeFields.AXIS3D_Z_TITLE_FONT_SIZE_SCALE;
            _tickLabelFontField = FontFields.AXIS3D_Z_TICK_LABEL;
            _titleFontField = FontFields.AXIS3D_Z_TITLE;
            _tickLabelFontColorField = ColorFields.AXIS3D_Z_TICK_LABEL_FONT;
            _titleFontColorField = ColorFields.AXIS3D_Z_TITLE_FONT;
        }
    }

    /**
     * Calculates and returns auxiliary transformation data used when determining ticks coordinates.
     *
     * @return container for supporting fields
     */
    private DirectionVectors getDirectionVectors()
    {
        DirectionVectors D = new DirectionVectors(3);
        space.Dimension[] B = _PC.getDrawingArea().getRenderingData().getCopyOfProjectionBounds();

        // starting positions
        if ((_align.equals(Align.FRONT_BOTTOM)) || (_align.equals(Align.FRONT_LEFT)) || (_align.equals(Align.LEFT_BOTTOM)))
        {
            D._s[0] = (float) B[0]._position;
            D._s[1] = (float) B[1]._position;
            D._s[2] = (float) B[2].getRightPosition();
        } else if ((_align.equals(Align.FRONT_RIGHT)) || (_align.equals(Align.RIGHT_BOTTOM)))
        {
            D._s[0] = (float) B[0].getRightPosition();
            D._s[1] = (float) B[1]._position;
            D._s[2] = (float) B[2].getRightPosition();
        } else if ((_align.equals(Align.FRONT_TOP)) || (_align.equals(Align.LEFT_TOP)))
        {
            D._s[0] = (float) B[0]._position;
            D._s[1] = (float) B[1].getRightPosition();
            D._s[2] = (float) B[2].getRightPosition();
        } else if ((_align.equals(Align.RIGHT_TOP)))
        {
            D._s[0] = (float) B[0].getRightPosition();
            D._s[1] = (float) B[1].getRightPosition();
            D._s[2] = (float) B[2].getRightPosition();
        } else if ((_align.equals(Align.BACK_LEFT)) || (_align.equals(Align.BACK_BOTTOM)))
        {
            D._s[0] = (float) B[0]._position;
            D._s[1] = (float) B[1]._position;
            D._s[2] = (float) B[2]._position;
        } else if (_align.equals(Align.BACK_RIGHT))
        {
            D._s[0] = (float) B[0].getRightPosition();
            D._s[1] = (float) B[1]._position;
            D._s[2] = (float) B[2]._position;
        } else if (_align.equals(Align.BACK_TOP))
        {
            D._s[0] = (float) B[0]._position;
            D._s[1] = (float) B[1].getRightPosition();
            D._s[2] = (float) B[2]._position;
        }

        // ending positions
        if ((_align.equals(Align.FRONT_BOTTOM)))
        {
            D._e[0] = (float) B[0].getRightPosition();
            D._e[1] = (float) B[1]._position;
            D._e[2] = (float) B[2].getRightPosition();
        } else if ((_align.equals(Align.LEFT_BOTTOM)))
        {
            D._e[0] = (float) B[0]._position;
            D._e[1] = (float) B[1]._position;
            D._e[2] = (float) B[2]._position;
        } else if ((_align.equals(Align.RIGHT_BOTTOM)) || (_align.equals(Align.BACK_BOTTOM)))
        {
            D._e[0] = (float) B[0].getRightPosition();
            D._e[1] = (float) B[1]._position;
            D._e[2] = (float) B[2]._position;
        } else if (_align.equals(Align.FRONT_LEFT))
        {
            D._e[0] = (float) B[0]._position;
            D._e[1] = (float) B[1].getRightPosition();
            D._e[2] = (float) B[2].getRightPosition();
        } else if ((_align.equals(Align.FRONT_TOP)) || (_align.equals(Align.FRONT_RIGHT)))
        {
            D._e[0] = (float) B[0].getRightPosition();
            D._e[1] = (float) B[1].getRightPosition();
            D._e[2] = (float) B[2].getRightPosition();
        } else if ((_align.equals(Align.LEFT_TOP)) || (_align.equals(Align.BACK_LEFT)))
        {
            D._e[0] = (float) B[0]._position;
            D._e[1] = (float) B[1].getRightPosition();
            D._e[2] = (float) B[2]._position;
        } else if ((_align.equals(Align.BACK_RIGHT)) || (_align.equals(Align.RIGHT_TOP)) || (_align.equals(Align.BACK_TOP)))
        {
            D._e[0] = (float) B[0].getRightPosition();
            D._e[1] = (float) B[1].getRightPosition();
            D._e[2] = (float) B[2]._position;
        }

        float[] ticksLoc = _ticksDataGetter.getTicksLocations().clone();

        int validTicks = 0;
        for (float f : ticksLoc)
            if (AbstractTicksDataGetter.isTickLockNormal(f)) validTicks++;
        D.instantiateTickArrays(validTicks);

        float[] DEB = new float[]{D._e[0] - D._s[0], D._e[1] - D._s[1], D._e[2] - D._s[2]};

        int pnt = -1;
        for (float v : ticksLoc)
        {
            if (!AbstractTicksDataGetter.isTickLockNormal(v)) continue;
            pnt++;
            D._tp[pnt][0] = D._s[0] + v * DEB[0];
            D._tp[pnt][1] = D._s[1] + v * DEB[1];
            D._tp[pnt][2] = D._s[2] + v * DEB[2];

            float dt = _tickLength;
            float dtl = _tickLabelOffset;
            float dl = _titleOffset;

            if ((_align.equals(Align.LEFT_BOTTOM)) || (_align.equals(Align.LEFT_TOP))
                    || (_align.equals(Align.FRONT_LEFT)) || (_align.equals(Align.BACK_LEFT))) D._dt[pnt][0] = -dt;
            else if ((_align.equals(Align.RIGHT_BOTTOM)) || (_align.equals(Align.RIGHT_TOP))
                    || (_align.equals(Align.FRONT_RIGHT)) || (_align.equals(Align.BACK_RIGHT))) D._dt[pnt][0] = dt;

            if ((_align.equals(Align.LEFT_BOTTOM)) || (_align.equals(Align.RIGHT_BOTTOM))
                    || (_align.equals(Align.FRONT_BOTTOM)) || (_align.equals(Align.BACK_BOTTOM))) D._dt[pnt][1] = -dt;
            else if ((_align.equals(Align.LEFT_TOP)) || (_align.equals(Align.RIGHT_TOP))
                    || (_align.equals(Align.FRONT_TOP)) || (_align.equals(Align.BACK_TOP))) D._dt[pnt][1] = dt;

            if ((_align.equals(Align.BACK_TOP)) || (_align.equals(Align.BACK_BOTTOM))
                    || (_align.equals(Align.BACK_LEFT)) || (_align.equals(Align.BACK_RIGHT))) D._dt[pnt][2] = -dt;
            else if ((_align.equals(Align.FRONT_LEFT)) || (_align.equals(Align.FRONT_RIGHT))
                    || (_align.equals(Align.FRONT_TOP)) || (_align.equals(Align.FRONT_BOTTOM))) D._dt[pnt][2] = dt;


            float norm = dt * (float) Math.sqrt(2.0d);
            D._dtl[pnt][0] = D._dt[pnt][0] / norm * dtl;
            D._dtl[pnt][1] = D._dt[pnt][1] / norm * dtl;
            D._dtl[pnt][2] = D._dt[pnt][2] / norm * dtl;

            D._dl[0] = D._s[0] + DEB[0] * 0.5f + D._dt[pnt][0] / norm * dl;
            D._dl[1] = D._s[1] + DEB[1] * 0.5f + D._dt[pnt][1] / norm * dl;
            D._dl[2] = D._s[2] + DEB[2] * 0.5f + D._dt[pnt][2] / norm * dl;
        }

        return D;
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
        _lineColor = scheme.getColors(_surpassedColors, _colorField);
        _tickLength = scheme.getSizes(_surpassedSizes, _tickLengthField);
        _tickLabelOffset = scheme.getSizes(_surpassedSizes, _tickLabelOffsetField);
        _titleOffset = scheme.getSizes(_surpassedSizes, _titleOffsetField);

        if (_tickLabelFonts != null)
        {
            for (Font3DProcessor tickLabelFont : _tickLabelFonts)
            {
                tickLabelFont._size.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _tickLabelFontScaleField));
                tickLabelFont._size.setUseRelativeSize(true);
                tickLabelFont._size.computeActualSize(1.0f);
                tickLabelFont._color = scheme.getColors(_surpassedColors, _tickLabelFontColorField);
                tickLabelFont._fontName = scheme.getFonts(_surpassedFonts, _tickLabelFontField);
            }
        }

        _titleFont._size.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _titleFontScaleField));
        _titleFont._size.setUseRelativeSize(true);
        _titleFont._size.computeActualSize(1.0f);
        _titleFont._color = scheme.getColors(_surpassedColors, _titleFontColorField);
        _titleFont._fontName = scheme.getFonts(_surpassedFonts, _titleFontField);

        _fontQualityUpscaling = scheme.getSizes(_surpassedSizes, SizeFields.FONT_3D_QUALITY_UPSCALING);

        if (_associatedDisplayRangeID == 0)
        {
            _tickLineWidth = scheme.getSizes(_surpassedSizes, SizeFields.AXIS3D_X_TICK_LINE_WIDTH);
            _mainLineWidth = scheme.getSizes(_surpassedSizes, SizeFields.AXIS3D_X_MAIN_LINE_WIDTH);
        } else if (_associatedDisplayRangeID == 1)
        {
            _tickLineWidth = scheme.getSizes(_surpassedSizes, SizeFields.AXIS3D_Y_TICK_LINE_WIDTH);
            _mainLineWidth = scheme.getSizes(_surpassedSizes, SizeFields.AXIS3D_Y_MAIN_LINE_WIDTH);
        } else if (_associatedDisplayRangeID == 2)
        {
            _tickLineWidth = scheme.getSizes(_surpassedSizes, SizeFields.AXIS3D_Z_TICK_LINE_WIDTH);
            _mainLineWidth = scheme.getSizes(_surpassedSizes, SizeFields.AXIS3D_Z_MAIN_LINE_WIDTH);
        }
        updateBuffers();
    }

    /**
     * Can be called to draw object.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void draw(GL2 gl)
    {
        gl.glLineWidth(1.0f);

        if (_lineColor != null) gl.glColor4f(_lineColor._r, _lineColor._g, _lineColor._b, _lineColor._a);
        if (_mainLineWidth != null) gl.glLineWidth(_mainLineWidth);
        super.draw(gl);

        if (_tickLineWidth != null) gl.glLineWidth(_tickLineWidth);
        else gl.glLineWidth(1.0f);

        if (_tickLabels != null)
        {
            for (int i = 0; i < _D._tp.length; i++)
            {
                if (_tickLabels.length - 1 < i) break;
                if (_tickLabels[i] == null) continue;
                if (_tickLabels[i].isEmpty()) continue;

                _tickLabelFonts[i].prepareTextDependentState(_tickLabels[i], 24.0f * _fontQualityUpscaling, null);
                String parsed = _tickLabelFonts[i].getCurrentParsedText();

                float scale = _tickLabelFonts[i]._size._actualSize;
                scale /= _fontQualityUpscaling;

                Rectangle2D referenceBounds = _tickLabelFonts[i].getCurrentReferenceTextBounds();
                Rectangle2D bounds = _tickLabelFonts[i].getCurrentParsedTextBounds();

                float[] t2 = new float[]{(float) (-bounds.getWidth() / 2.0f * scale),
                        (float) (-referenceBounds.getHeight() * scale), 0.0f}; // use reference bound's height
                if (_associatedDisplayRangeID == 0)
                {
                    if ((_align == Align.FRONT_TOP) || (_align == Align.BACK_TOP)) t2[1] = 0.0f;
                } else if (_associatedDisplayRangeID == 1)
                {
                    t2[0] = (float) (-bounds.getWidth() * scale);
                    t2[1] = (float) (-referenceBounds.getHeight() * scale / 2.0f);  // use reference bound's height
                    if ((_align == Align.FRONT_RIGHT) || (_align == Align.BACK_RIGHT)) t2[0] = 0.0f;
                } else
                {
                    t2[0] = (float) (-bounds.getWidth() * scale);
                    t2[1] = (float) (-referenceBounds.getHeight() * scale / 2.0f);  // use reference bound's height
                    if ((_align == Align.RIGHT_BOTTOM) || (_align == Align.RIGHT_TOP)) t2[0] = 0.0f;
                }

                float[] t1 = new float[]{
                        _D._tp[i][0] + _D._dtl[i][0],
                        _D._tp[i][1] + _D._dtl[i][1],
                        _D._tp[i][2] + _D._dtl[i][2]};

                if (_tickLabelFonts[i]._color != null) _tickLabelFonts[i].setColor(_tickLabelFonts[i]._color);
                drawLabel(_tickLabelFonts[i].getCurrentAttributedString(), bounds,
                        parsed, gl, t1, t2, _tickLabelFonts[i],
                        _tickLabelFonts[i].getIsRegularFontRenderRequiredFlag());
            }
        }

        if (_tickLineWidth != null) gl.glLineWidth(1.0f);

        // draw ax labels
        if ((_title != null) && (!_title.isEmpty()))
        {
            _titleFont.prepareTextDependentState(_title, 24.0f * _fontQualityUpscaling, null);
            String parsed = _titleFont.getCurrentParsedText();

            float scale = _titleFont._size._actualSize;
            scale /= _fontQualityUpscaling;

            Rectangle2D referenceBounds = _titleFont.getCurrentReferenceTextBounds();
            Rectangle2D bounds = _titleFont.getCurrentParsedTextBounds();

            float[] t2 = new float[]{(float) (-bounds.getWidth() / 2.0f * scale),
                    (float) (-referenceBounds.getHeight() * scale), 0.0f};  // use reference bound's height

            if (_associatedDisplayRangeID == 0)
            {
                if ((_align == Align.FRONT_TOP) || (_align == Align.BACK_TOP)) t2[1] = 0.0f;
            } else if (_associatedDisplayRangeID == 1)
            {
                t2[0] = (float) (-bounds.getWidth() * scale);
                t2[1] = (float) (-referenceBounds.getHeight() * scale / 2.0f);  // use reference bound's height
                if ((_align == Align.BACK_RIGHT) || (_align == Align.FRONT_RIGHT)) t2[0] = 0.0F;
            } else if (_associatedDisplayRangeID == 2)
            {
                t2[0] = 0.0f;
                t2[1] = (float) (-referenceBounds.getHeight() * scale / 2.0f);  // use reference bound's height
                if ((_align == Align.LEFT_BOTTOM) || (_align == Align.LEFT_TOP))
                    t2[0] = (float) (-bounds.getWidth() * scale);
            }
            if (_titleFont._color != null) _titleFont.setColor(_titleFont._color);
            drawLabel(_titleFont.getCurrentAttributedString(), bounds,
                    parsed, gl, _D._dl, t2, _titleFont,
                    _titleFont.getIsRegularFontRenderRequiredFlag());
        }
    }

    /**
     * Supportive method for drawing labels.
     *
     * @param label                label to be drawn (viewed as attributed string)
     * @param textBound            (pre-cached) text bound
     * @param parsed               parsed string
     * @param gl                   allows performing opengl rendering
     * @param t1                   primary translation
     * @param t2                   secondary translation (used to center the label; can be null -> not used)
     * @param fontProcessor        font properties
     * @param executeRegularRender flag indicating whether to use a regular render
     */
    private void drawLabel(AttributedString label,
                           Rectangle2D textBound,
                           String parsed, GL2 gl, float[] t1, float[] t2,
                           Font3DProcessor fontProcessor,
                           boolean executeRegularRender)
    {
        gl.glPushMatrix();
        gl.glTranslatef(t1[0], t1[1], t1[2]);

        if (_PC.getInteractListener() != null)
        {
            float[] or = _PC.getInteractListener().getObjectRotation();
            gl.glRotatef(-or[1], 0.0f, 1.0f, 0.0f);
            gl.glRotatef(-or[0], 1.0f, 0.0f, 0.0f);
        }
        if (t2 != null) gl.glTranslatef(t2[0], t2[1], t2[2]);

        fontProcessor.draw3D(executeRegularRender ? null : label, textBound,
                parsed, fontProcessor._size._actualSize / _fontQualityUpscaling);

        gl.glPopMatrix();
    }

    /**
     * Auxiliary method that instantiates rendering data.
     *
     * @return data to be buffered
     */
    protected BufferData createData()
    {
        _D = getDirectionVectors();
        float[] vertices = new float[(2 + _D._tp.length * 2) * 3];
        short[] indices = new short[(2 + _D._tp.length * 2)];

        vertices[0] = _D._s[0];
        vertices[1] = _D._s[1];
        vertices[2] = _D._s[2];
        vertices[3] = _D._e[0];
        vertices[4] = _D._e[1];
        vertices[5] = _D._e[2];

        int pnt = 6;
        for (int i = 0; i < _D._tp.length; i++)
        {
            vertices[pnt] = _D._tp[i][0];
            vertices[pnt + 1] = _D._tp[i][1];
            vertices[pnt + 2] = _D._tp[i][2];
            vertices[pnt + 3] = _D._tp[i][0] + _D._dt[i][0];
            vertices[pnt + 4] = _D._tp[i][1] + _D._dt[i][1];
            vertices[pnt + 5] = _D._tp[i][2] + _D._dt[i][2];
            pnt += 6;
        }

        assert 2 + _D._tp.length * 2 <= 32767;
        for (short i = 0; i < 2 + _D._tp.length * 2; i++) indices[i] = i;

        return new BufferData(vertices, indices, null);
    }


    /**
     * Can be called to instantiate VBO buffers (but the data is not yet transferred to GPU).
     */
    @Override
    public void createBuffers()
    {
        BufferData D = createData();
        _vbo = new VBOManager(D._vertices, D._indicesShort, null, GL2.GL_LINES, 3, 0);
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
        if (_tickLabelFonts != null)
            for (Font3DProcessor p : _tickLabelFonts)
                p.prepareRenderer(_fontQualityUpscaling);
        _titleFont.prepareRenderer(_fontQualityUpscaling);
    }


    /**
     * Can be called to update data in the VBO that has already been instantiated and sent to GPU  (but the updated is
     * not yet transferred to GPU).
     */
    @Override
    public void updateBuffers()
    {
        BufferData D = createData();
        _vbo.initDataUpdate(D._vertices, D._indicesShort, null, 3, 0);
    }

    /**
     * Can be called to update the VBO that is already instantiated in GPU.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void executeUpdate(GL2 gl)
    {
        if (_vbo != null) _vbo.updateData(gl);
        if (_tickLabelFonts != null)
            for (Font3DProcessor p : _tickLabelFonts)
                p.prepareRenderer(_fontQualityUpscaling);
        _titleFont.prepareRenderer(_fontQualityUpscaling);
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
        if (_tickLabelFonts != null)
            for (Font3DProcessor p : _tickLabelFonts) p.dispose();
        _tickLabelFonts = null;
        _titleFont.dispose();
    }

    /**
     * Can be called to clear memory.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        if (_tickLabelFonts != null)
            for (Font3DProcessor p : _tickLabelFonts) p.dispose();
        _titleFont.dispose();
        _tickLabelFonts = null;
        _tickLabels = null;
        _colorField = null;
        _tickLengthField = null;
        _tickLabelOffsetField = null;
        _titleOffsetField = null;
        _tickLabelFontScaleField = null;
        _titleFontScaleField = null;
        _mainLineWidth = null;
        _tickLineWidth = null;
        _D = null;
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
        _tickLabels = _ticksDataGetter.getLabels();
    }

    /**
     * Setter for the new ticks data getter associated with the dimension represented by the axis.
     * This method also re-initializes the ticks-related font data.
     *
     * @param ticksDataGetter new ticks data getter for the axis
     */
    public void setTicksDataGetter(ITicksDataGetter ticksDataGetter)
    {
        _ticksDataGetter = ticksDataGetter;
        if (_tickLabelFonts != null)
            for (Font3DProcessor p : _tickLabelFonts) p.dispose();
        _tickLabelFonts = null;
        _tickLabelFonts = new Font3DProcessor[ticksDataGetter.getNoTicks()];
        for (int i = 0; i < _tickLabelFonts.length; i++)
            _tickLabelFonts[i] = new Font3DProcessor();
    }

    /**
     * Getter for the new ticks data getter associated with the dimension represented by the axis.
     *
     * @return ticks data getter
     */
    public ITicksDataGetter getTicksDataGetter()
    {
        return _ticksDataGetter;
    }

    /**
     * Returns display range id associated with this axis (0 = X, 1 = Y, 2 = Z).
     *
     * @return display range id
     */
    public int getAssociatedDisplayRangeID()
    {
        return _associatedDisplayRangeID;
    }
}
