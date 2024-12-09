package swing;

import javax.swing.*;
import java.awt.*;

/**
 * Simple loading frame (without any logic).
 *
 * @author MTomczyk
 */

public class LoadingFrame extends JFrame
{
    /**
     * Parameterized constructor (customizes the frame)
     *
     * @param relativeSize frame size relative to screen size (in range of [0, 1]).
     */
    public LoadingFrame(double relativeSize)
    {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        int actualWidth =  2 * (int) (screenWidth * relativeSize + 0.5f);
        int actualHeight = (int) (screenHeight * relativeSize + 0.5f);

        setResizable(false);
        setTitle("Loading");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
        setPreferredSize(new Dimension(actualWidth, actualHeight));
        setSize(getPreferredSize());
        setLocationRelativeTo(null);

        try
        {
            ComponentUtils.initIcon(this);
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        int fontSize = (int) (actualHeight / 4 + 0.5f);
        Font font = new Font("Helvetica", Font.PLAIN, fontSize);
        JLabel label = new JLabel("    Loading...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(font);
        add(label);

        pack();
    }
}
