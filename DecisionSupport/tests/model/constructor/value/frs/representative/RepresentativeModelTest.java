package model.constructor.value.frs.representative;

import alternative.Alternative;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.representative.IRepresentativeValueModelSelector;
import model.constructor.value.representative.MDVF;
import model.internals.value.AbstractValueInternalModel;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link RepresentativeModel}.
 *
 * @author MTomczyk
 */
class RepresentativeModelTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
        IRandom R = new MersenneTwister64(0);
        IRepresentativeValueModelSelector<LNorm> RMS = new MDVF<>();
        RepresentativeModel.Params<LNorm> rmP = new RepresentativeModel.Params<>(RM, RMS);
        rmP._samplingLimit = 10000000;
        rmP._feasibleSamplesToGenerate = 1000;
        rmP._inconsistencyThreshold = 1;
        RepresentativeModel<LNorm> repModel = new RepresentativeModel<>(rmP);
        Report<LNorm> report = null;

        Alternative A1 = new Alternative("A1", new double[]{0.6d, 0.4d});
        Alternative A2 = new Alternative("A2", new double[]{0.2d, 0.8d});
        PairwiseComparison PC1 = PairwiseComparison.getPreference(A1, A2);

        Alternative A3 = new Alternative("A3", new double[]{0.4d, 0.6d});
        Alternative A4 = new Alternative("A4", new double[]{0.5d, 0.5d});
        PairwiseComparison PC2 = PairwiseComparison.getPreference(A3, A4);

        LinkedList<PreferenceInformationWrapper> pes = new LinkedList<>();
        pes.add(PreferenceInformationWrapper.getTestInstance(PC1));
        pes.add(PreferenceInformationWrapper.getTestInstance(PC2));

        String msg = null;
        try
        {
            DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null, null, null, false, 0, null, R);
            repModel.registerDecisionMakingContext(dmContext);
            report = repModel.constructModels(pes);
            repModel.unregisterDecisionMakingContext();
        } catch (ConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(report);
        assertFalse(report._inconsistencyDetected);
        assertEquals(1, report._models.size());

        AbstractValueInternalModel avm = report._models.get(0);

        assertNotNull(avm);
        assertNotNull(avm.getWeights());
        assertEquals(Double.POSITIVE_INFINITY, avm.getAuxParam(), 0.00001d);
        assertEquals(2, avm.getWeights().length);
        assertEquals(0.55d, avm.getWeights()[0], 0.1d);
        assertEquals(0.45d, avm.getWeights()[1], 0.1d);
    }
}