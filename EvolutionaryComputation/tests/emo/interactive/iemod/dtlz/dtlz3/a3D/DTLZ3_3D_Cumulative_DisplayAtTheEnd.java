package emo.interactive.iemod.dtlz.dtlz3.a3D;

import color.gradient.Gradient;
import criterion.Criteria;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import ea.EA;
import emo.interactive.Utils;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import exception.RunnerException;
import frame.Frame;
import interaction.reference.constructor.RandomPairs;
import interaction.trigger.rules.IterationInterval;
import model.definitions.LNorm;
import plot.Plot3D;
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
 * Solving DTLZ problem (3D) using IEMO/D.
 * The visualization module is run to present the results.
 *
 * @author MTomczyk
 */

public class DTLZ3_3D_Cumulative_DisplayAtTheEnd
{
    /**
     * Runs evolutionary algorithm.
     *
     * @param args (not used)
     */
    public static void main(String[] args)
    {
        // Sets DPI scaling to 1
        System.setProperty("sun.java2d.uiScale", "1");

        IRandom R = new MersenneTwister64(0);

        // create problem bundle
        Problem problem = Problem.DTLZ3;
        int noDecisionVariables = 5;
        DTLZBundle problemBundle = DTLZBundle.getBundle(problem, 3, noDecisionVariables);

        IGoal [] goals = GoalsFactory.getLNormsDND(3, 15, Double.POSITIVE_INFINITY, problemBundle._normalizations);
        int populationSize = goals.length;
        int generations = 1000;

        boolean dynamicObjectiveRanges = false;
        Criteria criteria = Criteria.constructCriteria("C", 3, false);

        EA iemod = Utils.getIEMOD(criteria, problemBundle, dynamicObjectiveRanges, R, goals,
                new Euclidean(), new IterationInterval(200, 100), new RandomPairs(),
                Utils.getDefaultDMFeedbackProvider3D(problemBundle._normalizations), new LNorm(),
                Utils.getModelConstructor(populationSize, 10000000, 3)
        );

        // create visualization module
        Plot3D plot = emo.Utils.getPlot3D(problemBundle._displayRanges, true);
        Frame frame = new Frame(plot, 0.5f, 0.5f);

        // create updater
        DataSet ds = DataSet.getFor3D("IEMO/D",  new MarkerStyle(0.02f, Gradient.getViridisGradient(), 3, Marker.SPHERE_LOW_POLY_3D));
        IVisualization visualization = emo.Utils.getVisualization(frame, iemod, ds, true);

        // create runner object
        Runner.Params pR = new Runner.Params(iemod, populationSize, visualization);
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
