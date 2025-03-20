package print;

/**
 * A set of auxiliary functions for printing.
 *
 * @author MTomczyk
 */


@SuppressWarnings("DuplicatedCode")
public class PrintUtils
{
    /**
     * Prints strings as lines.
     *
     * @param lines lines to be printed
     */
    public static void printLines(String[] lines)
    {
        if (lines == null) return;
        for (String l : lines) System.out.println(l);
    }

    /**
     * Constructs a string consisting of n dashes.
     *
     * @param n the number of dashes
     * @return the constructed string
     */
    public static String getDashes(int n)
    {
        return "-".repeat(Math.max(0, n));
    }

    /**
     * Constructs a string consisting of n spaces.
     *
     * @param n the number of spaces
     * @return the constructed string
     */
    public static String getSpaces(int n)
    {
        return " ".repeat(Math.max(0, n));
    }

    /**
     * Constructs a string representing a vector of doubles (separated by spaces).
     *
     * @param v    doubles
     * @param prec considered precision
     * @return the constructed string
     */
    public static String getVectorOfDoubles(double[] v, int prec)
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < v.length; i++)
        {
            String rule = "%." + prec + "f";
            if (i < v.length - 1) rule += " ";
            s.append(String.format(rule, v[i]));
        }
        return s.toString();
    }

    /**
     * Constructs a string representing a vector of doubles (separated by spaces).
     *
     * @param f    floats
     * @param prec considered precision
     * @return the constructed string
     */
    public static String getVectorOfFloats(float[] f, int prec)
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < f.length; i++)
        {
            String rule = "%." + prec + "f";
            if (i < f.length - 1) rule += " ";
            s.append(String.format(rule, f[i]));
        }
        return s.toString();
    }

    /**
     * Constructs a string representing a vector of integers (separated by spaces).
     *
     * @param v integers
     * @return the constructed string
     */
    public static String getVectorOfIntegers(int[] v)
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < v.length; i++)
        {
            String rule = "%d";
            if (i < v.length - 1) rule += " ";
            s.append(String.format(rule, v[i]));
        }
        return s.toString();
    }

    /**
     * Constructs a string representing a vector of booleans (separated by spaces).
     * Represents the flags as ``T'' (true) and ``F'' (false).
     *
     * @param v booleans
     * @return the constructed string
     */
    public static String getVectorOfBooleans(boolean[] v)
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < v.length; i++)
        {
            if (i < v.length - 1)
            {
                if (v[i]) s.append("T ");
                else s.append("F ");
            }
            else
            {
                if (v[i]) s.append("T");
                else s.append("F");
            }
        }
        return s.toString();
    }

    /**
     * Prints a string representing a vector of integers (separated by spaces).
     *
     * @param v integers
     */
    public static void printVectorOfIntegers(int[] v)
    {
        System.out.println(getVectorOfIntegers(v));
    }

    /**
     * Prints a string representing a vector of doubles (separated by spaces).
     *
     * @param v    doubles
     * @param prec considered precision
     */
    public static void printVectorOfDoubles(double[] v, int prec)
    {
        System.out.println(getVectorOfDoubles(v, prec));
    }

    /**
     * Prints a string representing a vector of floats (separated by spaces).
     *
     * @param f    floats
     * @param prec considered precision
     */
    public static void printVectorOfFloats(float[] f, int prec)
    {
        System.out.println(getVectorOfFloats(f, prec));
    }

    /**
     * Prints a string representing a vector of booleans (separated by spaces).
     * Prints the flags as ``T'' (true) and ``F'' (false).
     *
     * @param v booleans
     */
    public static void print1dBooleans(boolean[] v)
    {
        for (int j = 0; j < v.length; j++)
        {
            if (v[j]) System.out.print("T");
            else System.out.print("F");
            if (j < v.length - 1) System.out.print(" ");
        }
        System.out.println();
    }

    /**
     * Prints a 2D matrix of booleans (separated by spaces).
     *
     * @param v 2D matrix of booleans
     */
    public static void print2dBooleans(boolean[][] v)
    {
        for (boolean[] bs : v) print1dBooleans(bs);
    }

    /**
     * Prints a 2D matrix of integers (separated by spaces).
     *
     * @param v 2D matrix of integers
     */
    public static void print2dIntegers(int[][] v)
    {
        for (int[] ints : v)
        {
            for (int j = 0; j < ints.length; j++)
            {
                System.out.print(ints[j]);
                if (j < ints.length - 1) System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * Prints a 2D matrix of doubles (separated by spaces).
     *
     * @param v    doubles
     * @param prec considered precision
     */
    public static void print2dDoubles(double[][] v, int prec)
    {
        for (double[] doubles : v)
        {
            for (int j = 0; j < doubles.length; j++)
            {
                String rule = "%." + prec + "f";
                System.out.printf(rule, doubles[j]);
                if (j < doubles.length - 1) System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * Prints a 2D matrix of floats (separated by spaces).
     *
     * @param v    doubles
     * @param prec considered precision
     */
    public static void print2dFloats(float[][] v, int prec)
    {
        for (float[] floats : v)
        {
            for (int j = 0; j < floats.length; j++)
            {
                String rule = "%." + prec + "f";
                System.out.printf(rule, floats[j]);
                if (j < floats.length - 1) System.out.print(" ");
            }
            System.out.println();
        }
    }
}
