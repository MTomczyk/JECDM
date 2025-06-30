package phase;

import alternative.Alternative;
import exception.PhaseException;
import population.Specimen;

import java.util.ArrayList;

/**
 * Interface for classes responsible for evaluating specimens (delegates).
 * The default (implicit) assumptions are as follows:
 * - the evaluation result for each specimen will be stored as {@link Specimen#setEvaluations(double[])}
 * (see {@link Alternative#getPerformanceVector()}); when it comes to some auxiliary scores: in
 * {@link Specimen#setAuxScore(double)}; see {@link Alternative#getAuxScores()}.
 *
 * @author MTomczyk
 */
public interface IEvaluate
{
    /**
     * Evaluates specimens.
     * The default (implicit) assumptions are as follows:
     * - the evaluation result for each specimen will be stored as {@link Specimen#setEvaluations(double[])}
     * (see {@link Alternative#getPerformanceVector()}); when it comes to some auxiliary scores: in
     * {@link Specimen#setAuxScore(double)}; see {@link Alternative#getAuxScores()}.
     *
     * @param specimens array of specimens to be evaluated
     * @throws PhaseException the exception can be thrown 
     */
    void evaluateSpecimens(ArrayList<Specimen> specimens) throws PhaseException;
}
