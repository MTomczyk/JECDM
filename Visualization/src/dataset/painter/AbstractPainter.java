package dataset.painter;

import color.Color;
import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import dataset.Data;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import drmanager.DisplayRangesManager;
import listeners.FrameListener;
import plot.AbstractPlot;
import space.Dimension;
import space.Vector;
import statistics.movingaverage.MovingAverageLong;
import thread.swingworker.EventTypes;

import java.awt.event.ComponentEvent;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Abstract implementation of {@link IPainter}
 *
 * @author MTomczyk
 */
public abstract class AbstractPainter implements IPainter
{
    /**
     * Parent data set possessing the painter.
     */
    protected IDataSet _ds;

    /**
     * Painter's name.
     */
    protected String _name;

    /**
     * Reference to the global container.
     */
    protected GlobalContainer _GC;

    /**
     * Reference to the plot container.
     */
    protected PlotContainer _PC;

    /**
     * Current data to be displayed.
     */
    protected volatile Data _data;

    /**
     * Projection data (supports efficient rendering).
     */
    protected volatile IDS _IDS = null;

    /**
     * Marker style used when depicting data points.
     */
    protected MarkerStyle _ms;

    /**
     * Line style used when illustrating lines.
     */
    protected LineStyle _ls;

    /**
     * Determines the minimal segment line used when constructing gradient line (discretization level, the lower the value,
     * the greater the discretization but also computational resources used). The interpretation is implementation-dependent.
     * Default: percent value of an average screen dimension (in pixels).
     */
    protected float _gradientLineMinSegmentLength;

    /**
     * If true, IDS recalculation times are measured and stored.
     * Each array element corresponds to a different reconstruction phase (by default: three phases).
     */
    protected boolean[] _measureRecalculateIDSTimes;

    /**
     * Objects storing IDS recalculation times measured.
     * Each object corresponds to a different reconstruction phase (by default: three phases).
     */
    protected MovingAverageLong[] _IDSRecalculationTimes;


    /**
     * Parameterized constructor.
     *
     * @param ms marker style
     * @param ls line style
     */
    protected AbstractPainter(MarkerStyle ms, LineStyle ls)
    {
        _ms = ms;
        _ls = ls;
        _gradientLineMinSegmentLength = 0.005f;
        instantiateTimeStatistics();
        instantiateProjections();
    }

    /**
     * Setter for the parent data set possessing the painter.
     *
     * @param ds parent data set possessing the painter
     */
    public void setDataSet(IDataSet ds)
    {
        _ds = ds;
    }

    /**
     * Auxiliary method for constructing the projection data.
     */
    protected void instantiateProjections()
    {
        _IDS = new IDS();
    }

    /**
     * Auxiliary method instantiating fields related to measuring recalculation times.
     */
    protected void instantiateTimeStatistics()
    {
        _measureRecalculateIDSTimes = new boolean[]{true, true, true};
        _IDSRecalculationTimes = new MovingAverageLong[3];
        _IDSRecalculationTimes[0] = new MovingAverageLong(10);
        _IDSRecalculationTimes[1] = new MovingAverageLong(10);
        _IDSRecalculationTimes[2] = new MovingAverageLong(10);
    }

    /**
     * Supportive method for setting the painter's name.
     *
     * @param name the painter's name
     */
    @Override
    public void setName(String name)
    {
        _name = name;
    }


    /**
     * Setter for the input raw (unprocessed) data to be displayed.
     *
     * @param data data
     */
    @Override
    public void setData(Data data)
    {
        _data = data;
    }

    /**
     * Setter for the containers.
     *
     * @param GC global container
     * @param PC plot container
     */
    @Override
    public void setContainers(GlobalContainer GC, PlotContainer PC)
    {
        _GC = GC;
        _PC = PC;
    }


    /**
     * Method called to indicate that the data processing began.
     *
     * @param fromFirstLevel if true, the processing is executed from the beginning
     */
    @Override
    public void beginDataProcessing(boolean fromFirstLevel)
    {
        Notification.printNotification(_GC, _PC, _name + ": begin data processing method called");
        if (fromFirstLevel) _IDS.reset();
    }

    /**
     * Method that should be called after processing IDS.
     */
    @Override
    public void finishDataProcessing()
    {

    }

    /**
     * IDS = Internal Data Set Structures = data structures optimized for rendering.
     * Data ({@link Data}) is suitably processed and transformed into projection data {@link IDS} that is
     * easy-to-be-rendered. First level IDS corresponds to normalization of data points as imposed by display ranges.
     * The method should be called on the init phase (when the display ranges are fixed) are called by {@link AbstractPlot}
     * when the display ranges changed after the data update.
     *
     * @param DRM       display range manager maintained by the plot
     * @param eventType event type that triggered the method execution
     */
    @Override
    public void updateFirstLevelIDS(DisplayRangesManager DRM, EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, _name + ": update first level IDS method called (event type = " + eventType.toString() + ")");

        long startTime = 0;
        if (_measureRecalculateIDSTimes[0]) startTime = System.nanoTime();

        // Main conditions for pre-mature termination
        if (_data == null) return;
        if (_data.getData() == null) return;
        if (_data.getData().isEmpty()) return;

        calculateBasicStatistics(DRM);
        initNormalizedDataStructures();
        fillNormalizedData(DRM);
        fillMarkerGradientColors();
        fillLineGradientColors();
        if (_measureRecalculateIDSTimes[0]) _IDSRecalculationTimes[0].addData(System.nanoTime() - startTime);
    }

    /**
     * Called to fill basic entries of {@link IDS}.
     *
     * @param DRM display range manager maintained by the plot
     */
    protected void calculateBasicStatistics(DisplayRangesManager DRM)
    {
        _IDS._noAttributes = DRM.getNoAttributesMappedToDRs();
        _IDS._attIdx_to_drIdx = DRM.get_attIdx_to_drIdx().clone();
        _IDS._drIdx_to_attIdx = DRM.get_drIdx_to_attIdx().clone();
        _IDS._drIdx_to_flatAttIdx = DRM.get_drIdx_to_flatAttIdx().clone();
        _IDS._noMarkerPoints = 0;
        _IDS._noLinePoints = 0;
        _IDS._noLinePointsInContiguousLines = new LinkedList<>();

        boolean drawLines = ((_ls != null) && (_ls.isDrawable()));
        boolean drawMarkers = ((_ms != null) && (_ms.isDrawable()));

        int markerNo = 0;
        int pointNo = 0;

        int linePoints = 0;

        for (double[][] arr : _data.getData())
        {
            if (arr == null)
            {
                if (drawLines) // creates a break point (in line) + store current line
                {
                    if (linePoints > 1) // only valid sizes are passed
                    {
                        _IDS._noLinePointsInContiguousLines.add(linePoints);
                        _IDS._noLinePoints += linePoints;
                    }
                    linePoints = 0;
                }
                continue;
            }

            for (double[] p : arr)
            {
                if (p == null) continue; // no meaning

                // marker processing (accounts for "start painting from" and "paint every", i.e., markers
                // can be displayed with a frequency and phase different from 1, 0).
                if ((drawMarkers) && (pointNo >= _ms._startPaintingFrom))
                {
                    if ((_ms._paintEvery == 1) ||
                            ((_ms._paintEvery > 1) && ((markerNo == 0) || (markerNo == _ms._paintEvery))))
                    {
                        _IDS._noMarkerPoints++;
                        markerNo = 0;
                    }
                    markerNo++;
                }

                // line processing
                if (drawLines)
                    linePoints++;

                pointNo++;
            }
        }


        // another break point for the line (triggered at the end if the last segment is valid).
        if ((drawLines) && (linePoints > 1))
        {
            _IDS._noLinePointsInContiguousLines.add(linePoints);
            _IDS._noLinePoints += linePoints;
        }

        Notification.printNotification(_GC, _PC, _name + ": report on basic statistics");
        Notification.printNotification(_GC, _PC, "     No markers = " + _IDS._noMarkerPoints);
        Notification.printNotification(_GC, _PC, "     No line points (total) = " + _IDS._noLinePoints);
        Notification.printNotification(_GC, _PC, "     No contiguous lines (can be of invalid length) = " + _IDS._noLinePointsInContiguousLines.size());
        for (Integer i : _IDS._noLinePointsInContiguousLines)
            Notification.printNotification(_GC, _PC, "     No line points in a contiguous line = " + i);
    }

    /**
     * Auxiliary method for determining a size of a single projected entry (e.g., a point).
     * By default, the size equals the dimensionality of the drawing area.
     * Nonetheless, the method can be overwritten to suitably customize the processing.
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     * @return size of a projected entry
     */
    protected int getSizeOfAProjectedEntry(Dimension[] dimensions)
    {
        return dimensions.length;
    }


    /**
     * Called to init projection data structures (allocate the memory).
     */
    protected void initNormalizedDataStructures()
    {
        _IDS._normalizedMarkers = new float[_IDS._noMarkerPoints * _IDS._noAttributes];
        _IDS._normalizedContiguousLines = new LinkedList<>();
        if (_IDS._noLinePoints > 1)
            for (Integer i : _IDS._noLinePointsInContiguousLines)
                _IDS._normalizedContiguousLines.add(new float[i * _IDS._noAttributes]);
    }

    /**
     * Called to calculate normalized data.
     *
     * @param DRM display ranges manager
     */
    protected void fillNormalizedData(DisplayRangesManager DRM)
    {
        int markerNo = 0;
        int pointNo = 0;
        int markerIndex = 0;
        int lineIndex = 0;

        boolean drawLines = ((_ls != null) && (_ls.isDrawable()));
        boolean drawMarkers = ((_ms != null) && (_ms.isDrawable()));

        boolean fillFromMarker;

        ListIterator<float[]> lineIterator = _IDS._normalizedContiguousLines.listIterator();
        float[] normalizedLine = null;
        // has to be satisfied and should contain only valid entries (len > 1 and their sum should equal total no. line points).
        if (_IDS._noLinePoints > 1) normalizedLine = lineIterator.next();

        double[] pPoint = null;
        double[] nPoint = null;

        for (double[][] arr : _data.getData())
        {
            if (arr == null) // impose a break
            {
                // Execute list iterator if some lines have been stored
                if (lineIndex > 0)
                {
                    lineIndex = 0;
                    if (lineIterator.hasNext()) normalizedLine = lineIterator.next();
                }
                pPoint = null;
                nPoint = null;
                continue;
            }

            for (double[] p : arr)
            {
                if (p == null) continue; // has no meaning

                // === marker ========================================
                fillFromMarker = false;

                if ((drawMarkers) && (pointNo >= _ms._startPaintingFrom))
                {
                    if ((_ms._paintEvery == 1) ||
                            ((_ms._paintEvery > 1) && ((markerNo == 0) || (markerNo == _ms._paintEvery))))
                    {
                        fillNormalizedPoint(_IDS._normalizedMarkers, markerIndex, p, DRM);
                        fillFromMarker = true;
                        markerIndex += _IDS._noAttributes;
                        markerNo = 0;
                    }
                    markerNo++;
                }

                if (drawLines)
                {
                    if (nPoint == null) nPoint = p; // first assignment (does not guarantee validity)
                    else // data can be filled
                    {
                        if (pPoint == null) // data is filled for the first time (store nPoint)
                        {
                            fillNormalizedPoint(normalizedLine, lineIndex, nPoint, DRM);
                            lineIndex += _IDS._noAttributes;
                        }

                        // swap
                        pPoint = nPoint;
                        nPoint = p;

                        // store nPoint
                        if (fillFromMarker)
                        {
                            //noinspection DataFlowIssue
                            System.arraycopy(_IDS._normalizedMarkers, markerIndex - _IDS._noAttributes, // 2* (two elements back)
                                    normalizedLine, lineIndex, _IDS._noAttributes);
                        }
                        else fillNormalizedPoint(normalizedLine, lineIndex, nPoint, DRM);

                        lineIndex += _IDS._noAttributes;
                    }
                }
                pointNo++;
            }
        }
    }

    /**
     * Calculates the normalized point (in the display space) and accordingly fills the normalized data array.
     *
     * @param array      array to be filled
     * @param offset     offset index for the normalized point.
     * @param inputPoint raw, original input point
     * @param DRM        display ranges manager for handling data on display ranges
     */
    protected void fillNormalizedPoint(float[] array, int offset, double[] inputPoint, DisplayRangesManager DRM)
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
     */
    protected void fillMarkerGradientColors()
    {
        boolean fill = ((_ms != null) && (_ms.isToBeFilled()) && (!_ms._color.isMonoColor()));
        boolean edge = ((_ms != null) && (_ms._edge != null) && (_ms._edge.isDrawable()) && (!_ms._edge._color.isMonoColor()));

        if ((!fill) && (!edge)) return;

        if (fill) _IDS._markerFillGradientColors = new Color[_IDS._noMarkerPoints];
        if (edge) _IDS._markerEdgeGradientColors = new Color[_IDS._noMarkerPoints];

        int offset = 0;
        for (int i = 0; i < _IDS._noMarkerPoints; i++)
        {
            if (fill)
            {
                @SuppressWarnings("DataFlowIssue")
                float val = getGradientNormalizationValue(_IDS._normalizedMarkers, offset, _IDS._drIdx_to_flatAttIdx[_ms._drID]);
                _IDS._markerFillGradientColors[i] = _ms._color.getColor(val);
            }
            if (edge)
            {
                float val = getGradientNormalizationValue(_IDS._normalizedMarkers, offset, _IDS._drIdx_to_flatAttIdx[_ms._edge._drID]);
                _IDS._markerEdgeGradientColors[i] = _ms._edge._color.getColor(val);
            }
            offset += _IDS._noAttributes;
        }
    }

    /**
     * Auxiliary method for pre-determining and storing marker gradient colors.
     */
    protected void fillLineGradientColors()
    {
        boolean fill = ((_ls != null) && (_ls.isDrawable()) && (!_ls._color.isMonoColor()));
        if (!fill) return;

        _IDS._lineGradientColors = new LinkedList<>();

        int noPoints = 0;
        ListIterator<Integer> noPointsIt = _IDS._noLinePointsInContiguousLines.listIterator();

        for (float[] line : _IDS._normalizedContiguousLines)
        {
            if (noPointsIt.hasNext()) noPoints = noPointsIt.next();
            Color[] colors = new Color[noPoints];

            int offset = 0;
            for (int i = 0; i < noPoints; i++)
            {
                float val = getGradientNormalizationValue(line, offset, _IDS._drIdx_to_flatAttIdx[_ls._drID]);
                colors[i] = _ls._color.getColor(val);
                offset += _IDS._noAttributes;
            }
            _IDS._lineGradientColors.add(colors);
        }
    }


    /**
     * IDS = Internal Data Set Structures = data structures optimized for rendering.
     * Data ({@link Data}) is suitably processed and transformed into projection data {@link IDS} that is
     * easy-to-be-rendered. Second level IDS corresponds to normalization of data points as imposed by the drawing area coordinates.
     * The method should be called when the drawing area changes (see the top-level {@link FrameListener#componentResized(ComponentEvent)} ).
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     * @param eventType  event type that triggered the method execution
     */
    @Override
    public void updateSecondLevelIDS(Dimension[] dimensions, EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, _name + ": update second level IDS method called (event type = " + eventType.toString() + ")");

        long startTime = 0;
        if (_measureRecalculateIDSTimes[1]) startTime = System.nanoTime();

        if (areDimensionsNonZero(dimensions))
        {
            _IDS._pSize = getSizeOfAProjectedEntry(dimensions);

            fillProjectedMarkers(dimensions);
            fillProjectedLines(dimensions);
            if ((_ls != null) && (_ls.isDrawable() && (!_ls._color.isMonoColor())))
                fillGradientLinesAuxData(dimensions);
        }

        if (_measureRecalculateIDSTimes[1]) _IDSRecalculationTimes[1].addData(System.nanoTime() - startTime);
    }

    /**
     * Checks if all dimensions of the drawing area are not null and not zero.
     *
     * @param dimensions dimensions of the drawing area
     * @return true if all conditions are passed; false otherwise
     */
    protected boolean areDimensionsNonZero(Dimension[] dimensions)
    {
        if (dimensions == null) return false;

        for (Dimension d : dimensions)
        {
            if (d == null) return false;
            if (Double.compare(d._size, 0.0d) <= 0) return false;
        }
        return true;
    }


    /**
     * Fill data on projected markers.
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     */
    protected void fillProjectedMarkers(Dimension[] dimensions)
    {
        _IDS._projectedMarkers = new float[_IDS._noMarkerPoints * _IDS._pSize];
        if (_IDS._normalizedMarkers == null) return;

        int normalizedOffset = 0;
        for (int i = 0; i < _IDS._projectedMarkers.length; i += _IDS._pSize)
        {
            fillProjectedPoint(_IDS._projectedMarkers, i, _IDS._normalizedMarkers, normalizedOffset, dimensions, _IDS._pSize);
            normalizedOffset += _IDS._noAttributes;
        }
    }

    /**
     * Calculates the normalized point (in the display space) and accordingly fills the normalized data array.
     *
     * @param projectedArray   projection array to be filled
     * @param projectedOffset  offset index in the projection array for the projected point
     * @param normalizedArray  normalized array (data source)
     * @param normalizedOffset offset index in the normalized array for the source point
     * @param dimensions       dimensions of the rendering space
     * @param pSize            size of a projected entry
     */
    protected void fillProjectedPoint(float[] projectedArray, int projectedOffset,
                                      float[] normalizedArray, int normalizedOffset,
                                      Dimension[] dimensions, int pSize)
    {
        for (int i = 0; i < pSize; i++)
        {
            projectedArray[projectedOffset + i] =
                    (float) (dimensions[i]._position + normalizedArray[normalizedOffset +
                            _IDS._drIdx_to_flatAttIdx[i]] * (dimensions[i]._size));
        }
    }


    /**
     * Fills data on projected lines.
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     */
    protected void fillProjectedLines(Dimension[] dimensions)
    {
        _IDS._projectedContiguousLines = new LinkedList<>();
        if (_IDS._noLinePoints < 2) return;
        if (_IDS._normalizedContiguousLines == null) return;
        fillProjectedLines(dimensions, _IDS._normalizedContiguousLines, _IDS._projectedContiguousLines);
    }

    /**
     * Fills data on projected lines.
     *
     * @param dimensions                drawing area dimensions (coordinates + sizes)
     * @param normalizedContiguousLines data on the normalized contiguous lines
     * @param projectedContiguousLines  data on the projected contiguous lines
     */
    protected void fillProjectedLines(Dimension[] dimensions,
                                      LinkedList<float[]> normalizedContiguousLines,
                                      LinkedList<float[]> projectedContiguousLines)
    {
        ListIterator<Integer> noPointsIt = _IDS._noLinePointsInContiguousLines.listIterator();

        for (float[] normalizedArray : normalizedContiguousLines)
        {
            int points = noPointsIt.next();

            float[] projectedArray = new float[points * _IDS._pSize];
            projectedContiguousLines.add(projectedArray);

            int normalizedOffset = 0;
            for (int i = 0; i < projectedArray.length; i += _IDS._pSize)
            {
                fillProjectedPoint(projectedArray, i, normalizedArray, normalizedOffset, dimensions, _IDS._pSize);
                normalizedOffset += _IDS._noAttributes;
            }
        }
    }


    /**
     * Returns the minimal line segment length used when constructing gradient lines.
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     * @return minimal line segment length
     */
    protected float getMinLineSegmentLength(Dimension[] dimensions)
    {
        float sum = 0.0f;
        for (Dimension d : dimensions) sum += (float) d._size;
        sum /= dimensions.length;
        return sum * _gradientLineMinSegmentLength;
    }

    /**
     * Auxiliary method for generating data supporting rendering gradient lines.
     * It consists of additional projected data points and color that are between the original ones (increasing the discretization level).
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     */
    protected void fillGradientLinesAuxData(Dimension[] dimensions)
    {
        boolean fill = ((_ls != null) && (_ls.isDrawable()) && (!_ls._color.isMonoColor()));
        if (!fill) return;

        _IDS._auxProjectedLinesNoPoints = new LinkedList<>();
        if (_IDS._projectedContiguousLines == null) return;
        if (_IDS._normalizedContiguousLines == null) return;

        ListIterator<Integer> noLinePointsIt = _IDS._noLinePointsInContiguousLines.listIterator();
        int noLinePoints = 0;

        float sl = getMinLineSegmentLength(dimensions);

        // first : determine the number of aux line points per line segment
        for (float[] projectedArray : _IDS._projectedContiguousLines)
        {
            if (noLinePointsIt.hasNext()) noLinePoints = noLinePointsIt.next();
            int[] auxPoints = new int[noLinePoints - 1];
            _IDS._auxProjectedLinesNoPoints.add(auxPoints);

            int pOffset = 0;
            int auxIdx = 0;
            for (int offset = _IDS._pSize; offset < projectedArray.length; offset += _IDS._pSize)
            {
                int c = 1;
                if (Double.compare(sl, 0.0d) != 0)
                    c = Vector.getLineSegmentDivisions(projectedArray, pOffset, offset, _IDS._pSize, sl);
                auxPoints[auxIdx++] = c;
                pOffset += _IDS._pSize;
            }
        }

        // second: produce data point and colors
        ListIterator<int[]> noAuxPointsIt = _IDS._auxProjectedLinesNoPoints.listIterator();
        int[] noAuxPoints;

        ListIterator<float[]> normalizedArrayIt = _IDS._normalizedContiguousLines.listIterator();
        float[] normalizedArray;

        _IDS._auxProjectedContiguousLines = new LinkedList<>();
        _IDS._auxLineGradientColors = new LinkedList<>();

        for (float[] projectedArray : _IDS._projectedContiguousLines) // original line points in the projected space
        {
            noAuxPoints = noAuxPointsIt.next();
            normalizedArray = normalizedArrayIt.next();

            int totalAuxPoints = 0;
            for (int n : noAuxPoints) totalAuxPoints += n;

            float[] auxPoints = new float[totalAuxPoints * _IDS._pSize]; // aux points in the projected space
            Color[] auxColors = new Color[totalAuxPoints]; // aux colors

            _IDS._auxProjectedContiguousLines.add(auxPoints);
            _IDS._auxLineGradientColors.add(auxColors);

            // fill data
            int lineSegmentIdx = 0;
            int pOffset = 0;
            int pOffsetNormalized = 0;
            int nOffsetNormalized = _IDS._noAttributes;
            int auxOffset = 0;
            int auxColorIdx = 0;

            for (int nOffset = _IDS._pSize; nOffset < projectedArray.length; nOffset += _IDS._pSize)
            {
                if (noAuxPoints[lineSegmentIdx] != 0)
                {
                    // generate points that are between the original ones only
                    float pV = normalizedArray[pOffsetNormalized + _IDS._drIdx_to_flatAttIdx[_ls._drID]];
                    float nV = normalizedArray[nOffsetNormalized + _IDS._drIdx_to_flatAttIdx[_ls._drID]];

                    for (int pnt = 0; pnt < noAuxPoints[lineSegmentIdx]; pnt++)
                    {
                        float prop = (float) (pnt + 1) / (noAuxPoints[lineSegmentIdx] + 1);
                        fillInterpolatedPoint(auxPoints, projectedArray, auxOffset, pOffset, nOffset, _IDS._pSize, prop);

                        float iV = pV + (nV - pV) * prop;
                        auxColors[auxColorIdx] = _ls._color.getColor(iV);

                        auxColorIdx++;
                        auxOffset += _IDS._pSize;
                    }
                }

                lineSegmentIdx++;
                pOffset += _IDS._pSize;
                pOffsetNormalized += _IDS._noAttributes;
                nOffsetNormalized += _IDS._noAttributes;
            }
        }
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
    protected void fillInterpolatedPoint(float[] output, float[] input, int outputOffset, int pInputOffset, int nInputOffset, int stride, float prop)
    {
        for (int i = 0; i < stride; i++)
            output[outputOffset + i] = input[pInputOffset + i] + prop * (input[nInputOffset + i] - input[pInputOffset + i]);
    }

    /**
     * IDS = Internal Data Set Structures = data structures optimized for rendering.
     * Can be called to construct third-level IDS (e.g., data for VBOs when rendering in 3D)
     *
     * @param eventType event type that triggered the method execution
     */
    @Override
    public void updateThirdLevelIDS(EventTypes eventType)
    {
        long startTime = 0;
        if (_measureRecalculateIDSTimes[2]) startTime = System.nanoTime();
        if (_measureRecalculateIDSTimes[2]) _IDSRecalculationTimes[2].addData(System.nanoTime() - startTime);
    }

    /**
     * Main method for drawing a data set.
     *
     * @param r rendering context
     */
    @Override
    public void draw(Object r)
    {
        if (_ds.isRenderingSkipped()) return;
        setRenderer(r);
        if (_IDS != null)
        {
            drawLines();
            drawMarkers();
        }
        releaseRenderer();
    }

    /**
     * Used for drawing lines.
     */
    protected void drawLines()
    {

    }

    /**
     * Used for drawing lines.
     */
    protected void drawMarkers()
    {

    }

    /**
     * Supportive method for determining normalized gradient value (0->1).
     *
     * @param nArray array containing normalized values (stored as segments for data points)
     * @param offset in-array offset
     * @param attId  attribute ID (in-segment offset)
     * @return normalized gradient value
     */
    protected float getGradientNormalizationValue(float[] nArray, int offset, int attId)
    {
        float val = nArray[offset + attId];
        if (Double.compare(val, 0) < 0) val = 0.0f;
        if (Double.compare(val, 1) > 0) val = 1.0f;
        return val;
    }


    /**
     * Returns marker style.
     *
     * @return marker style
     */
    @Override
    public MarkerStyle getMarkerStyle()
    {
        return _ms;
    }

    /**
     * Returns line style.
     *
     * @return line style
     */
    @Override
    public LineStyle getLineStyle()
    {
        return _ls;
    }


    /**
     * Can be called to clear the data.
     */
    @Override
    public void dispose()
    {
        Notification.printNotification(_GC, _PC, _name + "': dispose method called");
        _data = null;
        _IDS.reset();
        _IDS = null;
    }

    /**
     * Getter for the IDS recalculation times.
     *
     * @param phase phase (implementation-specific, by default 0 = 1st level, 1 = 2nd level, 2 = 3rd level).
     * @return Recalculation time (in milliseconds). If null -> no times were measured.
     */
    @Override
    public Long getRecalculationTime(int phase)
    {
        if (_measureRecalculateIDSTimes == null) return null;
        if (_measureRecalculateIDSTimes.length <= phase) return null;
        if (!_measureRecalculateIDSTimes[phase]) return null;
        if (_IDSRecalculationTimes == null) return null;
        if (_IDSRecalculationTimes.length <= phase) return null;
        return _IDSRecalculationTimes[phase].getLastEntry();
    }

}
