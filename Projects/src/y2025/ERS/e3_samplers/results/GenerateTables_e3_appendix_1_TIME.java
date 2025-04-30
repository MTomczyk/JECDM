package y2025.ERS.e3_samplers.results;

import io.FileUtils;
import print.PrintUtils;
import tools.LatexTableFromXLSX;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Auxiliary script that generates latex code (for tables).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class GenerateTables_e3_appendix_1_TIME
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Path path;
        try
        {
            int table = 1;

            path = FileUtils.getPathRelatedToClass(GenerateTables_e3_appendix_1_TIME.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "e3_samplers_1_TimeWhenCompFound.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 1;
            p._topRow = 1 + (35 * table); // select different tables
            p._leftColumn = 1;
            p._columns = 23;
            p._rows = 34;
            p._hhLinesAfterRows = new int[]{-1, 0, 1, 5, 9, 13, 17, 21, 25, 29, 33};
            p._vertLinesAfterColumns = new int[]{-1, 0, 1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22};
            p._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                    3, 3, 3, 3, 3, 3, 3};
            p._wrapNumericalWithDollars = true;
            p._preProcessor = (original, r, c) -> {
                if ((original == null) || (original.isEmpty())) return original;
                if (original.charAt(original.length() - 1) == '*')
                    return original.substring(0, original.length() - 1);
                else return original;
            };
            p._postProcessor = (original, preprocessed, r, c) -> {
                if ((original == null) || (original.isEmpty())) return original;
                if (original.charAt(original.length() - 1) == '*')
                    return preprocessed + "*";
                else return preprocessed;
            };
            String[] text = LatexTableFromXLSX.getText(xlsxPath, p);
            if (text != null) PrintUtils.printLines(text);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
