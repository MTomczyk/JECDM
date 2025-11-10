package search;

/**
 * Simple implementation of a bi-section algorithm for finding roots.
 *
 * @author MTomczyk
 */
public class BiSection
{
    /**
     * The interface responsible for modelling the equation to be compared against 0.
     */
    public interface IEquation
    {
        /**
         * Evaluates the equation to be compared against zero
         *
         * @param x input decision variable
         * @return evaluation
         */
        double eval(double x);
    }

    /**
     * The interface responsible for delivering stopping criterion.
     */
    public interface IStop
    {
        /**
         * The stopping criterion.
         *
         * @param iteration  current iteration number
         * @param evaluation current evaluation (compare against 0)
         * @return true, if the search process should be stopped; false otherwise
         */
        boolean stop(int iteration, double evaluation);
    }

    /**
     * The interface reponsible for delivering a procedure for creating the mid-point from current bounds.
     */
    public interface IMidRule
    {
        /**
         * The method for creating the mid-point from current bounds. It is supposed that exactly one root is in the
         * provided bounds and the function is there monotonic.
         *
         * @param lb current left bound (lb &lt; rb)
         * @param rb current right bound (rb &gt; lb)
         * @return the mid-point (the default implementation returns lb + (rb - lb)/2)
         */
        default double getMidPoint(double lb, double rb)
        {
            return (lb + (rb - lb) / 2.0d);
        }
    }

    /**
     * Runs the search. It is supposed that exactly one root is in the provided bounds and the function is there
     * monotonic.
     *
     * @param lb current left bound (lb &lt; rb)
     * @param rb current right bound (rb &gt; lb)
     * @param E  evaluates the equation to be compared against zero
     * @param S  the stopping criterion.
     * @param M  the method for creating the mid-point from current bounds
     * @return the x-value in the bounds that provides the evaluation as close to zero as possible
     */
    public static double run(double lb, double rb, IEquation E, IStop S, IMidRule M)
    {
        int it = 0;
        double cx = M.getMidPoint(lb, rb);
        double ce = E.eval(cx);
        boolean inc = Double.compare(E.eval(rb), E.eval(lb)) > 0;
        while (!S.stop(it, ce))
        {
            int c = Double.compare(ce, 0.0d);
            if (inc)
            {
                if (c > 0) rb = cx;
                else if (c < 0) lb = cx;
            }
            else
            {
                if (c > 0) lb = cx;
                else if (c < 0) rb = cx;
            }
            cx = M.getMidPoint(lb, rb);
            ce = E.eval(cx);
            it++;
        }
        return cx;
    }
}
