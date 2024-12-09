package emo.interactive.nemo.nemo0.wfg.wfg5.a2D;

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
import model.definitions.LNorm;
import plot.Plot2D;
import problem.Problem;
import problem.moo.wfg.WFGBundle;
import problem.moo.wfg.evaluate.WFG5;
import problem.moo.wfg.evaluate.WFGEvaluate;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import visualization.IVisualization;

/**
 * Solving DTLZ problem (2D) using NEMO0.
 * The visualization module is run to present the results.
 *
 * @author MTomczyk
 */

public class WFG5_2D_Cumulative
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
        Problem problem = Problem.WFG5;
        WFGEvaluate evaluate = new WFG5(2, 4, 4);
        WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 2, 4, 4);
        //Problem problem = Problem.WFG5EASY;
        //WFGEvaluate evaluate = new WFG5Easy(2);
        //WFGBundle problemBundle = WFGBundle.getBundle(problem, evaluate, 2, 1, 1, R);

        boolean dynamicObjectiveRanges = false;
        Criteria criteria = Criteria.constructCriteria("C", 2, false);

        EA NEMO0 = emo.interactive.Utils.getNEMO0(criteria, populationSize, problemBundle, dynamicObjectiveRanges, R,
                new IterationInterval(200, 2, 5), new RandomPairs(),
                emo.interactive.Utils.getDefaultDMFeedbackProvider2D(problemBundle._normalizations),
                new LNorm(new model.internals.value.scalarizing.LNorm(Double.POSITIVE_INFINITY)),
                emo.interactive.Utils.getRepresentativeModelConstructor(populationSize, 1000000, criteria._no));

        // create visualization module
        Plot2D plot = Utils.getPlot2D(problemBundle._displayRanges, true);
        Frame frame = new Frame(plot, 0.5f, 0.5f);

        // create updater
        DataSet ds = DataSet.getFor2D("NEMO0", new MarkerStyle(2.0f, Gradient.getViridisGradient(), 2, Marker.CIRCLE));
        IVisualization visualization = Utils.getVisualization(frame, NEMO0, ds, true);

        // create runner object
        Runner.Params pR = new Runner.Params(NEMO0, visualization);
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
