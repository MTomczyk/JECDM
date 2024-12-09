package component.colorbar;

import color.gradient.Gradient;
import component.axis.ticksupdater.ITicksDataGetter;

/**
 * Default implementation of {@link AbstractColorbar} object.
 *
 * @author MTomczyk
 */
public class Colorbar extends AbstractColorbar
{
    /**
     * Params container.
     */
    public static class Params extends AbstractColorbar.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param gradient used
         * @param title    colorbar (its axis) title (can be null -> not used)
         * @param tickLabelsGetter supportive object that is used to automatically update tick labels (can be null -> not used)
         */
        public Params(Gradient gradient, String title, ITicksDataGetter tickLabelsGetter)
        {
            super("Colorbar", null, gradient, true, title, tickLabelsGetter);
        }

        /**
         * Parameterized constructor.
         *
         * @param gradient used
         * @param useAxis  axis used along the colorbar; can be null -> not used
         * @param title    colorbar (its axis) title (can be null -> not used)
         * @param tickLabelsGetter supportive object that is used to automatically update tick labels (can be null -> not used)
         */
        public Params(Gradient gradient, boolean useAxis, String title, ITicksDataGetter tickLabelsGetter)
        {
            super("Colorbar", null, gradient, useAxis, title, tickLabelsGetter);
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param gradient gradient used
     * @param title    colorbar (its axis) title (can be null -> not used)
     * @param tickLabelsGetter supportive object that is used to automatically update tick labels (can be null -> not used)
     */
    public Colorbar(Gradient gradient, String title, ITicksDataGetter tickLabelsGetter)
    {
        this(gradient, true, title, tickLabelsGetter);
    }

    /**
     * Parameterized constructor.
     *
     * @param gradient gradient used
     * @param useAxis  axis used along the colorbar; can be false -> not used (surpasses other relevant fields)
     * @param title    colorbar (its axis) title (can be null -> not used)
     * @param tickLabelsGetter supportive object that is used to automatically update tick labels (can be null -> not used)
     */
    public Colorbar(Gradient gradient, boolean useAxis, String title, ITicksDataGetter tickLabelsGetter)
    {
        this(new Params(gradient, useAxis, title, tickLabelsGetter));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public Colorbar(Params p)
    {
        super(p);
    }


}