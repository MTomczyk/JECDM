package tools.feedbackgenerators;

import alternative.Alternative;

/**
 * Auxiliary interface for objects responsible for parsing alternatives from a file (constructing string representation).
 * Should be used with {@link AlternativeWriterParser}.
 *
 * @author MTomczyk
 */
public class AlternativeReaderParser implements IAlternativeReaderParser
{
    /**
     * The main method for constructing an alternative from a string file (to be loaded from the results file).
     *
     * @param line line to be parsed
     * @return alternative object parsed from a line
     */
    @Override
    public Alternative parseFromString(String line)
    {
        if (!line.contains(";")) return null;
        String[] s = line.split(";");
        String name = s[0];
        int noAuxScores = Integer.parseInt(s[1]);
        double[] auxScores = new double[noAuxScores];
        for (int i = 1; i < 1 + noAuxScores; i++)
            auxScores[i - 1] = Double.parseDouble(s[i]);
        int noPerformances = Integer.parseInt(s[2 + noAuxScores]);
        double[] perf = new double[noPerformances];
        for (int i = 3 + noAuxScores; i < 3 + noAuxScores + noPerformances; i++)
            perf[i - (3 + noAuxScores)] = Double.parseDouble(s[i]);
        return new Alternative(name, perf, auxScores);
    }
}
