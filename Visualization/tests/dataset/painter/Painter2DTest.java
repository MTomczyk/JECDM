package dataset.painter;

import color.gradient.Color;
import container.GlobalContainer;
import dataset.Data;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import org.junit.jupiter.api.Test;
import print.PrintUtils;
import space.Dimension;
import space.Range;
import thread.swingworker.EventTypes;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Some tests for {@link Painter2D} class.
 *
 * @author MTomczyk
 */
class Painter2DTest
{
    /**
     * Test 1
     */
    @Test
    void Test1()
    {
        DisplayRangesManager DRM =
                new DisplayRangesManager(DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        MarkerStyle ms = new MarkerStyle(2.0f, Color.BLACK, Marker.SQUARE);
        ms._startPaintingFrom = 1;
        ms._paintEvery = 2;
        LineStyle ls = new LineStyle(1.0f, Color.RED);

        LinkedList<double[][]> d = new LinkedList<>();
        d.add(new double[][]{{-2.0f, -2.0f}, {-1.9f, -1.9f}});
        d.add(null);
        d.add(new double[][]{{-1.5f, -1.5f}});
        d.add(new double[][]{{0.0f, 0.0f}, {0.5f, 0.5f}, {1.0f, 1.0f}});
        d.add(null);
        d.add(null);
        d.add(new double[][]{{2.0f, 2.0f}});

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(2, painter._IDS._noAttributes);
        assertEquals(3, painter._IDS._noMarkerPoints);
        assertEquals(6, painter._IDS._noLinePoints);
        assertEquals(2, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(2, painter._IDS._noLinePointsInContiguousLines.get(0));
        assertEquals(4, painter._IDS._noLinePointsInContiguousLines.get(1));

        assertEquals(2, painter._IDS._normalizedContiguousLines.size());

        {
            float[] expectedLines = new float[]{0.0f, 0.0f, 0.1f / 4.0f, 0.1f / 4.0f};
            assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.get(0).length);
            for (int i = 0; i < expectedLines.length; i++)
                assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.get(0)[i], 0.0001f);
        }
        {
            float[] expectedLines = new float[]{0.5f / 4.0f, 0.5f / 4.0f, 0.5f, 0.5f, 2.5f / 4.0f, 2.5f / 4.0f, 0.75f, 0.75f};
            assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.get(1).length);
            for (int i = 0; i < expectedLines.length; i++)
                assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.get(1)[i], 0.0001f);
        }

        {
            float[] expectedMarkers = new float[]{0.1f / 4.0f, 0.1f / 4.0f, 0.5f, 0.5f, 0.75f, 0.75f};
            assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
            for (int i = 0; i < expectedMarkers.length; i++)
                assertEquals(expectedMarkers[i], painter._IDS._normalizedMarkers[i], 0.0001f);
        }


        float[] expectedProjectedMarkers = new float[]{99.0f * (0.1f / 4.0f), 499.0f - 499.0f * (0.1f / 4.0f),
                99.0f * (0.5f), 499.0f - 499.0f * (0.5f),
                99.0f * (3.0f / 4.0f), 499.0f - 499.0f * (3.0f / 4.0f)};

        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.001f);

        assertEquals(2, painter._IDS._projectedContiguousLines.size());
        {
            float[] expectedProjectedLines = new float[]{
                    99.0f * (0.0f), 499.0f - 499.0f * (0.0f),
                    99.0f * (0.1f / 4.0f), 499.0f - 499.0f * (0.1f / 4.0f)};
            assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.get(0).length);
            for (int i = 0; i < expectedProjectedLines.length; i++)
                assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.get(0)[i], 0.001f);
        }
        {
            float[] expectedProjectedLines = new float[]{
                    99.0f * (0.5f / 4.0f), 499.0f - 499.0f * (0.5f / 4.0f),
                    99.0f * (0.5f), 499.0f - 499.0f * (0.5f),
                    99.0f * (2.5f / 4.0f), 499.0f - 499.0f * (2.5f / 4.0f),
                    99.0f * (0.75f), 499.0f - 499.0f * (0.75f)
            };
            assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.get(1).length);
            for (int i = 0; i < expectedProjectedLines.length; i++)
                assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.get(1)[i], 0.001f);
        }
    }

    /**
     * Test 2
     */
    @Test
    void Test2()
    {
        DisplayRangesManager DRM =
                new DisplayRangesManager(DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        MarkerStyle ms = new MarkerStyle(2.0f, Color.BLACK, Marker.SQUARE);
        ms._startPaintingFrom = 1;
        ms._paintEvery = 2;
        LineStyle ls = new LineStyle(1.0f, Color.RED);

        LinkedList<double[][]> d = new LinkedList<>();
        d.add(new double[][]{{-2.0f, -2.0f}});
        d.add(null);
        d.add(null);
        d.add(null);
        d.add(new double[][]{{2.0f, 2.0f}});

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(2, painter._IDS._noAttributes);
        assertEquals(1, painter._IDS._noMarkerPoints);
        assertEquals(0, painter._IDS._noLinePoints);
        assertEquals(0, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(0, painter._IDS._normalizedContiguousLines.size());

        {
            float[] expectedMarkers = new float[]{1.0f, 1.0f};
            assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
            for (int i = 0; i < expectedMarkers.length; i++)
                assertEquals(expectedMarkers[i], painter._IDS._normalizedMarkers[i], 0.0001f);
        }


        float[] expectedProjectedMarkers = new float[]{99.0f, 0.0f};

        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.0001f);

        assertEquals(0, painter._IDS._projectedContiguousLines.size());
    }

    /**
     * Test 3
     */
    @Test
    void Test3()
    {
        DisplayRangesManager DRM =
                new DisplayRangesManager(DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        MarkerStyle ms = new MarkerStyle(2.0f, Color.BLACK, Marker.SQUARE);
        ms._startPaintingFrom = 0;
        ms._paintEvery = 1;
        LineStyle ls = new LineStyle(1.0f, Color.RED);

        LinkedList<double[][]> d = new LinkedList<>();
        d.add(null);
        d.add(null);
        d.add(null);

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(2, painter._IDS._noAttributes);
        assertEquals(0, painter._IDS._noMarkerPoints);
        assertEquals(0, painter._IDS._noLinePoints);
        assertEquals(0, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(0, painter._IDS._normalizedContiguousLines.size());

        assertEquals(0, painter._IDS._projectedMarkers.length);
        assertEquals(0, painter._IDS._projectedContiguousLines.size());
    }


    /**
     * Test 4
     */
    @Test
    void Test4()
    {
        DisplayRangesManager DRM =
                new DisplayRangesManager(DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        MarkerStyle ms = new MarkerStyle(2.0f, Color.BLACK, Marker.SQUARE);
        ms._startPaintingFrom = 0;
        ms._paintEvery = 2;
        LineStyle ls = new LineStyle(1.0f, Color.RED);

        LinkedList<double[][]> d = new LinkedList<>();
        d.add(null);
        d.add(new double[][]{{-2.0f, -2.0f}});
        d.add(new double[][]{{0.0f, 0.0f}});
        d.add(new double[][]{{2.0f, 2.0f}});
        d.add(null);

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(2, painter._IDS._noAttributes);
        assertEquals(2, painter._IDS._noMarkerPoints);
        assertEquals(3, painter._IDS._noLinePoints);
        assertEquals(1, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(3, painter._IDS._noLinePointsInContiguousLines.get(0));
        assertEquals(1, painter._IDS._normalizedContiguousLines.size());

        {
            float[] expectedLines = new float[]{0.0f, 0.0f, 0.5f, 0.5f, 1.0f, 1.0f};
            assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.get(0).length);
            for (int i = 0; i < expectedLines.length; i++)
                assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.get(0)[i], 0.0001f);
        }

        {
            float[] expectedMarkers = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
            assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
            for (int i = 0; i < expectedMarkers.length; i++)
                assertEquals(expectedMarkers[i], painter._IDS._normalizedMarkers[i], 0.0001f);
        }


        float[] expectedProjectedMarkers = new float[]{
                99.0f * (0.0f), 499.0f - 499.0f * (0.0f),
                99.0f, 0.0f};

        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.001f);

        assertEquals(1, painter._IDS._projectedContiguousLines.size());
        {
            float[] expectedProjectedLines = new float[]{
                    99.0f * (0.0f), 499.0f - 499.0f * (0.0f),
                    99.0f * (0.5f), 499.0f - 499.0f * (0.5f),
                    99.0f, 0.0f};
            assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.get(0).length);
            for (int i = 0; i < expectedProjectedLines.length; i++)
                assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.get(0)[i], 0.001f);
        }
    }


    /**
     * Test 5
     */
    @Test
    void Test5()
    {
        DisplayRangesManager DRM =
                new DisplayRangesManager(DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        MarkerStyle ms = new MarkerStyle(1.0f, Color.RED, Marker.SQUARE);
        ms._paintEvery = 1;
        LineStyle ls = null;

        double[][] d1 = new double[][]{{-1.0d, -1.0d}};
        double[][] d2 = new double[][]{{0.0d, 0.0d}, {1.0d, 1.0d}};
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(d1);
        d.add(d2);

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(3, painter._IDS._noMarkerPoints);
        assertEquals(0, painter._IDS._noLinePoints);
        assertEquals(0, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(0, painter._IDS._normalizedContiguousLines.size());

        float[] expectedMarkers = new float[]{0.25f, 0.25f, 0.5f, 0.5f, 0.75f, 0.75f};
        assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
        for (int i = 0; i < expectedMarkers.length; i++)
            assertEquals(expectedMarkers[i], painter._IDS._normalizedMarkers[i], 0.0001f);

        float[] expectedProjectedMarkers = new float[]{99.0f * 0.25f, 499.0f - 499 * 0.25f, 99.0f * 0.5f, 499.0f - 499.0f * 0.5f, 99.0f * 0.75f, 499.0f - 499.0f * 0.75f};
        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.0001f);

        assertEquals(0, painter._IDS._projectedContiguousLines.size());
    }

    /**
     * Test 6
     */
    @Test
    void Test6()
    {
        DisplayRangesManager DRM =
                new DisplayRangesManager(DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        MarkerStyle ms = new MarkerStyle(1.0f, Color.RED, Marker.SQUARE);
        ms._paintEvery = 3;
        LineStyle ls = new LineStyle(1.0f, Color.GREEN);

        double[][] d1 = new double[][]{{-1.0d, -1.0d}};
        double[][] d2 = new double[][]{{0.0d, 0.0d}, {1.0d, 1.0d}};
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(d1);
        d.add(d2);

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(1, painter._IDS._noMarkerPoints);
        assertEquals(3, painter._IDS._noLinePoints);
        assertEquals(1, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(1, painter._IDS._normalizedContiguousLines.size());

        float[] expectedMarkers = new float[]{0.25f, 0.25f};
        assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
        for (int i = 0; i < expectedMarkers.length; i++)
            assertEquals(painter._IDS._normalizedMarkers[i], expectedMarkers[i], 0.0001f);

        assertEquals(1, painter._IDS._normalizedContiguousLines.size());
        float[] expectedLines = new float[]{0.25f, 0.25f, 0.5f, 0.5f, 0.75f, 0.75f};
        assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.getFirst().length);
        for (int i = 0; i < expectedMarkers.length; i++)
            assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.getFirst()[i], 0.0001f);

        float[] expectedProjectedMarkers = new float[]{99.0f * 0.25f, 499.0f - 499.0f * 0.25f};
        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.0001f);

        assertEquals(1, painter._IDS._projectedContiguousLines.size());
        float[] expectedProjectedLines = new float[]{99.0f * 0.25f,
                499.0f - 499.0f * 0.25f,
                99.0f * 0.5f,
                499.0f - 499.0f * 0.5f,
                99.0f * 0.75f,
                499.0f - 499.0f * 0.75f,};
        assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.getFirst().length);
        for (int i = 0; i < expectedProjectedLines.length; i++)
            assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.getFirst()[i], 0.0001f);
    }

    /**
     * Test 7
     */
    @Test
    void Test7()
    {
        DisplayRangesManager DRM =
                new DisplayRangesManager(DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        MarkerStyle ms = new MarkerStyle(1.0f, Color.RED, Marker.SQUARE);
        ms._paintEvery = 3;
        ms._startPaintingFrom = 1;
        LineStyle ls = new LineStyle(1.0f, Color.GREEN);

        double[][] d1 = new double[][]{{-1.0d, -1.0d}};
        double[][] d2 = new double[][]{{0.0d, 0.0d}, {1.0d, 1.0d}};
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(d1);
        d.add(d2);

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(1, painter._IDS._noMarkerPoints);
        assertEquals(3, painter._IDS._noLinePoints);
        assertEquals(1, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(1, painter._IDS._normalizedContiguousLines.size());

        float[] expectedMarkers = new float[]{0.5f, 0.5f};
        assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
        for (int i = 0; i < expectedMarkers.length; i++)
            assertEquals(painter._IDS._normalizedMarkers[i], expectedMarkers[i], 0.0001f);

        float[] expectedProjectedMarkers = new float[]{99.0f * 0.5f,
                499.0f - 499.0f * 0.5f};
        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.0001f);

        assertEquals(1, painter._IDS._projectedContiguousLines.size());
        float[] expectedProjectedLines = new float[]{99.0f * 0.25f,
                499.0f - 499.0f * 0.25f,
                99.0f * 0.5f,
                499.0f - 499.0f * 0.5f,
                99.0f * 0.75f,
                499.0f - 499.0f * 0.75f,};

        assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.getFirst().length);
        for (int i = 0; i < expectedProjectedLines.length; i++)
            assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.getFirst()[i], 0.0001f);
    }

    /**
     * Test 8
     */
    @Test
    void Test8()
    {
        DisplayRangesManager DRM =
                new DisplayRangesManager(DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        MarkerStyle ms = new MarkerStyle(1.0f, Color.RED, Marker.SQUARE);
        ms._paintEvery = 3;
        ms._startPaintingFrom = 2;
        LineStyle ls = new LineStyle(1.0f, Color.GREEN);

        double[][] d1 = new double[][]{{-1.0d, -1.0d}};
        double[][] d2 = new double[][]{{0.0d, 0.0d}, {1.0d, 1.0d}};
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(d1);
        d.add(d2);

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(1, painter._IDS._noMarkerPoints);
        assertEquals(3, painter._IDS._noLinePoints);
        assertEquals(1, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(1, painter._IDS._normalizedContiguousLines.size());

        float[] expectedMarkers = new float[]{0.75f, 0.75f};
        assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
        for (int i = 0; i < expectedMarkers.length; i++)
            assertEquals(painter._IDS._normalizedMarkers[i], expectedMarkers[i], 0.0001f);

        float[] expectedProjectedMarkers = new float[]{99.0f * 0.75f,
                499.0f - 499.0f * 0.75f};
        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.0001f);

        assertEquals(1, painter._IDS._projectedContiguousLines.size());
        float[] expectedProjectedLines = new float[]{99.0f * 0.25f,
                499.0f - 499.0f * 0.25f,
                99.0f * 0.5f,
                499.0f - 499.0f * 0.5f,
                99.0f * 0.75f,
                499.0f - 499.0f * 0.75f};
        assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.getFirst().length);
        for (int i = 0; i < expectedProjectedLines.length; i++)
            assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.getFirst()[i], 0.0001f);
    }

    /**
     * Test 9
     */
    @Test
    void Test9()
    {
        DisplayRangesManager DRM =
                new DisplayRangesManager(DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        MarkerStyle ms = new MarkerStyle(1.0f, Color.RED, Marker.SQUARE);
        ms._paintEvery = 3;
        ms._startPaintingFrom = 4;
        LineStyle ls = new LineStyle(1.0f, Color.GREEN);

        double[][] d1 = new double[][]{{-1.0d, -1.0d}};
        double[][] d2 = new double[][]{{0.0d, 0.0d}, {1.0d, 1.0d}};
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(d1);
        d.add(d2);

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(0, painter._IDS._noMarkerPoints);
        assertEquals(3, painter._IDS._noLinePoints);
        assertEquals(1, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(1, painter._IDS._normalizedContiguousLines.size());
        assertEquals(0, painter._IDS._normalizedMarkers.length);

        assertEquals(1, painter._IDS._projectedContiguousLines.size());
        float[] expectedProjectedLines = new float[]{
                99.0f * 0.25f,
                499.0f - 499.0f * 0.25f,
                99.0f * 0.5f,
                499.0f - 499.0f * 0.5f,
                99.0f * 0.75f,
                499.0f - 499.0f * 0.75f
        };
        assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.getFirst().length);
        for (int i = 0; i < expectedProjectedLines.length; i++)
            assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.getFirst()[i], 0.0001f);
    }


    /**
     * Test 10
     */
    @Test
    void Test10()
    {
        DisplayRangesManager.Params pDRM = new DisplayRangesManager.Params();
        pDRM._DR = new DisplayRangesManager.DisplayRange[2];
        pDRM._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-2.0d, 2.0d), false, false);
        pDRM._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-2.0d, 2.0d), false, false);
        pDRM._attIdx_to_drIdx = new Integer[]{null, 0, 1};
        DisplayRangesManager DRM = new DisplayRangesManager(pDRM);

        MarkerStyle ms = new MarkerStyle(2.0f, Color.BLACK, Marker.SQUARE);
        ms._startPaintingFrom = 1;
        ms._paintEvery = 2;
        LineStyle ls = new LineStyle(1.0f, Color.RED);

        LinkedList<double[][]> d = new LinkedList<>();
        d.add(new double[][]{{0.0d, -2.0f, -2.0f}, {0.0d, -1.9f, -1.9f}});
        d.add(null);
        d.add(new double[][]{{0.0d, -1.5f, -1.5f}});
        d.add(new double[][]{{0.0d, 0.0f, 0.0f}, {0.0d, 0.5f, 0.5f}, {0.0d, 1.0f, 1.0f}});
        d.add(null);
        d.add(null);
        d.add(new double[][]{{0.0d, 2.0f, 2.0f}});

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(2, painter._IDS._noAttributes);
        assertEquals(3, painter._IDS._noMarkerPoints);
        assertEquals(6, painter._IDS._noLinePoints);
        assertEquals(2, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(2, painter._IDS._noLinePointsInContiguousLines.get(0));
        assertEquals(4, painter._IDS._noLinePointsInContiguousLines.get(1));
        assertEquals(2, painter._IDS._normalizedContiguousLines.size());

        {
            float[] expectedLines = new float[]{0.0f, 0.0f, 0.1f / 4.0f, 0.1f / 4.0f};
            assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.get(0).length);
            for (int i = 0; i < expectedLines.length; i++)
                assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.get(0)[i], 0.0001f);
        }
        {
            float[] expectedLines = new float[]{0.5f / 4.0f, 0.5f / 4.0f, 0.5f, 0.5f, 2.5f / 4.0f, 2.5f / 4.0f, 0.75f, 0.75f};
            assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.get(1).length);
            for (int i = 0; i < expectedLines.length; i++)
                assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.get(1)[i], 0.0001f);
        }

        {
            float[] expectedMarkers = new float[]{0.1f / 4.0f, 0.1f / 4.0f, 0.5f, 0.5f, 0.75f, 0.75f};
            assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
            for (int i = 0; i < expectedMarkers.length; i++)
                assertEquals(expectedMarkers[i], painter._IDS._normalizedMarkers[i], 0.0001f);
        }


        float[] expectedProjectedMarkers = new float[]{99.0f * (0.1f / 4.0f), 499.0f - 499.0f * (0.1f / 4.0f),
                99.0f * (0.5f), 499.0f - 499.0f * (0.5f),
                99.0f * (3.0f / 4.0f), 499.0f - 499.0f * (3.0f / 4.0f)};

        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.001f);

        assertEquals(2, painter._IDS._projectedContiguousLines.size());
        {
            float[] expectedProjectedLines = new float[]{
                    99.0f * (0.0f), 499.0f - 499.0f * (0.0f),
                    99.0f * (0.1f / 4.0f), 499.0f - 499.0f * (0.1f / 4.0f)};
            assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.get(0).length);
            for (int i = 0; i < expectedProjectedLines.length; i++)
                assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.get(0)[i], 0.001f);
        }
        {
            float[] expectedProjectedLines = new float[]{
                    99.0f * (0.5f / 4.0f), 499.0f - 499.0f * (0.5f / 4.0f),
                    99.0f * (0.5f), 499.0f - 499.0f * (0.5f),
                    99.0f * (2.5f / 4.0f), 499.0f - 499.0f * (2.5f / 4.0f),
                    99.0f * (0.75f), 499.0f - 499.0f * (0.75f)
            };
            assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.get(1).length);
            for (int i = 0; i < expectedProjectedLines.length; i++)
                assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.get(1)[i], 0.001f);
        }
    }


    /**
     * Test 11
     */
    @Test
    void Test11()
    {
        DisplayRangesManager.Params pDRM = new DisplayRangesManager.Params();
        pDRM._DR = new DisplayRangesManager.DisplayRange[2];
        pDRM._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-2.0d, 2.0d), false, false);
        pDRM._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-2.0d, 2.0d), false, false);
        pDRM._attIdx_to_drIdx = new Integer[]{null, 1, 0};
        DisplayRangesManager DRM = new DisplayRangesManager(pDRM);

        MarkerStyle ms = new MarkerStyle(2.0f, Color.BLACK, Marker.SQUARE);
        ms._startPaintingFrom = 1;
        ms._paintEvery = 2;
        LineStyle ls = new LineStyle(1.0f, Color.RED);

        LinkedList<double[][]> d = new LinkedList<>();
        d.add(new double[][]{{0.0d, -2.0f, -2.0f}, {0.0d, -1.9f, -1.9f}});
        d.add(null);
        d.add(new double[][]{{0.0d, -1.5f, -1.5f}});
        d.add(new double[][]{{0.0d, 1.0f, -1.0f}, {0.0d, 0.5f, 0.5f}, {0.0d, 1.0f, 1.0f}});
        d.add(null);
        d.add(null);
        d.add(new double[][]{{0.0d, 2.0f, 2.0f}});

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(2, painter._IDS._noAttributes);
        assertEquals(3, painter._IDS._noMarkerPoints);
        assertEquals(6, painter._IDS._noLinePoints);
        assertEquals(2, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(2, painter._IDS._noLinePointsInContiguousLines.get(0));
        assertEquals(4, painter._IDS._noLinePointsInContiguousLines.get(1));

        assertEquals(2, painter._IDS._normalizedContiguousLines.size());


        {
            float[] expectedMarkers = new float[]{0.1f / 4.0f, 0.1f / 4.0f, 0.75f, 0.25f, 0.75f, 0.75f};
            assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
            for (int i = 0; i < expectedMarkers.length; i++)
                assertEquals(expectedMarkers[i], painter._IDS._normalizedMarkers[i], 0.0001f);
        }

        {
            PrintUtils.printVectorOfFloats(painter._IDS._normalizedContiguousLines.get(0), 2);
            float[] expectedLines = new float[]{0.0f, 0.0f, 0.1f / 4.0f, 0.1f / 4.0f};
            assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.get(0).length);
            for (int i = 0; i < expectedLines.length; i++)
                assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.get(0)[i], 0.0001f);
        }
        {
            float[] expectedLines = new float[]{0.5f / 4.0f, 0.5f / 4.0f, 0.75f, 0.25f, 2.5f / 4.0f, 2.5f / 4.0f, 0.75f, 0.75f};
            assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.get(1).length);
            for (int i = 0; i < expectedLines.length; i++)
                assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.get(1)[i], 0.0001f);
        }


        float[] expectedProjectedMarkers = new float[]
                {
                        99.0f * (0.1f / 4.0f), 499.0f - 499.0f * (0.1f / 4.0f),
                        99.0f * (0.25f), 499.0f - 499.0f * (0.75f),
                        99.0f * (3.0f / 4.0f), 499.0f - 499.0f * (3.0f / 4.0f),
                };

        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.001f);

        assertEquals(2, painter._IDS._projectedContiguousLines.size());
        {
            float[] expectedProjectedLines = new float[]{
                    99.0f * (0.0f), 499.0f - 499.0f * (0.0f),
                    99.0f * (0.1f / 4.0f), 499.0f - 499.0f * (0.1f / 4.0f)
            };
            assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.get(0).length);
            for (int i = 0; i < expectedProjectedLines.length; i++)
                assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.get(0)[i], 0.001f);
        }
        {
            float[] expectedProjectedLines = new float[]{
                    99.0f * (0.5f / 4.0f), 499.0f - 499.0f * (0.5f / 4.0f),
                    99.0f * (0.25f), 499.0f - 499.0f * (0.75f),
                    99.0f * (2.5f / 4.0f), 499.0f - 499.0f * (2.5f / 4.0f),
                    99.0f * (0.75f), 499.0f - 499.0f * (0.75f)
            };
            assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.get(1).length);
            for (int i = 0; i < expectedProjectedLines.length; i++)
                assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.get(1)[i], 0.001f);
        }
    }

    /**
     * Test 12
     */
    @Test
    void Test12()
    {
        DisplayRangesManager.Params pDRM = new DisplayRangesManager.Params();
        pDRM._DR = new DisplayRangesManager.DisplayRange[2];
        pDRM._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-2.0d, 2.0d), false, false);
        pDRM._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-2.0d, 2.0d), false, false);
        pDRM._attIdx_to_drIdx = new Integer[]{null, 1, null, 0, null, null};
        DisplayRangesManager DRM = new DisplayRangesManager(pDRM);

        MarkerStyle ms = new MarkerStyle(2.0f, Color.BLACK, Marker.SQUARE);
        ms._startPaintingFrom = 1;
        ms._paintEvery = 2;
        LineStyle ls = new LineStyle(1.0f, Color.RED);

        LinkedList<double[][]> d = new LinkedList<>();
        d.add(new double[][]{{0.0d, -2.0f, 0.0f, -2.0f, 0.0f, 0.0f}, {0.0d, -1.9f, 0.0f, -1.9f, 0.0f, 0.0f}});
        d.add(null);
        d.add(new double[][]{{0.0d, -1.5f, 0.0f, -1.5f, 0.0f, 0.0f}});
        d.add(new double[][]{{0.0d, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f}, {0.0d, 0.5f, 0.0f, 0.5f, 0.0f, 0.0f}, {0.0d, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f}});
        d.add(null);
        d.add(null);
        d.add(new double[][]{{0.0d, 2.0f, 0.0f, 2.0f, 0.0f, 0.0f}});

        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, true);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(ms, ls));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();

        assertEquals(2, painter._IDS._noAttributes);
        assertEquals(3, painter._IDS._noMarkerPoints);
        assertEquals(6, painter._IDS._noLinePoints);
        assertEquals(2, painter._IDS._noLinePointsInContiguousLines.size());
        assertEquals(2, painter._IDS._noLinePointsInContiguousLines.get(0));
        assertEquals(4, painter._IDS._noLinePointsInContiguousLines.get(1));

        assertEquals(2, painter._IDS._normalizedContiguousLines.size());


        {
            float[] expectedMarkers = new float[]{0.1f / 4.0f, 0.1f / 4.0f, 0.75f, 0.25f, 0.75f, 0.75f};
            assertEquals(expectedMarkers.length, painter._IDS._normalizedMarkers.length);
            for (int i = 0; i < expectedMarkers.length; i++)
                assertEquals(expectedMarkers[i], painter._IDS._normalizedMarkers[i], 0.0001f);
        }

        {
            PrintUtils.printVectorOfFloats(painter._IDS._normalizedContiguousLines.get(0), 2);
            float[] expectedLines = new float[]{0.0f, 0.0f, 0.1f / 4.0f, 0.1f / 4.0f};
            assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.get(0).length);
            for (int i = 0; i < expectedLines.length; i++)
                assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.get(0)[i], 0.0001f);
        }
        {
            float[] expectedLines = new float[]{0.5f / 4.0f, 0.5f / 4.0f, 0.75f, 0.25f, 2.5f / 4.0f, 2.5f / 4.0f, 0.75f, 0.75f};
            assertEquals(expectedLines.length, painter._IDS._normalizedContiguousLines.get(1).length);
            for (int i = 0; i < expectedLines.length; i++)
                assertEquals(expectedLines[i], painter._IDS._normalizedContiguousLines.get(1)[i], 0.0001f);
        }


        float[] expectedProjectedMarkers = new float[]
                {
                        99.0f * (0.1f / 4.0f), 499.0f - 499.0f * (0.1f / 4.0f),
                        99.0f * (0.25f), 499.0f - 499.0f * (0.75f),
                        99.0f * (3.0f / 4.0f), 499.0f - 499.0f * (3.0f / 4.0f),
                };

        assertEquals(expectedProjectedMarkers.length, painter._IDS._projectedMarkers.length);
        for (int i = 0; i < expectedProjectedMarkers.length; i++)
            assertEquals(expectedProjectedMarkers[i], painter._IDS._projectedMarkers[i], 0.001f);

        assertEquals(2, painter._IDS._projectedContiguousLines.size());
        {
            float[] expectedProjectedLines = new float[]{
                    99.0f * (0.0f), 499.0f - 499.0f * (0.0f),
                    99.0f * (0.1f / 4.0f), 499.0f - 499.0f * (0.1f / 4.0f)
            };
            assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.get(0).length);
            for (int i = 0; i < expectedProjectedLines.length; i++)
                assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.get(0)[i], 0.001f);
        }
        {
            float[] expectedProjectedLines = new float[]{
                    99.0f * (0.5f / 4.0f), 499.0f - 499.0f * (0.5f / 4.0f),
                    99.0f * (0.25f), 499.0f - 499.0f * (0.75f),
                    99.0f * (2.5f / 4.0f), 499.0f - 499.0f * (2.5f / 4.0f),
                    99.0f * (0.75f), 499.0f - 499.0f * (0.75f)
            };
            assertEquals(expectedProjectedLines.length, painter._IDS._projectedContiguousLines.get(1).length);
            for (int i = 0; i < expectedProjectedLines.length; i++)
                assertEquals(expectedProjectedLines[i], painter._IDS._projectedContiguousLines.get(1)[i], 0.001f);
        }
    }
}