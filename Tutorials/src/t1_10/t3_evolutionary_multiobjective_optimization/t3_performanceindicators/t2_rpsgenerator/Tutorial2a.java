package t1_10.t3_evolutionary_multiobjective_optimization.t3_performanceindicators.t2_rpsgenerator;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
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
 * Tutorial on the {@link problem.moo.ReferencePointsFactory} class (constructs reference Pareto optimal points).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // random number generator
        IRandom R = new MersenneTwister64(0);
        // the number of objectives (should be >= 2)
        int M = 2;
        // the number of random samples to construct
        int n = 1000;

        // Set the problem ID
        Problem problem = Problem.DTLZ1;

        // Create reference points
        double [][] rps = ReferencePointsFactory.getRandomReferencePoints(problem, n, M, R);

        // Create problem bundle (contains useful data on PF bounds; the number of decision variables is not important -> we want to construct the normalization objects)
        AbstractMOOProblemBundle bundle = DTLZBundle.getBundle(Problem.DTLZ1, M, 2);

        // Create data set
        IDataSet ds = DataSet.getFor2D("RPs", rps, new MarkerStyle(1.5f, Color.RED, Marker.CIRCLE));

        // Create and display the plot
        Plot2D.Params pP = new Plot2D.Params();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(bundle._paretoFrontBounds[0], bundle._paretoFrontBounds[1]);
        pP._xAxisTitle = "f1";
        pP._yAxisTitle = "f2";
        pP._title = "Number of random points = " + n;
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
