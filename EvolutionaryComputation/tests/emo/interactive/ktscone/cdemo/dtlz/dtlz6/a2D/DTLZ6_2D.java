package emo.interactive.ktscone.cdemo.dtlz.dtlz6.a2D;

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
 * Solving DTLZ problem (2D) using CDEMO.
 * The visualization module is run to present the results.
 *
 * @author MTomczyk
 */

public class DTLZ6_2D
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
        Problem problem = Problem.DTLZ6;
        int noDecisionVariables = 5;
        DTLZBundle problemBundle = DTLZBundle.getBundle(problem, 2, noDecisionVariables);

        boolean dynamicObjectiveRanges = false;
        Criteria criteria = Criteria.constructCriteria("C", 2, false);

        EA CDEMO = emo.interactive.Utils.getCDEMO(criteria, populationSize, problemBundle, dynamicObjectiveRanges, R,
                new IterationInterval(200, 100), new RandomPairs(),
                emo.interactive.Utils.getDefaultDMFeedbackProvider2D(problemBundle._normalizations));
        // create visualization module
        Plot2D plot = Utils.getPlot2D(problemBundle._displayRanges, false);
        Frame frame = new Frame(plot, 0.5f, 0.5f);

        // create updater
        DataSet ds = DataSet.getFor2D("CDEMO", new MarkerStyle(2.0f, color.gradient.Color.RED, Marker.CIRCLE));
        IVisualization visualization = Utils.getVisualization(frame, CDEMO, ds, false);

        // create runner object
        Runner.Params pR = new Runner.Params(CDEMO, visualization);
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
