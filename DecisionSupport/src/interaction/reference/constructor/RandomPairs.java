package interaction.reference.constructor;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;
import interaction.reference.validator.IValidator;
import random.IRandom;

/**
 * Implementation of {@link IReferenceSetConstructor} for constructing random pairs of alternatives. This procedure
 * attempts to generate pairs from a uniform distribution. Note that a randomly drawn pair may be invalid (checked via
 * {@link IValidator}). If so, the process is repeated until a valid pair is drawn (the first phase -- follows the
 * uniform distribution). However, if most of the existing pairs are invalid, such a process may run extensively long.
 * To address this issue, the sampling process is repeated up to several times (as provided via this field). If the
 * valid pair is still not found, the process switches to ``cyclic search'' (the second phase -- does not guarantee
 * uniform distribution). In this step, the first pair is drawn randomly. Next, the index to the second alternative is
 * shifted right (cyclic) until a valid pair is found. If a full cycle is passed and no valid pair is still found, the
 * method returns no reference set (null).
 *
 * @author MTomczyk
 */
public class RandomPairs extends AbstractPairsConstructor implements IReferenceSetConstructor
{
    /**
     * The number of attempts for the first phase.
     */
    protected final int _noHitsBeforeCyclic;

    /**
     * Parameterized constructor (1 pair to generate, validators are not used).
     */
    public RandomPairs()
    {
        this((IValidator) null);
    }


    /**
     * Parameterized constructor (1 pair to generate).
     *
     * @param validator validators used when analysing the validity of candidate pairs (all must be satisfied)
     */
    public RandomPairs(IValidator validator)
    {
        this(new IValidator[]{validator}, 1);
    }

    /**
     * Parameterized constructor (1 pair to generate).
     *
     * @param validators validators used when analysing the validity of candidate pairs (all must be satisfied)
     */
    public RandomPairs(IValidator[] validators)
    {
        this(validators, 1);
    }


    /**
     * Parameterized constructor.
     *
     * @param validators validators used when analysing the validity of candidate pairs (all must be satisfied)
     * @param pairs      how many pairs are to be generated
     */
    public RandomPairs(IValidator[] validators, int pairs)
    {
        this(validators, pairs, 10);
    }

    /**
     * Parameterized constructor.
     *
     * @param validators         validators used when analysing validity of candidate pairs
     * @param pairs              how many pairs are to be generated
     * @param noHitsBeforeCyclic the number of attempts for the first phase
     */
    public RandomPairs(IValidator[] validators, int pairs, int noHitsBeforeCyclic)
    {
        super("Random pairs", validators, pairs);
        _noHitsBeforeCyclic = Math.max(noHitsBeforeCyclic, 0);
    }

    /**
     * A method for constructing random pair.
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
        IRandom R = dmContext.getR();
        if (R == null)
            throw new ReferenceSetsConstructorException("The random number generator is not supplied by the decision-making context", this.getClass());

        // the first phase
        for (int s = 0; s < _noHitsBeforeCyclic; s++)
        {
            int i = R.nextInt(filteredAlternatives.size());
            int j = R.nextInt(filteredAlternatives.size());
            if (validatePair(dmContext, filteredAlternatives.get(i), filteredAlternatives.get(j)))
                return new ReferenceSet(filteredAlternatives.get(i), filteredAlternatives.get(j));
        }

        // the second phase

        int i = R.nextInt(filteredAlternatives.size());
        int j = R.nextInt(filteredAlternatives.size());

        int allIterations = 0;
        int rowIterations = 0;

        while ((i == j) || (!validatePair(dmContext, filteredAlternatives.get(i), filteredAlternatives.get(j))))
        {
            j++;
            if (i == j) continue;

            if (j == filteredAlternatives.size())
            {
                j = 0;
                rowIterations++;
            }

            if (rowIterations == filteredAlternatives.size() - 1)
            {
                rowIterations = 0;
                i++;
                if (i == filteredAlternatives.size()) i = 0;
            }

            allIterations++;
            if (allIterations == filteredAlternatives.size() * filteredAlternatives.size() - filteredAlternatives.size())
                return null;
        }

        return new ReferenceSet(filteredAlternatives.get(i), filteredAlternatives.get(j));
    }

}
