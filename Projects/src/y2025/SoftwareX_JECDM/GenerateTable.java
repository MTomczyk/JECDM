package y2025.SoftwareX_JECDM;

import io.FileUtils;
import print.PrintUtils;
import tools.LatexTableFromXLSX;
import tools.MultiColumn;
import tools.MultiRow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This runnable generates a LaTeX table used in the publication from the Excel file.
 *
 * @author MTomczyk
 */
public class GenerateTable
{
    /**
     * Runs the procedure.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Path path;
        try
        {
            // Note that the final output is very close to the one used in the article's LaTeX source,
            // but not the same. Slight additional processing is required.
            path = FileUtils.getPathRelatedToClass(GenerateTable.class, "Projects", "src", File.separatorChar);
            String xlsxPath = path + File.separator + "TimeResults.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 1;
            p._topRow = 0;
            p._leftColumn = 0;
            p._columns = 11;
            p._rows = 51;

            p._preProcessor = (original, r, c) -> {
                if ((original == null) || (original.isEmpty())) return original;
                if (original.equals("M")) return "$M$";
                else if (original.equals("N")) return "$N$";
                else return original;
            };

            p._multiColumns = new MultiColumn[]{
                    new MultiColumn(0, 0, 3, "|c|"),
                    new MultiColumn(0, 3, 8, "c|"),
                    new MultiColumn(1, 0, 3, "|c|"),
            };
            p._multiRows = new MultiRow[]{
                    new MultiRow(3, 0, 12, "*"),
                    new MultiRow(15, 0, 12, "*"),
                    new MultiRow(27, 0, 12, "*"),
                    new MultiRow(39, 0, 12, "*"),

                    new MultiRow(3, 1, 3, "*"),
                    new MultiRow(6, 1, 3, "*"),
                    new MultiRow(9, 1, 3, "*"),
                    new MultiRow(12, 1, 3, "*"),

                    new MultiRow(15, 1, 3, "*"),
                    new MultiRow(18, 1, 3, "*"),
                    new MultiRow(21, 1, 3, "*"),
                    new MultiRow(24, 1, 3, "*"),

                    new MultiRow(27, 1, 3, "*"),
                    new MultiRow(30, 1, 3, "*"),
                    new MultiRow(33, 1, 3, "*"),
                    new MultiRow(36, 1, 3, "*"),

                    new MultiRow(39, 1, 3, "*"),
                    new MultiRow(42, 1, 3, "*"),
                    new MultiRow(45, 1, 3, "*"),
                    new MultiRow(48, 1, 3, "*"),
            };

            p._hhLinesAfterRows = new int[]{
                    -1, 0, 1, 2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35, 38, 41, 44, 47, 50};
            p._vertLinesAfterColumns = new int[]{-1, 0, 1, 2, 6, 10};
            p._decimalPrecisionForNumericalInColumns = new int[]{
                    0, 0, 0,
                    2, 2, 2, 2, 2, 2, 2, 2,};
            p._wrapNumericalWithDollars = true;

            String[] text = LatexTableFromXLSX.getText(xlsxPath, p);
            if (text != null) PrintUtils.printLines(text);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
