package system.ds;

import alternative.Alternative;
import alternative.Alternatives;
import inconsistency.RemoveOldest;
import interaction.feedbackprovider.FeedbackProvider;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.ReferenceSetsConstructor;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.reference.constructor.PWI;
import interaction.refine.Refiner;
import interaction.trigger.InteractionTrigger;
import interaction.trigger.rules.IterationInterval;
import model.IPreferenceModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.internals.AbstractInternalModel;
import model.internals.value.scalarizing.LNorm;
import space.Range;
import space.os.ObjectiveSpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * Provides dummy data.
 *
 * @author MTomczyk
 */
public class Dummy
{
    /**
     * Getter for dummy bundles (with no models).
     *
     * @return dummy bundles
     */
    public static DMBundle[] getDMBundlesNoModels()
    {
        DMBundle[] b = new DMBundle[2];
        {
            b[0] = new DMBundle("DM1");
        }
        {
            b[1] = new DMBundle("DM2");
        }
        return b;
    }

    /**
     * Getter for dummy bundles (models cannot be initialized).
     *
     * @return dummy bundles
     */
    public static DMBundle[] getDMBundlesModelsCannotBeInitialized()
    {
        DMBundle[] b = new DMBundle[2];
        {
            b[0] = new DMBundle("DM1");
            ModelBundle<LNorm> mb = new ModelBundle<>();
            b[0]._modelBundles = new ModelBundle<?>[]{mb};

        }
        {
            b[1] = new DMBundle("DM2");
            ModelBundle<LNorm> mb = new ModelBundle<>();
            b[1]._modelBundles = new ModelBundle<?>[]{mb};
        }
        return b;
    }

    /**
     * Getter for dummy bundles (valid models).
     *
     * @return dummy bundles
     */
    public static DMBundle[] getDMBundlesModels()
    {
        DMBundle[] b = new DMBundle[2];
        {
            b[0] = new DMBundle("DM1");
            ModelBundle<LNorm> mb = new ModelBundle<>();
            mb._preferenceModel = new model.definitions.LNorm();
            FRS.Params<LNorm> pFRS = new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._feasibleSamplesToGenerate = 1000;
            pFRS._samplingLimit = 1000000;
            mb._modelConstructor = new FRS<>(pFRS);
            mb._inconsistencyHandler = new RemoveOldest<>();
            b[0]._modelBundles = new ModelBundle<?>[]{mb};


        }
        {
            b[1] = new DMBundle("DM2");
            ModelBundle<LNorm> mb = new ModelBundle<>();
            mb._preferenceModel = new model.definitions.LNorm();
            FRS.Params<LNorm> pFRS = new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._feasibleSamplesToGenerate = 1000;
            pFRS._samplingLimit = 1000000;
            mb._modelConstructor = new FRS<>(pFRS);
            mb._inconsistencyHandler = new RemoveOldest<>();
            b[1]._modelBundles = new ModelBundle<?>[]{mb};
        }
        return b;
    }

    /**
     * Getter for the interaction trigger.
     *
     * @return interaction trigger
     */
    public static InteractionTrigger getInteractionTrigger()
    {
        return new InteractionTrigger(new IterationInterval(1, 1));
    }

    /**
     * Getter for the refiner.
     *
     * @return interaction trigger
     */
    public static Refiner getRefiner()
    {
        return Refiner.getDefault();
    }

    /**
     * Getter for the reference sets constructor.
     *
     * @param p1 preference model linked to the first dm
     * @param p2 preference model linked to the second dm
     * @return reference sets constructor
     */
    public static ReferenceSetsConstructor getReferenceSetsConstructor(IPreferenceModel<? extends AbstractInternalModel> p1,
                                                                       IPreferenceModel<? extends AbstractInternalModel> p2)
    {
        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = null;
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> rsc = new LinkedList<>();
            rsc.add(new PWI(p1));
            pRSC._dmConstructors.put("DM1", rsc);
        }
        {
            LinkedList<IReferenceSetConstructor> rsc = new LinkedList<>();
            rsc.add(new PWI(p2));
            pRSC._dmConstructors.put("DM2", rsc);
        }
        return new ReferenceSetsConstructor(pRSC);
    }

    /**
     * Getter for the feedback provider.
     *
     * @return feedback provider
     */
    public static FeedbackProvider getFeedbackProvider()
    {
        IDMFeedbackProvider dmFP1 = new ArtificialValueDM<>(new model.definitions.LNorm(
                new LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY)));
        IDMFeedbackProvider dmFP2 = new ArtificialValueDM<>(new model.definitions.LNorm(
                new LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY)));
        return FeedbackProvider.getForTwoDMs("DM1", dmFP1, "DM2", dmFP2);
    }

    /**
     * Getter for the alternatives superset.
     *
     * @return alternatives superset
     */
    public static Alternatives getAlternatives()
    {
        int no = 100;
        ArrayList<Alternative> alternatives = new ArrayList<>(no);
        for (int i = 0; i < no; i++)
        {
            double x = (double) i / (double) (no - 1);
            double y = 1.0d - x;
            alternatives.add(new Alternative("A" + i, new double[]{x, y}));
        }
        return new Alternatives(alternatives);
    }

    /**
     * Getter for the objective space.
     *
     * @return objective space
     */
    public static ObjectiveSpace getObjectiveSpace()
    {
        return new ObjectiveSpace(Range.getDefaultRanges(2, 1.0d), new boolean[2]);
    }
}
