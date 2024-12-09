package emo.utils.decomposition.family;

import emo.utils.decomposition.goal.Assignment;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.goal.GoalWrapper;
import emo.utils.decomposition.goal.IGoal;

import java.util.Arrays;

/**
 * Class representing a family of goals. Goals are stored in an array (in wrappers), and it is assumed that the
 * array index is equivalent to goal id.
 *
 * @author MTomczyk
 */

public class Family
{
    /**
     * Family ID.
     */
    private final FamilyID _id;

    /**
     * Family's optimization goals (wrapped; array index is equivalent to goal id).
     */
    private GoalWrapper[] _goals;

    /**
     * Goals assignments data (i:i linkage between this array and the _goals array).
     */
    private Assignment[] _assignments;

    /**
     * Parameterized constructor. Sets  goals' IDs and instantiates the assignment vector.
     *
     * @param id    unique id assigned to the family
     * @param goals goals belonging to a family
     */
    public Family(FamilyID id, IGoal[] goals)
    {
        _id = id;
        instantiateWrappers(goals);
        _assignments = new Assignment[_goals.length];
    }

    /**
     * Auxiliary method for instantiating goals (as wrappers).
     *
     * @param goals goals to be wrapped
     */
    private void instantiateWrappers(IGoal[] goals)
    {
        if (goals == null)
        {
            _goals = null;
            return;
        }

        _goals = new GoalWrapper[goals.length];
        for (int i = 0; i < goals.length; i++)
            _goals[i] = new GoalWrapper(goals[i], new GoalID(_id, i));
    }

    /**
     * Goals getter.
     *
     * @param arrayIndex goal ID (in-array index).
     * @return goal object
     */
    public GoalWrapper getGoal(int arrayIndex)
    {
        return _goals[arrayIndex];
    }

    /**
     * Goals getter.
     *
     * @return goals
     */
    public GoalWrapper[] getGoals()
    {
        return _goals;
    }

    /**
     * This method sets a new goals array. The new array length is assumed to be the same as the previous one.
     * Apart from setting {@link Family#_goals}, the method resets the elements of {@link Family#_assignments}. Note that
     * the new goals may vastly differ from the previous ones, so re-establishing the neighborhood structure may be
     * required. Additionally, the method instantiates goals' IDs.
     *
     * @param goals new goals
     */
    public void replaceGoals(IGoal[] goals)
    {
        instantiateWrappers(goals);
        Arrays.fill(_assignments, null);
    }

    /**
     * Method that instantiates the assignments objects (goal&lt;---&gt;specimens linkage; without assigning any specimens).
     * The method uses the goals maintained by the family. Therefore, they should not be null.
     */
    public void instantiateDefaultAssignments()
    {
        _assignments = new Assignment[_goals.length];
        for (int g = 0; g < _goals.length; g++)
            _assignments[g] = new Assignment(_goals[g]);
    }

    /**
     * Resets the niche count values (sets to zero) of all goals in the family.
     */
    public void resetAssignmentsNicheCounts()
    {
        for (Assignment a : _assignments) a.resetNicheCount();
    }

    /**
     * Clears the assignments lists (evaluations and specimens).
     */
    public void resetAssignmentsLists()
    {
        for (Assignment a : _assignments) a.clearLists();
    }

    /**
     * Returns the number of goals in the family.
     *
     * @return the number of goals in the family
     */
    public int getSize()
    {
        return _goals.length;
    }

    /**
     * Getter for the unique ID assigned to the family.
     *
     * @return family ID
     */
    public FamilyID getID()
    {
        return _id;
    }

    /**
     * Setter for the goal assignment.
     *
     * @param goalID     goal in-array id
     * @param assignment goal assignments
     */
    public void setAssignment(int goalID, Assignment assignment)
    {
        _assignments[goalID] = assignment;
    }

    /**
     * Getter for the goal assignment.
     *
     * @param goalID goal in-array id
     * @return assignment object
     */
    public Assignment getAssignment(int goalID)
    {
        return _assignments[goalID];
    }


    /**
     * Getter for the goals assignments data (array reference).
     *
     * @return assignments data
     */
    public Assignment[] getAssignments()
    {
        return _assignments;
    }


    /**
     * Overwritten method for comparing families (based on comparing {@link FamilyID}).
     *
     * @param o object to be compared with
     * @return true if families the same; false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof Family)) return false;
        return _id.equals(((Family) o)._id);
    }

    /**
     * Overwritten method for calculating the hash code. The array index stored in {@link FamilyID} is used as the hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return _id.hashCode();
    }

}
