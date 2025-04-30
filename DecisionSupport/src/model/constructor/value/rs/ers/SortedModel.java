package model.constructor.value.rs.ers;

import model.internals.value.AbstractValueInternalModel;

/**
 * Class representing an element being sorted in {@link ModelsQueue}.
 *
 * @author MTomczyk
 */
public class SortedModel<T extends AbstractValueInternalModel>
{
    /**
     * Unique ID (assigned by {@link ERS}).
     */
    public int _id;

    /**
     * Wrapped model (final, but not immutable).
     */
    public final T _model;

    /**
     * Degree to which the model is compatible with the DM's feedback
     * (thresholded at zero; more than zero = compatible; less or equal = incompatible; the greater the value, the greater the compatibility)
     */
    public double _compatibilityDegree;

    /**
     * Flag indicating whether a model is compatible (if {@link SortedModel#_compatibilityDegree} is non-negative).
     */
    public boolean _isCompatible;

    /**
     * Closest models queue.
     */
    public final ClosestModels<T> _closestModels;

    /**
     * Parameterized constructor.
     *
     * @param id                  model unique id
     * @param k                   size of the closest models queue (k-closest goals kept)
     * @param isLessMeaningClose  if true, smaller similarities means closer; false otherwise
     * @param model               model to be wrapped
     * @param compatibilityDegree compatibility with the preference information
     */
    public SortedModel(int id, int k, boolean isLessMeaningClose, T model, double compatibilityDegree)
    {
        _id = id;
        _model = model;
        _closestModels = new ClosestModels<>(k, isLessMeaningClose);
        updateCompatibilityDegree(compatibilityDegree);
    }

    /**
     * Auxiliary method for updating compatibility degree.
     *
     * @param compatibilityDegree compatibilityDegree
     */
    public void updateCompatibilityDegree(double compatibilityDegree)
    {
        _compatibilityDegree = compatibilityDegree;
        _isCompatible = Double.compare(compatibilityDegree, 0.0d) > 0;
    }
}
