package t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea;

import exception.PhaseException;
import phase.IEvaluate;
import population.Specimen;

import java.util.ArrayList;

/**
 * The implementation of the IEvaluate interface for evaluating the solutions to the knapsack problem. The specimens are
 * set to a two-element performance vector: total knapsack value and size. The evaluator can work in two modes: repair
 * and penalty. When the repair mode is on, the infeasible specimens are immediately repaired by removing items with the worst
 * value/size ratio. If the penalty mode is used, the specimens are penalized for having excessive size (see the
 * implementation for details).
 *
 * @author MTomczyk
 */


@SuppressWarnings("DuplicatedCode")
public class KnapsackEvaluator implements IEvaluate
{
    /**
     * Data container.
     */
    private final Data _data;

    /**
     * Knapsack capacity.
     */
    protected final double _capacity;

    /**
     * If true, infeasible solutions will be immediately repaired by removing items from the knapsack in the increasing
     * order of their value/size ratios until the feasibility is restored. If false, the algorithm works in "penalty mode".
     */
    public final boolean _repairMode;

    /**
     * Parameterized constructor.
     *
     * @param data       data container
     * @param capacity   knapsack capacity
     * @param repairMode if true, infeasible solutions will be immediately repaired by removing items from the knapsack
     *                   in the increasing order of their value/size ratios until the feasibility is restored;
     *                   if false, the algorithm works in "penalty mode"
     */
    public KnapsackEvaluator(Data data, double capacity, boolean repairMode)
    {
        _data = data;
        _capacity = capacity;
        _repairMode = repairMode;
    }

    /**
     * Evaluates specimens.
     *
     * @param specimens array of specimens to be evaluated
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void evaluateSpecimens(ArrayList<Specimen> specimens) throws PhaseException
    {
        for (Specimen s : specimens)
        {
            boolean[] used = s.getBooleanDecisionVector(); // get the decision vector

            double value = 0.0d;
            int size = 0;
            for (int i = 0; i < _data._items; i++) // update knapsack total size and value
                if (used[i])
                {
                    value += _data._values[i];
                    size += _data._sizes[i];
                }

            if ((_repairMode) && (size > _capacity)) // if the repair mode is used
            {
                for (int index : _data._sortedIndices)
                {
                    if (!used[index]) continue; // if item is used, it cannot be removed
                    used[index] = false; // remove the item
                    size -= _data._sizes[index]; // update the value
                    value -= _data._values[index]; // update the size
                    if (size <= _capacity) break; // break if the feasibility is restored
                }

                // Extra: some extra space may be left; one may try adding items that fit (starting from the ones
                // that have the best value/size ratio.

                // not really needed, the ``getBooleanDecisionVector'' method returns a reference (not copy)
                //s.setBooleanDecisionVector(used);
            }

            s.setEvaluations(new double[]{value, size}); // set the performance vector
            s.setAuxScore(value);

            if ((!_repairMode) && (size > _capacity))// if the penalty mode is used
                s.setAuxScore(_capacity - size);
        }
    }
}
