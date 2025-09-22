package phase;

import ea.AbstractPhasesEA;
import exception.PhaseException;
import population.Specimen;
import population.SpecimensContainer;

import java.util.ArrayList;

/**
 * Default "Evaluate" phase. The default (implicit) assumptions are as follows:
 * - The method delegates the evaluation of specimens stored in {@link SpecimensContainer#getPopulation()} and/or
 * {@link SpecimensContainer#getOffspring()} (depends on the
 * {@link SpecimensContainer#isPopulationRequiringEvaluation()}
 * and {@link SpecimensContainer#isOffspringRequiringEvaluation()} flags) to {@link IEvaluate}.
 *
 * @author MTomczyk
 */


public class Evaluate extends AbstractEvaluatePhase implements IPhase
{
    /**
     * Object responsible for evaluating solutions.
     */
    protected final IEvaluate _evaluate;

    /**
     * Parameterized constructor (sets the name to "EVALUATE").
     *
     * @param evaluate object responsible for evaluating solutions
     */
    public Evaluate(IEvaluate evaluate)
    {
        this("EVALUATE", evaluate);
    }

    /**
     * Parameterized constructor.
     *
     * @param name     name of the phase
     * @param evaluate object responsible for evaluating solutions
     */
    public Evaluate(String name, IEvaluate evaluate)
    {
        super(name);
        _evaluate = evaluate;
    }

    /**
     * Phase main action.
     * The default (implicit) assumptions are as follows:
     * - The method delegates the evaluation of specimens stored in {@link SpecimensContainer#getPopulation()} and/or
     * {@link SpecimensContainer#getOffspring()} (depends on the
     * {@link SpecimensContainer#isPopulationRequiringEvaluation()}
     * and {@link SpecimensContainer#isOffspringRequiringEvaluation()} flags) to {@link IEvaluate}.
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown
     */
    @Override
    public void action(AbstractPhasesEA ea, PhaseReport report) throws PhaseException
    {
        if (ea.getSpecimensContainer().isPopulationRequiringEvaluation())
        {
            ArrayList<Specimen> population = ea.getSpecimensContainer().getPopulation();
            _evaluate.evaluateSpecimens(population);
            ea.getSpecimensContainer().incrementPerformedFunctionEvaluations(population.size());
        }
        if (ea.getSpecimensContainer().isOffspringRequiringEvaluation())
        {
            ArrayList<Specimen> offspring = ea.getSpecimensContainer().getOffspring();
            _evaluate.evaluateSpecimens(ea.getSpecimensContainer().getOffspring());
            ea.getSpecimensContainer().incrementPerformedFunctionEvaluations(offspring.size());
        }
    }
}
