package system.modules.elicitation;

import exeption.DecisionMakerSystemException;
import exeption.ModelSystemException;
import exeption.ModuleException;
import inconsistency.RemoveOldest;
import interaction.feedbackprovider.FeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.ReferenceSetsConstructor;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.refine.Refiner;
import interaction.trigger.InteractionTrigger;
import interaction.trigger.rules.IterationInterval;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.definitions.LNorm;
import org.junit.jupiter.api.Test;
import system.dm.DM;
import system.dm.DecisionMakerSystem;
import system.model.ModelSystem;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Provides various tests for {@link PreferenceElicitationModule}.
 *
 * @author MTomczyk
 */
class PreferenceElicitationModule1Test
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        String msg = null;
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The interaction trigger is not provided", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));


        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        String msg = null;
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The refiner is not provided", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        String msg = null;
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The reference sets constructor is not provided", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new DummyConstructor(0, 1));
        pP._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        String msg = null;
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The feedback provider is not provided", msg);
    }


    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new DummyConstructor(0, 1));
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(2, 3));
            pRSC._dmConstructors.put(dms[0].getName(), dmRSC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(4, 5));
            pRSC._dmConstructors.put(dms[1].getName(), dmRSC);
        }

        pP._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        pP._feedbackProvider = FeedbackProvider.getForTwoDMs(
                dms[0].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY))),
                dms[1].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY)))
                );

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        String msg = null;
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker system(s) is (are) not provided (the array is null)", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new DummyConstructor(0, 1));
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(2, 3));
            pRSC._dmConstructors.put(dms[0].getName(), dmRSC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(4, 5));
            pRSC._dmConstructors.put(dms[1].getName(), dmRSC);
        }

        pP._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        pP._feedbackProvider = FeedbackProvider.getForTwoDMs(
                dms[0].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY))),
                dms[1].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY)))
        );

        pP._DMSs = new DecisionMakerSystem[]{};

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        String msg = null;
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker system(s) is (are) not provided (the array is empty)", msg);
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new DummyConstructor(0, 1));
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(2, 3));
            pRSC._dmConstructors.put(dms[0].getName(), dmRSC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(4, 5));
            pRSC._dmConstructors.put(dms[1].getName(), dmRSC);
        }

        pP._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        pP._feedbackProvider = FeedbackProvider.getForTwoDMs(
                dms[0].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY))),
                dms[1].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY)))
        );

        String msg = null;

        pP._DMSs = new DecisionMakerSystem[1];
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[0];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>();
            pMS._modelConstructor = new FRS<>(new FRS.Params<>(new LNormGenerator(2 , Double.POSITIVE_INFINITY)));
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[0] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }

            assertNull(msg);
        }

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker identifier(s) is (are) not provided (the array is null)", msg);
    }


    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new DummyConstructor(0, 1));
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(2, 3));
            pRSC._dmConstructors.put(dms[0].getName(), dmRSC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(4, 5));
            pRSC._dmConstructors.put(dms[1].getName(), dmRSC);
        }

        pP._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        pP._feedbackProvider = FeedbackProvider.getForTwoDMs(
                dms[0].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY))),
                dms[1].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY)))
        );

        String msg = null;

        pP._DMs = new DM[0];
        pP._DMSs = new DecisionMakerSystem[1];
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[0];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>();
            pMS._modelConstructor = new FRS<>(new FRS.Params<>(new LNormGenerator(2 , Double.POSITIVE_INFINITY)));
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[0] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }

            assertNull(msg);
        }

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker identifier(s) is (are) not provided (the array is empty)", msg);
    }

    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new DummyConstructor(0, 1));
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(2, 3));
            pRSC._dmConstructors.put(dms[0].getName(), dmRSC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(4, 5));
            pRSC._dmConstructors.put(dms[1].getName(), dmRSC);
        }

        pP._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        pP._feedbackProvider = FeedbackProvider.getForTwoDMs(
                dms[0].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY))),
                dms[1].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY)))
        );

        String msg = null;

        pP._DMs = new DM[1];
        pP._DMs[0] = new DM(11, "TEST");
        pP._DMSs = new DecisionMakerSystem[1];
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[0];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>();
            pMS._modelConstructor = new FRS<>(new FRS.Params<>(new LNormGenerator(2 , Double.POSITIVE_INFINITY)));
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[0] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }

            assertNull(msg);
        }

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker system(s) does (do) not match the decision maker identifier (s)", msg);
    }


    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new DummyConstructor(0, 1));
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(2, 3));
            pRSC._dmConstructors.put(dms[0].getName(), dmRSC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(4, 5));
            pRSC._dmConstructors.put(dms[1].getName(), dmRSC);
        }

        pP._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        pP._feedbackProvider = FeedbackProvider.getForTwoDMs(
                dms[0].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY))),
                dms[1].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY)))
        );

        String msg = null;

        pP._DMs = dms;
        pP._DMSs = new DecisionMakerSystem[1];
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[0];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>();
            pMS._modelConstructor = new FRS<>(new FRS.Params<>(new LNormGenerator(2 , Double.POSITIVE_INFINITY)));
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[0] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }

            assertNull(msg);
        }

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker system(s) does (do) not match the decision maker identifier (s)", msg);
    }


    /**
     * Test 11.
     */
    @Test
    void test11()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new DummyConstructor(0, 1));
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(2, 3));
            pRSC._dmConstructors.put(dms[0].getName(), dmRSC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(4, 5));
            pRSC._dmConstructors.put(dms[1].getName(), dmRSC);
        }

        pP._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        pP._feedbackProvider = FeedbackProvider.getForTwoDMs(
                dms[0].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY))),
                dms[1].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY)))
        );

        String msg = null;

        pP._DMs = dms;
        pP._DMSs = new DecisionMakerSystem[2];
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[0];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>();
            pMS._modelConstructor = new FRS<>(new FRS.Params<>(new LNormGenerator(2 , Double.POSITIVE_INFINITY)));
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[0] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }

            assertNull(msg);
        }
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[1];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>();
            pMS._modelConstructor = new FRS<>(new FRS.Params<>(new LNormGenerator(2 , Double.POSITIVE_INFINITY)));
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[1] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }
}