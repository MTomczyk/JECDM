package t11_20.t16_customizing_normalization;

import color.Color;
import color.gradient.ColorPalettes;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Line;
import dataset.painter.style.enums.Marker;
import decisionsupport.operators.LNormOnSimplex;
import dmcontext.DMContext;
import ea.IEA;
import emo.interactive.StandardDSSBuilder;
import emo.interactive.iemod.IEMOD;
import emo.interactive.iemod.IEMODBuilder;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import exception.EAException;
import exception.RunnerException;
import frame.Frame;
import history.PreferenceInformationWrapper;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.RequiredSpread;
import interaction.trigger.rules.IterationInterval;
import model.constructor.Report;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.ERS;
import model.constructor.value.rs.ers.evolutionary.EvolutionaryModelConstructor;
import model.constructor.value.rs.ers.evolutionary.Tournament;
import model.constructor.value.rs.iterationslimit.IIterationsLimit;
import model.internals.value.AbstractValueInternalModel;
import model.internals.value.scalarizing.LNorm;
import model.similarity.lnorm.Euclidean;
import plot.Plot2D;
import plot.Plot2DFactory;
import print.PrintUtils;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import runner.Runner;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.ColorFields;
import selection.Random;
import space.os.ObjectiveSpace;
import visualization.updaters.sources.EASource;
import visualization.utils.ReferenceParetoFront;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial showcases how to adjust the normalization process in the IEMO/D algorithm. Note that adjusting the
 * normalization-processing in preference-learning evolutionary algorithms for multiple-objective optimization
 * is somewhat challenging.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // NOTE that adjusting the normalization-processing in preference-learning evolutionary algorithms for
        // multiple-objective optimization is somewhat challenging. For practical uses, it is probably the best strategy
        // first to approximate the Pareto front and then treat the learnt bounds as fixed. Nonetheless, this tutorial
        // develops a normalization-learning procedure that aims to address specific problems that may arise in such
        // systems (discussed later) and provides a relatively efficient and stable implementation. The simulation is
        // designed so that IEMO/D first approximates the Pareto front and then interacts with the Decision Maker to
        // learn their preferences, ultimately converging towards the most preferred solutions.

        // Let us start instantiating an IEMO/D algorithm. The parameterization is mostly rather standard. The problem
        // to be solved is DTLZ2 with 2 objectives.
        IEMODBuilder<LNorm> b = new IEMODBuilder<>(new L32_X64_MIX(0));
        DTLZBundle dtlzBundle = DTLZBundle.getBundle(
                Problem.DTLZ2, 2, 10);
        b.setCriteria(dtlzBundle._criteria);
        // Population size = 40 (determined by the number of optimization goals.
        IGoal[] goals = GoalsFactory.getLNormsDND(2, 39, Double.POSITIVE_INFINITY, dtlzBundle._normalizations);
        b.setGoals(goals);
        b.setSimilarity(new emo.utils.decomposition.similarity.lnorm.Euclidean());
        b.setNeighborhoodSize(10);
        b.setParentsSelector(new Random());
        b.setInitialPopulationConstructor(dtlzBundle._construct); // set the initial population constructor
        b.setSpecimensEvaluator(dtlzBundle._evaluate); // set specimens evaluator
        b.setParentsReproducer(dtlzBundle._reproduce);

        // Let us turn on the dynamic OS-bounds learning.
        b.setDynamicOSBoundsLearningPolicy();
        b.setOSMParamsAdjuster(p -> {
            // In interactive preference-learning methods it is essential to use utopia and nadir incumbents when
            // learning the bounds. The reason is complex and refers to the fact that the preference-learning methods
            // implemented in JECDM are designed to execute preference-learning (or, in this context, re-learning,
            // as it does not concern any newly provided feedback) each time the method detects a change in the known
            // bounds in the objective (or Pareto) space. The result of such re-learning when applied to the optimizer
            // may change the course of optimization, which in turn may quickly trigger another re-learning.
            // For instance, imagine that the Decision Maker's feedback indicates that they prefer "middle" Pareto
            // optimal solutions. After making adjustments and performing convergence (focused search), the known
            // bounds could also become more focused if the incumbents are not used. Therefore, after re-learning,
            // the method would focus the search "in the middle of the previous middle", and so on, falling into
            // a vicious cycle. Therefore, using incumbents or other means of preserving information on the true PF
            // bounds is essential.
            p._updateNadirUsingIncumbent = true;
            p._updateUtopiaUsingIncumbent = true;
        });

        StandardDSSBuilder<LNorm> bD = new StandardDSSBuilder<>();
        bD.setPreferenceModel(new model.definitions.LNorm());
        bD.setInteractionRule(new IterationInterval(200, 20, 10)); // define the interaction
        // rule (starting from 200th generation, after every 20 generations, assume the limit of 10 interactions)
        bD.setReferenceSetConstructor(new RandomPairs(new RequiredSpread(1.0E-6)));
        bD.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                // NOTE THAT the artificial DM used to provide feedback is modelled using the true PF-bounds, i.e.,
                // normalization functions delivered via the bundle.
                new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY, dtlzBundle._normalizations))));

        ERS.Params<LNorm> pERS = new ERS.Params<>(
                // NOTE THAT various preference-learning-related components can be defined without any knowledge of the
                // OS-bounds (normalizations are null). The reason is that when the method is set to learn the OS bounds
                // dynamically, during each successful update, it propagates the knowledge of the new OS bounds
                // (and thus, normalizations) to all interested components, even the models maintained by ERS (this is
                // how the methods in this framework are designed).
                new LNormGenerator(2, Double.POSITIVE_INFINITY, null));
        pERS._similarity = new Euclidean();
        pERS._feasibleSamplesToGenerate = goals.length;
        pERS._inconsistencyThreshold = goals.length - 1;
        // The two fields below are essential in this example. The first indicates that when learning starts, the ERS
        // algorithm must first verify the compatibility of already maintained models. The second are the initially
        // supplied models, which are consistent with the optimizer's provided optimization goals used to steer
        // the evolution. Such a configuration will already provide a good start to ERS during the optimization phase
        // when the method is expected to approximate the Pareto front.
        pERS._validateAlreadyExistingSamplesFirst = true;
        pERS._initialModels = new LNorm[goals.length];
        for (int i = 0; i < goals.length; i++)
            pERS._initialModels[i] = new LNorm(goals[i].getParams()[0], Double.POSITIVE_INFINITY, null);

        pERS._EMC = new EvolutionaryModelConstructor<>(
                new LNormOnSimplex(Double.POSITIVE_INFINITY, 0.2d, 0.2d),
                new Tournament<>(2));

        // In this tutorial, we will specify a contextual limit for the number of iterations to perform by ERS during
        // preference learning:
        pERS._iterationsLimit = new IIterationsLimit()
        {
            @Override
            public <T extends AbstractValueInternalModel> int getIterations(DMContext dmContext,
                                                                            LinkedList<PreferenceInformationWrapper> preferenceInformation,
                                                                            Report<T> report, int N)
            {
                // The Decision-Making context now provides a reason for running the Decision Support System.
                // To make the implementation reasonably efficient, we will set different iteration limits depending
                // on the reason:
                // -- If the reason for running the system was the triggered update in the known OS-bounds (OS_CHANGED
                // flag), we will allow for a limited number of iterations because no new feedback was received; so
                // there is not much to learn
                // -- For any other reason that could trigger the interaction (e.g., REGULAR_ITERATION),
                // we will provide greater computational resources:
                if (dmContext.isReasonForRunning(DMContext.Reason.OS_CHANGED))
                {
                    // We wll split this case into two sub-cases:
                    if (dmContext.getCurrentIteration() < 200) return 0; // Before the method was allowed to interact;
                    // thus, it approximates the PF. In that case, ERS is not allowed to perform any iteration, and
                    // its initial model set will be considered the result of preference-learning (exactly the same
                    // optimization goals that were initially coupled with the optimizer).

                    return 10000; // This case concerns generations >= 200, so some interactions with the decision-maker
                    // could already have been performed; thus, the method could have already narrowed the search.
                    // Nonetheless, the context states that re-learning is triggered due to change in the known
                    // OS-bounds. Yet, such a change can be minor; thus, the models already maintained by ERS  may
                    // be relatively satisfactory. Therefore, a limited number of iterations is permitted.
                }
                else
                {
                    // This case refers to an iteration when the Decision Maker provided new feedback. A more demanding
                    // learning must thus be performed:
                    return 250000;
                }
            }
        };

        bD.setModelConstructor(new ERS<>(pERS));
        b.setStandardDSSBuilder(bD);


        // For convenience, this tutorial visualized the optimization process. The plot includes three data sets:
        // - the current population
        // - the known OS bounds
        // - the reference Pareto front
        Plot2D plot2D = Plot2DFactory.getPlot("f1", "f2", 3.0f, 1.2f,
                scheme -> {
                    scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE);
                    scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
                },
                pP -> pP._drawLegend = true);
        Frame frame = new Frame(plot2D, 1000, 1000);
        frame.setVisible(true);

        try
        {
            IEMOD iemod = b.getInstance();
            Runner runner = new Runner(iemod, goals.length);
            runner.init();
            draw(iemod, plot2D);

            for (int i = 1; i < 500; i++)
            {
                System.out.println("Generation = " + i);
                runner.executeSingleGeneration(i, null);
                draw(iemod, plot2D);
            }

        } catch (EAException | RunnerException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Auxiliary method for executing plotting.
     *
     * @param ea     evolutionary algorithm
     * @param plot2D plot
     */
    private static void draw(IEA ea, Plot2D plot2D)
    {
        ArrayList<IDataSet> dataSets = new ArrayList<>();
        EASource eaSource = new EASource(ea);
        double[][] data = eaSource.createData();
        dataSets.add(ReferenceParetoFront.getConcaveSpherical2DPF(1.0f,
                new LineStyle(0.5f, ColorPalettes.getFromDefaultPalette(1), Line.REGULAR)));
        dataSets.add(DSFactory2D.getDS("IEMO/D", data, new MarkerStyle(1.0f,
                ColorPalettes.getFromDefaultPalette(0), Marker.CIRCLE)));
        double[][] rect = new double[5][2];
        ObjectiveSpace os = ea.getObjectiveSpaceManager().getOS();
        if (os != null)
        {
            System.out.println("Utopia = " + PrintUtils.getVectorOfDoubles(os._utopia, 6));
            System.out.println("Nadir = " + PrintUtils.getVectorOfDoubles(os._nadir, 6));
            rect[0] = os._utopia.clone();
            rect[1][0] = os._utopia[0];
            rect[1][1] = os._nadir[1];
            rect[2] = os._nadir.clone();
            rect[3][0] = os._nadir[0];
            rect[3][1] = os._utopia[1];
            rect[4] = os._utopia.clone();
            dataSets.add(DSFactory2D.getDS("OS bounds", rect, null,
                    new LineStyle(1.0f, color.gradient.Color.BLACK, Line.REGULAR),
                    false));
        }
        plot2D.getModel().setDataSets(dataSets, true, true);
    }
}
