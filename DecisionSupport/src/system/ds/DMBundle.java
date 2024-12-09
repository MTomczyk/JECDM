package system.ds;

import model.internals.AbstractInternalModel;

/**
 * Bundle object used for wrapping all essential data required to establish a single decision maker system
 * (see {@link system.dm.DecisionMakerSystem}).
 *
 * @author MTomczyk
 */
public class DMBundle
{
    /**
     * Decision maker's name.
     */
    public final String _name;

    /**
     * Model bundles.
     */
    public ModelBundle<? extends AbstractInternalModel>[] _modelBundles;

    /**
     * Parameterized constructor (does not initialize model bundle(s)).
     *
     * @param name decision maker's name
     */
    public DMBundle(String name)
    {
        this(name, (ModelBundle<?>[]) null);
    }

    /**
     * Parameterized constructor.
     *
     * @param name        decision maker's name
     * @param modelBundle single model bundle
     */
    public DMBundle(String name, ModelBundle<?> modelBundle)
    {
        this(name, new ModelBundle[]{modelBundle});
    }

    /**
     * Parameterized constructor.
     *
     * @param name        decision maker's name
     * @param modelBundle model bundles
     */
    public DMBundle(String name, ModelBundle<?>[] modelBundle)
    {
        _name = name;
        _modelBundles = modelBundle;
    }
}
