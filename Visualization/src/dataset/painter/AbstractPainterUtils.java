package dataset.painter;

import color.Color;
import container.GlobalContainer;
import container.PlotContainer;
import dataset.painter.arrowsutils.IArrowProjectionDataConstructor;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import drmanager.DisplayRangesManager;
import space.Dimension;
import space.Vector;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Provides various utility methods for {@link AbstractPainter}.
 *
 * @author MTomczyk
 */
class AbstractPainterUtils
{
    /**
     * Calculates the normalized point (in the display space) and accordingly fills the normalized data array.
     *
     * @param array      array to be filled
     * @param offset     offset index for the normalized point.
     * @param inputPoint raw, original input point
     * @param DRM        display ranges manager for handling data on display ranges
     */
    protected static void fillNormalizedPoint(float[] array, int offset, double[] inputPoint, DisplayRangesManager DRM)
    {
        int idx = 0;
        for (int i = 0; i < inputPoint.length; i++)
        {
            DisplayRangesManager.DisplayRange DR = DRM.getDisplayRangeForAttribute(i);
            if (DR == null) continue;
            if (DR.getR() == null) array[offset + idx] = (float) inputPoint[i];
            else if (Double.compare(DR.getR().getInterval(), 0) == 0) array[offset + idx] = (float) inputPoint[i];
            else array[offset + idx] = (float) DR.getNormalizer().getNormalized(inputPoint[i]);
            idx++;
        }
    }

    /**
     * Auxiliary method for pre-determining and storing marker gradient colors.
     *
     * @param ids internal data structures
     * @param ms  marker style
     */
    protected static void fillMarkerGradientColors(IDS ids, MarkerStyle ms)
    {
        boolean fill = ((ms != null) && (ms.isToBeFilled()) && (!ms._color.isMonoColor()));
        boolean edge = ((ms != null) && (ms._edge != null) && (ms._edge.isDrawable()) && (!ms._edge._color.isMonoColor()));

        if ((!fill) && (!edge)) return;

        if (fill) ids._markerFillGradientColors = new Color[ids._noMarkerPoints];
        if (edge) ids._markerEdgeGradientColors = new Color[ids._noMarkerPoints];

        int offset = 0;
        for (int i = 0; i < ids._noMarkerPoints; i++)
        {
            if (fill)
            {
                float val = getGradientNormalizationValue(ids._normalizedMarkers, offset, ids._drIdx_to_flatAttIdx[ms._drID]);
                ids._markerFillGradientColors[i] = ms._color.getColor(val);
            }
            if (edge)
            {
                float val = getGradientNormalizationValue(ids._normalizedMarkers, offset, ids._drIdx_to_flatAttIdx[ms._edge._drID]);
                ids._markerEdgeGradientColors[i] = ms._edge._color.getColor(val);
            }
            offset += ids._noAttributes;
        }
    }

    /**
     * Supportive method for determining normalized gradient value (0->1).
     *
     * @param nArray array containing normalized values (stored as segments for data points)
     * @param offset in-array offset
     * @param attId  attribute ID (in-segment offset)
     * @return normalized gradient value
     */
    protected static float getGradientNormalizationValue(float[] nArray, int offset, int attId)
    {
        float val = nArray[offset + attId];
        if (Double.compare(val, 0) < 0) val = 0.0f;
        if (Double.compare(val, 1) > 0) val = 1.0f;
        return val;
    }

    /**
     * Returns the minimal line segment length used when constructing gradient lines.
     *
     * @param gradientLineMinSegmentLength determines the minimal segment line used when constructing gradient line
     *                                     (discretization level, the lower the value, the greater the discretization
     *                                     but also computational resources used); the interpretation is
     *                                     implementation-dependent; default: percent value of an average screen
     *                                     dimension (in pixels).
     * @param dimensions                   drawing area dimensions (coordinates + sizes)
     * @return minimal line segment length
     */
    protected static float getMinLineSegmentLength(float gradientLineMinSegmentLength, Dimension[] dimensions)
    {
        float sum = 0.0f;
        for (Dimension d : dimensions) sum += (float) d._size;
        sum /= dimensions.length;
        return sum * gradientLineMinSegmentLength;
    }

    /**
     * Auxiliary method for generating interpolated points between two input ones (convex combination). The method works on existing arrays.
     *
     * @param output       array to be filled with the interpolated point
     * @param input        input data containing two points
     * @param outputOffset output point offset in the output array
     * @param pInputOffset first input point offset in the input array
     * @param nInputOffset second input point offset in the input array
     * @param stride       data stride
     * @param prop         weight from 0 (first input point) to 1 (second input point)
     */
    protected static void fillInterpolatedPoint(float[] output, float[] input, int outputOffset, int pInputOffset, int nInputOffset, int stride, float prop)
    {
        for (int i = 0; i < stride; i++)
            output[outputOffset + i] = input[pInputOffset + i] + prop * (input[nInputOffset + i] - input[pInputOffset + i]);
    }


    /**
     * Auxiliary method for pre-determining and storing marker gradient colors.
     *
     * @param ids internal data structures
     * @param ls  line style
     */
    protected static void fillLineGradientColors(IDS ids, LineStyle ls)
    {
        boolean fill = ((ls != null) && (ls.isDrawable()) && (!ls._color.isMonoColor()));
        if (!fill) return;

        ids._lineGradientColors = new LinkedList<>();

        int noPoints = 0;
        ListIterator<Integer> noPointsIt = ids._noLinePointsInContiguousLines.listIterator();

        for (float[] line : ids._normalizedContiguousLines)
        {
            if (noPointsIt.hasNext()) noPoints = noPointsIt.next();
            Color[] colors = new Color[noPoints];
            int offset = 0;
            for (int i = 0; i < noPoints; i++)
            {
                float val = AbstractPainterUtils.getGradientNormalizationValue(line, offset, ids._drIdx_to_flatAttIdx[ls._drID]);
                colors[i] = ls._color.getColor(val);
                offset += ids._noAttributes;
            }
            ids._lineGradientColors.add(colors);
        }
    }


    /**
     * Supportive method that fills arrow gradient colors data.
     *
     * @param ids                       internal data structures
     * @param bAPDC     beginning arrow data projection constructor
     * @param eAPDC     ending arrow data projection constructor
     * @param as                        arrows styles
     * @param treadContiguousLinesAsNot flag indicating which line interpretation is used
     */
    protected static void fillArrowGradientColors(IDS ids,
                                                  IArrowProjectionDataConstructor bAPDC,
                                                  IArrowProjectionDataConstructor eAPDC,
                                                  ArrowStyles as,
                                                  boolean treadContiguousLinesAsNot)
    {
        if (ids._noLinesWithArrows == 0) return; // not used
        if (as == null) return; // should not be null at this point, but checked for safety

        boolean bd = bAPDC != null && !as._bas._color.isMonoColor();
        boolean ed = eAPDC != null && !as._eas._color.isMonoColor();

        if (bd) ids._baIDS._arrowFillColors = new Color[ids._noLinesWithArrows];
        if (ed) ids._eaIDS._arrowFillColors = new Color[ids._noLinesWithArrows];

        ListIterator<Integer> noPointsIt = ids._noLinePointsInContiguousLines.listIterator();
        int noPoints = 0;
        int idx = 0;
        int offset = 0;
        for (float[] line : ids._normalizedContiguousLines)
        {
            if (noPointsIt.hasNext()) noPoints = noPointsIt.next();
            if (treadContiguousLinesAsNot)    // repeat for all points (every two)
            {
                for (int i = 0; i < noPoints; i += 2)
                {
                    if (bd) fillArrowGradientFill(ids, line, offset, as._bas, ids._baIDS._arrowFillColors, idx);
                    if (ed) fillArrowGradientFill(ids, line, offset + ids._noAttributes,
                            as._eas, ids._eaIDS._arrowFillColors, idx);
                    offset += ids._noAttributes * 2;
                    idx++;
                }
            }
            else // do just for the beginning and ending
            {
                if (bd) fillArrowGradientFill(ids, line, 0, as._bas, ids._baIDS._arrowFillColors, idx);
                if (ed) fillArrowGradientFill(ids, line, line.length - ids._noAttributes, as._eas,
                        ids._eaIDS._arrowFillColors, idx);
                idx++;
            }
        }
    }

    /**
     * Auxiliary method for filling the color array (arrows gradient-based fill).
     *
     * @param ids        internal data structures
     * @param line       line coordinates
     * @param offset     offset in line
     * @param as         arrow style
     * @param colorArray color array being filled
     * @param idx        index in color array
     */
    private static void fillArrowGradientFill(IDS ids, float[] line, int offset, ArrowStyle as, Color[] colorArray, int idx)
    {
        float val = AbstractPainterUtils.getGradientNormalizationValue(line, offset, ids._drIdx_to_flatAttIdx[as._drID]);
        colorArray[idx] = as._color.getColor(val);
    }


    /**
     * Fills basic arrow projection data
     *
     * @param idsArrows         IDS for arrows
     * @param APDC              arrow projection data constructor
     * @param noLinesWithArrows no. lines with arrows
     */
    protected static void fillBasicArrowProjectionData(IDSArrows idsArrows, IArrowProjectionDataConstructor APDC, int noLinesWithArrows)
    {
        idsArrows._arrowProjectedData = APDC.getArrowProjectedDataArray(noLinesWithArrows);
        idsArrows._stride = APDC.getArrowProjectedDataSizeArrowStride();
        idsArrows._verts = APDC.getNoVertices();
    }

    /**
     * Auxiliary method that adjust the projected line data so that the lines begin/end in the middle of arrows.
     * Also, it creates the arrow(s) projection data.
     *
     * @param ids      internal data structures
     * @param as       arrow styles
     * @param GC       global container
     * @param PC       plot container
     * @param lines    contiguous line
     * @param bOffset  offset pointing to the beginning of a single line segment
     * @param eOffset  offset pointing to the ending of a single line segment
     * @param bAPDC     beginning arrow data projection constructor
     * @param eAPDC     ending arrow data projection constructor
     * @param bpOffset beginning arrow projection data offset
     * @param epOffset ending arrow projection data offset
     */
    protected static void modifyArrowLine(IDS ids,
                                          ArrowStyles as,
                                          GlobalContainer GC,
                                          PlotContainer PC,
                                          float[] lines,
                                          int bOffset,
                                          int eOffset,
                                          IArrowProjectionDataConstructor bAPDC,
                                          IArrowProjectionDataConstructor eAPDC,
                                          int bpOffset,
                                          int epOffset)
    {
        float[] dv = new float[ids._pSize];
        for (int i = 0; i < ids._pSize; i++) dv[i] = lines[eOffset + i] - lines[bOffset + i];
        Vector.normalize(dv);

        if (bAPDC != null)
        {
            float length = bAPDC.getModifiedLength(as._bas, GC, PC);
            float width = bAPDC.getModifiedWidth(as._bas, GC, PC);
            for (int i = 0; i < ids._pSize; i++) lines[bOffset + i] += (dv[i] * length / 2.0f);
            bAPDC.fillArrowProjectedDataArray(ids._baIDS._arrowProjectedData, bpOffset, lines, bOffset, dv,
                    length, width, as._bas._arrow);
        }
        if (eAPDC != null)
        {
            float length = eAPDC.getModifiedLength(as._eas, GC, PC);
            float width = eAPDC.getModifiedWidth(as._eas, GC, PC);
            for (int i = 0; i < ids._pSize; i++) lines[eOffset + i] -= (dv[i] * length / 2.0f);
            eAPDC.fillArrowProjectedDataArray(ids._eaIDS._arrowProjectedData, epOffset, lines, eOffset, dv,
                    length, width, as._eas._arrow);
        }
    }


    /**
     * Checks if all dimensions of the drawing area are not null and not zero.
     *
     * @param dimensions dimensions of the drawing area
     * @return true if all conditions are passed; false otherwise
     */
    protected static boolean areDimensionsNonZero(Dimension[] dimensions)
    {
        if (dimensions == null) return false;

        for (Dimension d : dimensions)
        {
            if (d == null) return false;
            if (Double.compare(d._size, 0.0d) <= 0) return false;
        }
        return true;
    }
}
