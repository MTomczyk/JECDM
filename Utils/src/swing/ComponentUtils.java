package swing;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Contains some common functionalities.
 *
 * @author MTomczyk
 */
public class ComponentUtils
{
    /**
     * Sets frame icon to framework's (seeks through the resources folder).
     *
     * @param frame frame object whose icon will be set
     * @throws Exception the exception can be thrown  (e.g., when the icon file cannot be located)
     */
    public static void initIcon(JFrame frame) throws Exception
    {
        // dependency to the root JECDM project required
        URL url = ComponentUtils.class.getClassLoader().getResource("icon_128_128.png");
        if (url == null) throw new Exception(("JECDM icon could not be found! (icon_128_128.png)"));
        ImageIcon img = new ImageIcon(url);
        frame.setIconImage(img.getImage());

        try
        {
            // set icon for the taskbar
            final Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(img.getImage());
        } catch (final UnsupportedOperationException e)
        {
            //throw new Exception("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e)
        {
            throw new Exception("There was a security exception for: 'taskbar.setIconImage'");
        }
    }
}
