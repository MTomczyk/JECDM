package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import ea.EA;
import frame.Frame;
import phase.PhasesBundle;
import plot.Plot3D;
import population.Specimen;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import scheme.AbstractScheme;
import scheme.WhiteScheme;
import scheme.enums.Align;
import selection.Tournament;
import space.Range;

/**
 * Provides some utility functions.
 *
 * @author MTomczyk
 */


public class Utils
{
    /**
     * Creates the default problem instance.
     *
     * @return array of kernels
     */
    public static Kernel[] getDefaultKernels()
    {
        return new Kernel[]
                {
                        new Kernel(2.5d, 5.0d, 0.7d, 0.3d),
                        new Kernel(1.0d, 6.0d, 0.2d, 0.2d),
                        new Kernel(1.5d, 6.0d, 0.3d, 0.6d),
                        new Kernel(1.2d, 3.0d, 0.7d, 0.9d),
                        new Kernel(1.7d, 14.0d, 0.35d, 0.35d),
                        new Kernel(1.4d, 6.5d, 0.6d, 0.6d),
                        new Kernel(2.0d, 10.0d, 0.2d, 0.9d),
                };
    }

    /**
     * Evaluates a point [x1, x2] given the kernel functions.
     *
     * @param x1      the first coordinate
     * @param x2      the second coordinate
     * @param kernels kernels used
     * @return final evaluation (sum of kernel values)
     */
    public static double evaluatePoint(double x1, double x2, Kernel[] kernels)
    {
        double sum = 0.0d;
        for (Kernel k : kernels) sum += k.evaluate(x1, x2);
        return sum;
    }

    /**
     * Auxiliary method for printing specimen data.
     *
     * @param s                    specimen
     * @param printDecisionVectors if true, the decision vectors will be printed
     */
    @SuppressWarnings("DuplicatedCode")
    public static void printSpecimenData(Specimen s, boolean printDecisionVectors)
    {
        String s1 = "Specimen = " + s.getID().toString() + ": [value = " + s.getEvaluations()[0] + ";" +
                " aux = " + s.getAlternative().getAuxScore() + "] ";
        String s2 = "";
        if (printDecisionVectors)
        {
            StringBuilder sb = new StringBuilder();
            double[] dv = s.getDoubleDecisionVector();
            for (double v : dv) sb.append(String.format("%.4f ", v));
            s2 = sb.toString();
        }
        System.out.println(s1 + s2);
    }

    /**
     * Auxiliary method that creates an evolutionary algorithm for solving the 2-variable continuous problem.
     *
     * @param populationSize population size (equals the offspring size)
     * @param kernels        kernels used
     * @param R              random number generator
     * @return instance of {@link EA}.
     */
    @SuppressWarnings("DuplicatedCode")
    public static EA getContEA(int populationSize, Kernel[] kernels, IRandom R)
    {
        // Create the bundle params container:
        ContEABundle.Params pB = new ContEABundle.Params(kernels);

        // Use a tournament selection (the winner is selected based on the aux value; greater values are preferred)
        Tournament.Params pTournament = new Tournament.Params(2, populationSize);
        pTournament._preferenceDirection = true; //  greater values are preferred
        pB._select = new Tournament(pTournament);

        // Create the parameterized bundle:
        ContEABundle bundle = new ContEABundle(pB);

        // Create the EA params container:
        EA.Params pEA = new EA.Params(bundle._name, pB._criteria);
        // Transfer all phases from the bundle to EA:
        PhasesBundle.copyPhasesFromBundleToEA(pEA, bundle._phasesBundle);
        // Set EA id:
        pEA._id = 0;
        // Set the random number generator (accessible from the ea object)"
        pEA._R = R;
        // Set the population size (and offspring size):
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize; // (equals the population size size)
        return new EA(pEA);
    }


    /**
     * Auxiliary method that generates an input data for the visualization module (double[][]) that illustrates
     * the fitness landscape of the tackled problem. The data points are three-element tuples, where the first (X)
     * and the third (Z) attribute are x1 and x2 coordinates, while the second attribute (Y) if the associated kernel-based
     * evaluation.
     *
     * @param disc    discretization level (number of divisions on x1 and x2 dimensions)
     * @param kernels kernels used
     * @return data to be visualized
     */
    public static double[][] getFitnessLandscape(int disc, Kernel[] kernels)
    {
        int div = disc - 1;
        int total = disc * disc;
        double[][] data = new double[total][3];

        int idx = 0;

        for (int i = 0; i < disc; i++)
        {
            for (int j = 0; j < disc; j++)
            {
                data[idx][0] = (double) i / div;
                data[idx][2] = (double) j / div;
                data[idx][1] = evaluatePoint(data[idx][0], data[idx][2], kernels);
                idx++;
            }
        }
        return data;
    }


    /**
     * Creates a 3D plot dedicated to the 2-variable continuous problem.
     *
     * @return 3D plot instance
     */
    public static Plot3D createDedicatedScatterPlot()
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._xAxisTitle = "x1";
        pP._yAxisTitle = "Fitness";
        pP._zAxisTitle = "x2";
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.getNormalRange(),
                new Range(0.0d, 3.0d), Range.getNormalRange());
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(null, true);
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Fitness", new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 5));
        pP._axesAlignments = new Align[]{Align.FRONT_BOTTOM, Align.LEFT_BOTTOM, Align.BACK_RIGHT};
        return new Plot3D(pP);
    }


    /**
     * Runs the visualization of the fitness landscape.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        Kernel[] kernels = getDefaultKernels();
        double[][] data = getFitnessLandscape(1000, kernels);

        Plot3D plot3D = createDedicatedScatterPlot();
        Frame frame = new Frame(plot3D, 0.5f);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot3D.getController().addRightClickPopupMenu(menu);

        AbstractScheme scheme = WhiteScheme.getForPlot3D(0.25f);

        plot3D.updateScheme(scheme);

        IDataSet ds = DataSet.getFor3D("Fitness landscape", data, new MarkerStyle(0.01f, Gradient.getViridisGradient(), 1, Marker.SPHERE_LOW_POLY_3D));
        plot3D.getModel().setDataSet(ds, true);

        frame.setVisible(true);
    }
}
