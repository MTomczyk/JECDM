package component.axis;

import scheme.enums.Align;
import utils.Font;
import utils.Projection;
import utils.Size;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Class representing a tick (axis element).
 *
 * @author MTomczyk
 */
public class Tick
{
    /**
     * Tick unique id (number, starts from 0).
     */
    public int _tickNo = 0;

    /**
     * Tick label font.
     */
    public Font _labelFont;

    /**
     * Tick color;
     */
    public Color _axColor;

    /**
     * Tick line stroke.
     */
    public Stroke _lineStroke;

    /**
     * Tick line width.
     */
    public Size _lineWidth;

    /**
     * Tick size.
     */
    public Size _size;

    /**
     * Offset = distance between the axis and the tick labels.
     */
    public Size _labelOffset;

    /**
     * Tick label.
     */
    public String _label;

    /**
     * Auxiliary flag that allows skipping drawing selected tick (flag true -> i-th tick is not drawn)
     */
    public boolean _tickDisabled = false;

    /**
     * Auxiliary flag that allows skipping drawing selected tick labels (flag true -> i-th tick label is not drawn)
     */
    public boolean _labelDisabled = false;

    /**
     * Main setter for all major fields.
     *
     * @param tickNo      tick unique id (number, starts from 0)
     * @param labelFont   font used when rendering tick label
     * @param axColor     tick color
     * @param lineStroke  tick line stroke
     * @param lineWidth   tick line width
     * @param size        tick size
     * @param labelOffset offset: distance between the label and the end of the tick
     */
    public void setAll(int tickNo, Font labelFont, Color axColor, Stroke lineStroke, Size lineWidth, Size size, Size labelOffset)
    {
        _tickNo = tickNo;
        _labelFont = labelFont;
        _axColor = axColor;
        _lineStroke = lineStroke;
        _lineWidth = lineWidth;
        _size = size;
        _labelOffset = labelOffset;
    }

    /**
     * Auxiliary method for rendering the tick (just line).
     *
     * @param g  Java AWT Graphics context
     * @param DV supportive object helping for determining coordinates for rendering
     */
    public void drawTick(Graphics g, DirectionVectors DV)
    {
        if (_tickDisabled) return;

        if ((_lineStroke != null) && (Double.compare(_size._actualSize, 0.0d) > 0))
        {
            g.setColor(_axColor);
            ((Graphics2D) g).setStroke(_lineStroke);
            float xb = DV._s[0] + DV._tp[_tickNo][0];
            float yb = DV._s[1] + DV._tp[_tickNo][1];
            float xe = xb + DV._dt[_tickNo][0];
            float ye = yb + DV._dt[_tickNo][1];
            g.drawLine(Projection.getP(xb), Projection.getP(yb), Projection.getP(xe), Projection.getP(ye));
        }
    }


    /**
     * Auxiliary method drawing tick labels.
     *
     * @param g     Java AWT Graphics context
     * @param DV    supportive object helping for determining coordinates for rendering
     * @param align axis alignment
     */
    public void drawTickLabels(Graphics g, DirectionVectors DV, Align align)
    {
        // TICK LABELS
        if (_label == null) return;
        if (_tickDisabled) return;
        if (_labelDisabled) return;

        g.setFont(_labelFont._font);
        g.setColor(_labelFont._color);

        float xb = DV._s[0] + DV._tp[_tickNo][0];
        float yb = DV._s[1] + DV._tp[_tickNo][1];
        Rectangle2D dRef = Font.getReferenceTextCorrectDimensions((Graphics2D) g);
        Rectangle2D d = Font.getCorrectDimensions((Graphics2D) g, _label);

        float tlx = xb + DV._dtl[_tickNo][0] - (float) d.getWidth() - (float) d.getMinX();
        float tly = yb + DV._dtl[_tickNo][1] + (float) dRef.getHeight() / 2.0f;

        if (align == Align.BOTTOM)
        {
            tlx = xb + DV._dtl[_tickNo][0] - (float) d.getWidth() / 2.0f - (float) d.getMinX() / 2.0f;
            tly = yb + DV._dtl[_tickNo][1] + (float) dRef.getHeight();
        }
        else if (align == Align.RIGHT)
        {
            tlx = xb + DV._dtl[_tickNo][0];
            tly = yb + DV._dtl[_tickNo][1] + (float) dRef.getHeight() / 2.0f;
        }
        else if (align == Align.TOP)
        {
            tlx = xb + DV._dtl[_tickNo][0] - (float) d.getWidth() / 2.0f - (float) d.getMinX() / 2.0f;
            tly = yb + DV._dtl[_tickNo][1];
        }

        g.drawString(_label, Projection.getP(tlx), Projection.getP(tly));
    }

    /**
     * Clears data.
     */
    public void dispose()
    {
        _label = null;
        _axColor = null;
        _lineStroke = null;
        _labelFont = null;
    }

}
