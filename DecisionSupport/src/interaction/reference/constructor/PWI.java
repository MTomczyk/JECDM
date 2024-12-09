package interaction.reference.constructor;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;
import interaction.reference.validator.IValidator;
import model.IPreferenceModel;
import model.internals.IInternalModel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class implements the Pairwise Winning Index procedure for selecting a pair of solutions to be compared
 * (<a href="https://doi.org/10.1145/3377930.3389826">link</a>). First, the object instance must be coupled with a
 * preference model managed by the system (e.g., linked to a decision maker's system {}). Then, anytime a pair is to be
 * generated, the method selects a pair for which the uncertainty regarding the preference direction is the greatest.
 * It is quantified by using the internal models managed by the preference model to determine how many times a solution
 * in a pair is a winner (50% vs. 50% is the most ambiguous situation possible). Note that it is only reasonable to use
 * this approach if a preference model linked manages a plurality of internal models (i.e., decomposes the preference
 * information into model instances).
 *
 * @author MTomczyk
 */
public class PWI extends AbstractPairsConstructor implements IReferenceSetConstructor
{

    /**
     * Connected preference model (object instance maintained by {@link system.dm.DecisionMakerSystem}, {@link system.model.ModelSystem}).
     */
    private final IPreferenceModel<?> _preferenceModel;

    /**
     * Random pairs (used to generate a random pair when the internal decision maker's model is not established).
     */
    private final RandomPairs _RP;

    /**
     * Parameterized constructor (1 pair to generate, validator are not used).
     *
     * @param preferenceModel linked preference model (object instance maintained by {@link system.dm.DecisionMakerSystem}, {@link system.model.ModelSystem})
     */
    public PWI(IPreferenceModel<?> preferenceModel)
    {
        this(preferenceModel, ((IValidator) null));
    }

    /**
     * Parameterized constructor (1 pair to generate).
     *
     * @param preferenceModel linked preference model (object instance maintained by {@link system.dm.DecisionMakerSystem}, {@link system.model.ModelSystem})
     * @param validator       validator used when analysing the candidate pairs
     */
    public PWI(IPreferenceModel<?> preferenceModel, IValidator validator)
    {
        this(preferenceModel, new IValidator[]{validator}, 1);
    }

    /**
     * Parameterized constructor (1 pair to generate).
     *
     * @param preferenceModel linked preference model (object instance maintained by {@link system.dm.DecisionMakerSystem}, {@link system.model.ModelSystem})
     * @param validators      validators used when analysing the candidate pairs
     */
    public PWI(IPreferenceModel<?> preferenceModel, IValidator[] validators)
    {
        this(preferenceModel, validators, 1);
    }

    /**
     * Parameterized constructor
     *
     * @param preferenceModel linked preference model
     * @param validators      validator used when analysing the candidate pairs
     * @param pairs           how many pairs are to be generated
     */
    public PWI(IPreferenceModel<?> preferenceModel, IValidator[] validators, int pairs)
    {
        super("PWI", validators, pairs);
        _preferenceModel = preferenceModel;
        _RP = new RandomPairs(validators, pairs);
    }

    /**
     * Main method for constructing a reference set (or sets).
     *
     * @param dmContext            current decision-making context
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @return constructed reference set (or sets)
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
    {
        ArrayList<? extends IInternalModel> models = _preferenceModel.getInternalModels();
        if ((models == null) || (models.isEmpty())) return _RP.constructReferenceSets(dmContext, filteredAlternatives);
        else return super.constructReferenceSets(dmContext, filteredAlternatives);
    }

    /**
     * Constructs the pair using a PWI procedure.
     *
     * @param dmContext            current decision-making context
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @param p                    iteration number
     * @return constructed reference set (returns null if the method was not able to construct a valid set)
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    @Override
    protected ReferenceSet constructSet(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives, int p) throws ReferenceSetsConstructorException
    {
        ArrayList<? extends IInternalModel> models = _preferenceModel.getInternalModels();

        Alternative bestFirst = null;
        Alternative bestSecond = null;
        int maxMin = -1;

        for (int i = 0; i < filteredAlternatives.size(); i++)
        {
            Alternative a1 = filteredAlternatives.get(i);
            for (int j = i + 1; j < filteredAlternatives.size(); j++)
            {
                Alternative a2 = filteredAlternatives.get(j);
                if (validatePair(dmContext, a1, a2))
                {
                    int w1 = 0;
                    int w2 = 0;
                    boolean skip = false;

                    int remains = models.size();
                    for (IInternalModel model : models)
                    {
                        remains--;
                        double s1 = model.evaluate(a1);
                        double s2 = model.evaluate(a2);
                        if (Double.compare(s1, s2) == 0) continue;
                        if (Double.compare(s1, s2) < 0) w1++;
                        else w2++;

                        if (Math.min(w1, w2) + remains < maxMin)
                        {
                            skip = true;
                            break;
                        }
                    }

                    if (skip) continue;

                    int min = Math.min(w1, w2);
                    if (min > maxMin)
                    {
                        maxMin = min;
                        bestFirst = a1;
                        bestSecond = a2;
                    }
                }
            }
        }

        if (bestFirst != null) return new ReferenceSet(bestFirst, bestSecond);
        return null;
    }
}
