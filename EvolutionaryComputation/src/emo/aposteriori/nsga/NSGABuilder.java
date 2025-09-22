package emo.aposteriori.nsga;

import emo.AbstractEMOABuilder;
import exception.EAException;
import random.IRandom;

/**
 * Auxiliary class assisting in instantiating the NSGA algorithm (see, e.g., {@link NSGA#getNSGA(NSGABuilder)}).
 * IMPORTANT NOTE ON THE PARENTS SELECTION PROCESS: The implemented sorting procedure assigns to specimens auxiliary
 * scores (see {@link population.Specimen#setAuxScore(double)}). They are used to perform the final sub-population
 * sorting so that the specimens are sorted according to their non-dominated front number and attained niche-count
 * value. Specifically, the specimens that are in the fronts that can be entirely passed to the new generation are
 * assigned scores 0, 1, 2, where the number indicates their non-dominated front level. The specimens that are in the
 * so-called ambiguous front (i.e., a front whose some members will be passed to the next generation and some will be
 * rejected) are assigned a score that equals: the number of entirely passed fronts + specimens' niche count / maximal
 * niche count in the front. Thus, the lower values of aux scores are considered preferred, and their order is
 * consistent with the NSGA sorting procedure. The aux scores can then be used when performing selection (e.g., when
 * indicating a winner in the tournament selection).
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public class NSGABuilder extends AbstractEMOABuilder<NSGA>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public NSGABuilder(IRandom R)
    {
        super(R);
        _name = "NSGA";
    }

    /**
     * Distance threshold for the niche count procedure.
     */
    private double _th = 0.1d;

    /**
     * Auxiliary object (can be null) responsible for customizing NSGA's params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     */
    private NSGABundle.IParamsAdjuster _nsgaParamsAdjuster = null;

    /**
     * Setter for the distance threshold for the niche count procedure.
     *
     * @param th distance threshold
     * @return NSGA builder being parameterized
     */
    public NSGABuilder setThreshold(double th)
    {
        _th = th;
        return this;
    }

    /**
     * Getter for the distance threshold for the niche count procedure.
     *
     * @return the distance threshold for the niche count procedure
     */
    public double getThreshold()
    {
        return _th;
    }

    /**
     * Setter for the auxiliary object (can be null) responsible for customizing NSGA's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * * completed.
     *
     * @param nsgaParamsAdjuster nemo-0 params adjuster
     * @return NSGA builder being parameterized
     */
    public NSGABuilder setNSGAParamsAdjuster(NSGABundle.IParamsAdjuster nsgaParamsAdjuster)
    {
        _nsgaParamsAdjuster = nsgaParamsAdjuster;
        return this;
    }

    /**
     * Getter for the auxiliary object (can be null) responsible for customizing NSGA's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @return NSGA params adjuster
     */
    public NSGABundle.IParamsAdjuster getNSGAParamsAdjuster()
    {
        return _nsgaParamsAdjuster;
    }

    /**
     * Auxiliary method that can be overwritten to perform simple data validation. It is called by default by
     * {@link NSGABuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public void validate() throws EAException
    {
        super.validate();
        if (Double.compare(_th, 0.0d) < 0)
            throw EAException.getInstanceWithSource("The niche-count distance threshold must not be negative " +
                    "(equals = " + _th + ")", this.getClass());
    }

    /**
     * The main method for instantiating the EA algorithm. Calls {@link NSGABuilder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones. Note that the auxiliary adjuster objects (e.g.,
     * {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are initialized as
     * imposed by the specified configuration; also note that the adjusters give greater access to the data being
     * instantiated and, thus, the validity of custom adjustments is typically unchecked and may lead to errors.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public NSGA getInstance() throws EAException
    {
        validate();
        return NSGA.getNSGA(this);
    }
}
