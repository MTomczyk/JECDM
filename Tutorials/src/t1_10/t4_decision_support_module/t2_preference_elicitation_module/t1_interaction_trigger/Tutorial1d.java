package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t1_interaction_trigger;

import dmcontext.DMContext;
import exeption.TriggerException;
import interaction.trigger.InteractionTrigger;
import interaction.trigger.Result;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.IterationInterval;
import print.PrintUtils;

import java.util.LinkedList;

/**
 * This tutorial focuses on the {@link interaction.trigger.InteractionTrigger} class.
 *
 * @author MTomczyk
 */
public class Tutorial1d
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create the rules:
        LinkedList<IRule> rules = new LinkedList<>();
        rules.add(new IterationInterval(1, 4, 3));
        rules.add(new IterationInterval(5));

        // Create the interaction trigger:
        InteractionTrigger interactionTrigger = new InteractionTrigger(rules);

        // Work for 10 iterations:
        for (int iteration = 0; iteration < 10; iteration++)
        {
            System.out.println("Current iteration = " + iteration);

            // Create the context
            DMContext.Params pDMC = new DMContext.Params();
            pDMC._currentIteration = iteration;
            DMContext context = new DMContext(pDMC, null, null);

            try
            {
                // Question the interaction trigger:
                Result shouldInteract = interactionTrigger.shouldTriggerTheInteractions(context);

                // Should interact?
                if (shouldInteract._shouldInteract)
                {
                    // Get the string representation (indent = 4) and print:
                    String [] log = shouldInteract.getStringRepresentation(4);
                    PrintUtils.printLines(log);

                    interactionTrigger.notifyPreferenceElicitationBegins(context);
                    interactionTrigger.notifyPreferenceElicitationEnds(context);
                }

            } catch (TriggerException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
