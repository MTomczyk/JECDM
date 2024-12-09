package gl;

import com.jogamp.opengl.GL2;

/**
 * Interface for classes representing VBO-rendering-based plot elements.
 *
 * @author MTomczyk
 */
public interface IVBOComponent
{
    /**
     * Can be called to draw an object.
     *
     * @param gl open gl rendering context
     */
    void draw(GL2 gl);

    /**
     * Can be called to instantiate VBO buffers (but the data has not yet been transferred to GPU).
     */
    void createBuffers();

    /**
     * Can be called to instantiate VBO buffers on the GPU.
     *
     * @param gl open gl rendering context
     */
    void executeInitialDataTransfer(GL2 gl);

    /**
     * Can be called to update data in the VBO that has already been instantiated and sent to GPU  (but the updated is not yet transferred to GPU).
     */
    void updateBuffers();

    /**
     * The method returns true if some of the wrapped buffers need to be sent to the GPU for the first time.
     *
     * @return true, if the update is required, false = otherwise.
     */
    boolean isInitialUpdateRequested();

    /**
     * The method returns true if some of the wrapped buffers were updated and the data needs to be sent to GPU.
     *
     * @return true, if the update is required, false = otherwise.
     */
    boolean isUpdateRequested();

    /**
     * Can be called to update the VBO that is already instantiated in GPU.
     *
     * @param gl open gl rendering context
     */
    void executeUpdate(GL2 gl);

    /**
     * Can be called to remove GL-related data.
     *
     * @param gl open gl rendering context
     */
    void dispose(GL2 gl);

    /**
     * Can be called to check whether the buffer objects have not been created yet.
     * @return true, buffer objects have not been created yet.
     */
    boolean areVBOsNull();
}
