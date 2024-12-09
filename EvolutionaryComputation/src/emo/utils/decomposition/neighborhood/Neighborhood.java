package emo.utils.decomposition.neighborhood;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.goal.GoalID;

/**
 * Class representing goals' neighbors. It is assumed that one instance of this class is linked to one
 * family of goals {@link Family}.
 *
 * @author MTomczyk
 */

public class Neighborhood
{
    /**
     * Neighborhood structure (for one family): P x T matrix where P is the number of goals in the family
     * and T stands for the neighborhood size (T nearest neighbors). Matrix elements are goals locations
     * (specifies family->goal location). Note that i-th object (array) corresponds to the i-th in-family maintained
     * goal, while j-th array[i] element is the j-th nearest neighbor. Also, T[p][0] is always the p-th goal
     * (it is its closest neighbor).
     */
    private final GoalID[][] _N;

    /**
     * Parameterized constructor.
     *
     * @param N neighborhood
     */
    public Neighborhood(GoalID[][] N)
    {
        _N = N;
    }

    /**
     * Gets the neighborhood of a goal
     *
     * @param goalArrayIndex goal ID (i-th position in the array corresponds to goal ID).
     * @return neighborhood: array of the most similar goals (goals IDs).
     */
    public GoalID[] getNeighborhood(int goalArrayIndex)
    {
        return _N[goalArrayIndex];
    }

    /**
     * Getter for the raw neighborhood data (matrix).
     * @return neighborhood data
     */
    public GoalID[][] getNeighborhoodMatrix()
    {
        return _N;
    }

}
