package visualization.updaters.sources;

import ea.EA;
import ea.IEA;
import population.ISpecimenGetter;
import population.PopulationGetter;
import updater.IDataSource;

/**
 * Implementation of {@link updater.IDataSource}. It is a default implementation that creates new data that is defined
 * as a 2D matrix: [population members x solutions' evaluations] (derived from an instance of {@link EA}; its current
 * population).
 *
 * @author MTomczyk
 */
public class EASource extends AbstractEASource implements IDataSource
{
    /**
     * Parameterized constructor.
     *
     * @param ea reference to the evolutionary algorithm.
     */
    public EASource(IEA ea)
    {
        this(ea, new PopulationGetter(), false);
    }

    /**
     * Parameterized constructor.
     *
     * @param ea             reference to the evolutionary algorithm.
     * @param specimenGetter allows retrieving specimen arrays (main input for data creation)
     */
    public EASource(IEA ea, ISpecimenGetter specimenGetter)
    {
        this(ea, specimenGetter, false);
    }


    /**
     * Parameterized constructor.
     *
     * @param ea           reference to the evolutionary algorithm.
     * @param addTimeStamp if true, the specimens' entries contributing to the final data (double [][]) are extended by
     *                     two additional doubles: [current (EA) generation number, current (EA) steady-state repeat]
     */
    public EASource(IEA ea, boolean addTimeStamp)
    {
        this(ea, new PopulationGetter(), addTimeStamp);
    }


    /**
     * Parameterized constructor.
     *
     * @param ea             reference to the evolutionary algorithm.
     * @param specimenGetter allows retrieving specimen arrays (main input for data creation)
     * @param addTimeStamp   if true, the specimens' entries contributing to the final data (double [][]) are extended
     *                       by two additional doubles: [current (EA) generation number, current (EA) steady-state
     *                       repeat]
     */
    public EASource(IEA ea, ISpecimenGetter specimenGetter, boolean addTimeStamp)
    {
        super(ea, specimenGetter, addTimeStamp);
    }
}
