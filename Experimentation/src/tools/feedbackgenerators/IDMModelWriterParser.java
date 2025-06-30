package tools.feedbackgenerators;

import model.internals.value.AbstractValueInternalModel;

/**
 * Auxiliary interface for objects responsible for parsing artificial DM's data into file (one line; string).
 *
 * @author MTomczyk
 */
public interface IDMModelWriterParser
{
    /**
     * The main method for constructing a string representing the DM (to be stored in the result file).
     *
     * @param model artificial DM's model
     * @return string representing the DM (without the new line character)
     */
    String parseToString(AbstractValueInternalModel model);
}
