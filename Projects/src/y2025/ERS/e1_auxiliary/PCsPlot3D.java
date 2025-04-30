package y2025.ERS.e1_auxiliary;

import alternative.Alternative;
import color.Color;
import color.gradient.Gradient;
import dataset.DSFactory3D;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Arrow;
import dataset.painter.style.enums.Line;
import dataset.painter.style.enums.Marker;
import drmanager.DRMPFactory;
import frame.Frame;
import io.FileUtils;
import io.image.ImageSaver;
import model.IPreferenceModel;
import model.internals.value.scalarizing.LNorm;
import plot.Plot3D;
import plot.Plot3DFactory;
import print.PrintUtils;
import y2025.ERS.common.PCsDataContainer;
import scheme.WhiteScheme;
import scheme.enums.Align;
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
public class PCsPlot3D
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

            int selectedM = 1;

            // 0 15
            // 1 4
            // 2 3

            int selectedAlpha = 2;
            int selectedTrial = 3;

            double[] w = PCs._PCs[selectedAlpha][selectedM]._trialPCs[selectedTrial]._dmW;
            double alpha = PCs._PCs[selectedAlpha][selectedM]._trialPCs[selectedTrial]._dmA;
            PrintUtils.printVectorOfDoubles(w, 2);
            System.out.println(alpha);
            IPreferenceModel<LNorm> model = new model.definitions.LNorm(new LNorm(w, alpha));

            double[][][] rA = PCs._PCs[selectedAlpha][selectedM]._trialPCs[selectedTrial]._referenceEvaluations;

            double[][] dataPairs = new double[10 * 2][4];
            int index = 0;
            for (int pc = 0; pc < 10; pc++)
            {
                double e1 = model.evaluate(new Alternative("A", rA[pc][0]));
                double e2 = model.evaluate(new Alternative("A", rA[pc][1]));
                if (Double.compare(e1, e2) < 0)
                {
                    dataPairs[index] = new double[4];
                    System.arraycopy(rA[pc][1], 0, dataPairs[index], 0, 3);
                    dataPairs[index][3] = pc + 1;
                    index++;
                    dataPairs[index] = new double[4];
                    System.arraycopy(rA[pc][0], 0, dataPairs[index], 0, 3);
                    dataPairs[index][3] = pc + 1;
                    index++;
                }
                else
                {
                    dataPairs[index] = new double[4];
                    System.arraycopy(rA[pc][0], 0, dataPairs[index], 0, 3);
                    dataPairs[index][3] = pc + 1;
                    index++;
                    dataPairs[index] = new double[4];
                    System.arraycopy(rA[pc][1], 0, dataPairs[index], 0, 3);
                    dataPairs[index][3] = pc + 1;
                    index++;
                }
            }

            Plot3D plot3D = Plot3DFactory.getPlot(WhiteScheme.getForPlot3D(),
                    "f1", "f2", "f3",
                    DRMPFactory.getFor4D(Range.get0R(1.5d), Range.get0R(1.5d),
                            Range.get0R(2.0d), new Range(1.0d, 10.0d)),
                    4, 4, 4,
                    "0.00", "0.00", "0.00",
                    2.2f, 2.0f, scheme ->
                    {
                        scheme._colors.put(ColorFields.PLOT_BACKGROUND, color.Color.WHITE);
                        scheme._sizes.put(SizeFields.TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.035f);
                        scheme._sizes.put(SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER, 0.031f);
                    },
                    pP -> {
                        pP._drawLegend = false;
                        pP._axesAlignments = new Align[]{Align.FRONT_BOTTOM, Align.LEFT_BOTTOM, Align.BACK_LEFT};
                    },
                    null);

            int plotSize = 1000;
            Frame frame = new Frame(plot3D, plotSize, plotSize);
            frame.setVisible(true);


            Color c1 = color.palette.ColorPalettes.getFromDefaultPalette(3);
            Color c2 = color.palette.ColorPalettes.getFromDefaultPalette(0);

            assert c1 != null;
            assert c2 != null;
            Gradient gradient = Gradient.getTwoColorBasedGradient(c1._r, c1._g, c1._b,
                    c2._r, c2._g, c2._b,
                    "RB", 10, false);

            ArrayList<IDataSet> dataSets = new ArrayList<>();
            dataSets.add(ReferenceParetoFront.getConvexSpherical3DPF(1.0f, 30, new MarkerStyle(0.0075f,
                    color.gradient.Color.GRAY_50, Marker.SPHERE_HIGH_POLY_3D, 2.0f)));
            dataSets.add(DSFactory3D.getDS("Pairwise comparisons", dataPairs,
                    new LineStyle(0.01f, gradient, 3, 1.0f, Line.POLY_OCTO),
                    new ArrowStyles(new ArrowStyle(0.04f, 0.01f,
                            gradient, 3, 2.0f, 2.0f, Arrow.TRIANGULAR_3D)),
                    false, true));

            plot3D.getModel().setDataSets(dataSets);

            // Set projection:
            plot3D.getController().getInteractListener().getObjectRotation()[0] = 356.81583f;
            plot3D.getController().getInteractListener().getObjectRotation()[1] = 41.52439f;
            plot3D.getController().getInteractListener().getTranslation()[0] = 0.0f;
            plot3D.getController().getInteractListener().getTranslation()[1] = 0.0f;
            plot3D.getController().getInteractListener().getTranslation()[2] = 1.87916f;

            plot3D.getModel().notifyDisplayRangesChangedListeners();
            plot3D.repaint();

            Thread.sleep(1000);

            // Create screenshot
            Screenshot screenshot = plot3D.getModel().requestScreenshotCreation(plotSize * 2, plotSize * 2,
                    false, new Color(255, 255, 255));
            screenshot._barrier.await();
            path = FileUtils.getPathRelatedToClass(PCsPlot3D.class, "Projects", "src", File.separatorChar);
            fp = path.toString() + File.separatorChar + "pcs_3d_" + selectedAlpha + "_" + selectedTrial;
            ImageSaver.saveImage(screenshot._image, fp, "jpg", 1.0f);

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
