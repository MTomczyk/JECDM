package population;

import java.util.ArrayList;

/**
 * Class representing a container for specimens maintained by the EA (wraps e.g., current population, mating pool, offspring).
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
     * A field representing a mating pool. Can be used if needed. It can be, e.g., considered as a selected subset of specimens from the current population.
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
    }

    /**
     * Auxiliary method for merging the population with the offspring.
     */
    public void mergeOffspringAndPopulation()
    {
        getPopulation().addAll(getOffspring());
    }

    /**
     * Auxiliary method that clears all data linked to the reproduction step (offspring, parents, and mating pool are nulled).
     */
    public void clearReproductionData()
    {
        setOffspring(null);
        setParents(null);
        setMatingPool(null);
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
        this._populationRequiresEvaluation = populationRequiresEvaluation;
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
        this._offspringRequiresEvaluation = offspringRequiresEvaluation;
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
        this._populationRequiresIDAssignment = populationRequiresIDAssignment;
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
        this._offspringRequiresIDAssignment = offspringRequiresIDAssignment;
    }
}
