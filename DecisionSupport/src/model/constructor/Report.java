package model.constructor;

import dmcontext.DMContext;
import model.internals.AbstractInternalModel;
import system.AbstractReport;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The supportive container-like class that serves as a result of the processing of {@link IConstructor}.
 *
 * @author MTomczyk
 */
public class Report<T extends AbstractInternalModel> extends AbstractReport
{
    /**
     * Auxiliary flag that can be used to inform that the constructor failed to provide compatible models
     * (inconsistency).
     */
    public boolean _inconsistencyDetected = false;

    /**
     * Models constructed by {@link IConstructor}
     */
    public ArrayList<T> _models = null;

    /**
     * Field indicating the construction starting time (System.nanoTime()).
     */
    public long _constructionStartTime = 0;

    /**
     * Field indicating the construction stopping time (System.nanoTime()).
     */
    public long _constructionStopTime = 0;

    /**
     * Field indicating the construction elapsed time (in ms).
     */
    public double _constructionElapsedTime = 0;

    /**
     * Auxiliary field that can be used to store the constructor's success rate in preserving existing models (relevant,
     * e.g.,
     * when the constructor is based on the Monte Carlo simulation).
     */
    public double _successRateInPreserving = 0.0d;


    /**
     * Auxiliary field that can be used to store data on how many already existing models were preserved by the
     * constructor (e.g., due to their remaining feasibility).
     */
    public int _modelsPreservedBetweenIterations = 0;

    /**
     * Auxiliary field that can be used to store data on how many already existing models were rejected by the
     * constructor (e.g., due to their lost feasibility).
     */
    public int _modelsRejectedBetweenIterations = 0;


    /**
     * Auxiliary field that can be used to store the constructor's success rate in building new models (relevant, e.g.,
     * when the constructor is based on the Monte Carlo simulation).
     */
    public double _successRateInConstructing = 0.0d;

    /**
     * An auxiliary field that can be used to store data on how many newly constructed models were accepted (e.g., due
     * to their compatibility).
     */
    public int _acceptedNewlyConstructedModels = 0;

    /**
     * An auxiliary field that can be used to store data on how many newly constructed models were rejected (e.g., due
     * to their incompatibility).
     */
    public int _rejectedNewlyConstructedModels = 0;


    /**
     * Additional flag indicating whether the normalizations were updated during the most recent model bundle
     * construction.
     */
    public boolean _normalizationsWereUpdated = false;

    /**
     * Provides the number of compatible models that were supposed to be sampled.
     */
    public int _compatibleModelsToSample = 0;

    /**
     * Parameterized constructor.
     *
     * @param dmContext current decision-making context
     */
    public Report(DMContext dmContext)
    {
        super(dmContext);
    }

    /**
     * Creates and returns the string representation of the state. Each line ends with a new line symbol and
     * is stored in a different output array element.
     *
     * @param indent auxiliary indent used when constructing the lines
     * @return string representation
     */
    @Override
    public String[] getStringRepresentation(int indent)
    {
        LinkedList<String> lines = new LinkedList<>();
        String ind = StringUtils.getIndent(indent);
        applyBasicLines(lines, ind);
        lines.add(ind + "Inconsistency detected = " + _inconsistencyDetected);
        lines.add(ind + "The number of generated models = " + _models.size());
        lines.add(ind + "Elapsed time = " + _constructionElapsedTime + " ms");
        lines.add(ind + "Success rate in preserving = " + _successRateInPreserving);
        lines.add(ind + "Models preserved between iterations = " + _modelsPreservedBetweenIterations);
        lines.add(ind + "Models rejected between iterations = " + _modelsRejectedBetweenIterations);
        lines.add(ind + "Success rate in constructing = " + _successRateInConstructing);
        lines.add(ind + "Accepted newly constructed models = " + _acceptedNewlyConstructedModels);
        lines.add(ind + "Rejected newly constructed models = " + _rejectedNewlyConstructedModels);
        lines.add(ind + "Normalizations were updated = " + _normalizationsWereUpdated);
        lines.add(ind + "Compatible models to sample = " + _compatibleModelsToSample);
        addExtraLogLines(lines, ind);
        return StringUtils.getArrayFromList(lines);
    }

    /**
     * Auxiliary method that can be overwritten to add extra log lines (used by
     * {@link Report#getStringRepresentation(int)}).
     *
     * @param lines lines being processed
     * @param ind   base indent
     */
    protected void addExtraLogLines(LinkedList<String> lines, String ind)
    {
        // does nothing
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        String[] lines = getStringRepresentation(0);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++)
        {
            sb.append(lines[i]);
            if (i < lines.length - 1) sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

}
