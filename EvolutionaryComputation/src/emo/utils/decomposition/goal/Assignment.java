package emo.utils.decomposition.goal;

import population.Specimen;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Represents the linkage goal &lt;---&gt; assigned specimens (e.g., in MOEA/D, each goal is assigned exactly one specimen;
 * but in NSGA-III the mapping is reversed). Specimens (and their evaluations) are kept stored using lists. The class
 * provides various functionalities that can be adapted by different algorithms. For instance, the method allows
 * inserting new elements into the list using the insertion sort procedure. This way, the specimens can be kept sorted
 * from the best to worst, given their evaluations (used, e.g., in NSGA-II).
 *
 * @author MTomczyk
 */

public class Assignment
{
    /**
     * Reference to the goal.
     */
    private final IGoal _goal;

    /**
     * Reference to the assigned specimens.
     */
    private final LinkedList<Specimen> _specimens;

    /**
     * Keeps evaluations of specimens that are stored in the specimens lists.
     */
    private final LinkedList<Double> _evaluations;

    /**
     * Auxiliary field that can reflect niche count of the goal's surroundings.
     */
    protected double _nicheCount = 0.0f;

    /**
     * Parameterized constructor.
     *
     * @param goal goal linked to the assignment
     */
    public Assignment(IGoal goal)
    {
        _goal = goal;
        _specimens = new LinkedList<>();
        _evaluations = new LinkedList<>();
    }

    /**
     * Getter for the linked goal.
     *
     * @return linked goal
     */
    public IGoal getGoal()
    {
        return _goal;
    }

    /**
     * Getter for the first specimen in the list that is assigned to the goal.
     *
     * @return the first specimen in the link
     */
    public Specimen getFirstSpecimen()
    {
        return _specimens.getFirst();
    }

    /**
     * Setter a new specimen as the first element in the list (if the list was non-empty prior to insertion,
     * the already existing first element is removed).
     *
     * @param specimen assigned specimen
     */
    public void setFirstSpecimen(Specimen specimen)
    {
        if (!_specimens.isEmpty()) _specimens.removeFirst();
        _specimens.addFirst(specimen);
    }


    /**
     * Auxiliary method that returns a specimen from the list with the given index.
     * Also, it removes it from the list as well as its linked evaluation entry from the evaluation list.
     *
     * @param index specimen index
     * @return requested specimen
     */
    public Specimen getSpecimenAndRemoveFromLists(int index)
    {
        Specimen specimen = _specimens.get(index);
        _specimens.remove(index); // ugly but necessary
        _evaluations.remove(index); // ugly but necessary
        return specimen;
    }

    /**
     * Getter for evaluation of the specimen that is stored first.
     *
     * @return evaluation of the first specimen
     */
    public double getFirstSpecimenEvaluation()
    {
        return _evaluations.getFirst();
    }

    /**
     * Setter for the evaluation of a specimens that is stored first in the list.
     *
     * @param evaluation specimen's evaluation
     */
    public void setFirstSpecimenEvaluation(double evaluation)
    {
        if (_specimens.isEmpty()) return;
        if (!_evaluations.isEmpty()) _evaluations.removeFirst();
        _evaluations.add(evaluation);
    }


    /**
     * Can be called to reevaluate stored specimen (e.g., when the goal's normalizations have been updated).
     */
    public void revaluateFirstSpecimen()
    {
        double evaluation = _goal.evaluate(_specimens.getFirst());
        setFirstSpecimenEvaluation(evaluation);
    }

    /**
     * Can be called to clear the specimen and evaluation lists.
     */
    public void clearLists()
    {
        _specimens.clear();
        _evaluations.clear();
    }

    /**
     * The method evaluates the input specimen (given the assigned goal) and inserts it (with evaluation) into the
     * lists in a way that all the elements are kept sorted. It is assumed that the first element is evaluated as best,
     * while the last is evaluated as worst.
     *
     * @param specimen new specimen to be added
     */
    public void insertSpecimen(Specimen specimen)
    {
        double evaluation = _goal.evaluate(specimen);

        if (_specimens.isEmpty()) // list are empty, add the specimen
        {
            _specimens.add(specimen);
            _evaluations.add(evaluation);
            return;
        }

        // compare with the first element
        if ((_goal.isLessPreferred()) && (Double.compare(evaluation, _evaluations.getFirst()) < 0))
        {
            _specimens.addFirst(specimen);
            _evaluations.addFirst(evaluation);
            return;
        }
        else if ((!_goal.isLessPreferred()) && (Double.compare(evaluation, _evaluations.getFirst()) > 0))
        {
            _specimens.addFirst(specimen);
            _evaluations.addFirst(evaluation);
            return;
        } // compare with the last element
        else if ((_goal.isLessPreferred()) && (Double.compare(_evaluations.getLast(), evaluation) <= 0))
        {
            _specimens.add(specimen);
            _evaluations.add(evaluation);
            return;
        }
        else if ((!_goal.isLessPreferred()) && (Double.compare(_evaluations.getLast(), evaluation) >= 0))
        {
            _specimens.add(specimen);
            _evaluations.add(evaluation);
            return;
        }

        // move to regular insertion sort
        ListIterator<Specimen> sIt = _specimens.listIterator();
        ListIterator<Double> eIt = _evaluations.listIterator();

        Double nE = eIt.next();
        while (((_goal.isLessPreferred()) && (Double.compare(nE, evaluation) <= 0)) ||
                ((!_goal.isLessPreferred()) && (Double.compare(nE, evaluation) >= 0)))
        {
            sIt.next();
            nE = eIt.next();
        }

        sIt.add(specimen);
        eIt.previous();
        eIt.add(evaluation);
    }

    /**
     * Resets the niche count value (sets to zero).
     */
    public void resetNicheCount()
    {
        _nicheCount = 0.0d;
    }

    /**
     * Getter for the niche count value.
     *
     * @return niche count value
     */
    public double getNicheCount()
    {
        return _nicheCount;
    }

    /**
     * Increases the niche count value (by 1).
     */
    public void incrementNicheCount()
    {
        _nicheCount++;
    }

    /**
     * Setter for the niche cunt value
     *
     * @param nicheCount niche count value
     */
    public void setNicheCount(double nicheCount)
    {
        _nicheCount = nicheCount;
    }

    /**
     * Getter for the assigned specimens.
     *
     * @return assigned specimens
     */
    public LinkedList<Specimen> getSpecimens()
    {
        return _specimens;
    }

    /**
     * Getter for the assigned specimens.
     *
     * @return assigned specimens
     */
    public LinkedList<Double> getEvaluations()
    {
        return _evaluations;
    }
}
