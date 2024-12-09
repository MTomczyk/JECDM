package utils;

import color.Color;
import color.gradient.Gradient;

import java.awt.*;

/**
 * Contains auxiliary methods for drawing.
 *
 * @author MTomczyk
 */


public class DrawUtils
{

    /**
     * Constant value.
     */
    private static final float _r3d2 = (float) (Math.sqrt(3) / 2.0d);

    /**
     * Constant value.
     */
    private static final float _sin30 = (float) (Math.sin(Math.toRadians(30.0f)));

    /**
     * Constant value.
     */
    private static final float _sin36 = (float) (Math.sin(Math.toRadians(36.0f)));

    /**
     * Constant value.
     */
    private static final float _sin60 = (float) (Math.sin(Math.toRadians(60.0f)));

    /**
     * Constant value.
     */
    private static final float _sin90 = (float) (Math.sin(Math.toRadians(90.0f)));

    /**
     * Constant value.
     */
    private static final float _sin72 = (float) (Math.sin(Math.toRadians(72.0f)));

    /**
     * Constant value.
     */
    private static final float _sin108 = (float) (Math.sin(Math.toRadians(108.0f)));

    /**
     * Constant value.
     */
    private static final float _sin120 = (float) (Math.sin(Math.toRadians(120.0f)));

    /**
     * Constant value.
     */
    private static final float _sin144 = (float) (Math.sin(Math.toRadians(144.0f)));

    /**
     * Constant value.
     */
    private static final float _sin150 = (float) (Math.sin(Math.toRadians(150.0f)));

    /**
     * Constant value.
     */
    private static final float _sin210 = (float) (Math.sin(Math.toRadians(210.0f)));

    /**
     * Constant value.
     */
    private static final float _sin240 = (float) (Math.sin(Math.toRadians(240.0f)));

    /**
     * Constant value.
     */
    private static final float _sin252 = (float) (Math.sin(Math.toRadians(252.0f)));

    /**
     * Constant value.
     */
    private static final float _sin270 = (float) (Math.sin(Math.toRadians(270.0f)));

    /**
     * Constant value.
     */
    private static final float _sin300 = (float) (Math.sin(Math.toRadians(300.0f)));

    /**
     * Constant value.
     */
    private static final float _sin324 = (float) (Math.sin(Math.toRadians(324.0f)));

    /**
     * Constant value.
     */
    private static final float _sin330 = (float) (Math.sin(Math.toRadians(330.0f)));

    /**
     * Constant value.
     */
    private static final float _cos30 = (float) (Math.cos(Math.toRadians(30.0f)));

    /**
     * Constant value.
     */
    private static final float _cos60 = (float) (Math.cos(Math.toRadians(60.0f)));

    /**
     * Constant value.
     */
    private static final float _cos36 = (float) (Math.cos(Math.toRadians(36.0f)));

    /**
     * Constant value.
     */
    private static final float _cos72 = (float) (Math.cos(Math.toRadians(72.0f)));

    /**
     * Constant value.
     */
    private static final float _cos90 = (float) (Math.cos(Math.toRadians(90.0f)));

    /**
     * Constant value.
     */
    private static final float _cos108 = (float) (Math.cos(Math.toRadians(108.0f)));

    /**
     * Constant value.
     */
    private static final float _cos120 = (float) (Math.cos(Math.toRadians(120.0f)));

    /**
     * Constant value.
     */
    private static final float _cos144 = (float) (Math.cos(Math.toRadians(144.0f)));

    /**
     * Constant value.
     */
    private static final float _cos150 = (float) (Math.cos(Math.toRadians(150.0f)));

    /**
     * Constant value.
     */
    private static final float _cos210 = (float) (Math.cos(Math.toRadians(210.0f)));

    /**
     * Constant value.
     */
    private static final float _cos240 = (float) (Math.cos(Math.toRadians(240.0f)));

    /**
     * Constant value.
     */
    private static final float _cos252 = (float) (Math.cos(Math.toRadians(252.0f)));

    /**
     * Constant value.
     */
    private static final float _cos270 = (float) (Math.cos(Math.toRadians(270.0f)));

    /**
     * Constant value.
     */
    private static final float _cos300 = (float) (Math.cos(Math.toRadians(300.0f)));

    /**
     * Constant value.
     */
    private static final float _cos324 = (float) (Math.cos(Math.toRadians(324.0f)));

    /**
     * Constant value.
     */
    private static final float _cos330 = (float) (Math.cos(Math.toRadians(330.0f)));

    /**
     * Constant value.
     */
    private static final float _tg1836div1836sin36 = (float) (
            (Math.tan(Math.toRadians(18.0f)) * Math.tan(Math.toRadians(36.0f))) /
            ((Math.tan(Math.toRadians(18.0f)) + Math.tan(Math.toRadians(36.0f))) * Math.sin(Math.toRadians(36.0f))));

    /**
     * Supportive method drawing a border.
     *
     * @param g Java AWT Graphics context
     * @param x left-top (x) coordinate
     * @param y left-top (y) coordinate
     * @param w shape width
     * @param h shape height
     */
    public static void drawBorder(Graphics g, float x, float y, float w, float h)
    {
        int lx = Projection.getP(x);
        int rx = Projection.getP(x + w - 1.0f);
        int ly = Projection.getP(y);
        int ry = Projection.getP(y + h - 1.0f);
        g.drawLine(lx, ly, rx, ly);
        g.drawLine(lx, ry, rx, ry);
        g.drawLine(lx, ly, lx, ry);
        g.drawLine(rx, ly, rx, ry);
    }

    /**
     * Auxiliary method for drawing vertical colorbars.
     *
     * @param g        Java AWT drawing context
     * @param gradient gradient object
     * @param left     left coordinate
     * @param top      top coordinate
     * @param width    width
     * @param height   height
     */
    public static void drawVerticalColorbar(Graphics g, Gradient gradient, float left, float top, float width, float height)
    {
        int iMod = getPixelModifier(left, width);
        float b = top + height - 1.0f;

        for (float yy = b - 1.0f; yy >= top; yy -= 0.1f)
        {
            float p = 1.0f - (yy - top) / (height - 1.0f);
            g.setColor(gradient.getColor(p));
            // fill rect is used to alleviate problems caused by using DPI scaling > 1
            g.fillRect(Projection.getP(left), Projection.getP(yy), Projection.getP(width) + iMod, Projection.getP(2.0f));
        }

        ((Graphics2D) g).setStroke(new BasicStroke(1.0f));
        g.setColor(gradient.getColor(1.0f));
        g.drawLine(Projection.getP(left), Projection.getP(top), Projection.getP(left + width - 1.0f), Projection.getP(top));
    }

    /**
     * Auxiliary method that supports {@link DrawUtils#drawVerticalColorbar(Graphics, Gradient, float, float, float, float)} and {@link DrawUtils#drawHorizontalColorbar(Graphics, Gradient, float, float, float, float)} in proper rendering (size correction).
     *
     * @param pos  colorbar position in selected dimension
     * @param size colorbar size in selected dimension
     * @return size modifier (-1, 0, +1).
     */
    private static int getPixelModifier(float pos, float size)
    {
        float mod = (Projection.getP(pos + size)) - (Projection.getP(pos)) - size;
        int iMod;
        if (Float.compare(mod, 0.0f) >= 0) iMod = Projection.getP(mod); // pixel mod
        else iMod = -Projection.getP(-mod); // pixel mod
        return iMod;
    }


    /**
     * Auxiliary method for drawing horizontal colorbars.
     *
     * @param g        Java AWT drawing context
     * @param gradient gradient object
     * @param left     left coordinate
     * @param top      top coordinate
     * @param width    width
     * @param height   height
     */
    public static void drawHorizontalColorbar(Graphics g, Gradient gradient, float left, float top, float width, float height)
    {
        int iMod = getPixelModifier(top, height);
        float r = left + width - 1.0f;

        for (float xx = r - 1.0f; xx >= left; xx -= 0.1f)
        {
            float p = (xx - left) / (width - 1.0f);
            g.setColor(gradient.getColor(p));
            g.fillRect(Projection.getP(xx), Projection.getP(top), Projection.getP(2.0f), Projection.getP(height) + iMod);
        }

        ((Graphics2D) g).setStroke(new BasicStroke(1.0f));
        g.setColor(gradient.getColor(1.0f));
        g.fillRect(Projection.getP(r), Projection.getP(top), Projection.getP(1), Projection.getP(height - 1.0f));
    }

    /**
     * Draws a circle.
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param d         diameter
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill circle
     * @param drawEdges true = draw edges
     */
    public static void drawCircle(Graphics g, float x, float y, float d, color.Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        int left = Projection.getP(x - d / 2.0f);
        int bottom = Projection.getP(y - d / 2.0f);
        int rad = Projection.getP(d);

        if (fill)
        {
            if (fillColor != null) g.setColor(fillColor);
            g.fillOval(left, bottom, rad, rad);
        }

        if (drawEdges)
        {
            if (edgeColor != null) g.setColor(edgeColor);
            g.drawOval(left, bottom, rad, rad);
        }
    }

    /**
     * Draws a square.
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param w         width
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill square
     * @param drawEdges true = draw edges
     */
    public static void drawSquare(Graphics g, float x, float y, float w, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        int left = Projection.getP(x - w / 2.0f);
        int bottom = Projection.getP(y - w / 2.0f);
        int size = Projection.getP(w);

        if (fill)
        {
            if (fillColor != null) g.setColor(fillColor);
            g.fillRect(left, bottom, size, size);
        }

        if (drawEdges)
        {
            if (edgeColor != null) g.setColor(edgeColor);
            g.drawRect(left, bottom, size - 1, size - 1);
        }
    }

    /**
     * Draws a triangle (oriented up, even-sided).
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param l         side length
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the triangle
     * @param drawEdges true = draw edges
     */
    public static void drawTriangleUp(Graphics g, float x, float y, float l, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float h = _r3d2 * l;
        int[] X = new int[]{Projection.getP(x - 0.5f * h), Projection.getP(x), Projection.getP(x + 0.5f * h)};
        int[] Y = new int[]{Projection.getP(y + 1.0f / 3.0f * h), Projection.getP(y - 2.0f / 3.0f * h), Projection.getP(y + 1.0f / 3.0f * h)};
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }

    /**
     * Draws a triangle (oriented down, even-sided).
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param l         side length
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the triangle
     * @param drawEdges true = draw edges
     */
    public static void drawTriangleDown(Graphics g, float x, float y, float l, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float h = _r3d2 * l;
        int[] X = new int[]{Projection.getP(x - 0.5f * h), Projection.getP(x), Projection.getP(x + 0.5f * h)};
        int[] Y = new int[]{Projection.getP(y - 1.0f / 3.0f * h), Projection.getP(y + 2.0f / 3.0f * h), Projection.getP(y - 1.0f / 3.0f * h)};
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }

    /**
     * Draws a triangle (oriented left, even-sided).
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param l         side length
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the triangle
     * @param drawEdges true = draw edges
     */
    public static void drawTriangleLeft(Graphics g, float x, float y, float l, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float h = _r3d2 * l;
        int[] X = new int[]{Projection.getP(x + 1.0f / 3.0f * h), Projection.getP(x - 2.0f / 3.0f * h), Projection.getP(x + 1.0f / 3.0f * h)};
        int[] Y = new int[]{Projection.getP(y - 0.5f * h), Projection.getP(y), Projection.getP(y + 0.5f * h)};
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }


    /**
     * Draws a triangle (oriented right, even-sided).
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param l         side length
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the triangle
     * @param drawEdges true = draw edges
     */
    public static void drawTriangleRight(Graphics g, float x, float y, float l, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float h = _r3d2 * l;
        int[] X = new int[]{Projection.getP(x - 1.0f / 3.0f * h), Projection.getP(x + 2.0f / 3.0f * h), Projection.getP(x - 1.0f / 3.0f * h)};
        int[] Y = new int[]{Projection.getP(y - 0.5f * h), Projection.getP(y), Projection.getP(y + 0.5f * h)};
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }


    /**
     * Draws a pentagon.
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param d         diameter of a circle spanned over vertices
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the triangle
     * @param drawEdges true = draw edges
     */
    public static void drawPentagon(Graphics g, float x, float y, float d, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float r = d / 2.0f;
        int[] X = new int[]{Projection.getP(x), Projection.getP(x + r * _sin72), Projection.getP(x + r * _sin144),
                Projection.getP(x - r * _sin144), Projection.getP(x - r * _sin72)};
        int[] Y = new int[]{Projection.getP(y - r), Projection.getP(y - r * _cos72), Projection.getP(y - r * _cos144),
                Projection.getP(y - r * _cos144), Projection.getP(y - r * _cos72)};
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }

    /**
     * Draws a star.
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param d         diameter of a circle spanned over the star
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the star
     * @param drawEdges true = draw edges
     */
    public static void drawStar(Graphics g, float x, float y, float d, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float r = d / 2.0f;
        float r2 = r * _tg1836div1836sin36;

        int[] X = new int[]{
                Projection.getP(x),
                Projection.getP(x + r2 * _sin36),
                Projection.getP(x + r * _sin72),
                Projection.getP(x + r2 * _sin108),
                Projection.getP(x + r * _sin144),
                Projection.getP(x),
                Projection.getP(x - r * _sin144),
                Projection.getP(x + r2 * _sin252),
                Projection.getP(x - r * _sin72),
                Projection.getP(x + r2 * _sin324)};
        int[] Y = new int[]{
                Projection.getP(y - r),
                Projection.getP(y - r2 * _cos36),
                Projection.getP(y - r * _cos72),
                Projection.getP(y - r2 * _cos108),
                Projection.getP(y - r * _cos144),
                Projection.getP(y + r2),
                Projection.getP(y - r * _cos144),
                Projection.getP(y - r2 * _cos252),
                Projection.getP(y - r * _cos72),
                Projection.getP(y - r2 * _cos324),
        };
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }

    /**
     * Draws a hexagon (oriented horizontally, even-sided).
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param d         diameter of a circle spanned over the hexagon
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the hexagon
     * @param drawEdges true = draw edges
     */
    public static void drawHexagonHor(Graphics g, float x, float y, float d, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float r = d / 2.0f;
        int[] X = new int[]{Projection.getP(x + r * _sin30), Projection.getP(x + r * _sin90), Projection.getP(x + r * _sin150),
                Projection.getP(x + r * _sin210), Projection.getP(x + r * _sin270), Projection.getP(x + r * _sin330)};
        int[] Y = new int[]{Projection.getP(y + r * _cos30), Projection.getP(y + r * _cos90), Projection.getP(y + r * _cos150),
                Projection.getP(y + r * _cos210), Projection.getP(y + r * _cos270), Projection.getP(y + r * _cos330)};
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }

    /**
     * Draws a hexagon (oriented vertically, even-sided).
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param d         diameter of a circle spanned over the hexagon
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the hexagon
     * @param drawEdges true = draw edges
     */
    public static void drawHexagonVert(Graphics g, float x, float y, float d, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float r = d / 2.0f;
        int[] X = new int[]{
                Projection.getP(x), Projection.getP(x + r * _sin60), Projection.getP(x + r * _sin120),
                Projection.getP(x), Projection.getP(x + r * _sin240), Projection.getP(x + r * _sin300)
        };
        int[] Y = new int[]{Projection.getP(y + r), Projection.getP(y + r * _cos60), Projection.getP(y + r * _cos120),
                Projection.getP(y - r), Projection.getP(y + r * _cos240), Projection.getP(y + r * _cos300)};
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }

    /**
     * Draws a diamond (oriented horizontally, even-sided).
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param d         diameter of a circle spanned over the bigger dimension of the diamond
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the diamond
     * @param drawEdges true = draw edges
     */
    @SuppressWarnings("DuplicatedCode")
    public static void drawDiamondHor(Graphics g, float x, float y, float d, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float r = d / 2.0f;
        float r2 = d / 4.0f;
        int[] X = new int[]{Projection.getP(x), Projection.getP(x + r), Projection.getP(x), Projection.getP(x - r)};
        int[] Y = new int[]{Projection.getP(y + r2), Projection.getP(y), Projection.getP(y - r2), Projection.getP(y)};
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }

    /**
     * Draws a diamond (oriented vertically, even-sided).
     *
     * @param g         Java AWT Graphics context
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param d         diameter of a circle spanned over the bigger dimension of the diamond
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill the diamond
     * @param drawEdges true = draw edges
     */
    @SuppressWarnings("DuplicatedCode")
    public static void drawDiamondVert(Graphics g, float x, float y, float d, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges)
    {
        float r = d / 2.0f;
        float r2 = d / 4.0f;
        int[] X = new int[]{Projection.getP(x), Projection.getP(x + r2), Projection.getP(x), Projection.getP(x - r2)};
        int[] Y = new int[]{Projection.getP(y + r), Projection.getP(y), Projection.getP(y - r), Projection.getP(y)};
        drawPolygon(g, fillColor, edgeColor, fill, drawEdges, X, Y);
    }

    /**
     * Auxiliary method (private) for drawing general polygons)
     *
     * @param g         Java AWT Graphics context
     * @param fillColor fill color (can be null -> fill color is not set)
     * @param edgeColor edge color (can be null -> edge color is not set)
     * @param fill      true = fill triangle
     * @param drawEdges true = draw edges
     * @param x         polygon x-coordinates
     * @param y         polygon y-coordinates
     */
    private static void drawPolygon(Graphics g, Color fillColor, color.Color edgeColor, boolean fill, boolean drawEdges, int[] x, int[] y)
    {
        if (fill)
        {
            if (fillColor != null) g.setColor(fillColor);
            g.fillPolygon(x, y, x.length);
        }

        if (drawEdges)
        {
            if (edgeColor != null) g.setColor(edgeColor);
            g.drawPolygon(x, y, x.length);
        }
    }


    /**
     * Constructs a rescaled version of the given stroke.
     *
     * @param scale           rescaling factor
     * @param referenceStroke reference stroke to be rescaled
     * @return constructed stroke
     */
    public static BasicStroke constructRescaledStroke(float scale, BasicStroke referenceStroke)
    {
        float[] arr = null;
        if (referenceStroke.getDashArray() != null) arr = referenceStroke.getDashArray().clone();
        if (arr != null) for (int i = 0; i < arr.length; i++) arr[i] *= scale;
        float nM = referenceStroke.getMiterLimit() * scale;
        if (Float.compare(nM, 1.0f) < 0) nM = 1.0f;
        return new BasicStroke(referenceStroke.getLineWidth() * scale,
                referenceStroke.getEndCap(),
                referenceStroke.getLineJoin(),
                nM, arr, referenceStroke.getDashPhase() * scale);
    }
}
