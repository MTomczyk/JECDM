package system.ds;

import inconsistency.IInconsistencyHandler;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.internals.AbstractInternalModel;

/**
 * Bundle object used for wrapping all essential data required to establish a single model system
 * (see {@link system.dm.DecisionMakerSystem}).
 *
 * @author MTomczyk
 */
public class ModelBundle <T extends AbstractInternalModel>
{
    /**
     * Preference model used to represent the decision maker's preferences.
     */
    public IPreferenceModel<T> _preferenceModel;

    /**
     * Preference model constructor.
     */
    public IConstructor<T> _modelConstructor;

    /**
     * Object responsible for handling inconsistency that can occur when building the model(s) being in line with the
     * decision maker's aspirations.
     */
    public IInconsistencyHandler<T> _inconsistencyHandler;

    /**
     * Default constructor.
     */
    public ModelBundle()
    {
        this(null, null, null);
    }

    /**
     * Parameterized constructor.
     * @param preferenceModel preference model used to represent the decision maker's preferences
     * @param modelConstructor preference model constructor
     * @param inconsistencyHandler object responsible for handling inconsistency that can occur when building the model(s)
     *                             being in line with the decision maker's aspirations
     */
    public ModelBundle( IPreferenceModel<T> preferenceModel, IConstructor<T> modelConstructor, IInconsistencyHandler<T> inconsistencyHandler)
    {
        _preferenceModel = preferenceModel;
        _modelConstructor = modelConstructor;
        _inconsistencyHandler = inconsistencyHandler;
    }
}
