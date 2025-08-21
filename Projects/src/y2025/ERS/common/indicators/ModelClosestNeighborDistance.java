package y2025.ERS.common.indicators;

import ea.EA;
import ea.IEA;
import exception.TrialException;
import indicator.AbstractIndicator;
import indicator.IIndicator;
import model.constructor.Report;
import model.internals.value.AbstractValueInternalModel;
import y2025.ERS.common.EAWrapperIterableSampler;
import space.distance.Euclidean;
import space.distance.IDistance;
import statistics.IStatistic;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Returns statistic built on closest neighbor distance (among models build by a sampler).
 *
 * @author MTomczyk
 */
public class ModelClosestNeighborDistance extends AbstractIndicator implements IIndicator
{
    /**
     * Euclidean distance function.
     */
    private final IDistance _d = new Euclidean();

    /**
     * Statistic function.
     */
    private final IStatistic _stat;

    /**
     * Parameterized constructor.
     *
     * @param stat            statistic function
     * @param lessIsPreferred if true, less is preferred; false otherwise
     */
    public ModelClosestNeighborDistance(IStatistic stat, boolean lessIsPreferred)
    {
        super("MCND_" + stat.getName(), lessIsPreferred, (scenario, trialID) -> new ModelClosestNeighborDistance(stat, lessIsPreferred));
        _stat = stat;
    }

    /**
     * Performs the evaluation.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment (0 if there are no compatible models)
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    @Override
    public double evaluate(IEA ea) throws TrialException
    {
        if (!(ea instanceof EAWrapperIterableSampler<?> eaWrapper))
            throw new TrialException("Invalid EA type", null, (Class<?>) null, _scenario, _trialID);

        Report<? extends AbstractValueInternalModel> report = eaWrapper.getReport();
        ArrayList<? extends AbstractValueInternalModel> models = report._models;
        if (models.isEmpty()) return 0.0d;
        double[] dist = new double[models.size()];
        Arrays.fill(dist, Double.MAX_VALUE);
        for (int i = 0; i < dist.length; i++)
        {
            double[] w1 = models.get(i).getWeights();
            for (int j = i + 1; j < dist.length; j++)
            {
                double[] w2 = models.get(j).getWeights();
                double d = _d.getDistance(w1, w2);
                if (Double.compare(d, dist[i]) < 0) dist[i] = d;
                if (Double.compare(d, dist[j]) < 0) dist[j] = d;
            }
        }

        return _stat.calculate(dist);
    }
}
