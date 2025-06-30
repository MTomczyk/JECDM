package tools.feedbackgenerators;

import alternative.Alternative;

/**
 * Auxiliary interface for objects responsible for parsing alternatives into file (constructing string representation).
 *
 * @author MTomczyk
 */
public interface IAlternativeWriterParser
{
    /**
     * The main method for constructing a string representing the alternative (to be stored in the result file).
     *
     * @param alternative alternative
     * @return string representing the alternative (without the new line character)
     */
    String parseToString(Alternative alternative);
}
