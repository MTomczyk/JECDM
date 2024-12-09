package component.legend;

import color.gradient.Gradient;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;

import java.awt.*;

/**
 * Interfaces for classes responsible for rendering legend entries.
 *
 * @author MTomczyk
 */
public interface IEntryPainter
{
    /**
     * Draws a marker.
     *
     * @param g         Java AWT graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param size      marker size
     * @param fillColor fill color (can be null -> color is not set, i.e., the current color set in the graphics context is used)
     * @param edgeColor fill color (can be null -> color is not set, i.e., the current color set in the graphics context is used)
     * @param fill      if false, the procedure for filling the marker is entirely skipped
     * @param drawEdge  if false, the procedure for drawing edges is entirely skipped
     */
    void drawMarker(Graphics g, float x, float y, float size, color.Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdge);

    /**
     * Draws a line.
     *
     * @param g  Java AWT rendering context
     * @param x1 first x-coordinate
     * @param y1 first y-coordinate
     * @param x2 second x-coordinate
     * @param y2 second y-coordinate
     */
    void drawLine(Graphics g, float x1, float y1, float x2, float y2);

    /**
     * Draws a horizontal line divided into equal segments that are differently colored.
     * Used when there is a need to draw a gradient line (division = linear interpolation).
     *
     * @param g          Java AWT rendering context
     * @param gradient   gradient used
     * @param lw  lineWidth (used to determine the basic line stroke)
     * @param x1         left coordinate x
     * @param x2         right coordinate x
     * @param y          y coordinate
     */
    void drawGradientLine(Graphics g, Gradient gradient, float lw, float x1, float x2, float y);

    /**
     * Setter for the marker style used when depicting data set.
     *
     * @param ms marker style used when depicting data set
     */
    void setMarkerStyle(MarkerStyle ms);

    /**
     * Setter for the line style used when depicting data set.
     *
     * @param ls line style used when depicting data set
     */
    void setLineStyle(LineStyle ls);
}
