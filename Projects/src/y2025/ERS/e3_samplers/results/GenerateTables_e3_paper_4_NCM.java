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
public class GenerateTables_e3_paper_4_NCM
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
            path = FileUtils.getPathRelatedToClass(GenerateTables_e3_paper_4_NCM.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "e3_samplers_4_NCM.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 0;
            p._topRow = 2;
            p._leftColumn = 1;
            p._columns = 12;
            p._rows = 26;
            p._hhLinesAfterRows = new int[]{-1, 0, 1, 5, 9, 13, 17, 21, 25};
            p._vertLinesAfterColumns = new int[]{-1, 3, 5, 7, 9, 11};
            p._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2};
            p._wrapNumericalWithDollars = true;
            String[] text = LatexTableFromXLSX.getText(xlsxPath, p);
            if (text != null) PrintUtils.printLines(text);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
