package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t1_interaction_trigger;

import dmcontext.DMContext;
import exeption.TriggerException;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.TimeInterval;

import java.time.LocalDateTime;

/**
 * This tutorial focuses on the {@link IRule} interface ({@link TimeInterval}).
 *
 * @author MTomczyk
 */
public class Tutorial1b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create the rule (starts after 1 second, perform every 2 seconds, max 3 interactions):
        IRule rule = new TimeInterval(500, 1500, 3);

        long startTime = System.currentTimeMillis();
        int iteration = 0;

        // The system start timestamp must be provided via the context:
        LocalDateTime systemStartTimestamp = LocalDateTime.now();

        // Work for 6 seconds:
        while ((System.currentTimeMillis() - startTime) < 6000)
        {
            // Create the context:
            DMContext.Params pDMC = new DMContext.Params();
            pDMC._currentIteration = iteration++;
            DMContext context = new DMContext(pDMC, null, systemStartTimestamp);

            try
            {
                // Should interact?
                boolean shouldInteract = rule.shouldInteract(context);

                if (shouldInteract)
                {
                    // If true: print the delta time and call proper notifications:
                    System.out.println("Interaction triggered in " + (System.currentTimeMillis() - startTime) + " ms");
                    rule.notifyPreferenceElicitationBegins(context);
                    rule.notifyPreferenceElicitationEnds(context);
                }

            } catch (TriggerException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
