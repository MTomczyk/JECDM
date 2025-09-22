package emo.interactive.ktscone.cdemo;

import ea.AbstractEABuilder;
import emo.AbstractStandardDSSEMOABuilder;
import exception.EAException;
import model.internals.value.scalarizing.KTSCone;
import random.IRandom;


/**
 * Auxiliary class assisting in instantiating the CDEMO algorithm (see, e.g., {@link CDEMO#getCDEMO(CDEMOBuilder)}).
 * IMPORTANT NOTE ON THE PARENTS SELECTION PROCESS: The implemented sorting procedure assigns to specimens auxiliary
 * scores (see {@link population.Specimen#setAuxScore(double)}) that are determined as 0, 1, 2, 3, and so on, where the
 * first numbers are reserved for population partitioning according to preference cones. In the case of the existence of
 * an ambiguous front (i.e., a front whose some members will be passed to the next generation and some will be
 * rejected), the front is further partitioned into non-dominated levels, and the remaining solutions are identified
 * arbitrarily starting from the 0-th non-dominated front. The following auxiliary scores are reserved for these fronts.
 * The aux scores can then be used when performing selection (e.g., when indicating a winner in the tournament
 * selection).
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public class CDEMOBuilder extends AbstractStandardDSSEMOABuilder<CDEMO, KTSCone>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public CDEMOBuilder(IRandom R)
    {
        super(R);
        _useUtopiaIncumbent = true;
        _name = "CDEMO";
    }

    /**
     * Auxiliary object (can be null) responsible for customizing CDEMO's params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     */
    private CDEMOBundle.IParamsAdjuster _cdemoParamsAdjuster = null;

    /**
     * Setter for the auxiliary object (can be null) responsible for customizing CDEMO's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @param cdemoParamsAdjuster cdemo params adjuster
     * @return CDEMO builder being parameterized
     */
    public CDEMOBuilder setCDEMOParamsAdjuster(CDEMOBundle.IParamsAdjuster cdemoParamsAdjuster)
    {
        _cdemoParamsAdjuster = cdemoParamsAdjuster;
        return this;
    }

    /**
     * Getter for the auxiliary object (can be null) responsible for customizing CDEMO's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @return CDEMO params adjuster
     */
    public CDEMOBundle.IParamsAdjuster getCDEMOParamsAdjuster()
    {
        return _cdemoParamsAdjuster;
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
     * The main method for instantiating the EA algorithm. Calls {@link CDEMOBuilder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones. Note that the auxiliary adjuster objects (e.g.,
     * {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are initialized as
     * imposed by the specified configuration; also note that the adjusters give greater access to the data being
     * instantiated and, thus, the validity of custom adjustments is typically unchecked and may lead to errors.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public CDEMO getInstance() throws EAException
    {
        validate();
        return CDEMO.getCDEMO(this);
    }
}
