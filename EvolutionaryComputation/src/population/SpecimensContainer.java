package population;

import java.util.ArrayList;

/**
 * Class representing a container for specimens maintained by the EA (wraps e.g., current population, mating pool,
 * offspring).
 *
 * @author MTomczyk
 */
public class SpecimensContainer
{
    /**
     * The population = array of specimens.
     */
    private ArrayList<Specimen> _population;

    /**
     * Array of parents selected for mating.
     */
    private ArrayList<Parents> _parents;

    /**
     * The generated offspring.
     */
    private ArrayList<Specimen> _offspring;

    /**
     * A field representing a mating pool. Can be used if needed. It can be, e.g., considered as a selected subset of
     * specimens from the current population.
     */
    private ArrayList<Specimen> _matingPool;

    /**
     * Auxiliary flag indicating whether the population requires evaluation.
     */
    private boolean _populationRequiresEvaluation = false;

    /**
     * Auxiliary flag indicating whether the offspring requires evaluation.
     */
    private boolean _offspringRequiresEvaluation = false;

    /**
     * Auxiliary flag indicating whether the population requires ID assignment.
     */
    private boolean _populationRequiresIDAssignment = false;

    /**
     * Auxiliary flag indicating whether the offspring requires ID assignment.
     */
    private boolean _offspringRequiresIDAssignment = false;

    /**
     * Auxiliary field that measures performed function evaluations. Updated by default by the {@link phase.Evaluate}
     * phase.
     */
    private int _performedFunctionEvaluations;

    /**
     * Auxiliary field that stores the data on how many specimens have been constructed throughout the generation being
     * processed so far.
     */
    private int _specimensConstructedDuringGeneration = 0;

    /**
     * Default constructor.
     */
    public SpecimensContainer()
    {
        this(null, null, null, null);
    }

    /**
     * Parameterized constructor that accepts an array of specimens and treats it as the current population.
     *
     * @param population array of specimens.
     */
    public SpecimensContainer(ArrayList<Specimen> population)
    {
        this(population, null, null, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param population array of specimens that is considered the current population
     * @param parents    parents selected for mating
     * @param offspring  constructed offspring
     * @param matingPool mating pool
     */
    public SpecimensContainer(ArrayList<Specimen> population,
                              ArrayList<Parents> parents,
                              ArrayList<Specimen> offspring,
                              ArrayList<Specimen> matingPool)
    {
        setPopulation(population);
        setParents(parents);
        setOffspring(offspring);
        setMatingPool(matingPool);
        _performedFunctionEvaluations = 0;
    }

    /**
     * Auxiliary method for merging the population with the offspring.
     */
    public void mergeOffspringAndPopulation()
    {
        getPopulation().addAll(getOffspring());
    }

    /**
     * Auxiliary method that clears all data linked to the reproduction step (offspring, parents, and mating pool are
     * nulled).
     *
     * @deprecated the method will be removed in the future
     */
    @Deprecated
    public void clearReproductionData()
    {
        setOffspring(null);
        setParents(null);
        setMatingPool(null);
    }

    /**
     * Getter for the value that indicates how many specimens have been constructed throughout the generation being
     * processed so far.
     *
     * @return the number of specimens have been constructed throughout the generation being processed so far.
     */
    public int getSpecimensConstructedDuringGeneration()
    {
        return _specimensConstructedDuringGeneration;
    }

    /**
     * Resets the counter that indicates how many specimens have been constructed throughout the generation being
     * processed so far.
     */
    public void resetSpecimensConstructedDuringGenerationCounter()
    {
        _specimensConstructedDuringGeneration = 0;
    }

    /**
     * Increments the counter that indicates how many specimens have been constructed throughout the generation being
     * processed so far.
     *
     * @param v indicates by how much the counter will be increased
     */
    public void incrementSpecimensConstructedDuringGenerationCounter(int v)
    {
        _specimensConstructedDuringGeneration += v;
    }


    /**
     * Getter for the population.
     *
     * @return population (array of specimens)
     */
    public ArrayList<Specimen> getPopulation()
    {
        return _population;
    }

    /**
     * Setter for the population.
     *
     * @param population array of specimens
     */
    public void setPopulation(ArrayList<Specimen> population)
    {
        _population = population;
    }

    /**
     * Getter for the parents.
     *
     * @return parents selected for mating
     */
    public ArrayList<Parents> getParents()
    {
        return _parents;
    }

    /**
     * Setter for the parents.
     *
     * @param parents array of parents
     */
    public void setParents(ArrayList<Parents> parents)
    {
        _parents = parents;
    }

    /**
     * Getter for the offspring.
     *
     * @return constructed offspring
     */
    public ArrayList<Specimen> getOffspring()
    {
        return _offspring;
    }

    /**
     * Setter for the offspring.
     *
     * @param offspring array of specimens (offspring)
     */
    public void setOffspring(ArrayList<Specimen> offspring)
    {
        _offspring = offspring;
    }

    /**
     * Getter for the mating pool.
     *
     * @return mating pool
     */
    public ArrayList<Specimen> getMatingPool()
    {
        return _matingPool;
    }

    /**
     * setter for the mating pool.
     *
     * @param matingPool mating pool
     */
    public void setMatingPool(ArrayList<Specimen> matingPool)
    {
        _matingPool = matingPool;
    }

    /**
     * Getter for the flag indicating whether the population requires evaluation.
     *
     * @return flag indicating whether the population requires evaluation
     */
    public boolean isPopulationRequiringEvaluation()
    {
        return _populationRequiresEvaluation;
    }

    /**
     * Setter for the flag indicating whether the population requires evaluation.
     *
     * @param populationRequiresEvaluation flag indicating whether the population requires evaluation
     */
    public void setPopulationRequiresEvaluation(boolean populationRequiresEvaluation)
    {
        _populationRequiresEvaluation = populationRequiresEvaluation;
    }

    /**
     * Getter for the flag indicating whether the offspring requires evaluation.
     *
     * @return flag indicating whether the offspring requires evaluation.
     */
    public boolean isOffspringRequiringEvaluation()
    {
        return _offspringRequiresEvaluation;
    }

    /**
     * Setter for the flag indicating whether the offspring requires evaluation.
     *
     * @param offspringRequiresEvaluation flag indicating whether the offspring requires evaluation.
     */
    public void setOffspringRequiresEvaluation(boolean offspringRequiresEvaluation)
    {
        _offspringRequiresEvaluation = offspringRequiresEvaluation;
    }


    /**
     * Getter for the flag indicating whether the population requires ID assignment.
     *
     * @return flag indicating whether the population requires ID assignment
     */
    public boolean isPopulationRequiringIDAssignment()
    {
        return _populationRequiresIDAssignment;
    }

    /**
     * Setter for the flag indicating whether the population requires ID assignment.
     *
     * @param populationRequiresIDAssignment flag indicating whether the population requires ID assignment
     */
    public void setPopulationRequiresIDAssignment(boolean populationRequiresIDAssignment)
    {
        _populationRequiresIDAssignment = populationRequiresIDAssignment;
    }

    /**
     * Getter for the flag indicating whether the offspring requires ID assignment.
     *
     * @return flag indicating whether the offspring requires ID assignment.
     */
    public boolean isOffspringRequiringIDAssignment()
    {
        return _offspringRequiresIDAssignment;
    }

    /**
     * Setter for the flag indicating whether the offspring requires ID assignment.
     *
     * @param offspringRequiresIDAssignment flag indicating whether the offspring requires ID assignment.
     */
    public void setOffspringRequiresIDAssignment(boolean offspringRequiresIDAssignment)
    {
        _offspringRequiresIDAssignment = offspringRequiresIDAssignment;
    }

    /**
     * Auxiliary method that can be called to increment the function evaluations counter. Updated by default by the
     * {@link phase.Evaluate} phase.
     *
     * @param no the number by which the counter will be incremented
     */
    public void incrementPerformedFunctionEvaluations(int no)
    {
        _performedFunctionEvaluations += no;
    }

    /**
     * Getter for the number of performed function evaluations.
     *
     * @return the number of performed function evaluations
     */
    public int getNoPerformedFunctionEvaluations()
    {
        return _performedFunctionEvaluations;
    }

    /**
     * This auxiliary methods reads all {@link Parents} objects and sums their {@link Parents#_noOffspringToConstruct}
     * field values. If no Parents are stored in the container, the method returns 0.
     *
     * @return the total expected number of offspring to construct
     */
    public int getNoExpectedOffspringToConstruct()
    {
        if (_parents == null) return 0;
        int o = 0;
        for (Parents p : _parents) o += p._noOffspringToConstruct;
        return o;
    }
}
