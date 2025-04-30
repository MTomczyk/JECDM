package tools;

/**
 * Interface for auxiliary objects responsible for preprocessing analyzed cells (e.g., by {@link LatexTableFromXLSX}).
 *
 * @author MTomczyk
 */
public interface ICellPreprocessor
{
    /**
     * The main method.
     *
     * @param original original cell value (viewed as string)
     * @param r        row coordinate
     * @param c        column coordinate
     * @return preprocessed cell value (string)
     */
    String process(String original, int r, int c);
}
