package emo.aposteriori.nsga.wfg.wfg1.a2d;

import color.gradient.Gradient;
import criterion.Criteria;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import ea.EA;
import emo.aposteriori.Utils;
import exception.RunnerException;
import frame.Frame;
import plot.Plot2D;
import problem.Problem;
import problem.moo.wfg.WFGBundle;
import problem.moo.wfg.evaluate.WFG1;
import problem.moo.wfg.evaluate.WFGEvaluate;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import visualization.IVisualization;

/**
 * Solving WFG problem (2D) using NSGA.
 * The visualization module is run to present the results.
 *
 * @author MTomczyk
 */

public class WFG1_2D_Cumulative
{
    /**
     * Runs evolutionary algorithm.
     *
     * @param args (not used)
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);

        int populationSize = 200;
        int offspringSize = 200;
        int generations = 1000;

        // create problem bundle
        Problem problem = Problem.WFG1;
        WFGEvaluate evaluate = new WFG1(2, 4, 4, 0.2d);
        WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 2, 4, 4);
        //Problem problem = Problem.WFG1EASY;
        //WFGEvaluate evaluate = new WFG1Easy(2);
        //WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 2, 1, 1, R);

        boolean dynamicObjectiveRanges = false;
        Criteria criteria = Criteria.constructCriteria("C", 2, false);

        EA nsga = Utils.getNSGA(criteria, problemBundle, dynamicObjectiveRanges, R, populationSize, offspringSize, 0.2d);

        // create visualization module
        Plot2D plot = emo.Utils.getPlot2D(problemBundle._displayRanges, true);
        Frame frame = new Frame(plot, 0.5f, 0.5f);

        // create updater
        DataSet ds = DataSet.getFor2D("NSGA", new MarkerStyle(2.0f, Gradient.getViridisGradient(), 2, Marker.CIRCLE));
        IVisualization visualization = emo.Utils.getVisualization(frame, nsga, ds, true);

        // create runner object
        Runner.Params pR = new Runner.Params(nsga, visualization);
        pR._displayMode = DisplayMode.FROM_THE_BEGINNING;
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
