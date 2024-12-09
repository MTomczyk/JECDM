package popupmenu.item;

import container.GlobalContainer;
import container.PlotContainer;
import popupmenu.AbstractRightClickPopupMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Abstract class representing menu items of {@link popupmenu.AbstractRightClickPopupMenu}.
 *
 * @author MTomczyk
 */


public class AbstractItem extends JMenuItem implements ActionListener, MouseListener
{
    /**
     * Global container.
     */
    protected GlobalContainer _GC;

    /**
     * Plot container.
     */
    protected PlotContainer _PC;

    /**
     * Parent menu.
     */
    protected AbstractRightClickPopupMenu _menu = null;

    /**
     * Parameterized constructor.
     *
     * @param label label to be displayed
     */
    public AbstractItem(String label)
    {
        super(label);
        addMouseListener(this);
        addActionListener(this);
    }

    /**
     * Releases action listeners.
     */
    public void unregisterListeners()
    {
        removeMouseListener(this);
        removeActionListener(this);
    }


    /**
     * Setter for containers.
     *
     * @param GC global container
     * @param PC plot container
     */
    public void setContainers(GlobalContainer GC, PlotContainer PC)
    {
        _GC = GC;
        _PC = PC;
    }

    /**
     * Setter for the parent menu.
     *
     * @param menu parent menu
     */
    public void setMenu(AbstractRightClickPopupMenu menu)
    {
        _menu = menu;
    }

    /**
     * Invoked when an action occurs. Displays an image saver (frame for file selection).
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (_menu != null) _menu.setVisible(false);
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
        setArmed(true);
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        setArmed(false);
    }
}
