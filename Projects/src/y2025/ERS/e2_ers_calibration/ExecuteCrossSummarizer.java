package y2025.ERS.e2_ers_calibration;

import container.Containers;
import executor.CrossSummarizer;
import summary.Summary;

/**
 * Performs cross-examination.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class ExecuteCrossSummarizer
{
    /**
     * Performs cross-examination.
     *
     * @param args arguments that can be passed via the console.
     */
    public static void main(String[] args)
    {
        try
        {
            Containers containers = ContainersGetter.getContainers();
            CrossSummarizer.Params pScS = new CrossSummarizer.Params(containers);
            CrossSummarizer crossSummarizer = new CrossSummarizer(pScS);

            Summary s = crossSummarizer.execute(null);
            System.out.println(s.getStringRepresentation(false));
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
