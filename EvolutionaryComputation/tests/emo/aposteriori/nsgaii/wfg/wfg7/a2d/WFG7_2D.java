package emo.aposteriori.nsgaii.wfg.wfg7.a2d;

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
import problem.moo.wfg.evaluate.WFG7;
import problem.moo.wfg.evaluate.WFGEvaluate;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import visualization.IVisualization;

/**
 * Solving WFG problem (2D) using NSGA-II.
 * The visualization module is run to present the results.
 *
 * @author MTomczyk
 */

public class WFG7_2D
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
        Problem problem = Problem.WFG7;
        WFGEvaluate evaluate = new WFG7(2, 4, 4);
        WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 2, 4, 4);
        //Problem problem = Problem.WFG7EASY;
        //WFGEvaluate evaluate = new WFG7Easy(2);
        //WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 2, 1, 1, R);

        boolean dynamicObjectiveRanges = false;
        Criteria criteria = Criteria.constructCriteria("C", 2, false);

        EA nsgaii = Utils.getNSGAII(criteria, problemBundle, dynamicObjectiveRanges, R, populationSize, offspringSize);

        // create visualization module
        Plot2D plot = emo.Utils.getPlot2D(problemBundle._displayRanges, false);
        Frame frame = new Frame(plot, 0.5f, 0.5f);

        // create updater
        DataSet ds = DataSet.getFor2D("NSGA-II", new MarkerStyle(2.0f, color.gradient.Color.RED, Marker.CIRCLE));
        IVisualization visualization = emo.Utils.getVisualization(frame, nsgaii, ds, false);

        // create runner object
        Runner.Params pR = new Runner.Params(nsgaii, visualization);
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
