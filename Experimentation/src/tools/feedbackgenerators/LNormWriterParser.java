package tools.feedbackgenerators;

import model.internals.value.AbstractValueInternalModel;
import model.internals.value.scalarizing.LNorm;

/**
 * Parser for storing L-norm data (alpha-value weights).
 *
 * @author MTomczyk
 */
public class LNormWriterParser implements IDMModelWriterParser
{
    /**
     * The main method for constructing a string representing the DM modeled using L-norm model (to be stored in the
     * result file). The line is constructed as "L-NORM;alpha;w1;w2;....", where alpha is the L-norms alpha compensation
     * level, while w1, w2, ... are the weights. The data on the normalization functions is not stored.
     *
     * @param model artificial DM's model (L-norm)
     * @return string representing the DM (without the new line character; empty string if the input is invalid)
     */
    @Override
    public String parseToString(AbstractValueInternalModel model)
    {
        if (!(model instanceof LNorm lnorm)) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("L-NORM;");
        sb.append(lnorm.getLNorm().getAuxParam());
        sb.append(";");
        sb.append(lnorm.getLNorm().getWeights().length);
        sb.append(";");
        double[] w = model.getWeights();
        for (int i = 0; i < w.length; i++)
        {
            sb.append(w[i]);
            if (i < w.length - 1) sb.append(";");
        }
        return sb.toString();
    }
}
