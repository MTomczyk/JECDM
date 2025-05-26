package dataset.painter;

import color.gradient.Color;
import container.GlobalContainer;
import dataset.Data;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.enums.Arrow;
import drmanager.DisplayRangesManager;
import org.junit.jupiter.api.Test;
import space.Dimension;
import space.Range;
import thread.swingworker.EventTypes;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Some tests for {@link Painter2D} class (focus on arrows-related data).
 *
 * @author MTomczyk
 */
class Painter2DArrowsTest
{
    /**
     * Test 1
     */
    @Test
    void Test1()
    {
        LineStyle[] lss = new LineStyle[2];
        ArrowStyles[] ass = new ArrowStyles[4];

        lss[0] = null;
        lss[1] = new LineStyle(1.0f, Color.RED);
        ass[0] = null;
        ass[1] = new ArrowStyles(new ArrowStyle(0.0f, 0.0f, Color.RED));
        ass[2] = new ArrowStyles(new ArrowStyle(1.0f, 1.0f, Color.RED));
        ass[3] = new ArrowStyles(new ArrowStyle(1.0f, 1.0f, Color.RED, Arrow.NONE));


        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (boolean mode : new boolean[]{false, true})
                {
                    DisplayRangesManager DRM = new DisplayRangesManager(
                            DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                                    new Range(-2.0d, 2.0d)));

                    double x = 2;
                    LinkedList<double[][]> d = new LinkedList<>();
                    d.add(null);
                    d.add(new double[][]{{x, 1.0d}});
                    d.add(new double[][]{{x, 2.0d}});
                    d.add(new double[][]{{x, 4.0d}, null, {x + 1.0d, 5.0d}, {x + 1.0d, 6.0d}});
                    d.add(null); // hard break
                    d.add(new double[][]{{x + 1.0d, 7.0d}, {x + 1.0d, 8.0d}, {x + 1.0d, 9.0d}});
                    Data data = new Data(d);

                    GlobalContainer GC = new GlobalContainer(null, false);
                    AbstractPainter painter = new Painter2D(new Painter2D.Params(null, lss[i], ass[j], mode, 0.005f));
                    painter.setData(data);
                    painter.setContainers(GC, null);
                    painter.beginDataProcessing(true);
                    painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
                    Dimension[] dimensions = new Dimension[2];
                    dimensions[0] = new Dimension(0.0f, 100.0f);
                    dimensions[1] = new Dimension(0.0f, 500.0f);
                    painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
                    painter.finishDataProcessing();

                    if ((i != 1) || (j != 2)) assertEquals(0, painter._IDS._noLinesWithArrows);
                    else
                    {
                        if (mode) assertEquals(3, painter._IDS._noLinesWithArrows);
                        else assertEquals(2, painter._IDS._noLinesWithArrows);
                    }
                }
            }
        }
    }

    /**
     * Test 2
     */
    @Test
    void Test2()
    {
        for (boolean mode : new boolean[]{false, true})
        {
            DisplayRangesManager DRM = new DisplayRangesManager(
                    DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                            new Range(-2.0d, 2.0d)));

            LinkedList<double[][]> d = new LinkedList<>();
            d.add(new double[][]{{0.0d, 0.0d}, {1.0d, 1.0d}});
            Data data = new Data(d);

            GlobalContainer GC = new GlobalContainer(null, false);
            AbstractPainter painter = new Painter2D(new Painter2D.Params(null, new LineStyle(1.0f, Color.BLACK),
                    new ArrowStyles(new ArrowStyle(1.0f, 1.0f, Color.BLACK)), mode, 0.005f));
            painter.setData(data);
            painter.setContainers(GC, null);
            painter.beginDataProcessing(true);
            painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
            Dimension[] dimensions = new Dimension[2];
            dimensions[0] = new Dimension(0.0f, 100.0f);
            dimensions[1] = new Dimension(0.0f, 500.0f);
            painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
            painter.finishDataProcessing();
            assertEquals(1, painter._IDS._noLinesWithArrows);
        }
    }

    /**
     * Test 3
     */
    @Test
    void Test3()
    {
        DisplayRangesManager DRM = new DisplayRangesManager(
                DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        LinkedList<double[][]> d = new LinkedList<>();
        d.add(null); // skipped
        d.add(null); // skipped
        d.add(new double[][]
                {
                        {0.2d, 0.6d},
                        null, // skipped
                        {0.4d, 0.7d},

                });
        d.add(null);
        d.add(new double[][]
                {
                        {0.5d, 0.7d},
                        null, // skipped
                        {0.6d, 0.7d},
                        {0.7d, 0.7d}, // odd, not used (but marker will be drawn)
                });
        d.add(null); // skipped
        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, false);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(null, new LineStyle(1.0f, Color.BLACK),
                new ArrowStyles(new ArrowStyle(1.0f, 1.0f, Color.BLACK)), true, 0.005f));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();
        assertEquals(2, painter._IDS._noLinesWithArrows);
    }

    /**
     * Test 4
     */
    @Test
    void Test4()
    {
        DisplayRangesManager DRM = new DisplayRangesManager(
                DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        LinkedList<double[][]> d = new LinkedList<>();
        d.add(null); // skipped
        d.add(new double[][]
                {
                        {0.9d, 0.1d},
                        null, // skipped
                        {0.7d, 0.2d},
                        null, // skipped
                        {0.7d, 0.3d},
                        null, // skipped
                        null, // skipped
                        {0.8d, 0.6d},
                        {0.9d, 0.7d},  // odd, not used (but marker will be drawn)
                        null // skipped
                });
        d.add(null); // skipped
        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, false);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(null, new LineStyle(1.0f, Color.BLACK),
                new ArrowStyles(new ArrowStyle(1.0f, 1.0f, Color.BLACK)), true, 0.005f));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();
        assertEquals(2, painter._IDS._noLinesWithArrows);
    }

    /**
     * Test 5
     */
    @Test
    void Test5()
    {
        DisplayRangesManager DRM = new DisplayRangesManager(
                DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d),
                        new Range(-2.0d, 2.0d)));

        double[][] d = new double[50][2];
        for (int i = 0; i < 50; i++)
        {
            d[i][0] = 0.05d;
            d[i][1] = 0.05d + i * 0.0185d;
        }
        Data data = new Data(d);

        GlobalContainer GC = new GlobalContainer(null, false);
        AbstractPainter painter = new Painter2D(new Painter2D.Params(null, new LineStyle(1.0f, Color.BLACK),
                new ArrowStyles(new ArrowStyle(1.0f, 1.0f, Color.BLACK)), true, 0.005f));
        painter.setData(data);
        painter.setContainers(GC, null);
        painter.beginDataProcessing(true);
        painter.updateFirstLevelIDS(DRM, EventTypes.ON_DEMAND);
        Dimension[] dimensions = new Dimension[2];
        dimensions[0] = new Dimension(0.0f, 100.0f);
        dimensions[1] = new Dimension(0.0f, 500.0f);
        painter.updateSecondLevelIDS(dimensions, EventTypes.ON_DEMAND);
        painter.finishDataProcessing();
        assertEquals(25, painter._IDS._noLinesWithArrows);
    }
}