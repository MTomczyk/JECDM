package y2025.ERS.e4_interactive.results;

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
public class GenerateTables_e4_appendix_1_HV
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
            path = FileUtils.getPathRelatedToClass(GenerateTables_e4_appendix_1_HV.class, "Projects", "src", File.separatorChar);
            //String xlsxPath = path + File.separator + "e4_interactive.xlsx";
            //String xlsxPath = path + File.separator + "e4_interactive_nemo0.xlsx";
            String xlsxPath = path + File.separator + "e4_interactive_nemoii.xlsx";
            LatexTableFromXLSX.Params p = new LatexTableFromXLSX.Params();
            p._sheet = 1;
            p._topRow = 0;
            p._leftColumn = 0;
            p._columns = 22;
            p._rows = 46;
            p._hhLinesAfterRows = new int[]{-1, 0, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43, 45};
            p._vertLinesAfterColumns = new int[]{-1, 1, 6, 11, 16, 21};
            p._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
            p._wrapNumericalWithDollars = true;
            p._multiColumns = new MultiColumn[]{
                    new MultiColumn(0, 2, 5, "|c|"),
                    new MultiColumn(0, 7, 5, "c|"),
                    new MultiColumn(0, 12, 5, "c|"),
                    new MultiColumn(0, 17, 5, "c|"),
            };
            p._multiRows = new MultiRow[]
                    {
                            new MultiRow(2, 0, 2, "*"),
                            new MultiRow(4, 0, 2, "*"),
                            new MultiRow(6, 0, 2, "*"),
                            new MultiRow(8, 0, 2, "*"),
                            new MultiRow(10, 0, 2, "*"),
                            new MultiRow(12, 0, 2, "*"),
                            new MultiRow(14, 0, 2, "*"),
                            new MultiRow(16, 0, 2, "*"),
                            new MultiRow(18, 0, 2, "*"),
                            new MultiRow(20, 0, 2, "*"),
                            new MultiRow(22, 0, 2, "*"),
                            new MultiRow(24, 0, 2, "*"),
                            new MultiRow(26, 0, 2, "*"),
                            new MultiRow(28, 0, 2, "*"),
                            new MultiRow(30, 0, 2, "*"),
                            new MultiRow(32, 0, 2, "*"),
                            new MultiRow(34, 0, 2, "*"),
                            new MultiRow(36, 0, 2, "*"),
                            new MultiRow(38, 0, 2, "*"),
                            new MultiRow(40, 0, 2, "*"),
                            new MultiRow(42, 0, 2, "*"),
                            new MultiRow(44, 0, 2, "*")
                    };
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
