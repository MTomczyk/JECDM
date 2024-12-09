package model.constructor.value;

import alternative.Alternative;
import dmcontext.DMContext;
import exeption.ConstructorException;
import exeption.HistoryException;
import history.History;
import history.PreferenceInformationWrapper;
import model.constructor.Report;
import org.junit.jupiter.api.Test;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import space.Range;
import space.os.ObjectiveSpace;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link KTSCone}.
 *
 * @author MTomczyk
 */
class KTSConeTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        LinkedList<IPreferenceInformation> PCs = new LinkedList<>();
        PCs.add(PairwiseComparison.getPreference(new Alternative("P0", new double[]{0.0d, 1.0d}),
                new Alternative("P1", new double[]{1.0d, 0.0d})));
        PCs.add(PairwiseComparison.getPreference(new Alternative("P2", new double[]{0.0d, 2.0d}),
                new Alternative("P3", new double[]{6.0d, 0.0d})));
        PCs.add(PairwiseComparison.getPreference(new Alternative("P4", new double[]{1.0d, 0.0d}),
                new Alternative("P5", new double[]{0.0d, 2.0d})));

        History history = new History("history");
        LinkedList<PreferenceInformationWrapper> piws = null;
        String msg = null;
        try
        {
            piws = history.registerPreferenceInformation(PCs, 0, true);
        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(piws);

        ObjectiveSpace OS = new ObjectiveSpace(new double[]{0.0d, 0.0d}, new double[]{1.0d, 1.0d},
                Range.getDefaultRanges(2, 1.0d), new boolean[2]);

        DMContext dmContext = new DMContext(null, null,
                null, OS, false, 0);
        Report<model.internals.value.scalarizing.KTSCone> bundle = null;

        KTSCone constructor = new KTSCone();
        try
        {
            constructor.registerDecisionMakingContext(dmContext);
            bundle = constructor.constructModels(piws);
            constructor.unregisterDecisionMakingContext();
        } catch (ConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        assertNotNull(bundle);
        assertNotNull(bundle._models);
        assertEquals(1, bundle._models.size());
        assertNotNull(bundle._models.get(0));

        model.internals.value.scalarizing.KTSCone ktsCone = bundle._models.get(0);

        double[][] e = new double[][]
                {
                        {0.0d, 0.0d}, // 1
                        {2.0d, 1.0d}, // 1
                        {4.0d, 0.0d}, // 2
                        {3.0d, 3.0d}, // 1
                        {6.0d, 4.0d}, // 1

                        {1.0d, 2.0d}, // 1
                        {2.0d, 3.0d}, // 0
                        {3.0d, 4.0d}, // 0
                        {4.0d, 5.0d}, // 0
                        {5.0d, 6.0d}, // 0
                        {1.0d, 5.0d}, // 1
                        {0.0d, 6.0d} // 1
                };

        double[] expected = new double[]{1.0d, 1.0d, 2.0d, 1.0d, 1.0d,
                1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 1.0d};
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        for (int i = 0; i < e.length; i++)
        {
            double score = ktsCone.evaluate(alternatives.get(i));
            assertEquals(expected[i], score, 0.000001d);
        }

    }
}