package system.modules.updater;

import dmcontext.DMContext;
import exeption.DecisionMakerSystemException;
import exeption.ModuleException;
import system.dm.DM;
import system.dm.DecisionMakerSystem;
import system.modules.AbstractModule;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class represents a module associated with updating the decision maker's (decision makers') preference models.
 *
 * @author MTomczyk
 */
public class ModelsUpdaterModule extends AbstractModule
{
    /**
     * Params container
     */
    public static class Params extends AbstractModule.Params
    {
        /**
         * If true, the results of all called model updates are stored in a list.
         */
        public boolean _collectModelsUpdatesResults = false;

        /**
         * Default constructor.
         */
        public Params()
        {
            super("Models updater module");
        }
    }

    /**
     * If true, the results of all called model updates are stored in a list.
     */
    private final boolean _collectModelsUpdatesResults;

    /**
     * Reports on the models updates.
     */
    private HashMap<DM, LinkedList<system.dm.Report>> _modelsUpdatesReports;

    /**
     * Parameterized constructor
     *
     * @param p params container
     */
    public ModelsUpdaterModule(Params p)
    {
        super(p);
        _collectModelsUpdatesResults = p._collectModelsUpdatesResults;
    }


    /**
     * The main method for executing the default models update process. It calls {@link DecisionMakerSystem#updateModels()}
     * method for each decision maker, and constructs suitable reports.
     *
     * @param dmContext current decision-making context
     * @return report on the process execution
     * @throws ModuleException the exception can be thrown and propagated higher
     */
    public Report executeProcess(DMContext dmContext) throws ModuleException
    {
        Report report = new Report(dmContext, _DMs);
        long startTime = System.nanoTime();

        registerDecisionMakingContext(dmContext);

        HashMap<DM, system.dm.Report> reports = new HashMap<>(_DMs.length);

        for (DecisionMakerSystem dms : _DMSs)
        {
            try
            {
                system.dm.Report r = dms.updateModels();
                reports.put(dms.getDM(), r);
            } catch (DecisionMakerSystemException e)
            {
                unregisterDecisionMakingContext();
                throw new ModuleException("Error occurred when updating the model of a decision maker = " + dms.getDM().getName()
                        + " " + e.getDetailedReasonMessage(), this.getClass(), e);
            }
        }

        report._modelsUpdatesReports = reports;
        if (_collectModelsUpdatesResults)
        {
            if (_modelsUpdatesReports == null)
            {
                _modelsUpdatesReports = new HashMap<>(_DMs.length);
                for (DM dm : _DMs) _modelsUpdatesReports.put(dm, new LinkedList<>());
            }
            for (DM dm : _DMs) _modelsUpdatesReports.get(dm).add(reports.get(dm));
        }

        unregisterDecisionMakingContext();

        report._processingTime = (System.nanoTime() - startTime) / 1000000;
        return report;
    }

    /**
     * Getter for the model update results.
     *
     * @return model update results
     */
    public HashMap<DM, LinkedList<system.dm.Report>> getModelUpdateReports()
    {
        return _modelsUpdatesReports;
    }

    /**
     * Auxiliary method for performing data validation.
     *
     * @throws ModuleException the exception will be thrown if the validation fails
     */
    @Override
    public void validate() throws ModuleException
    {
        super.validate();
    }
}
