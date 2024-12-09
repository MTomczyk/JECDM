package emo.interactive.utils.dmcontext;

import dmcontext.DMContext;
import ea.EA;

/**
 * Interface for classes responsible for constructing decision-making context objects (params; the main input for
 * {@link system.ds.DecisionSupportSystem#executeProcess(DMContext.Params)}.
 *
 * @author MTomczyk
 */
public interface IDMCParamsConstructor
{
    /**
     * The main method for retrieving decision-making context params.
     *
     * @param ea evolutionary algorithm linked with DSS
     * @return decision-making context params
     */
    DMContext.Params getDMCParams(EA ea);
}
