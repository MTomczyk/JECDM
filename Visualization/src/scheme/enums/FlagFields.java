package scheme.enums;

/**
 * PLot components that can be flagged.
 */
public enum FlagFields
{
    /**
     * Plot opaque flag.
     */
    PLOT_OPAQUE,

    /**
     * Plot border: use relative width flag.
     */
    PLOT_BORDER_USE_RELATIVE_WIDTH,

    /**
     * Loading panel: use relative size flag.
     */
    LOADING_PANEL_USE_RELATIVE_SIZE,

    /**
     * Left margin: use relative size flag.
     */
    MARGIN_LEFT_USE_RELATIVE_SIZE,

    /**
     * Top margin: use relative size flag.
     */
    MARGIN_TOP_USE_RELATIVE_SIZE,

    /**
     * Right margin: use relative size flag.
     */
    MARGIN_RIGHT_USE_RELATIVE_SIZE,

    /**
     * Bottom margin: use relative size flag.
     */
    MARGIN_BOTTOM_USE_RELATIVE_SIZE,

    /**
     * Margins: conditionally ignore flag.
     */
    MARGINS_CONDITIONALLY_IGNORE,

    /**
     * Drawing area: use border relative width flag.
     */
    DRAWING_AREA_USE_BORDER_RELATIVE_WIDTH,

    /**
     * Drawing area: use inner offset relative size flag.
     */
    DRAWING_AREA_USE_INNER_OFFSET_RELATIVE_SIZE,

    /**
     * Drawing area: opaque flag.
     */
    DRAWING_AREA_OPAQUE,

    /**
     * Title: opaque flag.
     */
    TITLE_OPAQUE,

    /**
     * Title border: use relative width flag.
     */
    TITLE_BORDER_USE_RELATIVE_WIDTH,

    /**
     * Title font: use relative size flag.
     */
    TITLE_FONT_USE_RELATIVE_SIZE,

    /**
     * Title offset: use relative size flag.
     */
    TITLE_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Popup menu item: font use relative size flag.
     */
    POPUP_MENU_ITEM_FONT_USE_RELATIVE_SIZE,

    /**
     * Grid main lines: use relative width flag.
     */
    GRID_MAIN_LINES_USE_RELATIVE_WIDTH,

    /**
     * Grid auxiliary lines: use relative width flag.
     */
    GRID_AUX_LINES_USE_RELATIVE_WIDTH,

    /**
     * Legend: border use relative width flag.
     */
    LEGEND_BORDER_USE_RELATIVE_WIDTH,

    /**
     * Legend: drawing-label offset use relative size flag.
     */
    LEGEND_DRAWING_LABEL_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Legend: columns separator use relative size flag.
     */
    LEGEND_COLUMNS_SEPARATOR_USE_RELATIVE_SIZE,

    /**
     * Legend: inner offset use relative size flag.
     */
    LEGEND_INNER_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Legend: font entry use relative size flag.
     */
    LEGEND_ENTRY_FONT_USE_RELATIVE_SIZE,

    /**
     * Legend: entries spacing use relative size flag.
     */
    LEGEND_ENTRIES_SPACING_USE_RELATIVE_SIZE,

    /**
     * Legend: drawing-label separator use relative size flag.
     */
    LEGEND_DRAWING_LABEL_SEPARATOR_USE_RELATIVE_SIZE,

    /**
     * Legend: opaque flag.
     */
    LEGEND_OPAQUE,

    /**
     * Colorbar: border use relative width flag.
     */
    COLORBAR_BORDER_USE_RELATIVE_WITH,

    /**
     * Colorbar: edge use relative width flag.
     */
    COLORBAR_EDGE_USE_RELATIVE_WIDTH,

    /**
     * Colorbar: offset use relative size flag.
     */
    COLORBAR_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Colorbar: width use relative size flag.
     */
    COLORBAR_WIDTH_USE_RELATIVE_SIZE,

    /**
     * Colorbar: opaque.
     */
    COLORBAR_OPAQUE,

    /**
     * X-axis: main line use relative width flag.
     */
    AXIS_X_MAIN_LINE_USE_RELATIVE_WIDTH,

    /**
     * Y-axis: main line use relative width flag.
     */
    AXIS_Y_MAIN_LINE_USE_RELATIVE_WIDTH,

    /**
     * Z-axis: main line use relative width flag.
     */
    AXIS_Z_MAIN_LINE_USE_RELATIVE_WIDTH,

    /**
     * A1-axis (auxiliary): main line use relative width flag.
     */
    AXIS_A1_MAIN_LINE_USE_RELATIVE_WIDTH,

    /**
     * Axis (colorbar): main line use relative width flag.
     */
    AXIS_COLORBAR_MAIN_LINE_USE_RELATIVE_WIDTH,

    /**
     * X-axis: tick line use relative width flag.
     */
    AXIS_X_TICK_LINE_USE_RELATIVE_WIDTH,

    /**
     * Y-axis: tick line use relative width flag.
     */
    AXIS_Y_TICK_LINE_USE_RELATIVE_WIDTH,

    /**
     * Z-axis: tick line use relative width flag.
     */
    AXIS_Z_TICK_LINE_USE_RELATIVE_WIDTH,

    /**
     * A1-axis (auxiliary): tick line use relative width flag.
     */
    AXIS_A1_TICK_LINE_USE_RELATIVE_WIDTH,

    /**
     * Axis (colorbar): tick line use relative width flag.
     */
    AXIS_COLORBAR_TICK_LINE_USE_RELATIVE_WIDTH,

    /**
     * X-axis: border use relative width flag.
     */
    AXIS_X_BORDER_USE_RELATIVE_WIDTH,

    /**
     * Y-axis: border use relative width flag.
     */
    AXIS_Y_BORDER_USE_RELATIVE_WIDTH,

    /**
     * Z-axis: border use relative width flag.
     */
    AXIS_Z_BORDER_USE_RELATIVE_WIDTH,

    /**
     * A1-axis (auxiliary): border use relative width flag.
     */
    AXIS_A1_BORDER_USE_RELATIVE_WIDTH,

    /**
     * Axis (colorbar): border use relative width flag.
     */
    AXIS_COLORBAR_BORDER_USE_RELATIVE_WIDTH,

    /**
     * X-axis: tick use relative size flag.
     */
    AXIS_X_TICK_USE_RELATIVE_SIZE,

    /**
     * Y-axis: tick use relative size flag.
     */
    AXIS_Y_TICK_USE_RELATIVE_SIZE,

    /**
     * Z-axis: tick use relative size flag.
     */
    AXIS_Z_TICK_USE_RELATIVE_SIZE,

    /**
     * A1-axis (auxiliary): tick use relative size flag.
     */
    AXIS_A1_TICK_USE_RELATIVE_SIZE,

    /**
     * Axis (colorbar): tick use relative size flag.
     */
    AXIS_COLORBAR_TICK_USE_RELATIVE_SIZE,

    /**
     * X-axis: tick label font use relative size flag.
     */
    AXIS_X_TICK_LABEL_FONT_USE_RELATIVE_SIZE,

    /**
     * Y-axis: tick label font use relative size flag.
     */
    AXIS_Y_TICK_LABEL_FONT_USE_RELATIVE_SIZE,

    /**
     * Z-axis: tick label font use relative size flag.
     */
    AXIS_Z_TICK_LABEL_FONT_USE_RELATIVE_SIZE,

    /**
     * A1-axis (auxiliary): tick label font use relative size flag.
     */
    AXIS_A1_TICK_LABEL_FONT_USE_RELATIVE_SIZE,

    /**
     * Axis (colorbar): tick label font use relative size flag.
     */
    AXIS_COLORBAR_TICK_LABEL_FONT_USE_RELATIVE_SIZE,

    /**
     * X-axis: tick label offset use relative size flag.
     */
    AXIS_X_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Y-axis: tick label offset use relative size flag.
     */
    AXIS_Y_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Z-axis: tick label offset use relative size flag.
     */
    AXIS_Z_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE,

    /**
     * A1-axis (auxiliary): tick label offset use relative size flag.
     */
    AXIS_A1_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Axis (colorbar): tick label offset use relative size flag.
     */
    AXIS_COLORBAR_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE,

    /**
     * X-axis: font title use relative size flag.
     */
    AXIS_X_TITLE_FONT_USE_RELATIVE_SIZE,

    /**
     * Y-axis: font title use relative size flag.
     */
    AXIS_Y_TITLE_FONT_USE_RELATIVE_SIZE,

    /**
     * Z-axis: font title use relative size flag.
     */
    AXIS_Z_TITLE_FONT_USE_RELATIVE_SIZE,

    /**
     * A1-axis (auxiliary): font title use relative size flag.
     */
    AXIS_A1_TITLE_FONT_USE_RELATIVE_SIZE,

    /**
     * Axis (colorbar): font title use relative size flag.
     */
    AXIS_COLORBAR_TITLE_FONT_USE_RELATIVE_SIZE,

    /**
     * X-axis: title offset use relative size flag.
     */
    AXIS_X_TITLE_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Y-axis: title offset use relative size flag.
     */
    AXIS_Y_TITLE_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Z-axis: title offset use relative size flag.
     */
    AXIS_Z_TITLE_OFFSET_USE_RELATIVE_SIZE,

    /**
     * A1-axis (auxiliary): title offset use relative size flag.
     */
    AXIS_A1_TITLE_OFFSET_USE_RELATIVE_SIZE,

    /**
     * Axis (colorbar): title offset use relative size flag.
     */
    AXIS_COLORBAR_TITLE_OFFSET_USE_RELATIVE_SIZE,

    /**
     * X-axis: opaque flag.
     */
    AXIS_X_OPAQUE,

    /**
     * Y-axis: opaque flag.
     */
    AXIS_Y_OPAQUE,

    /**
     * Z-axis: opaque flag.
     */
    AXIS_Z_OPAQUE,

    /**
     * A1-axis (auxiliary): opaque flag.
     */
    AXIS_A1_OPAQUE,

    /**
     * Axis (colorbar): opaque flag.
     */
    AXIS_COLORBAR_OPAQUE,

    /**
     * Debug flag: disable front drawing.
     */
    DEBUG_DISABLE_FRONT_DRAWING,
}
