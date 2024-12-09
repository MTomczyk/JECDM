package indicator.emo.interactive;

import exeption.PreferenceModelException;
import indicator.AbstractPerformanceIndicator;
import indicator.IPerformanceIndicator;
import model.IPreferenceModel;
import model.internals.value.AbstractValueInternalModel;
import population.Specimen;
import statistics.IStatistic;

import java.util.ArrayList;

/**
 * This indicator uses a model of a preference model ({@link model.IPreferenceModel}) to assess population members.
 * Then, it returns as statistics summarizing the derived relevance scores.
 *
 * @author MTomczyk
 */
public class ValueModelQuality<T extends AbstractValueInternalModel> extends AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * Preference model used to assess solutions.
     */
    private final IPreferenceModel<T> _model;

    /**
     * Statistic function used to summarize the scores.
     */
    private final IStatistic _statistic;

    /**
     * Parameterized constructor.
     *
     * @param artificialDM preference model used to assess solutions
     * @param statistic    statistic function used to summarize the scores
     */
    public ValueModelQuality(IPreferenceModel<T> artificialDM, IStatistic statistic)
    {
        super(artificialDM.isLessPreferred());
        _model = artificialDM;
        _statistic = statistic;
    }

    /**
     * Auxiliary method for assessing the performance of EA based on its population.
     * The method is to be overwritten.
     *
     * @param population input population
     * @return performance value (0, if an exception is thrown by the model)
     */
    protected double evaluate(ArrayList<Specimen> population)
    {
        double[] scores = new double[population.size()];
        for (int i = 0; i < scores.length; i++)
        {
            try
            {
                scores[i] = _model.evaluate(population.get(i).getAlternative());
            } catch (PreferenceModelException e)
            {
                return 0.0d;
            }
        }
        return _statistic.calculate(scores);
    }


    /**
     * Method for identifying preference direction.
     *
     * @return true - less is preferred
     */
    @Override
    public boolean isLessPreferred()
    {
        return _model.isLessPreferred();
    }

    /**
     * Returns string representation
     *
     * @return string representation: "VMQ_MODEL_" + model's name + "_" + statistic's name
     */
    @Override
    public String toString()
    {
        return "VMQ_MODEL_" + _model.getName() + "_" + _statistic.getName();
    }


    /**
     * Creates a cloned object in an initial state.
     * The parameters are not deep-copies (references are passed).
     *
     * @return cloning
     */
    @Override
    public IPerformanceIndicator getInstanceInInitialState()
    {
        return new ValueModelQuality<>(_model, _statistic);
    }
}
