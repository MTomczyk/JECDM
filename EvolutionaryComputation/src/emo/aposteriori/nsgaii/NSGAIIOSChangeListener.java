package emo.aposteriori.nsgaii;

import ea.EA;
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

public class NSGAIIOSChangeListener extends AbstractOSChangeListener implements IOSChangeListener
{
    /**
     * NSGA-II sorting phase.
     */
    private final NSGAIISort _sort;

    /**
     * Parameterized constructor.
     * @param sort NSGA-II sorting phase
     * @param builder normalization builder
     */
    public NSGAIIOSChangeListener(NSGAIISort sort, INormalizationBuilder builder)
    {
        super(builder);
        _sort = sort;
    }

    /**
     * Updates normalizations.
     * @param ea evolutionary algorithm
     * @param os objective space (updated)
     * @param prevOS objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(EA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {
        _sort.updateNormalizations(_builder.getNormalizations(os));
    }
}
