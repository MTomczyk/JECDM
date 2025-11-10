package component.title;

import component.AbstractSwingComponent;
import container.PlotContainer;
import scheme.AbstractScheme;
import scheme.enums.*;
import utils.FontProcessor;
import utils.Projection;
import utils.Size;

import java.awt.*;
import java.awt.geom.Rectangle2D;


/**
 * Class representing plot title.
 *
 * @author MTomczyk
 */


public abstract class AbstractTitle extends AbstractSwingComponent
{
    /**
     * Params container.
     */
    public static class Params extends AbstractSwingComponent.Params
    {
        /**
         * Plot title to be displayed.
         */
        public String _title;

        /**
         * Parameterized constructor.
         *
         * @param title title to be displayed
         * @param name  component name
         * @param PC    plot container: allows accessing various plot components
         */
        public Params(String title, String name, PlotContainer PC)
        {
            super(name, PC);
            _title = title;
        }
    }

    /**
     * Parameterized constructor
     *
     * @param p params container
     */
    public AbstractTitle(Params p)
    {
        super(p);
        _title = p._title;
        _font = new FontProcessor();
        _offset = new Size();
    }

    /**
     * Plot title to be displayed.
     */
    private final String _title;

    /**
     * Used font.
     */
    private final FontProcessor _font;

    /**
     * Offset (distance from the border of the drawing area).
     */
    private final Size _offset;

    /**
     * Called to update the component appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc).
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        super.updateScheme(scheme);

        float RV = _PC.getReferenceValueGetter().getReferenceValue();

        _align = scheme.getAlignments(_surpassedAlignments, AlignFields.TITLE);
        _backgroundColor = scheme.getColors(_surpassedColors, ColorFields.TITLE_BACKGROUND);

        _borderColor = scheme.getColors(_surpassedColors, ColorFields.TITLE_BORDER);
        _borderWidth.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.TITLE_BORDER_WIDTH_FIXED));
        _borderWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.TITLE_BORDER_WIDTH_RELATIVE_MULTIPLIER));
        _borderWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.TITLE_BORDER_USE_RELATIVE_WIDTH));
        _borderStroke = getStroke(_borderWidth);

        _font._fontName = scheme.getFonts(_surpassedFonts, FontFields.TITLE);
        _font._size.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.TITLE_FONT_SIZE_FIXED));
        _font._size.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.TITLE_FONT_SIZE_RELATIVE_MULTIPLIER));
        _font._size.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.TITLE_FONT_USE_RELATIVE_SIZE));
        _font._size.computeActualSize(RV);
        _font._color = scheme.getColors(_surpassedColors, ColorFields.TITLE_FONT);

        _offset.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.TITLE_OFFSET_FIXED));
        _offset.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.TITLE_OFFSET_RELATIVE_MULTIPLIER));
        _offset.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.TITLE_OFFSET_USE_RELATIVE_SIZE));
        _offset.computeActualSize(RV);

        setOpaque(scheme.getFlags(_surpassedFlags, FlagFields.TITLE_OPAQUE));
    }

    /**
     * Updates bounds of the primary drawing area (should be enclosed within the panel bounds).
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    @Override
    public void setPrimaryDrawingArea(int x, int y, int w, int h)
    {
        super.setPrimaryDrawingArea(x, y, w, h);
        updateRelativeFields();
    }

    /**
     * Method for updating relative fields values ({@link scheme.referencevalue.IReferenceValueGetter}).
     */
    @Override
    public void updateRelativeFields()
    {
        float RV = _PC.getReferenceValueGetter().getReferenceValue();

        if ((_align == Align.TOP) || (_align == Align.BOTTOM) ||
                (_align == Align.LEFT) || (_align == Align.RIGHT))
        {
            _font._size.computeActualSize(RV);
            _offset.computeActualSize(RV);
        } else _font._size.computeActualSize(0.0f);

        _font.prepareFont();
    }

    /**
     * Method for drawing the element.
     *
     * @param g Java AWT Graphics context
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics g2 = g.create();
        Graphics2D g2d = (Graphics2D) g2;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBorder(g2);

        g2.setFont(_font._font);
        if (_font._color != null) g2.setColor(_font._color);

        _font.prepareTextDependentState(_title, _font._size._actualSize, g2d.getFontRenderContext());
        Rectangle2D dRef = _font.getCurrentReferenceTextBounds();
        Rectangle2D d = _font.getCurrentParsedTextBounds();

        if (_align == Align.TOP)
        {
            float x = _translationVector[0] + _primaryDrawingArea.width / 2.0f - (float) d.getWidth() / 2.0f - (float) d.getMinX() / 2.0f;
            float y = _translationVector[1] + _primaryDrawingArea.height - _offset._actualSize;
            g2.drawString(_font.getCurrentAttributedString().getIterator(),
                    Projection.getP(x), Projection.getP(y));
        } else if (_align == Align.BOTTOM)
        {
            float x = _translationVector[0] + _primaryDrawingArea.width / 2.0f - (float) d.getWidth() / 2.0f - (float) d.getMinX() / 2.0f;
            float y = _translationVector[1] + _offset._actualSize + (float) dRef.getHeight();
            g2.drawString(_font.getCurrentAttributedString().getIterator(),
                    Projection.getP(x), Projection.getP(y));
        } else if (_align == Align.LEFT)
        {
            float x = _translationVector[0] + _primaryDrawingArea.width - _offset._actualSize;
            float y = _translationVector[1] + _primaryDrawingArea.height / 2.0f + (float) d.getWidth() / 2.0f + (float) d.getMinX();
            FontProcessor.drawRotatedString(g2d, _font.getCurrentAttributedString().getIterator(),
                    x, y, (float) (-Math.PI / 2.0f));
        } else if (_align == Align.RIGHT)
        {
            float x = _translationVector[0] + _offset._actualSize;
            float y = _translationVector[1] + _primaryDrawingArea.height / 2.0f - (float) d.getWidth() / 2.0f - (float) d.getMinX();
            FontProcessor.drawRotatedString(g2d, _font.getCurrentAttributedString().getIterator(),
                    x, y, (float) (Math.PI / 2.0f));
        }

        g2.dispose();
    }
}
