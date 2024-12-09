package emo.aposteriori.moead.dtlz.dtlz4.a2D;

import color.gradient.Gradient;
import criterion.Criteria;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import ea.EA;
import emo.aposteriori.Utils;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import exception.RunnerException;
import frame.Frame;
import plot.Plot2D;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import visualization.IVisualization;

/**
 * Solving DTLZ problem (2D) using MOEA/D.
 * The visualization module is run to present the results.
 *
 * @author MTomczyk
 */

public class DTLZ4_2D_Cumulative_DisplayAtTheEnd
{
    /**
     * Runs evolutionary algorithm.
     *
     * @param args (not used)
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);

        // create problem bundle
        Problem problem = Problem.DTLZ4;
        int noDecisionVariables = 5;
        DTLZBundle problemBundle = DTLZBundle.getBundle(problem, 2, noDecisionVariables);

        IGoal [] goals =  GoalsFactory.getLNormsDND( 2, 60, Double.POSITIVE_INFINITY, problemBundle._normalizations);
        int populationSize = goals.length;
        int generations = 1000;

        boolean dynamicObjectiveRanges = false;
        Criteria criteria = Criteria.constructCriteria("C", 2, false);

        EA moead = Utils.getMOEAD(criteria, problemBundle, dynamicObjectiveRanges, R, goals, new Euclidean());

        // create visualization module
        Plot2D plot = emo.Utils.getPlot2D(problemBundle._displayRanges, true);
        Frame frame = new Frame(plot, 0.5f, 0.5f);

        // create updater
        DataSet ds = DataSet.getFor2D("MOEA/D", new MarkerStyle(2.0f, Gradient.getViridisGradient(), 2, Marker.CIRCLE));
        IVisualization visualization = emo.Utils.getVisualization(frame, moead, ds, true);

        // create runner object
        Runner.Params pR = new Runner.Params(moead, populationSize, visualization);
        pR._displayMode = DisplayMode.AT_THE_END;
        pR._updaterMode = UpdaterMode.AFTER_GENERATION;

        IRunner runner = new Runner(pR);

        // run the evolution
         try
        {
            runner.executeEvolution(generations);
        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }
    }
}
