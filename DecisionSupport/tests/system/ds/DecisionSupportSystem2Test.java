package system.ds;

import criterion.Criteria;
import dmcontext.DMContext;
import exeption.DecisionSupportSystemException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various (complex) tests for {@link DecisionSupportSystem}.
 *
 * @author MTomczyk
 */
class DecisionSupportSystem2Test
{

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesModels();
        p._interactionTrigger = Dummy.getInteractionTrigger();
        p._refiner = Dummy.getRefiner();
        p._referenceSetsConstructor = Dummy.getReferenceSetsConstructor(
                p._dmBundles[0]._modelBundles[0]._preferenceModel,
                p._dmBundles[1]._modelBundles[0]._preferenceModel);
        p._feedbackProvider = Dummy.getFeedbackProvider();
        String msg = null;
        DecisionSupportSystem DSS = null;
        try
        {
            DSS = new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(DSS);

        try
        {
            DSS.executeProcess(null);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The 'notify system starts' method was not called", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesModels();
        p._interactionTrigger = Dummy.getInteractionTrigger();
        p._refiner = Dummy.getRefiner();
        p._referenceSetsConstructor = Dummy.getReferenceSetsConstructor(
                p._dmBundles[0]._modelBundles[0]._preferenceModel,
                p._dmBundles[1]._modelBundles[0]._preferenceModel);
        p._feedbackProvider = Dummy.getFeedbackProvider();
        String msg = null;
        DecisionSupportSystem DSS = null;
        try
        {
            DSS = new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(DSS);

        try
        {
            DSS.notifySystemStarts();
            DSS.executeProcess(null);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The params container is not provided", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesModels();
        p._interactionTrigger = Dummy.getInteractionTrigger();
        p._refiner = Dummy.getRefiner();
        p._referenceSetsConstructor = Dummy.getReferenceSetsConstructor(
                p._dmBundles[0]._modelBundles[0]._preferenceModel,
                p._dmBundles[1]._modelBundles[0]._preferenceModel);
        p._feedbackProvider = Dummy.getFeedbackProvider();
        String msg = null;
        DecisionSupportSystem DSS = null;
        try
        {
            DSS = new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(DSS);

        try
        {
            DSS.notifySystemStarts();
            DSS.executeProcess(new DMContext.Params());
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The current alternatives superset is not provided", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesModels();
        p._interactionTrigger = Dummy.getInteractionTrigger();
        p._refiner = Dummy.getRefiner();
        p._referenceSetsConstructor = Dummy.getReferenceSetsConstructor(
                p._dmBundles[0]._modelBundles[0]._preferenceModel,
                p._dmBundles[1]._modelBundles[0]._preferenceModel);
        p._feedbackProvider = Dummy.getFeedbackProvider();
        String msg = null;
        DecisionSupportSystem DSS = null;
        try
        {
            DSS = new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(DSS);

        try
        {
            DSS.notifySystemStarts();
            DMContext.Params pDMC = new DMContext.Params();
            pDMC._currentAlternativesSuperset = Dummy.getAlternatives();
            pDMC._currentOS = Dummy.getObjectiveSpace();
            DSS.executeProcess(pDMC);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The current iteration is not provided", msg);
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = Criteria.constructCriteria("C", 2, false);
        p._dmBundles = Dummy.getDMBundlesModels();
        p._interactionTrigger = Dummy.getInteractionTrigger();
        p._refiner = Dummy.getRefiner();
        p._referenceSetsConstructor = Dummy.getReferenceSetsConstructor(
                p._dmBundles[0]._modelBundles[0]._preferenceModel,
                p._dmBundles[1]._modelBundles[0]._preferenceModel);
        p._feedbackProvider = Dummy.getFeedbackProvider();
        String msg = null;
        DecisionSupportSystem DSS = null;
        try
        {
            DSS = new DecisionSupportSystem(p);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(DSS);

        try
        {
            DSS.notifySystemStarts();
            DSS.notifySystemStarts();
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The 'notify system starts' method was already called", msg);
    }
}