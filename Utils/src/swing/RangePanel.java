package swing;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.util.Hashtable;

/**
 * Simple panel allowing to alter value ranges.
 *
 * @author MTomczyk
 */

public class RangePanel extends JPanel
{
    /**
     * Key for the left (min range).
     */
    private static final int MIN_TYPE = 0;

    /**
     * Key for the right (max range).
     */
    private static final int MAX_TYPE = 1;

    /**
     * Custom user interface for JSlider.
     */
    public static class CustomUI extends BasicSliderUI
    {
        /**
         * Parameterized constructor.
         *
         * @param slider inheriting slider object
         */
        public CustomUI(JSlider slider)
        {
            super(slider);
        }

        /**
         * Custom paint thumb.
         *
         * @param g the graphics
         */
        @Override
        public void paintThumb(Graphics g)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            Rectangle t = thumbRect;
            g2d.setColor(Color.DARK_GRAY);
            g2d.fill(t);
        }
    }

    /**
     * Panel allowing to modify a single range value.
     */
    public static class ValueRangePanel extends JPanel
    {
        /**
         * Current value associated with the panel (min/max).
         */
        private int _currentValue;

        /**
         * Slider object.
         */
        private final JSlider _slider;

        /**
         * Spinner object.
         */
        private final JSpinner _spinner;

        /**
         * Parent panel object.
         */
        private final RangePanel _RP;

        /**
         * Parameterized constructor.
         *
         * @param title        title to be displayed (title border is used).
         * @param minValue     min global value
         * @param maxValue     max global value
         * @param currentValue current value
         * @param noTicks      no. ticks displayed on the slider
         * @param type         type (MIN_TYPE/MAX_TYPE)
         * @param RP           parent panel
         */
        public ValueRangePanel(String title, int minValue, int maxValue, int currentValue, int noTicks, int type, RangePanel RP)
        {
            _RP = RP;
            _currentValue = currentValue;

            Border border = BorderFactory.createTitledBorder(title);
            setBorder(border);

            GridBagLayout layout = new GridBagLayout();
            setLayout(layout);

            _slider = new JSlider(minValue, maxValue);

            _slider.setValue(currentValue);
            _slider.setPaintLabels(true);
            _slider.setPaintTicks(true);
            Hashtable<Integer, javax.swing.JComponent> labelTable = new Hashtable<>();
            double step = (maxValue - minValue) / ((double) noTicks - 1);

            for (int i = 0; i < noTicks; i++)
            {
                int val = (int) (minValue + i * step + 0.5d);
                labelTable.put(val, new JLabel(Integer.toString(val)));
            }
            _slider.setLabelTable(labelTable);
            _slider.setUI(new CustomUI(_slider));
            _slider.addChangeListener(e -> _RP.requestUpdate(type, _slider.getValue()));

            SpinnerModel sm = new SpinnerNumberModel(currentValue, minValue, maxValue, 1);
            _spinner = new JSpinner(sm);
            _spinner.setValue(currentValue);
            _spinner.addChangeListener(e -> {
                _RP.requestUpdate(type, (Integer) _spinner.getValue());
                _spinner.requestFocus(false);
            });
            _spinner.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weighty = 1;
            gbc.weightx = 0.66;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(0, 10, 0, 10);
            add(_slider, gbc);

            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weighty = 0.5;
            gbc.weightx = 0.34;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(0, 10, 0, 10);
            add(_spinner, gbc);
        }

        /**
         * Getter for the current value associated with the panel.
         *
         * @return current value associated with the panel
         */
        public int getCurrentValue()
        {
            return _currentValue;
        }

        /**
         * Setter for the current value associated with the panel.
         *
         * @param newValue new value associated with the panel
         */
        public void setCurrentValue(int newValue)
        {
            _currentValue = newValue;
            _slider.setValue(_currentValue);
            _spinner.setValue(_currentValue);
        }
    }

    /**
     * Min global value.
     */
    private final int _minValue;

    /**
     * Max global value.
     */
    private final int _maxValue;

    /**
     * Left panel linked to the min value.
     */
    protected ValueRangePanel _minPanel;

    /**
     * Right panel linked to the max value.
     */
    protected ValueRangePanel _maxPanel;

    /**
     * Parameterized constructor.
     *
     * @param minValue min value
     * @param maxValue max value
     */
    public RangePanel(int minValue, int maxValue)
    {
        setLayout(new GridLayout(1, 2));

        _minValue = minValue;
        _maxValue = maxValue;

        _minPanel = new ValueRangePanel("Min value", minValue, maxValue, minValue, 5, MIN_TYPE, this);
        _maxPanel = new ValueRangePanel("Max value", minValue, maxValue, maxValue, 5, MAX_TYPE, this);

        add(_minPanel, 0);
        add(_maxPanel, 1);

    }

    /**
     * method to be called by the sliders/spinners listeners to update (synchronize) all their settings
     *
     * @param type           caller type (MIN_TYPE/MAX_TYPE).
     * @param requestedValue newly set value
     */
    protected void requestUpdate(int type, int requestedValue)
    {
        int cMin = _minPanel.getCurrentValue();
        int cMax = _maxPanel.getCurrentValue();

        if (type == MIN_TYPE) cMin = requestedValue;
        else cMax = requestedValue;

        if (type == MIN_TYPE)
        {
            if (cMin >= cMax)
            {
                cMax = cMin + 1;
                if (cMax > _maxValue)
                {
                    cMax = _maxValue;
                    cMin = cMax - 1;
                }
            }
        }
        else
        {
            if (cMin >= cMax)
            {
                cMin = cMax - 1;
                if (cMin < _minValue)
                {
                    cMin = _minValue;
                    cMax = cMin + 1;
                }
            }
        }

        _minPanel.setCurrentValue(cMin);
        _maxPanel.setCurrentValue(cMax);
    }

    /**
     * Color setter. Can be used to customize the panel.
     *
     * @param backgroundColor new background color
     * @param fontColor new font color
     */
    public void setColors(Color backgroundColor, Color fontColor)
    {
        setBackground(backgroundColor);
        _minPanel.setBackground(backgroundColor);
        _minPanel._slider.setBackground(backgroundColor);

        _maxPanel.setBackground(backgroundColor);
        _maxPanel._slider.setBackground(backgroundColor);
    }



}
