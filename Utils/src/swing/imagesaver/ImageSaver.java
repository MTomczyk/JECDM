package swing.imagesaver;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Basic implementation of an image saver panel (built on a file chooser).
 *
 * @author MTomczyk
 */

public class ImageSaver extends JFrame implements ActionListener
{
    /**
     * File chooser.
     */
    protected JFileChooser _fileChooser;

    /**
     * Transparency check box.
     */
    protected JCheckBox _transparency = null;

    /**
     * Slider representing quality level (compression).
     */
    protected JSlider _quality = null;

    /**
     * Object responsible for obtaining data from file chooser and saving the image.
     */
    protected IFileSaveDelegate _saveDelegate;

    /**
     * Parameterized constructor.
     *
     * @param saveDelegate object responsible for obtaining data from file chooser and saving the image
     * @param exitOnClose if true, the EXIT_ON_CLOSE is set as the default close operation (should be false if the frame was created from already existing EDT).
     */
    public ImageSaver(IFileSaveDelegate saveDelegate, boolean exitOnClose)
    {
        _saveDelegate = saveDelegate;

        _fileChooser = new JFileChooser();
        _fileChooser.setApproveButtonText("Save as");
        _fileChooser.setAcceptAllFileFilterUsed(false);
        _fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
        _fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("bmp", "bmp"));
        _fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));
        _fileChooser.addActionListener(this);

        JPanel accessory = getAccessory();
        if (accessory != null) _fileChooser.setAccessory(accessory);
        add(_fileChooser);

        setTitle("Save image as...");
        if (exitOnClose) setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION))
        {
            dispose();
        }
        else if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
        {
            if (_saveDelegate != null)
            {
                String extension = _fileChooser.getFileFilter().getDescription();
                boolean transparency = _transparency.isSelected();
                float quality = (float) _quality.getValue() / 100.0f;
                _saveDelegate.saveImage(_fileChooser.getCurrentDirectory(), _fileChooser.getSelectedFile(),
                        extension, transparency, quality);
            }

            dispose();
        }
    }


    /**
     * Getter for the accessory (to be overwritten).
     *
     * @return accessory JPanel
     */
    protected JPanel getAccessory()
    {
        JPanel accessory = new JPanel();

        GridBagLayout gbl = new GridBagLayout();
        accessory.setLayout(gbl);

        // aux options
        {
            JPanel auxOptions = getAuxOptions();
            GridBagConstraints C = new GridBagConstraints();
            C.gridx = 0;
            C.gridy = 0;
            C.weightx = 1;
            C.fill = GridBagConstraints.HORIZONTAL;
            C.anchor = GridBagConstraints.NORTH;
            accessory.add(auxOptions, C);
        }

        // quality options
        {
            JPanel qualityOptions = getQualityOptions();
            GridBagConstraints C = new GridBagConstraints();
            C.gridx = 0;
            C.gridy = 1;
            C.weightx = 1;
            C.weighty = 1;
            C.fill = GridBagConstraints.HORIZONTAL;
            C.anchor = GridBagConstraints.NORTH;
            accessory.add(qualityOptions, C);
        }

        return accessory;
    }

    /**
     * Getter for the auxiliary options panel (accessory member).
     *
     * @return auxiliary options panel
     */
    protected JPanel getAuxOptions()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Options"));
        panel.setLayout(new BorderLayout());
        _transparency = new JCheckBox("Transparency");
        panel.add(_transparency, BorderLayout.WEST);
        return panel;
    }

    /**
     * Getter for the quality options panel (accessory member).
     *
     * @return quality options panel
     */
    protected JPanel getQualityOptions()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Quality: 100"));
        _quality = new JSlider(0, 100);
        _quality.setValue(100);
        _quality.setMajorTickSpacing(20);
        _quality.setPaintTicks(true);
        _quality.setPaintLabels(true);
        _quality.addChangeListener(e -> panel.setBorder(BorderFactory.createTitledBorder("Quality: " + _quality.getValue())));
        panel.add(_quality);
        return panel;
    }

}
