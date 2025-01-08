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
import space.Range;

/**
 * This tutorial showcases how to draw arrows (2D case).
 *
 * @author MTomczyk
 */
public class Tutorial2b
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
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[3];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d));
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d));
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true);
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2};

        pP._scheme = new WhiteScheme();
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
        Plot2D plot2D = new Plot2D(pP);

        int divAngle = 36;
        int divRad = 4;
        double dr = 1.0d / divRad;
        int points = divAngle * divRad * 2;
        double[][] data = new double[points][3];
        int idx = 0;
        double l = 0.5d;
        for (int i = 0; i < divAngle; i++)
        {
            double a = 2.0d * Math.PI * (double) i / divAngle;
            double cos = Math.cos(a);
            double sin = Math.sin(a);

            for (int j = 0; j < divRad; j++)
            {
                double r1 = 1.0d - j * dr;
                double r2 = r1 - dr * l;
                data[idx][0] = cos * r1;
                data[idx][1] = sin * r1;
                data[idx][2] = Math.sqrt(data[idx][0] * data[idx][0] + data[idx][1] * data[idx][1]);
                data[idx + 1][0] = cos * r2;
                data[idx + 1][1] = sin * r2;
                data[idx + 1][2] = Math.sqrt(data[idx + 1][0] * data[idx + 1][0] + data[idx + 1][1] * data[idx + 1][1]);
                idx += 2;
            }
        }

        LineStyle ls = new LineStyle(0.25f, Color.BLACK);
        ArrowStyle bas = new ArrowStyle(2.0f, 2.0f, Gradient.getRedBlueGradient(), 2, Arrow.TRIANGULAR_2D);
        ArrowStyle eas = new ArrowStyle(1.0f, 3.0f, Color.BLACK, Arrow.TRIANGULAR_2D);
        IDataSet ds = DSFactory2D.getDS("DS", data, null, ls, new ArrowStyles(bas, eas), true, 0.005f);

        //Frame frame = new Frame(plot2D, 0.5f);
        Frame frame = new Frame(plot2D, 800, 800);
        plot2D.getModel().setDataSet(ds, true);
        frame.setVisible(true);
    }
}
