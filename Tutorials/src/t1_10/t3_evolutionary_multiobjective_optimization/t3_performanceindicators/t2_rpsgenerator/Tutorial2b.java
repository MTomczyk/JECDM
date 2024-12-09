package t1_10.t3_evolutionary_multiobjective_optimization.t3_performanceindicators.t2_rpsgenerator;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.ReferencePointsFactory;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;

/**
 * Tutorial on the {@link ReferencePointsFactory} class (constructs reference Pareto optimal points).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        int M = 2;
        // Use a sufficiently large number of samples to generate:
        int n = 100000;

        Problem problem = Problem.DTLZ1;
        AbstractMOOProblemBundle bundle = DTLZBundle.getBundle(Problem.DTLZ1, M, 2);

        // Construct reference goals (29 = the number of cuts for the Das and Dennis' method):
        IGoal [] goals = GoalsFactory.getLNormsDND(M, 29, Double.POSITIVE_INFINITY, bundle._normalizations);
        //IGoal [] goals = GoalsFactory.getLNormsDND(M, 29, 2.0d, bundle._normalizations);

        // Create n random points but return a subset of them; identify the best performer among the random ones for each
        // goal; return all of these best performers found.
        double [][] rps = ReferencePointsFactory.getFilteredReferencePoints(problem, n, M, R, goals);

        IDataSet ds = DataSet.getFor2D("RPs", rps, new MarkerStyle(1.5f, Color.RED, Marker.CIRCLE));

        Plot2D.Params pP = new Plot2D.Params();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(bundle._paretoFrontBounds[0], bundle._paretoFrontBounds[1]);
        pP._xAxisTitle = "f1";
        pP._yAxisTitle = "f2";
        pP._title = "Random points = " + n + "; Goals (Chebyshev) = " + goals.length;
        //pP._title = "Random points = " + n + "; Goals (Euclidean) = " + goals.length;
        AbstractPlot plot = new Plot2D(pP);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 600, 600);

        plot.getModel().setDataSet(ds, true, false);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
