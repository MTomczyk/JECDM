package t11_20.t12_generating_reference_pcs;

import alternative.Alternative;
import color.Color;
import color.gradient.Gradient;
import condition.ScenarioDisablingConditions;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.enums.Arrow;
import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import exception.Exception;
import frame.Frame;
import io.FileUtils;
import plot.Heatmap2DFactory;
import plot.heatmap.Heatmap2D;
import preference.indirect.PairwiseComparison;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import tools.feedbackgenerators.*;
import visualization.utils.ReferenceParetoFront;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This tutorial is a continuation of Tutorial 1. It loads the data stored in a PCs.txt file and visualizes a selected
 * 2D scenario (DM's preferences and pairwise comparisons made).
 *
 * @author MTomczyk
 */
public class Tutorial2
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Let's attempt creating a file path to the PCs.txt file:
        Path path;
        try
        {
            path = FileUtils.getPathRelatedToClass(Tutorial1.class, "Tutorials", "src", File.separatorChar);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        String filePath = path.toString() + File.separatorChar + "PCs.txt";

        // Let's instantiate the params container. The specified data must be consistent with the one established
        // in Tutorial 1.
        PCsDataLoader.Params p = new PCsDataLoader.Params();
        p._keys = new String[]{"SHAPE", "M", "DM",};
        p._values = new String[][]
                {
                        {"CONVEX", "CONCAVE"},
                        {"2", "3", "4", "5"},
                        {"WSM", "CHEBYSHEV"},
                };

        p._trials = 100;
        p._scenarioDisablingConditions = new ScenarioDisablingConditions(new String[]{"SHAPE", "DM"},
                new String[]{"CONCAVE", "CHEBYSHEV"});
        //p._notify = false; // Turn off to stop receiving notifications.
        int interactions = 10; // Consider the number of interactions equal 10
        p._noInteractionsProvider = new Constant(interactions);
        p._extraAllowedCharacters = new Character[]{'_', '-'};
        p._filePath = filePath; // set file path
        p._modelReader = new LNormReaderParser(); // set the default L-norm parser (reader)
        p._alternativeReader = new AlternativeReaderParser(); // set the default alternatives parser (reader)

        // The loaded data will be stored in the FeedbackData object.
        FeedbackData FD;
        try
        {
            // Attempt loading the data:
            PCsDataLoader DL = new PCsDataLoader(p);
            FD = DL.process();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // Let's visualize an example 2-dimensional data:

        // First, a string representation of a concerned scenario must be established. Let's focus on SHAPE=CONVEX; M=2;
        // DM=WSM scenario (see the below string representation; other scenarios can be represented analogously).
        String scenario = "SHAPE_CONVEX_M_2_DM_WSM";
        int trialID = 3;

        // Retrieves general scenario data (provides data on all trials):
        FeedbackData.ScenarioData SD = FD._scenariosData.get(scenario);
        // Retrieves data on pairwise comparisons (associated with a specified trial ID [0,99]):
        ArrayList<PairwiseComparison> PCs = FD.getPairwiseComparisons(scenario, trialID, true);


        // The code below introduces relatively nothing new. It:
        // - establishes a heatmap with display ranges matching the ranges associated with the considered scenario
        // - the heatmap points are colored according to their relevance to the artificial DM
        // - the pairwise comparisons are illustrated as arrows with the heads pointing to preferred solutions and
        // colored using a gradient based on the interaction number (red = oldest; blue = newest)
        // - the convex shape of the PF is illustrated as a black line

        int xDiv = 100, yDiv = 100; // heatmap discretization level

        // Heatmap-dimension display range:
        DisplayRangesManager.DisplayRange hDR = new DisplayRangesManager.DisplayRange(null, true, true);

        // Instantiate heatmap:
        Heatmap2D heatmap2D = Heatmap2DFactory.getHeatmap2D("f1", "f2",
                DRMPFactory.getFor2D(SD._ranges[0], SD._ranges[1]), // display ranges based on the scenario's ranges
                5, 5,
                "0.00", "0.00",
                xDiv, yDiv,
                hDR,
                Gradient.getViridisGradient(),
                "DM's relevance score",
                1.75f,
                scheme -> {
                    scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE);
                    scheme._sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.16f);
                },
                pP -> {
                    pP._drawMainGridlines = true;
                    pP._verticalGridLinesWithBoxTicks = false;
                    pP._horizontalGridLinesWithBoxTicks = false;
                },
                plot -> plot.getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setNumberFormat(
                        new DecimalFormat("0.00")));

        int plotSize = 800;
        Frame frame = new Frame(heatmap2D, (int) ((double) plotSize * 1.15d), plotSize);

        // Instantiate heatmap data heatmap data:
        double[][] data = new double[yDiv][xDiv];
        for (int i = 0; i < yDiv; i++)
        {
            double y = ((double) i / (double) (yDiv - 1) + 0.5d / yDiv) * 2.0d;
            for (int j = 0; j < xDiv; j++)
            {
                double x = ((double) j / (double) (xDiv - 1) + 0.5d / xDiv) * 2.0d;
                // Set the value based on the relevance according to the DM's model
                data[i][j] = SD._trialData[trialID]._dmModel.evaluate(new Alternative("A", new double[]{x, y}));
            }
        }
        // Set heatmap data:
        heatmap2D.getModel().setDataAndPerformProcessing(data);

        Gradient gradient = Gradient.getRedBlueGradient();
        ArrayList<IDataSet> DSs = new ArrayList<>(PCs.size() + 1);
        // Add front (convex shape):
        DSs.add(ReferenceParetoFront.getConvexSpherical2DPF(1.0d, new LineStyle(0.5f, color.gradient.Color.BLACK)));

        // Add data sets representing pairwise comparisons:
        for (int i = 0; i < PCs.size(); i++)
        {
            float prop = 0.0f;
            if (PCs.size() > 1) prop = i / (float) (PCs.size() - 1);
            Color color = gradient.getColor(prop);

            DSs.add(DSFactory2D.getDS("PC_" + i, new double[][]{
                            PCs.get(i).getNotPreferredAlternative().getPerformanceVector(),
                            PCs.get(i).getPreferredAlternative().getPerformanceVector()},
                    new LineStyle(1.5f, new color.gradient.Color(color)),
                    new ArrowStyles(new ArrowStyle(6.0f, 4.0f,
                            new color.gradient.Color(color), Arrow.TRIANGULAR_2D)),
                    true));
        }

        // Perform the visualization:
        heatmap2D.getModel().setDataSets(DSs);
        frame.setVisible(true);
    }
}
