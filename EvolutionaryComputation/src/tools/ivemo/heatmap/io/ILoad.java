package tools.ivemo.heatmap.io;


/**
 * Interface for data loaders for displaying heatmaps.
 *
 * @author MTomczyk
 */

public interface ILoad
{
    /**
     * Can be called to save thedata file.
     * @param path folder path (relative to the jar; excludes filename)
     * @throws Exception exception
     */
    void load(String path) throws Exception;
}
