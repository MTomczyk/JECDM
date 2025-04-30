package y2025.ERS.e4_interactive.results;

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
public class GenerateTables_e4_paper_1_HV
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
            path = FileUtils.getPathRelatedToClass(GenerateTables_e4_paper_1_HV.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "e4_interactive.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 0;
            p._topRow = 0;
            p._leftColumn = 0;
            p._columns = 13;
            p._rows = 19;
            p._hhLinesAfterRows = new int[]{-1, 0, 1, 2, 18};
            p._vertLinesAfterColumns = new int[]{-1, 0, 3, 6, 9, 12};
            p._decimalPrecisionForNumericalInColumns = new int[]{0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
            p._wrapNumericalWithDollars = true;
            p._preProcessor = null;
            p._postProcessor = null;
            String[] text = LatexTableFromXLSX.getText(xlsxPath, p);
            if (text != null) PrintUtils.printLines(text);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
