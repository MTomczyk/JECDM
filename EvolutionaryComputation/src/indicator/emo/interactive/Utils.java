package indicator.emo.interactive;

import ea.IEA;
import system.dm.DecisionMakerSystem;
import system.ds.DecisionSupportSystem;
import system.model.ModelSystem;

/**
 * Provides various utility functions for performance indicators associated with methods employing a decision support system.
 *
 * @author MTomczyk
 */
class Utils
{
    /**
     * Tries to derive and return a decision maker system from an input EA. Method returns null if not possible.
     *
     * @param ea   evolutionary algorithm
     * @param dmID the DM's identifier (0, 1,...) in {@link system.ds.DecisionSupportSystem}
     * @return decision maker system (null, if not possible to retrieve).
     */
    public static DecisionMakerSystem getDMS(IEA ea, int dmID)
    {
        if (ea.getDecisionSupportSystem() == null) return null;
        DecisionSupportSystem DSS = ea.getDecisionSupportSystem();
        if ((DSS.getDecisionMakersSystems() == null) ||
                (DSS.getDecisionMakersSystems().length - 1 < dmID)) return null;
        return DSS.getDecisionMakersSystems()[dmID];
    }

    /**
     * Tries to derive and return a model system from an input EA. Method returns null if not possible.
     *
     * @param ea      evolutionary algorithm
     * @param dmID    the DM's identifier (0, 1,...) in {@link system.ds.DecisionSupportSystem}
     * @param modelID the model system identifier (0, 1,...) in {@link DecisionMakerSystem}
     * @return decision maker system (null, if not possible to retrieve).
     */
    public static ModelSystem<?> getMS(IEA ea, int dmID, int modelID)
    {
        DecisionMakerSystem DMS = getDMS(ea, dmID);
        if (DMS == null) return null;
        if ((DMS.getModelSystems() == null) || (DMS.getModelSystems().length - 1 < modelID)) return null;
        return DMS.getModelSystems()[modelID];
    }
}
