package t1_10.t7_drawing_arrows.t1_changing_interpretation;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DSFactory3D;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Line;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Dimension;
import space.Range;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial showcases how to alter the interpretation of lines, change the font of all plot components, and alter
 * the interpolation level when drawing gradient lines (3D case).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._drawLegend = true;
        pP._title = "Alternated data interpretation";
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.get0R(20.0d),
                Range.get0R(10.0d), new Range(-1.0d, 1.0d));
        pP._scheme = WhiteScheme.getForPlot3D(0.45f);
        pP._scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.1f);
        pP._scheme._sizes.put(SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER, 0.025f);
        pP._scheme.setAllFontsTo("Times New Roman"); // sets fonts to all relevant components
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP_EXTERNAL);
        pP._zProjectionBound = new Dimension(-0.1d, 0.2d); // shrink projection bound
        Plot3D plot3D = new Plot3D(pP);

        ArrayList<IDataSet> dataSets = new ArrayList<>(10);

        // Prepare line styles
        LineStyle[] lss = new LineStyle[6];
        Float legendSize = 1.0f;
        lss[0] = new LineStyle(1.0f, Color.BLACK, legendSize, Line.REGULAR);
        lss[1] = new LineStyle(1.0f, Gradient.getInfernoGradient(), 1, legendSize, Line.REGULAR);
        lss[2] = new LineStyle(0.01f, Color.BLACK, legendSize, Line.POLY_QUAD);
        lss[3] = new LineStyle(0.01f, Gradient.getInfernoGradient(), 1, legendSize, Line.POLY_QUAD);
        lss[4] = new LineStyle(0.01f, Color.BLACK, legendSize, Line.POLY_OCTO);
        lss[5] = new LineStyle(0.01f, Gradient.getInfernoGradient(), 1, legendSize, Line.POLY_OCTO);

        // Prepare marker styles
        MarkerStyle[] mss = new MarkerStyle[6];
        legendSize = 2.0f;
        mss[0] = new MarkerStyle(0.02f, Color.RED, legendSize, Marker.SPHERE_LOW_POLY_3D);
        mss[1] = new MarkerStyle(0.02f, Color.RED, legendSize, Marker.SPHERE_LOW_POLY_3D);
        mss[2] = new MarkerStyle(0.02f, Color.BLUE, legendSize, Marker.SPHERE_LOW_POLY_3D);
        mss[3] = new MarkerStyle(0.02f, Color.BLUE, legendSize, Marker.SPHERE_LOW_POLY_3D);
        mss[4] = new MarkerStyle(0.02f, Color.MAGENTA, legendSize, Marker.SPHERE_LOW_POLY_3D);
        mss[5] = new MarkerStyle(0.02f, Color.MAGENTA, legendSize, Marker.SPHERE_LOW_POLY_3D);

        // Iterate over line style cases:
        for (int i = 0; i < 6; i++)
        {
            // Data set that uses the original lines interpretation (contiguous; only nulls in the linked list break the line)
            {
                double x = 2 * i + 1;
                LinkedList<double[][]> data = new LinkedList<>();
                data.add(null);
                data.add(new double[][]{{x, 1.0d, 0.0d}});
                data.add(new double[][]{{x, 2.0d, 0.0d}});
                data.add(new double[][]{{x, 4.0d, 0.0d}, null, {x + 1.0d, 5.0d, 0.0d}, {x + 1.0d, 6.0d, 0.0d}});
                data.add(null); // hard break
                data.add(new double[][]{{x + 1.0d, 7.0d, 0.0d}, {x + 1.0d, 8.0d, 0.0d}, {x + 1.0d, 9.0d, 0.0d}});
                dataSets.add(DSFactory3D.getDS("DS" + (2 * i + 1) + " (cont; " + lss[i]._style + ")", data, mss[i], lss[i]));
            }

            // Set example datasets (LB = line beginning; LE = line end)
            {
                double x = 2 * i + 2;
                LinkedList<double[][]> data = new LinkedList<>();
                data.add(null); // ignored;
                data.add(new double[][]{{x, 1.0d, 0.0d}}); // LB
                data.add(new double[][]{{x, 2.0d, 0.0d}}); // LE; treated jointly with the previous
                data.add(new double[][]{{x, 4.0d, 0.0d}, // LB
                        null, // ignored
                        {x + 1, 5.0d, 0.0d}, // LE
                        {x + 1, 6.0d, 0.0d} // ignored; not passed to the next segment due to the following break
                });
                data.add(null); // hard break
                data.add(new double[][]{{x + 1, 7.0d, 0.0d}, // LB
                        {x + 1, 8.0d, 0.0d}, // LE
                        {x + 1, 9.0d, 0.0d}} // LB, ignored (has no following data point).
                );
                // Data sets can be built using DSFactory classes now.
                // The last flag changes the interpretation of originally contiguous lines.
                dataSets.add(DSFactory3D.getDS("DS" + (2 * i + 2) + " (breaks; " + lss[i]._style + ")", data, mss[i], lss[i], false, true));
            }
        }


        // Gradient line with fine interpolation level
        lss = new LineStyle[6];
        legendSize = 1.0f;
        lss[0] = new LineStyle(1.0f, Gradient.getViridisGradient(), 1, legendSize, Line.REGULAR);
        lss[1] = new LineStyle(1.0f, Gradient.getViridisGradient(), 1, legendSize, Line.REGULAR);
        lss[2] = new LineStyle(0.01f, Gradient.getViridisGradient(), 1, legendSize, Line.POLY_QUAD);
        lss[3] = new LineStyle(0.01f, Gradient.getViridisGradient(), 1, legendSize, Line.POLY_QUAD);
        lss[4] = new LineStyle(0.01f, Gradient.getViridisGradient(), 1, legendSize, Line.POLY_OCTO);
        lss[5] = new LineStyle(0.01f, Gradient.getViridisGradient(), 1, legendSize, Line.POLY_OCTO);

        for (int i = 0; i < 3; i++)
        {
            double x = 14 + 2 * i;
            {
                double[][] data = new double[][]{{x, 1.0d, 0.0d}, {x, 9.0d, 0.0d}};
                dataSets.add(DSFactory3D.getDS("DS" + (13 + 2 * i) + " (0.005)", data, null, lss[2 * i], false, 0.005f));
            }
            {
                double[][] data = new double[][]{{x + 1.0d, 1.0d, 0.0d}, {x + 1.0d, 9.0d, 0.0d}};
                dataSets.add(DSFactory3D.getDS("DS" + (13 + 2 * i + 1) + " (0.5)", data, null, lss[2 * i + 1], false, 0.75f));
            }
        }


        Frame frame = new Frame(plot3D, 0.4f, 0.5f);
        //Frame frame = new Frame(plot3D, 1200,1000);
        plot3D.getModel().setDataSets(dataSets, true);
        frame.setVisible(true);
    }
}
