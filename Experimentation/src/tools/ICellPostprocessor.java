package tools;

/**
 * Interface for auxiliary objects responsible for postprocessing analyzed cells (e.g., by {@link LatexTableFromXLSX}).
 *
 * @author MTomczyk
 */
public interface ICellPostprocessor
{
    /**
     * The main method.
     *
     * @param original     original cell value (viewed as string)
     * @param preprocessed original (before starting post-processing) cell string value
     * @param r            row coordinate
     * @param c            column coordinate
     * @return postprocessed cell value (viewed as string)
     */
    String process(String original, String preprocessed, int r, int c);
}
