package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t1_interaction_trigger;

import dmcontext.DMContext;
import exeption.TriggerException;
import interaction.trigger.rules.Flag;
import interaction.trigger.rules.IRule;

/**
 * This tutorial focuses on the {@link IRule} interface ({@link Flag}).
 *
 * @author MTomczyk
 */
public class Tutorial1c
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create the rule (set the flag to false):
        Flag rule = new Flag(false);

        // Work for 10 iterations:
        for (int iteration = 0; iteration < 10; iteration++)
        {
            // Create the context
            DMContext.Params pDMC = new DMContext.Params();
            pDMC._currentIteration = iteration;
            DMContext context = new DMContext(pDMC, null, null);

            // Simulate changing the flag (true for every 3 iterations):
            rule.setFlag(iteration % 3 == 0);

            try
            {
                boolean shouldInteract = rule.shouldInteract(context);
                if (shouldInteract)
                {
                    rule.notifyPreferenceElicitationBegins(context);
                    rule.notifyPreferenceElicitationEnds(context);
                }

                System.out.println("Iteration = " + iteration + ": should interact = " + shouldInteract);
            } catch (TriggerException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
