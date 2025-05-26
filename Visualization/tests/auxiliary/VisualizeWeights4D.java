package auxiliary;

import color.Color;
import color.gradient.Gradient;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DRMPFactory;
import frame.Frame;
import plot.PCP2DFactory;
import plot.Plot2D;
import random.*;
import scheme.enums.ColorFields;
import space.Range;

/**
 * Simple runnable displaying randomly generated 4D normalized weight vectors (drawn randomly from a uniform distribution).
 *
 * @author MTomczyk
 */
public class VisualizeWeights4D
{
    /**
     * Runs the test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        int n = 10000;
        double[][] W = new double[n][];
        for (int i = 0; i < n; i++) W[i] = WeightsGenerator.getNormalizedWeightVector(4, R);

        Plot2D plot2D = PCP2DFactory.getPlot("dimension", new String[]{"w1", "w2", "w3", "w3"},
                DRMPFactory.getForParallelCoordinatePlot2D(4, Range.getNormalRange(), false),
                5, "0.00", 1.5f, scheme -> scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE),
                 null, null);

        int plotHeight = 1000;
        int plotWidth = 1100;

        Frame frame = new Frame(plot2D, plotWidth, plotHeight);

        IDataSet ds = DSFactory2D.getDSForParallelCoordinatePlot("Weight vectors", 4, W, null,
                new LineStyle(0.5f, Gradient.getPlasmaGradient(), 0));
        plot2D.getModel().setDataSet(ds);

        frame.setVisible(true);
    }
}
