package emo.interactive.iemod;

import ea.AbstractEABuilder;
import emo.aposteriori.moead.AbstractMOEADBuilder;
import emo.interactive.StandardDSSBuilder;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.EAException;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

/**
 * Auxiliary class assisting in instantiating the IEMO/D algorithm (see, e.g., {@link IEMOD#getIEMOD(IEMODBuilder)}).
 *
 *
 *
 * @author MTomczyk
 */
public class IEMODBuilder<T extends AbstractValueInternalModel> extends AbstractMOEADBuilder<IEMOD>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public IEMODBuilder(IRandom R)
    {
        super(R);
        setUseUtopiaIncumbent(true);
    }

    /**
     * The object assisting in constructing a decision support system for IEMO/D.
     */
    private StandardDSSBuilder<T> _dssBuilder;

    /**
     * Auxiliary object (can be null) responsible for customizing IEMO/D params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     */
    private IEMODBundle.IParamsAdjuster<T> _iemodParamsAdjuster = null;

    /**
     * Solution assignment strategy triggered after updating the optimization goals and re-establishing the
     * neighborhood. The updates might have been triggered due to executing preference learning or due to updating the
     * * algorithm's internal data on the objective space bounds. The implementation is expected to suitably
     * adjust/create the {@link emo.utils.decomposition.goal.Assignment} objects maintained by
     * {@link MOEADGoalsManager}.
     */
    private IGoalsUpdateReassignmentStrategy _reassignmentStrategy = new BestReassignments();

    /**
     * Setter for the object assisting in constructing a decision support system for IEMO/D.
     *
     * @param dssBuilder the object assisting in constructing a decision support system for IEMO/D
     */
    public void setStandardDSSBuilder(StandardDSSBuilder<T> dssBuilder)
    {
        _dssBuilder = dssBuilder;
    }

    /**
     * Getter for the object assisting in constructing a decision support system for IEMO/D.
     *
     * @return the object assisting in constructing a decision support system for IEMO/D
     */
    public StandardDSSBuilder<T> getDSSBuilder()
    {
        return _dssBuilder;
    }

    /**
     * Setter for the OPTIONAL solution assignment strategy triggered after updating the optimization goals and
     * re-establishing the neighborhood. The updates might have been triggered due to executing preference learning or
     * due to updating the algorithm's internal data on the objective space bounds. The implementation is expected to
     * suitably adjust/create the {@link emo.utils.decomposition.goal.Assignment} objects maintained by
     * {@link MOEADGoalsManager}.
     *
     * @param reassignmentStrategy reassignment strategy
     */
    public void setReassignmentStrategy(IGoalsUpdateReassignmentStrategy reassignmentStrategy)
    {
        _reassignmentStrategy = reassignmentStrategy;
    }

    /**
     * Getter for the solution assignment strategy triggered after updating the optimization goals and re-establishing
     * the neighborhood. The updates might have been triggered due to executing preference learning or due to updating
     * the algorithm's internal data on the objective space bounds. The implementation is expected to suitably
     * adjust/create the {@link emo.utils.decomposition.goal.Assignment} objects maintained by
     * {@link MOEADGoalsManager}.
     *
     * @return reassignment strategy
     */
    public IGoalsUpdateReassignmentStrategy getReassignmentStrategy()
    {
        return _reassignmentStrategy;
    }

    /**
     * Setter for an object (can be null) responsible for customizing IEMO/D params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @param iemodParamsAdjuster IEMO/D params adjuster
     */
    public void setIEMODParamsAdjuster(IEMODBundle.IParamsAdjuster<T> iemodParamsAdjuster)
    {
        _iemodParamsAdjuster = iemodParamsAdjuster;
    }

    /**
     * Getter for an auxiliary object (can be null) responsible for customizing IEMO/D params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @return IEMO/D params adjuster
     */
    public IEMODBundle.IParamsAdjuster<T> getIEMODParamsAdjuster()
    {
        return _iemodParamsAdjuster;
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
        if (_reassignmentStrategy == null)
            throw EAException.getInstanceWithSource("The assignment strategy has not been provided",
                    this.getClass());
        if (_dssBuilder == null)
            throw EAException.getInstanceWithSource("The decision support system builder has not been provided",
                    this.getClass());
        _dssBuilder.validate();
    }

    /**
     * The main method for instantiating the EA algorithm. Calls {@link IEMODBuilder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones. Note that the auxiliary adjuster objects (e.g.,
     * {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are initialized as
     * imposed by the specified configuration; also note that the adjusters give greater access to the data being
     * instantiated and, thus, the validity of custom adjustments is typically unchecked and may lead to errors.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public IEMOD getInstance() throws EAException
    {
        validate();
        return IEMOD.getIEMOD(this);
    }
}
