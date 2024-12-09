package interaction.reference;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.Status;
import interaction.reference.constructor.AllAlternatives;
import interaction.reference.constructor.IReferenceSetConstructor;
import org.junit.jupiter.api.Test;
import system.dm.DM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ReferenceSetsConstructor}.
 *
 * @author MTomczyk
 */
class ReferenceSetsConstructorTest
{


    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertEquals("The reference sets constructors are not provided (neither the common nor unique for each decision maker)", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(null);
        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertEquals("One of the provided reference sets constructors is null", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(RSC);

        try
        {
            RSC.constructReferenceSets(null, null, null);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Decision-making context is not provided", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(RSC);

        try
        {
            RSC.constructReferenceSets(new DMContext(null, null, null, null, false, 0), null, null);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Criteria are not supplied", msg);
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(RSC);

        try
        {
            RSC.constructReferenceSets(new DMContext(Criteria.constructCriteria("C", 2, false), null, null, null, false, 0), null, null);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Refining results are not provided", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(RSC);

        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null,
                null, null, false, 1);
        interaction.refine.Result rr = new interaction.refine.Result(dmContext);
        rr._refinedAlternatives = null;

        try
        {
            RSC.constructReferenceSets(dmContext, null, rr);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("Refined alternatives are not provided (the array is null)", msg);
    }


    /**
     * Test 7.
     */
    @Test
    void test7()
    {

        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new IReferenceSetConstructor()
        {
            /**
             * Dummy implementation.
             * @param filteredAlternatives dummy implementation
             * @return dummy implementation
             * @throws ReferenceSetsConstructorException dummy implementation
             */
            @Override
            public int getExpectedSize(AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
            {
                return 100;
            }

            /**
             * Dummy implementation.
             * @param dmContext                  dummy implementation
             * @param filteredAlternatives dummy implementation
             * @return dummy implementation
             * @throws ReferenceSetsConstructorException dummy implementation
             */
            @Override
            public LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
            {
                return null;
            }
        });

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(RSC);

        double[][] evals = new double[][]{
                {0.0d, 11.0d},
                {1.0d, 5.0d},
                {3.0d, 3.0d},
                {3.0d, 7.0d},
                {7.0d, 7.0d},
                {11.0d, 1.0d},
                {5.0d, 2.0d},
                {5.0d, 2.0d},
                {9.0d, 9.0d},
                {3.0d, 7.0d},
                {1.0d, 5.0d}
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);
        ArrayList<Alternative> refined = new ArrayList<>();
        refined.add(alternatives.get(0));
        refined.add(alternatives.get(1));
        refined.add(alternatives.get(2));
        refined.add(alternatives.get(5));
        refined.add(alternatives.get(6));

        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null,
                new Alternatives(alternatives), null, false, 0);
        interaction.refine.Result rr = new interaction.refine.Result(dmContext);
        rr._refinedAlternatives = new Alternatives(refined);

        Result r = null;
        try
        {
            r = RSC.constructReferenceSets(dmContext, null, rr);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(r);

        assertEquals(Status.TERMINATED_DUE_TO_HAVING_NOT_ENOUGH_ALTERNATIVES, r._status);
        assertTrue(r._terminatedDueToNotEnoughAlternatives);
        assertNull(r._referenceSetsContainer);
        assertTrue(r._processingTime >= 0);
        assertEquals("Not enough alternatives (required = 100 but 5 remained after reduction)", r._terminatedDueToNotEnoughAlternativesMessage);
        assertEquals(0, r._iteration);
    }


    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(RSC);

        double[][] evals = new double[][]{
                {0.0d, 11.0d},
                {1.0d, 5.0d},
                {3.0d, 3.0d},
                {3.0d, 7.0d},
                {7.0d, 7.0d},
                {11.0d, 1.0d},
                {5.0d, 2.0d},
                {5.0d, 2.0d},
                {9.0d, 9.0d},
                {3.0d, 7.0d},
                {1.0d, 5.0d}
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);
        ArrayList<Alternative> refined = new ArrayList<>();
        refined.add(alternatives.get(0));
        refined.add(alternatives.get(1));
        refined.add(alternatives.get(2));
        refined.add(alternatives.get(5));
        refined.add(alternatives.get(6));

        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null,
                new Alternatives(alternatives), null, false, 1);
        interaction.refine.Result rr = new interaction.refine.Result(dmContext);
        rr._refinedAlternatives = new Alternatives(refined);

        Result r = null;
        try
        {
            r = RSC.constructReferenceSets(dmContext, null, rr);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(r);
        r.printStringRepresentation();
        assertEquals(Status.PROCESS_ENDED_SUCCESSFULLY, r._status);
        assertFalse(r._terminatedDueToNotEnoughAlternatives);
        assertNull(r._terminatedDueToNotEnoughAlternativesMessage);
        assertTrue(r._processingTime >= 0);
        String[] expNames = new String[]{"A0", "A1", "A2", "A5", "A6"};
        assertEquals(1, r._referenceSetsContainer.getNoSets());
        assertEquals(1, r._referenceSetsContainer.getCommonReferenceSets().getNoSets());
        assertEquals(1, r._referenceSetsContainer.getCommonReferenceSets().getUniqueSizes().length);
        assertEquals(5, r._referenceSetsContainer.getCommonReferenceSets().getUniqueSizes()[0]);
        assertEquals(1, r._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(5).size());
        ReferenceSet rs = r._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(5).get(0);
        assertEquals(5, rs.getSize());
        assertEquals("Alternatives = A0, A1, A2, A5, A6", rs.getStringRepresentation());
        for (int i = 0; i < 5; i++) assertEquals(expNames[i], rs.getAlternatives().get(i).getName());
        assertEquals(1, r._iteration);
    }


    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());
        p._dmConstructors = new HashMap<>();
        {
            p._dmConstructors.put("DM1", null);
        }
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(new AllAlternatives());
            p._dmConstructors.put("DM2", dmC);
        }

        DM[] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");


        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(dms);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertEquals("The reference sets constructors lists for the decision maker = DM1 are not provided (the array is null)", msg);
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());
        p._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(new AllAlternatives());
            p._dmConstructors.put("DM1", dmC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            p._dmConstructors.put("DM2", dmC);
        }
        DM[] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(dms);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertEquals("The reference sets constructors lists for the decision maker = DM2 are not provided (the array is empty)", msg);
    }

    /**
     * Test 11.
     */
    @Test
    void test11()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());
        p._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(new AllAlternatives());
            p._dmConstructors.put("DM1", dmC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(null);
            p._dmConstructors.put("DM2", dmC);
        }

        DM[] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(dms);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertEquals("One of the provided reference sets constructors for the decision maker =  DM2 is null", msg);
    }

    /**
     * Test 12.
     */
    @Test
    void test12()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());
        p._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(new AllAlternatives());
            p._dmConstructors.put("DM1", dmC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(new AllAlternatives());
            p._dmConstructors.put("DM2", dmC);
        }

        DM[] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(dms);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(RSC);

        double[][] evals = new double[][]{
                {0.0d, 1.0d},
                {0.5d, 5.0d},
                {1.0d, 0.0d},
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);
        ArrayList<Alternative> refined = new ArrayList<>();
        refined.add(alternatives.get(0));
        refined.add(alternatives.get(1));
        refined.add(alternatives.get(2));


        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null,
                new Alternatives(alternatives), null, false, 1);
        interaction.refine.Result rr = new interaction.refine.Result(dmContext);
        rr._refinedAlternatives = new Alternatives(refined);

        Result r = null;
        try
        {
            r = RSC.constructReferenceSets(dmContext, null, rr);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertEquals("The per-decision-makers reference sets are expected to be constructed but the decision makers' identifiers are not provided (the array is null)", msg);
        assertNull(r);
    }

    /**
     * Test 13.
     */
    @Test
    void test13()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new AllAlternatives());
        p._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(new AllAlternatives());
            p._dmConstructors.put("DM1", dmC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(new AllAlternatives());
            p._dmConstructors.put("DM2", dmC);
        }

        DM[] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(dms);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(RSC);

        double[][] evals = new double[][]{
                {0.0d, 1.0d},
                {0.5d, 5.0d},
                {1.0d, 0.0d},
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);
        ArrayList<Alternative> refined = new ArrayList<>();
        refined.add(alternatives.get(0));
        refined.add(alternatives.get(1));
        refined.add(alternatives.get(2));


        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null,
                new Alternatives(alternatives), null, false, 1);
        interaction.refine.Result rr = new interaction.refine.Result(dmContext);
        rr._refinedAlternatives = new Alternatives(refined);

        DM[] DMs = new DM[2];
        DMs[0] = new DM(0, "DM1");
        DMs[1] = new DM(1, "DM2");

        Result r = null;
        try
        {
            r = RSC.constructReferenceSets(dmContext, DMs, rr);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(r);
        assertEquals(Status.PROCESS_ENDED_SUCCESSFULLY, r._status);


        assertNotNull(r._referenceSetsContainer);
        assertNotNull(r._referenceSetsContainer.getCommonReferenceSets());
        assertNotNull(r._referenceSetsContainer.getDMReferenceSets());
        assertEquals(2, r._referenceSetsContainer.getDMReferenceSets().size());
        assertTrue(r._referenceSetsContainer.getDMReferenceSets().containsKey(DMs[0]));
        assertTrue(r._referenceSetsContainer.getDMReferenceSets().containsKey(DMs[1]));

        assertFalse(r._terminatedDueToNotEnoughAlternatives);
        assertNull(r._terminatedDueToNotEnoughAlternativesMessage);
        assertEquals(1, r._iteration);

        assertTrue(r._processingTime >= 0);
        String[] expNames = new String[]{"A0", "A1", "A2"};
        assertEquals(3, r._referenceSetsContainer.getNoSets());

        for (ReferenceSets rss : new ReferenceSets[]{r._referenceSetsContainer.getCommonReferenceSets(),
                r._referenceSetsContainer.getDMReferenceSets().get(DMs[0]),
                r._referenceSetsContainer.getDMReferenceSets().get(DMs[1])})
        {
            assertEquals(1, rss.getNoSets());
            assertEquals(1, rss.getUniqueSizes().length);
            assertEquals(3, rss.getUniqueSizes()[0]);
            assertEquals(1, rss.getReferenceSets().get(3).size());

            ReferenceSet rs = rss.getReferenceSets().get(3).get(0);
            assertEquals(3, rs.getSize());
            assertEquals("Alternatives = A0, A1, A2", rs.getStringRepresentation());
            for (int i = 0; i < 3; i++) assertEquals(expNames[i], rs.getAlternatives().get(i).getName());
        }
    }

    /**
     * Test 14.
     */
    @SuppressWarnings("ExtractMethodRecommender")
    @Test
    void test14()
    {
        ReferenceSetsConstructor.Params p = new ReferenceSetsConstructor.Params();
        p._commonConstructors = new LinkedList<>();
        p._commonConstructors.add(new IReferenceSetConstructor()
        {
            /**
             * Dummy.
             * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
             * @return 2
             * @throws ReferenceSetsConstructorException dummy
             */
            @Override
            public int getExpectedSize(AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
            {
                return 2;
            }

            /**
             * Dummy.
             * @param dmContext                  current decision-making context
             * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
             * @return dummy
             * @throws ReferenceSetsConstructorException dummy
             */
            @Override
            public LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
            {
                LinkedList<ReferenceSet> rss = new LinkedList<>();
                ReferenceSet rs = new ReferenceSet(filteredAlternatives.get(0), filteredAlternatives.get(1));
                rss.add(rs);
                return rss;
            }
        });


        p._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(new IReferenceSetConstructor()
            {
                /**
                 * Dummy.
                 * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
                 * @return 2
                 * @throws ReferenceSetsConstructorException dummy
                 */
                @Override
                public int getExpectedSize(AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
                {
                    return 2;
                }

                /**
                 * Dummy.
                 * @param dmContext                  current decision-making context
                 * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
                 * @return dummy
                 * @throws ReferenceSetsConstructorException dummy
                 */
                @Override
                public LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
                {
                    LinkedList<ReferenceSet> rss = new LinkedList<>();
                    rss.add(new ReferenceSet(filteredAlternatives.get(1), filteredAlternatives.get(2)));
                    return rss;
                }
            });
            p._dmConstructors.put("DM1", dmC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmC = new LinkedList<>();
            dmC.add(new IReferenceSetConstructor()
            {
                /**
                 * Dummy.
                 * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
                 * @return 2
                 * @throws ReferenceSetsConstructorException dummy
                 */
                @Override
                public int getExpectedSize(AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
                {
                    return 2;
                }

                /**
                 * Dummy.
                 * @param dmContext                  current decision-making context
                 * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
                 * @return dummy
                 * @throws ReferenceSetsConstructorException dummy
                 */
                @Override
                public LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
                {
                    LinkedList<ReferenceSet> rss = new LinkedList<>();
                    rss.add(new ReferenceSet(filteredAlternatives.get(0), filteredAlternatives.get(2)));
                    rss.add(new ReferenceSet(filteredAlternatives.get(0), filteredAlternatives.get(1)));
                    return rss;
                }
            });
            p._dmConstructors.put("DM2", dmC);
        }

        DM[] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(p);
        String msg = null;
        try
        {
            RSC.validate(dms);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(RSC);

        double[][] evals = new double[][]{
                {0.0d, 1.0d},
                {0.5d, 5.0d},
                {1.0d, 0.0d},
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);
        ArrayList<Alternative> refined = new ArrayList<>();
        refined.add(alternatives.get(0));
        refined.add(alternatives.get(1));
        refined.add(alternatives.get(2));


        DMContext dmContext = new DMContext(Criteria.constructCriteria("C", 2, false), null,
                new Alternatives(alternatives), null, false, 1);
        interaction.refine.Result rr = new interaction.refine.Result(dmContext);
        rr._refinedAlternatives = new Alternatives(refined);

        DM[] DMs = new DM[2];
        DMs[0] = new DM(0, "DM1");
        DMs[1] = new DM(1, "DM2");

        Result r = null;
        try
        {
            r = RSC.constructReferenceSets(dmContext, DMs, rr);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(r);
        assertEquals(Status.PROCESS_ENDED_SUCCESSFULLY, r._status);


        assertNotNull(r._referenceSetsContainer);
        assertNotNull(r._referenceSetsContainer.getCommonReferenceSets());
        assertNotNull(r._referenceSetsContainer.getDMReferenceSets());
        assertEquals(2, r._referenceSetsContainer.getDMReferenceSets().size());
        assertTrue(r._referenceSetsContainer.getDMReferenceSets().containsKey(DMs[0]));
        assertTrue(r._referenceSetsContainer.getDMReferenceSets().containsKey(DMs[1]));

        assertFalse(r._terminatedDueToNotEnoughAlternatives);
        assertNull(r._terminatedDueToNotEnoughAlternativesMessage);
        assertEquals(1, r._iteration);

        assertTrue(r._processingTime >= 0);
        assertEquals(4, r._referenceSetsContainer.getNoSets());

        ReferenceSets[] rssArray = new ReferenceSets[]{r._referenceSetsContainer.getCommonReferenceSets(),
                r._referenceSetsContainer.getDMReferenceSets().get(DMs[0]),
                r._referenceSetsContainer.getDMReferenceSets().get(DMs[1])};

        int[] noSets = new int[]{1, 1, 2};
        String[][] stringRepresentations = new String[][]{
                {"Alternatives = A0, A1"},
                {"Alternatives = A1, A2"},
                {"Alternatives = A0, A2", "Alternatives = A0, A1"}
        };

        String[][][] expNames = new String[][][]
                {
                        {{"A0", "A1"}},
                        {{"A1", "A2"}},
                        {{"A0", "A2"}, {"A0", "A1"}},
                };

        for (int i = 0; i < rssArray.length; i++)
        {
            assertEquals(noSets[i], rssArray[i].getNoSets());
            assertEquals(1, rssArray[i].getUniqueSizes().length);
            assertEquals(2, rssArray[i].getUniqueSizes()[0]);
            assertEquals(noSets[i], rssArray[i].getReferenceSets().get(2).size());

            for (int j = 0; j < noSets[i]; j++)
            {
                ReferenceSet rs = rssArray[i].getReferenceSets().get(2).get(j);
                assertEquals(2, rs.getSize());
                assertEquals(stringRepresentations[i][j], rs.getStringRepresentation());
                for (int k = 0; k < 2; k++) assertEquals(expNames[i][j][k], rs.getAlternatives().get(k).getName());
            }
        }

        r.printStringRepresentation();
    }
}