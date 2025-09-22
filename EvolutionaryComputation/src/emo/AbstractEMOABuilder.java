package emo;

import criterion.Criteria;
import ea.AbstractEABuilder;
import ea.EA;
import emo.aposteriori.nsgaii.NSGAIIBuilder;
import exception.EAException;
import os.ObjectiveSpaceManager;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import space.normalization.INormalization;

/**
 * Auxiliary class assisting in instantiating EMO algorithms (see, e.g., {@link NSGAIIBuilder#getInstance()}).
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class AbstractEMOABuilder<T extends EA> extends AbstractEABuilder<T>
{
    /**
     * Assumed policies for learning the objective space (or Pareto front) bounds.
     */
    private enum OSBoundsLearningPolicy
    {
        /**
         * Dynamic learning.
         */
        DYNAMIC,
        /**
         * Fixed normalizations provided.
         */
        FIXED,
    }

    /**
     * If true, the data on the known Pareto front bounds will be updated dynamically; false: the data is assumed fixed
     * (suitable normalization functions must be provided when instantiating the EA). If fixed, the objective space
     * manager will not be instantiated by default, and the normalizations will be directly passed to interested
     * components.
     */
    protected boolean _updateOSDynamically = true;

    /**
     * Optional initial normalization functions. One for each criterion. Should be provided when the parameterization
     * assumes that the EMO algorithm knows the true Pareto front bounds a priori, or does it dynamically, but has some
     * initial knowledge of them.
     */
    protected INormalization[] _initialNormalizations = null;

    /**
     * Optional utopia point. If the dynamic mode for objective bounds learning is used and the nadir point is provided,
     * the utopia will be used as the initial data when instantiating the objective space manager. When the
     * configuration assumes fixed and known Pareto front bounds: If provided with the nadir, they will be used to
     * instantiate a fixed objective space manager.
     */
    protected double[] _utopia = null;

    /**
     * Optional nadir point. If the dynamic mode for objective bounds learning is used and the utopia point is provided,
     * the utopia will be used as the initial data when instantiating the objective space manager. When the
     * configuration assumes fixed and known Pareto front bounds: If provided with the utopia, they will be used to
     * instantiate a fixed objective space manager.
     */
    protected double[] _nadir = null;

    /**
     * Field is in effect only when {@link AbstractEMOABuilder#_updateOSDynamically} is set to true. If true, the
     * {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be configured so that the objective space is
     * updated based not only on the current population but the historical data as well (compare with the incumbent to
     * determine the best value for each objective ever found).
     */
    protected boolean _useUtopiaIncumbent = false;

    /**
     * Field is in effect only when {@link AbstractEMOABuilder#_updateOSDynamically} is set to true. If true, the
     * {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be configured so that the objective space is
     * updated based not only on the current population but the historical data as well (compare with the incumbent to
     * determine the worst value for each objective ever found).
     */
    protected boolean _useNadirIncumbent = false;

    /**
     * Auxiliary object (can be null) responsible for customizing objective space manager params container built when
     * the updateOSDynamically flag is set to true (otherwise, it is possible that the manager will be null; the
     * adjuster is not used).
     */
    protected ObjectiveSpaceManager.IParamsAdjuster _osParamsAdjuster = null;

    /**
     * Objective space bounds learning policy. An auxiliary field used when checking if any mode has been selected at
     * all.
     */
    private OSBoundsLearningPolicy _osBoundsLearningPolicy = null;

    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public AbstractEMOABuilder(IRandom R)
    {
        super(R);
    }

    /**
     * This method parameterizes the container to assume that the data on the Pareto front bounds are fixed. The
     * suitable normalization functions mapping the performance vectors into [0, 1] hypercube (wth 0 being the utopia
     * and 1 the nadir) must be supplied.
     *
     * @param normalizations normalization functions
     * @return EMOA builder being parameterized
     */
    public AbstractEMOABuilder<T> setFixedOSBoundsLearningPolicy(INormalization[] normalizations)
    {
        return setFixedOSBoundsLearningPolicy(normalizations, null, null);
    }

    /**
     * This method parameterizes the container to assume that the data on the Pareto front bounds are fixed. The
     * suitable normalization functions mapping the performance vectors into [0, 1] hypercube (wth 0 being the utopia
     * and 1 the nadir) must be supplied.
     *
     * @param normalizations normalization functions
     * @param utopia         optional utopia point; if the dynamic mode for objective bounds learning is used and the
     *                       nadir point is provided, the utopia will be used as the initial data when instantiating the
     *                       objective space manager; when the configuration assumes fixed and known Pareto front
     *                       bounds: if provided with the nadir, they will be used to instantiate a fixed objective
     *                       space manager.
     * @param nadir          optional nadir point; if the dynamic mode for objective bounds learning is used and the
     *                       utopia point is provided, the utopia will be used as the initial data when instantiating
     *                       the objective space manager; when the configuration assumes fixed and known Pareto front
     *                       bounds: if provided with the utopia, they will be used to instantiate a fixed objective
     *                       space manager.
     * @return EMOA builder being parameterized
     */
    public AbstractEMOABuilder<T> setFixedOSBoundsLearningPolicy(INormalization[] normalizations, double[] utopia, double[] nadir)
    {
        _updateOSDynamically = false;
        _initialNormalizations = normalizations;
        _utopia = utopia;
        _nadir = nadir;
        _osBoundsLearningPolicy = OSBoundsLearningPolicy.FIXED;
        return this;
    }

    /**
     * This method parameterizes the container to assume that the data on the Pareto front bounds are fixed. The
     * suitable normalization functions mapping the performance vectors into [0, 1] hypercube (wth 0 being the utopia
     * and 1 the nadir) must be supplied. They are retrieved from the provided problem bundle.
     *
     * @param problemBundle provides normalization functions
     * @return EMOA builder being parameterized
     */
    public AbstractEMOABuilder<T> setFixedOSBoundsLearningPolicy(AbstractMOOProblemBundle problemBundle)
    {
        _updateOSDynamically = false;
        _initialNormalizations = problemBundle._normalizations;
        _osBoundsLearningPolicy = OSBoundsLearningPolicy.FIXED;
        _utopia = problemBundle._utopia;
        _nadir = problemBundle._nadir;
        return this;
    }

    /**
     * This method parameterizes the container to assume that the data on the Pareto front bounds are to be learnt
     * dynamically.
     *
     * @return EMOA builder being parameterized
     */
    public AbstractEMOABuilder<T> setDynamicOSBoundsLearningPolicy()
    {
        return setDynamicOSBoundsLearningPolicy((INormalization[]) null);
    }

    /**
     * This method parameterizes the container to assume that the data on the Pareto front bounds are to be learnt
     * dynamically. Additionally, the configuration assumes that initial suitable normalization functions mapping the
     * performance vectors into [0, 1] hypercube (wth 0 being the utopia and 1 the nadir) are delivered (and can be
     * updated later during the optimization).
     *
     * @param initialNormalizations initial normalization functions
     * @return EMOA builder being parameterized
     */
    public AbstractEMOABuilder<T> setDynamicOSBoundsLearningPolicy(INormalization[] initialNormalizations)
    {
        return setDynamicOSBoundsLearningPolicy(initialNormalizations, null, null);
    }

    /**
     * This method parameterizes the container to assume that the data on the Pareto front bounds are to be learnt
     * dynamically. Additionally, the configuration assumes that initial suitable normalization functions mapping the
     * performance vectors into [0, 1] hypercube (wth 0 being the utopia and 1 the nadir) are delivered (and can be
     * updated later during the optimization).
     *
     * @param initialNormalizations initial normalization functions
     * @param initialUtopia         optional initial utopia point (can be null); if provided with the nadir, they are
     *                              used to initially set the objective space manager
     * @param initialNadir          optional initial nadir point (can be null); if provided with the utopia, they are
     *                              used to initially set the objective space manager
     * @return EMOA builder being parameterized
     */
    public AbstractEMOABuilder<T> setDynamicOSBoundsLearningPolicy(INormalization[] initialNormalizations, double[] initialUtopia, double[] initialNadir)
    {
        _utopia = initialUtopia;
        _nadir = initialNadir;
        _updateOSDynamically = true;
        _initialNormalizations = initialNormalizations;
        _osBoundsLearningPolicy = OSBoundsLearningPolicy.DYNAMIC;
        return this;
    }

    /**
     * This method parameterizes the container to assume that the data on the Pareto front bounds are to be learnt
     * dynamically. Additionally, the configuration assumes that initial suitable normalization functions mapping the
     * performance vectors into [0, 1] hypercube (wth 0 being the utopia and 1 the nadir) are delivered (and can be
     * updated later during the optimization). They are retrieved from the provided problem bundle.
     *
     * @param problemBundle provides normalization functions
     * @return EMOA builder being parameterized
     */
    public AbstractEMOABuilder<T> setDynamicOSBoundsLearningPolicy(AbstractMOOProblemBundle problemBundle)
    {
        _utopia = null;
        _nadir = null;
        _updateOSDynamically = true;
        _initialNormalizations = problemBundle._normalizations;
        _osBoundsLearningPolicy = OSBoundsLearningPolicy.DYNAMIC;
        return this;
    }

    /**
     * Getter for the flag indicating whether the data on the known Pareto front bounds should be updated dynamically
     * (true); If false: the data is assumed fixed (suitable normalization functions must be provided when instantiating
     * the EA). If fixed, the objective space manager will not be instantiated by default, and the normalizations will
     * be directly passed to interested components.
     *
     * @return the flag
     */
    public boolean shouldUpdateOSDynamically()
    {
        return _updateOSDynamically;
    }

    /**
     * Getter for the optional initial normalization functions. One for each criterion. Should be provided when
     * instantiating the builder when the parameterization assumes that the EMO algorithm knows the true Pareto front
     * bounds a priori, or does it dynamically, but has some initial knowledge of them.
     *
     * @return initial normalization functions
     */
    public INormalization[] getInitialNormalizations()
    {
        return _initialNormalizations;
    }

    /**
     * Getter for the optional utopia point. If the dynamic mode for objective bounds learning is used and the nadir
     * point is provided, the utopia will be used as the initial data when instantiating the objective space manager.
     * When the configuration assumes fixed and known Pareto front bounds: If provided with the nadir, they will be used
     * to instantiate a fixed objective space manager.
     *
     * @return utopia point
     */
    public double[] getUtopia()
    {
        return _utopia;
    }

    /**
     * Getter for the optional nadir point. If the dynamic mode for objective bounds learning is used and the utopia
     * point is provided, the nadir will be used as the initial data when instantiating the objective space manager.
     * When the configuration assumes fixed and known Pareto front bounds: If provided with the utopia, they will be
     * used to instantiate a fixed objective space manager.
     *
     * @return nadir point
     */
    public double[] getNadir()
    {
        return _nadir;
    }

    /**
     * Setter for the {@link AbstractEMOABuilder#_useUtopiaIncumbent} field (in effect only when
     * {@link AbstractEMOABuilder#_updateOSDynamically} is true). If the input is true, the
     * {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be configured so that the objective space is
     * updated based not only on the current population but the historical data as well (compare with the incumbent to
     * determine the best value for each objective ever found).
     *
     * @param useUtopiaIncumbent if true, utopia incumbent should be used; false otherwise
     * @return EA builder being parameterized
     */
    public AbstractEMOABuilder<T> setUseUtopiaIncumbent(boolean useUtopiaIncumbent)
    {
        _useUtopiaIncumbent = useUtopiaIncumbent;
        return this;
    }

    /**
     * Getter for the flag that is in effect only when {@link AbstractEMOABuilder#_updateOSDynamically} is set to true.
     * If true, the {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be configured so that the
     * objective space is updated based not only on the current population but the historical data as well (compare with
     * the incumbent to determine the best value for each objective ever found).
     *
     * @return the flag
     */
    public boolean shouldUseUtopiaIncumbent()
    {
        return _useUtopiaIncumbent;
    }

    /**
     * Setter for the {@link AbstractEMOABuilder#_useNadirIncumbent} field (in effect only when
     * {@link AbstractEMOABuilder#_updateOSDynamically} is true). If the input is true, the
     * {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be configured so that the objective space is
     * updated based not only on the current population but the historical data as well (compare with the incumbent to
     * determine the worst value for each objective ever found).
     *
     * @param useNadirIncumbent if true, nadir incumbent should be used; false otherwise
     * @return EA builder being parameterized
     */
    public AbstractEMOABuilder<T> setUseNadirIncumbent(boolean useNadirIncumbent)
    {
        _useNadirIncumbent = useNadirIncumbent;
        return this;
    }

    /**
     * Getter for the flag that is in effect only when {@link AbstractEMOABuilder#_updateOSDynamically} is set to true.
     * If true, the {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be configured so that the
     * objective space is updated based not only on the current population but the historical data as well (compare with
     * the incumbent to determine the worst value for each objective ever found).
     *
     * @return the flag
     */
    public boolean shouldUseNadirIncumbent()
    {
        return _useNadirIncumbent;
    }

    /**
     * Setter for an auxiliary object (can be null) responsible for customizing objective space manager params container
     * built when the updateOSDynamically flag is set to true (otherwise, the manager should be set to null).
     *
     * @param osParamsAdjuster the params adjuster
     * @return EMOA builder being parameterized
     */
    public AbstractEMOABuilder<T> setOSMParamsAdjuster(ObjectiveSpaceManager.IParamsAdjuster osParamsAdjuster)
    {
        _osParamsAdjuster = osParamsAdjuster;
        return this;
    }

    /**
     * Getter for the auxiliary object responsible for customizing objective space manager params container built when
     * the updateOSDynamically flag is set to true (otherwise, the manager can be null).
     *
     * @return EMOA builder being parameterized
     */
    public ObjectiveSpaceManager.IParamsAdjuster getOSMParamsAdjuster()
    {
        return _osParamsAdjuster;
    }

    /**
     * Setter for the optimization criteria. It is retrieved from the provided problem bundle.
     *
     * @param problemBundle problem bundle
     * @return EA builder being parameterized
     */
    public AbstractEMOABuilder<T> setCriteria(AbstractMOOProblemBundle problemBundle)
    {
        _criteria = problemBundle._criteria;
        return this;
    }

    /**
     * Setter for the optimization criterion.
     *
     * @param criteria optimization criterion
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setCriteria(Criteria criteria)
    {
        _criteria = criteria;
        return this;
    }

    /**
     * Auxiliary method for performing a simple data validation. It is called by default by
     * {@link AbstractEABuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    protected void validate() throws EAException
    {
        super.validate();
        if (_osBoundsLearningPolicy == null)
            throw EAException.getInstanceWithSource("No OS bounds learning policy has been set", this.getClass());
    }
}
