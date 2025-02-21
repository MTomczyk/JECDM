package thread.swingworker;

/**
 * Class containing types of all blocks encapsulating swing workers used by the module.
 *
 * @author MTomczyk
 */
public enum BlockTypes
{
    /**
     * Data sets updater.
     */
    DATA_SETS_UPDATER,

    /**
     * IDS and render updater ID (on demand).
     */
    IDS_AND_RENDER_UPDATER_ON_DEMAND,

    /**
     * IDS and render updater ID (on resize).
     */
    IDS_AND_RENDER_UPDATER_ON_RESIZE,

    /**
     * IDS and render updater ID (on resize).
     */
    RENDER_UPDATER_ON_INTERACTION,

    /**
     * IDS and render updater ID (on resize).
     */
    RENDER_UPDATER_ON_HEATMAP_DATA_CHANGED,

    /**
     * Heatmap mask changed.
     */
    HEATMAP_MASK_CHANGED,

    /**
     * Heatmap value filter changed.
     */
    HEATMAP_VALUE_FILTER_CHANGED,

    /**
     * Manually notify that the display ranges changed.
     */
    NOTIFY_DISPLAY_RANGES_CHANGED,

    /**
     * Creates screenshot on demand.
     */
    CREATE_SCREENSHOT_ON_DEMAND,
}
