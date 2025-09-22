package t11_20.t16_customizing_normalization;

import emo.aposteriori.nsgaii.NSGAII;
import emo.aposteriori.nsgaii.NSGAIIBuilder;
import exception.EAException;
import exception.RunnerException;
import os.IOSChangeListener;
import population.ISpecimenGetter;
import population.OffspringGetter;
import population.PopulationGetter;
import population.Specimen;
import print.PrintUtils;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import runner.Runner;
import selection.Tournament;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import java.util.ArrayList;

/**
 * This tutorial showcases how to adjust the normalization process in the NSGA-II algorithm.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        NSGAIIBuilder b = new NSGAIIBuilder(new L32_X64_MIX(0));
        DTLZBundle dtlzBundle = DTLZBundle.getBundle(
                Problem.DTLZ2, 2, 10);
        b.setCriteria(dtlzBundle._criteria);
        b.setPopulationSize(100);
        b.setParentsSelector(new Tournament(2));
        b.setInitialPopulationConstructor(dtlzBundle._construct);
        b.setSpecimensEvaluator(dtlzBundle._evaluate);
        b.setParentsReproducer(dtlzBundle._reproduce);

        // CASE 1: This scenario assumes fixed data on the relevant (e.g., Pareto) objective space bounds.
        // The utopia and nadir points are set to [0.0, 0.0] and [1.0, 1.0]. Note that when both points are provided,
        // the initial and fixed ObjectiveSpace object will be instantiated and coupled with the ObjectiveSpaceManager.
        // Otherwise, the objective space will be considered null.
        /*{
            b.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations, dtlzBundle._utopia, dtlzBundle._nadir);
        }*/

        // CASE 2: This case assumes dynamically learning the objective space bounds without any initial data.
        /*{
            b.setDynamicOSBoundsLearningPolicy();
        }*/

        // CASE 3: This case assumes dynamic learning with utopia and nadir points provided. As with CASE 1,
        // the provision of both will ensure the creation of an initial ObjectiveSpace object with these two points.
        // While the initial utopia is set to [0.0, 0.0], the nadir is set to an infinite vector. Nonetheless, it will
        // be quickly updated just after creating and evaluating the initial population (see the following code):
        /*{
            b.setDynamicOSBoundsLearningPolicy(null, new double[]{0.0d, 0.0d},
                    new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY});
            // By using a dedicated adjuster, we may further customize the objective space manager maintained by the
            // algorithm:
            b.setOSMParamsAdjuster(p -> {
                // we will assume that in each iteration the data on the utopia point will be updated using the
                // additional incumbent point; i.e., the method will maintain a utopia consisting of the best objective
                // values found so far; since the initial point is set to [0.0, 0.0], which is also a true utopia for
                // DTLZ2, no further change is expected in this regard:
                p._updateUtopiaUsingIncumbent = true;
                //  In contrast, we will not use a nadir incumbent. It means that, by default, the nadir will always be
                //  determined using the current population and offspring:
                p._updateNadirUsingIncumbent = false;
            });
        }*/

        // CASE 4: Similar case to CASE 3, but uses opposite assumptions regarding the utopia and nadir. Thus, nadir is
        // not expected to change in this example (but could, if some solution would exceed its coordinates), but the
        // nadir will always be determined based on the current population and offspring:
        /*{
            b.setDynamicOSBoundsLearningPolicy(null, new double[]{0.0d, 0.0d},
                    new double[]{10.0d, 10.0d});
            b.setOSMParamsAdjuster(p -> {
                p._updateUtopiaUsingIncumbent = false;
                p._updateNadirUsingIncumbent = true;
            });
        }*/

        // CASE 5: This case assumes providing initial utopia and nadir and using their incumbents. Note that the
        // initial utopia is set to [0.2, 0.2]. It means that there is some space left for improvement. The following
        // code additionally notifies (prints to console) when there was an update in the data on the objective space
        // in the most recent generation.
        /*{
            b.setDynamicOSBoundsLearningPolicy(null, new double[]{0.2d, 0.2d},
                    new double[]{1.0d, 1.0d});
            b.setOSMParamsAdjuster(p -> {
                p._updateUtopiaUsingIncumbent = true;
                p._updateNadirUsingIncumbent = true;
                // We will add an additional-to-the-method-specific OS-changed listener to the manager. These listeners
                // are triggered when there is an update on the known data in the relevant part of the objective space:
                p._listeners = new IOSChangeListener[]{
                        (ea, os, prevOS) ->
                                System.out.println("Data on the PF bounds updated in "
                                        + ea.getCurrentGeneration() + " generation")
            };});
        }*/

        // CASE 6: The last case showcases that the OSManager has the potential to be coupled with an external archive.
        // The example assumes dynamic learning without any initial data. Also, using incumbents is disabled. However,
        // the code specifies "specimen getters" that serve as data providers when OSManager starts processing. The
        // PopulationGetter and OffspringGetter are regular (default) getters that request the manager to examine the
        // Pareto front bounds using the current population and offspring. This code, however, adds a custom provider
        // that delivers two pre-fixed dummy performance vectors [0.0, 0.0] and [5.0, 5.0]. Since the first point
        // cannot be improved and the second worsened (in the assumed problem definition), they will effectively serve
        // as a pre-fixed utopia and nadir.
        /*{
            b.setDynamicOSBoundsLearningPolicy();
            b.setOSMParamsAdjuster(p -> {
                p._updateUtopiaUsingIncumbent = false;
                p._updateNadirUsingIncumbent = false;
                p._specimenGetters = new ISpecimenGetter[]{
                  new PopulationGetter(),
                  new OffspringGetter(),
                        container -> {
                            ArrayList<Specimen> specimen = new ArrayList<>();
                            specimen.add(new Specimen(new double[]{0.0d, 0.0d}));
                            specimen.add(new Specimen(new double[]{5.0d, 5.0d}));
                            return specimen;
                        }
                };
            });
        }*/

        // CASE 7: This case assumes dynamically learning the objective space bounds without any initial data.
        {
            b.setDynamicOSBoundsLearningPolicy();
            b.setNSGAIIParamsAdjuster(p -> p._normalizationBuilder = OS -> {
                System.out.println("Objective space is...");
                System.out.println(OS);
                INormalization[] normalizations = new INormalization[]{
                        new Linear(0.0d, 1.0d),
                        new Linear(0.0d, 2.0d)
                };
                System.out.println("But the normalizations are...");
                for (INormalization n : normalizations) System.out.println(n);
                return normalizations;
            });
        }

        try
        {
            NSGAII nsgaii = b.getInstance();
            Runner runner = new Runner(nsgaii);
            runner.executeEvolution(500);
            for (Specimen s : nsgaii.getSpecimensContainer().getPopulation())
                PrintUtils.printVectorOfDoubles(s.getPerformanceVector(), 8);

            // The below code prints data on the objective space:
            System.out.println();
            nsgaii.getObjectiveSpaceManager().getOS().print();
        } catch (EAException | RunnerException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
