package emo.interactive.nemo.nemo0;

import ea.AbstractEABuilder;
import emo.AbstractStandardDSSEMOABuilder;
import exception.EAException;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

/**
 * Auxiliary class assisting in instantiating the NEMO-0 algorithm (see, e.g., {@link NEMO0#getNEMO0(NEMO0Builder)}).
 * IMPORTANT NOTE ON THE PARENTS SELECTION PROCESS: The implemented sorting procedure assigns to specimens auxiliary
 * scores (see {@link population.Specimen#setAuxScore(double)}). They are used to perform the final sub-population
 * sorting so that the specimens are sorted according to their non-dominated front number and attained relevance value.
 * Specifically, the specimens that are in the fronts that can be entirely passed to the new generation are assigned
 * scores 0, 1, 2, where the number indicates their non-dominated front level. The specimens that are in the so-called
 * ambiguous front (i.e., a front whose some members will be passed to the next generation and some will be rejected)
 * are assigned a score that equals: the number of entirely passed fronts + relevance score / maximal score in the
 * front, in the case of the relevance to be minimized. In the case of maximization, the aux scores are calculated as
 * the number of entirely passed fronts + 1 - relevance score / maximal score in the front. Thus, the lower values of
 * aux scores are considered preferred, and their order is consistent with the NEMO-0 sorting procedure. The aux scores
 * can then be used when performing selection (e.g., when indicating a winner in the tournament selection).
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public class NEMO0Builder<T extends AbstractValueInternalModel> extends AbstractStandardDSSEMOABuilder<NEMO0, T>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public NEMO0Builder(IRandom R)
    {
        super(R);
        _useUtopiaIncumbent = true;
        _name = "NEMO-0";
    }

    /**
     * Auxiliary object (can be null) responsible for customizing NEMO-0's params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     */
    private NEMO0Bundle.IParamsAdjuster _nemo0ParamsAdjuster = null;

    /**
     * Setter for the auxiliary object (can be null) responsible for customizing NEMO-0's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @param nsgaParamsAdjuster nsga params adjuster
     * @return NSGA builder being parameterized
     */
    public NEMO0Builder<T> setNEMO0ParamsAdjuster(NEMO0Bundle.IParamsAdjuster nsgaParamsAdjuster)
    {
        _nemo0ParamsAdjuster = nsgaParamsAdjuster;
        return this;
    }

    /**
     * Getter for the auxiliary object (can be null) responsible for customizing NEMO-0's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @return NEMO-0 params adjuster
     */
    public NEMO0Bundle.IParamsAdjuster getNEMO0ParamsAdjuster()
    {
        return _nemo0ParamsAdjuster;
    }

    /**
     * Auxiliary for performing a simple data validation. It is called by default
     * by {@link AbstractEABuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public void validate() throws EAException
    {
        super.validate();
        if (!(_dssBuilder.getModelConstructor() instanceof model.constructor.value.representative.RepresentativeModel<T>))
            throw EAException.getInstanceWithSource("The provided model constructor (" +
                    _dssBuilder.getModelConstructor().getClass().getName() + ") " +
                    "is not an instance of " + model.constructor.value.representative.RepresentativeModel.class.getName(), this.getClass());
    }

    /**
     * The main method for instantiating the EA algorithm. Calls {@link NEMO0Builder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones. Note that the auxiliary adjuster objects (e.g.,
     * {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are initialized as
     * imposed by the specified configuration; also note that the adjusters give greater access to the data being
     * instantiated and, thus, the validity of custom adjustments is typically unchecked and may lead to errors.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public NEMO0 getInstance() throws EAException
    {
        validate();
        return NEMO0.getNEMO0(this);
    }
}
