package interaction.reference;

import alternative.Alternative;
import exeption.ReferenceSetsConstructorException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ReferenceSets}.
 *
 * @author MTomczyk
 */
class ReferenceSetsTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        String msg = null;
        try
        {
            ReferenceSets rs = ReferenceSets.createJointSet(null, null);
            assertEquals(0, rs.getNoSets());
            assertEquals(0, rs.getUniqueSizes().length);
            assertEquals(0, rs.getReferenceSets().size());

        } catch (ReferenceSetsConstructorException e)
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
        String msg = null;
        try
        {
            ReferenceSets r1;
            {
                HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();
                map.put(2, new LinkedList<>());
                map.get(2).add(new ReferenceSet(new Alternative("A0", 2), new Alternative("A1", 2)));
                map.get(2).add(new ReferenceSet(new Alternative("A2", 2), new Alternative("A3", 2)));
                r1 = new ReferenceSets(2, new int[]{2}, map);
            }

            ReferenceSets rs = ReferenceSets.createJointSet(r1, null);
            assertEquals(2, rs.getNoSets());
            assertEquals(1, rs.getUniqueSizes().length);
            assertEquals(2, rs.getUniqueSizes()[0]);
            assertEquals(1, rs.getReferenceSets().size());
            assertTrue(rs.getReferenceSets().containsKey(2));
            assertEquals(2, rs.getReferenceSets().get(2).size());
            assertEquals("Alternatives = A0, A1", rs.getReferenceSets().get(2).getFirst().getStringRepresentation());
            assertEquals("Alternatives = A2, A3", rs.getReferenceSets().get(2).get(1).getStringRepresentation());

        } catch (ReferenceSetsConstructorException e)
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
        String msg = null;
        try
        {
            ReferenceSets r2;
            {
                HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();
                map.put(1, new LinkedList<>());
                map.get(1).add(new ReferenceSet(new Alternative("A4", 2)));
                map.put(3, new LinkedList<>());
                map.get(3).add(new ReferenceSet(new Alternative("A6", 2), new Alternative("A7", 2), new Alternative("A8", 2)));

                r2 = new ReferenceSets(2, new int[]{1, 3}, map);
            }

            System.out.println(r2.getReferenceSets().get(3).getFirst());

            ReferenceSets rs = ReferenceSets.createJointSet(null, r2);
            assertEquals(2, rs.getNoSets());
            assertEquals(2, rs.getUniqueSizes().length);
            assertEquals(1, rs.getUniqueSizes()[0]);
            assertEquals(3, rs.getUniqueSizes()[1]);
            assertEquals(2, rs.getReferenceSets().size());
            assertTrue(rs.getReferenceSets().containsKey(1));
            assertEquals(1, rs.getReferenceSets().get(1).size());
            assertEquals("Alternatives = A4", rs.getReferenceSets().get(1).getFirst().getStringRepresentation());
            assertTrue(rs.getReferenceSets().containsKey(3));
            assertEquals(1, rs.getReferenceSets().get(3).size());
            assertEquals("Alternatives = A6, A7, A8", rs.getReferenceSets().get(3).getFirst().getStringRepresentation());

        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        String msg = null;
        try
        {
            ReferenceSets r1;
            {
                HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();
                map.put(2, new LinkedList<>());
                map.get(2).add(new ReferenceSet(new Alternative("A0", 2), new Alternative("A1", 2)));
                map.get(2).add(new ReferenceSet(new Alternative("A2", 2), new Alternative("A3", 2)));

                map.put(3, new LinkedList<>());
                map.get(3).add(new ReferenceSet(new Alternative("A11", 2), new Alternative("A12", 2),
                        new Alternative("A13", 2)));
                r1 = new ReferenceSets(3, new int[]{2, 3}, map);
            }

            ReferenceSets r2;
            {
                HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();
                map.put(1, new LinkedList<>());
                map.get(1).add(new ReferenceSet(new Alternative("A4", 2)));
                map.put(3, new LinkedList<>());
                map.get(3).add(new ReferenceSet(new Alternative("A6", 2), new Alternative("A7", 2), new Alternative("A8", 2)));

                r2 = new ReferenceSets(2, new int[]{1, 3}, map);
            }

            {
                ReferenceSets rs = ReferenceSets.createJointSet(r1, r2);
                assertEquals(5, rs.getNoSets());
                assertEquals(3, rs.getUniqueSizes().length);
                assertEquals(1, rs.getUniqueSizes()[0]);
                assertEquals(2, rs.getUniqueSizes()[1]);
                assertEquals(3, rs.getUniqueSizes()[2]);
                assertEquals(3, rs.getReferenceSets().size());

                assertTrue(rs.getReferenceSets().containsKey(1));
                assertEquals(1, rs.getReferenceSets().get(1).size());
                assertEquals("Alternatives = A4", rs.getReferenceSets().get(1).getFirst().getStringRepresentation());

                assertTrue(rs.getReferenceSets().containsKey(2));
                assertEquals(2, rs.getReferenceSets().get(2).size());
                assertEquals("Alternatives = A0, A1", rs.getReferenceSets().get(2).getFirst().getStringRepresentation());
                assertEquals("Alternatives = A2, A3", rs.getReferenceSets().get(2).get(1).getStringRepresentation());

                assertTrue(rs.getReferenceSets().containsKey(3));
                assertEquals(2, rs.getReferenceSets().get(3).size());
                assertEquals("Alternatives = A11, A12, A13", rs.getReferenceSets().get(3).getFirst().getStringRepresentation());
                assertEquals("Alternatives = A6, A7, A8", rs.getReferenceSets().get(3).get(1).getStringRepresentation());
            }

            {
                ReferenceSets rs = ReferenceSets.createJointSet(r2, r1);
                assertEquals(5, rs.getNoSets());
                assertEquals(3, rs.getUniqueSizes().length);
                assertEquals(1, rs.getUniqueSizes()[0]);
                assertEquals(2, rs.getUniqueSizes()[1]);
                assertEquals(3, rs.getUniqueSizes()[2]);
                assertEquals(3, rs.getReferenceSets().size());

                assertTrue(rs.getReferenceSets().containsKey(1));
                assertEquals(1, rs.getReferenceSets().get(1).size());
                assertEquals("Alternatives = A4", rs.getReferenceSets().get(1).getFirst().getStringRepresentation());

                assertTrue(rs.getReferenceSets().containsKey(2));
                assertEquals(2, rs.getReferenceSets().get(2).size());
                assertEquals("Alternatives = A0, A1", rs.getReferenceSets().get(2).getFirst().getStringRepresentation());
                assertEquals("Alternatives = A2, A3", rs.getReferenceSets().get(2).get(1).getStringRepresentation());

                assertTrue(rs.getReferenceSets().containsKey(3));
                assertEquals(2, rs.getReferenceSets().get(3).size());
                assertEquals("Alternatives = A6, A7, A8", rs.getReferenceSets().get(3).getFirst().getStringRepresentation());
                assertEquals("Alternatives = A11, A12, A13", rs.getReferenceSets().get(3).get(1).getStringRepresentation());
            }


        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }
}