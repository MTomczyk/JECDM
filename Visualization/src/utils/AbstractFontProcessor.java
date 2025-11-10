package utils;

import color.Color;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Abstract class supporting the font-related processing.
 *
 * @author MTomczyk
 */

abstract class AbstractFontProcessor
{
    /**
     * Imposes italic font.
     */
    protected static final int IMPOSE_ITALIC = 0;

    /**
     * Imposes subcript.
     */
    protected static final int IMPOSE_SUBSCRIPT = 1;

    /**
     * Imposes superscript.
     */
    protected static final int IMPOSE_SUPERSCRIPT = 1;


    /**
     * Wrapper for text attributes.
     */
    protected static class TextAttributeWrapper
    {
        /**
         * Text attribute.
         */
        public final TextAttribute _attribute;

        /**
         * Attribute value.
         */
        public final Object _value;

        /**
         * Optional beginning index (closed interval).
         */
        public final Integer _bi;

        /**
         * Optional ending index (open interval)
         */
        public final Integer _ei;

        /**
         * Type of the imposed operation.
         */
        public final int _imposedOperation;

        /**
         * Parameterized constructor.
         *
         * @param attribute        text attribute
         * @param value            attribute value
         * @param bi               beginning index (closed interval)
         * @param ei               ending index (open interval)
         * @param imposedOperation type of the imposed operation
         */
        public TextAttributeWrapper(TextAttribute attribute, Object value, int bi, int ei, int imposedOperation)
        {
            _attribute = attribute;
            _value = value;
            _bi = bi;
            _ei = ei;
            _imposedOperation = imposedOperation;
        }

        /**
         * Adds the wrapper's data to the attributed string being built.
         *
         * @param as attributed string
         */
        public void addTo(AttributedString as)
        {
            as.addAttribute(_attribute, _value, _bi, _ei);
        }
    }

    /**
     * Auxiliary text-dependent (dependent on parsing) data container for text attributes. Represents the processor's
     * state.
     */
    protected static class TextDependentState
    {
        /**
         * Analyzed string (original).
         */
        public final String _text;

        /**
         * Parsed string.
         */
        public final String _parsed;

        /**
         * Attributed string.
         */
        public final AttributedString _as;

        /**
         * Font size.
         */
        public float _fontSize;

        /**
         * Reference text bounds.
         */
        public final Rectangle2D _referenceTextBounds;

        /**
         * Parsed text bounds.
         */
        public final Rectangle2D _parsedTextBounds;

        /**
         * Optional text-dependent (dependent on parsing) attributes.
         */
        public final LinkedList<TextAttributeWrapper> _textAttributes;

        /**
         * If true, a regular render is required (bounds do not include superscripts, subscripts, etc.).
         */
        public final boolean _isRegularFontRenderRequired;

        /**
         * Optional italic flags.
         */
        public final boolean[] _ii; // optional italic flags

        /**
         * Optional superscript flags.
         */
        public final boolean[] _supi; // optional superscript flags

        /**
         * Optional subscript flags.
         */
        public final boolean[] _subi;


        /**
         * Parameterized constructor.
         *
         * @param text                        analyzed string (original)
         * @param parsed                      parsed string
         * @param as                          attributed string
         * @param fontSize                    font size
         * @param referenceTextBounds         reference text bounds
         * @param parsedTextBounds            parsed text bounds
         * @param textAttributes              optional text-dependent (dependent on parsing) attributes
         * @param isRegularFontRenderRequired if true, a regular render is required (bounds do not include superscripts,
         *                                    subscripts, etc.)
         * @param ii                          optional italic flags
         * @param subi                        optional subscript flags
         * @param supi                        optional superscript flags
         */
        public TextDependentState(String text,
                                  String parsed,
                                  AttributedString as,
                                  float fontSize,
                                  Rectangle2D referenceTextBounds,
                                  Rectangle2D parsedTextBounds,
                                  LinkedList<TextAttributeWrapper> textAttributes,
                                  boolean isRegularFontRenderRequired,
                                  boolean[] ii,
                                  boolean[] subi,
                                  boolean[] supi)
        {
            //noinspection DuplicatedCode
            _text = text;
            _parsed = parsed;
            _as = as;
            _fontSize = fontSize;
            _referenceTextBounds = referenceTextBounds;
            _parsedTextBounds = parsedTextBounds;
            _textAttributes = textAttributes;
            _isRegularFontRenderRequired = isRegularFontRenderRequired;
            _ii = ii;
            _subi = subi;
            _supi = supi;
        }
    }


    /**
     * Font name.
     */
    public String _fontName = "Times New Roman";

    /**
     * Font size (fixed/relative).
     */
    public Size _size;

    /**
     * Font color.
     */
    public Color _color;

    /**
     * Maintained font.
     */
    public Font _font;

    /**
     * Maintained font (includes superscript).
     */
    protected Font _fontSupers;

    /**
     * Maintained font (includes subscripts).
     */
    protected Font _fontSubs;

    /**
     * Text-dependent attributes.
     */
    protected TextDependentState _tdAttributes;


    /**
     * Auxiliary method that prepare text dependent attributes and sets the object fields (current state).
     * Makes the "get current" methods operative.
     *
     * @param text     input string (text to be parsed)
     * @param fontSize font size
     * @param fdc      font render context (can be null, if delivered by custom extensions)
     */
    public void prepareTextDependentState(String text, float fontSize, FontRenderContext fdc)
    {
        if ((_tdAttributes != null)
                && (_tdAttributes._text.equals(text))
                && (Float.compare(fontSize, _tdAttributes._fontSize) == 0)) return; // skip, data already set
        _tdAttributes = parseTextDependentState(text, fontSize, fdc);
    }

    /**
     * Returns the current state's parsed text.
     *
     * @return parsed text
     */
    public String getCurrentParsedText()
    {
        if (_tdAttributes == null) return null;
        else return _tdAttributes._parsed;
    }

    /**
     * Returns the current state's reference text bounds.
     *
     * @return reference text bounds
     */
    public Rectangle2D getCurrentReferenceTextBounds()
    {
        if (_tdAttributes == null) return null;
        else return _tdAttributes._referenceTextBounds;
    }


    /**
     * Returns the current state's parsed text bounds.
     *
     * @return parsed text bounds
     */
    public Rectangle2D getCurrentParsedTextBounds()
    {
        if (_tdAttributes == null) return null;
        else return _tdAttributes._parsedTextBounds;
    }


    /**
     * Returns the current state's attributed string.
     *
     * @return attributed string
     */
    public AttributedString getCurrentAttributedString()
    {
        if (_tdAttributes == null) return null;
        else return _tdAttributes._as;
    }

    /**
     * Returns the current state's flag indicating whether to use the regular render.
     *
     * @return flag indicating whether to use the regular render
     */
    public boolean getIsRegularFontRenderRequiredFlag()
    {
        if (_tdAttributes == null) return true;
        else return _tdAttributes._isRegularFontRenderRequired;
    }


    /**
     * Auxiliary parser for text-dependent attributes. Current implementation detects only: <br>
     * - superscripts (e.g., f$_1$ or f$_{1}$), <br>
     * - subscripts (e.g., f$^1$ or f$^{1}$ <br>
     *
     * @param text     text to be parsed
     * @param fontSize font size
     * @param frc      font render context (can be null, if delivered by custom extensions)
     * @return text-dependent attributes (null, if the input is null).
     */
    @SuppressWarnings({"DataFlowIssue", "ConstantValue", "UnusedAssignment", "DuplicatedCode"})
    protected TextDependentState parseTextDependentState(String text, float fontSize, FontRenderContext frc)
    {
        if ((text == null) || (text.isEmpty())) return null;
        LinkedList<TextAttributeWrapper> taw = new LinkedList<>();

        boolean eie = false; // escape in effect flag
        boolean dip = false; // pattern in progress flag (dollar)
        boolean subp = false; // patten in progress flag (sub)
        boolean supp = false; // patten in progress flag (sup)

        boolean brackets = false; // brackets flag
        int dbi = -1; // dollar beginning index
        int subbi = -1;
        int subei = -1;
        int supbi = -1;
        int supei = -1;
        int skipped = 0;

        boolean cItalics = false; // contains italics
        boolean cSuper = false; // contains superscripts
        boolean cSub = false; // contains subscripts
        boolean[] ii = null; // optional italic flags
        boolean[] supi = null; // optional superscript flags
        boolean[] subi = null; // optional subscript flags

        StringBuilder sb = new StringBuilder(); // preparse parse text

        Font currentItalic = null; // current italic font
        LinkedList<TextAttributeWrapper> stacked = new LinkedList<>(); // stacked wrappers

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c == '\\')
            {
                if (!eie)
                {
                    eie = true;
                    skipped++;
                }
                else
                {
                    sb.append('\\');
                    eie = false;
                }
            }
            else
            {
                if ((!dip) && (c == '$') && (!eie))
                {
                    dip = true;
                    dbi = i - skipped;
                    skipped++;
                    subp = false;
                    brackets = false;
                    currentItalic = _font.deriveFont(Font.ITALIC);
                    stacked.clear();

                }
                else if ((subp) && (c == '{') && (!eie) && (!brackets))
                {
                    brackets = true;
                    skipped++;
                }
                else if ((subp) && (c == '}') && (!eie) && (brackets))
                {
                    subp = false;
                    brackets = false;
                    skipped++;
                    subei = i - skipped + 1;
                    if (subei > subbi)
                    {
                        Font tmp;
                        if (dip) tmp = currentItalic.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                                TextAttribute.SUPERSCRIPT_SUB));
                        else tmp = _font.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                                TextAttribute.SUPERSCRIPT_SUB));

                        TextAttributeWrapper TAW = new TextAttributeWrapper(
                                TextAttribute.FONT, tmp, subbi, subei, IMPOSE_SUBSCRIPT);
                        if (dip) stacked.add(TAW);
                        else taw.add(TAW);

                        cSub = true;
                    }
                    subbi = -1;
                    subei = -1;
                }
                else if ((supp) && (c == '{') && (!eie) && (!brackets))
                {
                    brackets = true;
                    skipped++;
                }
                else if ((supp) && (c == '}') && (!eie) && (brackets))
                {
                    supp = false;
                    brackets = false;
                    skipped++;
                    supei = i - skipped + 1;
                    if (supei > supbi)
                    {
                        Font tmp;
                        if (dip) tmp = currentItalic.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                                TextAttribute.SUPERSCRIPT_SUPER));
                        else tmp = _font.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                                TextAttribute.SUPERSCRIPT_SUPER));

                        TextAttributeWrapper TAW = new TextAttributeWrapper(TextAttribute.FONT,
                                tmp, supbi, supei, IMPOSE_SUPERSCRIPT);
                        if (dip) stacked.add(TAW);
                        else taw.add(TAW);

                        cSuper = true;
                    }
                    supbi = -1;
                    supei = -1;
                }
                else if ((brackets) && (subp || supp))
                {
                    if (!eie) sb.append(c);
                    else eie = false;
                }
                else if ((!subp) && (c == '_') && (!eie))
                {
                    subp = true;
                    brackets = false;
                    subbi = i - skipped;
                    skipped++;
                }
                else if ((!supp) && (c == '^') && (!eie))
                {
                    supp = true;
                    brackets = false;
                    supbi = i - skipped;
                    skipped++;
                }
                else if ((subp) && (!eie) && (!brackets))
                {
                    subp = false;
                    brackets = false;
                    subei = i - skipped + 1;
                    sb.append(c);
                    Font tmp;
                    if (dip) tmp = currentItalic.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                            TextAttribute.SUPERSCRIPT_SUB));
                    else tmp = _font.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                            TextAttribute.SUPERSCRIPT_SUB));

                    TextAttributeWrapper TAW = new TextAttributeWrapper(
                            TextAttribute.FONT, tmp, subbi, subei, IMPOSE_SUBSCRIPT);
                    if (dip) stacked.add(TAW);
                    else taw.add(TAW);

                    cSub = true;

                    subbi = -1;
                    subei = -1;
                }
                else if ((supp) && (!eie) && (!brackets))
                {
                    supp = false;
                    brackets = false;
                    supei = i - skipped + 1;
                    sb.append(c);
                    Font tmp;
                    if (dip) tmp = currentItalic.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                            TextAttribute.SUPERSCRIPT_SUPER));
                    else tmp = _font.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                            TextAttribute.SUPERSCRIPT_SUPER));

                    TextAttributeWrapper TAW = new TextAttributeWrapper(
                            TextAttribute.FONT, tmp, supbi, supei, IMPOSE_SUPERSCRIPT);
                    if (dip) stacked.add(TAW);
                    else taw.add(TAW);

                    cSuper = true;

                    supbi = -1;
                    supei = -1;
                }
                else if ((dip) && (c == '$') && (!eie))
                {
                    skipped++;
                    if (i - skipped - dbi + 1 > 0) // in effect
                        taw.add(new TextAttributeWrapper(TextAttribute.FONT, currentItalic,
                                dbi, i - skipped + 1, IMPOSE_ITALIC));
                    taw.addAll(stacked);
                    stacked.clear();
                    dip = false;
                    dbi = -1;
                    subbi = -1;
                    subei = -1;
                    supbi = -1;
                    supei = -1;
                    subp = false;
                    supp = false;
                    brackets = false;
                    currentItalic = null;
                    cItalics = true;
                }
                else
                {
                    sb.append(c);
                    eie = false;
                }
            }
        }

        if (!stacked.isEmpty()) taw.addAll(stacked);

        String parsed = sb.toString();
        AttributedString as = getAttributedStringFromPreparedData(parsed, fontSize, taw);

        Rectangle2D refTextBounds;
        Rectangle2D parsedTextBounds;

        boolean rrr = true;
        if (cSuper || cSub || cItalics) // need to derive unique fonts
        {
            rrr = false;

            ii = new boolean[parsed.length()];
            subi = new boolean[parsed.length()];
            supi = new boolean[parsed.length()];

            for (TextAttributeWrapper t : taw)
            {
                if (t._imposedOperation == IMPOSE_ITALIC)
                    for (int i = t._bi; i < t._ei; i++) ii[i] = true;
                else if (t._imposedOperation == IMPOSE_SUBSCRIPT)
                    for (int i = t._bi; i < t._ei; i++) subi[i] = true;
                else if (t._imposedOperation == IMPOSE_SUPERSCRIPT)
                    for (int i = t._bi; i < t._ei; i++) supi[i] = true;
            }

            parsedTextBounds = getNotRegularTextBounds(as, parsed, ii, subi, supi, frc);

        }
        else parsedTextBounds = getRegularTextBounds(parsed, frc);

        refTextBounds = getReferenceTextBounds(as, frc);

        return new TextDependentState(text, sb.toString(), as, fontSize, refTextBounds, parsedTextBounds,
                taw, rrr, ii, subi, supi);
    }

    /**
     * Calculates correct bounds for the attributed string.
     *
     * @param as     attributed string
     * @param parsed parsed string
     * @param frc    font rendering context
     * @param ii     italic character flags
     * @param subi   subscript character flags
     * @param supi   superscript character flags
     * @return correct bounds of the attributed string
     */
    protected Rectangle2D getCorrectBounds(AttributedString as,
                                           String parsed,
                                           FontRenderContext frc,
                                           boolean[] ii,
                                           boolean[] subi,
                                           boolean[] supi)
    {
        AttributedCharacterIterator it = as.getIterator();
        char c = it.first();
        Font tmp = _font.deriveFont(it.getAttributes());
        if (ii[0]) tmp = tmp.deriveFont(Font.ITALIC);
        if (subi[0]) tmp = tmp.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                TextAttribute.SUPERSCRIPT_SUB));
        if (supi[0]) tmp = tmp.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                TextAttribute.SUPERSCRIPT_SUPER));
        Rectangle2D r = tmp.createGlyphVector(frc, new char[]{c}).getVisualBounds();

        double x = r.getX();
        double w = r.getWidth();
        double y = r.getY();
        double yh = y + r.getHeight();

        for (int i = 1; i < parsed.length(); i++)
        {
            c = it.next();
            tmp = _font.deriveFont(it.getAttributes());
            if (ii[i]) tmp = tmp.deriveFont(Font.ITALIC);
            if (subi[i]) tmp = tmp.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                    TextAttribute.SUPERSCRIPT_SUB));
            else if (supi[i]) tmp = tmp.deriveFont(Collections.singletonMap(TextAttribute.SUPERSCRIPT,
                    TextAttribute.SUPERSCRIPT_SUPER));
            r = tmp.createGlyphVector(frc, new char[]{c}).getLogicalBounds();
            w += r.getWidth();
            if (Double.compare(r.getY(), y) < 0) y = r.getY();
            if (Double.compare(r.getY() + r.getHeight(), yh) > 0) yh = r.getY() + r.getHeight();
        }
        return new Rectangle2D.Double(x, y, w, yh - y);
    }


    /**
     * Auxiliary method for calculating dimensions (width, height) of a text to be rendered. Helps to align the text. TO
     * BE OVERWRITTEN. The method should use the main font.
     *
     * @param parsed parsed text
     * @param frc    font render context (can be null, if delivered by custom extensions)
     * @return rectangle object containing information on the glyph visual properties
     */
    protected Rectangle2D getRegularTextBounds(String parsed, FontRenderContext frc)
    {
        if (frc == null) return null;
        GlyphVector gv = _font.createGlyphVector(frc, parsed);
        return gv.getVisualBounds();
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
    protected Rectangle2D getNotRegularTextBounds(AttributedString as,
                                                  String parsed,
                                                  boolean[] ii,
                                                  boolean[] subi,
                                                  boolean[] supi,
                                                  FontRenderContext fdc)
    {
        if (fdc == null) return null;
        return getCorrectBounds(as, parsed, fdc, ii, subi, supi);
    }


    /**
     * Auxiliary method for calculating dimensions (width, height) of a reference text to be rendered: "REFERENCE TEXT"
     * (without diacritics and other letters that exceed ascend/baseline). Helps to align the text. TO BE OVERWRITTEN
     *
     * @param as  current state's attributed string
     * @param frc font render context (can be null, if delivered by custom extensions)
     * @return rectangle object containing information on the glyph visual properties
     */
    protected Rectangle2D getReferenceTextBounds(AttributedString as, FontRenderContext frc)
    {
        return getRegularTextBounds("REFERENCE TEXT", frc);
    }


    /**
     * Returns the attributed string from the prepared data.
     *
     * @param parsed   parsed text
     * @param fontSize font size
     * @param taw      text attribute wrappers
     * @return attributed string
     */
    protected AttributedString getAttributedStringFromPreparedData(String parsed, float fontSize, LinkedList<TextAttributeWrapper> taw)
    {
        AttributedString as = new AttributedString(parsed);
        if (!parsed.isEmpty())
        {
            as.addAttribute(TextAttribute.FONT, new Font(_fontName, Font.PLAIN, Projection.getP(fontSize)));
            as.addAttribute(TextAttribute.SIZE, Projection.getP(fontSize));
            as.addAttribute(TextAttribute.TRACKING, 1E-16);
            if (taw != null)
            {
                for (TextAttributeWrapper w : taw)
                    w.addTo(as);
            }
        }
        return as;
    }


    /**
     * Default constructor.
     */
    protected AbstractFontProcessor()
    {
        this(null);
    }

    /**
     * Parameterized constructor.
     *
     * @param font initial font to be set
     */
    protected AbstractFontProcessor(Font font)
    {
        _size = new Size();
        _font = font;
    }

    /**
     * Clears data.
     */
    public void dispose()
    {
        _font = null;
        _fontSubs = null;
        _fontSupers = null;
        _size = null;
        _color = null;
        _fontName = null;
        _tdAttributes = null;
    }
}
