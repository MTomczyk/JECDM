package system.modules.updater;


import exeption.DecisionMakerSystemException;
import exeption.ModelSystemException;
import exeption.ModuleException;
import inconsistency.RemoveOldest;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.definitions.LNorm;
import org.junit.jupiter.api.Test;
import system.dm.DM;
import system.dm.DecisionMakerSystem;
import system.model.ModelSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Provides various tests for {@link ModelsUpdaterModule}.
 *
 * @author MTomczyk
 */
class ModelsUpdaterModule1Test
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();
        ModelsUpdaterModule MUM = new ModelsUpdaterModule(pP);

        String msg = null;
        try
        {
            MUM.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker system(s) is (are) not provided (the array is null)", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();
        pP._DMSs = new DecisionMakerSystem[0];


        ModelsUpdaterModule MUM = new ModelsUpdaterModule(pP);

        String msg = null;
        try
        {
            MUM.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker system(s) is (are) not provided (the array is empty)", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();
        pP._DMSs = new DecisionMakerSystem[1];

        ModelsUpdaterModule MUM = new ModelsUpdaterModule(pP);

        String msg = null;
        try
        {
            MUM.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("One of the provided decision maker systems is null", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();


        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");
        String msg = null;

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

        ModelsUpdaterModule MUM = new ModelsUpdaterModule(pP);

        try
        {
            MUM.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker identifier(s) is (are) not provided (the array is null)", msg);
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();


        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");
        String msg = null;

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

        pP._DMs = new DM[0];
        ModelsUpdaterModule MUM = new ModelsUpdaterModule(pP);

        try
        {
            MUM.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker identifier(s) is (are) not provided (the array is empty)", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();


        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");
        String msg = null;

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

        pP._DMs = new DM[]{null};
        ModelsUpdaterModule MUM = new ModelsUpdaterModule(pP);

        try
        {
            MUM.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("One of the provided decision maker identifiers is null", msg);
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");
        String msg = null;

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

        pP._DMs = new DM[1];
        pP._DMs[0] = new DM(0, "TMP");
        ModelsUpdaterModule MUM = new ModelsUpdaterModule(pP);

        try
        {
            MUM.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker system(s) does (do) not match the decision maker identifier (s)", msg);
    }

    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");
        String msg = null;

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

        pP._DMs = dms;
        ModelsUpdaterModule MUM = new ModelsUpdaterModule(pP);

        try
        {
            MUM.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }
}