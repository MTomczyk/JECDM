package t1_10.t8_experimentation_tools_and_screenshots.t1_latex_from_excel;

import tools.LatexTableFromXLSX;
import tools.MultiColumn;
import tools.MultiRow;
import io.FileUtils;
import print.PrintUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This tutorial showcases how to use {@link LatexTableFromXLSX} class.
 * It uses the Data.xlsx file as an example input.
 *
 * @author MTomczyk
 */
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        try
        {
            // Construct a path to Data.xlsx.
            Path path = FileUtils.getPathRelatedToClass(Tutorial1.class, "Tutorials", "src", File.separatorChar);
            String stringPath = path + File.separator + "Data.xlsx";
            System.out.println("Path = " + stringPath);

            // Data.xlsx contains three tables. The lines below create separate params containers, each dedicated
            // to a different table.
            LatexTableFromXLSX.Params[] ps = new LatexTableFromXLSX.Params[3];
            {
                // Important note: the params are not validated. Exceptions may be thrown if the params are incorrect
                // (e.g., point to a non-existing sheet)
                // The constructor accepts coordinates of a table in the Data.xlsx file (see the JavaDoc).
                ps[0] = new LatexTableFromXLSX.Params(1, 1, 7, 7, 0);
                //  The param below determines directly after which columns (counting from 0) vertical lines should be put.
                ps[0]._vertLinesAfterColumns = new int[]{-1, 0, 1, 3, 6};
                //  The param below determines directly after which rows (counting from 0) horizontal lines should be put.
                ps[0]._hhLinesAfterRows = new int[]{-1, 0, 1, 3, 6};
                // The param below determines the decimal precision for numerical data (each number per one column)
                ps[0]._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 2, 2, 3, 3, 3};  // try nulling
                // The param below determines the decimal precision for numerical data (each number per one row; does
                // not work if _decimalPrecisionForNumericalInColumns is set)
                ps[0]._decimalPrecisionForNumericalInRows = new int[]{0, 0, 2, 2, 3, 3, 3};
                // Specify which cells to merge as a \multicolumn. Note that the text in cell pointed by row/column
                // parameters is used as the text in \multicolumn (text in other overlapped cells is skipped)
                ps[0]._multiColumns = new MultiColumn[]{new MultiColumn(0, 0, 2, "| c "),
                        new MultiColumn(0, 2, 2, "| c"),
                        new MultiColumn(0, 4, 3, "| c |")};
                // Specify which cells to merge as a \multirow. Note that the text in cell pointed by row/column
                // parameters is used as the text in \multirow (text in other overlapped cells is skipped)
                ps[0]._multiRows = new MultiRow[]{new MultiRow(2, 0, 2, "*"),
                        new MultiRow(4, 0, 3, "*")};
            }
            {
                ps[1] = ps[0].getClone();
                ps[1]._sheet = 1;
            }
            {
                ps[2] = new LatexTableFromXLSX.Params(6, 6, 2);
                ps[2]._vertLinesAfterColumns = new int[]{-1, 0, 1, 3, 5};
                ps[2]._hhLinesAfterRows = new int[]{-1, 0, 1, 3, 5};
                ps[2]._decimalPrecisionForNumericalInColumns = new int[]{0, 0, 2, 2, 3, 3};  // try nulling
                ps[2]._decimalPrecisionForNumericalInRows = new int[]{0, 0, 2, 2, 3, 3};
                ps[2]._multiColumns = new MultiColumn[]{new MultiColumn(0, 0, 2, "| c "),
                        new MultiColumn(0, 2, 2, "| c"),
                        new MultiColumn(0, 4, 2, "| c |")};
                ps[2]._multiRows = new MultiRow[]{new MultiRow(2, 0, 2, "*"),
                        new MultiRow(4, 0, 2, "*")};
            }

            // Get latex lines and print the results:
            String[][] lines = LatexTableFromXLSX.getText(stringPath, ps);
            for (int i = 0; i < ps.length; i++)
            {
                System.out.println("Printing table no. " + i + " =======================================");
                PrintUtils.printLines(lines[i]);
            }

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
