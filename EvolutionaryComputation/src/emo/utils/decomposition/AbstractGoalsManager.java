package emo.utils.decomposition;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.family.FamilyID;
import emo.utils.decomposition.goal.GoalWrapper;
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
    protected Family[] _F;

    /**
     * Represents mapping (family/goal) -> (population) specimen array index. The default implementations implicitly
     * assume that one goal should correspond to one population member. This abstract class instantiates this linkage
     * by iterating over all families and their goals and incrementally assigning subsequent indices, starting from 0.
     */
    protected int[][] _goalToSpecimenArrayIndex;

    /**
     * Complementary data to {@link AbstractGoalsManager#_goalToSpecimenArrayIndex}. This array tells where each i-th
     * family (i-th index in the array) is mapped into the specimen array (starting indices).
     */
    protected int[] _familyToSpecimenArrayIndex;

    /**
     * Field reporting the total number of goals (in all families).
     */
    protected int _totalNoGoals;

    /**
     * Parameterized constructor. The method prematurely terminates if the goals-related fields are null (arrays
     * of length = 0 are accepted, it will result in families of size 0).
     *
     * @param p params container
     */
    protected AbstractGoalsManager(Params p)
    {
        _F = null;
        _familyToSpecimenArrayIndex = null;
        _goalToSpecimenArrayIndex = null;
        _totalNoGoals = 0;
        if (!isGoalsMatrixValid(p._goals)) return;
        instantiateBasicData(p._goals);
    }

    /**
     * Auxiliary method that checks if all elements of the input goals matrix are not null.
     *
     * @param goals goals matrix
     * @return true, if the elements are not null; false otherwise
     */
    protected boolean isGoalsMatrixValid(IGoal[][] goals)
    {
        if (goals == null) return false;
        for (IGoal[] gs : goals)
        {
            if (gs == null) return false;
            for (IGoal g : gs) if (g == null) return false;
        }
        return true;
    }

    /**
     * Auxiliary method for instantiating basic data (families, IDs, etc.) based on the input goals matrix.
     *
     * @param goals goals matrix (each row correspond to a different family).
     */
    protected void instantiateBasicData(IGoal[][] goals)
    {
        _totalNoGoals = 0;
        _F = new Family[goals.length];
        for (int f = 0; f < goals.length; f++)
        {
            _F[f] = new Family(new FamilyID(f), goals[f]);
            _totalNoGoals += _F[f].getSize();
        }

        instantiateGoalToSpecimenArrayIndex();
        instantiateFamilyToSpecimenArrayIndex();
    }


    /**
     * Auxiliary method for instantiating {@link AbstractGoalsManager#_goalToSpecimenArrayIndex}.
     */
    private void instantiateGoalToSpecimenArrayIndex()
    {
        if (_F == null)
        {
            _goalToSpecimenArrayIndex = null;
            return;
        }

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
     * Auxiliary method for instantiating {@link AbstractGoalsManager#_familyToSpecimenArrayIndex}.
     */
    private void instantiateFamilyToSpecimenArrayIndex()
    {
        if (_F == null)
        {
            _familyToSpecimenArrayIndex = null;
            return;
        }

        _familyToSpecimenArrayIndex = new int[_F.length];
        int arrayIndex = 0;
        for (int f = 0; f < _F.length; f++)
        {
            _familyToSpecimenArrayIndex[f] = arrayIndex;
            arrayIndex += _F[f].getSize();
        }
    }

    /**
     * Auxiliary method that restructures the families-related data based on the new goals matrix. Note that this
     * implementation also implicitly forgets assignments, etc, as new data structures are created.
     *
     * @param goals new goals matrix (each row correspond to a different family)
     */
    protected void restructure(IGoal[][] goals)
    {
        _F = null;
        _familyToSpecimenArrayIndex = null;
        _goalToSpecimenArrayIndex = null;
        _totalNoGoals = 0;
        instantiateBasicData(goals);
    }

    /**
     * Can be called to update normalizations maintained by the goals. At also requires reevaluating the scores linked
     * to their assigned solutions.
     *
     * @param normalizations new normalizations used to rescale objective function values
     */
    public void updateNormalizations(INormalization[] normalizations)
    {
        if (_F == null) return;
        for (Family F : _F)
        {
            if (F == null) continue;
            for (IGoal G : F.getGoals())
                G.updateNormalizations(normalizations);
        }
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

    /**
     * Auxiliary method that validates basic data structures, e.g., IDs, array sizes, etc. It does not inspect
     * the actual assignments (specimens; they can be null).
     *
     * @return true if the data is valid; false otherwise
     */
    public boolean validate()
    {
        if (_F == null) return false;
        if (_goalToSpecimenArrayIndex == null) return false;
        if (_familyToSpecimenArrayIndex == null) return false;

        if (_F.length != _goalToSpecimenArrayIndex.length) return false;
        if (_F.length != _familyToSpecimenArrayIndex.length) return false;

        int idx = 0;
        int cumulativeIdx = 0;
        for (int f = 0; f < _F.length; f++)
        {
            if (_F[f] == null) return false;
            if (_goalToSpecimenArrayIndex[f] == null) return false;

            if (_F[f].getID() == null) return false;
            if (_F[f].getID().getArrayIndex() != f) return false;

            if (_F[f].getSize() != _goalToSpecimenArrayIndex[f].length) return false;

            GoalWrapper[] gws = _F[f].getGoals();
            for (int g = 0; g < _F[f].getSize(); g++)
            {
                if (_goalToSpecimenArrayIndex[f][g] != idx++) return false;
                if (gws[g].getID() == null) return false;
                if (gws[g].getID().getFamilyArrayIndex() != f) return false;
                if (gws[g].getID().getGoalArrayIndex() != g) return false;
            }
            if (_familyToSpecimenArrayIndex[f] != cumulativeIdx) return false;
            cumulativeIdx += _F[f].getSize();
        }
        return true;
    }
}
