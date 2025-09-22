package ea;

import criterion.Criteria;
import criterion.Criterion;
import exception.EAException;
import phase.IConstruct;
import phase.IEvaluate;
import problem.AbstractProblemBundle;
import random.IRandom;
import reproduction.*;
import selection.ISelect;

/**
 * Provides various means for constructing instances of evolutionary algorithms. Its non-static fields, and methods
 * constitute a simple data container and helpful parameterization means.
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class AbstractEABuilder<T extends EA>
{
    /**
     * Optional ID of the algorithm.
     */
    protected int _id = 0;

    /**
     * Algorithm's name.
     */
    protected String _name = null;

    /**
     * Population size.
     */
    protected int _populationSize = 0;

    /**
     * The random number generator.
     */
    protected final IRandom _R;

    /**
     * Optimization criteria assumed.
     */
    protected Criteria _criteria = null;

    /**
     * Parents selector
     */
    protected ISelect _select = null;

    /**
     * Specimens constructor;
     */
    protected IConstruct _construct = null;

    /**
     * Specimens evaluator;
     */
    protected IEvaluate _evaluate = null;

    /**
     * Specimens reproducer
     */
    protected IReproduce _reproduce = null;

    /**
     * An auxiliary object (can be null) responsible for customizing the EA's params container being parameterized. It
     * is assumed that the parameterization is done after the default parameterization is completed.
     */
    protected EA.IParamsAdjuster _eaParamsAdjuster = null;

    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    protected AbstractEABuilder(IRandom R)
    {
        _R = R;
    }

    /**
     * Setter for the algorithm's id.
     *
     * @param id the algorithm's id
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setID(int id)
    {
        _id = id;
        return this;
    }

    /**
     * Getter for the random number generator.
     *
     * @return the random number generator
     */
    public IRandom getR()
    {
        return _R;
    }

    /**
     * Getter for the algorithm's id.
     *
     * @return getter for the algorithm's id
     */
    public int getID()
    {
        return _id;
    }

    /**
     * Setter for the algorithm's name
     *
     * @param name the algorithm's name
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setName(String name)
    {
        _name = name;
        return this;
    }

    /**
     * Getter for the algorithm's name.
     *
     * @return the algorithm's name
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Setter for the population size.
     *
     * @param populationSize population size
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setPopulationSize(int populationSize)
    {
        _populationSize = populationSize;
        return this;
    }

    /**
     * Getter for the population size.
     *
     * @return the population size
     */
    public int getPopulationSize()
    {
        return _populationSize;
    }

    /**
     * Setter for the optimization criterion. It is retrieved from the provided problem bundle.
     *
     * @param problemBundle problem bundle
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setCriterion(AbstractProblemBundle problemBundle)
    {
        _criteria = problemBundle._criteria;
        return this;
    }

    /**
     * Setter for the optimization criterion.
     *
     * @param criterion optimization criterion
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setCriterion(Criterion criterion)
    {
        _criteria = Criteria.constructCriteria(criterion);
        return this;
    }

    /**
     * Getter for the criteria.
     *
     * @return the criteria
     */
    public Criteria getCriteria()
    {
        return _criteria;
    }

    /**
     * Setter for parents selector.
     *
     * @param select parents selector
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsSelector(ISelect select)
    {
        _select = select;
        return this;
    }


    /**
     * Setter for the problem-specific implementations ({@link IConstruct}, {@link IEvaluate}, and {@link IReproduce}).
     * They are retrieved from the provided problem bundle.
     *
     * @param problemBundle problem bundle
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setProblemImplementations(AbstractProblemBundle problemBundle)
    {
        return setProblemImplementations(problemBundle._construct, problemBundle._evaluate, problemBundle._reproduce);
    }

    /**
     * Setter for the problem-specific implementations ({@link IConstruct}, {@link IEvaluate}, and {@link IReproduce}).
     *
     * @param construct solution constructor
     * @param evaluate  solution evaluator
     * @param reproduce solution reproducer
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setProblemImplementations(IConstruct construct, IEvaluate evaluate, IReproduce reproduce)
    {
        return setInitialPopulationConstructor(construct).setSpecimensEvaluator(evaluate).setParentsReproducer(reproduce);
    }

    /**
     * Getter for parents selector.
     *
     * @return parents selector
     */
    public ISelect getParentsSelector()
    {
        return _select;
    }

    /**
     * Setter for the specimen (solution) constructor ({@link IConstruct}; used when constructing an initial
     * population).
     *
     * @param construct solution constructor
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setInitialPopulationConstructor(IConstruct construct)
    {
        _construct = construct;
        return this;
    }

    /**
     * Getter for the initial population constructor.
     *
     * @return initial population constructor
     */
    public IConstruct getInitialPopulationConstructor()
    {
        return _construct;
    }

    /**
     * Setter for the specimen (solution) evaluator ({@link IEvaluate}; used when evaluating the constructed specimens,
     * i.e., during the creation of the initial population or offspring generation).
     *
     * @param evaluate solution evaluator
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setSpecimensEvaluator(IEvaluate evaluate)
    {
        _evaluate = evaluate;
        return this;
    }

    /**
     * Getter for specimens evaluator.
     *
     * @return specimens evaluator
     */
    public IEvaluate getSpecimensEvaluator()
    {
        return _evaluate;
    }

    /**
     * Setter for the specimen (solution) reproducer ({@link IReproduce}; used when reproducing specimens.
     *
     * @param reproduce solution reproducer
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(IReproduce reproduce)
    {
        _reproduce = reproduce;
        return this;
    }

    /**
     * Setter for the specimen (solution) reproducer ({@link IReproduce}; used when reproducing specimens. This setter
     * sets the method to work with a standard reproducer that operates on double-vectors and assumes that one offspring
     * is to be produced using two parents.
     *
     * @param reproducer standard reproducer that operates on double-vectors and assumes that one offspring is to be
     *                   produced using two parents
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(StandardDoubleReproducer reproducer)
    {
        _reproduce = new DoubleReproduce(reproducer::reproduce);
        return this;
    }

    /**
     * Setter for the specimen (solution) reproducer ({@link IReproduce}; used when reproducing specimens. This setter
     * sets the method to work with a standard reproducer that operates on double-vectors and assumes that two offspring
     * solutions are to be produced using two parents.
     *
     * @param reproducer standard reproducer that operates on double-vectors and assumes that two offspring solutions
     *                   are to be produced using two parents
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(StandardDoubleTOReproducer reproducer)
    {
        _reproduce = DoubleReproduce.getInstanceTO(reproducer::reproduce);
        return this;
    }

    /**
     * Setter for the specimen (solution) reproducers; used when reproducing specimens. This setter sets the method
     * to work with various reproducers that operates on double-vectors and assumes various possible parents -> number
     * of offspring to generate mappings.
     *
     * @param reproducer   standard reproducer that operates on double-vectors and assumes that one offspring solution
     *                     is to be produced using two parents
     * @param reproducerTO standard reproducer that operates on double-vectors and assumes that twp offspring solutions
     *                     are to be produced using two parents
     * @param reproducerMO standard reproducer that operates on double-vectors and assumes that any number of offspring
     *                     solutions (but constant and pre-defined) are to be produced using two parents
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(StandardDoubleReproducer reproducer,
                                                     StandardDoubleTOReproducer reproducerTO,
                                                     StandardDoubleMOReproducer reproducerMO)
    {
        _reproduce = DoubleReproduce.getInstance(
                reproducer::reproduce,
                reproducerTO::reproduce,
                new DoubleReproduce.IReproduceMO[]{
                        new DoubleReproduce.IReproduceMO()
                        {
                            @Override
                            public double[][] reproduce(double[] p1, double[] p2, IRandom R)
                            {
                                return reproducerMO.reproduce(p1, p2, R);
                            }

                            @Override
                            public int getNoOffspring()
                            {
                                return reproducerMO.getNoOffspring();
                            }
                        }
                });
        return this;
    }

    /**
     * Setter for the specimen (solution) reproducer ({@link IReproduce}; used when reproducing specimens. This setter
     * sets the method to work with a standard reproducer that operates on integer-vectors and assumes that one
     * offspring is to be produced using two parents.
     *
     * @param reproducer standard reproducer that operates on integer-vectors and assumes that one offspring is to be
     *                   produced using two parents
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(StandardIntReproducer reproducer)
    {
        _reproduce = new IntReproduce(reproducer::reproduce);
        return this;
    }

    /**
     * Setter for the specimen (solution) reproducer ({@link IReproduce}; used when reproducing specimens. This setter
     * sets the method to work with a standard reproducer that operates on integer-vectors and assumes that two
     * offspring solutions are to be produced using two parents.
     *
     * @param reproducer standard reproducer that operates on integer-vectors and assumes that two offspring solutions
     *                   are to be produced using two parents
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(StandardIntTOReproducer reproducer)
    {
        _reproduce = IntReproduce.getInstanceTO(reproducer::reproduce);
        return this;
    }

    /**
     * Setter for the specimen (solution) reproducers; used when reproducing specimens. This setter sets the method
     * to work with various reproducers that operates on integer-vectors and assumes various possible parents -> number
     * of offspring to generate mappings.
     *
     * @param reproducer   standard reproducer that operates on integer-vectors and assumes that one offspring solution
     *                     is to be produced using two parents
     * @param reproducerTO standard reproducer that operates on integer-vectors and assumes that twp offspring solutions
     *                     are to be produced using two parents
     * @param reproducerMO standard reproducer that operates on integer-vectors and assumes that any number of offspring
     *                     solutions (but constant and pre-defined) are to be produced using two parents
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(StandardIntReproducer reproducer,
                                                     StandardIntTOReproducer reproducerTO,
                                                     StandardIntMOReproducer reproducerMO)
    {
        _reproduce = IntReproduce.getInstance(
                reproducer::reproduce,
                reproducerTO::reproduce,
                new IntReproduce.IReproduceMO[]{
                        new IntReproduce.IReproduceMO()
                        {
                            @Override
                            public int[][] reproduce(int[] p1, int[] p2, IRandom R)
                            {
                                return reproducerMO.reproduce(p1, p2, R);
                            }

                            @Override
                            public int getNoOffspring()
                            {
                                return reproducerMO.getNoOffspring();
                            }
                        }
                });
        return this;
    }


    /**
     * Setter for the specimen (solution) reproducer ({@link IReproduce}; used when reproducing specimens. This setter
     * sets the method to work with a standard reproducer that operates on boolean-vectors and assumes that one
     * offspring is to be produced using two parents.
     *
     * @param reproducer standard reproducer that operates on boolean-vectors and assumes that one offspring is to be
     *                   produced using two parents
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(StandardBoolReproducer reproducer)
    {
        _reproduce = new BoolReproduce(reproducer::reproduce);
        return this;
    }

    /**
     * Setter for the specimen (solution) reproducer ({@link IReproduce}; used when reproducing specimens. This setter
     * sets the method to work with a standard reproducer that operates on boolean-vectors and assumes that two
     * offspring solutions are to be produced using two parents.
     *
     * @param reproducer standard reproducer that operates on boolean-vectors and assumes that two offspring solutions
     *                   are to be produced using two parents
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(StandardBoolTOReproducer reproducer)
    {
        _reproduce = BoolReproduce.getInstanceTO(reproducer::reproduce);
        return this;
    }

    /**
     * Setter for the specimen (solution) reproducers; used when reproducing specimens. This setter sets the method
     * to work with various reproducers that operates on boolean-vectors and assumes various possible parents -> number
     * of offspring to generate mappings.
     *
     * @param reproducer   standard reproducer that operates on boolean-vectors and assumes that one offspring solution
     *                     is to be produced using two parents
     * @param reproducerTO standard reproducer that operates on boolean-vectors and assumes that twp offspring solutions
     *                     are to be produced using two parents
     * @param reproducerMO standard reproducer that operates on boolean-vectors and assumes that any number of offspring
     *                     solutions (but constant and pre-defined) are to be produced using two parents
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setParentsReproducer(StandardBoolReproducer reproducer,
                                                     StandardBoolTOReproducer reproducerTO,
                                                     StandardBoolMOReproducer reproducerMO)
    {
        _reproduce = BoolReproduce.getInstance(
                reproducer::reproduce,
                reproducerTO::reproduce,
                new BoolReproduce.IReproduceMO[]{
                        new BoolReproduce.IReproduceMO()
                        {
                            @Override
                            public boolean[][] reproduce(boolean[] p1, boolean[] p2, IRandom R)
                            {
                                return reproducerMO.reproduce(p1, p2, R);
                            }

                            @Override
                            public int getNoOffspring()
                            {
                                return reproducerMO.getNoOffspring();
                            }
                        }
                });
        return this;
    }

    /**
     * Getter for parents reproducer.
     *
     * @return parents reproducer
     */
    public IReproduce getParentsReproducer()
    {
        return _reproduce;
    }

    /**
     * Setter for the EA params adjuster (can be null): Auxiliary object responsible for customizing the EA's params
     * container being parameterized. It is assumed that the parameterization is done after its default parameterization
     * is completed.
     *
     * @param eaParamsAdjuster EA params adjuster
     * @return EA builder being parameterized
     */
    public AbstractEABuilder<T> setEAParamsAdjuster(EA.IParamsAdjuster eaParamsAdjuster)
    {
        _eaParamsAdjuster = eaParamsAdjuster;
        return this;
    }

    /**
     * Getter for the EA params adjuster: Auxiliary object responsible for customizing the EA's params container being
     * parameterized. It is assumed that the parameterization is done after its default parameterization is completed.
     *
     * @return EA params adjuster
     */
    public EA.IParamsAdjuster getEAParamsAdjuster()
    {
        return _eaParamsAdjuster;
    }

    /**
     * Auxiliary method for performing a simple data validation. It is called by default by
     * {@link AbstractEABuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    protected void validate() throws EAException
    {
        if (_R == null)
            throw EAException.getInstanceWithSource("The random number generator is not provided", this.getClass());
        if (_populationSize < 1)
            throw EAException.getInstanceWithSource("The population size should not be less than 1 (equals = " + _populationSize + ")", this.getClass());
        if (_criteria == null)
            throw EAException.getInstanceWithSource("The criterion (or criteria) is (are) not provided", this.getClass());
        if (_select == null)
            throw EAException.getInstanceWithSource("The parents selector is not provided", this.getClass());
        if (_construct == null)
            throw EAException.getInstanceWithSource("The initial population constructor is not provided", this.getClass());
        if (_evaluate == null)
            throw EAException.getInstanceWithSource("The specimen evaluator is not provided", this.getClass());
        if (_reproduce == null)
            throw EAException.getInstanceWithSource("The parents reproducer is not provided", this.getClass());
    }

    /**
     * The main method for instantiating the EA (to be overwritten). Calls {@link AbstractEABuilder#validate()}.
     * IMPORTANT: Only one instance should be created, as it is provided with references to objects stored in the
     * builder, not their clones.
     *
     * @return instance of the algorithm
     * @throws EAException an exception can be thrown and propagated higher.
     */
    protected T getInstance() throws EAException
    {
        validate();
        return null; // to be overwritten
    }
}
