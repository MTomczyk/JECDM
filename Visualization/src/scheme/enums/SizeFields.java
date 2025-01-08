package scheme.enums;

/**
 * PLot components that can be adjusted using some size.
 */
public enum SizeFields
{
    /**
     * Plot: border width (fixed size).
     */
    PLOT_BORDER_WIDTH_FIXED,

    /**
     * Plot: border width (size relative multiplier).
     */
    PLOT_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Loading panel: font size (fixed size).
     */
    LOADING_PANEL_FONT_SIZE_FIXED,

    /**
     * Loading panel: font size (size relative multiplier).
     */
    LOADING_PANEL_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Margin: left size (fixed size).
     */
    MARGIN_LEFT_SIZE_FIXED,

    /**
     * Margin: left size (size relative multiplier).
     */
    MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER,

    /**
     * Margin: top size (fixed size).
     */
    MARGIN_TOP_SIZE_FIXED,

    /**
     * Margin: top size (size relative multiplier).
     */
    MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER,

    /**
     * Margin: right size (fixed size).
     */
    MARGIN_RIGHT_SIZE_FIXED,

    /**
     * Margin: right size (size relative multiplier).
     */
    MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER,

    /**
     * Margin: bottom size (fixed size).
     */
    MARGIN_BOTTOM_SIZE_FIXED,

    /**
     * Margin: bottom size (size relative multiplier).
     */
    MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER,

    /**
     * Drawing area: border width (fixed size).
     */
    DRAWING_AREA_BORDER_WIDTH_FIXED,

    /**
     * Drawing area: border width (size relative multiplier).
     */
    DRAWING_AREA_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Drawing area: inner offset (fixed size).
     */
    DRAWING_AREA_INNER_OFFSET_FIXED,

    /**
     * Drawing area: inner offset (size relative multiplier).
     */
    DRAWING_AREA_INNER_OFFSET_RELATIVE_MULTIPLIER,


    /**
     * Title: font size (fixed size).
     */
    TITLE_FONT_SIZE_FIXED,

    /**
     * Title: font size (size relative multiplier).
     */
    TITLE_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Title: offset (fixed size).
     */
    TITLE_OFFSET_FIXED,

    /**
     * Title: offset (size relative multiplier).
     */
    TITLE_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Title: border width (fixed size).
     */
    TITLE_BORDER_WIDTH_FIXED,

    /**
     * Title: border width (size relative multiplier).
     */
    TITLE_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Popup menu: item font size (fixed size).
     */
    POPUP_MENU_ITEM_FONT_SIZE_FIXED,

    /**
     * Popup menu: item font size (size relative multiplier).
     */
    POPUP_MENU_ITEM_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Grid: main lines width (fixed size).
     */
    GRID_MAIN_LINES_WIDTH_FIXED,

    /**
     * Grid: main lines width (size relative multiplier).
     */
    GRID_MAIN_LINES_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Grid: auxiliary lines width (fixed size).
     */
    GRID_AUX_LINES_WIDTH_FIXED,

    /**
     * Grid: auxiliary lines width (size relative multiplier).
     */
    GRID_AUX_LINES_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Legend: border width (fixed size).
     */
    LEGEND_BORDER_WIDTH_FIXED,

    /**
     * Legend: border width (size relative multiplier).
     */
    LEGEND_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Legend: offset (fixed size).
     */
    LEGEND_OFFSET_FIXED,

    /**
     * Legend: offset (size relative multiplier).
     */
    LEGEND_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Legend: inner offset (fixed size).
     */
    LEGEND_INNER_OFFSET_FIXED,

    /**
     * Legend: inner offset (size relative multiplier).
     */
    LEGEND_INNER_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Legend: entry font size (fixed size).
     */
    LEGEND_ENTRY_FONT_SIZE_FIXED,

    /**
     * Legend: entry font size (size relative multiplier).
     */
    LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Legend: entries spacing (fixed size).
     */
    LEGEND_ENTRIES_SPACING_FIXED,

    /**
     * Legend: entries spacing (size relative multiplier).
     */
    LEGEND_ENTRIES_SPACING_RELATIVE_MULTIPLIER,

    /**
     * Legend: columns separator (fixed size).
     */
    LEGEND_COLUMNS_SEPARATOR_FIXED,

    /**
     * Legend: columns separator (size relative multiplier).
     */
    LEGEND_COLUMNS_SEPARATOR_RELATIVE_MULTIPLIER,

    /**
     * Legend: drawing-label separator (fixed size).
     */
    LEGEND_DRAWING_LABEL_SEPARATOR_FIXED,

    /**
     * Legend: drawing-label separator (size relative multiplier).
     */
    LEGEND_DRAWING_LABEL_SEPARATOR_RELATIVE_MULTIPLIER,

    /**
     * Legend: marker scaling factor multiplier.
     */
    LEGEND_MARKER_SCALING_FACTOR_MULTIPLIER,

    /**
     * Legend: line scaling factor multiplier.
     */
    LEGEND_LINE_SCALING_FACTOR_MULTIPLIER,

    /**
     * Colorbar: border width (fixed size).
     */
    COLORBAR_BORDER_WIDTH_FIXED,

    /**
     * Colorbar: border width (size relative multiplier).
     */
    COLORBAR_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Colorbar: edge width (fixed size).
     */
    COLORBAR_EDGE_WIDTH_FIXED,

    /**
     * Colorbar: edge width (size relative multiplier).
     */
    COLORBAR_EDGE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Colorbar: offset (fixed size).
     */
    COLORBAR_OFFSET_FIXED,

    /**
     * Colorbar: offset (size relative multiplier).
     */
    COLORBAR_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Colorbar: width (fixed size).
     */
    COLORBAR_WIDTH_FIXED,

    /**
     * Colorbar: width (size relative multiplier).
     */
    COLORBAR_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Colorbar: shrink value.
     */
    COLORBAR_SHRINK,

    /**
     * X-axis: main line width (fixed size).
     */
    AXIS_X_MAIN_LINE_WIDTH_FIXED,

    /**
     * Y-axis: main line width (fixed size).
     */
    AXIS_Y_MAIN_LINE_WIDTH_FIXED,

    /**
     * Z-axis: main line width (fixed size).
     */
    AXIS_Z_MAIN_LINE_WIDTH_FIXED,

    /**
     * A1-axis (auxiliary): main line width (fixed size).
     */
    AXIS_A1_MAIN_LINE_WIDTH_FIXED,

    /**
     * Axis (colorbar): main line width (fixed size).
     */
    AXIS_COLORBAR_MAIN_LINE_WIDTH_FIXED,

    /**
     * X-axis: main line width (size relative multiplier).
     */
    AXIS_X_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Y-axis: main line width (size relative multiplier).
     */
    AXIS_Y_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Z-axis: main line width (size relative multiplier).
     */
    AXIS_Z_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * A1-axis (auxiliary): main line width (size relative multiplier).
     */
    AXIS_A1_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Axis (colorbar): main line width (size relative multiplier).
     */
    AXIS_COLORBAR_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * X-axis: tick line width (fixed size).
     */
    AXIS_X_TICK_LINE_WIDTH_FIXED,

    /**
     * Y-axis: tick line width (fixed size).
     */
    AXIS_Y_TICK_LINE_WIDTH_FIXED,

    /**
     * Z-axis: tick line width (fixed size).
     */
    AXIS_Z_TICK_LINE_WIDTH_FIXED,
    /**
     * A1-axis (auxiliary): tick line width (fixed size).
     */
    AXIS_A1_TICK_LINE_WIDTH_FIXED,

    /**
     * Axis (colorbar): tick line width (fixed size).
     */
    AXIS_COLORBAR_TICK_LINE_WIDTH_FIXED,

    /**
     * X-axis: tick line width (size relative multiplier).
     */
    AXIS_X_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Y-axis: tick line width (size relative multiplier).
     */
    AXIS_Y_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Z-axis: tick line width (size relative multiplier).
     */
    AXIS_Z_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * A1-axis (auxiliary): tick line width (size relative multiplier).
     */
    AXIS_A1_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Axis (colorbar) tick line width (size relative multiplier).
     */
    AXIS_COLORBAR_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * X-axis: tick size (fixed size).
     */
    AXIS_X_TICK_SIZE_FIXED,

    /**
     * Y-axis: tick size (fixed size).
     */
    AXIS_Y_TICK_SIZE_FIXED,

    /**
     * Z-axis: tick size (fixed size).
     */
    AXIS_Z_TICK_SIZE_FIXED,

    /**
     * A1-axis (auxiliary): tick size (fixed size).
     */
    AXIS_A1_TICK_SIZE_FIXED,

    /**
     * Axis (colorbar): tick size (fixed size).
     */
    AXIS_COLORBAR_TICK_SIZE_FIXED,

    /**
     * X-axis: tick size (size relative multiplier).
     */
    AXIS_X_TICK_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Y-axis: tick size (size relative multiplier).
     */
    AXIS_Y_TICK_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Z-axis: tick size (size relative multiplier).
     */
    AXIS_Z_TICK_SIZE_RELATIVE_MULTIPLIER,

    /**
     * A1-axis (auxiliary): tick size (size relative multiplier).
     */
    AXIS_A1_TICK_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Axis (colorbar): tick size (size relative multiplier).
     */
    AXIS_COLORBAR_TICK_SIZE_RELATIVE_MULTIPLIER,

    /**
     * X-axis: border width (fixed size).
     */
    AXIS_X_BORDER_WIDTH_FIXED,

    /**
     * Y-axis: border width (fixed size).
     */
    AXIS_Y_BORDER_WIDTH_FIXED,

    /**
     * Z-axis: border width (fixed size).
     */
    AXIS_Z_BORDER_WIDTH_FIXED,

    /**
     * A1-axis (auxiliary): border width (fixed size).
     */
    AXIS_A1_BORDER_WIDTH_FIXED,

    /**
     * Axis (colorbar): border width (fixed size).
     */
    AXIS_COLORBAR_BORDER_WIDTH_FIXED,


    /**
     * X-axis: border width (size relative multiplier)).
     */
    AXIS_X_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Y-axis: border width (size relative multiplier)).
     */
    AXIS_Y_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Z-axis: border width (size relative multiplier)).
     */
    AXIS_Z_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * A1-axis (auxiliary): border width (size relative multiplier)).
     */
    AXIS_A1_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * Axis (colorbar): border width (size relative multiplier).
     */
    AXIS_COLORBAR_BORDER_WIDTH_RELATIVE_MULTIPLIER,

    /**
     * X-axis: tick label font size (fixed size).
     */
    AXIS_X_TICK_LABEL_FONT_SIZE_FIXED,

    /**
     * Y-axis: tick label font size (fixed size).
     */
    AXIS_Y_TICK_LABEL_FONT_SIZE_FIXED,

    /**
     * Z-axis: tick label font size (fixed size).
     */
    AXIS_Z_TICK_LABEL_FONT_SIZE_FIXED,

    /**
     * A1-axis (auxiliary): tick label font size (fixed size).
     */
    AXIS_A1_TICK_LABEL_FONT_SIZE_FIXED,

    /**
     * Axis (colorbar): tick label font size (fixed size).
     */
    AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_FIXED,

    /**
     * X-axis: tick label font size (size relative multiplier).
     */
    AXIS_X_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Y-axis: tick label font size (size relative multiplier).
     */
    AXIS_Y_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Z-axis: tick label font size (size relative multiplier).
     */
    AXIS_Z_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * A1-axis (auxiliary): tick label font size (size relative multiplier).
     */
    AXIS_A1_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Axis (colorbar): tick label font size (size relative multiplier).
     */
    AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * X-axis: tick label offset (fixed size).
     */
    AXIS_X_TICK_LABEL_OFFSET_FIXED,

    /**
     * Y-axis: tick label offset (fixed size).
     */
    AXIS_Y_TICK_LABEL_OFFSET_FIXED,

    /**
     * Z-axis: tick label offset (fixed size).
     */
    AXIS_Z_TICK_LABEL_OFFSET_FIXED,

    /**
     * A1-axis (auxiliary): tick label offset (fixed size).
     */
    AXIS_A1_TICK_LABEL_OFFSET_FIXED,

    /**
     * Axis (colorbar): tick label offset (fixed size).
     */
    AXIS_COLORBAR_TICK_LABEL_OFFSET_FIXED,

    /**
     * X-axis: tick label offset (size relative multiplier).
     */
    AXIS_X_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Y-axis: tick label offset (size relative multiplier).
     */
    AXIS_Y_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Z-axis: tick label offset (size relative multiplier).
     */
    AXIS_Z_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * A1-axis (auxiliary): tick label offset (size relative multiplier).
     */
    AXIS_A1_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Axis (colorbar): tick label offset (size relative multiplier).
     */
    AXIS_COLORBAR_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * X-axis: title font size (fixed size).
     */
    AXIS_X_TITLE_FONT_SIZE_FIXED,

    /**
     * Y-axis: title font size (fixed size).
     */
    AXIS_Y_TITLE_FONT_SIZE_FIXED,

    /**
     * Z-axis: title font size (fixed size).
     */
    AXIS_Z_TITLE_FONT_SIZE_FIXED,

    /**
     * A1-axis (auxiliary): title font size (fixed size).
     */
    AXIS_A1_TITLE_FONT_SIZE_FIXED,

    /**
     * Axis (colorbar): title font size (fixed size).
     */
    AXIS_COLORBAR_TITLE_FONT_SIZE_FIXED,

    /**
     * X-axis: title font size (size relative multiplier).
     */
    AXIS_X_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Y-axis: title font size (size relative multiplier).
     */
    AXIS_Y_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Z-axis: title font size (size relative multiplier).
     */
    AXIS_Z_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * A1-axis (auxiliary): title font size (size relative multiplier).
     */
    AXIS_A1_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER,

    /**
     * Axis (colorbar): title font size (size relative multiplier).
     */
    AXIS_COLORBAR_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER,


    /**
     * X-axis: title offset (fixed size).
     */
    AXIS_X_TITLE_OFFSET_FIXED,

    /**
     * Y-axis: title offset (fixed size).
     */
    AXIS_Y_TITLE_OFFSET_FIXED,

    /**
     * Z-axis: title offset (fixed size).
     */
    AXIS_Z_TITLE_OFFSET_FIXED,

    /**
     * A1-axis (auxiliary): title offset (fixed size).
     */
    AXIS_A1_TITLE_OFFSET_FIXED,

    /**
     * Axis (colorbar): title offset (fixed size).
     */
    AXIS_COLORBAR_TITLE_OFFSET_FIXED,

    /**
     * X-axis: title offset (size relative multiplier).
     */
    AXIS_X_TITLE_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Y-axis: title offset (size relative multiplier).
     */
    AXIS_Y_TITLE_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Z-axis: title offset (size relative multiplier).
     */
    AXIS_Z_TITLE_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * A1-axis (auxiliary): title offset (size relative multiplier).
     */
    AXIS_A1_TITLE_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * Axis (colorbar): title offset (size relative multiplier).
     */
    AXIS_COLORBAR_TITLE_OFFSET_RELATIVE_MULTIPLIER,

    /**
     * X-axis (3D): tick size (fixed size).
     */
    AXIS3D_X_TICK_SIZE,

    /**
     * Y-axis (3D): tick size (fixed size).
     */
    AXIS3D_Y_TICK_SIZE,

    /**
     * Z-axis (3D): tick size (fixed size).
     */
    AXIS3D_Z_TICK_SIZE,

    /**
     * X-axis (3D): tick label offset (fixed size).
     */
    AXIS3D_X_TICK_LABEL_OFFSET,

    /**
     * Y-axis (3D): tick label offset (fixed size).
     */
    AXIS3D_Y_TICK_LABEL_OFFSET,

    /**
     * Z-axis (3D): tick label offset (fixed size).
     */
    AXIS3D_Z_TICK_LABEL_OFFSET,

    /**
     * X-axis (3D): title offset (fixed size).
     */
    AXIS3D_X_TITLE_OFFSET,

    /**
     * Y-axis (3D): title offset (fixed size).
     */
    AXIS3D_Y_TITLE_OFFSET,

    /**
     * Z-axis (3D): title offset (fixed size).
     */
    AXIS3D_Z_TITLE_OFFSET,

    /**
     * X-axis (3D): tick label font size scale (fixed size).
     */
    AXIS3D_X_TICK_LABEL_FONT_SIZE_SCALE,

    /**
     * Y-axis (3D): tick label font size scale (fixed size).
     */
    AXIS3D_Y_TICK_LABEL_FONT_SIZE_SCALE,

    /**
     * Z-axis (3D): tick label font size scale (fixed size).
     */
    AXIS3D_Z_TICK_LABEL_FONT_SIZE_SCALE,

    /**
     * X-axis (3D): title font size scale (fixed size).
     */
    AXIS3D_X_TITLE_FONT_SIZE_SCALE,

    /**
     * Y-axis (3D): title font size scale (fixed size).
     */
    AXIS3D_Y_TITLE_FONT_SIZE_SCALE,

    /**
     * Z-axis (3D): title font size scale (fixed size).
     */
    AXIS3D_Z_TITLE_FONT_SIZE_SCALE,

}
