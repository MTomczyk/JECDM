package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t1_interaction_trigger;

import dmcontext.DMContext;
import exeption.TriggerException;
import interaction.trigger.Postpone;
import interaction.trigger.Reason;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.IterationInterval;

import java.time.LocalDateTime;

/**
 * This tutorial focuses on the {@link interaction.trigger.rules.IRule} interface ({@link IterationInterval}).
 *
 * @author MTomczyk
 */
public class Tutorial1a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create the rule (starting iteration = 2; interval = 2; limit = 2):
        IRule rule = new IterationInterval(2, 2, 3);

        // Work for 10 iterations:
        for (int iteration = 0; iteration < 10; iteration++)
        {
            // Typically, all the fields must be filled (but this simple tutorial does not check the validity):
            DMContext.Params pDMC = new DMContext.Params();
            pDMC._currentIteration = iteration;
            DMContext context = new DMContext(pDMC, null, null);

            try
            {
                boolean shouldInteract = rule.shouldInteract(context);
                System.out.println("Iteration = " + iteration + ": should interact = " + shouldInteract);

                if (shouldInteract)
                {
                    // added to ensure correct processing
                    rule.notifyPreferenceElicitationBegins(context);

                    // Simulate postponing interactions in iteration 2 and 4:
                    if ((iteration == 2) || (iteration == 4))
                    {
                        System.out.println("Postponing interactions in iteration = " + iteration + " \n (current interaction is cancelled)");
                        rule.postpone(new Postpone(Reason.COULD_NOT_DETERMINE_REFERENCE_SETS, iteration, LocalDateTime.now()));
                        // added to ensure correct processing
                        rule.notifyPreferenceElicitationFailed(context);
                    }
                    // added to ensure correct processing
                    rule.notifyPreferenceElicitationEnds(context);
                }

            } catch (TriggerException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
