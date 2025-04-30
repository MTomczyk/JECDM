package model.constructor.value.rs.ers;

import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;

/**
 * Class representing the closest model(s) queue (closest neighbors) to some model (implicit association; the reference to
 * the latter model is not kept). The class is built around a queue and provides a suitable insertion procedure (insertion sort).
 *
 * @author MTomczyk
 */
public class ClosestModels<T extends AbstractValueInternalModel>
{
    /**
     * Closest models (most similar).
     */
    public final ArrayList<T> _models;

    /**
     * Model unique ID (implementation dependent);
     */
    public final int[] _ids;

    /**
     * Similarities.
     */
    public final double[] _similarities;

    /**
     * Reports the number of stored models.
     */
    private int _storedModels;

    /**
     * Queues size.
     */
    private final int _k;

    /**
     * If true, lesser values mean ``more similar'', false otherwise (associated with used similarity measure).
     */
    private final boolean _isLessMeaningCloser;

    /**
     * Parameterized constructor.
     *
     * @param k                   queues capacities (k should be greater equal 1)
     * @param isLessMeaningCloser if true, lesser values mean ``more similar'', false otherwise (associated with used similarity measure)
     */
    public ClosestModels(int k, boolean isLessMeaningCloser)
    {
        if (k < 1) k = 1;
        _k = k;
        _isLessMeaningCloser = isLessMeaningCloser;
        _models = new ArrayList<>(k);
        for (int i = 0; i < k; i++) _models.add(null);
        _ids = new int[k];
        _similarities = new double[k];
        _storedModels = 0;
    }


    /**
     * Clears the data.
     */
    public void reset()
    {
        _storedModels = 0;
        // do not overwrite data for better efficiency
    }

    /**
     * The primary method for inserting a model into the queue. The models are kept sorted from the most similar to the least.
     * IMPORTANT NOTE: do not call if {@link ClosestModels#canAlterTheQueue(double)} returns false (call the latter method
     * to check if to call insert).
     *
     * @param model      model to be inserted
     * @param id         model id (implementation dependent)
     * @param similarity model similarity
     */
    public void insert(T model, int id, double similarity)
    {
        if (_storedModels < _k) _storedModels++;

        int index = _storedModels - 1;
        while ((index > 0) && (isMoreSimilar(similarity, _similarities[index - 1])))
        {
            _models.set(index, _models.get(index - 1));
            _similarities[index] = _similarities[index - 1];
            _ids[index] = _ids[index - 1];
            index--;
        }

        _models.set(index, model);
        _ids[index] = id;
        _similarities[index] = similarity;
    }

    /**
     * Auxiliary method for checking if a candidate model's similarity can be added to the queues
     * (comparison is based with the last element stored).
     *
     * @param candidateSimilarity candidate model's similarity
     * @return true, if the model can be added to the queue
     */
    public boolean canAlterTheQueue(double candidateSimilarity)
    {
        if (_storedModels < _k) return true;
        return isMoreSimilar(candidateSimilarity, _similarities[_storedModels - 1]);
    }

    /**
     * Auxiliary method for checking if a candidate similarity is better than a reference one.
     *
     * @param candidateSimilarity candidate model's similarity
     * @param referenceSimilarity reference similarity
     * @return true, the candidate similarity is better (more similar) than the reference one
     */
    private boolean isMoreSimilar(double candidateSimilarity, double referenceSimilarity)
    {
        if ((_isLessMeaningCloser) && (Double.compare(candidateSimilarity, referenceSimilarity) < 0)) return true;
        return (!_isLessMeaningCloser) && (Double.compare(referenceSimilarity, candidateSimilarity) < 0);
    }

    /**
     * Auxiliary method for checking if a model with a given ID is stored in the queue.
     *
     * @param id requested model id
     * @return false, if the model is not stored; true otherwise
     */
    public boolean isModelWithIDStored(int id)
    {
        if (_storedModels == 0) return false;
        for (int i = 0; i < _storedModels; i++) if (id == _ids[i]) return true;
        return false;
    }

    /**
     * Auxiliary method that replaces id1 (if exists) with id2.
     *
     * @param id1 id1
     * @param id2 id2
     */
    public void replaceIDWith(int id1, int id2)
    {
        for (int i = 0; i < _storedModels; i++)
            if (_ids[i] == id1)
            {
                _ids[i] = id2;
                break;
            }
    }

    /**
     * Returns the number of stored elements.
     *
     * @return stored elements
     */
    public int getNoStoredModels()
    {
        return _storedModels;
    }
}
