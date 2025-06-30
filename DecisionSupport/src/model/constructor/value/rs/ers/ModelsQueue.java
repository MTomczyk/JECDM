package model.constructor.value.rs.ers;

import compatibility.CompatibilityAnalyzer;
import model.constructor.value.rs.ers.comparators.MostSimilarWithTieResolving;
import model.similarity.ISimilarity;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.internals.value.AbstractValueInternalModel;

import java.util.*;

/**
 * The auxiliary component that keeps the model instances sorted in a queue that takes into account their compatibility
 * and similarity with the closest neighbor.
 *
 * @author MTomczyk
 */
public class ModelsQueue<T extends AbstractValueInternalModel>
{
    /**
     * Sorted queue. It keeps internal models sorted according to two criteria (imposed by the default comparator:
     * {@link MostSimilarWithTieResolving}):
     * 1) The compatibility (compatibility degree should be non-negative, if it is,
     * the models are sorted according to how close their compatibilities are to 0).
     * 2) Similarity with closes model (to be maximized in order to get satisfactorily spaced models)
     */
    private final LinkedList<SortedModel<T>> _queue;

    /**
     * Similarity measure used when comparing two models.
     */
    private final ISimilarity<T> _similarity;

    /**
     * Limit for the queue size.
     */
    private final int _queueLimit;

    /**
     * Represents the number of k most-similar neighbors kept per each model.
     */
    private final int _kMostSimilarNeighbors;

    /**
     * Represent the number of compatible models being stored in the queue.
     */
    private int _noCompatibleModels;

    /**
     * Auxiliary flag that informs whether all stored models are compatible or not.
     */
    private boolean _allStoredModelsAreCompatible;

    /**
     * Compatibility analyzer (used to calculate compatibility degree).
     */
    private final CompatibilityAnalyzer _CA;

    /**
     * Custom comparator object.
     */
    private final AbstractComparator<T> _comparator;

    /**
     * Similarity matrix.
     */
    private final double[][] _simM;

    /**
     * Similarity temp vector.
     */
    private final double[] _simT;

    /**
     * Parameterized constructor.
     *
     * @param queueLimit            limit for the queue size
     * @param kMostSimilarNeighbors represents the number of k most-similar neighbors kept per each model
     * @param CA                    compatibility analyzer (used to calculate compatibility degree)
     * @param comparator            comparator used to sort the models (see the default {@link MostSimilarWithTieResolving}
     * @param similarity            similarity measure used when comparing two models
     */
    public ModelsQueue(int queueLimit,
                       int kMostSimilarNeighbors,
                       CompatibilityAnalyzer CA,
                       AbstractComparator<T> comparator,
                       ISimilarity<T> similarity)
    {
        _queue = new LinkedList<>();
        _queueLimit = Math.max(queueLimit, 1);
        _CA = CA;
        _similarity = similarity;
        _comparator = comparator;
        _comparator.setSimilarity(similarity);
        _allStoredModelsAreCompatible = true;
        _kMostSimilarNeighbors = kMostSimilarNeighbors;
        _simM = new double[_queueLimit][_queueLimit];
        _simT = new double[_queueLimit];
    }

    /**
     * Resets the data.
     */
    public void reset()
    {
        _queue.clear();
        _noCompatibleModels = 0;
        _allStoredModelsAreCompatible = true;
        for (double[] v : _simM) Arrays.fill(v, 0.0d);
        Arrays.fill(_simT, 0.0d);
    }

    /**
     * Can be called to initialize the queue with a batch of models (a batch should be of a size that equals the queue size limit)
     *
     * @param models                models used to initialize the queue
     * @param preferenceInformation feedback
     * @throws ConstructorException the exception can be thrown 
     */
    public void initializeWithBatch(ArrayList<T> models, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        if (_CA == null) throw new ConstructorException("The compatibility analyzer is null", this.getClass());
        if (models == null) throw new ConstructorException("The input model array is null", this.getClass());
        for (T model : models)
            if (model == null) throw new ConstructorException("One of the supplied models is null", this.getClass());
        if (models.size() != _queueLimit)
            throw new ConstructorException("The number of supplied models (" + models.size() + ") does not equal the queue size limit (" + _queueLimit + ")", this.getClass());

        if (!_queue.isEmpty()) reset(); // precaution

        int id = 0;
        for (T model : models)
        {
            Double cd = _CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(preferenceInformation, model);
            if (cd == null)
                throw new ConstructorException("Cannot calculate the most discriminative compatibility with a value model", this.getClass());
            SortedModel<T> sortedModel = new SortedModel<>(id++, _kMostSimilarNeighbors, _similarity.isLessMeaningCloser(), model, cd);
            if (!sortedModel._isCompatible) _allStoredModelsAreCompatible = false;
            else _noCompatibleModels++;
            _queue.add(sortedModel);
        }
    }

    /**
     * Can be called to update the neighborhood structures and sort already-filled queue.
     *
     * @param deriveSimFromM          if true, similarities are derived from the matrix; if false, they are calculated and stored in the matrix
     * @param resetClosestModelsFirst if true, the analyzed closest models are reset before starting the processing
     */
    public void updateClosestModelsAndSortQueue(boolean deriveSimFromM, boolean resetClosestModelsFirst)
    {
        for (SortedModel<T> model : _queue)
        {
            if (resetClosestModelsFirst) model._closestModels.reset();
            updateClosestModels(model, _queue, deriveSimFromM);
        }
        _queue.sort(_comparator);
    }

    /**
     * Auxiliary method for updating the closest model empty of an input model.
     * NOTE that this method updates the data (compares the best-found model with the one  currently set);
     * it does not replace the data.
     *
     * @param model          input model
     * @param list           source of other models
     * @param deriveSimFromM if true, similarities are derived from the matrix; if false, they are calculated and stored in the matrix
     */
    private void updateClosestModels(SortedModel<T> model, LinkedList<SortedModel<T>> list,
                                     boolean deriveSimFromM)
    {
        if (!model._isCompatible) return;
        // Important: only among compatible ones
        for (SortedModel<T> m : list)
        {
            if (!m._isCompatible) continue;
            if (m._id == model._id) continue;
            double s;
            if (deriveSimFromM) s = _simM[model._id][m._id];
            else
            {
                s = _similarity.calculateSimilarity(model._model, m._model);
                _simM[model._id][m._id] = s;
            }
            if (model._closestModels.canAlterTheQueue(s)) model._closestModels.insert(m, m._id, s);
        }
    }

    /**
     * Can be called to reevaluate models' compatibilities.
     *
     * @param preferenceInformation feedback
     * @return if true, no model changed compatibility and all are compatible
     * @throws ConstructorException the exception can be thrown 
     */
    public boolean reevaluateCompatibilities(LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        _noCompatibleModels = _queue.size();
        _allStoredModelsAreCompatible = true;
        boolean changed = false;
        boolean pc;
        for (SortedModel<T> model : _queue)
        {
            pc = model._isCompatible;
            Double cd = _CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(preferenceInformation, model._model);
            if (cd == null)
                throw new ConstructorException("Cannot calculate the most discriminative compatibility with a value model", this.getClass());
            model.updateCompatibilityDegree(cd);
            if (!model._isCompatible)
            {
                _allStoredModelsAreCompatible = false;
                _noCompatibleModels--;
            }
            if (model._isCompatible != pc) changed = true;
        }
        return (!changed) && (_allStoredModelsAreCompatible);
    }


    /**
     * Main method for inserting the candidate model to the queue (insertion attempt)
     *
     * @param model                 candidate model
     * @param preferenceInformation current feedback (must be consistent with previous calls)
     * @return true, if the input model is compatible; false otherwise
     * @throws ConstructorException the exception can be thrown 
     */
    protected boolean insertModel(T model, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        Double cd = _CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(preferenceInformation, model);
        if (cd == null)
            throw new ConstructorException("Cannot calculate the most discriminative compatibility with a value model", this.getClass());
        boolean isCompatible = Double.compare(cd, 0.0d) > 0;

        if (isCompatible)
        {
            // challenging case
            LinkedList<SortedModel<T>> toAdd = new LinkedList<>();

            // Set temp ID (as the last element in the queue)
            SortedModel<T> candidateModel = new SortedModel<>(-1, _kMostSimilarNeighbors,
                    _similarity.isLessMeaningCloser(), model, cd);
            toAdd.add(candidateModel);

            ListIterator<SortedModel<T>> listIterator = _queue.listIterator();
            while (listIterator.hasNext())
            {
                SortedModel<T> m = listIterator.next();
                if (!m._isCompatible) break; // do not consider incompatible

                _simT[m._id] = _similarity.calculateSimilarity(model, m._model);
                if (candidateModel._closestModels.canAlterTheQueue(_simT[m._id]))
                    candidateModel._closestModels.insert(m, m._id, _simT[m._id]);

                if (m._closestModels.canAlterTheQueue(_simT[m._id]))
                {
                    m._closestModels.insert(candidateModel, candidateModel._id, _simT[m._id]);
                    toAdd.add(m); // to add
                    listIterator.remove();   // remove from the queue (needs to be added again)
                }
            }

            // add the elements one more time, starting from the front
            toAdd.sort(_comparator);
            doFrontalInsertionSort(toAdd);
            SortedModel<T> lastElement = _queue.getLast();


            // Let's check if the new model is preserved (must be not equal than the last in the queue)
            boolean isNewModelRemoved = lastElement.hashCode() == candidateModel.hashCode() && lastElement.equals(candidateModel);

            if (!isNewModelRemoved) // If not, adjustments must be made
            {
                // Pass IDs through neighborhood structure
                for (SortedModel<T> m : toAdd)
                    m._closestModels.replaceIDWith(-1, lastElement._id);
                candidateModel._id = lastElement._id;

                // Must update similarity matrix then
                if (_queueLimit >= 0) System.arraycopy(_simT, 0, _simM[candidateModel._id], 0, _queueLimit);
                for (int i = 0; i < _queueLimit; i++) _simM[i][candidateModel._id] = _simT[i];
            }

            _queue.removeLast(); // remove the worst element

            if (lastElement._isCompatible)
                updateNeighborhoodWithRespectToRemovedModel(lastElement);
            else _noCompatibleModels++; //otherwise do not need to update (1:1) replacement

            // this is the only case for switching to "all models are compatible":
            if (_queue.getLast()._isCompatible) _allStoredModelsAreCompatible = true;

            return true;
        }
        else
        {
            if (_allStoredModelsAreCompatible) return false; // easy case (do not add)

            // moderate case (this should not affect neighborhood)
            SortedModel<T> candidateModel = new SortedModel<>(-1, _kMostSimilarNeighbors, _similarity.isLessMeaningCloser(), model, cd);
            if (Double.compare(candidateModel._compatibilityDegree, _queue.getLast()._compatibilityDegree) < 0)
                return false; // premature termination

            ListIterator<SortedModel<T>> listIterator = getTheIteratorForIncompatibleInsertion(candidateModel);
            if (listIterator != null)
            {
                listIterator.add(candidateModel);
                // the analyzed model is not compatible, so it cannot force a compatible one to leave;
                // thus there is no need to update neighborhood with respect to removed model
                candidateModel._id = _queue.getLast()._id;
                _queue.removeLast();
            }
            // otherwise the element is not added (no concurrent modification)
            return false;
        }
    }

    /**
     * Auxiliary method that updates the neighborhood with respect to the removed model
     *
     * @param removedModel last model removed from the queue (must be compatible)
     */
    protected void updateNeighborhoodWithRespectToRemovedModel(SortedModel<T> removedModel)
    {
        LinkedList<SortedModel<T>> requireUpdate = new LinkedList<>();

        // Collect models whose neighborhood structure changed
        ListIterator<SortedModel<T>> listIterator = _queue.listIterator();

        while (listIterator.hasNext())
        {
            SortedModel<T> model = listIterator.next();
            if (!model._isCompatible) break;
            // if (!model._isCompatible) break; // ineffective, all queue elements must be compatible at this step
            if (model._closestModels.isModelWithIDStored(removedModel._id))
            {
                requireUpdate.add(model); // needs update
                //model._closestModels.removeModelAtIndex(index);
                model._closestModels.reset();
                // remove from queue
                listIterator.remove();  // remove from the queue (needs to be added again)
            }
        }

        if (requireUpdate.isEmpty()) return;

        // Update models
        for (SortedModel<T> m : requireUpdate)
        {
            // update with respect to both
            updateClosestModels(m, _queue, true);
            updateClosestModels(m, requireUpdate, true);
        }

        // need to sort
        requireUpdate.sort(_comparator);
        doFrontalInsertionSort(requireUpdate);
    }

    /**
     * Adds properly sorted models to the queue (insertion sort)
     *
     * @param modelsToAdd sorted models
     */
    protected void doFrontalInsertionSort(LinkedList<SortedModel<T>> modelsToAdd)
    {
        // Add the elements in linear time
        ListIterator<SortedModel<T>> listIterator = _queue.listIterator();
        SortedModel<T> ne;

        for (SortedModel<T> sm : modelsToAdd) // need to add all elements
        {
            while (listIterator.hasNext())
            {
                ne = listIterator.next();
                if ((!ne._isCompatible) || (_comparator.compare(sm, ne) < 0))
                {
                    if (listIterator.hasPrevious()) listIterator.previous();
                    break;
                }
            }
            listIterator.add(sm);
        }
    }


    /**
     * Auxiliary properly sets the iterator for insertion (incompatible case)
     *
     * @param sm element to be added
     * @return list iterator (null; if processing should be skipped)
     */
    private ListIterator<SortedModel<T>> getTheIteratorForIncompatibleInsertion(SortedModel<T> sm)
    {
        ListIterator<SortedModel<T>> listIterator = _queue.listIterator();

        SortedModel<T> e = listIterator.next();
        boolean found = false;

        if ((e._isCompatible)  // the next element is compatible (move forward)
                // or the next element is incompatible and has better score
                || (Double.compare(e._compatibilityDegree, sm._compatibilityDegree) > 0))
        {
            while (listIterator.hasNext())
            {
                e = listIterator.next();
                // current element is better (so break)
                if ((!e._isCompatible) && (Double.compare(e._compatibilityDegree, sm._compatibilityDegree) < 0))
                {
                    found = true;
                    break;
                }
            }
            if (!found) return null; // has not found proper index for insertion
            listIterator.previous(); // move back (previous element must be worse)
        }
        return listIterator;
    }


    /**
     * Getter for the sorted queue.
     *
     * @return sorted queue
     */
    public LinkedList<SortedModel<T>> getQueue()
    {
        return _queue;
    }

    /**
     * Returns a flag indicating if all sorted models (kept in the queue) are compatible.
     *
     * @return true, if all models are compatible; false otherwise.
     */
    public boolean areAllSortedModelsCompatible()
    {
        return _allStoredModelsAreCompatible;
    }

    /**
     * Returns the current number of compatible models.
     *
     * @return no. compatible models
     */
    public int getNoCompatibleModels()
    {
        return _noCompatibleModels;
    }

    /**
     * Returns similarities associated with all compatible models. Creates double[] array and suitably fills it;
     * the array length will equal the number of compatible models. The method assumed that the queue is initialized and valid.
     *
     * @param k similarities associated with models' k-nearest neighbors will be returned
     *          (the index is suitably truncated if the number of stored compatible models is smaller than k)
     * @return similarities associated with compatible models
     */
    public double[] getSimilarities(int k)
    {
        double[] r = new double[_noCompatibleModels];
        if (_noCompatibleModels < 2) return r; // if there is one, it is not compared against anything
        k = Math.min(k, _noCompatibleModels - 1);

        int idx = 0;
        for (SortedModel<T> m : _queue)
        {
            r[idx++] = m._closestModels._similarities[k];
            if (idx == _noCompatibleModels) break;
        }
        return r;
    }

    /**
     * Returns compatible models via array list. The array length will equal the number of compatible models.
     * The method assumed that the queue is initialized and valid.
     *
     * @return compatible models
     */
    public ArrayList<T> getCompatibleModels()
    {
        ArrayList<T> ms = new ArrayList<>(_noCompatibleModels);
        if (_noCompatibleModels == 0) return ms;
        for (SortedModel<T> m : _queue) if (m._isCompatible) ms.add(m._model);
        return ms;
    }

    /**
     * Returns queue's models via array list. The array length will equal the number of models. The method assumed that
     * the queue is initialized and valid.
     *
     * @return compatible models
     */
    public ArrayList<T> getModels()
    {
        ArrayList<T> ms = new ArrayList<>(_queue.size());
        for (SortedModel<T> m : _queue) ms.add(m._model);
        return ms;
    }

    /**
     * Auxiliary method for validating if the queue's content is properly sorted.
     *
     * @return null, if the queue is valid (or null/empty); message is returned otherwise
     */
    protected String isValid()
    {
        if (_queue == null) return null;
        if (_queue.isEmpty()) return null;

        // Check for nulls
        for (SortedModel<T> current : _queue)
            if (current == null) return "Queue element is empty";

        // Check sorting
        SortedModel<T> previous = null;
        for (SortedModel<T> current : _queue)
        {
            if (previous != null)
            {
                int c = _comparator.compare(previous, current);
                if (c > 0) return "Elements are ordered incorrectly";
            }
            previous = current;
        }

        int cM = 0;

        // Check incompatible
        for (SortedModel<T> current : _queue)
            if (!current._isCompatible)
            {
                if (current._closestModels.getNoStoredModels() != 0)
                    return "Incompatible model has a provided closest models data";
            }
            else cM++;

        if (cM != getNoCompatibleModels()) return "The no. compatible models reported is invalid";

        Set<Integer> Ids = new HashSet<>();
        HashMap<Integer, SortedModel<T>> map = new HashMap<>(_queue.size());

        // Gather IDs
        int maxID = -1;

        for (SortedModel<T> current : _queue)
            if (current._isCompatible)
            {
                if (current._id > maxID) maxID = current._id;
                if (current._id == -1) return "A compatible model is assigned an incompatible ID of -1";
                Ids.add(current._id);
                map.put(current._id, current);
            }

        if ((Ids.size() == _queueLimit) && (!_allStoredModelsAreCompatible))
            return "The number of unique IDs does not equal queue size or equals, but the ``all stored models are compatible'' flag is set improperly";

        for (SortedModel<T> current : _queue)
            if (current._isCompatible)
            {
                if (Ids.size() > 1)
                {
                    if (current._closestModels._ids[0] == -1)
                        return "A compatible model is not assigned a closest model ID (but should)";
                    if (current._closestModels._models.get(0) == null)
                        return "A compatible model is not assigned a closest model (but should)";
                    if (Double.compare(current._closestModels._similarities[0], -1.0d) == 0)
                        return "A compatible model is not assigned a closest model similarity (but should)";
                    if (current._id == current._closestModels._ids[0])
                        return "Model ID equals neighborhood model ID (should not)";
                    if (!Ids.contains(current._closestModels._ids[0]))
                        return "Closest model ID is not stored in the set";
                    if (!map.containsKey(current._closestModels._ids[0]))
                        return "Closest model ID is not stored in the map";
                }
                else
                {
                    if (current._closestModels.getNoStoredModels() != 0)
                        return "Closest model data is provided but should not";
                }
            }

        return areSimilaritiesCorrect();
    }

    /**
     * Auxiliary method for validating the similarity-related data.
     *
     * @return null if data is correct; message if not
     */
    protected String areSimilaritiesCorrect()
    {
        // validate closest goals
        for (SortedModel<T> m1 : _queue)
        {
            if (!m1._isCompatible) continue;
            ArrayList<SortedModel<T>> mps = new ArrayList<>(_queue.size());
            for (SortedModel<T> m2 : _queue)
            {
                if (!m2._isCompatible) continue; // do not consider incompatible
                if (m1._id == m2._id) continue;
                mps.add(new SortedModel<>(m2._id, _kMostSimilarNeighbors,
                        _similarity.isLessMeaningCloser(), m2._model, m2._compatibilityDegree));
            }

            mps.sort((o1, o2) -> {
                double sim1 = _similarity.calculateSimilarity(m1._model, o1._model);
                double sim2 = _similarity.calculateSimilarity(m1._model, o2._model);
                if (_similarity.isLessMeaningCloser()) return Double.compare(sim1, sim2);
                else return -Double.compare(sim1, sim2);
            });

            for (int i = 0; i < m1._closestModels.getNoStoredModels(); i++)
            {
                double sim = _similarity.calculateSimilarity(m1._model, mps.get(i)._model);
                if ((Double.compare(m1._closestModels._similarities[i], sim) != 0))
                    return "The k-nearest neighbours are invalid (based on the similarity comparison)";
            }
        }
        return null;
    }

}
