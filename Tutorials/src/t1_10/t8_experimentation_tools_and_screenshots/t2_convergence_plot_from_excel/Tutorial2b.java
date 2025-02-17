package t1_10.t8_experimentation_tools_and_screenshots.t2_convergence_plot_from_excel;

import dataset.IDataSet;
import tools.ConvergencePlotFromXLSX;
import tools.DataMatrixCoordinates;
import tools.DataSetData;
import frame.Frame;
import io.FileUtils;
import io.image.ImageSaver;
import plot.AbstractPlot;
import plot.Plot2D;
import space.Range;
import utils.Screenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This tutorial showcases how to use {@link ConvergencePlotFromXLSX} class.
 * This tutorial extends {@link Tutorial2a} by displaying a transparent envelope around the data associated with
 * standard deviations. It also demonstrates how to automatically create and save the plot render as an image.
 *
 * @author MTomczyk
 */
public class Tutorial2b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Path path;
        try
        {
            path = FileUtils.getPathRelatedToClass(Tutorial2b.class, "Private", "src", File.separatorChar);
            String stringPath = path + File.separator + "Data.xlsx";
            System.out.println("Path = " + stringPath);

            DataMatrixCoordinates DMC = new DataMatrixCoordinates(0, 1, 5, 101, 1);

            // The below DataSetData constructor allows for pointing to columns associated with a standard deviation
            // (or upper and lower bound, in general)
            DataSetData[] DSD = new DataSetData[]
                    {
                            DataSetData.getForDataWithStandardDeviation("A1", 0, 1, 2),
                            DataSetData.getForDataWithStandardDeviation("A2", 0, 3, 4),
                    };

            ArrayList<IDataSet> dataSets = ConvergencePlotFromXLSX.parseDataSetsFromXLSX(stringPath, DMC, DSD, 0.5f);

            Plot2D.Params pP = ConvergencePlotFromXLSX.getParamsContainerForConvergencePlotFromXLSX(
                    new ConvergencePlotFromXLSX.Params(
                            "X", "Y", new Range(0, 100), new Range(0, 100),
                            0.5f, 1.55f, 1.3f, 1.5f,
                            1.2f, 1.5f, 2.0f,
                            2.25f, 2.0f, 3.0f));

            Frame frame = ConvergencePlotFromXLSX.getFrame(pP, dataSets, 1500, 3.0f / 2.0f);
            frame.setVisible(true);
            AbstractPlot plot = frame.getModel().getPlotsWrapper().getModel().getPlot(0);

            // The lines below demonstrate how to automatically create a screenshot of the plot.
            // The result will be stored in Screenshot._image. Note that the ``requestScreenshotCreation'' method creates
            // and returns a count-down latch of size 1. A thread creating a render will call its countDown() upon
            // screenshot creation, thus allowing for thread synchronization with this thread (screenshot._barrier.await());
            // If no awaiting is called, the screenshot may not be generated entirely before proceeding.
            // Important note: the method will temporarily disable plot
            // visibility and resize it. Thus, some flickering may be observable. After the screenshot is created,
            // the plot's original state will be restored. The method will still work if the frame is not visible
            // (frame.setVisible(false)).
            Screenshot screenshot = plot.getModel().requestScreenshotCreation(2000, 2000);

            // Synchronization
            screenshot._barrier.await();

            // Save the screenshot int the class location:
            String screenshotPath = path + File.separator + "screenshot";
            ImageSaver.saveImage(screenshot._image, screenshotPath, "jpg", 1.0f);

        } catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }

    }
}
