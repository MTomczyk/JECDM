package history;

import alternative.Alternative;
import exeption.HistoryException;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link PreferenceInformationWrapper}.
 *
 * @author MTomczyk
 */
class PreferenceInformationWrapperTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        PairwiseComparison PC = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        String msg = null;
        try
        {
            PreferenceInformationWrapper w = new PreferenceInformationWrapper(PC, 1, 2, null);
            assertEquals("[Pairwise comparison: A1 PREFERENCE A2 ; id = 1 ; iteration = 2 ; date time = not provided]", w.toString());
            assertEquals(1, w._id);
            assertEquals(2, w._iteration);
            assertNull(w._dateTime);
        } catch (HistoryException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        PairwiseComparison PC1 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        PairwiseComparison PC2 = PairwiseComparison.getPreference(new Alternative("A3", 1), new Alternative("A4", 1));
        PairwiseComparison PC3 = PairwiseComparison.getPreference(new Alternative("A5", 1), new Alternative("A6", 1));

        String msg = null;
        try
        {
            LocalDateTime ldt = LocalDateTime.now();
            PreferenceInformationWrapper w1 = new PreferenceInformationWrapper(PC1, 1, 2, ldt);
            PreferenceInformationWrapper w2 = new PreferenceInformationWrapper(PC2, 1, 3, ldt);
            PreferenceInformationWrapper w3 = new PreferenceInformationWrapper(PC3, 2, 3, ldt);
            assertEquals(w1, w2);
            assertNotEquals(w1, w3);
            assertNotEquals(w2, w3);
        } catch (HistoryException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }
}