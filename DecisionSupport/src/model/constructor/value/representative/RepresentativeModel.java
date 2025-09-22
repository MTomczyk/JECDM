package model.constructor.value.representative;

import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.AbstractWrappedConstructor;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class generates a single, representative model instance. It is also a wrapper for any value-based model
 * constructor. When the main method is called
 * ({@link RepresentativeModel#mainConstructModels(Report, LinkedList)}), it first delegates the model
 * construction procedure to the wrapped model. Then, it uses an implementation of
 * {@link IRepresentativeValueModelSelector} to derive a single model instance and returns it in the result bundle (the
 * remaining bundle statistics are copied from the wrapped model's report).
 *
 * @author MTomczyk
 */
public class RepresentativeModel<T extends AbstractValueInternalModel> extends AbstractWrappedConstructor<T> implements IConstructor<T>
{
    /**
     * Params container. It assists in instantiating the object with any model constructor wrapped.
     */
    public static class Params<T extends AbstractValueInternalModel>
    {
        /**
         * Object used to select a representative model instance.
         */
        public final IConstructor<T> _modelConstructor;

        /**
         * Object used to select a representative model instance.
         */
        public final IRepresentativeValueModelSelector<T> _modelSelector;

        /**
         * Parameterized constructor.
         *
         * @param modelConstructor wrapped model constructor
         * @param modelSelector    representative model selector
         */
        public Params(IConstructor<T> modelConstructor, IRepresentativeValueModelSelector<T> modelSelector)
        {
            _modelConstructor = modelConstructor;
            _modelSelector = modelSelector;
        }
    }

    /**
     * Object used to select a representative model instance.
     */
    private final IRepresentativeValueModelSelector<T> _modelSelector;


    /**
     * Parameterized constructor.
     *
     * @param modelConstructor wrapped model constructor
     * @param modelSelector    representative model selector
     */
    public RepresentativeModel(IConstructor<T> modelConstructor, IRepresentativeValueModelSelector<T> modelSelector)
    {
        this(new Params<>(modelConstructor, modelSelector));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public RepresentativeModel(Params<T> p)
    {
        super("Representative model (" + p._modelConstructor.getName() + ")", p._modelConstructor);
        _modelSelector = p._modelSelector;
    }

    /**
     * The main-construct models phase.
     *
     * @param bundle                bundle result object to be filled (provided via wrappers)
     * @param preferenceInformation the decision maker's preference information stored
     * @throws ConstructorException the exception can be thrown
     */
    @Override
    protected void mainConstructModels(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        // use frs
        Report<T> wBundle = _wrappedConstructor.constructModels(preferenceInformation);

        // derive the representative model
        if (wBundle._models != null)
        {
            T selectedModel = _modelSelector.selectModel(wBundle._models, preferenceInformation);
            if (selectedModel != null)
            {
                bundle._models = new ArrayList<>(1);
                bundle._models.add(selectedModel);
                bundle._inconsistencyDetected = false;
            }
            else bundle._inconsistencyDetected = true;
        }
        else
        {
            // pass the statistics
            bundle._inconsistencyDetected = true;
            bundle._models = null;
        }

        _models = bundle._models;

        bundle._successRateInPreserving = wBundle._successRateInPreserving;
        bundle._modelsPreservedBetweenIterations = wBundle._modelsPreservedBetweenIterations;

        bundle._successRateInConstructing = wBundle._successRateInConstructing;
        bundle._acceptedNewlyConstructedModels = wBundle._acceptedNewlyConstructedModels;
        bundle._rejectedNewlyConstructedModels = wBundle._rejectedNewlyConstructedModels;

        bundle._normalizationsWereUpdated = wBundle._normalizationsWereUpdated;
    }

    /**
     * Getter for the constructor's name (Representative model (wrapped constructor's name).
     *
     * @return constructor's name
     */
    @Override
    public String getName()
    {
        return "Representative model (" + _wrappedConstructor.getName() + ")";
    }
}
