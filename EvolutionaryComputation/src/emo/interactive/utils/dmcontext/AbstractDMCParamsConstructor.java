package emo.interactive.utils.dmcontext;

import dmcontext.DMContext;
import ea.EA;
import exception.PhaseException;
import os.IOSChangeListener;
import population.Specimens;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;

import java.util.Objects;

/**
 * Abstract implementation of {@link IDMCParamsConstructor}. It additionally implements {@link IOSChangeListener}.
 * It is assumed that this class is the highest in the hierarchy.
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
     */
    protected final INormalizationBuilder _normalizationBuilder;

    /**
     * Default constructor.
     */
    public AbstractDMCParamsConstructor()
    {
        this(new StandardLinearBuilder());
    }

    /**
     * Parameterized constructor.
     *
     * @param normalizationBuilder object used to construct normalization functions; if null, the standard min max builder
     *                             will be instantiated {@link StandardLinearBuilder}
     */
    public AbstractDMCParamsConstructor(INormalizationBuilder normalizationBuilder)
    {
        _normalizationBuilder = Objects.requireNonNullElseGet(normalizationBuilder, StandardLinearBuilder::new);
    }


    /**
     * The main method for retrieving decision-making context params.
     * It builds decision-making context based on the current field value of the input EA.
     *
     * @param ea evolutionary algorithm linked with DSS
     * @return decision-making context params
     */
    @Override
    public DMContext.Params getDMCParams(EA ea)
    {
        DMContext.Params pDMC = new DMContext.Params();
        pDMC._currentIteration = ea.getCurrentGeneration();
        if (ea.getObjectiveSpaceManager() == null) pDMC._currentOS = null;
        else pDMC._currentOS = ea.getObjectiveSpaceManager()._os;
        pDMC._osChanged = _osChanged;
        _osChanged = false; // reset the flag
        pDMC._currentAlternativesSuperset = new Specimens(ea.getSpecimensContainer().getPopulation());
        pDMC._normalizationBuilder = _normalizationBuilder;
        pDMC._R = ea.getR();
        return pDMC;
    }

    /**
     * Action to be performed when there is a change in the objective space.
     * It sets the suitable flag to true.
     *
     * @param ea     evolutionary algorithm
     * @param os     objective space (updated)
     * @param prevOS objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {
        _osChanged = true;
    }
}
