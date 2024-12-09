package interaction.feedbackprovider.dm.artificial.value;

import alternative.Alternative;
import exeption.FeedbackProviderException;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;
import interaction.reference.ReferenceSets;
import model.IPreferenceModel;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import relation.Relations;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link PairwiseComparisons}.
 *
 * @author MTomczyk
 */
class PairwiseComparisonsTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        String msg = null;

        PairwiseComparisons<LNorm> PCs = new PairwiseComparisons<>();
        int[] uniqueSizes = new int[]{2};
        HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();
        LinkedList<ReferenceSet> lis = new LinkedList<>();

        LNorm lnorm = new LNorm(new double[]{0.1d, 0.9d}, Double.POSITIVE_INFINITY);
        IPreferenceModel<LNorm> model = new model.definitions.LNorm(lnorm);

        try
        {
            lis.add(new ReferenceSet(new Alternative("A0", new double[]{0.25d, 0.75d}),
                    new Alternative("A1", new double[]{0.75d, 0.25d})));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        map.put(2, lis);

        ReferenceSets rs = null;
        try
        {
            rs = new ReferenceSets(1, uniqueSizes, map);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        LinkedList<IPreferenceInformation> result;
        try
        {
            result = PCs.getFeedback(rs, model);
        } catch (FeedbackProviderException e)
        {
            throw new RuntimeException(e);
        }

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.getFirst());
        assertTrue(result.getFirst() instanceof PairwiseComparison);
        PairwiseComparison PC1 = (PairwiseComparison) result.getFirst();
        assertEquals("A1", PC1.getPreferredAlternative().getName());
        assertEquals("A0", PC1.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC1.getRelation());
    }


    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        String msg = null;

        PairwiseComparisons<LNorm> PCs = new PairwiseComparisons<>();
        int[] uniqueSizes = new int[]{2};
        HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();
        LinkedList<ReferenceSet> lis = new LinkedList<>();

        LNorm lnorm = new LNorm(new double[]{0.6d, 0.4d}, Double.POSITIVE_INFINITY);
        IPreferenceModel<LNorm> model = new model.definitions.LNorm(lnorm);

        try
        {
            lis.add(new ReferenceSet(new Alternative("A0", new double[]{0.25d, 0.75d}),
                    new Alternative("A1", new double[]{0.75d, 0.25d})));
            lis.add(new ReferenceSet(new Alternative("A2", new double[]{0.1d, 0.9d}),
                    new Alternative("A3", new double[]{0.3d, 0.7d})));
            lis.add(new ReferenceSet(new Alternative("A4", new double[]{0.8d, 0.2d}),
                    new Alternative("A5", new double[]{0.5d, 0.5d})));
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        map.put(2, lis);

        ReferenceSets rs = null;
        try
        {
            rs = new ReferenceSets(3, uniqueSizes, map);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        LinkedList<IPreferenceInformation> result;
        try
        {
            result = PCs.getFeedback(rs, model);
        } catch (FeedbackProviderException e)
        {
            throw new RuntimeException(e);
        }

        assertNotNull(result);
        assertEquals(3, result.size());
        assertNotNull(result.getFirst());
        assertNotNull(result.get(1));
        assertNotNull(result.get(2));

        assertTrue(result.getFirst() instanceof PairwiseComparison);
        assertTrue(result.get(1) instanceof PairwiseComparison);
        assertTrue(result.get(2) instanceof PairwiseComparison);

        PairwiseComparison PC1 = (PairwiseComparison) result.getFirst();
        assertEquals("A0", PC1.getPreferredAlternative().getName());
        assertEquals("A1", PC1.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC1.getRelation());

        PairwiseComparison PC2 = (PairwiseComparison) result.get(1);
        assertEquals("A3", PC2.getPreferredAlternative().getName());
        assertEquals("A2", PC2.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC2.getRelation());

        PairwiseComparison PC3 = (PairwiseComparison) result.get(2);
        assertEquals("A5", PC3.getPreferredAlternative().getName());
        assertEquals("A4", PC3.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC3.getRelation());
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        String msg = null;

        PairwiseComparisons<LNorm> PCs = new PairwiseComparisons<>(true);
        int[] uniqueSizes = new int[]{1, 2, 3};
        HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();

        LNorm lnorm = new LNorm(new double[]{0.6d, 0.4d}, Double.POSITIVE_INFINITY);
        IPreferenceModel<LNorm> model = new model.definitions.LNorm(lnorm);

        try
        {
            LinkedList<ReferenceSet> lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A10", new double[]{0.25d, 0.75d})));
            map.put(1, lis);

            lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A0", new double[]{0.25d, 0.75d}),
                    new Alternative("A1", new double[]{0.75d, 0.25d})));
            lis.add(new ReferenceSet(new Alternative("A2", new double[]{0.1d, 0.9d}),
                    new Alternative("A3", new double[]{0.3d, 0.7d})));
            lis.add(new ReferenceSet(new Alternative("A4", new double[]{0.8d, 0.2d}),
                    new Alternative("A5", new double[]{0.5d, 0.5d})));
            map.put(2, lis);

            lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A111", new double[]{0.25d, 0.75d}),
                    new Alternative("A222", new double[]{0.75d, 0.25d}),
                    new Alternative("A333", new double[]{0.75d, 0.25d})));
            map.put(3, lis);

        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        ReferenceSets rs = null;
        try
        {
            rs = new ReferenceSets(5, uniqueSizes, map);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        LinkedList<IPreferenceInformation> result;
        try
        {
            result = PCs.getFeedback(rs, model);
        } catch (FeedbackProviderException e)
        {
            throw new RuntimeException(e);
        }

        assertNotNull(result);
        assertEquals(3, result.size());
        assertNotNull(result.getFirst());
        assertNotNull(result.get(1));
        assertNotNull(result.get(2));

        assertTrue(result.getFirst() instanceof PairwiseComparison);
        assertTrue(result.get(1) instanceof PairwiseComparison);
        assertTrue(result.get(2) instanceof PairwiseComparison);

        PairwiseComparison PC1 = (PairwiseComparison) result.getFirst();
        assertEquals("A0", PC1.getPreferredAlternative().getName());
        assertEquals("A1", PC1.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC1.getRelation());

        PairwiseComparison PC2 = (PairwiseComparison) result.get(1);
        assertEquals("A3", PC2.getPreferredAlternative().getName());
        assertEquals("A2", PC2.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC2.getRelation());

        PairwiseComparison PC3 = (PairwiseComparison) result.get(2);
        assertEquals("A5", PC3.getPreferredAlternative().getName());
        assertEquals("A4", PC3.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC3.getRelation());
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        String msg = null;

        PairwiseComparisons<LNorm> PCs = new PairwiseComparisons<>(false);
        int[] uniqueSizes = new int[]{1, 2, 3};
        HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();

        LNorm lnorm = new LNorm(new double[]{0.6d, 0.4d}, Double.POSITIVE_INFINITY);
        IPreferenceModel<LNorm> model = new model.definitions.LNorm(lnorm);

        try
        {
            LinkedList<ReferenceSet> lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A10", new double[]{0.25d, 0.75d})));
            map.put(1, lis);

            lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A0", new double[]{0.25d, 0.75d}),
                    new Alternative("A1", new double[]{0.75d, 0.25d})));
            lis.add(new ReferenceSet(new Alternative("A2", new double[]{0.1d, 0.9d}),
                    new Alternative("A3", new double[]{0.3d, 0.7d})));
            lis.add(new ReferenceSet(new Alternative("A4", new double[]{0.8d, 0.2d}),
                    new Alternative("A5", new double[]{0.5d, 0.5d})));
            map.put(2, lis);

            lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A111", new double[]{0.3d, 0.7d}),
                    new Alternative("A222", new double[]{0.4d, 0.6d}),
                    new Alternative("A333", new double[]{0.6d, 0.4d})));
            map.put(3, lis);

        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        ReferenceSets rs = null;
        try
        {
            rs = new ReferenceSets(5, uniqueSizes, map);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        LinkedList<IPreferenceInformation> result;
        try
        {
            result = PCs.getFeedback(rs, model);
        } catch (FeedbackProviderException e)
        {
            throw new RuntimeException(e);
        }

        assertNotNull(result);
        assertEquals(6, result.size());
        assertNotNull(result.getFirst());
        assertNotNull(result.get(1));
        assertNotNull(result.get(2));
        assertNotNull(result.get(3));
        assertNotNull(result.get(4));
        assertNotNull(result.get(5));

        assertTrue(result.getFirst() instanceof PairwiseComparison);
        assertTrue(result.get(1) instanceof PairwiseComparison);
        assertTrue(result.get(2) instanceof PairwiseComparison);
        assertTrue(result.get(3) instanceof PairwiseComparison);
        assertTrue(result.get(4) instanceof PairwiseComparison);
        assertTrue(result.get(5) instanceof PairwiseComparison);

        PairwiseComparison PC1 = (PairwiseComparison) result.getFirst();
        assertEquals("A0", PC1.getPreferredAlternative().getName());
        assertEquals("A1", PC1.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC1.getRelation());

        PairwiseComparison PC2 = (PairwiseComparison) result.get(1);
        assertEquals("A3", PC2.getPreferredAlternative().getName());
        assertEquals("A2", PC2.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC2.getRelation());

        PairwiseComparison PC3 = (PairwiseComparison) result.get(2);
        assertEquals("A5", PC3.getPreferredAlternative().getName());
        assertEquals("A4", PC3.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC3.getRelation());

        PairwiseComparison PC4 = (PairwiseComparison) result.get(3);
        assertEquals("A222", PC4.getPreferredAlternative().getName());
        assertEquals("A111", PC4.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC4.getRelation());

        PairwiseComparison PC5 = (PairwiseComparison) result.get(4);
        assertEquals("A111", PC5.getPreferredAlternative().getName());
        assertEquals("A333", PC5.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC5.getRelation());

        PairwiseComparison PC6 = (PairwiseComparison) result.get(5);
        assertEquals("A222", PC6.getPreferredAlternative().getName());
        assertEquals("A333", PC6.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC6.getRelation());
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        String msg = null;

        PairwiseComparisons<LNorm> PCs = new PairwiseComparisons<>(false, 0.1d);
        int[] uniqueSizes = new int[]{1, 2, 3};
        HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();

        LNorm lnorm = new LNorm(new double[]{0.6d, 0.4d}, Double.POSITIVE_INFINITY);
        IPreferenceModel<LNorm> model = new model.definitions.LNorm(lnorm);

        try
        {
            LinkedList<ReferenceSet> lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A10", new double[]{0.25d, 0.75d})));
            map.put(1, lis);

            lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A0", new double[]{0.25d, 0.75d}),
                    new Alternative("A1", new double[]{0.75d, 0.25d})));
            lis.add(new ReferenceSet(new Alternative("A2", new double[]{0.1d, 0.9d}),
                    new Alternative("A3", new double[]{0.3d, 0.7d})));
            lis.add(new ReferenceSet(new Alternative("A4", new double[]{0.8d, 0.2d}),
                    new Alternative("A5", new double[]{0.5d, 0.5d})));
            map.put(2, lis);

            lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A111", new double[]{0.3d, 0.7d}),
                    new Alternative("A222", new double[]{0.4d, 0.6d}),
                    new Alternative("A333", new double[]{0.6d, 0.4d})));
            map.put(3, lis);

        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        ReferenceSets rs = null;
        try
        {
            rs = new ReferenceSets(5, uniqueSizes, map);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        LinkedList<IPreferenceInformation> result;
        try
        {
            result = PCs.getFeedback(rs, model);
        } catch (FeedbackProviderException e)
        {
            throw new RuntimeException(e);
        }

        assertNotNull(result);
        assertEquals(6, result.size());
        assertNotNull(result.getFirst());
        assertNotNull(result.get(1));
        assertNotNull(result.get(2));
        assertNotNull(result.get(3));
        assertNotNull(result.get(4));
        assertNotNull(result.get(5));

        assertTrue(result.getFirst() instanceof PairwiseComparison);
        assertTrue(result.get(1) instanceof PairwiseComparison);
        assertTrue(result.get(2) instanceof PairwiseComparison);
        assertTrue(result.get(3) instanceof PairwiseComparison);
        assertTrue(result.get(4) instanceof PairwiseComparison);
        assertTrue(result.get(5) instanceof PairwiseComparison);

        PairwiseComparison PC1 = (PairwiseComparison) result.getFirst();
        assertEquals("A0", PC1.getPreferredAlternative().getName());
        assertEquals("A1", PC1.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC1.getRelation());

        PairwiseComparison PC2 = (PairwiseComparison) result.get(1);
        assertNull(PC2.getPreferredAlternative());
        assertNull(PC2.getNotPreferredAlternative());
        assertEquals("A2", PC2.getFirstAlternative().getName());
        assertEquals("A3", PC2.getSecondAlternative().getName());
        assertEquals(Relations.INDIFFERENCE, PC2.getRelation());

        PairwiseComparison PC3 = (PairwiseComparison) result.get(2);
        assertEquals("A5", PC3.getPreferredAlternative().getName());
        assertEquals("A4", PC3.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC3.getRelation());

        PairwiseComparison PC4 = (PairwiseComparison) result.get(3);
        assertNull(PC4.getPreferredAlternative());
        assertNull(PC4.getNotPreferredAlternative());
        assertEquals("A111", PC4.getFirstAlternative().getName());
        assertEquals("A222", PC4.getSecondAlternative().getName());
        assertEquals(Relations.INDIFFERENCE, PC4.getRelation());

        PairwiseComparison PC5 = (PairwiseComparison) result.get(4);
        assertNull(PC5.getPreferredAlternative());
        assertNull(PC5.getNotPreferredAlternative());
        assertEquals("A111", PC5.getFirstAlternative().getName());
        assertEquals("A333", PC5.getSecondAlternative().getName());
        assertEquals(Relations.INDIFFERENCE, PC5.getRelation());

        PairwiseComparison PC6 = (PairwiseComparison) result.get(5);
        assertEquals("A222", PC6.getPreferredAlternative().getName());
        assertEquals("A333", PC6.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC6.getRelation());
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        String msg = null;

        PairwiseComparisons<LNorm> PCs = new PairwiseComparisons<>(false, 0.1d);
        int[] uniqueSizes = new int[]{1};
        HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();

        LNorm lnorm = new LNorm(new double[]{0.6d, 0.4d}, Double.POSITIVE_INFINITY);
        IPreferenceModel<LNorm> model = new model.definitions.LNorm(lnorm);

        try
        {
            LinkedList<ReferenceSet> lis = new LinkedList<>();
            lis.add(new ReferenceSet(new Alternative("A10", new double[]{0.25d, 0.75d})));
            map.put(1, lis);

        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        ReferenceSets rs = null;
        try
        {
            rs = new ReferenceSets(1, uniqueSizes, map);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        LinkedList<IPreferenceInformation> result;
        try
        {
            result = PCs.getFeedback(rs, model);
        } catch (FeedbackProviderException e)
        {
            throw new RuntimeException(e);
        }

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}