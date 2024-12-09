package t1_10.t4_decision_support_module.t4_decision_support_system.t2_use_case;

import ea.EA;
import model.IPreferenceModel;
import model.internals.IInternalModel;
import population.PopulationGetter;
import population.Specimen;
import statistics.IStatistic;
import updater.IDataSource;
import visualization.updaters.sources.AbstractEASource;

import java.util.ArrayList;

/**
 * Implementation of {@link IDataSource}. This implementations works similarly to {@link visualization.updaters.sources.EASource}.
 * However, instead of adding ea timestamp, the resulting vectors are extended by statistics on the alternative's relevance
 * given the internal preference models.
 *
 * @author MTomczyk
 */
public class PopulationRelevance<T extends IInternalModel> extends AbstractEASource implements IDataSource
{
    /**
     * Reference to the preference model coupled with {@link system.model.ModelSystem}.
     */
    private final IPreferenceModel<T> _preferenceModel;

    /**
     * Statistics function applied to alternatives' relevance scores.
     */
    private final IStatistic _statistics;

    /**
     * Parameterized constructor.
     *
     * @param ea              reference to the evolutionary algorithm.
     * @param preferenceModel preference model associated with the DM
     * @param statistics      statistics function applied to alternatives' relevance scores
     */
    public PopulationRelevance(EA ea, IPreferenceModel<T> preferenceModel, IStatistic statistics)
    {
        super(ea, new PopulationGetter());
        _preferenceModel = preferenceModel;
        _statistics = statistics;
    }

    /**
     * Creates new data and returns it. The data is defined as a 2D matrix: [population members x solutions' evaluations].
     * Additionally, if the _addTimestamp flag is true, the specimens' entries contributing to the final data (double [][])
     * are extended will be extended by two additional doubles [generation number, steady-state repeat].
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        ArrayList<Specimen> P = getDefaultSpecimens();
        double[][] data = new double[P.size()][];
        for (int i = 0; i < P.size(); i++)
        {
            double[] e = P.get(i).getEvaluations(); // do not clone, use array copy below
            data[i] = new double[e.length + 1]; //  +1 for relevance // +2 for pairwise comparisons
            System.arraycopy(P.get(i).getEvaluations(), 0, data[i], 0, e.length);
            // gather statistics
            ArrayList<T> internalModels = _preferenceModel.getInternalModels();
            if ((internalModels != null) &&  (!internalModels.isEmpty()))
            {
                double[] sample = new double[internalModels.size()];
                for (int j = 0; j < internalModels.size(); j++)
                    sample[j] = internalModels.get(j).evaluate(P.get(i).getAlternative());
                data[i][e.length] = _statistics.calculate(sample);
            }
        }
        return data;
    }

}
