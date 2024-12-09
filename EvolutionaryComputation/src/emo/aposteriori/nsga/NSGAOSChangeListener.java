package emo.aposteriori.nsga;

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

public class NSGAOSChangeListener extends AbstractOSChangeListener implements IOSChangeListener
{
    /**
     * NSGA sorting phase.
     */
    private final NSGASort _sort;

    /**
     * Parameterized constructor.
     * @param sort NSGA sorting phase
     * @param builder normalization builder
     */
    public NSGAOSChangeListener(NSGASort sort, INormalizationBuilder builder)
    {
        super(builder);
        _sort = sort;
    }

    /**
     * Updates normalizations.
     * @param ea evolutionary algorithm
     * @param os objective space (updated)
     * @param prevOS objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {
        _sort.updateNormalizations(_builder.getNormalizations(os));
    }
}
