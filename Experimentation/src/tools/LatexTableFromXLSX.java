package tools;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Provides auxiliary methods for parsing data tables stored in XLSX files and producing LaTeX text for producing tables.
 *
 * @author MTomczyk
 */
public class LatexTableFromXLSX
{
    /**
     * Params container.
     */
    public static class Params extends DataMatrixCoordinates
    {
        /**
         * If true, numerical cells are wrapped with dollars.
         */
        public boolean _wrapNumericalWithDollars = true;

        /**
         * Separator used to merge cells in the same rows
         */
        String _separator = " & ";

        /**
         * If true, two backslash symbols are used to indicate end of the line
         */
        boolean _useEndLineSymbol = true;

        /**
         * If true, commas in strings are replaced with dots.
         */
        boolean _replaceCommasWithDots = true;

        /**
         * If provided (can be null): decimal precision for numerical values in columns
         * (starting from 0, each entry per one column in the already derived from Excel data matrix).
         */
        public int[] _decimalPrecisionForNumericalInColumns = null;

        /**
         * If provided (can be null): decimal precision for numerical values in columns is set  (starting from 0, each
         * entry per one column in the already derived from Excel data matrix; not working if
         * decimalPrecisionForNumericalInColumns is set).
         */
        public int[] _decimalPrecisionForNumericalInRows = null;

        /**
         * If provided (can be null): specifies directly after which rows \hhline{*{column}{-}} command should be
         * inserted (starting from 0, each entry per one row in the already derived from Excel data matrix; -1 should
         * be used to insert in front).
         */
        public int[] _hhLinesAfterRows = null;

        /**
         * If provided (can be null): specifies directly after which columns vertical line should be inserted
         * (starting from 0, each entry per one column in the already derived from Excel data matrix, -1 should be
         * used to insert in front).
         */
        public int[] _vertLinesAfterColumns = null;

        /**
         * Data for multicolumn command (note that the data is not validated); the text for multicolumn if taken from the
         * first cell in the pointed series.
         */
        public MultiColumn[] _multiColumns = null;

        /**
         * Data for multirows command  (note that the data is not validated);  the text for multirow if taken from
         * the first cell in the pointed series.
         */
        public MultiRow[] _multiRows = null;

        /**
         * Optional cell preprocessor. When used, processed cells (strings) are preprocessed as imposed by this
         * implementation. It is triggered at the beginning of single-cell processing, i.e., before, e.g., applying
         * decimal precision to numerical values or wrapping the value with dollars. See the implementation of
         * {@link LatexTableFromXLSX#getText(String, Params)}
         */
        public ICellPreprocessor _preProcessor = null;

        /**
         * Optional cell postprocessor. When used, processed cells (strings) are postprocessed as imposed by this
         * implementation. It is triggered at the end of single-cell processing, i.e., after, e.g., applying
         * decimal precision to numerical values or wrapping the value with dollars. See the implementation of
         * {@link LatexTableFromXLSX#getText(String, Params)}
         */
        public ICellPostprocessor _postProcessor = null;

        /**
         * Default constructor.
         */
        public Params()
        {
            this(0, 0, 0);
        }

        /**
         * Parameterized constructor.
         *
         * @param columns the number of columns to read
         * @param rows    the number of rows to read
         * @param sheet   sheet index (starts from 0)
         */
        public Params(int columns, int rows, int sheet)
        {
            this(0, 0, columns, rows, sheet);
        }

        /**
         * Parameterized constructor.
         *
         * @param leftColumn index of the left -- starting -- column of the data table (starts from 0)
         * @param topRow     index of the top -- starting -- row of the data table (starts from 0)
         * @param columns    the number of columns to read
         * @param rows       the number of rows to read
         * @param sheet      sheet index (starts from 0)
         */
        public Params(int leftColumn, int topRow, int columns, int rows, int sheet)
        {
            super(leftColumn, topRow, columns, rows, sheet);
        }


        /**
         * Constructs a deep copy of this object;
         *
         * @return deep copy
         */
        public Params getClone()
        {
            Params p = new Params(_leftColumn, _topRow, _columns, _rows, _sheet);
            p._wrapNumericalWithDollars = _wrapNumericalWithDollars;
            p._separator = _separator;
            p._useEndLineSymbol = _useEndLineSymbol;
            p._replaceCommasWithDots = _replaceCommasWithDots;
            p._decimalPrecisionForNumericalInColumns = _decimalPrecisionForNumericalInColumns.clone();
            p._decimalPrecisionForNumericalInRows = _decimalPrecisionForNumericalInRows.clone();
            p._hhLinesAfterRows = _hhLinesAfterRows.clone();
            p._vertLinesAfterColumns = _vertLinesAfterColumns.clone();
            if (_multiColumns != null)
            {
                p._multiColumns = new MultiColumn[_multiColumns.length];
                for (int i = 0; i < p._multiColumns.length; i++)
                    if (_multiColumns[i] != null)
                        p._multiColumns[i] = _multiColumns[i].getClone();
            }
            if (_multiRows != null)
            {
                p._multiRows = new MultiRow[_multiRows.length];
                for (int i = 0; i < p._multiRows.length; i++)
                    if (_multiRows[i] != null)
                        p._multiRows[i] = _multiRows[i].getClone();
            }
            return p;
        }
    }

    /**
     * The method for loaded a data matrix from an XLSX file (excel) and producing a series of lines suitable for Latex table.
     * Note that the parsed Excel file is not validated (use must ensure that valid data exists).
     *
     * @param path absolute path to the file (includes prefix)
     * @param p    params container
     * @return text (null, if a file cannot be loaded)
     */
    public static String[] getText(String path, Params p)
    {
        return getText(path, new Params[]{p})[0];
    }

    /**
     * The method for loaded a data matrix from an XLSX file (excel) and producing a series of lines suitable for Latex table.
     * Note that the parsed Excel file is not validated (use must ensure that valid data exists).
     *
     * @param path absolute path to the file (includes prefix)
     * @param p    params container; each container will produce one data series (for one table)
     * @return text (null, if a file cannot be loaded); each stored array correspond to one params container passed
     */
    public static String[][] getText(String path, Params[] p)
    {
        File file = new File(path);
        if (p == null) return null;
        if (p.length == 0) return null;

        try (XSSFWorkbook workbook = new XSSFWorkbook(file))
        {
            String[][] text = new String[p.length][];
            for (int i = 0; i < p.length; i++)
            {
                if (p[i] == null) continue;
                Cell[][] cells = DataMatrixFromXLSX.getCellsData(workbook, p[i]);
                String[][] sMatrix = DataMatrixFromXLSX.getStringData(cells);
                text[i] = getText(sMatrix, p[i]);
            }
            return text;

        } catch (IOException | InvalidFormatException e)
        {
            System.out.println("Could not load the file: " + path + "(" + e.getMessage() + ")");
            return null;
        }
    }


    /**
     * The method for loaded a data matrix from an XLSX file (excel) and producing a series of lines suitable for Latex table.
     * Note that the parsed Excel file is not validated (use must ensure that valid data exists).
     *
     * @param data data matrix loaded from Excel file
     * @param p    params container
     * @return text (null, if a file cannot be loaded)
     */
    protected static String[] getText(String[][] data, Params p)
    {
        if (data == null) return null;

        int R = data.length;
        int C = data[0].length;

        // multicolumn/row processing
        Set<Integer> ignoreSeparator = new HashSet<>(R * C);
        if (p._multiColumns != null)
            for (MultiColumn mc : p._multiColumns)
            {
                if ((R <= mc._row) || (data[mc._row] == null)) continue;
                String text = "\\multicolumn{" + mc._span + "}{" + mc._arg + "}{" + data[mc._row][mc._column] + "}";
                data[mc._row][mc._column] = text;
                for (int j = 1; j < mc._span; j++)
                {
                    data[mc._row][mc._column + j] = "";
                    ignoreSeparator.add(mc._row * C + (mc._column + j - 1));
                }
            }


        if (p._multiRows != null)
            for (MultiRow mr : p._multiRows)
            {
                if ((R <= mr._row) || (data[mr._row] == null)) continue;
                String text = "\\multirow{" + mr._span + "}{" + mr._arg + "}{" + data[mr._row][mr._column] + "}";
                data[mr._row][mr._column] = text;
                for (int j = 1; j < mr._span; j++)
                {
                    if ((R <= mr._row + j) || (data[mr._row + j] == null)) continue;
                    data[mr._row + j][mr._column] = "";
                }
            }

        // Set decimal data
        String[] dFormats = null;
        if (p._decimalPrecisionForNumericalInColumns != null)
        {
            dFormats = new String[p._decimalPrecisionForNumericalInColumns.length];
            for (int i = 0; i < p._decimalPrecisionForNumericalInColumns.length; i++)
                dFormats[i] = "%." + p._decimalPrecisionForNumericalInColumns[i] + "f";
        }
        else if (p._decimalPrecisionForNumericalInRows != null)
        {
            dFormats = new String[p._decimalPrecisionForNumericalInRows.length];
            for (int i = 0; i < p._decimalPrecisionForNumericalInRows.length; i++)
                dFormats[i] = "%." + p._decimalPrecisionForNumericalInRows[i] + "f";
        }

        // Create HHLinesData
        Set<Integer> hhLinesRows = new HashSet<>();
        if (p._hhLinesAfterRows != null) for (Integer r : p._hhLinesAfterRows) hhLinesRows.add(r);
        String hhText = "\\hhline{*{" + C + "}{-}}";

        // Create vertical lines data
        Set<Integer> vLinesAfterColumns = new HashSet<>();
        if (p._vertLinesAfterColumns != null) for (Integer r : p._vertLinesAfterColumns) vLinesAfterColumns.add(r);

        LinkedList<String> lines = new LinkedList<>();

        // Create "tabular" row
        StringBuilder sb = new StringBuilder();
        if (vLinesAfterColumns.contains(-1)) sb.append(" |");
        for (int i = 0; i < C; i++)
        {
            sb.append(" c");
            if (vLinesAfterColumns.contains(i)) sb.append(" |");
        }

        lines.add("\\begin{tabular}{" + sb + " }");

        // Check if hhline should be added in front
        if (hhLinesRows.contains(-1)) lines.add(hhText);

        // Parse data
        for (int i = 0; i < R; i++)
        {
            sb = new StringBuilder();
            for (int j = 0; j < C; j++)
            {
                String s = data[i][j];
                if (s == null) s = " ";

                boolean numerical = true;

                String ps = s;
                if (p._preProcessor != null) ps = p._preProcessor.process(s, i, j);

                boolean containsComma = ps.contains(",");

                try
                {
                    double d = Double.parseDouble(ps.replace(',', '.'));
                    if ((p._decimalPrecisionForNumericalInColumns != null) && (j < dFormats.length))
                        ps = String.format(dFormats[j], d);
                    else if ((p._decimalPrecisionForNumericalInRows != null) && (i < dFormats.length))
                        ps = String.format(dFormats[i], d);
                    else ps = String.valueOf(d);
                } catch (NumberFormatException e)
                {
                    numerical = false;
                }

                // bring back comma
                if ((numerical) && (containsComma)) ps = ps.replace('.', ',');

                if ((!ps.isEmpty()) && (numerical) && (p._wrapNumericalWithDollars))
                    ps = "$" + ps + "$";

                if (p._postProcessor != null)
                    ps = p._postProcessor.process(s, ps, i, j);

                sb.append(ps);

                if ((j < C - 1) && (!ignoreSeparator.contains(i * C + j))) sb.append(p._separator);
            }
            if (p._useEndLineSymbol) sb.append(" \\\\");
            if (p._replaceCommasWithDots) lines.add(sb.toString().replace(',', '.'));
            else lines.add(sb.toString());

            if (hhLinesRows.contains(i)) lines.add(hhText);
        }

        // End the table
        lines.add("\\end{tabular}");

        // Parse list to array
        String[] text = new String[lines.size()];
        int index = 0;
        for (String s : lines) text[index++] = s;
        return text;
    }
}
