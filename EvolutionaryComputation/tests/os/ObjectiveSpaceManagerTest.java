package os;

import alternative.Alternative;
import criterion.Criteria;
import ea.EATimestamp;
import ea.IEA;
import exception.PhaseException;
import org.junit.jupiter.api.Test;
import population.Specimen;
import population.SpecimenID;
import population.SpecimensContainer;
import space.Range;
import space.normalization.INormalization;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import space.normalization.minmax.AbstractMinMaxNormalization;
import space.os.ObjectiveSpace;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provide various tests for {@link ObjectiveSpaceManager}
 *
 * @author MTomczyk
 */
class ObjectiveSpaceManagerTest
{
    /**
     * Creates and returns default OS (with null values).
     *
     * @param criteriaType criteria type
     * @return default OS.
     */
    private static os.ObjectiveSpace getOSNull(boolean criteriaType)
    {
        return new os.ObjectiveSpace(null, null, null, new boolean[]{criteriaType, criteriaType});
    }

    /**
     * Creates and returns default OS (with not null values).
     *
     * @param criteriaType criteria type
     * @return default OS.
     */
    @SuppressWarnings("SameParameterValue")
    private static os.ObjectiveSpace getOSNotNull(boolean criteriaType)
    {
        return getOSNotNull(criteriaType, criteriaType);
    }

    /**
     * Creates and returns default OS (with not null values).
     *
     * @param g1 gain flag for the first criterion
     * @param g2 gain flag for the second criterion
     * @return default OS.
     */
    @SuppressWarnings("SameParameterValue")
    private static os.ObjectiveSpace getOSNotNull(boolean g1, boolean g2)
    {
        double[] u = new double[]{1.0d, 2.0d};
        double[] n = new double[]{2.0d, 3.0d};

        if (g1)
        {
            u[0] = 2.0d;
            n[0] = 1.0d;
        }

        if (g2)
        {
            u[1] = 3.0d;
            n[1] = 2.0d;
        }

        return new os.ObjectiveSpace(u, n, new Range[]{new Range(1.0d, 2.0d),
                new Range(2.0d, 3.0d)}, new boolean[]{g1, g2});
    }


    /**
     * Creates the specimen array based on the input evaluation matrix.
     *
     * @param e evaluation matrix
     * @return specimen array
     */
    private static ArrayList<Specimen> getSpecimens(double[][] e)
    {
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);
        ArrayList<Specimen> specimens = new ArrayList<>(e.length);
        for (int i = 0; i < e.length; i++)
        {
            Specimen S = new Specimen(2, new SpecimenID(0, 0, 0, i));
            S.setAlternative(alternatives.get(i));
            specimens.add(S);
        }
        return specimens;
    }


    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(null);
        pOS._doNotUpdateOS = true;
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        String msg = null;
        try
        {
            assertFalse(OSM.update(null, new EATimestamp(0, 0), true));
        } catch (PhaseException e)
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
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._doNotUpdateOS = true;
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        String msg = null;
        try
        {
            assertFalse(OSM.update(null, new EATimestamp(0, 0), true));

        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._doNotUpdateOS = false;
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        String msg = null;
        try
        {
            assertTrue(OSM.update(null, new EATimestamp(0, 0), true));


        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The calculated range for the 0-th objective is invalid (left = Infinity ; right = -Infinity)", msg);
    }

    /**
     * Dummy OS changed listener.
     */
    public static class OSCL implements IOSChangeListener
    {
        /**
         * Constructed normalizations.
         */
        public INormalization[] _normalizations;

        /**
         * Dummy action
         *
         * @param ea     evolutionary algorithm
         * @param os     objective space (updated)
         * @param prevOS objective space (outdated; for comparison)
         * @throws PhaseException do not throw an exception
         */
        @Override
        public void action(IEA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
        {
            INormalizationBuilder NB = new StandardLinearBuilder();
            _normalizations = NB.getNormalizations(os);
        }
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM.getOS()._ranges);
            assertNull(OSM.getOS()._utopia);
            assertNull(OSM.getOS()._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(0.0d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(0.0d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(0.4d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(0.4d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._nadir[1], 1.0E-6);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(1.1d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(1.6d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(1.9d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(1.1d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(1.6d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(0.5d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(1.9d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(2.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(0.1d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(1.1d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(1.9d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(1.6d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(2.0d, OSM.getOS()._nadir[1], 1.0E-6);

        } catch (PhaseException e)
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
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNull(true));
        pOS._criteria = Criteria.constructCriteria("C", 2, true);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM.getOS()._ranges);
            assertNull(OSM.getOS()._utopia);
            assertNull(OSM.getOS()._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(0.0d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(0.0d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(0.4d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(0.4d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._nadir[1], 1.0E-6);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(1.1d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(1.6d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(1.9d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(1.1d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(1.6d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(0.5d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(1.9d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(2.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(0.1d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(1.6d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(2.0d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(1.1d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(1.9d, OSM.getOS()._nadir[1], 1.0E-6);

        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }


    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM.getOS()._ranges);
            assertNull(OSM.getOS()._utopia);
            assertNull(OSM.getOS()._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(3.0d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(2.6d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(3.2d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(0.4d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(3.0d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._nadir[1], 1.0E-6);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(1.6d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(1.6d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(1.2d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(2.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(1.2d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(0.4d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(1.6d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(2.0d, OSM.getOS()._nadir[1], 1.0E-6);

        } catch (PhaseException e)
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
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = true;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM.getOS()._ranges);
            assertNull(OSM.getOS()._utopia);
            assertNull(OSM.getOS()._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(3.0d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(2.6d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(3.2d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(0.4d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(3.0d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._nadir[1], 1.0E-6);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(1.1d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(1.9d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(1.1d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(3.0d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(1.9d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(1.9d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(2.1d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(1.1d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(1.9d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(3.0d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._nadir[1], 1.0E-6);

        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }


    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = true;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM.getOS()._ranges);
            assertNull(OSM.getOS()._utopia);
            assertNull(OSM.getOS()._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(3.0d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(2.6d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(3.2d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(0.4d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(3.0d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._nadir[1], 1.0E-6);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertFalse(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(3.0d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(2.6d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(3.2d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(0.4d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(3.0d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._nadir[1], 1.0E-6);

        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNull(true));
        pOS._criteria = Criteria.constructCriteria("C", 2, true);
        pOS._updateNadirUsingIncumbent = true;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM.getOS()._ranges);
            assertNull(OSM.getOS()._utopia);
            assertNull(OSM.getOS()._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(3.0d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(2.6d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(3.2d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(3.0d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(0.4d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._nadir[1], 1.0E-6);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertFalse(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(3.0d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(2.6d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(3.2d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(3.0d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(4.0d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(0.4d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(0.8d, OSM.getOS()._nadir[1], 1.0E-6);

        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = true;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(1.0d, OSM.getOS()._ranges[0].getLeft());
            assertEquals(2.0d, OSM.getOS()._ranges[0].getRight());
            assertEquals(2.0d, OSM.getOS()._ranges[1].getLeft());
            assertEquals(3.0d, OSM.getOS()._ranges[1].getRight());
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(1.0d, OSM.getOS()._utopia[0]);
            assertEquals(2.0d, OSM.getOS()._utopia[1]);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(2.0d, OSM.getOS()._nadir[0]);
            assertEquals(3.0d, OSM.getOS()._nadir[1]);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 3.8d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
            assertEquals(3.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);

            assertNotNull(OSM.getOS()._ranges);
            assertEquals(2, OSM.getOS()._ranges.length);
            assertEquals(0.4d, OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(2.0d, OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(1.6d, OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
            assertEquals(2.0d, OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(3.8d, OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(1.8d, OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
            assertNotNull(OSM.getOS()._utopia);
            assertEquals(2, OSM.getOS()._utopia.length);
            assertEquals(0.4d, OSM.getOS()._utopia[0], 1.0E-6);
            assertEquals(2.0d, OSM.getOS()._utopia[1], 1.0E-6);
            assertNotNull(OSM.getOS()._nadir);
            assertEquals(2, OSM.getOS()._nadir.length);
            assertEquals(2.0d, OSM.getOS()._nadir[0], 1.0E-6);
            assertEquals(3.8d, OSM.getOS()._nadir[1], 1.0E-6);


        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 11.
     */
    @Test
    void test11()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.5d, 2.5d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            examineOS(L, OSM, new double[]{1.5d, 2.5d}, new double[]{1.5d, 2.5d}, false, false);
        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 12.
     */
    @Test
    void test12()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.5d, 2.5d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));
            examineOS(L, OSM, new double[]{1.0d, 2.0d}, new double[]{1.0d, 2.0d}, false, false);
        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 13.
     */
    @Test
    void test13()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.5d, 2.0d}, {1.0d, 2.5d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));
            examineOS(L, OSM, new double[]{1.0d, 2.0d}, new double[]{1.5d, 2.5d}, false, false);
        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 14.
     */
    @Test
    void test14()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = true;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._deriveNadirFromVectorsFormingUtopia = false;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.5d, 2.0d}, {1.0d, 2.5d}}));
            assertFalse(OSM.update(SC, new EATimestamp(0, 0), true));
            examineOS(null, OSM, new double[]{1.0d, 2.0d}, new double[]{2.0d, 3.0d}, false, false);
        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 15.
     */
    @Test
    void test15()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.25d, 2.25d}, {1.5d, 0.5d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));
            examineOS(L, OSM, new double[]{1.0d, 0.5d}, new double[]{1.5d, 2.0d}, false, false);
        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 16.
     */
    @Test
    void test16()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.25d, 2.25d}, {1.5d, 0.5d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));
            examineOS(L, OSM, new double[]{1.25d, 0.5d}, new double[]{1.5d, 2.25d}, false, false);
        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 17.
     */
    @Test
    void test17()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(true));
        pOS._criteria = Criteria.constructCriteria("C", 2, true);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.25d, 2.25d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));
            examineOS(L, OSM, new double[]{1.25d, 2.25d}, new double[]{1.25d, 2.25d}, true, true);
        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 18.
     */
    @Test
    void test18()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(true));
        pOS._criteria = Criteria.constructCriteria("C", 2, true);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.25d, 2.25d}, new double[]{0.0d, 0.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));
            examineOS(L, OSM, new double[]{2.0d, 3.0d}, new double[]{2.0d, 3.0d}, true, true);
        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * Test 19.
     */
    @Test
    void test19()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(true));
        pOS._criteria = Criteria.constructCriteria("C", 2, true);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.25d, 2.25d}, new double[]{0.0d, 0.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));
            examineOS(L, OSM, new double[]{1.25d, 2.25d}, new double[]{1.25d, 2.25d}, true, true);

        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 20.
     */
    @Test
    void test20()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(true));
        pOS._criteria = Criteria.constructCriteria("C", 2, true);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{3.0d, 2.0d}, new double[]{1.0d, 4.0d}}));
                boolean f = OSM.update(SC, new EATimestamp(0, 0), true);
                OSM.getOS().print();
                assertTrue(f);
                examineOS(L, OSM, new double[]{3.0d, 4.0d}, new double[]{1.0d, 2.0d}, true, true);
            }
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{2.0d, 2.0d}, new double[]{3.0d, 3.0d}}));
                boolean f = OSM.update(SC, new EATimestamp(1, 0), true);
                OSM.getOS().print();
                assertFalse(f);
            }

        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 21.
     */
    @Test
    void test21()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{2.0d, 2.0d}, {1.0d, 3.0d}}));
                boolean f = OSM.update(SC, new EATimestamp(0, 0), true);
                assertFalse(f);
            }
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{1.5d, 1.0d}}));
                boolean f = OSM.update(SC, new EATimestamp(1, 0), true);
                assertTrue(f);
                examineOS(L, OSM, new double[]{1.0d, 1.0d}, new double[]{1.5d, 3.0d}, false, false);
            }
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{0.0d, 1.5d}}));
                boolean f = OSM.update(SC, new EATimestamp(1, 0), true);
                assertTrue(f);
                examineOS(L, OSM, new double[]{0.0d, 1.0d}, new double[]{1.5d, 1.5d}, false, false);
            }


        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * Test 22.
     */
    @Test
    void test22()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(false));
        pOS._criteria = Criteria.constructCriteria("C", 2, false);
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{2.0d, 2.0d}, {1.0d, 3.0d}}));
                boolean f = OSM.update(SC, new EATimestamp(0, 0), true);
                assertFalse(f);
            }
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{1.5d, 1.0d}}));
                boolean f = OSM.update(SC, new EATimestamp(1, 0), true);
                assertTrue(f);
                examineOS(L, OSM, new double[]{1.5d, 1.0d}, new double[]{1.5d, 1.0d}, false, false);
            }

        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 23.
     */
    @Test
    void test23()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(true, false));
        pOS._criteria = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{true, false});
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = true;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{1.0d, 2.0d}, {2.0d, 3.0d}}));
                boolean f = OSM.update(SC, new EATimestamp(0, 0), true);
                assertFalse(f);
            }
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{3.0d , 2.5d}}));
                boolean f = OSM.update(SC, new EATimestamp(1, 0), true);
                assertTrue(f);
                examineOS(L, OSM, new double[]{3.0d, 2.0d}, new double[]{1.0d, 2.5d}, true, false);
            }
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{1.5d, 0.0d}}));
                boolean f = OSM.update(SC, new EATimestamp(1, 0), true);
                assertTrue(f);
                examineOS(L, OSM, new double[]{3.0d, 0.0d}, new double[]{1.5d, 2.5d}, true, false);
            }

        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test 24.
     */
    @Test
    void test24()
    {
        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(getOSNotNull(true, false));
        pOS._criteria = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{true, false});
        pOS._updateNadirUsingIncumbent = false;
        pOS._updateUtopiaUsingIncumbent = false;
        pOS._deriveNadirFromVectorsFormingUtopia = true;
        pOS._doNotUpdateOS = false;

        OSCL L = new OSCL();
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOS);
        OSM.clearOSChangeListeners();
        OSM.addOSChangeListeners(new IOSChangeListener[]{L});

        try
        {
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{1.0d, 2.0d}, {2.0d, 3.0d}}));
                boolean f = OSM.update(SC, new EATimestamp(0, 0), true);
                assertFalse(f);
            }
            {
                SpecimensContainer SC = new SpecimensContainer();
                SC.setPopulation(getSpecimens(new double[][]{{3.0d , 2.5d}}));
                boolean f = OSM.update(SC, new EATimestamp(1, 0), true);
                assertTrue(f);
                examineOS(L, OSM, new double[]{3.0d , 2.5d}, new double[]{3.0d , 2.5d}, true, false);
            }

        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * Auxiliary method for examining OSM
     *
     * @param L   test objective space manager listener
     * @param OSM objective space manager
     * @param u   utopia point
     * @param n   nadir point
     * @param g1  gain flag for the first criterion
     * @param g2  gain flag for the second criterion
     */
    protected void examineOS(OSCL L, ObjectiveSpaceManager OSM, double[] u, double[] n, boolean g1, boolean g2)
    {
        if (L != null)
        {
            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);

            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[0]);
            assertInstanceOf(AbstractMinMaxNormalization.class, L._normalizations[1]);

            if (g1)
            {
                assertEquals(n[0], ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
                assertEquals(u[0], ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            } else
            {
                assertEquals(u[0], ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 1.0E-6);
                assertEquals(n[0], ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 1.0E-6);
            }

            if (g2)
            {
                assertEquals(n[1], ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
                assertEquals(u[1], ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);
            } else
            {
                assertEquals(u[1], ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 1.0E-6);
                assertEquals(n[1], ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 1.0E-6);
            }
        }

        assertNotNull(OSM.getOS()._ranges);
        assertEquals(2, OSM.getOS()._ranges.length);

        if (g1)
        {
            assertEquals(n[0], OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(u[0], OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(u[0] - n[0], OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
        } else
        {
            assertEquals(u[0], OSM.getOS()._ranges[0].getLeft(), 1.0E-6);
            assertEquals(n[0], OSM.getOS()._ranges[0].getRight(), 1.0E-6);
            assertEquals(n[0] - u[0], OSM.getOS()._ranges[0].getInterval(), 1.0E-6);
        }

        if (g2)
        {
            assertEquals(n[1], OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(u[1], OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(u[1] - n[1], OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
        } else
        {
            assertEquals(u[1], OSM.getOS()._ranges[1].getLeft(), 1.0E-6);
            assertEquals(n[1], OSM.getOS()._ranges[1].getRight(), 1.0E-6);
            assertEquals(n[1] - u[1], OSM.getOS()._ranges[1].getInterval(), 1.0E-6);
        }

        assertNotNull(OSM.getOS()._utopia);
        assertEquals(2, OSM.getOS()._utopia.length);
        assertEquals(u[0], OSM.getOS()._utopia[0], 1.0E-6);
        assertEquals(u[1], OSM.getOS()._utopia[1], 1.0E-6);

        assertNotNull(OSM.getOS()._nadir);
        assertEquals(2, OSM.getOS()._nadir.length);
        assertEquals(n[0], OSM.getOS()._nadir[0], 1.0E-6);
        assertEquals(n[1], OSM.getOS()._nadir[1], 1.0E-6);
    }
}