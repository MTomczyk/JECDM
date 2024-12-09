package emo.utils.decomposition;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.family.FamilyID;
import emo.utils.decomposition.goal.IGoal;
import space.normalization.INormalization;

/**
 * Abstract class providing various functionalities for maintaining/processing goals/assignments in a decomposition-based
 * algorithm (primarily MOEA/D, but it can be suitably adapted to various other algorithmic purposes).
 *
 * @author MTomczyk
 */
public abstract class AbstractGoalsManager
{
    /**
     * Params container.
     */
    protected static class Params
    {
        /**
         * Optimization goals (matrix: each entry is a separate family).
         */
        public IGoal[][] _goals;

        /**
         * Parameterized constructor. Establishes one-family decomposition data.
         *
         * @param goals optimization goals (the only family)
         */
        public Params(IGoal[] goals)
        {
            this(new IGoal[][]{goals});
        }

        /**
         * Parameterized constructor. Establishes several families.
         *
         * @param goals optimization goals (matrix: each entry is a separate family)
         */
        public Params(IGoal[][] goals)
        {
            _goals = goals;
        }
    }

    /**
     * Goals families (is there is more than one, the method can, e.g., be run in a co-evolutionary mode).
     */
    protected final Family[] _F;

    /**
     * Represents mapping (family/goal) -> (population) specimen array index. The default implementations implicitly
     * assume that one goal should correspond to one population member. This abstract class instantiates this linkage
     * by iterating over all families and their goals and incrementally assigning subsequent indices, starting from 0.
     */
    protected int[][] _goalToSpecimenArrayIndex;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    protected AbstractGoalsManager(Params p)
    {
        _F = new Family[p._goals.length];
        for (int f = 0; f < p._goals.length; f++) _F[f] = new Family(new FamilyID(f), p._goals[f]);
        instantiateGoalToSpecimenArrayIndex(p);
    }

    /**
     * Auxiliary method for instantiating {@link AbstractGoalsManager#_goalToSpecimenArrayIndex}.
     *
     * @param p params container
     */
    protected void instantiateGoalToSpecimenArrayIndex(Params p)
    {
        _goalToSpecimenArrayIndex = new int[_F.length][];
        int arrayID = 0;
        for (int f = 0; f < _F.length; f++)
        {
            int goals = _F[f].getGoals().length;
            _goalToSpecimenArrayIndex[f] = new int[goals];
            for (int g = 0; g < goals; g++) _goalToSpecimenArrayIndex[f][g] = arrayID++;
        }
    }

    /**
     * Can be called to update normalizations maintained by the goals. At also requires reevaluating the scores linked
     * to their assigned solutions.
     *
     * @param normalizations new normalizations used to rescale objective function values
     */
    public void updateNormalizations(INormalization[] normalizations)
    {
        for (Family F : _F)
            for (IGoal G : F.getGoals())
                G.updateNormalizations(normalizations);
    }

    /**
     * Getter for the families of goals.
     *
     * @return families of goals
     */
    public Family[] getFamilies()
    {
        return _F;
    }
}
