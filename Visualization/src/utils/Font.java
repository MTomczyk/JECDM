package utils;

import color.Color;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

/**
 * Class representing a font.
 * Wrapper of {@link java.awt.Font}.
 * Stores useful additional fields and provides various functionalities.
 *
 * @author MTomczyk
 */

public class Font
{
    /**
     * Font name.
     */
    public String _fontName;

    /**
     * Font size (fixed/relative).
     */
    public Size _size;

    /**
     * Java AWT font.
     */
    public java.awt.Font _font;

    /**
     * Font color.
     */
    public Color _color;

    /**
     * Text rendered: used for drawing 3D text.
     */
    public TextRenderer _renderer = null;

    /**
     * Default constructor.
     */
    public Font()
    {
        _size = new Size();
    }

    /**
     * Prepares Open GL text renderer. Requires having a current GL context set.
     */
    public void prepareRenderer()
    {
        if (_renderer != null) _renderer.dispose();
        _renderer = new TextRenderer(new java.awt.Font(_fontName, java.awt.Font.PLAIN, 24));
    }

    /**
     * Creates the font object.
     */
    public void prepareFont()
    {
        _font = new java.awt.Font(_fontName, java.awt.Font.PLAIN, Projection.getP(_size._actualSize));
    }

    /**
     * Auxiliary method for calculating dimensions (width, height) of a reference text to be rendered: "REFERENCE TEXT"
     * (without diacritics and other letters that exceed ascend/baseline). Helps to align the text.
     *
     * @param g2d Java AWT graphics 2D context
     * @return rectangle object containing information on the glyph visual properties
     */
    public static Rectangle2D getReferenceTextCorrectDimensions(Graphics2D g2d)
    {
        return getCorrectDimensions(g2d, "REFERENCE TEXT");
    }

    /**
     * Auxiliary method for calculating dimensions (width, height) of a rendered text.
     *
     * @param g2d    Java AWT graphics 2D context
     * @param string text to be displayed
     * @return rectangle object containing information on the glyph visual properties
     */
    public static Rectangle2D getCorrectDimensions(Graphics2D g2d, String string)
    {
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = g2d.getFont().createGlyphVector(frc, string);
        return gv.getVisualBounds();
    }

    /**
     * Draws rotated string.
     *
     * @param g2d    Java AWT Graphics 2D context
     * @param text   text to be drawn
     * @param x      x-coordinate
     * @param y      y-coordinate
     * @param rotate angle (radians)
     */
    public static void drawRotatedString(Graphics2D g2d, String text, float x, float y, float rotate)
    {
        g2d.translate(x, y);
        g2d.rotate(rotate);
        g2d.drawString(text, 0, 0);
        g2d.rotate(-rotate);
        g2d.translate(-x, -y);
    }

    /**
     * Clears data.
     */
    public void dispose()
    {
        _size = null;
        _color = null;
        _font = null;
        if (_renderer != null) _renderer.dispose();
        _renderer = null;
        _fontName = null;
    }
}
