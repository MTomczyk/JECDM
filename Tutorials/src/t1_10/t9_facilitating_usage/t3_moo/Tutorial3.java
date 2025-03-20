package t1_10.t9_facilitating_usage.t3_moo;

import color.gradient.Color;
import criterion.Criteria;
import dataset.DSFactory2D;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import emo.aposteriori.moead.MOEAD;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import frame.Frame;
import phase.DoubleConstruct;
import phase.DoubleEvaluate;
import plot.Plot2D;
import plot.Plot2DFactory;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.DoubleReproduce;
import reproduction.StandardDoubleReproducer;
import reproduction.operators.crossover.SBX;
import reproduction.operators.mutation.PM;
import reproduction.valuecheck.Wrap;
import runner.Runner;
import scheme.enums.ColorFields;
import updater.DataUpdater;
import updater.DataUpdaterFactory;
import utils.MathUtils;
import visualization.IVisualization;
import visualization.Visualization;
import visualization.updaters.sources.EASource;

/**
 * This tutorial showcases how to concisely instantiate the MOEA/D algorithm and visualize its performance.
 * It assumes that the specimens are represented as an array of doubles (decision vectors).
 * As for the optimization problem, a simple two-objective problem is defined:
 * f1 = x0*(1+sum x1,...x19),
 * f2 = (1-x0)*(1+sum x1,...x19),
 * x1,...,x19 \in [0, 1].
 *
 * @author MTomczyk
 */
public class Tutorial3
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Criteria criteria = Criteria.constructCriteria("C", 2, false); // Criteria
        IRandom R = new MersenneTwister64(0); // RGN
        DoubleConstruct.IConstruct constructor = R1 -> R1.nextDoubles(20); // initial decision vectors: random arrays of doubles (size = n)
        DoubleEvaluate.IEvaluate evaluator = v -> new double[]{v[0] * (1.0d + MathUtils.getSum(v, 1, 19)),
                (1.0d - v[0]) * (1.0d + MathUtils.getSum(v, 1, 19)),}; // evaluation function: sum of decision variable values
        StandardDoubleReproducer sdr = new StandardDoubleReproducer(new SBX(1.0d, 30.0d),
                new PM(1.0d/(double) 20, 30.0d), new Wrap(), 0.0d, 1.0d);
        DoubleReproduce.IReproduce reproducer = sdr::reproduce; // instantiates reproducer
        IGoal[] goals = GoalsFactory.getPBIsDND(2, 19, 5.0d); // instantiates goals
        MOEAD moead = MOEAD.getMOEAD(R, goals, criteria, constructor, evaluator, reproducer, new Euclidean(), 5); // instantiates MOEAD

        try
        {
            // Creates standard 2D plot and the frame:
            Plot2D plot2D = Plot2DFactory.getPlot("f1", "f2", 1.0f, 1.5f,
                    scheme -> scheme._colors.put(ColorFields.PLOT_BACKGROUND, color.Color.WHITE), null);
            Frame frame = new Frame(plot2D, 0.5f);
            // Creates standard data updater:
            DataUpdater du = DataUpdaterFactory.getSimpleDataUpdater(frame.getModel().getPlotsWrapper(),
                    new EASource(moead), DSFactory2D.getReferenceDS("MOEAD", new MarkerStyle(2.0f, Color.RED, Marker.CIRCLE)));
            // Creates the visualization object and the runner:
            IVisualization visualization = new Visualization(frame,du);
            Runner runner = new Runner(new Runner.Params(moead, 20, visualization));
            // Executes the evolution:
            runner.executeEvolution(500);

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
