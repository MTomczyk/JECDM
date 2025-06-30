package relation;

import alternative.Alternative;
import alternative.Alternatives;
import model.IPreferenceModel;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link PO}
 *
 * @author MTomczyk
 */
class POTest
{

    /**
     * Tests {@link PO#isHolding(Alternative)} method.
     */
    @Test
    void isHolding()
    {
        {
            ArrayList<LNorm> models = new ArrayList<>(10);
            models.add(new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY));
            IPreferenceModel<LNorm> model = new model.definitions.LNorm();
            model.setInternalModels(models);
            ArrayList<Alternative> alternatives = new ArrayList<>();
            alternatives.add(new Alternative("A", new double[]{0.5d, 0.5d}));
            PO<LNorm> po = new PO<>(model, new Alternatives(alternatives));
            assertEquals(Relations.POTENTIAL_OPTIMALITY, po.getType());
            assertTrue(po.isHolding(alternatives.get(0)));
        }
        {
            ArrayList<LNorm> models = new ArrayList<>(10);
            models.add(new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY));
            IPreferenceModel<LNorm> model = new model.definitions.LNorm();
            model.setInternalModels(models);
            ArrayList<Alternative> alternatives = new ArrayList<>();
            alternatives.add(new Alternative("A1", new double[]{0.5d, 0.5d}));
            alternatives.add(new Alternative("A2", new double[]{0.75d, 0.75d}));
            PO<LNorm> po = new PO<>(model, new Alternatives(alternatives));
            assertEquals(Relations.POTENTIAL_OPTIMALITY, po.getType());
            assertTrue(po.isHolding(alternatives.get(0)));
            assertFalse(po.isHolding(alternatives.get(1)));
        }
        {
            ArrayList<LNorm> models = new ArrayList<>(10);
            models.add(new LNorm(new double[]{0.0d, 1.0d}, Double.POSITIVE_INFINITY));
            models.add(new LNorm(new double[]{1.0d, 0.0d}, Double.POSITIVE_INFINITY));
            IPreferenceModel<LNorm> model = new model.definitions.LNorm();
            model.setInternalModels(models);
            ArrayList<Alternative> alternatives = new ArrayList<>();
            alternatives.add(new Alternative("A1", new double[]{0.25d, 0.75d}));
            alternatives.add(new Alternative("A2", new double[]{0.5d, 0.5d}));
            alternatives.add(new Alternative("A3", new double[]{0.75d, 0.25d}));
            PO<LNorm> po = new PO<>(model, new Alternatives(alternatives));
            assertEquals(Relations.POTENTIAL_OPTIMALITY, po.getType());
            assertTrue(po.isHolding(alternatives.get(0)));
            assertFalse(po.isHolding(alternatives.get(1)));
            assertTrue(po.isHolding(alternatives.get(2)));
        }
    }
}