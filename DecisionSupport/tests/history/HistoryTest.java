package history;

import alternative.Alternative;
import exeption.HistoryException;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;

import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link History}
 *
 * @author MTomczyk
 */
class HistoryTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        PairwiseComparison PC1 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        PairwiseComparison PC2 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        String msg = null;
        try
        {
            PreferenceInformationWrapper piw1 = new PreferenceInformationWrapper(PC1, 0, 0, LocalDateTime.now());
            Thread.sleep(10);
            PreferenceInformationWrapper piw2 = new PreferenceInformationWrapper(PC2, 1, 1, LocalDateTime.now());

            History history = new History("H");
            LinkedList<PreferenceInformationWrapper> piws = new LinkedList<>();
            piws.add(piw1);
            piws.add(piw2);
            history.registerPreferenceInformation(piws);

        } catch (HistoryException | InterruptedException e)
        {
            msg = e.getMessage();
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
        PairwiseComparison PC2 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        String msg = null;
        try
        {
            PreferenceInformationWrapper piw1 = new PreferenceInformationWrapper(PC1, 0, 0, LocalDateTime.now());
            Thread.sleep(10);
            PreferenceInformationWrapper piw2 = new PreferenceInformationWrapper(PC2, 1, 1, LocalDateTime.now());

            History history = new History("H");
            LinkedList<PreferenceInformationWrapper> piws = new LinkedList<>();
            piws.add(piw2);
            piws.add(piw1);
            history.registerPreferenceInformation(piws);

        } catch (HistoryException | InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The preference examples are not sorted in a non-decreasing order of iteration", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        PairwiseComparison PC1 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        PairwiseComparison PC2 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        String msg = null;
        try
        {
            PreferenceInformationWrapper piw1 = new PreferenceInformationWrapper(PC1, 0, 2, LocalDateTime.now());
            Thread.sleep(10);
            PreferenceInformationWrapper piw2 = new PreferenceInformationWrapper(PC2, 1, 1, LocalDateTime.now());

            History history = new History("H");
            LinkedList<PreferenceInformationWrapper> piws = new LinkedList<>();
            piws.add(piw2);
            piws.add(piw1);
            history.registerPreferenceInformation(piws);

        } catch (HistoryException | InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The preference examples are not sorted in a non-decreasing order of date and time", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        PairwiseComparison PC1 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        PairwiseComparison PC2 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        String msg = null;
        try
        {
            LocalDateTime ldt = LocalDateTime.now();
            PreferenceInformationWrapper piw1 = new PreferenceInformationWrapper(PC1, 0, 0, ldt);
            Thread.sleep(10);
            PreferenceInformationWrapper piw2 = new PreferenceInformationWrapper(PC2, 1, 1, ldt);

            History history = new History("H");
            LinkedList<PreferenceInformationWrapper> piws = new LinkedList<>();
            piws.add(piw1);
            piws.add(piw2);
            history.registerPreferenceInformation(piws);

        } catch (HistoryException | InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }


    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        PairwiseComparison PC1 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        String msg = null;
        try
        {
            PreferenceInformationWrapper piw1 = new PreferenceInformationWrapper(PC1, 0, 0, null);

            History history = new History("H");
            LinkedList<PreferenceInformationWrapper> piws = new LinkedList<>();
            piws.add(piw1);
            piws.add(piw1);
            history.registerPreferenceInformation(piws);

        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The preference example = [Pairwise comparison: A1 PREFERENCE A2 ; id = 0 ; iteration = 0 ; date time = not provided] is not unique", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        PairwiseComparison PC1 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        PairwiseComparison PC2 = PairwiseComparison.getPreference(new Alternative("A3", 1), new Alternative("A4", 1));
        PairwiseComparison PC3 = PairwiseComparison.getPreference(new Alternative("A5", 1), new Alternative("A6", 1));
        PairwiseComparison PC4 = PairwiseComparison.getPreference(new Alternative("A7", 1), new Alternative("A8", 1));
        PairwiseComparison PC5 = PairwiseComparison.getPreference(new Alternative("A9", 1), new Alternative("A10", 1));


        String msg = null;
        try
        {
            PreferenceInformationWrapper piw1 = new PreferenceInformationWrapper(PC1, 0, 0, null);
            PreferenceInformationWrapper piw2 = new PreferenceInformationWrapper(PC2, 1, 1, null);
            PreferenceInformationWrapper piw3 = new PreferenceInformationWrapper(PC3, 2, 2, null);
            PreferenceInformationWrapper piw4 = new PreferenceInformationWrapper(PC4, 3, 3, null);
            PreferenceInformationWrapper piw5 = new PreferenceInformationWrapper(PC5, 4, 4, null);

            History history = new History("H");
            LinkedList<PreferenceInformationWrapper> piws = new LinkedList<>();
            piws.add(piw1);
            piws.add(piw2);
            piws.add(piw3);
            piws.add(piw4);
            piws.add(piw5);

            history.registerPreferenceInformation(piws);

            LinkedList<PreferenceInformationWrapper> wrappers = history.getPreferenceInformationCopy();
            wrappers.remove(1);
            wrappers.remove(2);
            LocalDateTime ldt = LocalDateTime.now();
            Report report = history.updateHistoryWithASubset(wrappers, 10, ldt);
            assertEquals(5, report._numberOfPreferenceExamplesBeforeUpdate);
            assertEquals(3, report._numberOfPreferenceExamplesAfterUpdate);
            assertEquals(2, report._numberOfPreferenceExamplesRemovedDuringUpdate);
            assertEquals(2, report._removedPreferenceExamples.size());
            assertEquals(piw2, report._removedPreferenceExamples.getFirst());
            assertEquals(piw4, report._removedPreferenceExamples.getLast());
            assertEquals(10, report._iteration);
            assertEquals(ldt, report._dateTime);


        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        PairwiseComparison PC1 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        PairwiseComparison PC2 = PairwiseComparison.getPreference(new Alternative("A3", 1), new Alternative("A4", 1));


        String msg = null;
        try
        {
            PreferenceInformationWrapper piw1 = new PreferenceInformationWrapper(PC1, 0, 0, null);
            PreferenceInformationWrapper piw2 = new PreferenceInformationWrapper(PC2, 1, 1, null);

            History history = new History("H");
            LinkedList<PreferenceInformationWrapper> piws = new LinkedList<>();
            piws.add(piw1);
            history.registerPreferenceInformation(piws);

            LinkedList<PreferenceInformationWrapper> wrappers = new LinkedList<>();
            wrappers.add(piw1);
            wrappers.add(piw2);
            history.updateHistoryWithASubset(wrappers, 10, null);

        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The preference example = [Pairwise comparison: A3 PREFERENCE A4 ; id = 1 ; iteration = 1 ; date time = not provided] does not exist in the history", msg);
    }


    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        PairwiseComparison PC1 = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
        PairwiseComparison PC2 = PairwiseComparison.getPreference(new Alternative("A3", 1), new Alternative("A4", 1));
        PairwiseComparison PC3 = PairwiseComparison.getPreference(new Alternative("A5", 1), new Alternative("A6", 1));

        String msg = null;
        try
        {
            PreferenceInformationWrapper piw1 = new PreferenceInformationWrapper(PC1, 0, 0, null);
            PreferenceInformationWrapper piw2 = new PreferenceInformationWrapper(PC2, 1, 1, null);
            PreferenceInformationWrapper piw3 = new PreferenceInformationWrapper(PC3, 2, 2, null);

            History history = new History("H");
            LinkedList<PreferenceInformationWrapper> piws = new LinkedList<>();
            piws.add(piw1);
            piws.add(piw2);
            history.registerPreferenceInformation(piws);

            assertFalse(history.remove(piw3));
            assertTrue(history.remove(piw2));
            assertFalse(history.remove(piw2));
            assertTrue(history.remove(piw1));
            assertFalse(history.remove(piw1));
            assertFalse(history.remove(piw1));
            assertFalse(history.remove(piw2));
            assertFalse(history.remove(piw3));
        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
     }
}