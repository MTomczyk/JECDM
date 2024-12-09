package t1_10.t4_decision_support_module.t1_concepts.t3_history;

import alternative.Alternative;
import exeption.HistoryException;
import history.History;
import history.PreferenceInformationWrapper;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial focuses on the {@link preference.IPreferenceInformation} interface ({@link preference.indirect.PairwiseComparison}).
 *
 * @author MTomczyk
 */
public class Tutorial3
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create preference information (dummy alternatives, i.e., not evaluated):
        ArrayList<IPreferenceInformation> preferenceInformation = new ArrayList<>(3);
        preferenceInformation.add(PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1)));
        preferenceInformation.add(PairwiseComparison.getIndifference(new Alternative("A3", 1), new Alternative("A4", 1)));
        preferenceInformation.add(PairwiseComparison.getIncomparable(new Alternative("A5", 1), new Alternative("A6", 1)));

        // Create the history object:
        History history = new History("History (tutorial)");
        // Create criteria:

        try
        {
            // Go through iterations 0, 1,...,5.
            // Let's add the preference information in iteration = 0, 1, 2.
            // We will remove them in the remaining steps.
            for (int i = 0; i < 6; i++)
            {
                System.out.println("Iteration = " + i);

                // Register the preference information (creates the wrappers; see the return type):
                if (i < 3) history.registerPreferenceInformation(preferenceInformation.get(i), i, true);
                // ... or remove it (the list is small, so getting elements by the index does not hurt).
                else
                {
                    // Get copy (note that the deep-copy is not guaranteed at the lower levels (e.g., implementations
                    // of the IPreferenceInformation interface)
                    LinkedList<PreferenceInformationWrapper> wrappers = history.getPreferenceInformationCopy();
                    history.remove(wrappers.getFirst());
                }

                System.out.println(history.getFullStringRepresentation());
                System.out.println();
            }

        } catch (HistoryException e)
        {
            throw new RuntimeException(e);
        }
    }
}
