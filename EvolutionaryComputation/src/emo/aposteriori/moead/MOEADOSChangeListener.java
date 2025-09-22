package emo.aposteriori.moead;

import ea.IEA;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import os.AbstractOSChangeListener;
import os.IOSChangeListener;
import space.normalization.builder.INormalizationBuilder;
import space.os.ObjectiveSpace;

/**
 * "Objective space changed" listener (properly updates "normalizations" in the sorting procedure).
 *
 * @author MTomczyk
 */

public class MOEADOSChangeListener extends AbstractOSChangeListener implements IOSChangeListener
{
    /**
     * Goals manager steering the evolutionary process.
     */
    private final MOEADGoalsManager _goalsManager;

    /**
     * Parameterized constructor.
     *
     * @param goalsManager goals manager steering the evolutionary process.
     * @param builder      normalization builder
     * @deprecated this constructor will be removed in future releases
     */
    @Deprecated
    public MOEADOSChangeListener(MOEADGoalsManager goalsManager, INormalizationBuilder builder)
    {
        super(builder);
        _goalsManager = goalsManager;
    }

    /**
     * Parameterized constructor.
     *
     * @param goalsManager goals manager steering the evolutionary process.
     */
    public MOEADOSChangeListener(MOEADGoalsManager goalsManager)
    {
        super();
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
    public void action(IEA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {
        _goalsManager.updateNormalizations(ea.getNormalizationBuilder().getNormalizations(os));
    }
}
