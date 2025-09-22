package emo.aposteriori.nsgaii;

import emo.AbstractEMOABuilder;
import exception.EAException;
import random.IRandom;

/**
 * Auxiliary class assisting in instantiating the NSGA-II algorithm (see, e.g.,
 * {@link NSGAII#getNSGAII(NSGAIIBuilder)}). IMPORTANT NOTE ON THE PARENTS SELECTION PROCESS: The implemented sorting
 * procedure assigns to specimens auxiliary scores (see {@link population.Specimen#setAuxScore(double)}). They are used
 * to perform the final sub-population sorting so that the specimens are sorted according to their non-dominated front
 * number and attained crowding-distance value. Specifically, the specimens that are in the fronts that can be entirely
 * passed to the new generation are assigned scores 0, 1, 2, where the number indicates their non-dominated front level.
 * The specimens that are in the so-called ambiguous front (i.e., a front whose some members will be passed to the next
 * generation and some will be rejected) are assigned a score that equals: the number of entirely passed fronts + (1 -
 * specimens' crowding-distance / maximal crowding-distance in the front). Thus, the lower values of aux scores are
 * considered preferred, and their order is consistent with the NSGA-II sorting procedure. The aux scores can then be
 * used when performing selection (e.g., when indicating a winner in the tournament selection).
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public class NSGAIIBuilder extends AbstractEMOABuilder<NSGAII>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public NSGAIIBuilder(IRandom R)
    {
        super(R);
        _name = "NSGA-II";
    }

    /**
     * Auxiliary object (can be null) responsible for customizing NSGA-II's params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     */
    private NSGAIIBundle.IParamsAdjuster _nsgaiiParamsAdjuster = null;

    /**
     * Setter for the auxiliary object (can be null) responsible for customizing NSGA-II's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * * completed.
     *
     * @param nsgaiiParamsAdjuster nsga params adjuster
     * @return NSGA-II builder being parameterized
     */
    public NSGAIIBuilder setNSGAIIParamsAdjuster(NSGAIIBundle.IParamsAdjuster nsgaiiParamsAdjuster)
    {
        _nsgaiiParamsAdjuster = nsgaiiParamsAdjuster;
        return this;
    }

    /**
     * Getter for the auxiliary object (can be null) responsible for customizing NSGA-II's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @return NSGA-II params adjuster
     */
    public NSGAIIBundle.IParamsAdjuster getNSGAIIParamsAdjuster()
    {
        return _nsgaiiParamsAdjuster;
    }

    /**
     * Auxiliary method that can be overwritten to perform simple data validation. It is called by default by
     * {@link NSGAIIBuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public void validate() throws EAException
    {
        super.validate();
    }

    /**
     * The main method for instantiating the NSGA-II algorithm. Calls {@link NSGAIIBuilder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones. Note that the auxiliary adjuster objects (e.g.,
     * {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are initialized as
     * imposed by the specified configuration; also note that the adjusters give greater access to the data being
     * instantiated and, thus, the validity of custom adjustments is typically unchecked and may lead to errors.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public NSGAII getInstance() throws EAException
    {
        validate();
        return NSGAII.getNSGAII(this);
    }
}
