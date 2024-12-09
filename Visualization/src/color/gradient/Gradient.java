package color.gradient;

import color.Color;
import color.ColorAssignment;
import color.gradient.gradients.*;

import java.util.ArrayList;

/**
 * Class representing a gradient.
 *
 * @author MTomczyk
 */

@SuppressWarnings("DuplicatedCode")
public class Gradient
{
    /**
     * Array of value -> color assignments.
     * These assignments should be sorted according to Color.Assignment.value (increasing order).
     */
    protected ArrayList<ColorAssignment> _points;

    /**
     * Gradient name.
     */
    private final String _name;

    /**
     * If true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}).
     * Produces accurate colors but also constructs new color objects.
     * If false (default), the closest matching color from the maintained assignments is returned.
     * False option can be useful when designing a discretized gradient.
     */
    private boolean _interpolateColors = false;

    /**
     * Default constructor.
     */
    public Gradient()
    {
        this("Custom");
    }

    /**
     * Parameterized constructor.
     *
     * @param name gradient name
     */
    public Gradient(String name)
    {
        _name = name;
    }

    /**
     * Parameterized constructor. Instantiates color assignments with a single color (value = 0.0).
     *
     * @param c input color
     */
    public Gradient(Color c)
    {
        this(c, "Custom");
    }

    /**
     * Parameterized constructor. Instantiates color assignments with a single color (value = 0.0).
     *
     * @param c    input color
     * @param name gradient name
     */
    public Gradient(Color c, String name)
    {
        this(c, name, false);
    }

    /**
     * Parameterized constructor. Instantiates color assignments with a single color (value = 0.0).
     *
     * @param c                 input color
     * @param name              gradient name
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     */
    public Gradient(Color c, String name, boolean interpolateColors)
    {
        _points = new ArrayList<>(1);
        _points.add(new ColorAssignment(0.0f, c));
        _name = name;
        _interpolateColors = interpolateColors;
    }


    /**
     * Parameterized constructor. Instantiates point array as provided.
     *
     * @param colors            n-element array of colors represented as [r, g, b, a] (0-1 floats)
     * @param name              gradient name
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     */
    public Gradient(float[][] colors, String name, boolean interpolateColors)
    {
        _points = new ArrayList<>(colors.length);
        for (int i = 0; i < colors.length; i++)
        {
            Color c = new Color(colors[i][0], colors[i][1], colors[i][2], colors[i][3]);
            float val = (float) i / (float) (colors.length - 1);
            _points.add(new ColorAssignment(val, c));
        }
        _name = name;
        _interpolateColors = interpolateColors;
    }

    /**
     * Parameterized constructor. Instantiates point array as provided. Note that the values should be in [0, 1] bounds (that is, the gradient is normalized).
     * Note that: points does not need to be sorted according to value. They will be sorted by the method anyway.
     *
     * @param points provided array
     */
    public Gradient(ArrayList<ColorAssignment> points)
    {
        this(points, "Custom");
    }

    /**
     * Parameterized constructor. Instantiates point array as provided. Note that the values should be in [0, 1] bounds (that is, the gradient is normalized).
     * Note that: points does not need to be sorted according to value. They will be sorted by the method anyway.
     *
     * @param points provided array
     * @param name   gradient name
     */
    public Gradient(ArrayList<ColorAssignment> points, String name)
    {
        this(points, name, false);
    }

    /**
     * Parameterized constructor. Instantiates point array as provided. Note that the values should be in [0, 1] bounds (that is, the gradient is normalized).
     * Note that: points does not need to be sorted according to value. They will be sorted by the method anyway.
     *
     * @param points            provided array
     * @param name              gradient name
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     */
    public Gradient(ArrayList<ColorAssignment> points, String name, boolean interpolateColors)
    {
        for (ColorAssignment ca : points)
        {
            assert ca._value >= 0.0d;
            assert ca._value <= 1.0d;
        }
        _points = points;
        _name = name;
        _interpolateColors = interpolateColors;
        sort();
    }


    /**
     * Parameterized constructor. Instantiates point array with a size as provided.
     *
     * @param size provided size for the array
     */
    public Gradient(int size)
    {
        this(size, "Custom");
    }

    /**
     * Parameterized constructor. Instantiates point array with a size as provided.
     *
     * @param size provided size for the array
     * @param name gradient name
     */
    public Gradient(int size, String name)
    {
        this(size, name, false);
    }

    /**
     * Parameterized constructor. Instantiates point array with a size as provided.
     *
     * @param size              provided size for the array
     * @param name              gradient name
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     */
    public Gradient(int size, String name, boolean interpolateColors)
    {
        _points = new ArrayList<>(size);
        _name = name;
        _interpolateColors = interpolateColors;
    }


    /**
     * Constructs object's deep copy.
     *
     * @return deep copy
     */
    public Gradient getClone()
    {
        if (_points == null) return new Gradient(_name);
        ArrayList<ColorAssignment> na = new ArrayList<>(_points.size());
        for (ColorAssignment ca : _points) na.add(new ColorAssignment(ca._value, ca._color.getClone()));
        return new Gradient(na, _name, _interpolateColors);
    }

    /**
     * Checks if the object represents just a single color.
     *
     * @return true = there is no gradient; false = otherwise
     */
    public boolean isMonoColor()
    {
        if (_points.size() == 1) return true;
        for (int i = 1; i < _points.size(); i++)
            if (!_points.get(i - 1)._color.equals(_points.get(i)._color)) return false;
        return true;
    }

    /**
     * Auxiliary method that thresholds gradient input value.
     * Helps to make corrections in the case of asynchronous computing.
     *
     * @param expected input value that is supposed to be in [0, 1] range
     * @return proper value
     */
    private float getCorrectedGradientValue(float expected)
    {
        if (Float.compare(expected, 0.0f) < 0) return 0.0f;
        if (Float.compare(expected, 1.0f) > 0) return 1.0f;
        return expected;
    }

    /**
     * Returns a gradient color for a normalized value in range [0,1].
     * If {@link Gradient#_interpolateColors} is true, color is interpolated and a new instance is returned.
     * If false, the closest matching color from maintained assignments is returned.
     *
     * @param value input value; if it is not in the proper bounds, it is thresholded
     * @return interpolated color
     */
    public Color getColor(float value)
    {
        // premature termination
        if (_points.size() == 1) return _points.get(0)._color;
        if (Double.compare(value, 0.0d) == 0) return _points.get(0)._color;
        else if (Double.compare(value, 1.0d) == 0) return _points.get(_points.size() - 1)._color;

        // check thresholding

        value = getCorrectedGradientValue(value);
        // check linear interpolation first
        int idx = (int) (value * (_points.size() - 1));

        if (Float.compare(value, _points.get(idx)._value) < 0)
        {
            while ((idx > 0) && (Float.compare(value, _points.get(idx)._value) < 0)) idx--;
        }
        else if ((idx < _points.size() - 1) && (Float.compare(value, _points.get(idx + 1)._value) >= 0))
        {
            while ((idx < _points.size() - 1) && (value >= _points.get(idx + 1)._value)) idx++;
        }

        if (idx == _points.size() - 1) return _points.get(idx)._color;

        if (_interpolateColors)
        {
            float proportion = (value - _points.get(idx)._value) / (_points.get(idx + 1)._value - _points.get(idx)._value);
            return getColorBetween(_points.get(idx)._color, _points.get(idx + 1)._color, proportion);
        }
        else
        {
            if ((value - _points.get(idx)._value) < (_points.get(idx + 1)._value - value))
                return _points.get(idx)._color;
            else return _points.get(idx + 1)._color;
        }
    }

    /**
     * Calculates linear interpolation between two colors based on the provided weight (weight = 0 -> A color; weight = 1 -> B color).
     *
     * @param A      first color (for weight 0)
     * @param B      second color (for weight 1)
     * @param weight weight for calculating the linear interpolation
     * @return interpolated color
     */
    private Color getColorBetween(Color A, Color B, float weight)
    {
        float r = A.getRed() + weight * (B.getRed() - A.getRed());
        float g = A.getGreen() + weight * (B.getGreen() - A.getGreen());
        float b = A.getBlue() + weight * (B.getBlue() - A.getBlue());
        float a = A.getAlpha() + weight * (B.getAlpha() - A.getAlpha());
        return new Color((int) (r + 0.5f), (int) (g + 0.5f), (int) (b + 0.5f), (int) (a + 0.5f));
    }

    /**
     * Add a new color to the assignment array.
     *
     * @param ca color assignment
     */
    public void add(ColorAssignment ca)
    {
        _points.add(ca);
    }

    /**
     * Sorts colors according to value (increasing order) (bubble sort).
     */
    private void sort()
    {
        for (int i = 1; i < _points.size(); i++) sort(i);
    }

    /**
     * Sorts colors according to value (increasing order).
     *
     * @param position array index for the backwards sorting process (bubble sort).
     */
    private void sort(int position)
    {
        for (int i = position; i > 0; i--)
        {
            if (Float.compare(_points.get(i)._value, _points.get(i - 1)._value) > 0) continue;
            ColorAssignment tmp = _points.get(i);
            _points.set(i, _points.get(i - 1));
            _points.set(i - 1, tmp);
        }
    }


    /**
     * Getter for the gradient name.
     *
     * @return gradient name
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Returns a gradient instance based on provided two colors (extreme colors).
     *
     * @param r1                red component linked to the color associated with the minimal value of 0.0
     * @param g1                green component linked to the color associated with the minimal value of 0.0
     * @param b1                blue component linked to the color associated with the minimal value of 0.0
     * @param r2                red component linked to the color associated with the maximal value of 1.0
     * @param g2                green component linked to the color associated with the maximal value of 1.0
     * @param b2                blue component linked to the color associated with the maximal value of 1.0
     * @param name              gradient name
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return two-color-based gradient
     */
    public static Gradient getTwoColorBasedGradient(float r1, float g1, float b1,
                                                    float r2, float g2, float b2,
                                                    String name,
                                                    int noColors,
                                                    boolean interpolateColors)
    {
        assert noColors > 1;

        ArrayList<ColorAssignment> cas = new ArrayList<>(noColors);

        float dR = (r2 - r1) / (noColors - 1);
        float dG = (g2 - g1) / (noColors - 1);
        float dB = (b2 - b1) / (noColors - 1);

        for (int i = 0; i < noColors - 1; i++)
        {
            float val = (float) i / (noColors - 1);
            float r = r1 + dR * i;
            float g = g1 + dG * i;
            float b = b1 + dB * i;
            ColorAssignment ca = new ColorAssignment(val, new Color(r, g, b, 1.0f));
            cas.add(ca);
        }

        {
            float alpha = 1.0f;
            ColorAssignment ca = new ColorAssignment(1.0f, new Color(r2, g2, b2, alpha));
            cas.add(ca);
        }

        return new Gradient(cas, name, interpolateColors);
    }

    /**
     * Returns a gradient instance based on provided three colors (extreme colors and a middle one).
     *
     * @param r1                red component linked to the color associated with the minimal value of 0.0
     * @param g1                green component linked to the color associated with the minimal value of 0.0
     * @param b1                blue component linked to the color associated with the minimal value of 0.0
     * @param r2                red component linked to the color associated with the middle value of 0.5
     * @param g2                green component linked to the color associated with the middle value of 0.5
     * @param b2                blue component linked to the color associated with the middle value of 0.5
     * @param r3                red component linked to the color associated with the maximal value of 1.0
     * @param g3                green component linked to the color associated with the maximal value of 1.0
     * @param b3                blue component linked to the color associated with the maximal value of 1.0
     * @param name              gradient name
     * @param noHalfColors      no. colors physically generated and stored per one side of the gradient (excluding the middle point; hence the total no. points is halfNoColors * 2 + 1) (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 1
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return three-color-based gradient instance.
     */
    public static Gradient getThreeColorBasedGradient(float r1, float g1, float b1,
                                                      float r2, float g2, float b2,
                                                      float r3, float g3, float b3,
                                                      String name,
                                                      int noHalfColors,
                                                      boolean interpolateColors)
    {
        assert noHalfColors > 0;

        ArrayList<ColorAssignment> cas = new ArrayList<>(noHalfColors * 2 + 1);

        float dR1 = (r2 - r1) / (noHalfColors);
        float dG1 = (g2 - g1) / (noHalfColors);
        float dB1 = (b2 - b1) / (noHalfColors);

        float dR2 = (r3 - r2) / (noHalfColors);
        float dG2 = (g3 - g2) / (noHalfColors);
        float dB2 = (b3 - b2) / (noHalfColors);

        for (int i = 0; i < noHalfColors; i++)
        {
            float val = (float) i / (noHalfColors) * 0.5f;
            float r = r1 + dR1 * i;
            float g = g1 + dG1 * i;
            float b = b1 + dB1 * i;
            ColorAssignment ca = new ColorAssignment(val, new Color(r, g, b, 1.0f));
            cas.add(ca);
        }

        for (int i = 0; i < noHalfColors; i++)
        {
            float val = 0.5f + 0.5f * (float) i / (noHalfColors);
            float r = r2 + dR2 * i;
            float g = g2 + dG2 * i;
            float b = b2 + dB2 * i;
            ColorAssignment ca = new ColorAssignment(val, new Color(r, g, b, 1.0f));
            cas.add(ca);
        }

        {
            ColorAssignment ca = new ColorAssignment(1.0f, new Color(r3, g3, b3, 1.0f));
            cas.add(ca);
        }

        return new Gradient(cas, name, interpolateColors);
    }


    // =================================================================================================================

    /**
     * Returns black-white gradient instance (101 points/no interpolation).
     *
     * @return black-white gradient instance
     */
    public static Gradient getBlackWhiteGradient()
    {
        return getBlackWhiteGradient(101, false);
    }


    /**
     * Returns black-white gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return black-white gradient instance
     */
    public static Gradient getBlackWhiteGradient(int noColors, boolean interpolateColors)
    {
        return Gradient.getTwoColorBasedGradient(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, "BW",
                noColors, interpolateColors);
    }

    // =================================================================================================================


    /**
     * Returns white-black gradient instance (101 points/no interpolation).
     *
     * @return white-black gradient instance
     */
    public static Gradient getWhiteBlackGradient()
    {
        return getWhiteBlackGradient(101, false);
    }

    /**
     * Returns white-black gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return white-black gradient instance
     */
    public static Gradient getWhiteBlackGradient(int noColors, boolean interpolateColors)
    {
        return Gradient.getTwoColorBasedGradient(1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, "WB",
                noColors, interpolateColors);
    }

    // =================================================================================================================

    /**
     * Returns blue-red gradient instance (101 points/no interpolation).
     *
     * @return blue-red gradient instance
     */
    public static Gradient getBlueRedGradient()
    {
        return getBlueRedGradient(101, false);
    }


    /**
     * Returns blue-red gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return blue-red gradient instance
     */
    public static Gradient getBlueRedGradient(int noColors, boolean interpolateColors)
    {
        return Gradient.getTwoColorBasedGradient(0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, "BR",
                noColors, interpolateColors);
    }


    // =================================================================================================================

    /**
     * Returns red-blue gradient instance (101 points/no interpolation).
     *
     * @return red-blue gradient instance
     */
    public static Gradient getRedBlueGradient()
    {
        return getRedBlueGradient(101, false);
    }


    /**
     * Returns red-blue gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return red-blue gradient instance
     */
    public static Gradient getRedBlueGradient(int noColors, boolean interpolateColors)
    {
        return Gradient.getTwoColorBasedGradient(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, "RB",
                noColors, interpolateColors);
    }

    // =================================================================================================================

    /**
     * Returns red-green gradient instance (101 points/no interpolation).
     *
     * @return red-green gradient instance
     */
    public static Gradient getRedGreenGradient()
    {
        return getRedGreenGradient(101, false);
    }

    /**
     * Returns red-green gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return red-green gradient instance
     */
    public static Gradient getRedGreenGradient(int noColors, boolean interpolateColors)
    {
        return Gradient.getTwoColorBasedGradient(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, "RG",
                noColors, interpolateColors);
    }


    // =================================================================================================================

    /**
     * Returns green-red gradient instance (101 points/no interpolation).
     *
     * @return green-red gradient instance
     */
    public static Gradient getGreenRedGradient()
    {
        return getGreenRedGradient(101, false);
    }

    /**
     * Returns green-red gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return green-red gradient instance
     */
    public static Gradient getGreenRedGradient(int noColors, boolean interpolateColors)
    {
        return Gradient.getTwoColorBasedGradient(0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, "GR",
                noColors, interpolateColors);
    }


    // =================================================================================================================

    /**
     * Returns red-yellow-green gradient instance (101 points/no interpolation).
     *
     * @return red-yellow-green gradient instance
     */
    public static Gradient getRedYellowGreenGradient()
    {
        return getRedYellowGreenGradient(101, false);
    }

    /**
     * Returns red-yellow-green gradient instance.
     *
     * @param noHalfColors      no. colors physically generated and stored per one side of the gradient (excluding the middle point; hence the total no. points is halfNoColors * 2 + 1) (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 1
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return red-yellow-green gradient instance
     */
    public static Gradient getRedYellowGreenGradient(int noHalfColors, boolean interpolateColors)
    {
        return Gradient.getThreeColorBasedGradient(1.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, "RYG",
                noHalfColors, interpolateColors);
    }


    // =================================================================================================================

    /**
     * Returns gradient instance constructed via providing stored gradient tables (see, e.g., {@link Viridis}).
     *
     * @param colors            color matrix; rows contains RGBA values
     * @param name              gradient name
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2; colors are obtained from the provided colors matrix
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return viridis gradient instance.
     */
    public static Gradient getGradientFromColorMatrix(float[][] colors, String name, int noColors, boolean interpolateColors)
    {
        Gradient V = new Gradient(colors, name, interpolateColors);
        ArrayList<ColorAssignment> cas = new ArrayList<>();
        for (int i = 0; i < noColors; i++)
        {
            float v = (float) i / (noColors - 1);
            cas.add(new ColorAssignment(v, V.getColor(v)));
        }
        return new Gradient(cas, name, interpolateColors);
    }

    // =================================================================================================================


    /**
     * Returns viridis gradient instance with 256 data points representing colors (no interpolation).
     *
     * @return viridis gradient instance
     */
    public static Gradient getViridisGradient()
    {
        return getViridisGradient(false);
    }

    /**
     * Returns viridis gradient instance with 256 data points representing colors (no interpolation).
     * The colors are in inverse order.
     *
     * @return viridis gradient instance
     */
    public static Gradient getViridisGradientInverse()
    {
        return getViridisGradient(false, true);
    }

    /**
     * Returns viridis gradient instance with 256 data points representing colors (no interpolation).
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return viridis gradient instance
     */
    public static Gradient getViridisGradient(boolean interpolateColors)
    {
        return getViridisGradient(interpolateColors, false);
    }

    /**
     * Returns viridis gradient instance with 256 data points representing colors.
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return viridis gradient instance
     */
    public static Gradient getViridisGradient(boolean interpolateColors, boolean inverse)
    {
        if (inverse) return new Gradient(Viridis.colorsInverse, "ViridisInverse", interpolateColors);
        return new Gradient(Viridis.colors, "Viridis", interpolateColors);
    }


    /**
     * Returns viridis gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return viridis gradient instance
     */
    public static Gradient getViridisGradient(int noColors, boolean interpolateColors)
    {
        return getViridisGradient(noColors, interpolateColors, false);
    }

    /**
     * Returns viridis gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return viridis gradient instance
     */
    public static Gradient getViridisGradient(int noColors, boolean interpolateColors, boolean inverse)
    {
        return createGradient(getViridisGradient(true), noColors, interpolateColors, inverse);
    }

    /**
     * Auxiliary method for creating parameterized gradient based on the reference one.
     *
     * @param reference         reference gradient
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return gradient instance
     */
    private static Gradient createGradient(Gradient reference, int noColors, boolean interpolateColors, boolean inverse)
    {
        ArrayList<ColorAssignment> cas = new ArrayList<>();
        for (int i = 0; i < noColors; i++)
        {
            float v = (float) i / (noColors - 1);
            if (inverse) cas.add(new ColorAssignment(v, reference.getColor(1.0f - v)));
            else cas.add(new ColorAssignment(v, reference.getColor(v)));
        }
        String suffix = "";
        if (inverse) suffix = "Inverse";
        return new Gradient(cas, reference._name + suffix, interpolateColors);
    }


    // =================================================================================================================

    /**
     * Returns inferno gradient instance with 256 data points representing colors (no interpolation).
     *
     * @return inferno gradient instance
     */
    public static Gradient getInfernoGradient()
    {
        return getInfernoGradient(false);
    }

    /**
     * Returns inferno gradient instance with 256 data points representing colors (no interpolation).
     * The colors are in inverse order.
     *
     * @return inferno gradient instance
     */
    public static Gradient getInfernoGradientInverse()
    {
        return getInfernoGradient(false, true);
    }

    /**
     * Returns inferno gradient instance with 256 data points representing colors.
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return inferno gradient instance
     */
    public static Gradient getInfernoGradient(boolean interpolateColors)
    {
        return getInfernoGradient(interpolateColors, false);
    }

    /**
     * Returns inferno gradient instance with 256 data points representing colors.
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return inferno gradient instance
     */
    public static Gradient getInfernoGradient(boolean interpolateColors, boolean inverse)
    {
        if (inverse) return new Gradient(Inferno.colorsInverse, "InfernoInverse", interpolateColors);
        return new Gradient(Inferno.colors, "Inferno", interpolateColors);
    }


    /**
     * Returns inferno gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return inferno gradient instance
     */
    public static Gradient getInfernoGradient(int noColors, boolean interpolateColors)
    {
        return getInfernoGradient(noColors, interpolateColors, false);
    }

    /**
     * Returns inferno gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return inferno gradient instance
     */
    public static Gradient getInfernoGradient(int noColors, boolean interpolateColors, boolean inverse)
    {
        return createGradient(getInfernoGradient(true), noColors, interpolateColors, inverse);
    }


    // =================================================================================================================

    /**
     * Returns magma gradient instance with 256 data points representing colors (no interpolation).
     *
     * @return magma gradient instance
     */
    public static Gradient getMagmaGradient()
    {
        return getMagmaGradient(false);
    }

    /**
     * Returns magma gradient instance with 256 data points representing colors (no interpolation).
     * The colors are in inverse order.
     *
     * @return magma gradient instance
     */
    public static Gradient getMagmaGradientInverse()
    {
        return getMagmaGradient(false, true);
    }


    /**
     * Returns magma gradient instance with 256 data points representing colors.
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return magma gradient instance
     */
    public static Gradient getMagmaGradient(boolean interpolateColors)
    {
        return getMagmaGradient(interpolateColors, false);
    }

    /**
     * Returns magma gradient instance with 256 data points representing colors.
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return magma gradient instance
     */
    public static Gradient getMagmaGradient(boolean interpolateColors, boolean inverse)
    {
        if (inverse) return new Gradient(Magma.colorsInverse, "MagmaInverse", interpolateColors);
        return new Gradient(Magma.colors, "Magma", interpolateColors);
    }

    /**
     * Returns magma gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return magma gradient instance
     */
    public static Gradient getMagmaGradient(int noColors, boolean interpolateColors)
    {
        return getMagmaGradient(noColors, interpolateColors, false);
    }

    /**
     * Returns magma gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return magma gradient instance
     */
    public static Gradient getMagmaGradient(int noColors, boolean interpolateColors, boolean inverse)
    {
        return createGradient(getMagmaGradient(true), noColors, interpolateColors, inverse);
    }

    // =================================================================================================================


    /**
     * Returns plasma gradient instance with 256 data points representing colors (no interpolation).
     *
     * @return plasma gradient instance
     */
    public static Gradient getPlasmaGradient()
    {
        return getPlasmaGradient(false);
    }

    /**
     * Returns plasma gradient instance with 256 data points representing colors (no interpolation).
     * The colors are in inverse order.
     *
     * @return plasma gradient instance
     */
    public static Gradient getPlasmaGradientInverse()
    {
        return getPlasmaGradient(false, true);
    }

    /**
     * Returns plasma gradient instance with 256 data points representing colors.
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return plasma gradient instance
     */
    public static Gradient getPlasmaGradient(boolean interpolateColors)
    {
        return getPlasmaGradient(interpolateColors, false);
    }

    /**
     * Returns plasma gradient instance with 256 data points representing colors.
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return plasma gradient instance
     */
    public static Gradient getPlasmaGradient(boolean interpolateColors, boolean inverse)
    {
        if (inverse) return new Gradient(Plasma.colorsInverse, "PlasmaInverse", interpolateColors);
        return new Gradient(Plasma.colors, "Plasma", interpolateColors);
    }

    /**
     * Returns plasma gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return plasma gradient instance
     */
    public static Gradient getPlasmaGradient(int noColors, boolean interpolateColors)
    {
        return getPlasmaGradient(noColors, interpolateColors, false);
    }

    /**
     * Returns plasma gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return plasma gradient instance
     */
    public static Gradient getPlasmaGradient(int noColors, boolean interpolateColors, boolean inverse)
    {
        return createGradient(getPlasmaGradient(true), noColors, interpolateColors, inverse);
    }


    // =================================================================================================================


    /**
     * Returns cividis gradient instance with 256 data points representing colors (no interpolation).
     *
     * @return cividis gradient instance
     */
    public static Gradient getCividisGradient()
    {
        return getCividisGradient(false);
    }

    /**
     * Returns cividis gradient instance with 256 data points representing colors (no interpolation).
     * The colors are in inverse order.
     *
     * @return cividis gradient instance
     */
    public static Gradient getCividisGradientInverse()
    {
        return getCividisGradient(false, true);
    }

    /**
     * Returns cividis gradient instance with 256 data points representing colors.
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return cividis gradient instance
     */
    public static Gradient getCividisGradient(boolean interpolateColors)
    {
        return getCividisGradient(interpolateColors, false);
    }

    /**
     * Returns cividis gradient instance with 256 data points representing colors.
     *
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return cividis gradient instance
     */
    public static Gradient getCividisGradient(boolean interpolateColors, boolean inverse)
    {
        if (inverse) return new Gradient(Cividis.colorsInverse, "CividisInverse", interpolateColors);
        return new Gradient(Cividis.colors, "Cividis", interpolateColors);
    }

    /**
     * Returns cividis gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @return cividis gradient instance
     */
    public static Gradient getCividisGradient(int noColors, boolean interpolateColors)
    {
        return getCividisGradient(noColors, interpolateColors, false);
    }

    /**
     * Returns cividis gradient instance.
     *
     * @param noColors          no. colors physically generated and stored (can be set to some reasonably large value to generate color instance and store them so that there will be no need for interpolation), should be at least 2
     * @param interpolateColors if true, colors can be interpolated linearly (between color assignments {@link ColorAssignment}); produces accurate colors but also constructs new color objects; ff false (default), the closest matching color from the maintained assignments is returned; false option can be useful when designing a discretized gradient
     * @param inverse           inverses the gradient
     * @return cividis gradient instance
     */
    public static Gradient getCividisGradient(int noColors, boolean interpolateColors, boolean inverse)
    {
        return createGradient(getCividisGradient(true), noColors, interpolateColors, inverse);
    }

}
