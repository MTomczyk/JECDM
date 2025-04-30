package model.constructor.value.rs.ers.updaters;

import model.constructor.value.rs.ers.ModelsQueue;
import model.constructor.value.rs.ers.SortedModel;
import model.internals.value.scalarizing.LNorm;
import updater.IDataSource;

/**
 * Implementation of {@link IDataSource} for coupling {@link model.constructor.value.rs.ers.ModelsQueue} set for L-norms
 * {@link LNorm} with the visualization module. It creates the data by aggregating
 * the model's parameters into a matrix (each row is associated with a different model instance).
 * One row contains the weights and the compensation level alpha (the last element).
 *
 * @author MTomczyk
 */
public class LNormsQueueSource extends AbstractModelsQueueSource<LNorm> implements IDataSource
{
    /**
     * Parameterized constructor.
     *
     * @param modelsQueue            wrapped model queue
     * @param considerOnlyCompatible if true, only compatible models are used when creating the data; false otherwise
     */
    public LNormsQueueSource(ModelsQueue<LNorm> modelsQueue, boolean considerOnlyCompatible)
    {
        super(modelsQueue, considerOnlyCompatible);
    }

    /**
     * Method for processing a single data point (to be overwritten).
     *
     * @param data  data matrix being filled
     * @param idx   index of the currently processed entry
     * @param model currently processed model
     */
    @Override
    protected void processPoint(double[][] data, int idx, SortedModel<LNorm> model)
    {
        double[] w = model._model.getWeights();
        double a = model._model.getAuxParam();
        data[idx] = new double[w.length + 1];
        System.arraycopy(w, 0, data[idx], 0, w.length);
        data[idx][w.length] = a;
    }
}
