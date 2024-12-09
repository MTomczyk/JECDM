package popupmenu.item;


import java.awt.*;

/**
 * Dummy extension of {@link AbstractItem} (for debugging).
 *
 * @author MTomczyk
 */

public class DummyItem extends AbstractItem
{
    /**
     * Parameterized constructor.
     * @param label label to be displayed
     */
    public DummyItem(String label)
    {
        super(label);
        setFont( new Font("SansSerif", Font.BOLD, 30));
    }
}
