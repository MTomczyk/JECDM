package os;

import alternative.Alternative;
import criterion.Criteria;
import ea.EA;
import ea.EATimestamp;
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
    private static ObjectiveSpace getOSNull(boolean criteriaType)
    {
        return new ObjectiveSpace(null, null, null, new boolean[]{criteriaType, criteriaType});
    }

    /**
     * Creates and returns default OS (with null values).
     *
     * @param criteriaType criteria type
     * @return default OS.
     */
    @SuppressWarnings("SameParameterValue")
    private static ObjectiveSpace getOSNotNull(boolean criteriaType)
    {
        return new ObjectiveSpace(new double[]{1.0d, 2.0d}, new double[]{2.0d, 3.0d},
                new Range[]{new Range(1.0d, 2.0d), new Range(2.0d, 3.0d)},
                new boolean[]{criteriaType, criteriaType});
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
        public void action(EA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
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
        OSM.setOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM._os._ranges);
            assertNull(OSM._os._utopia);
            assertNull(OSM._os._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(0.4d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(0.0d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(0.0d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(0.4d, OSM._os._utopia[0], 0.000001d);
            assertEquals(0.8d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(0.4d, OSM._os._nadir[0], 0.000001d);
            assertEquals(0.8d, OSM._os._nadir[1], 0.000001d);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(1.1d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(1.6d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(1.9d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(1.1d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(1.6d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(0.5d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(1.9d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(2.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(0.1d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(1.1d, OSM._os._utopia[0], 0.000001d);
            assertEquals(1.9d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(1.6d, OSM._os._nadir[0], 0.000001d);
            assertEquals(2.0d, OSM._os._nadir[1], 0.000001d);

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
        OSM.setOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM._os._ranges);
            assertNull(OSM._os._utopia);
            assertNull(OSM._os._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(0.4d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(0.0d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(0.0d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(0.4d, OSM._os._utopia[0], 0.000001d);
            assertEquals(0.8d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(0.4d, OSM._os._nadir[0], 0.000001d);
            assertEquals(0.8d, OSM._os._nadir[1], 0.000001d);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(1.1d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(1.6d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(1.9d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(1.1d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(1.6d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(0.5d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(1.9d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(2.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(0.1d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(1.6d, OSM._os._utopia[0], 0.000001d);
            assertEquals(2.0d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(1.1d, OSM._os._nadir[0], 0.000001d);
            assertEquals(1.9d, OSM._os._nadir[1], 0.000001d);

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
        OSM.setOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM._os._ranges);
            assertNull(OSM._os._utopia);
            assertNull(OSM._os._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(3.0d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(2.6d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(4.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(3.2d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(0.4d, OSM._os._utopia[0], 0.000001d);
            assertEquals(0.8d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(3.0d, OSM._os._nadir[0], 0.000001d);
            assertEquals(4.0d, OSM._os._nadir[1], 0.000001d);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(1.6d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(1.6d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(1.2d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(2.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(1.2d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(0.4d, OSM._os._utopia[0], 0.000001d);
            assertEquals(0.8d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(1.6d, OSM._os._nadir[0], 0.000001d);
            assertEquals(2.0d, OSM._os._nadir[1], 0.000001d);

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
        OSM.setOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM._os._ranges);
            assertNull(OSM._os._utopia);
            assertNull(OSM._os._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(3.0d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(2.6d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(4.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(3.2d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(0.4d, OSM._os._utopia[0], 0.000001d);
            assertEquals(0.8d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(3.0d, OSM._os._nadir[0], 0.000001d);
            assertEquals(4.0d, OSM._os._nadir[1], 0.000001d);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(1.1d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(1.9d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(1.1d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(3.0d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(1.9d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(1.9d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(4.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(2.1d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(1.1d, OSM._os._utopia[0], 0.000001d);
            assertEquals(1.9d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(3.0d, OSM._os._nadir[0], 0.000001d);
            assertEquals(4.0d, OSM._os._nadir[1], 0.000001d);

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
        OSM.setOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM._os._ranges);
            assertNull(OSM._os._utopia);
            assertNull(OSM._os._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(3.0d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(2.6d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(4.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(3.2d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(0.4d, OSM._os._utopia[0], 0.000001d);
            assertEquals(0.8d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(3.0d, OSM._os._nadir[0], 0.000001d);
            assertEquals(4.0d, OSM._os._nadir[1], 0.000001d);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertFalse(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(3.0d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(2.6d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(4.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(3.2d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(0.4d, OSM._os._utopia[0], 0.000001d);
            assertEquals(0.8d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(3.0d, OSM._os._nadir[0], 0.000001d);
            assertEquals(4.0d, OSM._os._nadir[1], 0.000001d);

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
        OSM.setOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNull(OSM._os._ranges);
            assertNull(OSM._os._utopia);
            assertNull(OSM._os._nadir);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 0.8d}, {3.0d, 4.0d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(3.0d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(2.6d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(4.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(3.2d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(3.0d, OSM._os._utopia[0], 0.000001d);
            assertEquals(4.0d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(0.4d, OSM._os._nadir[0], 0.000001d);
            assertEquals(0.8d, OSM._os._nadir[1], 0.000001d);

            SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{1.1d, 2.0d}, {1.6d, 1.9d}}));
            assertFalse(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(3.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(0.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(4.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(3.0d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(2.6d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(0.8d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(4.0d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(3.2d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(3.0d, OSM._os._utopia[0], 0.000001d);
            assertEquals(4.0d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(0.4d, OSM._os._nadir[0], 0.000001d);
            assertEquals(0.8d, OSM._os._nadir[1], 0.000001d);

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
        OSM.setOSChangeListeners(new IOSChangeListener[]{L});

        String msg = null;
        try
        {
            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(1.0d, OSM._os._ranges[0].getLeft());
            assertEquals(2.0d, OSM._os._ranges[0].getRight());
            assertEquals(2.0d, OSM._os._ranges[1].getLeft());
            assertEquals(3.0d, OSM._os._ranges[1].getRight());
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(1.0d, OSM._os._utopia[0]);
            assertEquals(2.0d, OSM._os._utopia[1]);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(2.0d, OSM._os._nadir[0]);
            assertEquals(3.0d, OSM._os._nadir[1]);
            assertNull(L._normalizations);

            SpecimensContainer SC = new SpecimensContainer();
            SC.setPopulation(getSpecimens(new double[][]{{0.4d, 3.8d}}));
            assertTrue(OSM.update(SC, new EATimestamp(0, 0), true));

            assertNotNull(L._normalizations);
            assertEquals(2, L._normalizations.length);
            assertTrue(L._normalizations[0] instanceof AbstractMinMaxNormalization);
            assertTrue(L._normalizations[1] instanceof AbstractMinMaxNormalization);
            assertEquals(0.4d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMin(), 0.000001d);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[0]).getMax(), 0.000001d);
            assertEquals(2.0d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMin(), 0.000001d);
            assertEquals(3.8d, ((AbstractMinMaxNormalization) L._normalizations[1]).getMax(), 0.000001d);

            assertNotNull(OSM._os._ranges);
            assertEquals(2, OSM._os._ranges.length);
            assertEquals(0.4d, OSM._os._ranges[0].getLeft(), 0.000001d);
            assertEquals(2.0d, OSM._os._ranges[0].getRight(), 0.000001d);
            assertEquals(1.6d, OSM._os._ranges[0].getInterval(), 0.000001d);
            assertEquals(2.0d, OSM._os._ranges[1].getLeft(), 0.000001d);
            assertEquals(3.8d, OSM._os._ranges[1].getRight(), 0.000001d);
            assertEquals(1.8d, OSM._os._ranges[1].getInterval(), 0.000001d);
            assertNotNull(OSM._os._utopia);
            assertEquals(2, OSM._os._utopia.length);
            assertEquals(0.4d, OSM._os._utopia[0], 0.000001d);
            assertEquals(2.0d, OSM._os._utopia[1], 0.000001d);
            assertNotNull(OSM._os._nadir);
            assertEquals(2, OSM._os._nadir.length);
            assertEquals(2.0d, OSM._os._nadir[0], 0.000001d);
            assertEquals(3.8d, OSM._os._nadir[1], 0.000001d);


        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }
}