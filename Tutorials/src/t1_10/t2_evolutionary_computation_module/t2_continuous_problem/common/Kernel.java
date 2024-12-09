package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common;

/**
 * Simple kernel function defined as: amplitude * e^{-(distance multiplier * |x-coordinates, center coordinates|)^2}.
 *
 * @author MTomczyk
 */


public class Kernel
{
    /**
     * Amplitude.
     */
    private final double _a;

    /**
     * Distance multiplier.
     */
    private final double _dm;

    /**
     * Center x-coordinate.
     */
    private final double _cx1;

    /**
     * Center y-coordinate.
     */
    private final double _cx2;

    /**
     * Parameterized constructor.
     *
     * @param a  amplitude
     * @param dm distance multiplier
     * @param cx1 center x1-coordinate
     * @param cx2 center x2-coordinate
     */
    public Kernel(double a, double dm, double cx1, double cx2)
    {
        _a = a;
        _dm = dm;
        _cx1 = cx1;
        _cx2 = cx2;
    }

    /**
     * Evaluates the kernel function.
     *
     * @param x1 x1-coordinate
     * @param x2 x2-coordinate
     * @return value
     */
    public double evaluate(double x1, double x2)
    {
        double dx1 = x1 - _cx1;
        double dx2 = x2 - _cx2;
        double dist = _dm * _dm * (dx1*dx1+dx2*dx2);
        return _a * Math.pow(Math.E, -dist);
    }
}
