package reproduction.operators.mutation;

import random.IRandom;
import reproduction.operators.AbstractOperator;
import reproduction.valuecheck.IValueCheck;
import reproduction.valuecheck.Wrap;
import space.IntRange;
import space.Range;

/**
 * Abstract class implementing the {@link IMutate} interface.
 *
 * @author MTomczyk
 */

public abstract class AbstractMutation extends AbstractOperator implements IMutate
{
    /**
     * Params container.
     */
    public static class Params extends AbstractOperator.Params
    {

        /**
         * Parameterized constructor (sets the Wrap object to check values).
         *
         * @param probability the probability of triggering the operation
         */
        public Params(double probability)
        {
            this(probability, new Wrap());
        }

        /**
         * Parameterized constructor.
         *
         * @param probability the probability of triggering the operation
         * @param valueCheck  procedure used for correcting invalid gene values
         */
        public Params(double probability, IValueCheck valueCheck)
        {
            this(probability, valueCheck, (Range[]) null);
        }

        /**
         * Parameterized constructor.
         *
         * @param probability  the probability of triggering the operation
         * @param valueCheck   procedure used for correcting invalid gene values
         * @param doubleBounds feasible bounds for doubles
         */
        public Params(double probability, IValueCheck valueCheck, Range[] doubleBounds)
        {
            super(probability, valueCheck, doubleBounds);
        }

        /**
         * Parameterized constructor.
         *
         * @param probability the probability of triggering the operation
         * @param valueCheck  procedure used for correcting invalid gene values
         * @param intBounds   feasible bounds for integers
         */
        public Params(double probability, IValueCheck valueCheck, IntRange[] intBounds)
        {
            super(probability, valueCheck, intBounds);
        }
    }


    /**
     * Parameterized constructor. Parameters are passed via params container.
     *
     * @param p params container
     */
    public AbstractMutation(Params p)
    {
        super(p);
    }


    /**
     * Default implementation of the method.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     */
    @Override
    public void mutate(int[] o, IRandom R)
    {
    }

    /**
     * Default implementation of the method.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     */
    @Override
    public void mutate(double[] o, IRandom R)
    {

    }

    /**
     * Default implementation of the method.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     */
    @Override
    public void mutate(boolean[] o, IRandom R)
    {

    }
}