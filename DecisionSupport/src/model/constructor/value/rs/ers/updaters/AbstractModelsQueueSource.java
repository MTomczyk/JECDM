package model.constructor.value.rs.ers.updaters;


import model.constructor.value.rs.ers.ModelsQueue;
import model.constructor.value.rs.ers.SortedModel;
import model.internals.value.AbstractValueInternalModel;
import updater.AbstractSource;
import updater.IDataSource;

import java.util.LinkedList;


/**
 * Default implementation of {@link updater.IDataSource} for coupling {@link model.constructor.value.rs.ers.ModelsQueue}
 * with the visualization module. It creates the data by aggregating the model's parameters into a matrix (each row
 * is associated with a different model instance).
 *
 * @author MTomczyk
 */
abstract public class AbstractModelsQueueSource<T extends AbstractValueInternalModel> extends AbstractSource implements IDataSource
{
    /**
     * Wrapped model queue.
     */
    protected final ModelsQueue<T> _modelQueue;

    /**
     * If true, only compatible models are used when creating the data; false otherwise.
     */
    protected final boolean _considerOnlyCompatible;

    /**
     * Parameterized constructor.
     *
     * @param modelsQueue            wrapped model queue
     * @param considerOnlyCompatible if true, only compatible models are used when creating the data; false otherwise
     */
    public AbstractModelsQueueSource(ModelsQueue<T> modelsQueue, boolean considerOnlyCompatible)
    {
        _modelQueue = modelsQueue;
        _considerOnlyCompatible = considerOnlyCompatible;
    }

    /**
     * Main method for creating data.
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        LinkedList<SortedModel<T>> models = _modelQueue.getQueue();
        if (models == null) return null;
        double[][] data;
        if (_considerOnlyCompatible) data = new double[_modelQueue.getNoCompatibleModels()][];
        else data = new double[models.size()][];
        for (int i = 0; i < models.size(); i++)
        {
            if ((_considerOnlyCompatible) && (!models.get(i)._isCompatible)) break;
            processPoint(data, i, models.get(i));
        }
        return data;
    }

    /**
     * Method for processing a single data point (to be overwritten).
     *
     * @param data  data matrix being filled
     * @param idx   index of the currently processed entry
     * @param model currently processed model
     */
    protected void processPoint(double[][] data, int idx, SortedModel<T> model)
    {

    }
}
