package emo.utils.decomposition.goal;

import population.Specimen;
import space.normalization.INormalization;

/**
 * Representation of a scalarizing optimization function (optimization goal).
 *
 * @author MTomczyk
 */

public interface IGoal
{
    /**
     * Can be used to evaluate a specimen.
     *
     * @param specimen specimen object
     * @return specimen score
     */
    double evaluate(Specimen specimen);

    /**
     * Used to determine preference direction (i.e., whether the smaller or bigger values are preferred).
     * @return true -> smaller values are preferred; false otherwise.
     */
    boolean isLessPreferred();

    /**
     * Can be called to update normalizations used to rescale specimen evaluations
     *
     * @param normalizations normalization functions (one per objective)
     */
    void updateNormalizations(INormalization[] normalizations);

    /**
     * Implementation specific getter for data that can be used, e.g., to quantify the similarity between goals.
     *
     * @return data
     */
    double[][] getParams();


}
