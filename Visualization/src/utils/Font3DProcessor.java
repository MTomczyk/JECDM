package utils;

import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.util.HashMap;

/**
 * Abstract class supporting the font-related processing (Open GL).
 *
 * @author MTomczyk
 */

public class Font3DProcessor extends AbstractFontProcessor
{
    /**
     * Overwritten render delegate.
     */
    static class AttributedRenderDelegate extends TextRenderer.DefaultRenderDelegate
    {
        /**
         * Parent font processor.
         */
        private final AbstractFontProcessor _apf;

        /**
         * Main font (bypasses input).
         */
        private Font _font;

        /**
         * Attributed string that is used to bypass the input of
         * {@link AttributedRenderDelegate#draw(Graphics2D, String, int, int)}.
         */
        private AttributedString _as = null;

        /**
         * Pre-cached bound.
         */
        protected Rectangle2D _precachedBound;

        /**
         * Optional italic flags.
         */
        private boolean[] _ii; // optional italic flags

        /**
         * Optional superscript flags.
         */
        private boolean[] _supi; // optional superscript flags

        /**
         * Optional subscript flags.
         */
        private boolean[] _subi;

        /**
         * Parameterized constructor.
         *
         * @param apf parent font processor
         */
        protected AttributedRenderDelegate(AbstractFontProcessor apf)
        {
            _apf = apf;
        }

        /**
         * Current font (bypasses input).
         *
         * @param font font
         */
        protected void setFont(Font font)
        {
            _font = font;
        }

        /**
         * Setter for the pre-cached bound.
         *
         * @param precachedBound precached bound
         */
        protected void setPrecachedBound(Rectangle2D precachedBound)
        {
            _precachedBound = precachedBound;
        }

        /**
         * Setter for the auxiliary character flags
         *
         * @param ii   optional italic flags
         * @param subi optional subscript flags
         * @param supi optional superscript flags
         */
        protected void setAuxiliaryCharacterFlags(boolean[] ii, boolean[] subi, boolean[] supi)
        {
            _ii = ii;
            _subi = subi;
            _supi = supi;
        }

        /**
         * Setter for the attributed string that is used to bypass the input of
         * {@link AttributedRenderDelegate#getBounds(String, Font, FontRenderContext)}
         *
         * @param as attributed string
         */
        protected void setAttributedString(AttributedString as)
        {
            _as = as;
        }

        /**
         * Overwritten method. Delegates the processing to the second get bounds method.
         *
         * @param var1 var1 (string; not used; the class main field is used instead).
         * @param var2 var2 (font)
         * @param var3 var3 (render context)
         * @return bounds
         */
        @Override
        public Rectangle2D getBounds(CharSequence var1, Font var2, FontRenderContext var3)
        {
            return getBounds((String) var1, var2, var3);
        }


        /**
         * Overwritten method.
         *
         * @param var1 var1 (string)
         * @param var2 var2 (font)
         * @param var3 var3 (render context)
         * @return bounds
         */
        @Override
        public Rectangle2D getBounds(String var1, java.awt.Font var2, FontRenderContext var3)
        {
            if (_as == null) return getBounds(_font.createGlyphVector(var3, var1), var3);
            if (_precachedBound != null) return _precachedBound;
            return _apf.getCorrectBounds(_as, var1, var3, _ii, _subi, _supi);
        }

        /**
         * Overwritten method.
         *
         * @param var1 var1 (graphics context)
         * @param var2 var2 (string; not used; the class main field is used instead)
         * @param var3 var3 (placement)
         * @param var4 var4 (placement)
         */
        @Override
        public void draw(Graphics2D var1, String var2, int var3, int var4)
        {
            if (_as == null) var1.drawString(var2, var3, var4);
            else var1.drawString(_as.getIterator(), var3, var4);
        }
    }

    /**
     * Text rendered: used for drawing 3D text.
     */
    private TextRenderer _renderer = null;

    /**
     * Attribute render delegate.
     */
    private AttributedRenderDelegate _ard;

    /**
     * Default constructor.
     */
    public Font3DProcessor()
    {
        super();
    }

    /**
     * Draws the attributed string.
     *
     * @param as       attributed string (if null, a regular render is executed; excludes superscripts, etc.)
     * @param bound    (pre-cached) text bound
     * @param parsed   parsed string
     * @param fontSize font size
     */
    public void draw3D(AttributedString as, Rectangle2D bound, String parsed, float fontSize)
    {
        _renderer.begin3DRendering();
        _ard.setAttributedString(as);
        _ard.setPrecachedBound(bound);
        _renderer.draw3D(parsed, 0.0f, 0.0f, 0.0f, fontSize); // not used, the attributed string is used
        _ard.setPrecachedBound(null);
        _ard.setAttributedString(null);
        _renderer.end3DRendering();
    }


    /**
     * Prepares Open GL text renderer. Requires having a current GL context set.
     *
     * @param fontQualityUpscaling font quality upscaling factor (the higher, the higher the upscaling level; thus
     *                             better quality)
     */
    public void prepareRenderer(float fontQualityUpscaling)
    {
        if (_renderer != null) _renderer.dispose();
        if (_ard == null) _ard = new AttributedRenderDelegate(this);
        _font = new Font(_fontName, Font.PLAIN, Projection.getP(24.0f * fontQualityUpscaling));
        HashMap<TextAttribute, Object> atts = new HashMap<>();
        atts.put(TextAttribute.SIZE, Projection.getP(24.0f * fontQualityUpscaling));
        atts.put(TextAttribute.TRACKING, 1E-16);
        _font = _font.deriveFont(atts);
        _ard.setFont(_font);
        _renderer = new TextRenderer(_font, true, true, _ard, true);
    }

    /**
     * Setter for the renderer's colors (determines the font color)
     *
     * @param c font color
     */
    public void setColor(Color c)
    {
        _renderer.setColor(c);
    }


    /**
     * Auxiliary method for calculating dimensions (width, height) of a text to be rendered. Helps to align the text.
     * The method should use a dynamic font dedicated to superscript, etc.
     *
     * @param as     attributed string
     * @param parsed parsed string
     * @param ii     italic flags
     * @param subi   subscript flags
     * @param supi   superscript flags
     * @param fdc    font render context (can be null, if delivered by custom extensions)
     * @return rectangle object containing information on the glyph visual properties
     */
    @Override
    protected Rectangle2D getNotRegularTextBounds(AttributedString as, String parsed,
                                                  boolean[] ii, boolean[] subi, boolean[] supi, FontRenderContext fdc)
    {
        _ard.setAttributedString(as); // use attributed string
        _ard.setAuxiliaryCharacterFlags(ii, subi, supi);
        Rectangle2D r = _renderer.getBounds(parsed);
        _ard.setAuxiliaryCharacterFlags(null, null, null);
        _ard.setAttributedString(null);
        return r;
    }

    /**
     * Auxiliary method for calculating dimensions (width, height) of a text to be rendered. Helps to align the text.
     * The method should use the main font.
     *
     * @param parsed parsed text
     * @param frc font render context (can be null, if delivered by custom extensions)
     * @return rectangle object containing information on the glyph visual properties
     */
    @Override
    protected Rectangle2D getRegularTextBounds(String parsed, FontRenderContext frc)
    {
        _ard.setAttributedString(null); // use attributed string
        return _renderer.getBounds(parsed);
    }

    /**
     * Auxiliary method for calculating dimensions (width, height) of a reference text to be rendered: "REFERENCE TEXT"
     * (without diacritics and other letters that exceed ascend/baseline). Helps to align the text.
     *
     * @param as current state's attributed string
     * @param frc font render context (can be null, if delivered by custom extensions)
     * @return rectangle object containing information on the glyph visual properties
     */
    @Override
    protected Rectangle2D getReferenceTextBounds(AttributedString as, FontRenderContext frc)
    {
        _ard.setAttributedString(null); // use attributed string
        Rectangle2D r = _renderer.getBounds("REFERENCE TEXT"); // do not use the original input;
        _ard.setAttributedString(null);
        return r;
    }

    /**
     * Clears data.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        if (_renderer != null) _renderer.dispose();
        _renderer = null;
        _ard = null;
    }
}
