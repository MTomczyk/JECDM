package emo.interactive.utils.dmcontext;

import dmcontext.DMContext;
import ea.IEA;
import exception.PhaseException;
import os.IOSChangeListener;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;

import java.util.Objects;

/**
 * Abstract implementation of {@link IDMCParamsConstructor}. It additionally implements {@link IOSChangeListener}. It is
 * assumed that this class is the highest in the hierarchy.
 *
 * @author MTomczyk
 */
public abstract class AbstractDMCParamsConstructor implements IDMCParamsConstructor, IOSChangeListener
{
    /**
     * Flag indicating whether the os changed since the last method call.
     */
    protected boolean _osChanged = false;

    /**
     * Object used to construct normalization functions.
     *
     * @deprecated this field will be removed in future releases
     */
    @Deprecated
    protected INormalizationBuilder _normalizationBuilder;

    /**
     * Delivers the alternatives superset to instantiate the decision-making context.
     */
    protected final IAlternativesProvider _alternativesProvider;

    /**
     * Default constructor.
     *
     * @deprecated this constructor will be removed in future releases
     */
    @Deprecated
    public AbstractDMCParamsConstructor()
    {
        this(new StandardLinearBuilder(), new FromPopulation());
    }

    /**
     * Parameterized constructor.
     *
     * @param alternativesProvider delivers the alternatives superset to instantiate the decision-making context
     */
    public AbstractDMCParamsConstructor(IAlternativesProvider alternativesProvider)
    {
        _alternativesProvider = alternativesProvider;
    }

    /**
     * Parameterized constructor.
     *
     * @param normalizationBuilder object used to construct normalization functions; if null, the standard min max
     *                             builder will be instantiated {@link StandardLinearBuilder}
     * @param alternativesProvider delivers the alternatives superset to instantiate the decision-making context
     * @deprecated this constructor will be removed in future releases
     */
    @Deprecated
    public AbstractDMCParamsConstructor(INormalizationBuilder normalizationBuilder,
                                        IAlternativesProvider alternativesProvider)
    {
        _normalizationBuilder = Objects.requireNonNullElseGet(normalizationBuilder, StandardLinearBuilder::new);
        _alternativesProvider = alternativesProvider;
    }


    /**
     * The main method for retrieving decision-making context params. It builds decision-making context based on the
     * current field value of the input EA.
     *
     * @param ea evolutionary algorithm linked with DSS
     * @return decision-making context params
     */
    @Override
    public DMContext.Params getDMCParams(IEA ea)
    {
        DMContext.Params pDMC = new DMContext.Params();
        pDMC._currentIteration = ea.getCurrentGeneration();
        if (ea.getObjectiveSpaceManager() == null) pDMC._currentOS = null;
        else pDMC._currentOS = ea.getObjectiveSpaceManager().getOS();
        pDMC._osChanged = _osChanged;
        _osChanged = false; // reset the flag
        pDMC._currentAlternativesSuperset = _alternativesProvider.getAlternatives(ea);
        pDMC._normalizationBuilder = ea.getNormalizationBuilder();
        pDMC._R = ea.getR();
        return pDMC;
    }

    /**
     * Action to be performed when there is a change in the objective space. It sets the suitable flag to true.
     *
     * @param ea     evolutionary algorithm
     * @param os     objective space (updated)
     * @param prevOS objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown
     */
    @Override
    public void action(IEA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {
        _osChanged = true;
    }
}
