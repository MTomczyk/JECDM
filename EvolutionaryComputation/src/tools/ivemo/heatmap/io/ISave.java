package tools.ivemo.heatmap.io;

import tools.ivemo.heatmap.AbstractHeatmapProcessor;

/**
 * Interface for data saves for {@link AbstractHeatmapProcessor} (heatmap data processors).
 *
 * @author MTomczyk
 */

public interface ISave
{
    /**
     * Can be called to save the data file.
     * @param path folder path (relative to the jar; excludes filename)
     * @throws Exception exception
     */
    void save(String path) throws Exception;
}
