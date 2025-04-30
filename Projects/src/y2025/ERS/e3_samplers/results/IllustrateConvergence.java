package y2025.ERS.e3_samplers.results;

import dataset.IDataSet;
import frame.Frame;
import io.FileUtils;
import io.image.ImageSaver;
import plot.Plot2D;
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

        int scenario = 15; // scenario no
        int[] sheet = new int[]{0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3}; // sheet IDX
        int[] xIndexFRS = new int[]{0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1}; // x-axis-related column no. (FRS)
        int[] yIndexFRS = new int[]{3, 3, 5, 7, 3, 3, 5, 7, 3, 3, 5, 7, 3, 3, 5, 7}; // y-axis-related column no. (FRS)
        int[] stdIndexFRS = new int[]{4, 4, 6, 8, 4, 4, 6, 8, 4, 4, 6, 8, 4, 4, 6, 8}; // std-related column no. (FRS)
        int[] xIndexERS = new int[]{0, 9, 9, 9, 0, 9, 9, 9, 0, 9, 9, 9, 0, 9, 9, 9}; // x-axis-related column no. (ERS)
        int[] yIndexERS = new int[]{11, 11, 13, 15, 11, 11, 13, 15, 11, 11, 13, 15, 11, 11, 13, 15}; // y-axis-related column no. (ERS)
        int[] stdIndexERS = new int[]{12, 12, 14, 16, 12, 12, 14, 16, 12, 12, 14, 16, 12, 12, 14, 16}; // std-related column no. (ERS)

        String[] saveFilename = new String[]{
                "NCM_h_5_alpha_1_iter",
                "NCM_h_5_alpha_1_time",
                "MINCN_h_5_alpha_1_time",
                "STDCN_h_5_alpha_1_time",

                "NCM_h_10_alpha_1_iter",
                "NCM_h_10_alpha_1_time",
                "MINCN_h_10_alpha_1_time",
                "STDCN_h_10_alpha_1_time",

                "NCM_h_5_alpha_infty_iter",
                "NCM_h_5_alpha_infty_time",
                "MINCN_h_5_alpha_infty_time",
                "STDCN_h_5_alpha_infty_time",

                "NCM_h_10_alpha_infty_iter",
                "NCM_h_10_alpha_infty_time",
                "MINCN_h_10_alpha_infty_time",
                "STDCN_h_10_alpha_infty_time"

        };

        String [] yAxisDecimalFormat = new String[]{"0", "0", "0.00", "0.000", "0", "0", "0.00", "0.000",
                "0", "0", "0.00", "0.000", "0", "0", "0.00", "0.000"};

        Range[] yRanges = new Range[] // y-axis-related ranges
                {
                        new Range(0.0d, 200.0d),
                        new Range(0.0d, 200.0d),
                        new Range(0.0d, 0.1d),
                        new Range(0.0d, 0.025d),

                        new Range(0.0d, 200.0d),
                        new Range(0.0d, 200.0d),
                        new Range(0.0d, 0.05d),
                        new Range(0.0d, 0.025d),

                        new Range(0.0d, 200.0d),
                        new Range(0.0d, 200.0d),
                        new Range(0.0d, 0.1d),
                        new Range(0.0d, 0.03d),

                        new Range(0.0d, 200.0d),
                        new Range(0.0d, 200.0d),
                        new Range(0.0d, 0.06d),
                        new Range(0.0d, 0.02d),
                };

        Range[] xRanges = new Range[] // x-axis-related ranges
                {
                        new Range(0.0d, 5000.0),//0
                        new Range(0.0d, 300.0),//1
                        new Range(0.0d, 600.0),//2
                        new Range(0.0d, 600.0),//3

                        new Range(0.0d, 10000.0),//4
                        new Range(0.0d, 500.0),//5
                        new Range(0.0d, 2000.0),//6
                        new Range(0.0d, 2000.0),//7

                        new Range(0.0d, 1250.0),//8
                        new Range(0.0d, 100.0),//9
                        new Range(0.0d, 400.0),//10
                        new Range(0.0d, 400.0),//11

                        new Range(0.0d, 1250.0),//12
                        new Range(0.0d, 500.0),//13
                        new Range(0.0d, 1000.0),//14
                        new Range(0.0d, 1000.0),//15

                };

        String[] yLabels = new String[] // y-axis-related labels
                {
                        "NCM", "NCM", "MIN-CN", "STD-CN",
                        "NCM", "NCM", "MIN-CN", "STD-CN",

                        "NCM", "NCM", "MIN-CN", "STD-CN",
                        "NCM", "NCM", "MIN-CN", "STD-CN",
                };

        String[] xAxisLabel = new String[]{ // x-axis-related labels
                "Iteration (x 100)",
                "Time [ms] (EXEC-TIME)",
                "Time [ms] (EXEC-TIME)",
                "Time [ms] (EXEC-TIME)",

                "Iteration (x 100)",
                "Time [ms] (EXEC-TIME)",
                "Time [ms] (EXEC-TIME)",
                "Time [ms] (EXEC-TIME)",

                "Iteration (x 100)",
                "Time [ms] (EXEC-TIME)",
                "Time [ms] (EXEC-TIME)",
                "Time [ms] (EXEC-TIME)",

                "Iteration (x 100)",
                "Time [ms] (EXEC-TIME)",
                "Time [ms] (EXEC-TIME)",
                "Time [ms] (EXEC-TIME)",
        };

        float[] yAxisTitleOffsets = new float[]{ // for plot customization
                1.8f,
                1.8f,
                1.8f,
                1.8f,

                1.4f,
                1.4f,
                1.8f,
                1.8f,

                1.4f,
                1.4f,
                1.8f,
                1.8f,

                1.4f,
                1.4f,
                1.8f,
                1.8f,
        };

        float widthHeightRatio = 1.75f; // for plot customization

        try
        {
            path = FileUtils.getPathRelatedToClass(IllustrateConvergence.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "e3_samplers_convergence.xlsx";
            DataMatrixCoordinates DMC = new DataMatrixCoordinates(0, 3, 17, 10000, sheet[scenario]);

            DataSetData[] DSD = new DataSetData[]
                    {
                            DataSetData.getForDataWithStandardDeviation("FRS", xIndexFRS[scenario], yIndexFRS[scenario], stdIndexFRS[scenario]),
                            DataSetData.getForDataWithStandardDeviation("ERS", xIndexERS[scenario], yIndexERS[scenario], stdIndexERS[scenario]),
                    };

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
