package component.legend;

import color.gradient.Gradient;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import utils.DrawUtils;
import utils.Projection;

import java.awt.*;

/**
 * Abstract implementation of {@link IEntryPainter}.
 *
 * @author MTomczyk
 */
public class AbstractEntryPainter implements IEntryPainter
{
    /**
     * Marker style.
     */
    private MarkerStyle _ms;

    /**
     * Line style.
     */
    private LineStyle _ls;

    /**
     * Auxiliary constant basic stroke.
     */
    private final BasicStroke _bs = new BasicStroke(1.0f);

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
    @Override
    public void drawMarker(Graphics g, float x, float y, float size, color.Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdge)
    {
        switch (_ms._style)
        {
            case SQUARE, CUBE_3D -> DrawUtils.drawSquare(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case CIRCLE, POINT_3D, SPHERE_LOW_POLY_3D, SPHERE_MEDIUM_POLY_3D, SPHERE_HIGH_POLY_3D ->
                    DrawUtils.drawCircle(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case TRIANGLE_LEFT, TETRAHEDRON_LEFT_3D ->
                    DrawUtils.drawTriangleLeft(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case TRIANGLE_RIGHT, TETRAHEDRON_RIGHT_3D ->
                    DrawUtils.drawTriangleRight(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case TRIANGLE_DOWN, TETRAHEDRON_DOWN_3D, TETRAHEDRON_BACK_3D ->
                    DrawUtils.drawTriangleDown(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case TRIANGLE_UP, TETRAHEDRON_UP_3D, TETRAHEDRON_FRONT_3D ->
                    DrawUtils.drawTriangleUp(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case PENTAGON -> DrawUtils.drawPentagon(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case STAR -> DrawUtils.drawStar(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case HEXAGON_HOR -> DrawUtils.drawHexagonHor(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case HEXAGON_VERT -> DrawUtils.drawHexagonVert(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case DIAMOND_HOR -> DrawUtils.drawDiamondHor(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
            case DIAMOND_VERT -> DrawUtils.drawDiamondVert(g, x, y, size, fillColor, edgeColor, fill, drawEdge);
        }
    }

    /**
     * Draws a line.
     *
     * @param g  Java AWT rendering context
     * @param x1 first x-coordinate
     * @param y1 first y-coordinate
     * @param x2 second x-coordinate
     * @param y2 second y-coordinate
     */
    @Override
    public void drawLine(Graphics g, float x1, float y1, float x2, float y2)
    {
        g.drawLine(Projection.getP(x1), Projection.getP(y1), Projection.getP(x2), Projection.getP(y2));
    }


    /**
     * Draws a horizontal line divided into equal segments that are differently colored.
     * Used when there is a need to draw a gradient line (division = linear interpolation).
     *
     * @param g        Java AWT rendering context
     * @param gradient gradient used
     * @param lw       lineWidth (used to determine the basic line stroke)
     * @param x1       left coordinate x
     * @param x2       right coordinate x
     * @param y        y coordinate
     */
    @Override
    public void drawGradientLine(Graphics g, Gradient gradient, float lw, float x1, float x2, float y)
    {
        ((Graphics2D) g).setStroke(_bs);
        DrawUtils.drawHorizontalColorbar(g, gradient, x1, y - lw / 2.0f, x2 - x1, lw);
    }

    /**
     * Setter for the marker style used when depicting data set.
     *
     * @param ms marker style used when depicting data set
     */
    @Override
    public void setMarkerStyle(MarkerStyle ms)
    {
        _ms = ms;
    }

    /**
     * Setter for the line style used when depicting data set.
     *
     * @param ls line style used when depicting data set
     */
    @Override
    public void setLineStyle(LineStyle ls)
    {
        _ls = ls;
    }

}
