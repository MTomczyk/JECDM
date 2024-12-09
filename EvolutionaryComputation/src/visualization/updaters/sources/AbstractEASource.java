package visualization.updaters.sources;

import ea.EA;
import population.ISpecimenGetter;
import population.Specimen;
import updater.AbstractSource;
import updater.IDataSource;

import java.util.ArrayList;

/**
 * Provides common fields/functionalities for EA-related implementations of {@link updater.IDataSource}.
 *
 * @author MTomczyk
 */
public class AbstractEASource extends AbstractSource implements IDataSource
{
    /**
     * Reference to the associated EA.
     */
    protected final EA _ea;

    /**
     * Allows retrieving specimen arrays (main input for data creation).
     */
    protected final ISpecimenGetter _specimenGetter;

    /**
     * Parameterized constructor.
     *
     * @param ea             reference to the associated EA
     * @param specimenGetter allows retrieving specimen arrays (main input for data creation)
     */
    public AbstractEASource(EA ea, ISpecimenGetter specimenGetter)
    {
        _ea = ea;
        _specimenGetter = specimenGetter;
    }

    /**
     * Returns a specimen array obtained by using the specimen getter implementation.
     *
     * @return specimen array (main input for data creation)
     */
    protected ArrayList<Specimen> getDefaultSpecimens()
    {
        return _specimenGetter.getSpecimens(_ea.getSpecimensContainer());
    }
}
