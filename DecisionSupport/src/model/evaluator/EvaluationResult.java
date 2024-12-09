package model.evaluator;

import print.PrintUtils;

/**
 * Container-like class for storing evaluation results and additional data.
 *
 * @author MTomczyk
 */
public class EvaluationResult
{
    /**
     * Attained scores (each element corresponds to a different alternative, 1:1 connection with registered alternatives)
     */
    public double[] _evaluations;

    /**
     * Time required to perform the evaluation (in ms).
     */
    public long _elapsedTime;

    /**
     * Auxiliary timestamp (System.nanoTime()).
     */
    public long _startTime;


    /**
     * Creates the string representation
     *
     * @return string representation
     */
    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Evaluation time = ").append(_elapsedTime).append(" ms").append(System.lineSeparator());
        sb.append("Evaluation vector = ").append(PrintUtils.getVectorOfDoubles(_evaluations, 2));
        return sb.toString();
    }
}
