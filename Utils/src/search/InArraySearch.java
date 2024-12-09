package search;

import valuewrapper.DoubleWrapper;

import java.util.ArrayList;

/**
 * Collection of procedures searching for elements in arrays.
 * The class is also implemented as generic to allow searching throughout elements extending {@link ValueGetter} double value getter class.
 *
 * @author MTomczyk
 */


@SuppressWarnings({ "DuplicatedCode"})
public class InArraySearch<E extends DoubleWrapper>
{
    /**
     * Returns an index starting from which in-array-stored values (sorted in ascending order) have a value of
     * at least ">=" (or greater ">") as the provided threshold (strict or non-strict relation).
     * The index is searched using the binary search.
     *
     * @param atLeast threshold
     * @param array   input array (array list): the values are assumed to be sorted in ascending order
     * @param strict  if true, strict relation (>) is used, (>=) relation is used if false
     * @return index (if the index exceeds the array limits, -1 or "array.length" is returned; 0 is returned if the array length is 0)

     */
    public int getIndexAtLeast(double atLeast, ArrayList<E> array, boolean strict)
    {
        if (array.isEmpty()) return -1;
        int lI = array.size() - 1;

        // check boundary conditions (left)
        // >= and the first element is greater/equal than at least
        if ((!strict) && (Double.compare(array.get(0).getValue(), atLeast) >= 0)) return 0;
            // > and the first element is greater than the threshold
        else if ((strict) && (Double.compare(array.get(0).getValue(), atLeast) > 0)) return 0;
            // check boundary conditions (right)
            // >= and the first element is greater/equal than at least
        else if ((!strict) && (Double.compare(atLeast, array.get(lI).getValue()) > 0)) return lI + 1;
            // > and the first element is greater than the threshold
        else if ((strict) && (Double.compare(atLeast, array.get(lI).getValue()) >= 0)) return lI + 1;


        // check boundary conditions (shifts)
        // >= and the last element equals threshold (shift to the left)
        if ((!strict) && ((Double.compare(array.get(lI).getValue(), atLeast) == 0)))
        {
            while ((lI - 1 >= 0) && (Double.compare(array.get(lI - 1).getValue(), atLeast) == 0)) lI--;
            return lI;
        }// > and the first element equals threshold (shift to right)
        else if ((strict) && ((Double.compare(array.get(0).getValue(), atLeast) == 0))) // equal, so shift
        {
            lI = 0;
            while ((lI + 1 < array.size()) && (Double.compare(array.get(lI).getValue(), atLeast) == 0)) lI++;
            if ((lI == array.size() - 1) && (Double.compare(array.get(lI).getValue(), atLeast) == 0)) return array.size();
            return lI;
        }

        BinarySearcher bs = new BinarySearcher(0, lI);
        int ci = bs.generateIndex();
        if (ci == 0)
        {
            bs.goRight();
            ci = bs.generateIndex();
        }

        while (
                ((!strict) && (Double.compare(array.get(ci - 1).getValue(), atLeast) >= 0)) ||
                        ((strict) && (Double.compare(array.get(ci - 1).getValue(), atLeast) > 0)) ||
                        ((!strict) && (Double.compare(array.get(ci).getValue(), atLeast) < 0)) ||
                        ((strict) && (Double.compare(array.get(ci).getValue(), atLeast) <= 0)))
        {
            if  ( ((!strict) && (Double.compare(array.get(ci - 1).getValue(), atLeast) >= 0))
                    || ((strict) && (Double.compare(array.get(ci - 1).getValue(), atLeast) > 0))) bs.goLeft();
            else bs.goRight();

            ci = bs.generateIndex();
            if (ci == 0)
            {
                bs.goRight();
                ci = bs.generateIndex();
            }
        }
        return ci;
    }


    /**
     * Returns an index starting from which in-array-stored values (sorted in ascending order) have a value of
     * at least ">=" (or greater ">") as the provided threshold (strict or non-strict relation).
     * The index is searched using the binary search.
     *
     * @param atLeast threshold
     * @param array   input array: the values are assumed to be sorted in ascending order
     * @param strict  if true, strict relation (>) is used, (>=) relation is used if false
     * @return index (if the index exceeds the array limits, -1 or "array.length" is returned; 0 is returned if the array length is 0)
     */
    public static int getIndexAtLeast(double atLeast, double[] array, boolean strict)
    {
        if (array.length == 0) return 0;
        int lI = array.length - 1;
        // check boundary conditions (left)
        // >= and the first element is greater/equal than at least
        if ((!strict) && (Double.compare(array[0], atLeast) >= 0)) return 0;
            // > and the first element is greater than the threshold
        else if ((strict) && (Double.compare(array[0], atLeast) > 0)) return 0;
            // check boundary conditions (right)
            // >= and the first element is greater/equal than at least
        else if ((!strict) && (Double.compare(atLeast, array[lI]) > 0)) return lI + 1;
            // > and the first element is greater than the threshold
        else if ((strict) && (Double.compare(atLeast, array[lI]) >= 0)) return lI + 1;


        // check boundary conditions (shifts)
        // >= and the last element equals threshold (shift to left)
        if ((!strict) && ((Double.compare(array[lI], atLeast) == 0)))
        {
            while ((lI - 1 >= 0) && (Double.compare(array[lI - 1], atLeast) == 0)) lI--;
            return lI;
        }// > and the first element equals threshold (shift to right)
        else if ((strict) && ((Double.compare(array[0], atLeast) == 0))) // equal, so shift
        {
            lI = 0;
            while ((lI + 1 < array.length) && (Double.compare(array[lI], atLeast) == 0)) lI++;
            if ((lI == array.length - 1) && (Double.compare(array[lI], atLeast) == 0)) return array.length;
            return lI;
        }

        BinarySearcher bs = new BinarySearcher(0, lI);
        int ci = bs.generateIndex();
        if (ci == 0)
        {
            bs.goRight();
            ci = bs.generateIndex();
        }

        while (
                ((!strict) && (Double.compare(array[ci - 1], atLeast) >= 0)) ||
                        ((strict) && (Double.compare(array[ci - 1], atLeast) > 0)) ||
                        ((!strict) && (Double.compare(array[ci], atLeast) < 0)) ||
                        ((strict) && (Double.compare(array[ci], atLeast) <= 0)))
        {
            if  ( ((!strict) && (Double.compare(array[ci - 1], atLeast) >= 0))
                    || ((strict) && (Double.compare(array[ci - 1], atLeast) > 0))) bs.goLeft();
            else bs.goRight();

            ci = bs.generateIndex();
            if (ci == 0)
            {
                bs.goRight();
                ci = bs.generateIndex();
            }
        }
        return ci;
    }


    /**
     * Returns an index up to which in-array-stored values (sorted in ascending order) have a value smaller &lt; or no greater &lt;=
     * than the provided threshold (strict or non-strict relation). The index is searched using the binary search.
     *
     * @param atMost threshold
     * @param array  input array (array list): the values are assumed to be sorted in ascending order
     * @param strict  if true, strict relation &lt; is used, &lt;= relation is used if false
     * @return index (if the index exceeds the array limits, -1 or "array.length" is returned; 0 is returned if the array length is 0)
     */
    public int getIndexAtMost(double atMost, ArrayList<E> array, boolean strict)
    {
        if (array.isEmpty()) return -1;
        int lI = array.size() - 1;
        // check boundary conditions (right)
        // last element smaller/equal to the threshold
        if ((!strict) && (Double.compare(array.get(lI).getValue(), atMost) <= 0)) return lI;
            // last element smaller than the threshold
        else if ((strict) && (Double.compare(array.get(lI).getValue(), atMost) < 0)) return lI;
            // first element smaller/equal to the threshold
        else if ((!strict) && (Double.compare(atMost, array.get(0).getValue()) < 0)) return -1;
            // first element greater than the threshold
        else if ((strict) && (Double.compare(atMost, array.get(0).getValue()) <= 0)) return -1;

        // shift to the left
        if ((strict) && (Double.compare(array.get(lI).getValue(), atMost) == 0))
        {
            while ((lI - 1 >= 0) && (Double.compare(array.get(lI).getValue(), atMost) == 0)) lI--;
            if (((lI == 0) && (Double.compare(array.get(lI).getValue(), atMost) == 0))) return -1;
            return lI;
        }

        // shift to the right
        else if ((!strict) && (Double.compare(array.get(0).getValue(), atMost) == 0))
        {
            lI = 0;
            while ((lI + 1 < array.size()) && (Double.compare(array.get(lI + 1).getValue(), atMost) == 0)) lI++;
            return lI;
        }

        BinarySearcher bs = new BinarySearcher(0, array.size() - 1);
        int ci = bs.generateIndex();
        if (ci == lI)
        {
            bs.goLeft();
            ci = bs.generateIndex();
        }


        while (
                ((!strict) && Double.compare(array.get(ci + 1).getValue(), atMost) <= 0) ||
                        ((strict) && Double.compare(array.get(ci + 1).getValue(), atMost) < 0) ||
                        ((!strict) && Double.compare(array.get(ci).getValue(), atMost) > 0) ||
                        ((strict) && Double.compare(array.get(ci).getValue(), atMost) >= 0)
        )
        {
            if   (((!strict) && Double.compare(array.get(ci + 1).getValue(), atMost) <= 0) ||
                    ((strict) && Double.compare(array.get(ci + 1).getValue(), atMost) < 0)) bs.goRight();
            else bs.goLeft();

            ci = bs.generateIndex();
            if (ci == lI)
            {
                bs.goLeft();
                ci = bs.generateIndex();
            }
        }
        return ci;
    }



    /**
     * Returns an index up to which in-array-stored values (sorted in ascending order) have a value smaller &lt; or no greater &lt;=
     * than the provided threshold (strict or non-strict relation). The index is searched using the binary search.
     *
     * @param atMost threshold
     * @param array  input array: the values are assumed to be sorted in ascending order
     * @param strict  if true, strict relation &lt; is used, &lt;= relation is used if false
     * @return index (if the index exceeds the array limits, -1 or "array.length" is returned; 0 is returned if the array length is 0)

     */
    public static int getIndexAtMost(double atMost, double[] array,  boolean strict)
    {
        if (array.length == 0) return -1;
        int lI = array.length - 1;
        // check boundary conditions (right)
        // last element smaller/equal to the threshold
        if ((!strict) && (Double.compare(array[lI], atMost) <= 0)) return lI;
        // last element smaller than the threshold
        else if ((strict) && (Double.compare(array[lI], atMost) < 0)) return lI;
        // first element smaller/equal to the threshold
        else if ((!strict) && (Double.compare(atMost, array[0]) < 0)) return -1;
        // first element greater than the threshold
        else if ((strict) && (Double.compare(atMost, array[0]) <= 0)) return -1;

        // shift to the left
        if ((strict) && (Double.compare(array[lI], atMost) == 0))
        {
            while ((lI - 1 >= 0) && (Double.compare(array[lI], atMost) == 0)) lI--;
            if (((lI == 0) && (Double.compare(array[lI], atMost) == 0))) return -1;
            return lI;
        }

        // shift to the right
        else if ((!strict) && (Double.compare(array[0], atMost) == 0))
        {
            lI = 0;
            while ((lI + 1 < array.length) && (Double.compare(array[lI + 1], atMost) == 0)) lI++;
            return lI;
        }

        BinarySearcher bs = new BinarySearcher(0, array.length - 1);
        int ci = bs.generateIndex();
        if (ci == lI)
        {
            bs.goLeft();
            ci = bs.generateIndex();
        }


        while (
                ((!strict) && Double.compare(array[ci + 1], atMost) <= 0) ||
                ((strict) && Double.compare(array[ci + 1], atMost) < 0) ||
                 ((!strict) && Double.compare(array[ci], atMost) > 0) ||
                        ((strict) && Double.compare(array[ci], atMost) >= 0)
        )
        {
            if   (((!strict) && Double.compare(array[ci + 1], atMost) <= 0) ||
            ((strict) && Double.compare(array[ci + 1], atMost) < 0)) bs.goRight();
            else bs.goLeft();

            ci = bs.generateIndex();
            if (ci == lI)
            {
                bs.goLeft();
                ci = bs.generateIndex();
            }
        }
        return ci;
    }


}
