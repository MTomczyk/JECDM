package relation;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.IAlternativeWrapper;
import model.IPreferenceModel;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;

/**
 * Relation of potential optimality the (see <a href="https://ieeexplore.ieee.org/document/6729055">paper</a>). An
 * alternative is considered potentially optimal (potentially the most relevant) if there exist one model instance (in
 * the supplied preference model) that ranks this solution the best in a set.
 *
 * @author MTomczyk
 */
public class PO<T extends AbstractValueInternalModel> implements IUnaryRelation
{
    /**
     * Parameterized constructor.
     *
     * @param preferenceModel DM's instantiated preference model
     * @param alternatives    alternatives superset (alternatives should be unique given their names)
     */
    public PO(IPreferenceModel<T> preferenceModel, AbstractAlternatives<?> alternatives)
    {
        _preferenceModel = preferenceModel;
        _alternatives = alternatives;
    }

    /**
     * DM's instantiated preference model.
     */
    private final IPreferenceModel<T> _preferenceModel;

    /**
     * Alternatives superset (alternatives should be unique given their names).
     */
    private final AbstractAlternatives<?> _alternatives;

    /**
     * Checks if a relation R for an alternative A holds.
     *
     * @param A the alternative (should be included in the supplied alternatives superset).
     * @return true if the relation holds, false otherwise
     */
    @Override
    public boolean isHolding(Alternative A)
    {
        return isHolding(A, _alternatives, _preferenceModel.getInternalModels());
    }

    /**
     * Checks if a relation R for an alternative A holds.
     *
     * @param A            the alternative (should be included in the supplied alternatives superset).
     * @param alternatives alternatives superset
     * @param models       internal value models
     * @param <T>          preference model definition
     * @return true if the relation holds, false otherwise
     */
    public static <T extends AbstractValueInternalModel> boolean isHolding(Alternative A,
                                                                           AbstractAlternatives<?> alternatives,
                                                                           ArrayList<T> models)
    {
        for (T model : models)
        {
            boolean passes = true;

            double score = model.evaluate(A);
            for (IAlternativeWrapper B : alternatives)
            {
                if (A.equals(B.getAlternative())) continue;
                double s2 = model.evaluate(B.getAlternative());
                if (((model.isLessPreferred()) && (Double.compare(s2, score) < 0)) ||
                        ((!model.isLessPreferred()) && (Double.compare(s2, score) > 0)))
                {
                    passes = false;
                    break;
                }
            }
            if (passes) return true;
        }
        return false;
    }

    /**
     * Returns relation type (POTENTIAL_OPTIMALITY).
     *
     * @return relation type (id)
     */
    @Override
    public Relations getType()
    {
        return Relations.POTENTIAL_OPTIMALITY;
    }
}
