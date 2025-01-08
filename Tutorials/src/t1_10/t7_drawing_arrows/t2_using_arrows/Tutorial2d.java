package t1_10.t7_drawing_arrows.t2_using_arrows;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DSFactory3D;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.enums.Arrow;
import dataset.painter.style.enums.Line;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;

/**
 * This tutorial showcases how to draw arrows (3D case).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2d
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";
        pP._drawLegend = true;
        pP._title = "Drawing arrows";
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d));
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d));
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d));
        pP._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, true);
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};

        pP._scheme = WhiteScheme.getForPlot3D();
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
        Plot3D plot3D = new Plot3D(pP);

        int disc = 10;
        double[][] data = new double[disc * disc * disc * 2][4];
        double dc = 1.0d / (disc - 1);
        float mul = 0.1f;

        int idx = 0;
        for (int i = 0; i < disc; i++)
            for (int j = 0; j < disc; j++)
                for (int k = 0; k < disc; k++)
                {
                    data[idx][0] = -1.0d + 2.0d * i * dc;
                    data[idx][1] = -1.0d + 2.0d * j * dc;
                    data[idx][2] = -1.0d + 2.0d * k * dc;
                    data[idx][3] = Math.sqrt(data[idx][0] * data[idx][0] + data[idx][1] * data[idx][1] + data[idx][2] * data[idx][2]);

                    data[idx + 1][0] = data[idx][0] - data[idx][0] * mul;
                    data[idx + 1][1] = data[idx][1] - data[idx][1] * mul;
                    data[idx + 1][2] = data[idx][2] - data[idx][2] * mul;
                    data[idx + 1][3] = Math.sqrt(data[idx + 1][0] * data[idx + 1][0] + data[idx + 1][1] * data[idx + 1][1] +
                            data[idx + 1][2] * data[idx + 1][2]);
                    idx += 2;
                }

        IDataSet dataSet = DSFactory3D.getDS("DS", data,
                new LineStyle(0.005f, Color.BLACK, 0.01f, Line.POLY_OCTO),
                new ArrowStyles(new ArrowStyle(0.02f, 0.01f, Gradient.getViridisGradient(), 3, 0.06f, 0.03f, Arrow.TRIANGULAR_3D)),
                false, true);

        Frame frame = new Frame(plot3D, 1200,1000);
        frame.setVisible(true);
        plot3D.getModel().setDataSet(dataSet, true);

        plot3D.getModel().updatePlotIDSsAndRenderOnDemand(); // need to be called
    }
}
