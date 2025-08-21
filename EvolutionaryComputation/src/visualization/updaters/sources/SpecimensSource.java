package visualization.updaters.sources;

import population.Specimen;
import updater.IDataSource;

import java.util.ArrayList;

/**
 * Implementation of {@link IDataSource}. It is a default implementation that creates new data that is defined as a 2D
 * matrix: [population members x solutions' evaluations] (derived from the pre-supplied array).
 *
 * @author MTomczyk
 */
public class SpecimensSource extends AbstractSpecimensSource implements IDataSource
{
    /**
     * Parameterized constructor.
     *
     * @param specimens specimens array
     */
    public SpecimensSource(ArrayList<Specimen> specimens)
    {
        super(specimens);
    }

    /**
     * Creates new data and returns it (the entries are clones from specimens' evaluations; i.e., new objects are
     * created). The data is defined as a 2D matrix: [population members x solutions' evaluations].
     *
     * @return new data (null, if specimens cannot be derived)
     */
    @Override
    public double[][] createData()
    {
        ArrayList<Specimen> P = getSpecimens();
        if (P == null) return null;
        double[][] data = new double[P.size()][];
        for (int i = 0; i < P.size(); i++) data[i] = P.get(i).getEvaluations().clone();
        return data;
    }

}
