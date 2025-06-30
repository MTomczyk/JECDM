package emo.aposteriori.nsgaiii;

import ea.EA;
import emo.utils.decomposition.nsgaiii.NSGAIIIGoalsManager;
import exception.PhaseException;
import os.AbstractOSChangeListener;
import os.IOSChangeListener;
import space.normalization.builder.INormalizationBuilder;
import space.os.ObjectiveSpace;

/**
 * "Objective space changed" listener (properly updates normalization objects maintained by the sorting procedure).
 *
 * @author MTomczyk
 */

public class NSGAIIIOSChangeListener extends AbstractOSChangeListener implements IOSChangeListener
{
    /**
     * Main goals manager steering the evolutionary process.
     */
    private final NSGAIIIGoalsManager _goalsManager;

    /**
     * Parameterized constructor.
     *
     * @param goalsManager main goals manager steering the evolutionary process.
     * @param builder      normalization builder
     */
    public NSGAIIIOSChangeListener(NSGAIIIGoalsManager goalsManager, INormalizationBuilder builder)
    {
        super(builder);
        _goalsManager = goalsManager;
    }

    /**
     * Updates normalizations.
     *
     * @param ea     evolutionary algorithm
     * @param os     objective space (updated)
     * @param prevOS objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(EA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {
        _goalsManager.updateNormalizations(_builder.getNormalizations(os));
    }
}
