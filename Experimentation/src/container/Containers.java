package container;

import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;

/**
 * Simple class for keeping {@link container.global.GlobalDataContainer},
 * {@link container.scenario.ScenarioDataContainerFactory},
 * and {@link container.trial.TrialDataContainerFactory}.
 *
 * @author MTomczyk
 */
public class Containers
{
    /**
     * Global data container factory.
     */
    public final GlobalDataContainer _GDC;

    /**
     * Scenario data container factory.
     */
    public final ScenarioDataContainerFactory _SDCF;

    /**
     * Trial data container factory.
     */
    public final TrialDataContainerFactory _TDCF;

    /**
     * Parameterized constructor.
     * @param GDC global data container
     * @param SDCF scenario data container factory
     * @param TDCF trial data container factory
     */
    public Containers(GlobalDataContainer GDC, ScenarioDataContainerFactory SDCF, TrialDataContainerFactory TDCF)
    {
        _GDC = GDC;
        _SDCF = SDCF;
        _TDCF = TDCF;
    }
}
