package visualization.plot3D;

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
import space.Range;

/**
 * Test drawing arrows.
 *
 * @author MTomczyk
 */
public class Test31_Arrows
{
    /**
     * Runs the test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._scheme = new WhiteScheme();
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor4D(new Range(-1.0d, 1.0d),
                new Range(-1.0d, 1.0d), new Range(-1.0d, 1.0d), new Range(0.0d, Math.sqrt(3.0d)));
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";
        Plot3D plot3D = new Plot3D(pP);
        Frame frame = new Frame(plot3D, 0.5f, 0.5f);

        int divAngle1 = 36;
        int divAngle2 = 36;
        int divRad = 4;
        double dr = 1.0d / divRad;
        int points = divAngle1 * divAngle2 * divRad * 2;

        double[][] data = new double[points][4];
        int idx = 0;
        double l = 0.5d;
        for (int i = 0; i < divAngle1; i++)
        {
            double a = 2.0d * Math.PI * (double) i / divAngle1;

            for (int j = 0; j < divAngle2; j++)
            {
                double b = -Math.PI + 2.0d * Math.PI * (double) j / divAngle2;
                double coscos = Math.cos(a) * Math.cos(b);
                double cossin = Math.cos(a) * Math.sin(b);
                double sin = Math.sin(a);

                for (int k = 0; k < 4; k++)
                {
                    double r1 = 1.0d - k * dr;
                    double r2 = r1 - dr * l;
                    data[idx][0] = coscos * r1;
                    data[idx][1] = cossin * r1;
                    data[idx][2] = sin * r1;
                    data[idx][3] = Math.sqrt(data[idx][0] * data[idx][0] + data[idx][1] * data[idx][1] + data[idx][2] * data[idx][2]);
                    data[idx + 1][0] = coscos * r2;
                    data[idx + 1][1] = cossin * r2;
                    data[idx + 1][2] = sin * r2;
                    data[idx + 1][3] = Math.sqrt(data[idx + 1][0] * data[idx + 1][0] + data[idx + 1][1] *
                            data[idx + 1][1] + data[idx + 1][2] * data[idx + 1][2]);
                    idx += 2;
                }
            }
        }


        LineStyle ls = new LineStyle(0.005f, Gradient.getPlasmaGradient(), 3, Line.POLY_OCTO);
        ArrowStyle bas = null;
        ArrowStyle eas = new ArrowStyle(0.01f, 0.01f, Gradient.getViridisGradient(), 3, Arrow.TRIANGULAR_REVERSED_3D);
        IDataSet ds = DSFactory3D.getDS("DS", data, null, ls, new ArrowStyles(bas, eas), false, true, 0.005f);
        plot3D.getModel().setDataSet(ds, true);

        frame.setVisible(true);

        //plot3D.getModel().setDataSet(ds, true);
    }
}
