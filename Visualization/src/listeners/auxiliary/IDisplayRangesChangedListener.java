package listeners.auxiliary;

import component.axis.swing.AbstractAxis;
import drmanager.DisplayRangesManager;

/**
 * Interfaces for objects listening for changes in display ranges.
 * E.g., axes {@link AbstractAxis} that may wish to update their tick labels.
 *
 * @author MTomczyk
 */
public interface IDisplayRangesChangedListener
{
    /**
     * Method notifying on changes in display ranges.
     *
     * @param drm    display ranges manager
     * @param report report on the last update of display ranges
     */
    void displayRangesChanged(DisplayRangesManager drm, DisplayRangesManager.Report report);
}
