package emo.aposteriori.nsgaiii.wfg.wfg8.a3d;

import criterion.Criteria;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import ea.EA;
import emo.aposteriori.Utils;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import exception.RunnerException;
import frame.Frame;
import plot.Plot3D;
import problem.Problem;
import problem.moo.wfg.WFGBundle;
import problem.moo.wfg.evaluate.WFG8;
import problem.moo.wfg.evaluate.WFGEvaluate;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import visualization.IVisualization;

/**
 * Solving WFG problem (3D) using NSGA-III.
 * The visualization module is run to present the results.
 *
 * @author MTomczyk
 */

public class WFG8_3D
{
    /**
     * Runs evolutionary algorithm.
     *
     * @param args (not used)
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);

        int generations = 1000;

        // create problem bundle
        Problem problem = Problem.WFG8;
        WFGEvaluate evaluate = new WFG8(3, 2, 4);
        WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 3, 2, 4);
        //Problem problem = Problem.WFG8EASY;
        //WFGEvaluate evaluate = new WFG8Easy(3);
        //WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 3, 2, 1, R);
        IGoal [] goals =  GoalsFactory.getPointLineProjectionsDND(3, 15, problemBundle._normalizations);

        boolean dynamicObjectiveRanges = false;
        Criteria criteria = Criteria.constructCriteria("C", 3, false);

        EA nsgaiii = Utils.getNSGAIII(criteria, problemBundle, dynamicObjectiveRanges, R, goals);

        // create visualization module
        Plot3D plot = emo.Utils.getPlot3D(problemBundle._displayRanges, false);
        Frame frame = new Frame(plot, 0.5f, 0.5f);

        // create updater
        DataSet ds = DataSet.getFor3D("NSGA-III", new MarkerStyle(0.02f, color.gradient.Color.RED, Marker.SPHERE_LOW_POLY_3D));
        IVisualization visualization = emo.Utils.getVisualization(frame, nsgaiii, ds, false);

        // create runner object
        Runner.Params pR = new Runner.Params(nsgaiii, visualization);
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
