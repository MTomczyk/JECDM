package tools.feedbackgenerators;

import model.internals.value.AbstractValueInternalModel;
import scenario.Scenario;
import space.normalization.INormalization;

/**
 * Auxiliary interface for objects responsible for parsing artificial DM's model data from file (one line; string).
 *
 * @author MTomczyk
 */
public interface IDMModelReaderParser
{
    /**
     * The main method for constructing a model from a string representing the DM (stored in the result file).
     *
     * @param scenario       scenario being processed
     * @param normalizations auxiliary normalizations derived when parsing the file
     * @param line           line to be parsed
     * @return model object
     */
    AbstractValueInternalModel parseFromString(Scenario scenario, INormalization[] normalizations, String line);
}
