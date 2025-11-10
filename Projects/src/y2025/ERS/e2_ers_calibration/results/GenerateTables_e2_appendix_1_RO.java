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
public class GenerateTables_e2_appendix_1_RO
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
            path = FileUtils.getPathRelatedToClass(GenerateTables_e2_appendix_1_RO.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "CalibrationMutation.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 0;
            p._topRow = 0;
            p._leftColumn = 0;
            p._columns = 19;
            p._rows = 34;
            p._columnAlignments = "ccccrrrrrrrrrrrrrrr";
            p._hhLinesAfterRows = new int[]{-1, 0, 1, 5, 9, 13, 17, 21, 25, 29, 33};
            p._vertLinesAfterColumns = new int[]{-1, 0, 1, 3, 8, 13, 18};
            if (p._sheet == 0)
                p._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2};
            else
                p._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 1, 1, 5, 5, 2, 2, 2, 5, 5, 2, 2,
                        2, 5, 5, 2, 2, 2};

            p._wrapNumericalWithDollars = true;
            p._multiColumns = new MultiColumn[]{
                    new MultiColumn(0, 0, 4, "|c|"),
                    new MultiColumn(0, 4, 5, "c|"),
                    new MultiColumn(0, 9, 5, "c|"),
                    new MultiColumn(0, 14, 5, "c|")
            };
            p._multiRows = new MultiRow[]
                    {
                            new MultiRow(2, 0, 8, "*"),
                            new MultiRow(10, 0, 8, "*"),
                            new MultiRow(18, 0, 8, "*"),
                            new MultiRow(26, 0, 8, "*"),

                            new MultiRow(2, 1, 4, "*"),
                            new MultiRow(6, 1, 4, "*"),
                            new MultiRow(10, 1, 4, "*"),
                            new MultiRow(14, 1, 4, "*"),
                            new MultiRow(18, 1, 4, "*"),
                            new MultiRow(22, 1, 4, "*"),
                            new MultiRow(26, 1, 4, "*"),
                            new MultiRow(30, 1, 4, "*"),
                    };
            p._preProcessor = (original, r, c) -> {

                if ((r == 1) && (c >= 4))
                {
                    if ((c == 8) || (c == 13) || (c == 18)) return "\\multicolumn{1}{c|}{" + original + "}";
                    return "\\multicolumn{1}{c}{" + original + "}";
                }
                else
                {
                    if (p._sheet != 0) return original;
                    if (r < 2) return original;
                    if (c < 4) return original;
                    if (c == 6) return original;
                    if (c == 7) return original;
                    if (c == 8) return original;
                    if (c == 11) return original;
                    if (c == 12) return original;
                    if (c == 13) return original;
                    if (c == 16) return original;
                    if (c == 17) return original;
                    if (c == 18) return original;
                    double value = Double.parseDouble(original);
                    value *= 100;
                    return String.format("$%.2f\\%%$", value);
                }
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
