package emo.utils.decomposition.goal;

import population.Specimen;
import space.normalization.INormalization;

/**
 * Wrapper for object instances implementing {@link IGoal}. The method, such as MOEA/D, operates internally on wrappers,
 * not the input goals. The wrapper provides method-related auxiliary data, e.g., goal identifier (set via the constructor).
 *
 * @author MTomczyk
 */
public class GoalWrapper implements IGoal
{
    /**
     * Wrapped goal.
     */
    private final IGoal _goal;

    /**
     * Goal ID.
     */
    private final GoalID _ID;


    /**
     * Parameterized constructor.
     *
     * @param goal   wrapped goal (not null)
     * @param goalID assigned goal ID
     */
    public GoalWrapper(IGoal goal, GoalID goalID)
    {
        _goal = goal;
        _ID = goalID;
    }

    /**
     * Can be used to evaluate a specimen.
     *
     * @param specimen specimen object
     * @return specimen score
     */
    @Override
    public double evaluate(Specimen specimen)
    {
        return _goal.evaluate(specimen);
    }

    /**
     * Can be called to update normalizations used to rescale specimen evaluations
     *
     * @param normalizations normalization functions (one per objective)
     */
    @Override
    public void updateNormalizations(INormalization[] normalizations)
    {
        _goal.updateNormalizations(normalizations);
    }

    /**
     * Implementation specific getter for data that can be used, e.g., to quantify the similarity between goals.
     *
     * @return data
     */
    @Override
    public double[][] getParams()
    {
        return _goal.getParams();
    }

    /**
     * Getter for the unique ID assigned to the goal.
     *
     * @return goal ID
     */
    public GoalID getID()
    {
        return _ID;
    }

    /**
     * Getter for the wrapped goal.
     *
     * @return wrapped goal
     */
    public IGoal getGoal()
    {
        return _goal;
    }

    /**
     * Used to determine preference direction (i.e., whether the smaller or bigger values are preferred).
     *
     * @return true -> smaller values are preferred; false otherwise.
     */
    @Override
    public boolean isLessPreferred()
    {
        return _goal.isLessPreferred();
    }
}
