package random;


import java.util.Arrays;

/**
 * Provides an auxiliary method that divides (distributes) an integer among receivers with a uniform distribution. E.g.,
 * there is a number 10. The goal can be to split it three times (producing integers). The method's complexity is
 * pseudo-polynomial and depends on (integer x number of splits).
 *
 * @author MTomczyk
 */
public class IntegerDivider
{
    /**
     * Splits an integer number a desired number of times, producing integer numbers that sum to the input number.
     * The integer values each receiver can take are obtained from a uniform distribution.
     *
     * @param number input number to be split (non-negative)
     * @param splits the number of splits
     * @param R      random number generator
     * @return splits represented as an array (returns null if the inputs are incorrect)
     */
    public static int[] split(int number, int splits, IRandom R)
    {
        return split(number, splits, R, false);
    }

    /**
     * Splits an integer number a desired number of times, producing integer numbers that sum to the input number.
     * The integer values each receiver can take are obtained from a uniform distribution.
     *
     * @param number     input number to be split (non-negative)
     * @param splits     the number of splits
     * @param R          random number generator
     * @param atLeastOne if true, each of the splits will be equal at least 1  (hence, the total number of splits should be positive,
     *                   smaller equal than the input number).
     * @return splits represented as an array (returns null if the inputs are incorrect)
     */
    public static int[] split(int number, int splits, IRandom R, boolean atLeastOne)
    {
        if (R == null) return null;
        if (number < 0) return null;
        if (splits < 1) return null;
        if ((atLeastOne) && (splits > number)) return null;

        int[] r = new int[splits];
        int remains = number;

        if (atLeastOne)
        {
            remains -= splits;
            Arrays.fill(r, 1);
            if (remains == 0) return r;
        }

        if (splits == 1)
        {
            r[0] += remains;
            return r;
        }
        else if (splits == 2)
        {
            int add = R.nextInt(remains + 1);
            r[0] += add;
            r[1] += (remains - add);
            return r;
        }

        // combinatorial approach
        int[][] dm = new int[splits][remains + 1];
        for (int i = 0; i < remains + 1; i++) dm[0][i] = 1;
        for (int i = 0; i < splits; i++) dm[i][0] = 1;
        for (int s = 1; s < splits; s++)
            for (int d = 1; d < remains + 1; d++)
                dm[s][d] = dm[s - 1][d] + dm[s][d - 1];
   //     PrintUtils.print2dIntegers(dm);

        int s = splits;
        int d = remains;
        int idx = 0;
        int a = 0;

        while (remains > 0)
        {
          //  System.out.println();
           // System.out.println("accu = " + a);
         //   System.out.println("idx = " + idx);
          //  System.out.println("remains = " + remains);


            int up = dm[s - 2][d];
            boolean goUp = true;
          //  System.out.println("s = " + s);
           // System.out.println("d = " + d);

           // System.out.println("up = " + up);
            if (d > 0)
            {
                int left = dm[s - 1][d - 1];
              //  System.out.println("left = " + left);
                goUp = R.nextInt(up + left) < up;
            }

           // System.out.println("up? = " + goUp);

            if (goUp)
            {
                s--;
                remains -= a;
                r[idx++] += a;
                a = 0;

                if (s < 2)
                {
                    r[r.length - 1] += remains;
                    remains = 0;
                }
            }
            else
            {
                d--;
                a++;
            }

          //  PrintUtils.printVectorOfIntegers(r);
        }

      //  PrintUtils.printVectorOfIntegers(r);
        return r;
    }
}
