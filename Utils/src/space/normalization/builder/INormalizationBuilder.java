package space.normalization.builder;

import space.normalization.INormalization;
import space.os.ObjectiveSpace;

/**
 * Interface for classes building normalization objects based on the input data on the objective space.
 *
 * @author MTomczyk
 */


public interface INormalizationBuilder
{
    /**
     * Builds an array of normalization objects based on the input objective space.
     *
     * @param OS objective space
     * @return array of normalization objects
     */
    INormalization[] getNormalizations(ObjectiveSpace OS);

    /**
     * Should return the lowest possible class in hierarchy which all objects returned by {@link INormalization} extend.
     *
     * @return class
     */
    Class<?> getBuiltSuperclass();
}
