package model.constructor.value.rs.representative;

import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.AbstractWrappedConstructor;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.constructor.value.representative.IRepresentativeValueModelSelector;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class generates a single, representative model instance. It is also a wrapper for FRS {@link FRS}. When the main
 * method is called ({@link RepresentativeModel#mainConstructModels(Report, LinkedList)}), it first delegates the model
 * construction procedure to FRS. Then, it uses an implementation of {@link IRepresentativeValueModelSelector} to derive a single
 * model instance and returns it in the result bundle (the remaining bundle statistics are copied from the FRS's bundle).
 *
 * @author MTomczyk
 */
public class RepresentativeModel<T extends AbstractValueInternalModel> extends AbstractWrappedConstructor<T> implements IConstructor<T>
{
    /**
     * Params container.
     */
    public static class Params<T extends AbstractValueInternalModel> extends FRS.Params<T>
    {
        /**
         * Object used to select a representative model instance.
         */
        public final IRepresentativeValueModelSelector<T> _modelSelector;

        /**
         * Parameterized constructor.
         *
         * @param RM            random model generator
         * @param modelSelector representative model selector
         */
        public Params(IRandomModel<T> RM, IRepresentativeValueModelSelector<T> modelSelector)
        {
            super(RM);
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
     * @param p params container
     */
    public RepresentativeModel(Params<T> p)
    {
        super("Representative model (FRS)", new FRS<>(p));
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

}
