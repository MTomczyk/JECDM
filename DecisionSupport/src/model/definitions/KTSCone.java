package model.definitions;

import model.AbstractPreferenceModel;
import model.IPreferenceModel;
import model.evaluator.RepresentativeModel;

/**
 * Definition for the KTSCone model (see {@link model.internals.value.scalarizing.KTSCone}).
 *
 * @author MTomczyk
 */
public class KTSCone extends AbstractPreferenceModel<model.internals.value.scalarizing.KTSCone>
        implements IPreferenceModel<model.internals.value.scalarizing.KTSCone>
{
    /**
     * Default constructor.
     */
    public KTSCone()
    {
        this(new model.internals.value.scalarizing.KTSCone(null, null));
    }

    /**
     * Parameterized constructor.
     *
     * @param internalModel internal cone model
     */
    public KTSCone(model.internals.value.scalarizing.KTSCone internalModel)
    {
        super("KTSCone", new RepresentativeModel<>());
        if (internalModel != null) setInternalModel(internalModel);
    }
}
