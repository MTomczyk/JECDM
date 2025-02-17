package t1_10.t8_experimentation_tools_and_screenshots.t2_convergence_plot_from_excel;

import dataset.IDataSet;
import tools.ConvergencePlotFromXLSX;
import tools.DataMatrixCoordinates;
import tools.DataSetData;
import frame.Frame;
import io.FileUtils;
import plot.Plot2D;
import space.Range;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This tutorial showcases how to use {@link ConvergencePlotFromXLSX} class.
 * It uses the Data.xlsx file as an example input.
 *
 * @author MTomczyk
 */
public class Tutorial2a
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
            path = FileUtils.getPathRelatedToClass(Tutorial2a.class, "Private", "src", File.separatorChar);
            String stringPath = path + File.separator + "Data.xlsx";
            System.out.println("Path = " + stringPath);

            // The below line creates coordinates pointing to the data in Data.xlsx. It is assumed that the per data set
            // data are stored in columns.
            DataMatrixCoordinates DMC = new DataMatrixCoordinates(1, 2, 3, 101, 0);

            // Create two data sets (name, index of the X-axis-related column; index of the Y-axis-related column; counting from 0)
            DataSetData[] DSD = new DataSetData[]
                    {
                            new DataSetData("A1", 0, 1),
                            new DataSetData("A2", 0, 2)
                    };

            // Derive proper datasets:
            ArrayList<IDataSet> dataSets = ConvergencePlotFromXLSX.parseDataSetsFromXLSX(stringPath, DMC, DSD);

            // Derive 2D plot suitably adjusted to illustrate convergence plot (see JavaDoc for details):
            Plot2D.Params pP = ConvergencePlotFromXLSX.getParamsContainerForConvergencePlotFromXLSX(
                    new ConvergencePlotFromXLSX.Params(
                            "X", "Y", new Range(0, 100), new Range(0, 100),
                            0.5f, 1.55f, 1.3f, 1.5f,
                            1.2f, 1.5f, 2.0f,
                            2.25f, 2.0f, 3.0f));

            // Create frame and display it.
            Frame frame = ConvergencePlotFromXLSX.getFrame(pP, dataSets, 1500, 3.0f / 2.0f);
            frame.setVisible(true);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
