package emo.interactive.ktscone.dcemo;

import ea.AbstractEABuilder;
import emo.AbstractStandardDSSEMOABuilder;
import exception.EAException;
import model.internals.value.scalarizing.KTSCone;
import random.IRandom;


/**
 * Auxiliary class assisting in instantiating the DCEMO algorithm (see, e.g., {@link DCEMO#getDCEMO(DCEMOBuilder)}).
 * IMPORTANT NOTE ON THE PARENTS SELECTION PROCESS: The implemented sorting procedure assigns to specimens auxiliary
 * scores (see {@link population.Specimen#setAuxScore(double)}) that are determined as 0, 1, 2, 3, and so on, where the
 * first numbers are reserved for population partitioning according to non-dominated sorting. In the case of the
 * existence of an ambiguous front (i.e., a front whose some members will be passed to the next generation and some will
 * be rejected), the front is further partitioned according to preference cones, and the remaining solutions are
 * identified arbitrarily starting from the 0-th non-dominated front. The following auxiliary scores are reserved for
 * these fronts. The aux scores can then be used when performing selection (e.g., when indicating a winner in the
 * tournament selection).
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public class DCEMOBuilder extends AbstractStandardDSSEMOABuilder<DCEMO, KTSCone>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public DCEMOBuilder(IRandom R)
    {
        super(R);
        _useUtopiaIncumbent = true;
        _name = "DCEMO";
    }

    /**
     * Auxiliary object (can be null) responsible for customizing DCEMO's params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     */
    private DCEMOBundle.IParamsAdjuster _dcemoParamsAdjuster = null;

    /**
     * Setter for the auxiliary object (can be null) responsible for customizing DCEMO's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @param dcemoParamsAdjuster DCEMO params adjuster
     * @return DCEMO builder being parameterized
     */
    public DCEMOBuilder setDCEMOParamsAdjuster(DCEMOBundle.IParamsAdjuster dcemoParamsAdjuster)
    {
        _dcemoParamsAdjuster = dcemoParamsAdjuster;
        return this;
    }

    /**
     * Getter for the auxiliary object (can be null) responsible for customizing DCEMO's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @return DCEMO params adjuster
     */
    public DCEMOBundle.IParamsAdjuster getDCEMOParamsAdjuster()
    {
        return _dcemoParamsAdjuster;
    }


    /**
     * Auxiliary method for performing a simple data validation. It is called by default
     * by {@link AbstractEABuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public void validate() throws EAException
    {
        super.validate();
        if (!(_dssBuilder.getPreferenceModel() instanceof model.definitions.KTSCone))
            throw EAException.getInstanceWithSource("The provided model definition (" +
                    _dssBuilder.getPreferenceModel().getClass().getName() + ") " +
                    "is not an instance of " + model.definitions.KTSCone.class.getName(), this.getClass());
        if (!(_dssBuilder.getModelConstructor() instanceof model.constructor.value.KTSCone))
            throw EAException.getInstanceWithSource("The provided model constructor (" +
                    _dssBuilder.getPreferenceModel().getClass().getName() + ") " +
                    "is not an instance of " + model.constructor.value.KTSCone.class.getName(), this.getClass());
    }

    /**
     * The main method for instantiating the EA algorithm. Calls {@link DCEMOBuilder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones. Note that the auxiliary adjuster objects (e.g.,
     * {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are initialized as
     * imposed by the specified configuration; also note that the adjusters give greater access to the data being
     * instantiated and, thus, the validity of custom adjustments is typically unchecked and may lead to errors.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public DCEMO getInstance() throws EAException
    {
        validate();
        return DCEMO.getDCEMO(this);
    }
}
