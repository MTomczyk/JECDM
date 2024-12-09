package component.drawingarea;

import com.jogamp.opengl.GLAutoDrawable;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for updating the final render.
 * This implementation calls the display method of a GLAutoDrawable.
 *
 * @author MTomczyk
 */
public class DrawingArea3DRenderUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * GL auto drawable.
     */
    protected final GLAutoDrawable _gl;


    /**
     * Parameterized constructor.
     *
     * @param gl         auto drawable
     */
    public DrawingArea3DRenderUpdater(GLAutoDrawable gl)
    {
        _gl = gl;
    }


    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        _gl.display();
        notifyTermination();
        return null;
    }

    /**
     * Finalizes data set update.
     */
    @Override
    protected void done()
    {
        if (isCancelled()) return;
        try
        {
            get();
        } catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }
}
