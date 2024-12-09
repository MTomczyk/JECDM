package phase;

import ea.EA;
import exception.PhaseException;
import population.SpecimensContainer;

/**
 * Default "Evaluate" phase.
 * The default (implicit) assumptions are as follows:
 * - The method delegates the evaluation of specimens stored in {@link SpecimensContainer#getPopulation()} and/or
 * {@link SpecimensContainer#getOffspring()} (depends on the {@link SpecimensContainer#isPopulationRequiringEvaluation()}
 * and {@link SpecimensContainer#isOffspringRequiringEvaluation()} flags) to {@link IEvaluate}.
 *
 * @author MTomczyk
 */


public class Evaluate extends AbstractEvaluatePhase implements IPhase
{
    /**
     *  Object responsible for evaluating solutions.
     */
    protected final IEvaluate _evaluate;

    /**
     * Parameterized constructor.
     *
     * @param evaluate object responsible for evaluating solutions
     */
    public Evaluate(IEvaluate evaluate)
    {
        this("Evaluate", evaluate);
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
     * {@link SpecimensContainer#getOffspring()} (depends on the {@link SpecimensContainer#isPopulationRequiringEvaluation()}
     * and {@link SpecimensContainer#isOffspringRequiringEvaluation()} flags) to {@link IEvaluate}.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        if (ea.getSpecimensContainer().isPopulationRequiringEvaluation())
            _evaluate.evaluateSpecimens(ea.getSpecimensContainer().getPopulation());
        if (ea.getSpecimensContainer().isOffspringRequiringEvaluation())
            _evaluate.evaluateSpecimens(ea.getSpecimensContainer().getOffspring());
    }
}
