package y2025.ERS.e4_interactive.results;

import dataset.IDataSet;
import frame.Frame;
import io.FileUtils;
import io.image.ImageSaver;
import plot.Plot2D;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;
import tools.ConvergencePlotFromXLSX;
import tools.DataMatrixCoordinates;
import tools.DataSetData;
import utils.Screenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Generated convergence plots.
 *
 * @author MTomczyk
 */
public class IllustrateConvergence
{
    /**
     * Runs the visualization
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Path path;

        int scenario = 0; // scenario no
        int[] sheet = new int[]{0, 0}; // sheet IDX
        int[] xIndexFRS = new int[]{0, 0}; // x-axis-related column no. (FRS)
        int[] yIndexFRS = new int[]{1, 3}; // y-axis-related column no. (FRS)
        int[] stdIndexFRS = new int[]{2, -1}; // std-related column no. (FRS)
        int[] xIndexERS = new int[]{0, 0}; // x-axis-related column no. (ERS)
        int[] yIndexERS = new int[]{4, 6}; // y-axis-related column no. (ERS)
        int[] stdIndexERS = new int[]{5, -1}; // std-related column no. (ERS)
        Align [] aligns = new Align[]{Align.RIGHT_TOP, Align.RIGHT};
        boolean[] useStd = new boolean[]{true, false};

        String[] saveFilename = new String[]{
                "HV_MEAN_DTLZ2_3D",
                "HV_WINS_DTLZ2_3D",
        };

        String[] yAxisDecimalFormat = new String[]{"0.00", "0"};

        Range[] yRanges = new Range[] // y-axis-related ranges
                {
                        new Range(0.17d, 0.7d),
                        new Range(0.0d, 100.0d),
                };

        Range[] xRanges = new Range[] // x-axis-related ranges
                {
                        new Range(0.0d, 1299.0d),
                        new Range(0.0d, 1299.0d),
                };

        String[] yLabels = new String[] // y-axis-related labels
                {
                        "HV (mean and std)",
                        "HV (pairwise wins)"

                };

        String[] xAxisLabel = new String[]{ // x-axis-related labels
                "Generation", "Generation"
        };

        float[] yAxisTitleOffsets = new float[]{ // for plot customization
                1.8f, 1.8f,
        };

        float widthHeightRatio = 1.75f; // for plot customization

        try
        {
            path = FileUtils.getPathRelatedToClass(IllustrateConvergence.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "e4_interactive_convergence_short.xlsx";
            DataMatrixCoordinates DMC = new DataMatrixCoordinates(0, 2, 7, 1299, sheet[scenario]);

            DataSetData[] DSD = new DataSetData[2];

            if (useStd[scenario])
            {
                DSD[0] = DataSetData.getForDataWithStandardDeviation("FRS", xIndexFRS[scenario], yIndexFRS[scenario], stdIndexFRS[scenario]);
                DSD[1] = DataSetData.getForDataWithStandardDeviation("ERS", xIndexERS[scenario], yIndexERS[scenario], stdIndexERS[scenario]);
            }
            else
            {
                DSD[0] = DataSetData.getData("FRS", xIndexFRS[scenario], yIndexFRS[scenario]);
                DSD[1] = DataSetData.getData("ERS", xIndexERS[scenario], yIndexERS[scenario]);
            }

            ArrayList<IDataSet> dataSets = ConvergencePlotFromXLSX.parseDataSetsFromXLSX(xlsxPath, DMC, DSD, 0.5f);

            Plot2D.Params pP = ConvergencePlotFromXLSX.getParamsContainerForConvergencePlotFromXLSX(
                    new ConvergencePlotFromXLSX.Params(
                            xAxisLabel[scenario],
                            yLabels[scenario],
                            xRanges[scenario],
                            yRanges[scenario],
                            0.5f, 1.75f, 1.7f, 1.7f,
                            1.65f, yAxisTitleOffsets[scenario],
                            2.5f, 2.75f, 2.5f, 3.0f));
            pP._scheme._sizes.put(SizeFields.LEGEND_INNER_OFFSET_RELATIVE_MULTIPLIER, 0.015f);
            pP._scheme._aligns.put(AlignFields.LEGEND, aligns[scenario]);

            int width = 1000;
            int height = (int) (1000 / widthHeightRatio);

            Frame frame = ConvergencePlotFromXLSX.getFrame(pP, dataSets, 1000, height,
                    "0", yAxisDecimalFormat[scenario]);

            frame.setVisible(true);

            Screenshot screenshot = frame.getModel().getPlotsWrapper().getModel().getPlot(0).getModel().requestScreenshotCreation(width, height);
            screenshot._barrier.await();
            String outputFile = path + File.separator + saveFilename[scenario];
            ImageSaver.saveImage(screenshot._image, outputFile, "jpg", 1.0f);


        } catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
