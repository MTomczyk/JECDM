package t1_10.t8_experimentation_tools_and_screenshots.t3_3D_screenshot;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Line;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import io.FileUtils;
import io.image.ImageSaver;
import plot.Plot3D;
import scheme.WhiteScheme;
import space.Range;
import utils.Screenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This tutorial demonstrates how to automatically create and save the plot render as an image.
 *
 * @author MTomczyk
 */
public class Tutorial3
{
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(new Range(-1.0f, 1.0f),
                new Range(-1.0f, 1.0f), new Range(-1.0f, 1.0f));
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";
        pP._scheme = WhiteScheme.getForPlot3D();
        pP._drawLegend = true;

        Plot3D plot = new Plot3D(pP);

        ArrayList<IDataSet> dataSets = new ArrayList<>(4);
        {
            double[][] data = new double[][]
                    {
                            {-1.0f, -1.0f, -1.0f}, {-0.5f, -0.5f, -0.5f},
                            {0.0f, 0.0f, 0.0f}, {0.5f, 0.5f, 0.5f}, {1.0f, 1.0f, 1.0f}
                    };
            LineStyle ls = new LineStyle(1.0f, Color.BLACK, 0.05f); // bypass legend entry size
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.CUBE_3D, ls);
            dataSets.add(DataSet.getFor3D("DS1", data, ms));
        }

        {
            double[][] data = new double[][]
                    {
                            {-1.0f, 0.25f, 0.25f},
                            {-0.5f, 0.25f, 0.25f},
                            {0.0f, 0.25f, 0.25f},
                            {0.5f, 0.25f, 0.25f},
                            {1.0f, 0.25f, 0.25f},
                    };
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getPlasmaGradient(), 0, Marker.SPHERE_HIGH_POLY_3D);
            dataSets.add(DataSet.getFor3D("DS2", data, ms));
        }

        {
            double[][] data = new double[][]
                    {
                            {-1.0f, -1.0f, 1.0f},
                            {0.0f, -1.0f, -0.25f},
                            {1.0f, 0.5f, -0.5f},
                    };
            LineStyle ls = new LineStyle(0.05f, Gradient.getViridisGradient(), 2, Line.POLY_OCTO);
            dataSets.add(DataSet.getFor3D("DS3", data, ls));
        }

        Frame frame = new Frame(plot, 1200, 1200);
        plot.getModel().setDataSets(dataSets, true);

        // The below line adjusts the projection. Note that introducing more user-friendly means of manipulating them is
        // part of plans for future development.
        plot.getController().getInteractListener().getTranslation()[0] = 0.0f;
        plot.getController().getInteractListener().getTranslation()[1] = 0.0f;
        plot.getController().getInteractListener().getTranslation()[2] = 2.16f;
        plot.getController().getInteractListener().getObjectRotation()[0] = 6.97f;
        plot.getController().getInteractListener().getObjectRotation()[1] = 36.18f;
        plot.getController().getInteractListener().getCameraRotation()[0] = 0.0f;
        plot.getController().getInteractListener().getCameraRotation()[1] = 0.0f;


        frame.setVisible(true);

        Screenshot screenshot = plot.getModel().requestScreenshotCreation(1000, 1000);

        try
        {
            screenshot._barrier.await();
            Path path = FileUtils.getPathRelatedToClass(Tutorial3.class, "Private", "src", File.separatorChar);
            String screenshotPath = path + File.separator + "screenshot";
            ImageSaver.saveImage(screenshot._image, screenshotPath, "jpg", 1.0f);

        } catch (InterruptedException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
