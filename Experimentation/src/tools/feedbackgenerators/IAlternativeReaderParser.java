package tools.feedbackgenerators;

import alternative.Alternative;

/**
 * Auxiliary interface for objects responsible for parsing alternatives from a file (constructing string representation).
 *
 * @author MTomczyk
 */
public interface IAlternativeReaderParser
{
    /**
     * The main method for constructing an alternative from a string file (to be loaded from the results file).
     *
     * @param line line to be parsed
     * @return alternative object parsed from a line
     */
    Alternative parseFromString(String line);
}
