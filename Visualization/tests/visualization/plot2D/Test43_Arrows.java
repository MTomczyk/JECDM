package visualization.plot2D;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DSFactory2D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Arrow;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Test drawing arrows.
 *
 * @author MTomczyk
 */
public class Test43_Arrows
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
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP_EXTERNAL);
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._drawLegend = true;
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        Plot2D plot2D = new Plot2D(pP);

        Frame frame = new Frame(plot2D, 0.5f, 0.5f);

        ArrayList<IDataSet> DSs = new ArrayList<>();

        {    // easy

            double[][] data = new double[][]
                    {
                            {0.125d, 0.375d},
                            {0.375d, 0.375d},
                            {0.575d, 0.125d},
                            {0.825d, 0.125d}
                    };

            LineStyle ls = new LineStyle(1.0f, Gradient.getViridisGradient(), 0);
            DataSet ds = DSFactory2D.getDS("Arrows 1", data, null, ls,
                    new ArrowStyles(
                            new ArrowStyle(10.0f, 10.0f, Gradient.getPlasmaGradient(), 1, 1.0f,1.0f, Arrow.TRIANGULAR_2D),
                            new ArrowStyle(10.0f, 10.0f, Gradient.getRedBlueGradient(), 0, 1.0f,1.0f, Arrow.TRIANGULAR_2D)
                    ),
                    true, 0.08f);
            DSs.add(ds);
        }
        {
            LinkedList<double[][]> lData = new LinkedList<>();
            lData.add(null); // skipped
            lData.add(null); // skipped
            lData.add(new double[][]
                    {
                            {0.2d, 0.6d},
                            null, // skipped
                            {0.4d, 0.7d},

                    });
            lData.add(null);
            lData.add(new double[][]
                    {
                            {0.5d, 0.7d},
                            null, // skipped
                            {0.6d, 0.7d},
                            {0.7d, 0.7d}, // odd, not used (but marker will be drawn)
                    });
            lData.add(null); // skipped

            MarkerStyle ms = new MarkerStyle(2.0f, Color.RED, Marker.CIRCLE);
            LineStyle ls = new LineStyle(1.0f, Color.BLUE);
            DataSet ds = DSFactory2D.getDS("Arrows 2", lData, ms, ls, null, true, 0.005f);
            DSs.add(ds);
        }

        {
            LinkedList<double[][]> lData = new LinkedList<>();
            lData.add(null); // skipped
            lData.add(new double[][]
                    {
                            {0.9d, 0.1d},
                            null, // skipped
                            {0.7d, 0.2d},
                            null, // skipped
                            {0.7d, 0.3d},
                            null, // skipped
                            null, // skipped
                            {0.8d, 0.6d},
                            {0.9d, 0.7d},  // odd, not used (but marker will be drawn)
                            null // skipped
                    });
            lData.add(null); // skipped

            MarkerStyle ms = new MarkerStyle(2.0f, Gradient.getPlasmaGradient(), 1, Marker.CIRCLE);
            LineStyle ls = new LineStyle(1.0f, Color.RED);
            DataSet ds = DSFactory2D.getDS("Arrows 3", lData, ms, ls, null, true, 0.005f);
            DSs.add(ds);
        }


        {
            LinkedList<double[][]> lData = new LinkedList<>();
            lData.add(null); // skipped
            lData.add(new double[][]
                    {
                            {0.1d, 0.7d},
                            null, // skipped
                            {0.2d, 0.8d},
                            null, // skipped
                            {0.4d, 0.85d},
                            null, // skipped
                            null, // skipped
                            {0.6d, 0.8d},
                            {0.8d, 0.9d},  // odd, not used (but marker will be drawn)
                            null // skipped
                    });
            lData.add(null); // skipped

            MarkerStyle ms = new MarkerStyle(2.0f, Color.BLACK, Marker.CIRCLE);
            LineStyle ls = new LineStyle(1.0f, Gradient.getRedBlueGradient(), 0);
            DataSet ds = DSFactory2D.getDS("Arrows 4", lData, ms, ls, null, true, 0.1f);
            DSs.add(ds);
        }

        {
            double[][] data = new double[50][2];
            for (int i = 0; i < 50; i++)
            {
                data[i][0] = 0.05d;
                data[i][1] = 0.05d + i * 0.0185d;
            }
            LineStyle ls = new LineStyle(1.0f, Gradient.getInfernoGradient(), 1);
            DataSet ds = DSFactory2D.getDS("Arrows 5", data, null, ls, null, true, 0.005f);
            DSs.add(ds);
        }

        plot2D.getModel().setDataSets(DSs, true);
        frame.setVisible(true);
    }
}
