package space.normalization.builder;

import space.normalization.INormalization;
import space.normalization.minmax.AbstractMinMaxNormalization;
import space.normalization.minmax.Linear;
import space.normalization.minmax.LinearWithFlip;
import space.os.ObjectiveSpace;

/**
 * Builds min-max normalizations based on the info on the objective space. All objective values are scaled
 * to 0-1 bounds. It is assumed that the criteria are of cost type (0-vector = utopia). Hence, when they are of gain type,
 * the min max with flip (threshold = 1.0) is used to reverse the preference order for such criteria.
 *
 * @author MTomczyk
 */
public class StandardLinearBuilder implements INormalizationBuilder
{
    /**
     * Constructs min-max normalizations (one per objective)
     *
     * @param OS objective space objective space
     * @return array of normalizations
     */
    @Override
    public INormalization[] getNormalizations(ObjectiveSpace OS)
    {
        INormalization[] normalizations = new INormalization[OS._criteriaTypes.length];

        for (int c = 0; c < OS._criteriaTypes.length; c++)
        {
            double L = OS._ranges[c].getLeft();
            double R = OS._ranges[c].getRight();

            if (OS._criteriaTypes[c]) normalizations[c] = new LinearWithFlip(L, R, 1.0d);
            else normalizations[c] = new Linear(L, R);
        }
        return normalizations;
    }

    /**
     * Should return the lowest possible class in hierarchy which all objects returned by {@link INormalization} extend.
     *
     * @return class
     */
    @Override
    public Class<?> getBuiltSuperclass()
    {
        return AbstractMinMaxNormalization.class;
    }
}
