package tools.ivemo.heatmap.io;

import color.gradient.Gradient;
import dataset.painter.style.enums.Bucket;
import drmanager.DisplayRangesManager;
import org.junit.jupiter.api.Test;
import scheme.AbstractScheme;
import scheme.BlackScheme;
import space.Range;
import tools.ivemo.heatmap.io.load.LoadInitFile;
import tools.ivemo.heatmap.io.params.FrameParams;
import tools.ivemo.heatmap.io.params.PlotParams;
import tools.ivemo.heatmap.io.save.SaveInitFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for the {@link SaveInitFile} and {@link LoadInitFile} classes.
 */
class SaveAndLoadInit2DFileTest
{

    /**
     * Test #1.
     */
    @Test
    void save1()
    {
        // PREPARE
        float frameSize = 0.5f;
        String frameTitle = "FT";
        boolean printFPS = true;
        String fName = "nsgaii2D";
        int lg = 0;
        int rg = 100;
        int dimensions = 2;
        AbstractScheme scheme = new BlackScheme(); // can be null
        Gradient gradient = Gradient.getViridisGradient();
        Bucket style = Bucket.SQUARE_2D; // can be null
        String xt = "f1";
        String yt = "f2";
        int xdiv = 100;
        int ydiv = 100;
        int lx = 0;
        int rx = 2;
        int ly = 1;
        int ry = 4;
        String plotTitle = "test plot TITLE"; // can be null
        String heatmapTitle = "heatmap title";

        FrameParams FP = new FrameParams();
        FP._frameSize = frameSize;
        FP._frameTitle = frameTitle;
        FP._printFPS = printFPS;

        PlotParams PP = new PlotParams();
        PP._fileName = fName;
        PP._title = plotTitle;
        PP._dimensions = dimensions;
        PP._scheme = scheme;
        PP._gradient = gradient;
        PP._heatmapDisplayRange = new Range(lg, rg);
        PP._bucketStyle = style;
        PP._xAxisTitle = xt;
        PP._yAxisTitle = yt;
        PP._xAxisDivisions = xdiv;
        PP._yAxisDivisions = ydiv;
        PP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(lx, rx), new Range(ly, ry));
        PP._heatmapTitle = heatmapTitle;

        ISave save = new SaveInitFile(FP, PP);

        try
        {
            save.save("./");
        } catch (Exception e)
        {
            System.out.println("Error during saving: " + e.getMessage());
        }


        LoadInitFile loader = new LoadInitFile();
        try
        {
            loader.load("./");
        } catch (Exception e)
        {
            System.out.println("Error during loading: " + e.getMessage());
        }

        assertEquals(frameSize, loader._FP._frameSize, 0.0000001d);
        assertEquals(frameTitle, loader._FP._frameTitle);
        assertEquals(printFPS, loader._FP._printFPS);

        assertEquals(1, loader._PP.length);
        assertEquals(fName, loader._PP[0]._fileName);
        assertEquals(lg, loader._PP[0]._heatmapDisplayRange.getLeft(), 0.0000001d);
        assertEquals(rg, loader._PP[0]._heatmapDisplayRange.getRight(), 0.0000001d);
        assertEquals(heatmapTitle, loader._PP[0]._heatmapTitle);
        assertEquals(dimensions, loader._PP[0]._dimensions, 0.0000001d);
        assertEquals(scheme.getName().toLowerCase(), loader._PP[0]._scheme.getName().toLowerCase());
        assertEquals(PP._scheme.getName().toLowerCase(), loader._PP[0]._scheme.getName().toLowerCase());
        assertEquals(gradient.getName().toLowerCase(), loader._PP[0]._gradient.getName().toLowerCase());
        assertEquals(Bucket.SQUARE_2D, loader._PP[0]._bucketStyle);
        assertEquals(xt, loader._PP[0]._xAxisTitle);
        assertEquals(yt, loader._PP[0]._yAxisTitle);
        assertEquals(xdiv, loader._PP[0]._xAxisDivisions, 0.0000001d);
        assertEquals(ydiv, loader._PP[0]._yAxisDivisions, 0.0000001d);
        assertEquals(lx, loader._PP[0]._pDisplayRangesManager._DR[0].getR().getLeft(), 0.0000001d);
        assertEquals(rx, loader._PP[0]._pDisplayRangesManager._DR[0].getR().getRight(), 0.0000001d);
        assertEquals(ly, loader._PP[0]._pDisplayRangesManager._DR[1].getR().getLeft(), 0.0000001d);
        assertEquals(ry, loader._PP[0]._pDisplayRangesManager._DR[1].getR().getRight(), 0.0000001d);
        assertEquals(plotTitle, loader._PP[0]._title);

        File initFile = new File("./init.xml");
        if (!initFile.delete())
            System.out.println("Failed to delete init.xml file");
    }
}