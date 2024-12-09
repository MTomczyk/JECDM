package model.constructor.value.frs.representative;

import alternative.Alternative;
import history.PreferenceInformationWrapper;
import model.constructor.value.representative.IRepresentativeValueModelSelector;
import model.constructor.value.representative.MDVF;
import model.internals.value.AbstractValueInternalModel;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Provides various tests for {@link MDVF}.
 *
 * @author MTomczyk
 */
class MDVFTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        double [][] w = new double[21][2];
        for (int i = 0; i < 21; i++)
        {
            w[i][0] = (double) i / 20.0d;
            w[i][1] = 1.0d - w[i][0];
        }

        ArrayList< LNorm> models = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) models.add(new LNorm(w[i], Double.POSITIVE_INFINITY));

        Alternative A1 = new Alternative("A1", new double[]{0.6d, 0.4d});
        Alternative A2 = new Alternative("A2", new double[]{0.2d, 0.8d});
        PairwiseComparison PC1 = PairwiseComparison.getPreference(A1, A2);

        Alternative A3 = new Alternative("A3", new double[]{0.4d, 0.6d});
        Alternative A4 = new Alternative("A4", new double[]{0.5d, 0.5d});
        PairwiseComparison PC2 = PairwiseComparison.getPreference(A3, A4);

        LinkedList<PreferenceInformationWrapper> pes = new LinkedList<>();
        pes.add(PreferenceInformationWrapper.getTestInstance(PC1));
        pes.add(PreferenceInformationWrapper.getTestInstance(PC2));

        IRepresentativeValueModelSelector<LNorm> RMS = new MDVF<>();
        AbstractValueInternalModel avm = RMS.selectModel(models, pes);

        assertNotNull(avm);
        assertNotNull(avm.getWeights());
        assertEquals(Double.POSITIVE_INFINITY, avm.getAuxParam(), 0.00001d);
        assertEquals(2, avm.getWeights().length);
        assertEquals(0.55d, avm.getWeights()[0], 0.00001d);
        assertEquals(0.45d, avm.getWeights()[1], 0.00001d);
    }
}