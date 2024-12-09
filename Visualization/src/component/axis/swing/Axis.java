package component.axis.swing;

/**
 * General implementation of {@link AbstractAxis}.
 *
 * @author MTomczyk
 */
public class Axis extends AbstractAxis
{
    /**
     * Parameterized constructor for the X-axis.
     *
     * @param title  axis title (can be null -> not used)
     * @param fields a supportive container for field references (scheme attributes) for the axis
     * @param type   axis type, equivalent to a dimension no (rendering space), i.e., x = 0, y = 1, z = 2, etc.
     */
    public Axis(String title, Fields fields, Type type)
    {
        super(new Params("Axis (" + title + ")", null));
        _title = title;
        _fields = fields;
        _type = type;
    }
}
