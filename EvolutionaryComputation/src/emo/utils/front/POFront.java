package emo.utils.front;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import model.internals.AbstractInternalModel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Auxiliary class providing method for constructing fronts of potential optimality.
 *
 * @author MTomczyk
 */

public class POFront
{

    /**
     * Default constructor.
     */
    public POFront()
    {
    }

    /**
     * Constructs fronts of potential optimality. The potential optimality is determined using a set of input preference
     * model instance. This method assigns each input alternative into a front.
     *
     * @param alternatives alternatives set
     * @param models       preference models
     * @param <T>          definition of the preference model
     * @return fronts of potential optimality (each element in the list is one front; the internal list consists of indices
     * pointing to alternatives in the input alternatives set
     */
    public <T extends AbstractInternalModel> LinkedList<LinkedList<Integer>> getFrontAssignments(AbstractAlternatives<?> alternatives,
                                                                                                 ArrayList<T> models)
    {
        return getFrontAssignments(alternatives, models, alternatives.size());
    }


    /**
     * Constructs fronts of potential optimality. The potential optimality is determined using a set of input preference
     * model instance. This method stops the process after a specified number of assignments is done (the process
     * starts from the first front, construct a single front cannot be interrupted).
     *
     * @param alternatives alternatives set
     * @param models       preference models
     * @param stopAfter    specifier after how many assignments the process should stop
     * @param <T>          definition of the preference model
     * @return fronts of potential optimality (each element in the list is one front; the internal list consists of indices
     * pointing to alternatives in the input alternatives set
     */
    public <T extends AbstractInternalModel> LinkedList<LinkedList<Integer>> getFrontAssignments(AbstractAlternatives<?> alternatives,
                                                                                                 ArrayList<T> models,
                                                                                                 int stopAfter)
    {

        LinkedList<LinkedList<Integer>> fronts = new LinkedList<>();
        int assigned = 0;

        boolean[] passedAlternative = new boolean[alternatives.size()];
        boolean[] selectedAlternative = new boolean[alternatives.size()];
        int selected = 0;


        while ((assigned <= alternatives.size()) && (assigned < stopAfter))
        {

            for (T M : models)
            {
                boolean lessPreferred = M.isLessPreferred();

                Alternative incumbentAlternative = null;
                double incumbentEvaluation = Double.POSITIVE_INFINITY;
                int incumbentIndex = 0;
                if (!lessPreferred) incumbentEvaluation = Double.NEGATIVE_INFINITY;

                for (int i = 0; i < alternatives.size(); i++)
                {
                    if (passedAlternative[i]) continue;
                    Alternative a = alternatives.get(i);

                    double ce = M.evaluate(a);
                    if ((incumbentAlternative == null) ||
                            ((lessPreferred) && (Double.compare(ce, incumbentEvaluation) < 0)) ||
                            ((!lessPreferred) && (Double.compare(ce, incumbentEvaluation) > 0))

                    )
                    {
                        incumbentEvaluation = ce;
                        incumbentAlternative = a;
                        incumbentIndex = i;
                    }

                }

                if (incumbentAlternative != null)
                {
                    if (!selectedAlternative[incumbentIndex]) selected++;
                    selectedAlternative[incumbentIndex] = true;
                }
            }

            if (selected > 0)
            {
                LinkedList<Integer> f = new LinkedList<>();
                for (int i = 0; i < selectedAlternative.length; i++)
                {
                    if (selectedAlternative[i])
                    {
                        f.add(i);
                        passedAlternative[i] = true;
                        selectedAlternative[i] = false;
                    }
                }
                selected = 0;
                fronts.add(f);
                assigned += f.size();

            }
            else break;
        }

        return fronts;
    }
}
