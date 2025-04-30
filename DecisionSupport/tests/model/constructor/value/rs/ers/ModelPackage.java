package model.constructor.value.rs.ers;

import model.internals.value.AbstractValueInternalModel;

/**
 * Auxiliary class for testing.
 *
 * @author MTomczyk
 */
class ModelPackage <T extends AbstractValueInternalModel>
{
    /**
     * Model.
     */
    public final T _model;

    /**
     * ID.
     */
    public final int _id;

    /**
     * Similarity.
     */
    public final double _similarity;

    /**
     * Parameterized constructor.
     *
     * @param model      model
     * @param id         id
     * @param similarity similarity
     */
    public ModelPackage(T model, int id, double similarity)
    {
        _model = model;
        _id = id;
        _similarity = similarity;
    }
}
