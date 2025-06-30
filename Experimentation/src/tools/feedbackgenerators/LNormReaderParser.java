package tools.feedbackgenerators;

import model.internals.value.AbstractValueInternalModel;
import model.internals.value.scalarizing.LNorm;
import scenario.Scenario;
import space.normalization.INormalization;

/**
 * Auxiliary interface for objects responsible for parsing artificial DM's model (L-norm) data from file (one line; string).
 * Should be used with {@link LNormWriterParser} (reads alpha-value and weights).
 *
 * @author MTomczyk
 */
public class LNormReaderParser implements IDMModelReaderParser
{
    /**
     * The main method for constructing a model from a string representing the DM (stored in the result file).
     *
     * @param scenario       scenario being processed
     * @param normalizations auxiliary normalizations derived when parsing the file
     * @param line           line to be parsed
     * @return model object (null, if the input is invalid)
     */
    @Override
    public AbstractValueInternalModel parseFromString(Scenario scenario, INormalization[] normalizations, String line)
    {
        if (!line.contains(";")) return null;
        String[] s = line.split(";");
        if (!s[0].equals("L-NORM")) return null;
        double alpha = Double.parseDouble(s[1]);
        int m = Integer.parseInt(s[2]);
        double[] w = new double[m];
        for (int i = 3; i < 3 + m; i++)
            w[i - 3] = Double.parseDouble(s[i]);
        return new LNorm(w, alpha, normalizations);
    }
}
