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
import space.Dimension;
import space.Range;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial showcases how to draw arrows (3D case).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2c
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
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.get0R(20.0d),
                new Range(1.0d, 3.0d), new Range(-1.0d, 1.0d));
        pP._scheme = WhiteScheme.getForPlot3D();
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
        pP._zProjectionBound = new Dimension(-0.2f, 0.4f);
        Plot3D plot3D = new Plot3D(pP);

        ArrayList<IDataSet> dataSets = new ArrayList<>(10);

        ArrowStyles[] as = new ArrowStyles[7];
        as[0] = new ArrowStyles(new ArrowStyle(0.02f, 0.02f, Color.RED, Arrow.TRIANGULAR_3D));
        as[1] = new ArrowStyles(new ArrowStyle(0.03f, 0.02f, Color.RED, Arrow.TRIANGULAR_REVERSED_3D));
        as[2] = new ArrowStyles(new ArrowStyle(0.02f, 0.02f, Color.BLUE, Arrow.TRIANGULAR_3D),
                new ArrowStyle(0.03f, 0.02f, Color.RED, Arrow.TRIANGULAR_3D));
        as[3] = new ArrowStyles(new ArrowStyle(0.03f, 0.02f, Color.BLUE, Arrow.TRIANGULAR_3D),
                new ArrowStyle(0.03f, 0.02f, Color.RED, Arrow.TRIANGULAR_REVERSED_3D));
        as[4] = new ArrowStyles(new ArrowStyle(0.03f, 0.02f, Color.BLUE, Arrow.TRIANGULAR_REVERSED_3D),
                new ArrowStyle(0.03f, 0.02f, Color.RED, Arrow.TRIANGULAR_3D));
        as[5] = new ArrowStyles(new ArrowStyle(0.03f, 0.02f, Color.BLUE, Arrow.TRIANGULAR_REVERSED_3D),
                new ArrowStyle(0.03f, 0.02f, Color.RED, Arrow.TRIANGULAR_REVERSED_3D));
        as[6] = new ArrowStyles(new ArrowStyle(0.03f, 0.02f, Gradient.getViridisGradient(), 0, Arrow.TRIANGULAR_3D),
                new ArrowStyle(0.03f, 0.02f, Gradient.getRedBlueGradient(), 1, Arrow.TRIANGULAR_3D));

        for (int i = 0; i < as.length; i++)
        {
            LinkedList<double[][]> data = new LinkedList<>();
            data.add(new double[][]{{2 * i + 1.0d, 1.0d, 0.0d}}); // will be ignored
            data.add(null); // break
            data.add(new double[][]{{2 * i + 1.0d, 1.0d, 0.0d}});
            data.add(new double[][]{
                    {2 * i + 1.0d, 2.0d, 0.0d},
                    null,
                    {2 * i + 2.0d, 2.0d, 0.0d},
                    {2 * i + 2.0d, 3.0d, 0.0d}}
            );

            LineStyle ls;

            if (i == 0) ls = new LineStyle(1.0f, Color.BLACK, 0.01f, Line.REGULAR); // legend size needs to be surpassed
            else if (i < 6)
                ls = new LineStyle(0.01f, Color.BLACK, Line.POLY_OCTO); // arrows are not drawn without lines
            else ls = new LineStyle(0.01f, Gradient.getPlasmaGradient(), 1, Line.POLY_OCTO);

            dataSets.add(DSFactory3D.getDS("DS" + (i + 1), data, ls, as[i], false, false));
        }

        Frame frame = new Frame(plot3D, 0.5f);
        frame.setVisible(true);
        plot3D.getModel().setDataSets(dataSets, true);

        plot3D.getModel().updatePlotIDSsAndRenderOnDemand(); // need to be called
    }
}
