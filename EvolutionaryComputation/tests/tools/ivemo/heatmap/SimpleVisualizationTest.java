package tools.ivemo.heatmap;

import alternative.Alternative;
import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import ea.dummy.populations.EADummyPopulations;
import ea.dummy.populations.EADummyPopulationsFactory;
import ea.factory.IEAFactory;
import frame.Frame;
import plot.heatmap.Heatmap2D;
import population.Specimen;
import population.SpecimenID;
import print.PrintUtils;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;
import statistics.Mean;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;
import tools.ivemo.heatmap.feature.Generation;
import tools.ivemo.heatmap.utils.BucketData;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for the {@link AbstractHeatmapProcessor} class.
 *
 * @author MTomczyk
 */
class SimpleVisualizationTest
{
    /**
     * Performs visualization test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // DPI TEST SCALE
        System.setProperty("sun.java2d.uiScale", "1");
        // DPI TEST SCALE

        /* The following input data is considered.
         Params:
         - 2D
         - buckets 4 x 4
         - 3 generations
         - 3 trials
         - x display bounds = [0, 1] -> buckets = [0, 0.25), [0.25, 0.5), [0.5, 0.75), [0.75, 1.0]
         - y display bounds = [0, 2] -> buckets = [0, 0.5), [0.5, 1.0), [1.0, 1.5), [1.5, 2.0]
         Run for Average (top level) from average (trial level) from generations reported

         TRIAL 0
         Expected matrix (rows = y dim; columns = x dim):
         GEN 0         GEN 1         GEN 2
         - , -, 0, -   1, -, -, 1    2, 2, -, -
         - , 0, 0, -   1, 1, 1, 1    2, 2, 2, 2
         - , -, -, -   -, -, 1, -    -, -, 2, -
         0 , -, -, 0   1, 1, 1, 1    -, -, -, -*/

        double[][] imG0T0 = new double[][] // EXPECTED INTERMEDIATE MATRICES
                {
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0, Double.NEGATIVE_INFINITY},
                        {Double.NEGATIVE_INFINITY, 0, 0, Double.NEGATIVE_INFINITY},
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                        {0, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0}
                };


        // POPULATIONS REPRODUCING EXPECTED MATRICES
        ArrayList<Specimen> populG0T0 = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
            {
                if (Double.compare(imG0T0[y][x], Double.NEGATIVE_INFINITY) == 0) continue;
                double[] c = new double[]{1.0d / 8.0d + x / 4.0d, 1.0d / 4.0d + y / 2.0d};
                Specimen spec = new Specimen(2, new SpecimenID(0, 0, 0, 0));
                spec.setAlternative(new Alternative("A", c));
                populG0T0.add(spec);
            }

        BucketCoordsTransform BC = new BucketCoordsTransform(2, new int[]{4, 4},
                new Range[]{new Range(0.0d, 1.0d), new Range(0.0d, 2.0d)}, new LinearlyThresholded());
        for (Specimen s : populG0T0)
        {
            int[] coords = BC.getBucketCoords(s.getEvaluations());
            assertEquals(0, imG0T0[coords[1]][coords[0]]);
        }


        double[][] imG1T0 = new double[][] // EXPECTED INTERMEDIATE MATRICES
                {
                        {1, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1},
                        {1, 1, 1, 1},
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1, Double.NEGATIVE_INFINITY},
                        {1, 1, 1, 1}
                };

        ArrayList<Specimen> populG1T0 = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
            {
                if (Double.compare(imG1T0[y][x], Double.NEGATIVE_INFINITY) == 0) continue;
                double[] c = new double[]{1.0d / 8.0d + x / 4.0d, 1.0d / 4.0d + y / 2.0d};
                Specimen spec = new Specimen(2, new SpecimenID(0, 1, 0, 0));
                spec.setAlternative(new Alternative("A", c));
                populG1T0.add(spec);
            }

        for (Specimen s : populG1T0)
        {
            int[] coords = BC.getBucketCoords(s.getEvaluations());
            assertEquals(1, imG1T0[coords[1]][coords[0]]);
        }


        double[][] imG2T0 = new double[][] // EXPECTED INTERMEDIATE MATRICES
                {
                        {2, 2, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                        {2, 2, 2, 2},
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 2, Double.NEGATIVE_INFINITY},
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}
                };

        ArrayList<Specimen> populG2T0 = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
            {
                if (Double.compare(imG2T0[y][x], Double.NEGATIVE_INFINITY) == 0) continue;
                double[] c = new double[]{1.0d / 8.0d + x / 4.0d, 1.0d / 4.0d + y / 2.0d};
                Specimen spec = new Specimen(2, new SpecimenID(0, 2, 0, 0));
                spec.setAlternative(new Alternative("A", c));
                populG2T0.add(spec);
            }

        for (Specimen s : populG2T0)
        {
            int[] coords = BC.getBucketCoords(s.getEvaluations());
            assertEquals(2, imG2T0[coords[1]][coords[0]]);
        }

        double[][][] dummyPopulsT0 = new double[3][][];
        dummyPopulsT0[0] = new double[populG0T0.size()][];
        for (int i = 0; i < populG0T0.size(); i++) dummyPopulsT0[0][i] = populG0T0.get(i).getEvaluations();
        dummyPopulsT0[1] = new double[populG1T0.size()][];
        for (int i = 0; i < populG1T0.size(); i++) dummyPopulsT0[1][i] = populG1T0.get(i).getEvaluations();
        dummyPopulsT0[2] = new double[populG2T0.size()][];
        for (int i = 0; i < populG2T0.size(); i++) dummyPopulsT0[2][i] = populG2T0.get(i).getEvaluations();

         /*TRIAL 1
         Expected matrix (rows = y dim; columns = x dim):
         GEN 0         GEN 1         GEN 2
         0 , 0, 0, 0   1, -, 1, 1    2, -, 2, -
         0 , 0, 0, 0   1, -, -, 1    2, -, -, 2
         0 , -, -, -   1, -, 1, -    -, -, 2, -
         0 , -, -, 0   1, -, 1, -    2, -, -, -*/

        double[][] imG0T1 = new double[][] // EXPECTED INTERMEDIATE MATRICES
                {
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                        {0, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0}
                };

        ArrayList<Specimen> populG0T1 = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
            {
                if (Double.compare(imG0T1[y][x], Double.NEGATIVE_INFINITY) == 0) continue;
                double[] c = new double[]{1.0d / 8.0d + x / 4.0d, 1.0d / 4.0d + y / 2.0d};
                Specimen spec = new Specimen(2, new SpecimenID(0, 0, 0, 0));
                spec.setAlternative(new Alternative("A", c));
                populG0T1.add(spec);
            }

        for (Specimen s : populG0T1)
        {
            int[] coords = BC.getBucketCoords(s.getEvaluations());
            assertEquals(0, imG0T1[coords[1]][coords[0]]);
        }


        double[][] imG1T1 = new double[][] // EXPECTED INTERMEDIATE MATRICES
                {
                        {1, Double.NEGATIVE_INFINITY, 1, 1},
                        {1, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1},
                        {1, Double.NEGATIVE_INFINITY, 1, Double.NEGATIVE_INFINITY},
                        {1, Double.NEGATIVE_INFINITY, 1, Double.NEGATIVE_INFINITY}
                };

        ArrayList<Specimen> populG1T1 = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
            {
                if (Double.compare(imG1T1[y][x], Double.NEGATIVE_INFINITY) == 0) continue;
                double[] c = new double[]{1.0d / 8.0d + x / 4.0d, 1.0d / 4.0d + y / 2.0d};
                Specimen spec = new Specimen(2, new SpecimenID(0, 1, 0, 0));
                spec.setAlternative(new Alternative("A", c));
                populG1T1.add(spec);
            }

        for (Specimen s : populG1T1)
        {
            int[] coords = BC.getBucketCoords(s.getEvaluations());
            assertEquals(1, imG1T1[coords[1]][coords[0]]);
        }


        double[][] imG2T1 = new double[][] // EXPECTED INTERMEDIATE MATRICES
                {
                        {2, Double.NEGATIVE_INFINITY, 2, Double.NEGATIVE_INFINITY},
                        {2, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 2},
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 2, Double.NEGATIVE_INFINITY},
                        {2, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}
                };

        ArrayList<Specimen> populG2T1 = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
            {
                if (Double.compare(imG2T1[y][x], Double.NEGATIVE_INFINITY) == 0) continue;
                double[] c = new double[]{1.0d / 8.0d + x / 4.0d, 1.0d / 4.0d + y / 2.0d};
                Specimen spec = new Specimen(2, new SpecimenID(0, 2, 0, 0));
                spec.setAlternative(new Alternative("A", c));
                populG2T1.add(spec);
            }

        for (Specimen s : populG2T1)
        {
            int[] coords = BC.getBucketCoords(s.getEvaluations());
            assertEquals(2, imG2T1[coords[1]][coords[0]]);
        }

        double[][][] dummyPopulsT1 = new double[3][][];
        dummyPopulsT1[0] = new double[populG0T1.size()][];
        for (int i = 0; i < populG0T1.size(); i++) dummyPopulsT1[0][i] = populG0T1.get(i).getEvaluations();
        dummyPopulsT1[1] = new double[populG1T1.size()][];
        for (int i = 0; i < populG1T1.size(); i++) dummyPopulsT1[1][i] = populG1T1.get(i).getEvaluations();
        dummyPopulsT1[2] = new double[populG2T1.size()][];
        for (int i = 0; i < populG2T1.size(); i++) dummyPopulsT1[2][i] = populG2T1.get(i).getEvaluations();

         /*TRIAL 2
         Expected matrix (rows = y dim; columns = x dim):
         GEN 0         GEN 1         GEN 2
         0 , 0, 0, 0   -, -, -, 1    -, -, 2, -
         - , -, -, 0   -, -, 1, 1    -, 2, -, 2
         - , 0, -, -   1, -, 1, -    -, -, 2, -
         - , -, -, 0   1, -, 1, -    2, -, -, 2*/

        double[][] imG0T2 = new double[][] // EXPECTED INTERMEDIATE MATRICES
                {
                        {0, 0, 0, 0},
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0},
                        {Double.NEGATIVE_INFINITY, 0, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0}
                };

        ArrayList<Specimen> populG0T2 = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
            {
                if (Double.compare(imG0T2[y][x], Double.NEGATIVE_INFINITY) == 0) continue;
                double[] c = new double[]{1.0d / 8.0d + x / 4.0d, 1.0d / 4.0d + y / 2.0d};
                Specimen spec = new Specimen(2, new SpecimenID(0, 0, 0, 0));
                spec.setAlternative(new Alternative("A", c));
                populG0T2.add(spec);
            }

        for (Specimen s : populG0T2)
        {
            int[] coords = BC.getBucketCoords(s.getEvaluations());
            assertEquals(0, imG0T2[coords[1]][coords[0]]);
        }


        double[][] imG1T2 = new double[][] // EXPECTED INTERMEDIATE MATRICES
                {
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1},
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1, 1},
                        {1, Double.NEGATIVE_INFINITY, 1, Double.NEGATIVE_INFINITY},
                        {1, Double.NEGATIVE_INFINITY, 1, Double.NEGATIVE_INFINITY}
                };

        ArrayList<Specimen> populG1T2 = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
            {
                if (Double.compare(imG1T2[y][x], Double.NEGATIVE_INFINITY) == 0) continue;
                double[] c = new double[]{1.0d / 8.0d + x / 4.0d, 1.0d / 4.0d + y / 2.0d};
                Specimen spec = new Specimen(2, new SpecimenID(0, 1, 0, 0));
                spec.setAlternative(new Alternative("A", c));
                populG1T2.add(spec);
            }

        for (Specimen s : populG1T2)
        {
            int[] coords = BC.getBucketCoords(s.getEvaluations());
            assertEquals(1, imG1T2[coords[1]][coords[0]]);
        }

        double[][] imG2T2 = new double[][] // EXPECTED INTERMEDIATE MATRICES
                {
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 2, Double.NEGATIVE_INFINITY},
                        {Double.NEGATIVE_INFINITY, 2, Double.NEGATIVE_INFINITY, 2},
                        {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 2, Double.NEGATIVE_INFINITY},
                        {2, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 2}
                };

        ArrayList<Specimen> populG2T2 = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
            {
                if (Double.compare(imG2T2[y][x], Double.NEGATIVE_INFINITY) == 0) continue;
                double[] c = new double[]{1.0d / 8.0d + x / 4.0d, 1.0d / 4.0d + y / 2.0d};
                Specimen spec = new Specimen(2, new SpecimenID(0, 2, 0, 0));
                spec.setAlternative(new Alternative("A", c));
                populG2T2.add(spec);
            }

        for (Specimen s : populG2T2)
        {
            int[] coords = BC.getBucketCoords(s.getEvaluations());
            assertEquals(2, imG2T2[coords[1]][coords[0]]);
        }

        double[][][] dummyPopulsT2 = new double[3][][];
        dummyPopulsT2[0] = new double[populG0T2.size()][];
        for (int i = 0; i < populG0T2.size(); i++) dummyPopulsT2[0][i] = populG0T2.get(i).getEvaluations();
        dummyPopulsT2[1] = new double[populG1T2.size()][];
        for (int i = 0; i < populG1T2.size(); i++) dummyPopulsT2[1][i] = populG1T2.get(i).getEvaluations();
        dummyPopulsT2[2] = new double[populG2T2.size()][];
        for (int i = 0; i < populG2T2.size(); i++) dummyPopulsT2[2][i] = populG2T2.get(i).getEvaluations();

        EADummyPopulations eaT0 = new EADummyPopulations(2, dummyPopulsT0);
        EADummyPopulations eaT1 = new EADummyPopulations(2, dummyPopulsT1);
        EADummyPopulations eaT2 = new EADummyPopulations(2, dummyPopulsT2);
        IEAFactory EAF = new EADummyPopulationsFactory(new EADummyPopulations[]{eaT0, eaT1, eaT2});

        // CONSTRUCT AND RUN THE PROCESSOR
        Heatmap2DProcessor.Params pHP = new Heatmap2DProcessor.Params();
        pHP._eaFactory = EAF;
        pHP._notify = true;
        pHP._trials = 3;
        pHP._steadyStateRepeats = 1;
        pHP._generations = 3;
        pHP._generationalDataUpdate = true;
        pHP._featureGetter = new Generation();
        pHP._trialStatistics = new Mean();
        pHP._finalStatistics = new Mean();
        pHP._xAxisDivisions = 4;
        pHP._xAxisDisplayRange = new Range(0.0d, 1.0d);
        pHP._yAxisDivisions = 4;
        pHP._yAxisDisplayRange = new Range(0.0d, 2.0d);
        pHP._clearIntermediateData = false;
        Heatmap2DProcessor h2D = new Heatmap2DProcessor(pHP);

        // execute processing
        h2D.executeProcessing();
        double[][][] hmd = h2D.getRawData();
        assertEquals(1, hmd.length);

        ArrayList<BucketData[][][]> btd = h2D.getTrialData();
        assertEquals(3, btd.size());

        assertEquals(1, btd.get(0).length);
        System.out.println("Trial 1 results:");
        {
            double[][] expM = new double[][]
                    {
                            {1.5d, 2.0d, 0.0d, 1.0d},
                            {1.5d, 1.0d, 1.0d, 1.5d},
                            {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1.5d, Double.NEGATIVE_INFINITY},
                            {0.5d, 1.0d, 1.0d, 0.5d},
                    };

            BucketData[][] bd = btd.get(0)[0];
            for (int y = 0; y < bd.length; y++)
            {
                for (int x = 0; x < bd[0].length; x++)
                {
                    System.out.print(bd[y][x]._value + " : [");
                    if (bd[y][x]._LA.getNoStoredElements() > 0)
                        for (double v : bd[y][x]._LA.getTransformedToArray()) System.out.print(v + "; ");
                    System.out.print("] ");

                    assertEquals(expM[y][x], bd[y][x]._value, 0.00001d);
                }
                System.out.println();
            }
            System.out.println();
        }


        assertEquals(1, btd.get(1).length);
        assertEquals(1, btd.get(2).length);

        System.out.println();
        System.out.println("Final results");
        PrintUtils.print2dDoubles(hmd[0], 2);

        double[][] expF = new double[][]
                {
                        {(1.5d + 1.0d + 0.0d) / 3.0d, (2.0d + 0.0d + 0.0d) / 3.0d, (0.0d + 1.0d + 1.0d) / 3.0d, (1.0d + 0.5d + 0.5d) / 3},
                        {(1.5d + 1.0d) / 2.0d, (1.0d + 0.0d + 2.0d) / 3.0d, (1.0d + 0.0d + 1.0d) / 3.0d, (1.5d + 1.0d + 1.0d) / 3.0d},
                        {(0.5d + 1.0d) / 2.0d, (0.0d) / 3.0d, (1.5d + 1.5d + 1.5d) / 3.0d, Double.NEGATIVE_INFINITY},
                        {(0.5d + 1.0d + 1.5d) / 3.0d, 1.0d, (1.0d + 1.0d + 1.0d) / 3.0d, (0.5d + 0.0d + 1.0d) / 3.0d}
                };

        assertEquals(4, expF.length);
        assertEquals(4, expF[0].length);

        for (int y = 0; y < expF.length; y++)
            for (int x = 0; x < expF[0].length; x++)
                assertEquals(expF[y][x], hmd[0][y][x], 0.00001d);


        Heatmap2D.Params pPlot = new Heatmap2D.Params();
        pPlot._xDiv = 4;
        pPlot._yDiv = 4;
        pPlot._scheme = new WhiteScheme();
        pPlot._scheme._aligns.put(AlignFields.TITLE, Align.TOP);
        pPlot._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
        pPlot._title = null;
        pPlot._drawXAxis = true;
        pPlot._xAxisTitle = "X";
        pPlot._drawYAxis = true;
        pPlot._yAxisTitle = "Y";
        pPlot._drawMainGridlines = false;
        pPlot._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), new Range(0.0d, 2.0d));
        pPlot._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), true, false);
        pPlot._gradient = Gradient.getViridisGradient();
        pPlot._colorbar = new Colorbar(Gradient.getViridisGradient(), "Value", new FromDisplayRange(pPlot._heatmapDisplayRange, 5));
        pPlot._xAxisWithBoxTicks = true;
        pPlot._yAxisWithBoxTicks = true;
        Heatmap2D HM = new Heatmap2D(pPlot);

        Frame F = new Frame(HM, 0.5f);
        HM.getModel().setDataAndPerformProcessing(hmd[0]);

        F.setVisible(true);
    }
}