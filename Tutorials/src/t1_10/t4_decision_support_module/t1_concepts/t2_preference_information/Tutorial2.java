package t1_10.t4_decision_support_module.t1_concepts.t2_preference_information;

import alternative.Alternative;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;

import java.util.LinkedList;

/**
 * This tutorial focuses on the {@link preference.IPreferenceInformation} interface ({@link preference.indirect.PairwiseComparison}).
 *
 * @author MTomczyk
 */
public class Tutorial2
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Store pieces of preferences in the list:
        LinkedList<IPreferenceInformation> preferenceInformation = new LinkedList<>();

        // Create the pairwise comparison result -- preference:
        preferenceInformation.add(PairwiseComparison.getPreference(new Alternative("A1", new double[]{1.0d, 0.0d}),
                new Alternative("A2", new double[]{0.0d, 1.0d})));
        // Create the pairwise comparison result -- indifference:
        preferenceInformation.add(PairwiseComparison.getIndifference(new Alternative("A3", new double[]{0.6d, 0.4d}),
                new Alternative("A4", new double[]{0.4d, 0.6d})));
        // Create the pairwise comparison result -- incomparable:
        preferenceInformation.add(PairwiseComparison.getIncomparable(new Alternative("A1", new double[]{0.7d, 0.5d}),
                new Alternative("A1", new double[]{0.5d, 0.7d})));

        // Display the preference information:
        for (IPreferenceInformation pi : preferenceInformation)
        {
            System.out.println(pi.toString());
            System.out.println("Form = " + pi.getForm() + "; is indirect = " + pi.isIndirect());
        }
    }
}
