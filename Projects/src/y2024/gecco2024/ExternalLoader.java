package y2024.gecco2024;

/**
 * Used to wrap the main {@link Loader} class so that args can be passed (path prefix surpass).
 *
 * @author MTomczyk
 */

public class ExternalLoader
{
    /**
     * Main method to be executed.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        String pathPrefix = "Projects/src/y2024/gecco2024/case2/save/";
        Loader.main(new String[]{pathPrefix});
    }
}
