package y2025.ERS.e3_samplers.results;

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
public class GenerateTables_e3_appendix_5_STD_CN
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
            int table = 2;

            path = FileUtils.getPathRelatedToClass(GenerateTables_e3_appendix_5_STD_CN.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "e3_samplers_6_STD_CN.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 1;
            p._topRow = 1 + (35 * table); // select different tables
            p._leftColumn = 1;
            p._columns = 23;
            p._rows = 34;
            p._columnAlignments = "cccrrrrrrrrrrrrrrrrrrrr";

            p._hhLinesAfterRows = new int[]{-1, 0, 1, 5, 9, 13, 17, 21, 25, 29, 33};
            p._vertLinesAfterColumns = new int[]{-1, 0, 1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22};
            p._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                    5, 5, 5, 5, 5, 5, 5};
            p._wrapNumericalWithDollars = true;
            p._multiColumns = new MultiColumn[]{
                    new MultiColumn(0, 0, 3, "|c|"),
                    new MultiColumn(0, 3, 2, "c|"),
                    new MultiColumn(0, 5, 2, "c|"),
                    new MultiColumn(0, 7, 2, "c|"),
                    new MultiColumn(0, 9, 2, "c|"),
                    new MultiColumn(0, 11, 2, "c|"),
                    new MultiColumn(0, 13, 2, "c|"),
                    new MultiColumn(0, 15, 2, "c|"),
                    new MultiColumn(0, 17, 2, "c|"),
                    new MultiColumn(0, 19, 2, "c|"),
                    new MultiColumn(0, 21, 2, "c|"),
            };
            p._multiRows = new MultiRow[]
                    {
                            new MultiRow(2, 0, 16, "*"),
                            new MultiRow(18, 0, 16, "*"),
                            new MultiRow(2, 1, 4, "*"),
                            new MultiRow(6, 1, 4, "*"),
                            new MultiRow(10, 1, 4, "*"),
                            new MultiRow(14, 1, 4, "*"),
                            new MultiRow(18, 1, 4, "*"),
                            new MultiRow(22, 1, 4, "*"),
                            new MultiRow(26, 1, 4, "*"),
                            new MultiRow(30, 1, 4, "*"),
                    };
            p._hhLinePreprocessor = (row, columns) -> {
                int r = 0;
                if ((row == 5) || (row == 9) || (row == 13) || (row == 21) || (row == 25) || (row == 29)) r = 1;
                return "\\hhline{" + "~".repeat(r) + "*{" + (columns - r) + "}{-}}";
            };
            p._preProcessor = (original, r, c) -> {
                if ((original == null) || (original.isEmpty())) return original;
                if (r == 1)
                {
                    if (c > 2)
                    {
                        if (c % 2 == 0) return "\\multicolumn{1}{c|}{" + original + "}";
                        else return "\\multicolumn{1}{c}{" + original + "}";
                    }
                    else return original;
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
