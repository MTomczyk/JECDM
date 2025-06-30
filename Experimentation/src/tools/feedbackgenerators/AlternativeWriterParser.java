package tools.feedbackgenerators;

import alternative.Alternative;

/**
 * Standard implementation of {@link IAlternativeWriterParser}.
 *
 * @author MTomczyk
 */
public class AlternativeWriterParser implements IAlternativeWriterParser
{
    /**
     * The main method for constructing a string representing the alternative (to be stored in the result file).
     * THe line is constructed as:
     * "name;no. auxiliary;scores;a1;a2;...;(auxiliary scores);no. performance values;p1;p2 ...",
     * where ai is the vector of auxiliary scores, wile pi is the performance vector.
     *
     * @param alternative alternative
     * @return string representing the alternative (without the new line character)
     */
    @Override
    public String parseToString(Alternative alternative)
    {
        StringBuilder SB = new StringBuilder();
        SB.append(alternative.getName());
        SB.append(";");
        {
            double[] av = alternative.getAuxScores();
            SB.append(av.length);
            SB.append(";");
            for (double a : av)
            {
                SB.append(a);
                SB.append(";");
            }
        }
        {
            double[] pv = alternative.getPerformanceVector();
            SB.append(pv.length);
            SB.append(";");
            for (int i = 0; i < pv.length; i++)
            {
                SB.append(pv[i]);
                if (i < pv.length - 1) SB.append(";");
            }
        }
        return SB.toString();
    }
}
