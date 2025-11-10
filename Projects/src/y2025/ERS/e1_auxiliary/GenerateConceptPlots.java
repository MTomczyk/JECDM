package y2025.ERS.e1_auxiliary;

import color.Color;
import color.palette.AbstractColorPalette;
import color.palette.DefaultPalette;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Arrow;
import dataset.painter.style.enums.Marker;
import drmanager.DRMPFactory;
import frame.Frame;
import io.FileUtils;
import io.image.ImageSaver;
import plot.Plot2D;
import plot.Plot2DFactory;
import plot.PlotUtils;
import plotswrapper.GridPlots;
import random.IRandom;
import random.MersenneTwister64;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import space.Range;
import utils.Screenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This executable generates the conceptual plots portraying the search imposed by the IEMO/D algorithm.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class GenerateConceptPlots
{
    /**
     * Runs the code.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        AbstractColorPalette cp = new DefaultPalette();

        Plot2D[] plots = new Plot2D[3];
        for (int i = 0; i < 3; i++)
            plots[i] = Plot2DFactory.getPlot("f_1", "f_2",
                    DRMPFactory.getFor2D(Range.get0R(1.5d),
                            Range.get0R(1.5d)),
                    6,
                    6,
                    PlotUtils.getDecimalFormat('.',1),
                    PlotUtils.getDecimalFormat('.',1),
                    2.2f,
                    scheme ->
                    {
                        scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE);
                        scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
                        scheme._sizes.put(SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER, 0.055f);
                        scheme._sizes.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.155f);
                        scheme._sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.18f);
                        scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.04f);
                        scheme._sizes.put(SizeFields.AXIS_Y_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.13f);
                        scheme._sizes.put(SizeFields.AXIS_X_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.08f);
                    },
                    pP -> pP._drawLegend = true,
                    null);

        GridPlots gp = new GridPlots(plots, 1, 3);
        int plotSize = 600;
        Frame frame = new Frame(gp, 3 * plotSize, plotSize);

        IRandom R = new MersenneTwister64(0);

        // Initial DS
        int ps = 10;
        {
            ArrayList<IDataSet> dataSets1 = new ArrayList<>();
            ArrayList<IDataSet> dataSets2 = new ArrayList<>();
            ArrayList<IDataSet> dataSets3 = new ArrayList<>();
            double[] angles1 = new double[ps];
            double[] angles2 = new double[ps];
            double[][] populationData1 = new double[ps][2];

            {
                double da = Math.PI / 2.0d / (ps - 1);
                int idx = 0;
                double[][] data = new double[ps * 2][2];
                for (int i = 0; i < ps; i++)
                {
                    angles1[i] = da * i;
                    double rx = Math.sin(angles1[i]);
                    double ry = Math.cos(angles1[i]);
                    data[idx][0] = rx * 2.0d;
                    data[idx][1] = ry * 2.0d;
                    data[idx + 1][0] = rx * 0.2d;
                    data[idx + 1][1] = ry * 0.2d;
                    idx += 2;
                }
                dataSets1.add(DSFactory2D.getDS("Optimization directions", data, new LineStyle(0.5f,
                                new color.gradient.Color(150, 150, 150), 1.0f),
                        new ArrowStyles(new ArrowStyle(3.0f, 1.5f,
                                new color.gradient.Color(150, 150, 150), 6.0f, 3.0f, Arrow.TRIANGULAR_2D)),
                        true));
                dataSets2.add(DSFactory2D.getDS("Optimization directions", data, new LineStyle(0.5f,
                                new color.gradient.Color(150, 150, 150), 1.0f), new ArrowStyles(
                                new ArrowStyle(3.0f, 1.5f, new color.gradient.Color(150, 150, 150),
                                        6.0f, 3.0f, Arrow.TRIANGULAR_2D)),
                        true));
            }
            {
                dataSets1.add(DSFactory2D.getDS("Pareto front",
                        new double[][]{{0.75d, 0.0d}, {0.0d, 0.75d}},
                        new LineStyle(1.0f, color.gradient.Color.BLACK, 1.5f)));
                dataSets2.add(DSFactory2D.getDS("Pareto front",
                        new double[][]{{0.75d, 0.0d}, {0.0d, 0.75d}},
                        new LineStyle(1.0f, color.gradient.Color.BLACK, 1.5f)));
                dataSets3.add(DSFactory2D.getDS("Pareto front",
                        new double[][]{{0.75d, 0.0d}, {0.0d, 0.75d}},
                        new LineStyle(1.0f, color.gradient.Color.BLACK, 1.5f)));
            }
            {
                double hspread = 0.05d;
                double spread = hspread * 2.0d;
                double l = 1.0d;
                for (int i = 0; i < ps; i++)
                {
                    double rx = Math.sin(angles1[i]) * l;
                    double ry = Math.cos(angles1[i]) * l;
                    double x = rx;
                    double y = ry;
                    do
                    {
                        x += (-hspread + R.nextDouble() * spread);
                        y += (-hspread + R.nextDouble() * spread);
                    }
                    while ((x < 0.0d) || (y < 0.0d) || (x + y < 0.75d));
                    populationData1[i][0] = x;
                    populationData1[i][1] = y;
                }
                dataSets1.add(DSFactory2D.getDS("Population (size = 10)", populationData1,
                        new MarkerStyle(4.0f, new color.gradient.Color(cp.getColor(0)), Marker.CIRCLE, 5.0f)));
                dataSets2.add(DSFactory2D.getDS("Population (size = 10)", populationData1,
                        new MarkerStyle(4.0f, new color.gradient.Color(cp.getColor(0)), Marker.CIRCLE, 5.0f)));
            }
            {
                double[][] pcs = new double[4][2];
                pcs[0][0] = populationData1[2][0];
                pcs[0][1] = populationData1[2][1];
                pcs[1][0] = populationData1[4][0];
                pcs[1][1] = populationData1[4][1];
                pcs[2][0] = populationData1[ps - 2][0];
                pcs[2][1] = populationData1[ps - 2][1];
                pcs[3][0] = populationData1[ps - 4][0];
                pcs[3][1] = populationData1[ps - 4][1];

                dataSets1.add(DSFactory2D.getReferenceDS("Pairwise comparisons", new LineStyle(1.0f,
                                new color.gradient.Color(cp.getColor(3)), 1.0f),
                        new ArrowStyles(new ArrowStyle(6.0f, 4.0f,
                                new color.gradient.Color(cp.getColor(3)), 6.0f, 4.0f, Arrow.TRIANGULAR_2D)),
                        true));
                //dataSets1.get(dataSets1.size() - 1).setDisplayableOnLegend(false);

                dataSets2.add(DSFactory2D.getDS("Pairwise comparisons", pcs, new LineStyle(1.0f,
                                new color.gradient.Color(cp.getColor(3)), 1.0f),
                        new ArrowStyles(new ArrowStyle(6.0f, 4.0f,
                                new color.gradient.Color(cp.getColor(3)), 6.0f, 4.0f, Arrow.TRIANGULAR_2D)),
                        true));
                dataSets3.add(DSFactory2D.getDS("Pairwise comparisons", pcs, new LineStyle(1.0f,
                                new color.gradient.Color(cp.getColor(3)), 1.0f),
                        new ArrowStyles(new ArrowStyle(6.0f, 4.0f,
                                new color.gradient.Color(cp.getColor(3)), 6.0f, 4.0f, Arrow.TRIANGULAR_2D)),
                        true));
            }
            {
                double bAngle = angles1[3];
                double eAngle = angles1[7];
                double da = (eAngle - bAngle) / (ps - 1);
                int idx = 0;
                double[][] data = new double[ps * 2][2];
                for (int i = 0; i < ps; i++)
                {
                    angles2[i] = bAngle + da * i;
                    double rx = Math.sin(angles2[i]);
                    double ry = Math.cos(angles2[i]);
                    data[idx][0] = rx * 2.0d;
                    data[idx][1] = ry * 2.0d;
                    data[idx + 1][0] = rx * 0.2d;
                    data[idx + 1][1] = ry * 0.2d;
                    idx += 2;
                }
                dataSets3.add(DSFactory2D.getDS("Optimization directions", data, new LineStyle(0.5f,
                                new color.gradient.Color(150, 150, 150), 1.0f),
                        new ArrowStyles(new ArrowStyle(3.0f, 1.5f,
                                new color.gradient.Color(150, 150, 150), 6.0f, 3.0f, Arrow.TRIANGULAR_2D)),
                        true));
            }
            {
                double hspread = 0.02d;
                double spread = hspread * 2.0d;
                double l = 0.8d;
                double[][] data = new double[ps][2];
                for (int i = 0; i < ps; i++)
                {
                    double rx = Math.sin(angles2[i]) * l;
                    double ry = Math.cos(angles2[i]) * l;
                    double x = rx;
                    double y = ry;
                    do
                    {
                        x += (-hspread + R.nextDouble() * spread);
                        y += (-hspread + R.nextDouble() * spread);
                    }
                    while ((x < 0.0d) || (y < 0.0d) || (x + y < 0.75d));
                    data[i][0] = x;
                    data[i][1] = y;
                }
                dataSets3.add(DSFactory2D.getDS("Population (size = 10)", data,
                        new MarkerStyle(4.0f, new color.gradient.Color(cp.getColor(0)), 5.0f, Marker.CIRCLE)));
            }

            ArrayList<IDataSet> swapped = new ArrayList<>(4);
            swapped.add(dataSets3.get(2));
            swapped.add(dataSets3.get(0));
            swapped.add(dataSets3.get(3));
            swapped.add(dataSets3.get(1));

            plots[0].getModel().setDataSets(dataSets1);
            plots[1].getModel().setDataSets(dataSets2);
            plots[2].getModel().setDataSets(swapped);
        }

        frame.setVisible(true);

        for (int i = 0; i < 3; i++)
        {
            Screenshot screenshot = plots[i].getModel().requestScreenshotCreation(plotSize * 2, plotSize * 2, false);
            try
            {
                screenshot._barrier.await();
                Path path = FileUtils.getPathRelatedToClass(GenerateConceptPlots.class,
                        "Projects", "src", File.separatorChar);
                String fp = path.toString() + File.separatorChar + "concept_plot" + i;
                System.out.println(fp);
                ImageSaver.saveImage(screenshot._image, fp, "jpg", 1.0f);

            } catch (InterruptedException | IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
