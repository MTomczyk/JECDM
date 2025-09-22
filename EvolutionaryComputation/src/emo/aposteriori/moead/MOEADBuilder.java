package emo.aposteriori.moead;

import exception.EAException;
import random.IRandom;

/**
 * Auxiliary class assisting in instantiating the MOEA/D algorithm (see, e.g., {@link MOEAD#getMOEAD(MOEADBuilder)}).
 * Important note: the population size parameter has no meaning as the population size is explicitly determined from
 * the number of optimization goals supplied. IMPORTANT NOTE ON THE PARENTS SELECTION PROCESS: In contrast to other
 * implementations, e.g., NSGA-II, this implementation does not assign any auxiliary scores for constructed specimens.
 * Thus, selection procedures relying on these scores are ineffective. Nonetheless, MOEA/D was primarily designed to be
 * coupled with a random selection procedure due to employing a restricted mating pool.
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public class MOEADBuilder extends AbstractMOEADBuilder<MOEAD>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public MOEADBuilder(IRandom R)
    {
        super(R);
        _name = "MOEA/D";
    }

    /**
     * Auxiliary object (can be null) responsible for customizing MOEA/D's params container built during the
     * initialization process. It is assumed that the parameterization is done after the default parameterization is
     * completed.
     */
    private MOEADBundle.IParamsAdjuster _moeadParamsAdjuster = null;

    /**
     * Setter for the auxiliary object (can be null) responsible for customizing MOEA/D's params container built
     * during the initialization process. It is assumed that the parameterization is done after the default
     * parameterization is completed.
     *
     * @param moeadParamsAdjuster MOEA/D params adjuster
     * @return MOEA/D builder being parameterized
     */
    public MOEADBuilder setMOEADParamsAdjuster(MOEADBundle.IParamsAdjuster moeadParamsAdjuster)
    {
        _moeadParamsAdjuster = moeadParamsAdjuster;
        return this;
    }

    /**
     * Getter for the auxiliary object (can be null) responsible for customizing MOEA/D's params container built
     * during the initialization process. It is assumed that the parameterization is done after the default
     * parameterization is completed.
     *
     * @return MOEA/D params adjuster
     */
    public MOEADBundle.IParamsAdjuster getMOEADParamsAdjuster()
    {
        return _moeadParamsAdjuster;
    }

    /**
     * The main method for instantiating the MOEA/D algorithm. Calls {@link MOEADBuilder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones. Note that the auxiliary adjuster objects (e.g.,
     * {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are initialized as
     * imposed by the specified configuration; also note that the adjusters give greater access to the data being
     * instantiated and, thus, the validity of custom adjustments is typically unchecked and may lead to errors.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public MOEAD getInstance() throws EAException
    {
        validate();
        return MOEAD.getMOEAD(this);
    }
}
