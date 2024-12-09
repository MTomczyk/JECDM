package t1_10.t4_decision_support_module.t4_decision_support_system.t1_use_case;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.DecisionSupportSystemException;
import inconsistency.RemoveOldest;
import interaction.feedbackprovider.FeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.ReferenceSetsConstructor;
import interaction.refine.Refiner;
import interaction.trigger.InteractionTrigger;
import interaction.trigger.rules.IterationInterval;
import model.constructor.random.LNormGenerator;
import model.constructor.value.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import random.IRandom;
import random.MersenneTwister64;
import space.Range;
import space.os.ObjectiveSpace;
import system.ds.DMBundle;
import system.ds.DecisionSupportSystem;
import system.ds.ModelBundle;
import system.ds.Report;

/**
 * This tutorial showcases how to create the decision support system ({@link system.ds.DecisionSupportSystem}).
 *
 * @author MTomczyk
 */
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);

        // DSS params container
        DecisionSupportSystem.Params pDSS = new DecisionSupportSystem.Params();
        pDSS._criteria = Criteria.constructCriteria("C", 2, false);
        pDSS._interactionTrigger = new InteractionTrigger(new IterationInterval(10)); // interact after every 10 interactions
        pDSS._refiner = Refiner.getDefault(); // get default refiner
        pDSS._referenceSetsConstructor = ReferenceSetsConstructor.getDefault(); // random pairs / common to all DMs
        pDSS._feedbackProvider = FeedbackProvider.getForSingleDM("DM1", new ArtificialValueDM<>( // one L-norm based DM
                new model.definitions.LNorm(new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))));

        // create model bundle
        ModelBundle<LNorm> modelBundle = new ModelBundle<>();
        modelBundle._preferenceModel = new model.definitions.LNorm();
        FRS.Params<LNorm> pFRS = new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
        pFRS._samplingLimit = 1000000;
        pFRS._feasibleSamplesToGenerate = 1000;
        modelBundle._modelConstructor = new FRS<>(pFRS);
        modelBundle._inconsistencyHandler = new RemoveOldest<>();

        // create dm bundle
        DMBundle dmBundle = new DMBundle("DM1");
        dmBundle._modelBundles = new ModelBundle<?>[]{modelBundle};

        pDSS._dmBundles = new DMBundle[]{dmBundle};

        // Create DSS
        DecisionSupportSystem DSS;
        try
        {
            DSS = new DecisionSupportSystem(pDSS);
            // Important note: use must call ``notifySystemStarts'' before using DSS's functionalities
            DSS.notifySystemStarts();

            // Example alternatives (performance vectors):
            double[][] evaluations = new double[][]{{1.0d, 0.0d}, {0.75d, 0.25d}, {0.5d, 0.5d}, {0.25d, 0.75d}, {0.0d, 1.0d}};

            // Create the context:
            DMContext.Params pDMC = new DMContext.Params();
            pDMC._currentIteration = 0;
            pDMC._currentOS = new ObjectiveSpace(new Range[]{Range.getNormalRange(), Range.getNormalRange()}, new boolean[2]);
            pDMC._osChanged = false;
            pDMC._currentAlternativesSuperset = new Alternatives(Alternative.getAlternativeArray("A", evaluations));
            pDMC._R = R;

            // Central function: executes the preference elicitation step and (if interactions were triggered) models updater module.
            Report report = DSS.executeProcess(pDMC);

            // Print the report:
            report.printStringRepresentation();

        } catch (DecisionSupportSystemException e)
        {
            throw new RuntimeException(e);
        }


    }

}
