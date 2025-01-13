package visualization.plot3D;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DSFactory3D;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;

import java.util.ArrayList;

/**
 * Tests selective DS update.
 *
 * @author MTomczyk
 */
public class Test34_SelectiveUpdate1
{
    /**
     * Runs the test
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._scheme = WhiteScheme.getForPlot3D(0.3f);
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP_EXTERNAL);
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._drawLegend = true;
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(new Range(-2.0d, 2.0d), new Range(-2.0d, 2.0d),
                new Range(-2.0d, 2.0d));
        Plot3D plot3D = new Plot3D(pP);

        Frame frame = new Frame(plot3D, 0.5f);

        {
            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            dataSets.add(DSFactory3D.getDS("DS1", new double[][]{
                            {-1.0d, 0.0d, 0.0d}, {1.0d, 0.0d, 0.0d}, {0.0d, 0.0d, 0.0d}, {0.0d, 1.0d, 0.0d}, {0.0d, -1.0d, 0.0d}},
                    new MarkerStyle(0.05f, Color.BLUE, Marker.SPHERE_MEDIUM_POLY_3D)));
            dataSets.add(DSFactory3D.getDS("DS2", new double[][]{{-2.0d, 0.0d, 0.0d}, {2.0d, 0.0d, 0.0d},
                            {0.0d, 2.0d, 0.0d}, {0.0d, -2.0d, 0.0d}},
                    new MarkerStyle(0.05f, Color.RED, Marker.SPHERE_MEDIUM_POLY_3D)));
            plot3D.getModel().setDataSets(dataSets, true);
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
            plot3D.getModel().updateSelectedDataSets(dataSets, true, true);
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
            dataSets.add(DSFactory3D.getDS("DS2", new double[][]{{-2.0d, 1.0d, 0.0d}, {2.0d, -1.0d, 0.0d},
                            {1.0d, 2.0d, 0.0d}, {-1.0d, -2.0d, 0.0d}},
                    new MarkerStyle(0.05f, Color.RED, Marker.SPHERE_MEDIUM_POLY_3D)));
            plot3D.getModel().updateSelectedDataSets(dataSets);
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
            dataSets.add(DSFactory3D.getDS("DS3", new double[][]{{-2.0d, 1.0d},
                            {2.0d, -1.0d, 0.0d}, {1.0d, 2.0d, 0.0d}, {-1.0d, -2.0d, 0.0d}},
                    new MarkerStyle(0.05f, Color.GREEN, Marker.SPHERE_MEDIUM_POLY_3D)));
            plot3D.getModel().updateSelectedDataSets(dataSets);
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
            dataSets.add(DSFactory3D.getDS("DS3", new double[][]{{-2.0d, 1.0d, 0.0d}, {2.0d, -1.0d, 0.0d},
                            {1.0d, 2.0d, 0.0d}, {-1.0d, -2.0d, 0.0d}},
                    new MarkerStyle(0.05f, Color.GREEN, Marker.SPHERE_MEDIUM_POLY_3D)));
            dataSets.add(DSFactory3D.getDS("DS4", new double[][]{{-2.0d, 1.0d, 1.0d}, {2.0d, -1.0d, 1.0d},
                            {1.0d, 2.0d, 1.0d}, {-1.0d, -2.0d, 1.0d}},
                    new MarkerStyle(0.05f, Gradient.getPlasmaGradient(), 0, Marker.SPHERE_MEDIUM_POLY_3D)));
            plot3D.getModel().updateSelectedDataSets(dataSets);
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
            dataSets.add(DSFactory3D.getDS("DS1", new double[][]{{-2.0d, 1.0d, 0.0d}, {2.0d, -1.0d, 0.0d},
                            {1.0d, 2.0d, 0.0d}, {-1.0d, -2.0d, 0.0d}},
                    new MarkerStyle(0.05f, Color.GREEN, Marker.SPHERE_MEDIUM_POLY_3D)));
            dataSets.add(DSFactory3D.getDS("DS2", new double[][]{{-2.0d, 1.0d, 1.0d}, {2.0d, -1.0d, 1.0d},
                            {1.0d, 2.0d, 1.0d}, {-1.0d, -2.0d, 1.0d}},
                    new MarkerStyle(0.05f, Gradient.getPlasmaGradient(), 0, Marker.SPHERE_MEDIUM_POLY_3D)));
            plot3D.getModel().updateSelectedDataSets(dataSets);
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
            dataSets.add(DSFactory3D.getDS("DS5", new double[][]{{-1.0d, 0.0d, 0.0d}, {1.0d, 0.0d, 0.0d},
                            {0.0d, 0.0d, 0.0d}, {0.0d, 1.0d, 0.0d}, {0.0d, -1.0d, 0.0d}},
                    new MarkerStyle(0.05f, Color.GREEN, Marker.SPHERE_MEDIUM_POLY_3D)));
            dataSets.add(DSFactory3D.getDS("DS6", new double[][]{{-2.0d, 1.0d, 0.0d}, {2.0d, -1.0d, 0.0d},
                            {1.0d, 2.0d, 0.0d}, {-1.0d, -2.0d, 0.0d}},
                    new MarkerStyle(0.05f, Gradient.getPlasmaGradient(), 0, Marker.SPHERE_MEDIUM_POLY_3D)));
            plot3D.getModel().setDataSets(dataSets);
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
            dataSets.add(DSFactory3D.getDS("DS7", new double[][]{{-1.0d, 0.0d, 0.0d}, {1.0d, 0.0d, 0.0d},
                            {0.0d, 0.0d, 0.0d}, {0.0d, 1.0d, 0.0d}, {0.0d, -1.0d, 0.0d}},
                    new MarkerStyle(0.05f, Color.GREEN, Marker.SPHERE_MEDIUM_POLY_3D)));
            dataSets.add(DSFactory3D.getDS("DS8", new double[][]{{-2.0d, 1.0d, 0.0d}, {2.0d, -1.0d, 0.0d},
                            {1.0d, 2.0d, 0.0d}, {-1.0d, -2.0d, 0.0d}},
                    new MarkerStyle(0.05f, Gradient.getPlasmaGradient(), 0, Marker.SPHERE_MEDIUM_POLY_3D)));
            plot3D.getModel().setDataSets(dataSets);
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
            dataSets.add(DSFactory3D.getDS("DS70", new double[][]{{-1.0d, 0.0d, 0.0d}, {1.0d, 0.0d, 0.0d},
                            {0.0d, 0.0d, 0.0d}, {0.0d, 1.0d, 0.0d}, {0.0d, -1.0d, 0.0d}},
                    new MarkerStyle(0.05f, Gradient.getRedBlueGradient(), 0, Marker.SPHERE_MEDIUM_POLY_3D)));
            dataSets.add(DSFactory3D.getDS("DS80", new double[][]{{-1.0d, 1.0d}, {1.0d, -1.0d}, {1.0d, 1.0d}, {-1.0d, -1.0d}},
                    new MarkerStyle(0.05f, Gradient.getPlasmaGradient(), 0, Marker.SPHERE_MEDIUM_POLY_3D)));
            plot3D.getModel().updateSelectedDataSets(dataSets);
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
            dataSets.add(DSFactory3D.getDS("DS700", new double[][]{{-1.0d, 0.0d, 0.0d}, {1.0d, 0.0d, 0.0d},
                            {0.0d, 0.0d, 0.0d}, {0.0d, 1.0d, 0.0d}, {0.0d, -1.0d, 0.0d}},
                    new MarkerStyle(0.05f, Gradient.getRedBlueGradient(), 0, Marker.SPHERE_MEDIUM_POLY_3D)));
            dataSets.add(DSFactory3D.getDS("DS800", new double[][]{{-1.0d, 1.0d, 0.0d}, {1.0d, -1.0d, 0.0d},
                            {1.0d, 1.0d, 0.0d}, {-1.0d, -1.0d, 0.0d}},
                    new MarkerStyle(0.05f, Gradient.getPlasmaGradient(), 0, Marker.SPHERE_MEDIUM_POLY_3D)));
            plot3D.getModel().setDataSets(dataSets, true, true);
        }
    }
}
