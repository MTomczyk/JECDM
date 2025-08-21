package ea;

import criterion.Criteria;
import phase.*;
import random.IRandom;
import reproduction.BoolReproduce;
import reproduction.ChromosomeReproduce;
import reproduction.DoubleReproduce;
import reproduction.IntReproduce;
import selection.ISelect;

/**
 * Provides various means for constructing instances of an evolutionary algorithm.
 *
 * @author MTomczyk
 */
public class EAFactory
{
    /**
     * This method creates and returns a simple evolutionary algorithm, primarily for single-objective optimization.
     * It employs a straightforward generational scheme, and it is assumed that solutions' decision vectors are defined
     * as double arrays.
     *
     * @param name           name of the algorithm
     * @param populationSize population size (equal offspring size)
     * @param criteria       definitions of considered criteria
     * @param R              random number generator
     * @param constructor    object responsible for constructing initial decision vectors (doubles)
     * @param evaluator      object responsible for evaluating decision vectors (doubles)
     * @param reproducer     object responsible for constructing offspring decision vectors (doubles)
     * @param sortPhase      phase responsible for sorting specimens
     * @param selector       object responsible for selecting parents for reproduction
     * @return instance of an evolutionary algorithm
     */
    public static EA getGenerationalEA(String name,
                                       int populationSize,
                                       Criteria criteria,
                                       IRandom R,
                                       DoubleConstruct.IConstruct constructor,
                                       DoubleEvaluate.IEvaluate evaluator,
                                       DoubleReproduce.IReproduce reproducer,
                                       Sort sortPhase,
                                       ISelect selector)
    {
        EA.Params pEA = getGenerationalEAParams(name, populationSize, criteria, R);
        PhasesBundle phasesBundle = PhasesBundle.getDefaultInstance();
        phasesBundle._constructInitialPopulation = new ConstructInitialPopulation(new DoubleConstruct(constructor));
        phasesBundle._evaluate = new Evaluate(new DoubleEvaluate(evaluator));
        phasesBundle._reproduce = new Reproduce(new DoubleReproduce(reproducer));
        phasesBundle._sort = sortPhase;
        phasesBundle._selectParents = new SelectParents(selector);
        pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(phasesBundle);
        return new EA(pEA);
    }

    /**
     * Creates and returns a default params container for a generational EA (excludes phases).
     *
     * @param name           name of the algorithm
     * @param populationSize population size (equal offspring size)
     * @param criteria       definitions of considered criteria
     * @param R              random number generator
     * @return params container
     */
    private static EA.Params getGenerationalEAParams(String name, int populationSize, Criteria criteria, IRandom R)
    {
        EA.Params pEA = new EA.Params(name, 0, R, true, criteria);
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._osManager = null;
        return pEA;
    }

    /**
     * This method creates and returns a simple evolutionary algorithm, primarily for single-objective optimization.
     * It employs a straightforward generational scheme, and it is assumed that solutions' decision vectors are defined
     * as integer arrays.
     *
     * @param name           name of the algorithm
     * @param populationSize population size (equal offspring size)
     * @param criteria       definitions of considered criteria
     * @param R              random number generator
     * @param constructor    object responsible for constructing initial decision vectors (integers)
     * @param evaluator      object responsible for evaluating decision vectors (integers)
     * @param reproducer     object responsible for constructing offspring decision vectors (integers)
     * @param sortPhase      phase responsible for sorting specimens
     * @param selector       object responsible for selecting parents for reproduction
     * @return instance of an evolutionary algorithm
     */
    public static EA getGenerationalEA(String name,
                                       int populationSize,
                                       Criteria criteria,
                                       IRandom R,
                                       IntConstruct.IConstruct constructor,
                                       IntEvaluate.IEvaluate evaluator,
                                       IntReproduce.IReproduce reproducer,
                                       Sort sortPhase,
                                       ISelect selector)
    {
        EA.Params pEA = getGenerationalEAParams(name, populationSize, criteria, R);
        PhasesBundle phasesBundle = PhasesBundle.getDefaultInstance();
        phasesBundle._constructInitialPopulation = new ConstructInitialPopulation(new IntConstruct(constructor));
        phasesBundle._evaluate = new Evaluate(new IntEvaluate(evaluator));
        phasesBundle._reproduce = new Reproduce(new IntReproduce(reproducer));
        phasesBundle._sort = sortPhase;
        phasesBundle._selectParents = new SelectParents(selector);
        pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(phasesBundle);
        return new EA(pEA);
    }

    /**
     * This method creates and returns a simple evolutionary algorithm, primarily for single-objective optimization.
     * It employs a straightforward generational scheme, and it is assumed that solutions' decision vectors are defined
     * as boolean arrays.
     *
     * @param name           name of the algorithm
     * @param populationSize population size (equal offspring size)
     * @param criteria       definitions of considered criteria
     * @param R              random number generator
     * @param constructor    object responsible for constructing initial decision vectors (booleans)
     * @param evaluator      object responsible for evaluating decision vectors (booleans)
     * @param reproducer     object responsible for constructing offspring decision vectors (booleans)
     * @param sortPhase      phase responsible for sorting specimens
     * @param selector       object responsible for selecting parents for reproduction
     * @return instance of an evolutionary algorithm
     */
    public static EA getGenerationalEA(String name,
                                       int populationSize,
                                       Criteria criteria,
                                       IRandom R,
                                       BoolConstruct.IConstruct constructor,
                                       BoolEvaluate.IEvaluate evaluator,
                                       BoolReproduce.IReproduce reproducer,
                                       Sort sortPhase,
                                       ISelect selector)
    {
        EA.Params pEA = getGenerationalEAParams(name, populationSize, criteria, R);
        PhasesBundle phasesBundle = PhasesBundle.getDefaultInstance();
        phasesBundle._constructInitialPopulation = new ConstructInitialPopulation(new BoolConstruct(constructor));
        phasesBundle._evaluate = new Evaluate(new BoolEvaluate(evaluator));
        phasesBundle._reproduce = new Reproduce(new BoolReproduce(reproducer));
        phasesBundle._sort = sortPhase;
        phasesBundle._selectParents = new SelectParents(selector);
        pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(phasesBundle);
        return new EA(pEA);
    }

    /**
     * This method creates and returns a simple evolutionary algorithm, primarily for single-objective optimization.
     * It employs a straightforward generational scheme, and it is assumed that solutions' decision vectors are defined
     * as double arrays.
     *
     * @param name           name of the algorithm
     * @param populationSize population size (equal offspring size)
     * @param criteria       definitions of considered criteria
     * @param R              random number generator
     * @param constructor    object responsible for constructing initial decision vectors (doubles)
     * @param evaluator      object responsible for evaluating decision vectors (doubles)
     * @param reproducer     object responsible for constructing offspring decision vectors (doubles)
     * @param sortPhase      phase responsible for sorting specimens
     * @param selector       object responsible for selecting parents for reproduction
     * @return instance of an evolutionary algorithm
     */
    public static EA getGenerationalEA(String name,
                                       int populationSize,
                                       Criteria criteria,
                                       IRandom R,
                                       ChromosomeConstruct.IConstruct constructor,
                                       ChromosomeEvaluate.IEvaluate evaluator,
                                       ChromosomeReproduce.IReproduce reproducer,
                                       Sort sortPhase,
                                       ISelect selector)
    {
        EA.Params pEA = getGenerationalEAParams(name, populationSize, criteria, R);
        PhasesBundle phasesBundle = PhasesBundle.getDefaultInstance();
        phasesBundle._constructInitialPopulation = new ConstructInitialPopulation(new ChromosomeConstruct(constructor));
        phasesBundle._evaluate = new Evaluate(new ChromosomeEvaluate(evaluator));
        phasesBundle._reproduce = new Reproduce(new ChromosomeReproduce(reproducer));
        phasesBundle._sort = sortPhase;
        phasesBundle._selectParents = new SelectParents(selector);
        pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(phasesBundle);
        return new EA(pEA);
    }
}
