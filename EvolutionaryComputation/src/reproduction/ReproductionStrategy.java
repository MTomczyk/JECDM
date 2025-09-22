package reproduction;

import ea.AbstractEA;
import ea.IEA;

/**
 * This class serves as a container for various auxiliary data related to the reproduction strategy employed by the EA.
 *
 * @author MTomczyk
 */
public class ReproductionStrategy
{
    /**
     * Auxiliary interface that can be coupled with the reproduction strategy and employed by the selection and
     * reproduction mechanisms to dynamically indicate the expected number of offspring to be constructed per one
     * Parents selection. Called during {@link selection.ISelect#selectParents(IEA)} stage. The results are stored
     * in the {@link population.Parents} objects and passed to reproduction.
     */
    public interface INoOffspringFromParentsGenerator
    {
        /**
         * The main method.
         *
         * @param ea                           reference to the evolutionary algorithm (provides, e.g., the RNG)
         * @param counter                      current Parents-processing counter (starts from 0)
         * @param noExpectedOffspringGenerated the total number of expected numbers of offspring generated so far
         * @return and expected number of offspring to be constructed from the Parents object
         */
        int getNoOffspringPerParents(IEA ea, int counter, int noExpectedOffspringGenerated);
    }

    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Determines the number of offspring specimens to be constructed from one Parents object
         * ({@link population.Parents}). E.g., if = 1, one offspring is to be generated from one Parents. Thus, the
         * reproduction must be repeated {@link IEA#getOffspringSize()} times to produce an offspring pool of a desired
         * size. If = 2, two offspring specimens are to be constructed from one Parents object. And so on. The field
         * value must be at least 1 (passed value is checked and thresholded at 1).
         */
        public int _noOffspringFromParents;

        /**
         * Flag indicating whether the reproduction strategy should be considered constant.
         * The flag is automatically set to true if {@link Params#_noOffspringFromParentsGenerator} is not provided.
         */
        public boolean _isReproductionStrategyConstant = true;

        /**
         * This flag is in effect when {@link Params#_isReproductionStrategyConstant} is false (thus, dynamic).
         * If it is true, the potential excess number of offspring generated during the reproduction will
         * be thresholded to match {@link IEA#getOffspringSize()}. If false and there is an excess, an exception is
         * expected to be thrown.
         */
        public boolean _enableOffspringThresholding = false;

        /**
         * Auxiliary object (can be null) that can be coupled with the reproduction strategy and employed by the
         * selection and reproduction mechanisms to dynamically indicate the expected number of offspring to be
         * constructed per one Parents selection. Called during {@link selection.ISelect#selectParents(IEA)} stage
         * (ONLY when {@link Params#_isReproductionStrategyConstant} if false). The results are stored in the
         * {@link population.Parents} objects and passed to reproduction.
         */
        public INoOffspringFromParentsGenerator _noOffspringFromParentsGenerator = null;

        /**
         * Default constructor.
         */
        public Params()
        {
            this(1);
        }

        /**
         * @param noOffspringFromParents determines the number of offspring specimens to be constructed from one Parents
         *                               object ({@link population.Parents}); e.g., if = 1, one offspring is to be
         *                               generated from one Parents; thus, the reproduction must be repeated
         *                               {@link IEA#getOffspringSize()} times to produce an offspring pool of a desired
         *                               size; if = 2, two offspring specimens are to be constructed from one Parents
         *                               object; and so on; the field value must be at least 1 (passed value is checked
         *                               and thresholded at 1)
         */
        public Params(int noOffspringFromParents)
        {
            _noOffspringFromParents = noOffspringFromParents;
        }
    }

    /**
     * Parameterized constructor.
     * <p>
     * Determines the number of offspring specimens to be constructed from one Parents object
     * ({@link population.Parents}). E.g., if = 1, one offspring is to be generated from one Parents. Thus, the
     * reproduction must be repeated {@link IEA#getOffspringSize()} times to produce an offspring pool of a desired
     * size.
     * If = 2, two offspring specimens are to be constructed from one Parents object. And so on. The field value must
     * be
     * at least 1 (passed value is checked and thresholded at 1).
     */
    private final int _noOffspringFromParents;

    /**
     * Flag indicating whether the reproduction strategy should be considered constant.
     * The flag is automatically set to true if {@link Params#_noOffspringFromParentsGenerator} is not provided.
     */
    private final boolean _isReproductionStrategyConstant;

    /**
     * This flag is in effect when {@link Params#_isReproductionStrategyConstant} is false (thus, dynamic).
     * If it is true, the potential excess number of offspring generated during the reproduction will
     * be thresholded to match {@link IEA#getOffspringSize()}. If false and there is an excess, an exception is
     * expected to be thrown.
     */
    private final boolean _enableOffspringThresholding;

    /**
     * Auxiliary object (can be null) that can be coupled with the reproduction strategy and employed by the
     * selection and reproduction mechanisms to dynamically indicate the expected number of offspring to be
     * constructed per one Parents selection. Called during {@link selection.ISelect#selectParents(IEA)} stage. The
     * results are stored in the {@link population.Parents} objects and passed to reproduction.
     */
    private final INoOffspringFromParentsGenerator _noOffspringFromParentGenerator;

    /**
     * This field serves as an additional cap on the number of offspring solutions that can be generated in one
     * generation. In most implementations, it can be left set to the max value. E.g., in generational
     * implementations, the number of offspring to produce is primarily determined by
     * {@link AbstractEA.Params#_offspringSize}. In most implementations, it can be left set to the max value. E.g., in
     * generational implementations, the number of offspring to produce is primarily determined by
     * {@link AbstractEA.Params#_offspringSize}. As for the steady-state implementations, they typically set
     * {@link AbstractEA.Params#_offspringSize} to 1 and assume "multiple parents to one offspring" reproduction scheme,
     * which
     * ultimately leads to producing the same number of offspring as the population size. Nonetheless, assume that
     * "multiple parents to two offspring" scheme is used in a steady-state algorithm. It would lead to producing
     * twice as many offspring as the population size dictates. If this is a desired strategy, then this field can
     * be left as it is. However, if one wants to impose an additional threshold, it can be done via this field. The
     * algorithm measures the number of constructed offspring throughout the generation. If the number would exceed
     * the allowed number, executing redundant steady-state repeats is skipped.
     */
    private int _offspringLimitPerGeneration = Integer.MAX_VALUE;


    /**
     * Parameterized constructor.
     *
     * @param noOffspringFromParents determines the number of offspring specimens to be constructed from one Parents
     *                               object ({@link population.Parents}); e.g., if = 1, one offspring is to be
     *                               generated from one Parents; thus, the reproduction must be repeated
     *                               {@link IEA#getOffspringSize()} times to produce an offspring pool of a desired
     *                               size; if = 2, two offspring specimens are to be constructed from one Parents
     *                               object; and so on; the field value must be at least 1 (passed value is checked and
     *                               thresholded at 1)
     */
    public ReproductionStrategy(int noOffspringFromParents)
    {
        this(new Params(noOffspringFromParents));
    }

    /**
     * Returns a default strategy that:
     * - is considered constant
     * - expects to produce one offspring from one Parents selection during the reproduction phase
     *
     * @return the default strategy
     */
    public static ReproductionStrategy getDefaultStrategy()
    {
        ReproductionStrategy.Params p = new Params();
        p._isReproductionStrategyConstant = true;
        p._noOffspringFromParents = 1;
        p._enableOffspringThresholding = false;
        p._noOffspringFromParentsGenerator = null;
        return new ReproductionStrategy(p);
    }

    /**
     * Returns a dynamic strategy that: <br>
     * - uses the provided generator to return an excepted number of offspring to construct per Parents (called by
     * {@link selection.ISelect#selectParents(IEA)});<br>
     * - sets the "isReproductionStrategyConstant" flag to false;<br>
     * - sets the "enableOffspringThresholding" flag to true;
     *
     * @param generator generator of the expected numbers of offspring to produce
     * @return the dynamic strategy
     */
    public static ReproductionStrategy getDynamicStrategy(INoOffspringFromParentsGenerator generator)
    {
        ReproductionStrategy.Params p = new Params();
        p._isReproductionStrategyConstant = false;
        p._noOffspringFromParents = 1;
        p._enableOffspringThresholding = true;
        p._noOffspringFromParentsGenerator = generator;
        return new ReproductionStrategy(p);
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public ReproductionStrategy(Params p)
    {
        _noOffspringFromParents = Math.max(1, p._noOffspringFromParents);
        if (p._noOffspringFromParentsGenerator == null) _isReproductionStrategyConstant = true;
        else _isReproductionStrategyConstant = p._isReproductionStrategyConstant;
        _enableOffspringThresholding = p._enableOffspringThresholding;
        _noOffspringFromParentGenerator = p._noOffspringFromParentsGenerator;
    }

    /**
     * Checks if it is expected to produce one offspring specimen from one Parents selection (Parents can encapsulate an
     * arbitrary number of Parent objects).
     *
     * @return true, if it is expected to produce one offspring specimen from one Parents selection; false otherwise
     */
    public boolean isOneOffspringFromOneParents()
    {
        return _noOffspringFromParents == 1;
    }

    /**
     * Checks if it is expected to produce two offspring specimen from one Parents selection (Parents can encapsulate an
     * arbitrary number of Parent objects).
     *
     * @return true, if it is expected to produce one offspring specimen from one Parents selection; false otherwise
     */
    public boolean areTwoOffspringFromParents()
    {
        return _noOffspringFromParents == 2;
    }

    /**
     * Returns the constant number of offspring to be generated from one Parents selection.
     *
     * @return the constant number of offspring to be generated from one Parents selection.
     */
    public int getConstantNoOffspringFromParents()
    {
        return _noOffspringFromParents;
    }

    /**
     * Returns the flag indicating whether the reproduction strategy should be considered constant.
     *
     * @return the flag indicating whether the reproduction strategy should be considered constant
     */
    public boolean isReproductionStrategyConstant()
    {
        return _isReproductionStrategyConstant;
    }

    /**
     * Returns the flag indicating whether the offspring thresholding is allowed. It is in effect when
     * {@link Params#_isReproductionStrategyConstant} is false (thus, dynamic). If it is true, the potential excess
     * number of offspring generated during the reproduction will be thresholded to match
     * {@link IEA#getOffspringSize()}. If false and there is an excess, an exception is expected to be thrown.
     *
     * @return the flag
     */
    public boolean isOffspringThresholdingEnabled()
    {
        return _enableOffspringThresholding;
    }

    /**
     * Getter for the auxiliary object (can be null) that can be coupled with the reproduction strategy and employed by
     * the selection and reproduction mechanisms to dynamically indicate the expected number of offspring to be
     * constructed per one Parents selection. Called during {@link selection.ISelect#selectParents(IEA)} stage. The
     * results are stored in the {@link population.Parents} objects and passed to reproduction.
     *
     * @return the no. offspring from parents generator
     */
    public INoOffspringFromParentsGenerator getNoOffspringFromParentsGenerator()
    {
        return _noOffspringFromParentGenerator;
    }

    /**
     * Setter for the field that serves as an additional cap on the number of offspring solutions that can be generated
     * in one generation. In most implementations, it can be left set to the max value. E.g., in generational
     * implementations, the number of offspring to produce is primarily determined by
     * {@link AbstractEA.Params#_offspringSize}. In most implementations, it can be left set to the max value. E.g., in
     * generational implementations, the number of offspring to produce is primarily determined by
     * {@link AbstractEA.Params#_offspringSize}. As for the steady-state implementations, they typically set
     * {@link AbstractEA.Params#_offspringSize} to 1 and assume "multiple parents to one offspring" reproduction scheme,
     * which
     * ultimately leads to producing the same number of offspring as the population size. Nonetheless, assume that
     * "multiple parents to two offspring" scheme is used in a steady-state algorithm. It would lead to producing
     * twice as many offspring as the population size dictates. If this is a desired strategy, then this field can
     * be left as it is. However, if one wants to impose an additional threshold, it can be done via this field. The
     * algorithm measures the number of constructed offspring throughout the generation. If the number would exceed
     * the allowed number, executing redundant steady-state repeats is skipped.
     *
     * @param offspringLimitPerGeneration offspring limit in generation
     */
    public void setOffspringLimitPerGeneration(int offspringLimitPerGeneration)
    {
        _offspringLimitPerGeneration = offspringLimitPerGeneration;
    }


    /**
     * Getter for the field value that serves as an additional cap on the number of offspring solutions that can be
     * generated in one generation. In most implementations, it can be left set to the max value. E.g., in generational
     * implementations, the number of offspring to produce is primarily determined by
     * {@link AbstractEA.Params#_offspringSize}. In most implementations, it can be left set to the max value. E.g., in
     * generational implementations, the number of offspring to produce is primarily determined by
     * {@link AbstractEA.Params#_offspringSize}. As for the steady-state implementations, they typically set
     * {@link AbstractEA.Params#_offspringSize} to 1 and assume "multiple parents to one offspring" reproduction scheme,
     * which ultimately leads to producing the same number of offspring as the population size. Nonetheless, assume that
     * "multiple parents to two offspring" scheme is used in a steady-state algorithm. It would lead to producing
     * twice as many offspring as the population size dictates. If this is a desired strategy, then this field can
     * be left as it is. However, if one wants to impose an additional threshold, it can be done via this field. The
     * algorithm measures the number of constructed offspring throughout the generation. If the number would exceed
     * the allowed number, executing redundant steady-state repeats is skipped.
     *
     * @return offspring limit per generation
     */
    public int getOffspringLimitPerGeneration()
    {
        return _offspringLimitPerGeneration;
    }

}
