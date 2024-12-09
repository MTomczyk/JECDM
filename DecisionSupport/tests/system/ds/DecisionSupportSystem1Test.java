package system.ds;

import criterion.Criteria;
import exeption.DecisionSupportSystemException;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Provides various (complex) tests for {@link DecisionSupportSystem}.
 *
 * @author MTomczyk
 */
class DecisionSupportSystem1Test
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        String msg = null;
        try
        {
            new DecisionSupportSystem(null);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The params object is not provided", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The criteria are not provided (the object is null)", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 0, false);
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The criteria are not provided (the number of criteria is less than 1)", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker bundles are not provided (the array is null)", msg);
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = new DMBundle[0];
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker bundles are not provided (the array is empty)", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = new DMBundle[]{null};
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("One of the provided decision maker bundles is null", msg);
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = new DMBundle[]{new DMBundle(null)};
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("One of the provided decision maker bundles provides no name", msg);
    }

    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = new DMBundle[]{new DMBundle("DM2"), new DMBundle("DM2")};
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision maker name = DM2 is not unique", msg);
    }

    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesNoModels();
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The model bundle(s) for the decision maker = DM1 is (are) not provided (the array is null)", msg);
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesNoModels();
        p._dmBundles[0]._modelBundles = new ModelBundle<?>[0];
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The model bundle(s) for the decision maker = DM1 is (are) not provided (the array is empty)", msg);
    }


    /**
     * Test 11.
     */
    @Test
    void test11()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesNoModels();
        p._dmBundles[0]._modelBundles = new ModelBundle<?>[]{null};
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("One of the model bundles provided for the decision maker = DM1 is null", msg);
    }

    /**
     * Test 12.
     */
    @Test
    void test12()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesModelsCannotBeInitialized();
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Could not initialize a model system for a decision maker = DM1 (handler = system.model.ModelSystem, reason = The preference model is not provided)", msg);
    }

    /**
     * Test 13.
     */
    @Test
    void test13()
    {
        IRandom R = new MersenneTwister64(0);
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesModels(R);
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Validation of the preference elicitation module failed (handler = system.modules.elicitation.PreferenceElicitationModule, reason = The interaction trigger is not provided)", msg);
    }

    /**
     * Test 14.
     */
    @Test
    void test14()
    {
        IRandom R = new MersenneTwister64(0);
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesModels(R);
        p._interactionTrigger = Dummy.getInteractionTrigger();
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Validation of the preference elicitation module failed (handler = system.modules.elicitation.PreferenceElicitationModule, reason = The refiner is not provided)", msg);
    }

    /**
     * Test 15.
     */
    @Test
    void test15()
    {
        IRandom R = new MersenneTwister64(0);
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesModels(R);
        p._interactionTrigger = Dummy.getInteractionTrigger();
        p._refiner = Dummy.getRefiner();
        p._referenceSetsConstructor = Dummy.getReferenceSetsConstructor(
                p._dmBundles[0]._modelBundles[0]._preferenceModel,
                p._dmBundles[1]._modelBundles[0]._preferenceModel);
        p._feedbackProvider = Dummy.getFeedbackProvider();
        String msg = null;
        try
        {
            new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }
}