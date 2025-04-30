package y2025.ERS.e1_auxiliary;

import alternative.Alternative;
import color.Color;
import color.gradient.Gradient;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.enums.Arrow;
import drmanager.DRMPFactory;
import frame.Frame;
import io.FileUtils;
import io.image.ImageSaver;
import model.IPreferenceModel;
import model.internals.value.scalarizing.LNorm;
import plot.Plot2D;
import plot.Plot2DFactory;
import print.PrintUtils;
import y2025.ERS.common.PCsDataContainer;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import space.Range;
import utils.Screenshot;
import visualization.utils.ReferenceParetoFront;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Auxiliary script that generates example plots portraying the reference pairs to be compared by the DM, using the data in the pcs.txt file.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class PCsPlot2D
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Path path;
        try
        {
            path = FileUtils.getPathRelatedToClass(GeneratePCsData.class, "Projects", "src", File.separatorChar);
            String fp = path.toString() + File.separatorChar + "pcs.txt";
            PCsDataContainer PCs = new PCsDataContainer(fp, 4, 3, 100, 10);

            int selectedM = 0;

            // 0, 15
            // 1 6
            // 2 1

            int selectedAlpha = 2;
            int selectedTrial = 1;

            double[] w = PCs._PCs[selectedAlpha][selectedM]._trialPCs[selectedTrial]._dmW;
            double alpha = PCs._PCs[selectedAlpha][selectedM]._trialPCs[selectedTrial]._dmA;
            PrintUtils.printVectorOfDoubles(w, 2);
            System.out.println(alpha);
            IPreferenceModel<LNorm> model = new model.definitions.LNorm(new LNorm(w, alpha));

            double[][][] rA = PCs._PCs[selectedAlpha][selectedM]._trialPCs[selectedTrial]._referenceEvaluations;

            double[][] dataPairs = new double[10 * 2][];
            int index = 0;
            for (int pc = 0; pc < 10; pc++)
            {
                double e1 = model.evaluate(new Alternative("A", rA[pc][0]));
                double e2 = model.evaluate(new Alternative("A", rA[pc][1]));

                if (Double.compare(e1, e2) < 0)
                {
                    dataPairs[index] = new double[3];
                    System.arraycopy(rA[pc][1], 0, dataPairs[index], 0, 2);
                    dataPairs[index][2] = pc + 1;
                    index++;
                    dataPairs[index] = new double[3];
                    System.arraycopy(rA[pc][0], 0, dataPairs[index], 0, 2);
                    dataPairs[index][2] = pc + 1;
                    index++;
                }
                else
                {
                    dataPairs[index] = new double[3];
                    System.arraycopy(rA[pc][0], 0, dataPairs[index], 0, 2);
                    dataPairs[index][2] = pc + 1;
                    index++;
                    dataPairs[index] = new double[3];
                    System.arraycopy(rA[pc][1], 0, dataPairs[index], 0, 2);
                    dataPairs[index][2] = pc + 1;
                    index++;
                }
            }

            Plot2D plot2D = Plot2DFactory.getPlot("f1", "f2",
                    DRMPFactory.getFor3D(Range.get0R(1.5d), Range.get0R(1.5d),
                            new Range(1.0d, 10.0d)), 4, 4, 2.2f, scheme ->
                    {
                        scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE);
                        scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
                        scheme._sizes.put(SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER, 0.05f);
                        scheme._sizes.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.12f);
                        scheme._sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.15f);
                        scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.04f);
                        scheme._sizes.put(SizeFields.AXIS_Y_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.1f);
                        scheme._sizes.put(SizeFields.AXIS_X_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.07f);
                    },
                    pP -> pP._drawLegend = false);

            int plotSize = 1000;
            Frame frame = new Frame(plot2D, plotSize, plotSize);
            frame.setVisible(true);

            Color c1 = color.palette.ColorPalettes.getFromDefaultPalette(3);
            Color c2 = color.palette.ColorPalettes.getFromDefaultPalette(0);


            assert c1 != null;
            assert c2 != null;
            Gradient gradient = Gradient.getTwoColorBasedGradient(c1._r, c1._g, c1._b,
                    c2._r, c2._g, c2._b,
                    "RB", 10, false);

            ArrayList<IDataSet> dataSets = new ArrayList<>();
            dataSets.add(ReferenceParetoFront.getConvexSpherical2DPF(1.0d,
                    new LineStyle(0.5f, color.gradient.Color.BLACK, 1.0f)));
            dataSets.add(DSFactory2D.getDS("Pairwise comparisons", dataPairs, null,
                    new LineStyle(1.0f, gradient, 2, 1.0f),
                    new ArrowStyles(new ArrowStyle(7.0f, 5.0f, gradient, 2,
                            2.0f, 2.0f, Arrow.TRIANGULAR_2D)),
                    true, 0.1f));


            plot2D.getModel().setDataSets(dataSets);

            // Create screenshot
            Screenshot screenshot = plot2D.getModel().requestScreenshotCreation(plotSize * 2, plotSize * 2,
                    false, new color.Color(255, 255, 255));
            screenshot._barrier.await();
            path = FileUtils.getPathRelatedToClass(PCsPlot2D.class, "Projects", "src", File.separatorChar);
            fp = path.toString() + File.separatorChar + "pcs_2d_" + selectedAlpha + "_" + selectedTrial;
            ImageSaver.saveImage(screenshot._image, fp, "jpg", 1.0f);

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }
}
