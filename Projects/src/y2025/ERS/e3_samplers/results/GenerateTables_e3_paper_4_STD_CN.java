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
public class GenerateTables_e3_paper_4_STD_CN
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
            path = FileUtils.getPathRelatedToClass(GenerateTables_e3_paper_4_STD_CN.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "e3_samplers_6_STD_CN.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 0;
            p._topRow = 2;
            p._leftColumn = 1;
            p._columns = 12;
            p._rows = 26;
            p._columnAlignments = "ccccrrrrrrrr";
            p._hhLinesAfterRows = new int[]{-1, 0, 1, 5, 9, 13, 17, 21, 25};
            p._vertLinesAfterColumns = new int[]{-1, 0, 1, 3, 5, 7, 9, 11};
            p._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5};
            p._wrapNumericalWithDollars = true;
            p._multiColumns = new MultiColumn[]{
                    new MultiColumn(0, 0, 4, "|c"),
                    new MultiColumn(0, 4, 2, "|c|"),
                    new MultiColumn(0, 6, 2, "c|"),
                    new MultiColumn(0, 8, 2, "c|"),
                    new MultiColumn(0, 10, 2, "c|"),
            };
            p._multiRows = new MultiRow[]
                    {
                            new MultiRow(2, 0, 8, "*"),
                            new MultiRow(10, 0, 8, "*"),
                            new MultiRow(18, 0, 8, "*"),
                            new MultiRow(2, 1, 4, "*"),
                            new MultiRow(6, 1, 4, "*"),
                            new MultiRow(10, 1, 4, "*"),
                            new MultiRow(14, 1, 4, "*"),
                            new MultiRow(18, 1, 4, "*"),
                            new MultiRow(22, 1, 4, "*"),
                    };
            p._hhLinePreprocessor = (row, columns) -> {
                if ((row == 5) || (row == 13) || (row == 21)) return "\\hhline{" + "~" + "*{" + (columns - 1) + "}{-}}";
                else if ((row == 9) || (row == 17)) return "\\hhline{" + "*{" + (columns) + "}{=}}";
                return "\\hhline{" + "*{" + (columns) + "}{-}}";
            };
            p._preProcessor = (original, r, c) -> {
                if ((original == null) || (original.isEmpty())) return original;
                if ((r == 1) && (c > 3))
                {
                    if (c % 2 == 1) return "\\multicolumn{1}{c|}{" + original + "}";
                    else return "\\multicolumn{1}{c}{" + original + "}";
                }
                else return original;
            };
            p._postProcessor = (original, preprocessed, r, c) -> {
                if ((original == null) || (original.isEmpty())) return original;
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
