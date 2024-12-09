package interaction.reference.constructor;

import alternative.Alternative;
import alternative.Alternatives;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;
import model.IPreferenceModel;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link PWI}.
 *
 * @author MTomczyk
 */
class PWITest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        double[][] W = new double[][]
                {
                        {1.0d / 6.0d / (1.0d / 6.0d + 1.0d / 10.0d), 1.0d / 10.0d / (1.0d / 6.0d + 1.0d / 10.0d)},
                        {1.0d / 8.0d / (1.0d / 8.0d + 1.0d / 10.0d), 1.0d / 10.0d / (1.0d / 8.0d + 1.0d / 10.0d)},
                        {0.5d, 0.5d},
                        {1.0d / 10.0d / (1.0d / 8.0d + 1.0d / 10.0d), 1.0d / 8.0d / (1.0d / 8.0d + 1.0d / 10.0d)},
                        {1.0d / 10.0d / (1.0d / 6.0d + 1.0d / 10.0d), 1.0d / 6.0d / (1.0d / 6.0d + 1.0d / 10.0d)}
                };
        ArrayList<LNorm> lNorms = new ArrayList<>();
        for (double[] w : W) lNorms.add(new LNorm(w, Double.POSITIVE_INFINITY));
        IPreferenceModel<LNorm> model = new model.definitions.LNorm();
        model.setInternalModels(lNorms);


        IReferenceSetConstructor RSC = new PWI(model, null, 1);

        double[][] evals = new double[][]
                {
                        {0.0d, 1.0d},
                        {0.25d, 0.75d},
                        {0.4d, 0.6d},
                        {0.75d, 0.25d},
                };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);

        LinkedList<ReferenceSet> RS = null;
        String msg = null;
        try
        {
            RS = RSC.constructReferenceSets(null, new Alternatives(alternatives));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(RS);
        assertEquals(1, RS.size());
        ReferenceSet rs = RS.getFirst();
        assertEquals(2, rs.getSize());
        assertEquals(2, rs.getAlternatives().size());
        assertEquals("Alternatives = A1, A3", rs.getStringRepresentation());
        assertEquals("A1", rs.getAlternatives().get(0).getName());
        assertEquals("A3", rs.getAlternatives().get(1).getName());
    }
}