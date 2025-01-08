package t1_10.t7_drawing_arrows.t2_using_arrows;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.enums.Arrow;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.NumberFields;
import space.Range;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial showcases how to draw arrows (2D case).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._drawLegend = true;
        pP._title = "Drawing arrows";
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.get0R(20.0d), Range.get0R(4.0d));
        pP._scheme = new WhiteScheme();
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.CENTER_TOP);
        // The limit for the number of entries in one column can be set in the following way:
        pP._scheme._numbers.put(NumberFields.LEGEND_NO_ENTRIES_PER_COLUMN_LIMIT, 4);
        Plot2D plot2D = new Plot2D(pP);

        ArrayList<IDataSet> dataSets = new ArrayList<>(10);

        ArrowStyles[] as = new ArrowStyles[7];
        as[0] = new ArrowStyles(new ArrowStyle(4.0f, 2.0f, Color.RED, Arrow.TRIANGULAR_2D));
        as[1] = new ArrowStyles(new ArrowStyle(4.0f, 4.0f, Color.RED, Arrow.TRIANGULAR_REVERSED_2D));
        as[2] = new ArrowStyles(new ArrowStyle(4.0f, 2.0f, Color.BLUE, Arrow.TRIANGULAR_2D),
                new ArrowStyle(4.0f, 2.0f, Color.RED, Arrow.TRIANGULAR_2D));
        as[3] = new ArrowStyles(new ArrowStyle(4.0f, 2.0f, Color.BLUE, Arrow.TRIANGULAR_2D),
                new ArrowStyle(4.0f, 2.0f, Color.RED, Arrow.TRIANGULAR_REVERSED_2D));
        as[4] = new ArrowStyles(new ArrowStyle(4.0f, 2.0f, Color.BLUE, Arrow.TRIANGULAR_REVERSED_2D),
                new ArrowStyle(4.0f, 2.0f, Color.RED, Arrow.TRIANGULAR_REVERSED_2D));
        as[5] = new ArrowStyles(new ArrowStyle(4.0f, 2.0f, Color.BLUE, Arrow.TRIANGULAR_REVERSED_2D),
                new ArrowStyle(4.0f, 2.0f, Color.RED, Arrow.TRIANGULAR_REVERSED_2D));
        as[6] = new ArrowStyles(new ArrowStyle(4.0f, 2.0f, Gradient.getViridisGradient(), 0, Arrow.TRIANGULAR_2D),
                new ArrowStyle(4.0f, 2.0f, Gradient.getRedBlueGradient(), 1, Arrow.TRIANGULAR_2D));

        for (int i = 0; i < as.length; i++)
        {
            LinkedList<double[][]> data = new LinkedList<>();
            data.add(new double[][]{{2 * i + 1.0d, 1.0d}}); // will be ignored
            data.add(null); // break
            data.add(new double[][]{{2 * i + 1.0d, 1.0d}});
            data.add(new double[][]{
                    {2 * i + 1.0d, 2.0d},
                    null,
                    {2 * i + 2.0d, 2.0d},
                    {2 * i + 2.0d, 3.0d}}
            );

            LineStyle ls;
            if (i < 6) ls = new LineStyle(0.5f, Color.BLACK); // arrows are not drawn without lines
            else ls = new LineStyle(0.5f, Gradient.getPlasmaGradient(), 1);

            dataSets.add(DSFactory2D.getDS("DS" + (i + 1), data, ls, as[i], false));
        }

        Frame frame = new Frame(plot2D, 0.5f);
        plot2D.getModel().setDataSets(dataSets, true);
        frame.setVisible(true);
    }
}
