package y2025.ERS.e2_ers_calibration.results;

import io.FileUtils;
import print.PrintUtils;
import tools.LatexTableFromXLSX;
import tools.MultiColumn;
import tools.MultiRow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Auxiliary script that generates latex code (for tables).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class GenerateTables_e2_appendix_2_RO_Summary
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
            path = FileUtils.getPathRelatedToClass(GenerateTables_e2_appendix_2_RO_Summary.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "CalibrationCrossover.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 2;
            p._topRow = 0;
            p._leftColumn = 14;
            p._columns = 4;
            p._rows = 17;
            p._columnAlignments = "crrr";
            p._hhLinesAfterRows = new int[]{-1, 0, 4, 8, 12, 16};
            p._vertLinesAfterColumns = new int[]{-1, 0, 2, 3};
            p._decimalPrecisionForNumericalInColumns = new int[]{0, 1, 1, 2};
            p._wrapNumericalWithDollars = true;
            p._multiRows = new MultiRow[]
                    {
                            new MultiRow(1, 0, 4, "*"),
                            new MultiRow(5, 0, 4, "*"),
                            new MultiRow(9, 0, 4, "*"),
                            new MultiRow(13, 0, 4, "*"),
                    };
            p._preProcessor = (original, r, c) -> {
                if (r == 0)
                {
                    if (c >= 1)
                    {
                        if (c == 3) return "\\multicolumn{1}{c|}{" + original + "}";
                        else return "\\multicolumn{1}{c}{" + original + "}";
                    }
                    else return original;
                }
                return original;
            };
            p._postProcessor = null;
            p._hhLinePreprocessor = (row, columns) -> {
                int r = 0;
                if ((row == 5) || (row == 13) || (row == 21) || (row == 29)) r = 1;
                return "\\hhline{" + "~".repeat(r) + "*{" + (columns - r) + "}{-}}";
            };
            String[] text = LatexTableFromXLSX.getText(xlsxPath, p);
            if (text != null) PrintUtils.printLines(text);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
