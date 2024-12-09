package scheme.referencevalue;

import java.awt.*;

/**
 * Implementation of the {@link IReferenceValueGetter} interface.
 * Returns the reference value defined as min { screen.width , screen.height }.
 *
 * @author MTomczyk
 */

public class MinScreenDimension implements IReferenceValueGetter
{

    /**
     * Returns the reference value defined as min { screen.width , screen.height }.
     *
     * @return reference value
     */
    @Override
    public float getReferenceValue()
    {
        return Math.min(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
    }
}
