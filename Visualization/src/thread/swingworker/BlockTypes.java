package thread.swingworker;

/**
 * Class containing types of all blocks encapsulating swing workers used by the module.
 *
 * @author MTomczyk
 */
public class BlockTypes
{
    /**
     * The number of unique and contiguous types.
     */
    public static final int NO_TYPES = 9;

    /**
     * Data sets updater.
     */
    public static final int DATA_SETS_UPDATER = 0;

    /**
     * IDS and render updater ID (on demand).
     */
    public static final int IDS_AND_RENDER_UPDATER_ON_DEMAND = 1;

    /**
     * IDS and render updater ID (on resize).
     */
    public static final int IDS_AND_RENDER_UPDATER_ON_RESIZE = 2;

    /**
     * IDS and render updater ID (on resize).
     */
    public static final int RENDER_UPDATER_ON_INTERACTION = 3;

    /**
     * IDS and render updater ID (on resize).
     */
    public static final int RENDER_UPDATER_ON_HEATMAP_DATA_CHANGED = 4;

    /**
     * Heatmap mask changed.
     */
    public static final int HEATMAP_MASK_CHANGED = 5;

    /**
     * Heatmap value filter changed.
     */
    public static final int HEATMAP_VALUE_FILTER_CHANGED = 6;

    /**
     * Manually notify that the display ranges changed.
     */
    public static final int NOTIFY_DISPLAY_RANGES_CHANGED = 7;

    /**
     * Creates screenshot on demand.
     */
    public static final int CREATE_SCREENSHOT_ON_DEMAND = 8;
}
