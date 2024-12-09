package emo.interactive.utils.dmcontext;

import os.IOSChangeListener;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;

/**
 * Default extension of {@link AbstractDMCParamsConstructor}. Important note: it implements {@link IOSChangeListener}.
 * It can be registered in {@link os.ObjectiveSpaceManager} so that the 'os changed' flag will be switched to true each
 * time the objective space is updated.
 *
 * @author MTomczyk
 */
public class DefaultConstructor extends AbstractDMCParamsConstructor implements IDMCParamsConstructor, IOSChangeListener
{
    /**
     * Parameterized constructor.
     */
    public DefaultConstructor()
    {
        super();
    }

    /**
     * Parameterized constructor.
     *
     * @param normalizationBuilder object used to construct normalization functions; if null, the standard min max builder
     *                             will be instantiated {@link StandardLinearBuilder}
     */
    public DefaultConstructor(INormalizationBuilder normalizationBuilder)
    {
       super(normalizationBuilder);
    }

}
