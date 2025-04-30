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
public class GenerateTables_e3_appendix_3_SR
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    @SuppressWarnings("ConstantValue")
    public static void main(String[] args)
    {
        Path path;
        try
        {
            int table = 0;

            path = FileUtils.getPathRelatedToClass(GenerateTables_e3_appendix_3_SR.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "e3_samplers_3_SR.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 1;
            p._topRow = 1 + (35 * table); // select different tables
            p._leftColumn = 1;
            p._columns = 23;
            p._rows = 34;
            p._hhLinesAfterRows = new int[]{-1, 0, 1, 5, 9, 13, 17, 21, 25, 29, 34};
            p._vertLinesAfterColumns = new int[]{-1, 0, 1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22};
            p._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                    3, 3, 3, 3, 3, 3, 3};
            p._wrapNumericalWithDollars = true;
            p._postProcessor = (original, preprocessed, r, c) -> {
                if ((original == null) || (original.isEmpty())) return original;
                if ((r > 1) && (c > 2))
                {
                    double d = Double.parseDouble(original);
                    return String.format("$%.2f", d * 100) + "\\%$";
                }
                else return original;
            };
            String[] text = LatexTableFromXLSX.getText(xlsxPath, p);
            if (text != null) PrintUtils.printLines(text);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
