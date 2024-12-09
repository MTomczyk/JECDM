package interaction.feedbackprovider;

import alternative.Alternative;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.FeedbackProviderException;
import exeption.ReferenceSetsConstructorException;
import interaction.feedbackprovider.dm.DMResult;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.feedbackprovider.dm.artificial.value.IFormConstructor;
import interaction.feedbackprovider.dm.artificial.value.PairwiseComparisons;
import interaction.reference.ReferenceSet;
import interaction.reference.ReferenceSets;
import interaction.reference.ReferenceSetsContainer;
import interaction.reference.Result;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;
import relation.Relations;
import space.Range;
import space.os.ObjectiveSpace;
import system.dm.DM;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link FeedbackProvider}.
 *
 * @author MTomczyk
 */
class FeedbackProviderTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {

        FeedbackProvider.Params pF = new FeedbackProvider.Params();
        FeedbackProvider feedbackProvider = new FeedbackProvider(pF);
        String msg = null;
        try
        {
            feedbackProvider.validate(null);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Neither common nor per-DMs feedback provider is provided (the objects are null)", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        DM dm = new DM(0, "DM1");
        LNorm lnorm = new LNorm(new double[]{0.4d, 0.6d}, Double.POSITIVE_INFINITY);
        IDMFeedbackProvider provider = new ArtificialValueDM<>(new model.definitions.LNorm(lnorm));
        FeedbackProvider feedbackProvider = FeedbackProvider.getForSingleDM(dm.getName(), provider);
        String msg = null;
        try
        {
            feedbackProvider.validate(null);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The decision makers are not provided (the array is null)", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        DM dm = new DM(0, "DM1");
        LNorm lnorm = new LNorm(new double[]{0.4d, 0.6d}, Double.POSITIVE_INFINITY);
        IDMFeedbackProvider provider = new ArtificialValueDM<>(new model.definitions.LNorm(lnorm));
        FeedbackProvider feedbackProvider = FeedbackProvider.getForSingleDM(dm.getName(), provider);
        String msg = null;
        try
        {
            DM [] dms = new DM[2];
            dms[0] = dm;
            dms[1] = new DM(1, "DM2");
            feedbackProvider.validate(dms);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertEquals("No feedback provider is associated with a decision maker = DM2", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        DM dm = new DM(0, "DM1");
        LNorm lnorm = new LNorm(new double[]{0.4d, 0.6d}, Double.POSITIVE_INFINITY);
        IDMFeedbackProvider provider = new ArtificialValueDM<>(new model.definitions.LNorm(lnorm), (IFormConstructor<LNorm>) null);
        FeedbackProvider feedbackProvider = FeedbackProvider.getForSingleDM(dm.getName(), provider);
        String msg = null;
        try
        {
            DM [] dms = new DM[1];
            dms[0] = dm;
            feedbackProvider.validate(dms);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The form constructors are not specified (the array is null)", msg);
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        DM dm = new DM(0, "DM1");
        LNorm lnorm = new LNorm(new double[]{0.4d, 0.6d}, Double.POSITIVE_INFINITY);
        IDMFeedbackProvider provider = new ArtificialValueDM<>(new model.definitions.LNorm(lnorm), new LinkedList<>());
        FeedbackProvider feedbackProvider = FeedbackProvider.getForSingleDM(dm.getName(), provider);
        String msg = null;
        try
        {
            DM [] dms = new DM[1];
            dms[0] = dm;
            feedbackProvider.validate(dms);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The form constructors are not specified (the array is empty)", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        DM dm = new DM(0, "DM1");
        LNorm lnorm = new LNorm(new double[]{0.4d, 0.6d}, Double.POSITIVE_INFINITY);
        LinkedList<IFormConstructor<LNorm>> fc = new LinkedList<>();
        fc.add(null);
        IDMFeedbackProvider provider = new ArtificialValueDM<>(new model.definitions.LNorm(lnorm), fc);
        FeedbackProvider feedbackProvider = FeedbackProvider.getForSingleDM(dm.getName(), provider);
        String msg = null;
        try
        {
            DM [] dms = new DM[1];
            dms[0] = dm;
            feedbackProvider.validate(dms);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertEquals("One of the provided form constructors is null", msg);
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        DM dm = new DM(0, "DM1");
        DM [] dms = new DM[1];
        dms[0] = dm;

        LNorm lnorm = new LNorm(new double[]{0.4d, 0.6d}, Double.POSITIVE_INFINITY);
        IDMFeedbackProvider provider = new ArtificialValueDM<>(new model.definitions.LNorm(lnorm));
        FeedbackProvider feedbackProvider = FeedbackProvider.getForSingleDM(dm.getName(), provider);
        String msg = null;
        try
        {

            feedbackProvider.validate(dms);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            feedbackProvider.generateFeedback(null, dms, null);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The current decision-making context is not provided", msg);
    }

    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        DM dm = new DM(0, "DM1");
        DM [] dms = new DM[1];
        dms[0] = dm;

        LNorm lnorm = new LNorm(new double[]{0.4d, 0.6d}, Double.POSITIVE_INFINITY);
        IDMFeedbackProvider provider = new ArtificialValueDM<>(new model.definitions.LNorm(lnorm));
        FeedbackProvider feedbackProvider = FeedbackProvider.getForSingleDM(dm.getName(), provider);
        String msg = null;
        try
        {

            feedbackProvider.validate(dms);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        LocalDateTime dateTime = LocalDateTime.now();
        ObjectiveSpace currentOs = new ObjectiveSpace(Range.getDefaultRanges(2, 1.0d), new boolean[2]);

        DMContext dmContext = new DMContext(criteria, dateTime, null, currentOs, false, 0);

        try
        {
            feedbackProvider.generateFeedback(dmContext, dms, null);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The reference sets are not provided (the input is null)", msg);
    }

    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        DM dm = new DM(0, "DM1");
        DM [] dms = new DM[1];
        dms[0] = dm;

        LNorm lnorm = new LNorm(new double[]{0.4d, 0.6d}, Double.POSITIVE_INFINITY);
        IDMFeedbackProvider provider = new ArtificialValueDM<>(new model.definitions.LNorm(lnorm));
        FeedbackProvider feedbackProvider = FeedbackProvider.getForSingleDM(dm.getName(), provider);
        String msg = null;
        try
        {

            feedbackProvider.validate(dms);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        LocalDateTime dateTime = LocalDateTime.now();
        ObjectiveSpace currentOs = new ObjectiveSpace(Range.getDefaultRanges(2, 1.0d), new boolean[2]);

        DMContext dmContext = new DMContext(criteria, dateTime, null, currentOs, false, 0);
        Result result = new Result(dmContext, null);

        try
        {
            feedbackProvider.generateFeedback(dmContext, dms, result);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The reference sets are not provided (the input is null)", msg);
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        DM dm = new DM(0, "DM1");
        DM [] dms = new DM[1];
        dms[0] = dm;

        LNorm lnorm = new LNorm(new double[]{0.6d, 0.4d}, Double.POSITIVE_INFINITY);
        IDMFeedbackProvider provider = new ArtificialValueDM<>(new model.definitions.LNorm(lnorm), new PairwiseComparisons<>(false));
        FeedbackProvider feedbackProvider = FeedbackProvider.getForSingleDM(dm.getName(), provider);
        String msg = null;
        try
        {

            feedbackProvider.validate(dms);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        LocalDateTime dateTime = LocalDateTime.now();
        ObjectiveSpace currentOs = new ObjectiveSpace(Range.getDefaultRanges(2, 1.0d), new boolean[2]);

        DMContext dmContext = new DMContext(criteria, dateTime, null, currentOs, false, 0);
        Result result = new Result(dmContext, null);

        ReferenceSets commonRS = null;
        try
        {
            HashMap<Integer,LinkedList<ReferenceSet>> map = new HashMap<>();
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

            commonRS = new ReferenceSets(5, new int[]{1, 2, 3}, map);


        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        result._referenceSetsContainer = new ReferenceSetsContainer(5, commonRS, new HashMap<>());

        interaction.feedbackprovider.Result rF = null;
        try
        {
            rF = feedbackProvider.generateFeedback(dmContext, dms, result);
        } catch (FeedbackProviderException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(rF);
        assertEquals(0, rF._iteration);
        assertTrue(dateTime.isBefore(rF._dmContextDateTime) || dateTime.isEqual(rF._dmContextDateTime));
        assertTrue(rF._processingTime >= 0);
        assertNotNull(rF._feedback);
        HashMap<DM, DMResult> map = rF._feedback;
        assertEquals(1, map.size());
        assertTrue(map.containsKey(dm));
        DMResult dmResult = map.get(dm);

        assertNotNull(dmResult);
        assertEquals(0, dmResult._iteration);
        assertTrue(dateTime.isBefore(dmResult._dmContextDateTime) || dateTime.isEqual(dmResult._dmContextDateTime));
        assertTrue(dmResult._processingTime >= 0);

        assertNotNull(dmResult._feedback);
        assertEquals(6, dmResult._feedback.size());

        assertNotNull(dmResult._feedback.getFirst());
        assertNotNull(dmResult._feedback.get(1));
        assertNotNull(dmResult._feedback.get(2));
        assertNotNull(dmResult._feedback.get(3));
        assertNotNull(dmResult._feedback.get(4));
        assertNotNull(dmResult._feedback.get(5));

        assertTrue(dmResult._feedback.getFirst() instanceof PairwiseComparison);
        assertTrue(dmResult._feedback.get(1) instanceof PairwiseComparison);
        assertTrue(dmResult._feedback.get(2) instanceof PairwiseComparison);
        assertTrue(dmResult._feedback.get(3) instanceof PairwiseComparison);
        assertTrue(dmResult._feedback.get(4) instanceof PairwiseComparison);
        assertTrue(dmResult._feedback.get(5) instanceof PairwiseComparison);

        PairwiseComparison PC1 = (PairwiseComparison) dmResult._feedback.getFirst();
        assertEquals("A0", PC1.getPreferredAlternative().getName());
        assertEquals("A1", PC1.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC1.getRelation());

        PairwiseComparison PC2 = (PairwiseComparison) dmResult._feedback.get(1);
        assertEquals("A3", PC2.getPreferredAlternative().getName());
        assertEquals("A2", PC2.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC2.getRelation());

        PairwiseComparison PC3 = (PairwiseComparison) dmResult._feedback.get(2);
        assertEquals("A5", PC3.getPreferredAlternative().getName());
        assertEquals("A4", PC3.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC3.getRelation());

        PairwiseComparison PC4 = (PairwiseComparison) dmResult._feedback.get(3);
        assertEquals("A222", PC4.getPreferredAlternative().getName());
        assertEquals("A111", PC4.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC4.getRelation());

        PairwiseComparison PC5 = (PairwiseComparison) dmResult._feedback.get(4);
        assertEquals("A111", PC5.getPreferredAlternative().getName());
        assertEquals("A333", PC5.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC5.getRelation());

        PairwiseComparison PC6 = (PairwiseComparison) dmResult._feedback.get(5);
        assertEquals("A222", PC6.getPreferredAlternative().getName());
        assertEquals("A333", PC6.getNotPreferredAlternative().getName());
        assertEquals(Relations.PREFERENCE, PC6.getRelation());

        System.out.println("------------------------------------------------------------------");
        result.printStringRepresentation();
        System.out.println("------------------------------------------------------------------");
        rF.printStringRepresentation();
        System.out.println("------------------------------------------------------------------");
        dmResult.printStringRepresentation();
        System.out.println("------------------------------------------------------------------");

    }
}