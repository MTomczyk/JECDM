package system.ds;

import criterion.Criteria;
import dmcontext.DMContext;
import exeption.DecisionSupportSystemException;
import interaction.Status;
import model.internals.AbstractInternalModel;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;
import system.dm.DM;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various (complex) tests for {@link DecisionSupportSystem}.
 *
 * @author MTomczyk
 */
class DecisionSupportSystem3Test
{
    /**
     * Test 1.
     */
    @Test
    void test1()
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

        System.out.println("=========================================================================================");
        Report report = null;
        try
        {
            DSS.notifySystemStarts();
            DMContext.Params pDMC = new DMContext.Params();
            pDMC._currentAlternativesSuperset = Dummy.getAlternatives();
            pDMC._currentOS = Dummy.getObjectiveSpace();
            pDMC._currentIteration = 0;
            pDMC._R = R;
            report = DSS.executeProcess(pDMC);
        } catch (DecisionSupportSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(report);


        report.printStringRepresentation();
        assertEquals(0, report._iteration);
        assertNotNull(report._elicitationReport);
        assertNull(report._updateReport);
        assertEquals(Status.INTERACTION_WAS_NOT_TRIGGERED, report._elicitationReport._interactionStatus);


        System.out.println("=========================================================================================");
        int t = 10;
        double[] rw = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        double tolerance = 0.001d;

        for (int i = 1; i < t; i++)
        {
            report = null;
            try
            {
                DMContext.Params pDMC = new DMContext.Params();
                pDMC._currentAlternativesSuperset = Dummy.getAlternatives();
                pDMC._currentOS = Dummy.getObjectiveSpace();
                pDMC._currentIteration = i;
                pDMC._R = R;
                report = DSS.executeProcess(pDMC);
            } catch (DecisionSupportSystemException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(report);

            //report.printStringRepresentation();
            assertEquals(i, report._iteration);
            assertNotNull(report._elicitationReport);
            assertNotNull(report._updateReport);
            assertEquals(Status.PROCESS_ENDED_SUCCESSFULLY, report._elicitationReport._interactionStatus);
            DM[] dms = DSS.getDMs();
            system.model.Report<? extends AbstractInternalModel> mR1 = report._updateReport._modelsUpdatesReports.get(dms[0])._reportsOnModelUpdates.getFirst();
            system.model.Report<? extends AbstractInternalModel> mR2 = report._updateReport._modelsUpdatesReports.get(dms[1])._reportsOnModelUpdates.getFirst();
            assertFalse(mR1._inconsistencyOccurred);
            assertFalse(mR2._inconsistencyOccurred);
            assertFalse(mR1._report._inconsistencyDetected);
            assertFalse(mR2._report._inconsistencyDetected);
            System.out.println("Success rates = " + mR1._report._successRateInConstructing + " " + mR2._report._successRateInConstructing);

            system.model.Report<? extends AbstractInternalModel>[] mRS = new system.model.Report<?>[]{mR1, mR2};
            for (int dm = 0; dm < 2; dm++)
            {
                double[][] d = new double[][]{{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY},
                        {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}};

                for (AbstractInternalModel model : mRS[dm]._report._models)
                {
                    double[] w = model.getWeights();
                    if (Double.compare(w[0], d[0][0]) < 0) d[0][0] = w[0];
                    if (Double.compare(w[0], d[0][1]) > 0) d[0][1] = w[0];
                    if (Double.compare(w[1], d[1][0]) < 0) d[1][0] = w[1];
                    if (Double.compare(w[1], d[1][1]) > 0) d[1][1] = w[1];
                }
                System.out.printf("Weights bounds for DM = %d: [%.4f, %.4f], [%.4f, %.4f]%n",
                        dm, d[0][0], d[0][1], d[1][0], d[1][1]);

                if (dm == 0)
                {
                    assertTrue(Double.compare(d[0][0], 0.3d + tolerance) <= 0);
                    assertTrue(Double.compare(d[0][1], 0.3d - tolerance) >= 0);
                    assertTrue(Double.compare(d[1][0], 0.7d + tolerance) <= 0);
                    assertTrue(Double.compare(d[1][1], 0.7d - tolerance) >= 0);
                    assertTrue(Double.compare(d[0][1] - d[0][0], rw[0] + tolerance) <= 0);
                    assertTrue(Double.compare(d[1][1] - d[1][0], rw[1] + tolerance) <= 0);
                    rw[0] = d[0][1] - d[0][0];
                    rw[1] = d[1][1] - d[1][0];
                    if (i == t - 1)
                    {
                        assertTrue(Double.compare(rw[0], 0.1d) <= 0);
                        assertTrue(Double.compare(rw[1], 0.1d) <= 0);
                    }
                }
                else
                {
                    assertTrue(Double.compare(d[0][0], 0.7d + tolerance) <= 0);
                    assertTrue(Double.compare(d[0][1], 0.7d - tolerance) >= 0);
                    assertTrue(Double.compare(d[1][0], 0.3d + tolerance) <= 0);
                    assertTrue(Double.compare(d[1][1], 0.3d - tolerance) >= 0);
                    assertTrue(Double.compare(d[0][1] - d[0][0], rw[2] + tolerance) <= 0);
                    assertTrue(Double.compare(d[1][1] - d[1][0], rw[3] + tolerance) <= 0);
                    rw[2] = d[0][1] - d[0][0];
                    rw[3] = d[1][1] - d[1][0];
                    if (i == t - 1)
                    {
                        assertTrue(Double.compare(rw[2], 0.1d) <= 0);
                        assertTrue(Double.compare(rw[3], 0.1d) <= 0);
                    }
                }


            }

        }

        report.printStringRepresentation();

    }
}