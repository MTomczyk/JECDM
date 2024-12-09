package runner.enums;

/**
 * Provides flags that indicate when the visualization (if turned on) should begin.
 *
 * @author MTomczyk
 */
public enum DisplayMode
{
    /**
     * If this flag is used, visualization modules are displayed at the beginning and updated throughout evolution.
     */
    FROM_THE_BEGINNING,

    /**
     * If this flag is used, visualization modules are displayed at the very end.
     */
    AT_THE_END
}
