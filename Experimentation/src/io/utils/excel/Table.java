package io.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Simple wrapper class for cells that are supposed to represent a table.
 */
public class Table
{
    /**
     * Table width (in columns).
     */
    public final int _w;

    /**
     * Table height (in rows).
     */
    public final int _h;

    /**
     * Table x-coordinate (left-top; in columns).
     */
    public final int _x;

    /**
     * Table y-coordinate (left-top; in rows).
     */
    public final int _y;

    /**
     * Cells constituting the table.
     */
    public final Cell[][] _cell;

    /**
     * Sheet containing the table.
     */
    public final Sheet _sheet;

    /**
     * Parameterized constructor.
     *
     * @param x     x-coordinate (left-top; in columns)
     * @param y     y-coordinate (left-top; in rows)
     * @param w     width (in columns)
     * @param h     height (in rows)
     * @param sheet sheet (parent for the table)
     */
    public Table(int x, int y, int w, int h, Sheet sheet)
    {
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        _sheet = sheet;
        _cell = new Cell[h][w];
        for (int r = y; r < y + h; r++)
        {
            Row row = sheet.getRow(r);
            if (row == null) row = sheet.createRow(r);

            for (int c = x; c < x + w; c++)
            {
                Cell cell = row.getCell(c);
                if (cell == null) cell = row.createCell(c);
                _cell[r - y][c - x] = cell;
            }
        }
    }

    /**
     * Applies cell style to all cells. Important note: all cell styles are defined as new objects (input clones)
     *
     * @param cellStyle cell style to be set
     * @param wb        workbook
     */
    public void applyCellStyleClones(CellStyle cellStyle, Workbook wb)
    {
        if (_cell == null) return;
        for (Cell[] cells : _cell)
        {
            if (cells == null) continue;
            for (Cell cell : cells)
            {
                if (cell == null) continue;
                CellStyle newStyle = wb.createCellStyle();
                newStyle.cloneStyleFrom(cellStyle);
                cell.setCellStyle(newStyle);
            }
        }
    }

    /**
     * The method applies the background for cells within the specified coordinates (the x and y coordinates are
     * relative to the table's x and y coordinates). If the bounds exceed the table, they are truncated.
     *
     * @param x     x-coordinate (left-top; relative to the table's x-coordinate)
     * @param y     y-coordinate (left-top; relative to the table's y-coordinate)
     * @param w     selection width
     * @param h     selection height
     * @param color color to be set (index)
     */
    public void applyBackground(int x, int y, int w, int h, short color)
    {
        int[] tc = getTruncatedCoords(x, y, w, h);
        if (tc == null) return;

        for (int j = tc[1]; j < tc[1] + tc[3]; j++)
        {
            for (int i = tc[0]; i < tc[0] + tc[2]; i++)
            {
                CellStyle cs = _cell[j][i].getCellStyle();
                cs.setFillForegroundColor(color);
                cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
        }
    }


    /**
     * The method applies the cell style to those cells within the specified coordinates (the x and y coordinates are
     * relative to the table's x and y coordinates). If the bounds exceed the table, they are truncated.
     *
     * @param x         x-coordinate (left-top; relative to the table's x-coordinate)
     * @param y         y-coordinate (left-top; relative to the table's y-coordinate)
     * @param w         selection width
     * @param h         selection height
     * @param cellStyle cell style to be set
     */
    public void applyCellStyleToCells(int x, int y, int w, int h, CellStyle cellStyle)
    {
        if (cellStyle == null) return;
        int[] tc = getTruncatedCoords(x, y, w, h);
        if (tc == null) return;
        for (int j = tc[1]; j < tc[1] + tc[3]; j++)
            for (int i = tc[0]; i < tc[0] + tc[2]; i++)
                _cell[j][i].setCellStyle(cellStyle);
    }

    /**
     * The method applies the alignment for cells within the specified coordinates (the x and y coordinates are
     * relative to the table's x and y coordinates). If the bounds exceed the table, they are truncated.
     *
     * @param x  x-coordinate (left-top; relative to the table's x-coordinate)
     * @param y  y-coordinate (left-top; relative to the table's y-coordinate)
     * @param w  selection width
     * @param h  selection height
     * @param ha horizontal alignment
     * @param va vertical alignment
     */
    public void applyAlignment(int x, int y, int w, int h, HorizontalAlignment ha, VerticalAlignment va)
    {
        int[] tc = getTruncatedCoords(x, y, w, h);
        if (tc == null) return;

        for (int j = tc[1]; j < tc[1] + tc[3]; j++)
        {
            for (int i = tc[0]; i < tc[0] + tc[2]; i++)
            {
                CellStyle cs = _cell[j][i].getCellStyle();
                cs.setAlignment(ha);
                cs.setVerticalAlignment(va);
            }
        }
    }

    /**
     * Apples chessboard backgrounds (every second segment).
     *
     * @param x     starting X-coordinate
     * @param y     starting Y-coordinates
     * @param dxs   delta-x (widths) for segments
     * @param dys   delta-y (height) for segments
     * @param color color for the chessboard
     */
    public void applyChessboard(int x, int y, int[] dxs, int[] dys, short color)
    {
        boolean startGrey = true;
        boolean grey;
        int sX = x;
        for (int dx : dxs)
        {
            startGrey = !startGrey;
            grey = startGrey;
            int sY = y;
            for (int dy : dys)
            {
                if (grey) applyBackground(sX, sY, dx, dy, color);
                grey = !grey;
                sY += dy;
            }
            sX += dx;
        }
    }


    /**
     * The method applies a border to the area within the specified coordinates (the x and y coordinates are
     * relative to the table's x and y coordinates). If the bounds exceed the table, they are truncated.
     *
     * @param color color to be set (index)
     * @param x     x-coordinate (left-top; relative to the table's x-coordinate)
     * @param y     y-coordinate (left-top; relative to the table's y-coordinate)
     * @param w     selection width
     * @param h     selection height
     * @param bs    border style
     */
    public void applyBorder(int x, int y, int w, int h, short color, BorderStyle bs)
    {
        int[] tc = getTruncatedCoords(x, y, w, h);
        if (tc == null) return;

        // top/bottom
        for (int i = tc[0]; i < tc[0] + tc[2]; i++)
        {
            _cell[tc[1]][i].getCellStyle().setBorderTop(bs);
            _cell[tc[1]][i].getCellStyle().setTopBorderColor(color);
            _cell[tc[1] + tc[3] - 1][i].getCellStyle().setBorderBottom(bs);
            _cell[tc[1] + tc[3] - 1][i].getCellStyle().setBottomBorderColor(color);
        }
        // left/right
        for (int j = tc[1]; j < tc[1] + tc[3]; j++)
        {
            _cell[j][tc[0]].getCellStyle().setBorderLeft(bs);
            _cell[j][tc[0]].getCellStyle().setLeftBorderColor(color);
            _cell[j][tc[0] + tc[2] - 1].getCellStyle().setBorderRight(bs);
            _cell[j][tc[0] + tc[2] - 1].getCellStyle().setRightBorderColor(color);
        }
    }

    /**
     * The method makes all the cells within the specified coordinates blank (the x and y coordinates are
     * relative to the table's x and y coordinates). If the bounds exceed the table, they are truncated.
     *
     * @param x x-coordinate (left-top; relative to the table's x-coordinate)
     * @param y y-coordinate (left-top; relative to the table's y-coordinate)
     * @param w selection width
     * @param h selection height
     */
    public void makeBlank(int x, int y, int w, int h)
    {
        int[] tc = getTruncatedCoords(x, y, w, h);
        if (tc == null) return;
        for (int j = tc[1]; j < tc[1] + tc[3]; j++)
        {
            for (int i = tc[0]; i < tc[0] + tc[2]; i++)
            {
                _cell[j][i].setBlank();
            }
        }
    }

    /**
     * Specifies the merging region for the table. All the coordinates should be provided relative to x and y
     * (left-top) table's coordinates.
     *
     * @param x x-coordinate (left-top; relative to the table's x-coordinate)
     * @param y y-coordinate (left-top; relative to the table's y-coordinate)
     * @param w selection width
     * @param h selection height
     */
    public void mergeCells(int x, int y, int w, int h)
    {
        if ((w < 2) && (h < 2)) return;
        int[] tc = getTruncatedCoords(x, y, w, h);
        if (tc == null) return;
        _sheet.addMergedRegion(new CellRangeAddress(_y + tc[1], _y + tc[1] + tc[3] - 1,
                _x + tc[0], _x + tc[0] + tc[2] - 1));
    }

    /**
     * Truncates the coordinates. Returns null if there is no common part with the table.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     * @return truncated coordinates (x, y, w, h); returns null if there is no common part with the table
     */
    private int[] getTruncatedCoords(int x, int y, int w, int h)
    {
        if (x >= _w) return null;
        if (y >= _h) return null;
        if (x < 0)
        {
            w += x;
            x = 0;
        }
        if (y < 0)
        {
            h += y;
            y = 0;
        }
        if (w < 1) return null;
        if (h < 1) return null;

        if (x + w >= _w) w = _w - x;
        if (y + h >= _h) h = _h - y;

        if (w < 1) return null;
        if (h < 1) return null;
        return new int[]{x, y, w, h};
    }


}
