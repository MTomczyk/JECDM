package y2025.ERS.e4_interactive.results;

import io.FileUtils;
import print.PrintUtils;
import tools.LatexTableFromXLSX;
import tools.MultiColumn;

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
            p._columns = 17;
            p._rows = 25;
            p._hhLinesAfterRows = new int[]{-1, 0, 1, 2, 9, 18, 24};
            p._vertLinesAfterColumns = new int[]{-1, 0, 4, 8, 12, 16};
            p._decimalPrecisionForNumericalInColumns = new int[]{0, 3, 3, 2, 2, 3, 3, 2, 2, 3, 3, 2, 2, 3, 3, 2, 2};
            p._wrapNumericalWithDollars = true;
            p._multiColumns = new MultiColumn[]{
                    new MultiColumn(0, 1, 4, "|c|"),
                    new MultiColumn(0, 5, 4, "c|"),
                    new MultiColumn(0, 9, 4, "c|"),
                    new MultiColumn(0, 13, 4, "c|"),
            };
            p._preProcessor = null;
            p._postProcessor = null;
            p._hhLinePreprocessor = (row, columns) -> {
                if ((row == 9) || (row == 18)) return "\\hhline{" + "*{" + columns + "}{=}}";
                else return "\\hhline{" + "*{" + columns + "}{-}}";
            };

            String[] text = LatexTableFromXLSX.getText(xlsxPath, p);
            if (text != null) PrintUtils.printLines(text);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
