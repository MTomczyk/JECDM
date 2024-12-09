package swing;


import javax.swing.*;
import java.awt.*;

/**
 * Several tests for the {@link RangePanel} class.
 *
 * @author MTomczyk
 */
class RangePanelTest
{
    /**
     * Visualization test.
     * @param args ignored
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Test frame");
        int h = Toolkit.getDefaultToolkit().getScreenSize().height;
        int w = Toolkit.getDefaultToolkit().getScreenSize().width;

        frame.setPreferredSize(new Dimension(w / 2, h / 5));
        frame.setSize(frame.getPreferredSize());
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout());

        RangePanel RP = new RangePanel(0, 100);
        frame.add(RP);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}