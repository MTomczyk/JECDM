package tools;

/**
 * Interface for auxiliary preprocessors used by {@link LatexTableFromXLSX} when determining the "hhline" command
 * (string).
 *
 * @author MTomczyk
 */
public interface IHHLinePreprocessor
{
    /**
     * The main method. Should return the "hhline" command string (e.g., "\\hhline{*{" + columns + "}{-}}").
     *
     * @param row     current row number being processed
     * @param columns total number of columns in the table
     * @return command string
     */
    String process(int row, int columns);
}
