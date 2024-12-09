package reproduction.operators;

import reproduction.valuecheck.IValueCheck;
import reproduction.valuecheck.Wrap;
import space.IntRange;
import space.Range;

/**
 * Provides common fields/functionalities.
 *
 * @author MTomczyk
 */
public class AbstractOperator
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * The probability of triggering the operation.
         */
        public double _probability;


        /**
         * Procedure used for correcting invalid gene values.
         */
        public IValueCheck _valueCheck;

        /**
         * Feasible bounds for doubles (if value check is provided but double bounds not, the [0-1] default bound should be used).
         */
        public Range[] _doubleBounds;

        /**
         * Feasible bounds for integers (if value check is provided but integer bounds not, the [0, 1] default should will be used).
         */
        public IntRange[] _intBounds;

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
            _probability = probability;
            _valueCheck = valueCheck;
            _doubleBounds = doubleBounds;
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
            _probability = probability;
            _valueCheck = valueCheck;
            _intBounds = intBounds;
        }
    }


    /**
     * The probability of triggering the operation.
     */
    protected final double _probability;

    /**
     * Feasible bounds for doubles.
     */
    protected Range[] _doubleBounds;

    /**
     * Feasible bounds for doubles.
     */
    protected IntRange[] _intBounds;

    /**
     * Procedure used for correcting invalid gene values.
     */
    protected IValueCheck _valueCheck;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractOperator(Params p)
    {
        _probability = p._probability;
        _valueCheck = p._valueCheck;
        _doubleBounds = p._doubleBounds;
        _intBounds = p._intBounds;
    }

    /**
     * Auxiliary method that checks if the input double value is in correct bounds (the method terminates if che
     * value check object is not provided of the double bound is not specified). If the method proceeds, the value check
     * object will conditionally apply the correction (if the double bound is not specified, a default bound of [0, 1] will
     * be used).
     *
     * @param v     input value
     * @param index index for the double bound
     * @return corrected value (will equal the input if it is in the bound).
     */
    public double applyDoubleCorrection(double v, int index)
    {
        if (_valueCheck != null)
        {
            if ((_doubleBounds != null) && (_doubleBounds.length > index) && (_doubleBounds[index] != null))
                return _valueCheck.checkAndCorrect(v, _doubleBounds[index].getLeft(), _doubleBounds[index].getRight());
            else return _valueCheck.checkAndCorrect(v, 0.0d, 1.0d); // use normal bound otherwise
        }
        else return v;
    }

    /**
     * Setter for the "value check" object (checks if the variable value is in bounds).
     *
     * @param valueCheck value check
     */
    public void setValueCheck(IValueCheck valueCheck)
    {
        _valueCheck = valueCheck;
    }

    /**
     * Method for setting feasible bounds for doubles.
     *
     * @param bounds new bounds for doubles
     */
    public void setFeasibleBounds(Range[] bounds)
    {
        _doubleBounds = bounds;
    }

    /**
     * Method for setting feasible bounds for integers.
     *
     * @param intBounds new bounds for integers
     */
    public void setFeasibleBounds(IntRange[] intBounds)
    {
        _intBounds = intBounds;
    }
}
