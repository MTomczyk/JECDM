package utils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;

/**
 * Abstract class supporting the font-related processing (Java AWT).
 *
 * @author MTomczyk
 */

public class FontProcessor extends AbstractFontProcessor
{
    /**
     * Default constructor.
     */
    public FontProcessor()
    {
        super();
    }

    /**
     * Parameterized constructor.
     *
     * @param font initial font to be set
     */
    public FontProcessor(Font font)
    {
        super(font);
    }

    /**
     * Updates the font data. Dedicated to 2D visualization.
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
     * @deprecated to be deleted
     */
    @Deprecated
    public static Rectangle2D getReferenceTextBounds(Graphics2D g2d)
    {
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = g2d.getFont().createGlyphVector(frc, "REFERENCE TEXT");
        return gv.getVisualBounds();
    }

    /**
     * Auxiliary method for calculating dimensions (width, height) of a rendered text.
     *
     * @param g2d    Java AWT graphics 2D context
     * @param string text to be displayed
     * @return rectangle object containing information on the glyph visual properties
     */
    @Deprecated
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
     * @param it     attributed character iterator
     * @param x      x-coordinate
     * @param y      y-coordinate
     * @param rotate angle (radians)
     */
    public static void drawRotatedString(Graphics2D g2d, AttributedCharacterIterator it, float x, float y, float rotate)
    {
        g2d.translate(x, y);
        g2d.rotate(rotate);
        g2d.drawString(it, 0, 0);
        g2d.rotate(-rotate);
        g2d.translate(-x, -y);
    }

    /**
     * Draws rotated string.
     *
     * @param g2d    Java AWT Graphics 2D context
     * @param text   text to be drawn
     * @param x      x-coordinate
     * @param y      y-coordinate
     * @param rotate angle (radians)
     * @deprecated to be deleted
     */
    @Deprecated
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
    @Override
    public void dispose()
    {
        super.dispose();
        _font = null;
    }
}
