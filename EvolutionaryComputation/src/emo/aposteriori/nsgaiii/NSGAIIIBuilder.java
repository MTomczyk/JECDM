package emo.aposteriori.nsgaiii;

import emo.AbstractDecompositionEMOABuilder;
import emo.utils.decomposition.nsgaiii.IAssignmentResolveTie;
import emo.utils.decomposition.nsgaiii.ISpecimenResolveTie;
import emo.utils.decomposition.nsgaiii.RandomAssignment;
import emo.utils.decomposition.nsgaiii.RandomSpecimen;
import exception.EAException;
import random.IRandom;

/**
 * Auxiliary class assisting in instantiating the NSGA-III algorithm (see, e.g.,
 * {@link NSGAIII#getNSGAIII(NSGAIIIBuilder)}). Important note: the {@link emo.aposteriori.nsgaii.NSGAIIBuilder}
 * population size has no meaning as the population size is explicitly determined from the number of optimization goals
 * supplied {@link NSGAIIIBuilder#getGoals()}. IMPORTANT NOTE ON THE PARENTS SELECTION PROCESS: The implemented sorting
 * procedure assigns to specimens auxiliary scores (see {@link population.Specimen#setAuxScore(double)}). Specifically,
 * the specimens that are in the fronts that can be entirely passed to the new generation are assigned scores 0, 1, 2,
 * where the number indicates their non-dominated front level. The specimens that are in the so-called ambiguous front
 * (i.e., a front whose some members will be passed to the next generation and some will be rejected) are assigned a
 * score that equals: the number of entirely passed fronts + 0.5. Thus, the lower values of aux scores are considered
 * preferred, and their order is consistent only with the non-dominated sorting procedure. The aux scores can then be
 * used when performing selection (e.g., when indicating a winner in the tournament selection). Nonetheless, NSGA-III
 * was primarily designed to be coupled with random selection, assuming that the reproduction operators will produce
 * offspring relatively similar to parents.
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public class NSGAIIIBuilder extends AbstractDecompositionEMOABuilder<NSGAIII>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public NSGAIIIBuilder(IRandom R)
    {
        super(R);
        _name = "NSGA-II";
    }

    /**
     * Object resolving the NSGA-III's assignment selection ties (the random assignment procedure is used by default).
     */
    private IAssignmentResolveTie _assignmentResolveTie = new RandomAssignment();

    /**
     * Object resolving the NSGA-III's specimen selection ties (the random assignment procedure is used by default).
     */
    private ISpecimenResolveTie _specimenResolveTie = new RandomSpecimen();

    /**
     * Auxiliary object (can be null) responsible for customizing NSGA-III's params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     */
    private NSGAIIIBundle.IParamsAdjuster _nsgaiiiParamsAdjuster = null;

    /**
     * Setter for the object resolving the NSGA-III's assignment selection ties (the random assignment procedure is used
     * by default).
     *
     * @param assignmentResolveTie procedure for resolving assignment ties
     * @return NSGA-III builder being parameterized
     */
    public NSGAIIIBuilder setAssigmentResolveTie(IAssignmentResolveTie assignmentResolveTie)
    {
        _assignmentResolveTie = assignmentResolveTie;
        return this;
    }

    /**
     * Getter for the object resolving the NSGA-III's assignment selection ties (the random assignment procedure
     * is used by default).
     *
     * @return the object resolving the NSGA-III's assignment selection ties
     */
    public IAssignmentResolveTie getAssignmentResolveTie()
    {
        return _assignmentResolveTie;
    }

    /**
     * Setter for the object resolving the NSGA-III's specimen selection ties (the random assignment procedure is used
     * by default).
     *
     * @param specimenResolveTie procedure for resolving specimen selection ties
     * @return NSGA-III builder being parameterized
     */
    public NSGAIIIBuilder setSpecimenResolveTie(ISpecimenResolveTie specimenResolveTie)
    {
        _specimenResolveTie = specimenResolveTie;
        return this;
    }

    /**
     * Getter for the object resolving the NSGA-III's specimen selection ties (the random assignment procedure is used
     * by default).
     *
     * @return procedure for resolving specimen selection ties
     */
    public ISpecimenResolveTie getSpecimenResolveTie()
    {
        return _specimenResolveTie;
    }

    /**
     * Setter for the auxiliary object (can be null) responsible for customizing NSGA-III's params container built
     * during the initialization process. It is assumed that the parameterization is done after the default
     * parameterization is completed.
     *
     * @param nsgaiiiParamsAdjuster nsga params adjuster
     * @return NSGA-III builder being parameterized
     */
    public NSGAIIIBuilder setNSGAIIIParamsAdjuster(NSGAIIIBundle.IParamsAdjuster nsgaiiiParamsAdjuster)
    {
        _nsgaiiiParamsAdjuster = nsgaiiiParamsAdjuster;
        return this;
    }

    /**
     * Getter for the auxiliary object (can be null) responsible for customizing NSGA-III's params container built
     * during the initialization process. It is assumed that the parameterization is done after the default
     * parameterization is completed.
     *
     * @return NSGA-III params adjuster
     */
    public NSGAIIIBundle.IParamsAdjuster getNSGAIIIParamsAdjuster()
    {
        return _nsgaiiiParamsAdjuster;
    }

    /**
     * Auxiliary method that can be overwritten to perform simple data validation. It is called by default by
     * {@link NSGAIIIBuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public void validate() throws EAException
    {
        super.validate();
        if (_assignmentResolveTie == null)
            throw EAException.getInstanceWithSource("The procedure for resolving assignment selection ties " +
                    "is not provided", this.getClass());
        if (_specimenResolveTie == null)
            throw EAException.getInstanceWithSource("The procedure for resolving specimen selection ties " +
                    "is not provided", this.getClass());
    }

    /**
     * The main method for instantiating the NSGA-III algorithm. Calls {@link NSGAIIIBuilder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones. Note that the auxiliary adjuster objects (e.g.,
     * {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are initialized as
     * imposed by the specified configuration; also note that the adjusters give greater access to the data being
     * instantiated and, thus, the validity of custom adjustments is typically unchecked and may lead to errors.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public NSGAIII getInstance() throws EAException
    {
        validate();
        return NSGAIII.getNSGAIII(this);
    }
}
