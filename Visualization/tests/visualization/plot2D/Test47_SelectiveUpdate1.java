package visualization.plot2D;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;

import java.util.ArrayList;

/**
 * Tests selective DS update.
 *
 * @author MTomczyk
 */
public class Test47_SelectiveUpdate1
{
    /**
     * Runs the test
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP_EXTERNAL);
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._drawLegend = true;
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(-2.0d, 2.0d), new Range(-2.0d, 2.0d));
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        Plot2D plot2D = new Plot2D(pP);

        Frame frame = new Frame(plot2D, 0.5f);

        {
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory2D.getDS("DS1", new double[][]{{-1.0d, 0.0d}, {1.0d, 0.0d}, {0.0d, 0.0d}, {0.0d, 1.0d}, {0.0d, -1.0d}},
                    new MarkerStyle(5.0f, Color.BLUE, Marker.SQUARE)));
            dataSets.add(DSFactory2D.getDS("DS2", new double[][]{{-2.0d, 0.0d}, {2.0d, 0.0d}, {0.0d, 2.0d}, {0.0d, -2.0d}},
                    new MarkerStyle(5.0f, Color.RED, Marker.SQUARE)));
            plot2D.getModel().setDataSets(dataSets, true);
        }

        frame.setVisible(true);

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        {
            System.out.println("Update 1");
            // should not change anything
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            plot2D.getModel().updateSelectedDataSets(dataSets, true, true);
        }

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        {
            System.out.println("Update 2");
            // red should change
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory2D.getDS("DS2", new double[][]{{-2.0d, 1.0d}, {2.0d, -1.0d}, {1.0d, 2.0d}, {-1.0d, -2.0d}},
                    new MarkerStyle(5.0f, Color.RED, Marker.SQUARE)));
            plot2D.getModel().updateSelectedDataSets(dataSets);
        }

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        {
            System.out.println("Update 3");
            // should be ignored
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory2D.getDS("DS3", new double[][]{{-2.0d, 1.0d}, {2.0d, -1.0d}, {1.0d, 2.0d}, {-1.0d, -2.0d}},
                    new MarkerStyle(5.0f, Color.GREEN, Marker.SQUARE)));
            plot2D.getModel().updateSelectedDataSets(dataSets);
        }

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        {
            System.out.println("Update 4");
            // should be ignored
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory2D.getDS("DS3", new double[][]{{-2.0d, 1.0d}, {2.0d, -1.0d}, {1.0d, 2.0d}, {-1.0d, -2.0d}},
                    new MarkerStyle(5.0f, Color.GREEN, Marker.SQUARE)));
            dataSets.add(DSFactory2D.getDS("DS4", new double[][]{{-2.0d, 1.0d}, {2.0d, -1.0d}, {1.0d, 2.0d}, {-1.0d, -2.0d}},
                    new MarkerStyle(5.0f, Gradient.getPlasmaGradient(), 0, Marker.SQUARE)));
            plot2D.getModel().updateSelectedDataSets(dataSets);
        }

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        {
            System.out.println("Update 5");
            // both should be set
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory2D.getDS("DS1", new double[][]{{-2.0d, 1.0d}, {2.0d, -1.0d}, {1.0d, 2.0d}, {-1.0d, -2.0d}},
                    new MarkerStyle(5.0f, Color.GREEN, Marker.SQUARE)));
            dataSets.add(DSFactory2D.getDS("DS2", new double[][]{{-2.0d, 0.0d}, {2.0d, 0.0d}, {0.0d, 2.0d}, {0.0d, -2.0d}},
                    new MarkerStyle(5.0f, Gradient.getPlasmaGradient(), 0, Marker.SQUARE)));
            plot2D.getModel().updateSelectedDataSets(dataSets);
        }

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        {
            System.out.println("Update 6");
            // should be set
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory2D.getDS("DS5", new double[][]{{-1.0d, 0.0d}, {1.0d, 0.0d}, {0.0d, 0.0d}, {0.0d, 1.0d}, {0.0d, -1.0d}},
                    new MarkerStyle(5.0f, Color.GREEN, Marker.SQUARE)));
            dataSets.add(DSFactory2D.getDS("DS6", new double[][]{{-2.0d, 1.0d}, {2.0d, -1.0d}, {1.0d, 2.0d}, {-1.0d, -2.0d}},
                    new MarkerStyle(5.0f, Gradient.getPlasmaGradient(), 0, Marker.SQUARE)));
            plot2D.getModel().setDataSets(dataSets);
        }

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        {
            System.out.println("Update 7");
            // should be set
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory2D.getDS("DS7", new double[][]{{-1.0d, 0.0d}, {1.0d, 0.0d}, {0.0d, 0.0d}, {0.0d, 1.0d}, {0.0d, -1.0d}},
                    new MarkerStyle(5.0f, Color.GREEN, Marker.SQUARE)));
            dataSets.add(DSFactory2D.getDS("DS8", new double[][]{{-2.0d, 1.0d}, {2.0d, -1.0d}, {1.0d, 2.0d}, {-1.0d, -2.0d}},
                    new MarkerStyle(5.0f, Gradient.getPlasmaGradient(), 0, Marker.SQUARE)));
            plot2D.getModel().setDataSets(dataSets);
        }

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        {
            System.out.println("Update 8");
            // should be ignored
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory2D.getDS("DS70", new double[][]{{-1.0d, 0.0d}, {1.0d, 0.0d}, {0.0d, 0.0d}, {0.0d, 1.0d}, {0.0d, -1.0d}},
                    new MarkerStyle(5.0f, Gradient.getRedBlueGradient(), 0, Marker.SQUARE)));
            dataSets.add(DSFactory2D.getDS("DS80", new double[][]{{-1.0d, 1.0d}, {1.0d, -1.0d}, {1.0d, 1.0d}, {-1.0d, -1.0d}},
                    new MarkerStyle(5.0f, Gradient.getPlasmaGradient(), 0, Marker.SQUARE)));
            plot2D.getModel().updateSelectedDataSets(dataSets);
        }


        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        {
            System.out.println("Update 9");
            // should be set
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory2D.getDS("DS700", new double[][]{{-1.0d, 0.0d}, {1.0d, 0.0d}, {0.0d, 0.0d}, {0.0d, 1.0d}, {0.0d, -1.0d}},
                    new MarkerStyle(5.0f, Gradient.getRedBlueGradient(), 0, Marker.SQUARE)));
            dataSets.add(DSFactory2D.getDS("DS800", new double[][]{{-1.0d, 1.0d}, {1.0d, -1.0d}, {1.0d, 1.0d}, {-1.0d, -1.0d}},
                    new MarkerStyle(5.0f, Gradient.getPlasmaGradient(), 0, Marker.SQUARE)));
            plot2D.getModel().setDataSets(dataSets, true, true);
        }
    }
}
