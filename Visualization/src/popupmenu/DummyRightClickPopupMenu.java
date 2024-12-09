package popupmenu;


import popupmenu.item.DummyItem;

/**
 * Dummy extension of {@link RightClickPopupMenu}.
 * It instantiates with three dummy items (for debugging).
 *
 * @author MTomczyk
 */


public class DummyRightClickPopupMenu extends RightClickPopupMenu
{
    /**
     * Default constructor.
     */
    public DummyRightClickPopupMenu()
    {
        addItem(new DummyItem("Dummy 1"));
        addItem(new DummyItem("Dummy 2"));
        addItem(new DummyItem("Dummy 3"));
    }
}
