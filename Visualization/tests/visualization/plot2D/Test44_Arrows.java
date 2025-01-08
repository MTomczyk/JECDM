package visualization.plot2D;

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
import space.Range;

/**
 * Test drawing arrows.
 *
 * @author MTomczyk
 */
public class Test44_Arrows
{
    /**
     * Runs the test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        pP._scheme = new WhiteScheme();
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(-1.0d, 1.0d),
                new Range(-1.0d, 1.0d));
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        Plot2D plot2D = new Plot2D(pP);
        Frame frame = new Frame(plot2D, 0.5f, 0.5f);

        int divAngle = 36;
        int divRad = 4;
        double dr = 1.0d / divRad;
        int points = divAngle * divRad * 2;
        double[][] data = new double[points][2];
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
                data[idx + 1][0] = cos * r2;
                data[idx + 1][1] = sin * r2;
                idx += 2;
            }
        }

        LineStyle ls = new LineStyle(0.5f, Gradient.getPlasmaGradient(), 0);
        //ArrowStyle eas = new ArrowStyle(15.0f, 15.0f, Color.BLACK);
        ArrowStyle bas = new ArrowStyle(3.0f, 3.0f, Gradient.getRedBlueGradient(), 1, null, null, Arrow.TRIANGULAR_2D);
        ArrowStyle eas = new ArrowStyle(2.0f, 5.0f, Gradient.getPlasmaGradient(), 0, null, null, Arrow.TRIANGULAR_2D);
        IDataSet ds = DSFactory2D.getDS("DS", data, null, ls, new ArrowStyles(bas, eas), true, 0.005f);
        frame.setVisible(true);
        plot2D.getModel().setDataSet(ds, true);

    }
}
