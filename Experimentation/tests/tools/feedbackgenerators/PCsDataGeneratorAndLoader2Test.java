package tools.feedbackgenerators;

import exception.Exception;
import io.FileUtils;
import preference.indirect.PairwiseComparison;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Performs simple visualization test of the data in {@link FeedbackData}.
 *
 * @author MTomczyk
 */
class PCsDataGeneratorAndLoader2Test
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        PCsDataLoader.Params pL = new PCsDataLoader.Params();
        pL._keys = new String[]{"SHAPE", "M", "DM",};
        pL._values = new String[][]
                {
                        {"convex", "CONCAVE"}, // will be converted to uppercase
                        {"2", "3", "4", "5"},
                        {"LINEAR", "CHEBYSHEV"},
                };
        pL._trials = 5;
        //pG._notify = false;
        pL._noInteractionsProvider = new Constant(5);

        try
        {
            Path path = FileUtils.getPathRelatedToClass(PCsDataGeneratorAndLoader2Test.class,
                    "Private", "tests", File.separatorChar);
            pL._filePath = path.toString() + File.separatorChar + "results.txt";

            pL._modelReader = new LNormReaderParser();
            pL._alternativeReader = new AlternativeReaderParser();
            PCsDataLoader DL = new PCsDataLoader(pL);
            FeedbackData FD = DL.process();

            ArrayList<PairwiseComparison> PCs = FD.getPairwiseComparisons("SHAPE_CONVEX_M_2_DM_LINEAR", 0, true);

        } catch (Exception | IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}