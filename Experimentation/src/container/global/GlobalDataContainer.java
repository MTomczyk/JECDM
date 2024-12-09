package container.global;


/**
 * Default extension of {@link AbstractGlobalDataContainer}.
 *
 * @author MTomczyk
 */
public class GlobalDataContainer extends AbstractGlobalDataContainer
{
    /**
     * Params container.
     */
    public static class Params extends AbstractGlobalDataContainer.Params
    {

    }

    /**
     * Parameterized constructor
     *
     * @param p params container
     */
    public GlobalDataContainer(Params p)
    {
        super(p);
    }

}
