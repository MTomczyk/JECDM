package model.constructor.value;

import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.AbstractConstructor;
import model.constructor.IConstructor;
import model.constructor.Report;
import preference.indirect.PairwiseComparison;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Constructor dedicated to {@link model.internals.value.scalarizing.KTSCone} model. It essentially wraps all the pairwise comparisons
 * that can be retrieved from the input preference information and creates a single object instance of
 * {@link model.internals.value.scalarizing.KTSCone}, supplying it with these comparisons.
 *
 * @author MTomczyk
 */
public class KTSCone extends AbstractConstructor<model.internals.value.scalarizing.KTSCone>
        implements IConstructor<model.internals.value.scalarizing.KTSCone>
{
    /**
     * Default constructor.
     */
    public KTSCone()
    {
        super("KTSCone", null);
    }

    /**
     * The main-construct models phase (to be overwritten).
     * The concrete extension should provide the constructed models via the bundle object.
     *
     * @param bundle                bundle result object to be filled (provided via wrappers)
     * @param preferenceInformation the decision maker's preference information stored
     * @throws ConstructorException the exception can be thrown 
     */
    protected void mainConstructModels(Report<model.internals.value.scalarizing.KTSCone> bundle,
                                       LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        LinkedList<PairwiseComparison> PCs = new LinkedList<>();
        for (PreferenceInformationWrapper piw : preferenceInformation)
        {
            PairwiseComparison[] pcs = piw._preferenceInformation.getPairwiseComparisons();
            if (pcs != null)
                for (PairwiseComparison pc : pcs)
                    if (pc != null) PCs.add(pc);
        }

        ArrayList<model.internals.value.scalarizing.KTSCone> models = new ArrayList<>(1);
        models.add(new model.internals.value.scalarizing.KTSCone(PCs, _dmContext.getNormalizationsCurrentOS()));
        _models = models;

        bundle._models = _models;
        bundle._acceptedNewlyConstructedModels = 1;
        bundle._rejectedNewlyConstructedModels = 0;
        bundle._successRateInConstructing = 1.0d;
        bundle._successRateInPreserving = 0.0d;
        bundle._modelsPreservedBetweenIterations = 0;
    }

}
