package emo.interactive.ktscone.cdemo.wfg.wfg1.a3D;

import color.gradient.Gradient;
import criterion.Criteria;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import ea.EA;
import emo.Utils;
import exception.RunnerException;
import frame.Frame;
import interaction.reference.constructor.RandomPairs;
import interaction.trigger.rules.IterationInterval;
import plot.Plot3D;
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
 * Solving DTLZ problem (3D) using CDEMO.
 * The visualization module is run to present the results.
 *
 * @author MTomczyk
 */

public class WFG1_3D_Cumulative
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
        int generations = 1000;

        // create problem bundle
        Problem problem = Problem.WFG1;
        WFGEvaluate evaluate = new WFG1(3, 4, 4, 0.2d);
        WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 3, 4, 4);
        //Problem problem = Problem.WFG1EASY;
        //WFGEvaluate evaluate = new WFG1Easy(3);
        //WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 3, 2, 1, R);

        boolean dynamicObjectiveRanges = false;
        Criteria criteria = Criteria.constructCriteria("C", 3, false);

        EA cdemo = emo.interactive.Utils.getCDEMO(criteria, populationSize, problemBundle, dynamicObjectiveRanges, R,
                new IterationInterval(200, 100), new RandomPairs(),
                emo.interactive.Utils.getDefaultDMFeedbackProvider3D(problemBundle._normalizations));
        // create visualization module
        Plot3D plot = Utils.getPlot3D(problemBundle._displayRanges, true);
        Frame frame = new Frame(plot, 0.5f, 0.5f);

        // create updater
        DataSet ds = DataSet.getFor3D("CDEMO", new MarkerStyle(0.02f, Gradient.getViridisGradient(), 3, Marker.CUBE_3D));
        IVisualization visualization = Utils.getVisualization(frame, cdemo, ds, true);

        // create runner object
        Runner.Params pR = new Runner.Params(cdemo, visualization);
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
