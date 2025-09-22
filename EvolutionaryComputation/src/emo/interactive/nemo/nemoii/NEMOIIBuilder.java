package emo.interactive.nemo.nemoii;

import emo.AbstractStandardDSSEMOABuilder;
import exception.EAException;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

/**
 * Auxiliary class assisting in instantiating the NEMO-II algorithm (see, e.g.,
 * {@link NEMOII#getNEMOII(NEMOIIBuilder)}). IMPORTANT NOTE ON THE PARENTS SELECTION PROCESS: The implemented sorting
 * procedure assigns to specimens auxiliary scores (see {@link population.Specimen#setAuxScore(double)}). They are used
 * to perform the final sub-population sorting so that the specimens are sorted according to their non-dominated front
 * number (calculated according to the relation of potential optimality) and attained crowding-distance value.
 * Specifically, the specimens that are in the fronts that can be entirely passed to the new generation are assigned
 * scores 0, 1, 2, where the number indicates their non-dominated front level. The specimens that are in the so-called
 * ambiguous front (i.e., a front whose some members will be passed to the next generation and some will be rejected)
 * are assigned a score that equals: the number of entirely passed fronts + (1 - specimens' crowding-distance / maximal
 * crowding-distance in the front). Thus, the lower values of aux scores are considered preferred, and their order is
 * consistent with the NEMO-II sorting procedure. The aux scores can then be used when performing selection (e.g., when
 * indicating a winner in the tournament selection).
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public class NEMOIIBuilder<T extends AbstractValueInternalModel> extends AbstractStandardDSSEMOABuilder<NEMOII, T>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public NEMOIIBuilder(IRandom R)
    {
        super(R);
        _useUtopiaIncumbent = true;
        _name = "NEMO-II";
    }

    /**
     * Auxiliary object (can be null) responsible for customizing NEMO-II's params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     */
    private NEMOIIBundle.IParamsAdjuster _nemoIIParamsAdjuster = null;

    /**
     * Setter for the auxiliary object (can be null) responsible for customizing NEMO-II's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @param nsgaParamsAdjuster nsga params adjuster
     * @return NSGA builder being parameterized
     */
    public NEMOIIBuilder<T> setNEMOIIParamsAdjuster(NEMOIIBundle.IParamsAdjuster nsgaParamsAdjuster)
    {
        _nemoIIParamsAdjuster = nsgaParamsAdjuster;
        return this;
    }

    /**
     * Getter for the auxiliary object (can be null) responsible for customizing NEMO-II's params container built during
     * the initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     *
     * @return NEMO-II params adjuster
     */
    public NEMOIIBundle.IParamsAdjuster getNEMOIIParamsAdjuster()
    {
        return _nemoIIParamsAdjuster;
    }

    /**
     * The main method for instantiating the EA algorithm. Calls {@link NEMOIIBuilder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones. Note that the auxiliary adjuster objects (e.g.,
     * {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are initialized as
     * imposed by the specified configuration; also note that the adjusters give greater access to the data being
     * instantiated and, thus, the validity of custom adjustments is typically unchecked and may lead to errors.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public NEMOII getInstance() throws EAException
    {
        validate();
        return NEMOII.getNEMOII(this);
    }
}
