package visualization.updaters.sources;

import population.Specimen;
import updater.AbstractSource;
import updater.IDataSource;

import java.util.ArrayList;

/**
 * Provides common fields/functionalities for specimens-related implementations of {@link IDataSource}.
 *
 * @author MTomczyk
 */
public abstract class AbstractSpecimensSource extends AbstractSource implements IDataSource
{
    /**
     * Pre-supplied specimens array.
     */
    protected final ArrayList<Specimen> _specimens;

    /**
     * Parameterized constructor.
     *
     * @param specimens    pre-supplied specimens array (can be null, if {@link AbstractSpecimensSource#getSpecimens()}
     *                     is overwritten)
     */
    protected AbstractSpecimensSource(ArrayList<Specimen> specimens)
    {
        _specimens = specimens;
    }

    /**
     * Returns a specimen array obtained by using the specimen getter implementation.
     *
     * @return specimen array (main input for data creation)
     */
    protected ArrayList<Specimen> getSpecimens()
    {
        return _specimens;
    }

}
