package t1_10.t7_drawing_arrows.t1_changing_interpretation;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial showcases how to alter the interpretation of lines, change the font of all plot components, and alter
 * the interpolation level when drawing gradient lines (2D case).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1a
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
        pP._title = "Alternated data interpretation";
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.get0R(12.0d), Range.get0R(10.0d));
        pP._scheme = new WhiteScheme();
        pP._scheme.setAllFontsTo("Times New Roman"); // sets fonts to all relevant components
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
        Plot2D plot2D = new Plot2D(pP);

        ArrayList<IDataSet> dataSets = new ArrayList<>(10);

        // Prepare line styles
        LineStyle[] lss = new LineStyle[2];
        lss[0] = new LineStyle(1.0f, Color.BLACK);
        lss[1] = new LineStyle(1.0f, Gradient.getInfernoGradient(), 1);

        // Iterate over line style cases:
        for (int i = 0; i < 2; i++)
        {
            // Data set that uses the original lines interpretation (contiguous; only nulls in the linked list break the line)
            {
                double x = 2 * i + 1;
                LinkedList<double[][]> data = new LinkedList<>();
                data.add(null);
                data.add(new double[][]{{x, 1.0d}});
                data.add(new double[][]{{x, 2.0d}});
                data.add(new double[][]{{x, 4.0d}, null, {x + 1.0d, 5.0d}, {x + 1.0d, 6.0d}});
                data.add(null); // hard break
                data.add(new double[][]{{x + 1.0d, 7.0d}, {x + 1.0d, 8.0d}, {x + 1.0d, 9.0d}});
                MarkerStyle ms = new MarkerStyle(2.0f, Color.RED, Marker.SQUARE);
                dataSets.add(DSFactory2D.getDS("DS" + (2 * i + 1) + " (cont)", data, ms, lss[i]));
            }

            // Set example datasets (LB = line beginning; LE = line end)
            {
                double x = 2 * i + 2;
                LinkedList<double[][]> data = new LinkedList<>();
                data.add(null); // ignored;
                data.add(new double[][]{{x, 1.0d}}); // LB
                data.add(new double[][]{{x, 2.0d}}); // LE; treated jointly with the previous
                data.add(new double[][]{{x, 4.0d}, // LB
                        null, // ignored
                        {x + 1, 5.0d}, // LE
                        {x + 1, 6.0d} // ignored; not passed to the next segment due to the following break
                });
                data.add(null); // hard break
                data.add(new double[][]{{x + 1, 7.0d}, // LB
                        {x + 1, 8.0d}, // LE
                        {x + 1, 9.0d}} // LB, ignored (has no following data point).
                );
                MarkerStyle ms = new MarkerStyle(2.0f, Color.RED, Marker.SQUARE);
                // Data sets can be built using DSFactory classes now.
                // The last flag changes the interpretation of originally contiguous lines.
                dataSets.add(DSFactory2D.getDS("DS" + (2 * i + 2) + " (breaks)", data, ms, lss[i], true));
            }
        }

        // Gradient line with fine interpolation level
        {
            double[][] data = new double[][]{{6.0d, 1.0d}, {6.0d, 9.0d}};
            LineStyle ls = new LineStyle(1.0f, Gradient.getViridisGradient(), 1);
            dataSets.add(DSFactory2D.getDS("DS5 (0.005)", data, null, ls, 0.005f));
        }

        // Gradient line with low interpolation level
        {
            double[][] data = new double[][]{{7.0d, 1.0d}, {7.0d, 9.0d}};
            LineStyle ls = new LineStyle(1.0f, Gradient.getViridisGradient(), 1);
            dataSets.add(DSFactory2D.getDS("DS6 (0.2)", data, null, ls, 0.2f));
        }


        Frame frame = new Frame(plot2D, 0.5f);
        //Frame frame = new Frame(plot2D, 800, 800);
        plot2D.getModel().setDataSets(dataSets, true);
        frame.setVisible(true);
    }
}
