package emo.aposteriori.nsga.dtlz.dtlz1.b3D;

import criterion.Criteria;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import ea.EA;
import emo.aposteriori.Utils;
import exception.RunnerException;
import frame.Frame;
import org.junit.jupiter.api.Test;
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
 * Solving DTLZ problem (3D) using NSGA.
 * The visualization module is run to present the results.
 *
 * @author MTomczyk
 */

public class DTLZ1_3D
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        (new DTLZ1_3D()).test1();
    }

    /**
     * Tests the method.
     */
    @Test
    public void test1()
    {
        IRandom R = new MersenneTwister64(0);

        int populationSize = 200;
        int offspringSize = 200;
        int generations = 4000;

        // create problem bundle
        Problem problem = Problem.DTLZ1;
        int noDecisionVariables = 5;
        DTLZBundle problemBundle = DTLZBundle.getBundle(problem, 3, noDecisionVariables);

        boolean dynamicObjectiveRanges = false;
        Criteria criteria = Criteria.constructCriteria("C", 3, false);

        // create algorithm bundle
        EA nsga = Utils.getNSGA(criteria, problemBundle, dynamicObjectiveRanges, R, populationSize, offspringSize, 0.2d);

        // create visualization module
        Plot3D plot = emo.Utils.getPlot3D(problemBundle._displayRanges, false);
        Frame frame = new Frame(plot, 0.5f, 0.5f);


        // create updater
        DataSet ds = DataSet.getFor3D("NSGA", new MarkerStyle(0.01f, color.gradient.Color.RED, Marker.SPHERE_LOW_POLY_3D));
        IVisualization visualization = emo.Utils.getVisualization(frame, nsga, ds, false);

        // create runner object
        Runner.Params pR = new Runner.Params(nsga, visualization);
        pR._displayMode = DisplayMode.FROM_THE_BEGINNING;
        pR._updaterMode = UpdaterMode.AFTER_GENERATION;

        IRunner runner = new Runner(pR);

        // run the evolution
        try
        {
            runner.executeEvolution(generations);
            Thread.sleep(100);
            runner.dispose();
        } catch (RunnerException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
