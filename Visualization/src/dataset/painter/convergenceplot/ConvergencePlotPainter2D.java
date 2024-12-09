package dataset.painter.convergenceplot;

import color.Color;
import dataset.Data;
import dataset.painter.IDS;
import dataset.painter.IPainter;
import dataset.painter.Painter2D;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import drmanager.DisplayRangesManager;
import space.Dimension;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Implementation of {@link IPainter} for rendering convergence plots. It supports rendering additional
 * transparent envelopes around the convergence lines to illustrate, e.g., error level.
 * It is assumed that the input data points in {@link Data} consist of at
 * - two attributes (typically: X and Y coordinates); or
 * - four attributes: the last two ones one is considered to be an optional deviation-like statistic, e.g.,
 * Y + and - standard deviation around the input data point.
 * Note: X attribute value should increase/decrease monotonically.
 * <p>
 * The envelope color is controlled by
 * the additional class field "envelope color" that can be tuned. If not provided,
 * Line color with additional an 50% transparency is used. Note that gradients are not allowed
 * for the envelope (if the style is derived from the line style that uses gradient, a default color
 * for 0.0f value is used).
 *
 * @author MTomczyk
 */
public class ConvergencePlotPainter2D extends Painter2D implements IPainter
{
    /**
     * Envelope color.
     */
    protected Color _envelopeColor;

    /**
     * Reference to the projection data (additional reference is kept to avoid casting).
     */
    private ConvergencePlotIDS _cpProjection;

    /**
     * Parameterized constructor.
     *
     * @param ms marker style
     * @param ls line style
     */
    public ConvergencePlotPainter2D(MarkerStyle ms, LineStyle ls)
    {
        this(ms, ls, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param ms            marker style
     * @param ls            line style
     * @param envelopeColor envelopeColor (can be null -> line color is used)
     */
    public ConvergencePlotPainter2D(MarkerStyle ms, LineStyle ls, Color envelopeColor)
    {
        super(ms, ls);
        _envelopeColor = envelopeColor;
    }

    /**
     * The method clones the painter, but ignores the rendering data.
     * The method supports data changing.
     *
     * @return cloned painter (except for rendering data).
     */
    @Override
    public IPainter getEmptyClone()
    {
        MarkerStyle ms = null;
        if (_ms != null) ms = _ms.getClone();
        LineStyle ls = null;
        if (_ls != null) ls = _ls.getClone();
        return new ConvergencePlotPainter2D(ms, ls, _envelopeColor);
    }

    /**
     * Auxiliary method for constructing the projection data.
     */
    @Override
    protected void instantiateProjections()
    {
        _cpProjection = new ConvergencePlotIDS(); // shared reference
        _IDS = _cpProjection;
    }


    /**
     * Called to fill basic entries of {@link IDS}.
     *
     * @param DRM display range manager maintained by the plot
     */
    protected void calculateBasicStatistics(DisplayRangesManager DRM)
    {
        super.calculateBasicStatistics(DRM);

        boolean drawEnvelope = ((_envelopeColor != null) || ((_ls != null) && (_ls.isDrawable())));

        _cpProjection._noEnvelopePoints = 0;
        if (!drawEnvelope)
        {
            _cpProjection._noEnvelopePointsInContiguousLines = null;
            return;
        }

        _cpProjection._noEnvelopePointsInContiguousLines = new LinkedList<>();
        int envelopePoints = 0;

        for (double[][] arr : _data.getData())
        {
            if (arr == null)
            {
                if (envelopePoints > 1) // only valid sizes are passed
                {
                    _cpProjection._noEnvelopePointsInContiguousLines.add(envelopePoints);
                    _cpProjection._noEnvelopePoints += envelopePoints;
                }
                envelopePoints = 0;
            }
            else
            {
                for (double[] p : arr)
                {
                    if (p == null) continue; // no meaning
                    if (p.length != 4) continue; // turn off invalid entries
                    envelopePoints++;
                }
            }

        }

        if (envelopePoints > 1)
        {
            _cpProjection._noEnvelopePointsInContiguousLines.add(envelopePoints);
            _cpProjection._noEnvelopePoints += envelopePoints;
        }
    }

    /**
     * Called to init projection data structures (allocate the memory).
     * The extended method creates normalized data related to the envelope bounds.
     */
    @Override
    protected void initNormalizedDataStructures()
    {
        super.initNormalizedDataStructures();

        _cpProjection._normalizedUpperLine = new LinkedList<>();
        _cpProjection._normalizedLowerLine = new LinkedList<>();
        if (_cpProjection._noEnvelopePoints > 1)
        {
            for (Integer i : _cpProjection._noEnvelopePointsInContiguousLines)
            {
                _cpProjection._normalizedUpperLine.add(new float[i * 2]);
                _cpProjection._normalizedLowerLine.add(new float[i * 2]);
            }
        }
    }

    /**
     * Called to calculate normalized data.
     * The extended method fills normalized data related to the envelope bounds.
     */
    @Override
    protected void fillNormalizedData(DisplayRangesManager DRM)
    {
        super.fillNormalizedData(DRM);

        if (_cpProjection._noEnvelopePoints <= 1) return;

        ListIterator<float[]> upperLineIterator = _cpProjection._normalizedUpperLine.listIterator();
        ListIterator<float[]> lowerLineIterator = _cpProjection._normalizedLowerLine.listIterator();

        float[] normalizedUpperLine = upperLineIterator.next();
        float[] normalizedLowerLine = lowerLineIterator.next();

        double[] pPoint = null;
        double[] nPoint = null;

        int lineIndex = 0;

        for (double[][] arr : _data.getData())
        {
            if (arr == null) // impose a break
            {
                // Execute list iterator if some lines have been stored
                if (lineIndex > 0)
                {
                    lineIndex = 0;
                    if (upperLineIterator.hasNext()) normalizedUpperLine = upperLineIterator.next();
                    if (lowerLineIterator.hasNext()) normalizedLowerLine = lowerLineIterator.next();
                }
                pPoint = null;
                nPoint = null;
                continue;
            }

            for (double[] p : arr)
            {
                if (p == null) continue; // has no meaning

                if (nPoint == null) nPoint = p; // first assignment (does not guarantee validity)
                else // data can be filled
                {
                    if (pPoint == null) // data is filled for the first time (store nPoint)
                    {
                        fillNormalizedEnvelopePoints(normalizedUpperLine, normalizedLowerLine, lineIndex, nPoint, DRM);
                        lineIndex += 2;
                    }

                    pPoint = nPoint;
                    nPoint = p;

                    fillNormalizedEnvelopePoints(normalizedUpperLine, normalizedLowerLine, lineIndex, nPoint, DRM);
                    lineIndex += 2;
                }
            }
        }
    }


    /**
     * Calculates the normalized upper/lower envelope points (in the display space)
     * and accordingly fills the normalized data array. It assumes that
     * the input point is a four-element tuple consisting of three values:
     * X, Y, Y + deviation, Y - deviation. The line points are produced as:
     * - upper: [X, Y + deviation]
     * - lower: [X, Y - deviation]
     *
     * @param upperLine  array to be filled (upper line)
     * @param lowerLine  array to be filled (lower line)
     * @param offset     offset index for the normalized point.
     * @param inputPoint raw, original input point
     * @param DRM        display ranges manager for handling data on display ranges
     */
    protected void fillNormalizedEnvelopePoints(float[] upperLine,
                                                float[] lowerLine,
                                                int offset,
                                                double[] inputPoint,
                                                DisplayRangesManager DRM)
    {
        DisplayRangesManager.DisplayRange DR = DRM.getDisplayRangeForAttribute(_IDS._drIdx_to_flatAttIdx[0]);

        // x - coordinate
        double v = inputPoint[0];
        float n = 0.0f;
        if (Double.compare(DR.getR().getInterval(), 0) != 0) n = (float) DR.getNormalizer().getNormalized(v);
        upperLine[offset] = n;
        lowerLine[offset] = n;

        // y + dev
        DR = DRM.getDisplayRangeForAttribute(2);
        v = inputPoint[2];
        n = 0.0f;
        if (Double.compare(DR.getR().getInterval(), 0) != 0) n = (float) DR.getNormalizer().getNormalized(v);
        upperLine[offset + 1] = n;

        // y - dev
        DR = DRM.getDisplayRangeForAttribute(3);
        v = inputPoint[3];
        n = 0.0f;
        if (Double.compare(DR.getR().getInterval(), 0) != 0) n = (float) DR.getNormalizer().getNormalized(v);
        lowerLine[offset + 1] = n;
    }


    /**
     * Fills data on projected lines.
     * Projected envelope is calculated here.
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     */
    @Override
    protected void fillProjectedLines(Dimension[] dimensions)
    {
        super.fillProjectedLines(dimensions);

        _cpProjection._projectedXCoords = new LinkedList<>();
        _cpProjection._projectedYCoords = new LinkedList<>();
        if (_cpProjection._noEnvelopePoints < 2) return;

        ListIterator<float[]> upperLinesIterator = _cpProjection._normalizedUpperLine.listIterator();
        ListIterator<float[]> lowerLinesIterator = _cpProjection._normalizedLowerLine.listIterator();
        float[] upperLines;
        float[] lowerLines;

        while ((upperLinesIterator.hasNext()) && (lowerLinesIterator.hasNext()))
        {
            upperLines = upperLinesIterator.next();
            lowerLines = lowerLinesIterator.next();

            int[] x = new int[upperLines.length];
            int[] y = new int[upperLines.length];

            int idx = 0;
            for (int offset = 0; offset < upperLines.length; offset += 2)
            {
                x[idx] = utils.Projection.getP((float) (dimensions[0]._position + upperLines[offset] * (dimensions[0]._size)));
                y[idx++] = utils.Projection.getP((float) (dimensions[1]._position + (1.0f - upperLines[offset + 1]) * (dimensions[1]._size)));
            }

            for (int offset = lowerLines.length - 2; offset >= 0; offset -= 2)
            {
                x[idx] = utils.Projection.getP((float) (dimensions[0]._position + lowerLines[offset] * (dimensions[0]._size)));
                y[idx++] = utils.Projection.getP((float) (dimensions[1]._position + (1.0f - lowerLines[offset + 1]) * (dimensions[1]._size)));
            }

            _cpProjection._projectedXCoords.add(x);
            _cpProjection._projectedYCoords.add(y);
        }
    }

    /**
     * Main method for drawing a data set.
     * The envelope is drawn before lines and markers.
     *
     * @param r rendering context
     */
    @Override
    public void draw(Object r)
    {
        setRenderer(r);
        drawEnvelope();
        drawLines();
        drawMarkers();
        releaseRenderer();
    }

    /**
     * Used for drawing the envelope.
     */
    protected void drawEnvelope()
    {
        if (_cpProjection._noEnvelopePoints < 2) return;
        if (_cpProjection._projectedXCoords == null) return;

        if (_envelopeColor != null) _G.setColor(_envelopeColor);
        else _G.setColor(_ls._color.getColor(0.0f));

        int[] xCoords;
        int[] yCoords;

        ListIterator<int[]> xIterator = _cpProjection._projectedXCoords.listIterator();
        ListIterator<int[]> yIterator = _cpProjection._projectedYCoords.listIterator();

        while ((xIterator.hasNext()) && (yIterator.hasNext()))
        {
            xCoords = xIterator.next();
            yCoords = yIterator.next();
            _G.fillPolygon(xCoords, yCoords, xCoords.length);
        }
    }

}
