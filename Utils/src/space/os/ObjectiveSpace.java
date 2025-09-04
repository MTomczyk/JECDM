package space.os;

import print.PrintUtils;
import space.Range;
import space.Vector;

/**
 * Class maintaining data on the objective space. When used in the EMO context,
 * the objective space ranges are often spanned based on the non-dominated solutions.
 *
 * @author MTomczyk
 */
public class ObjectiveSpace
{
    /**
     * Utopia point.
     */
    public final double[] _utopia;

    /**
     * Nadir point.
     */
    public final double[] _nadir;

    /**
     * Objective space ranges (spanned over the utopia and nadir points).
     */
    public final Range[] _ranges;

    /**
     * Criteria types: true = gain; false = cost. It impacts the position of the utopia/nadir point.
     */
    public final boolean[] _criteriaTypes;

    /**
     * Parameterized constructor.
     *
     * @param ranges        objective space ranges
     * @param criteriaTypes types (gain/cost) of the criteria
     */
    public ObjectiveSpace(Range[] ranges, boolean[] criteriaTypes)
    {
        _utopia = new double[ranges.length];
        _nadir = new double[ranges.length];
        _ranges = ranges;
        _criteriaTypes = criteriaTypes;

        for (int i = 0; i < ranges.length; i++)
        {
            if ((criteriaTypes != null) && (_criteriaTypes[i]))
            {
                _utopia[i] = ranges[i].getRight();
                _nadir[i] = ranges[i].getLeft();
            }
            else
            {
                _utopia[i] = ranges[i].getLeft();
                _nadir[i] = ranges[i].getRight();
            }
        }
    }

    /**
     * Parameterized constructor. Criteria types are based on the utopia-nadir relationship.
     *
     * @param utopia utopia point
     * @param nadir  nadir point
     */
    public ObjectiveSpace(double[] utopia, double[] nadir)
    {
        _utopia = utopia;
        _nadir = nadir;
        _criteriaTypes = new boolean[utopia.length];
        _ranges = new Range[utopia.length];
        for (int i = 0; i < utopia.length; i++)
        {
            if (Double.compare(utopia[i], nadir[i]) < 0)
                _ranges[i] = new Range(utopia[i], nadir[i]);
            else
            {
                _ranges[i] = new Range(nadir[i], utopia[i]);
                _criteriaTypes[i] = true;
            }
        }
    }

    /**
     * Parameterized constructor. Criteria types are set to false.
     *
     * @param utopia utopia point
     * @param nadir  nadir point
     * @param ranges objective space ranges
     */
    public ObjectiveSpace(double[] utopia, double[] nadir, Range[] ranges)
    {
        _utopia = utopia;
        _nadir = nadir;
        _ranges = ranges;
        _criteriaTypes = new boolean[utopia.length];
    }

    /**
     * Parameterized constructor.
     *
     * @param utopia        utopia point
     * @param nadir         nadir point
     * @param ranges        objective space ranges
     * @param criteriaTypes criteria types
     */
    public ObjectiveSpace(double[] utopia, double[] nadir, Range[] ranges, boolean[] criteriaTypes)
    {
        _utopia = utopia;
        _nadir = nadir;
        _ranges = ranges;
        _criteriaTypes = criteriaTypes;
    }

    /**
     * Constructs a class instance representing an objective space spanned over the whole domain
     * (utopia = Double.POSITIVE_INFINITY and nadir = Double.NEGATIVE_INFINITY for a cost-type criterion,
     * utopia = Double.NEGATIVE_INFINITY and nadir = Double.POSITIVE_INFINITY for a cost-type criterion)
     *
     * @param criteriaTypes criteria types
     * @return class instance (null, if the input is null)
     */
    public static ObjectiveSpace getOSMaximallySpanned(boolean[] criteriaTypes)
    {
        return getOSMaximallySpanned(criteriaTypes, false);
    }

    /**
     * Constructs a class instance representing an objective space spanned over the whole domain
     * (utopia = Double.POSITIVE_INFINITY and nadir = Double.NEGATIVE_INFINITY for a cost-type criterion,
     * utopia = Double.NEGATIVE_INFINITY and nadir = Double.POSITIVE_INFINITY for a cost-type criterion)
     *
     * @param criteriaTypes      criteria types
     * @param inverseUtopiaNadir if true, utopia and nadir positions are inversed
     * @return class instance (null, if the input is null)
     */
    public static ObjectiveSpace getOSMaximallySpanned(boolean[] criteriaTypes, boolean inverseUtopiaNadir)
    {
        if (criteriaTypes == null) return null;
        double[] u = new double[criteriaTypes.length];
        double[] n = new double[criteriaTypes.length];
        Range[] ranges = new Range[criteriaTypes.length];

        for (int i = 0; i < criteriaTypes.length; i++)
        {
            ranges[i] = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (((!inverseUtopiaNadir) && (!criteriaTypes[i])) ||
                    ((inverseUtopiaNadir) && (criteriaTypes[i]))
            )
            {
                u[i] = Double.NEGATIVE_INFINITY;
                n[i] = Double.POSITIVE_INFINITY;
            }
            else
            {
                u[i] = Double.POSITIVE_INFINITY;
                n[i] = Double.NEGATIVE_INFINITY;
            }
        }

        return new ObjectiveSpace(u, n, ranges, criteriaTypes);
    }

    /**
     * Checks if this object is equal to the other one.
     *
     * @param os the other object
     * @return true = both objects represent the same objective space; false = otherwise
     */
    public boolean isEqual(ObjectiveSpace os)
    {
        if (!Vector.areVectorsEqual(_utopia, os._utopia)) return false;
        if (!Vector.areVectorsEqual(_nadir, os._nadir)) return false;
        if (((_ranges == null) && (os._ranges != null)) || ((_ranges != null) && (os._ranges == null))) return false;
        if (_ranges == null) return true;
        if (_ranges.length != os._ranges.length) return false;
        if ((_criteriaTypes != null) && (os._criteriaTypes == null)) return false;
        if ((os._criteriaTypes != null) && (_criteriaTypes == null)) return false;

        if (_criteriaTypes != null)
        {
            if (_criteriaTypes.length != os._criteriaTypes.length) return false;
            for (int i = 0; i < _criteriaTypes.length; i++) if (_criteriaTypes[i] != os._criteriaTypes[i]) return false;
        }
        for (int i = 0; i < _ranges.length; i++)
            if (!_ranges[i].isEqual(os._ranges[i])) return false;
        return true;
    }

    /**
     * Constructs a deep copy of the object.
     *
     * @return constructed copy
     */
    public ObjectiveSpace getClone()
    {
        Range[] ranges = null;
        if (_ranges != null)
        {
            ranges = new Range[_ranges.length];
            for (int i = 0; i < _ranges.length; i++) ranges[i] = _ranges[i].getClone();
        }
        return new ObjectiveSpace(_utopia.clone(), _nadir.clone(), ranges, _criteriaTypes.clone());
    }

    /**
     * Prints info on the objective space.
     */
    public void print()
    {
        print(15);
    }


    /**
     * Prints info on the objective space.
     *
     * @param prec decimal precision used when printing doubles
     */
    public void print(int prec)
    {
        System.out.print(getStringRepresentation(prec));
    }

    /**
     * Returns the string representation (decimal precision of 4 digits is used).
     */
    @Override
    public String toString()
    {
        return getStringRepresentation(4);
    }

    /**
     * Returns the string representation.
     *
     * @param prec decimal precision used when printing doubles
     * @return string representation
     */
    public String getStringRepresentation(int prec)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Utopia point = ");
        sb.append(PrintUtils.getVectorOfDoubles(_utopia, prec));
        sb.append(System.lineSeparator());

        sb.append("Nadir point = ");
        sb.append(PrintUtils.getVectorOfDoubles(_nadir, prec));
        sb.append(System.lineSeparator());

        sb.append("Ranges data:").append(System.lineSeparator());
        String rule = "Left = %." + prec + "f; Right = %." + prec + "f ";
        for (Range v : _ranges)
            sb.append(String.format(rule, v.getLeft(), v.getRight())).append(System.lineSeparator());
        sb.append("Criteria types:").append(System.lineSeparator());
        for (int t = 0; t < _criteriaTypes.length; t++)
        {
            if (_criteriaTypes[t]) sb.append("Maximization");
            else sb.append("Minimization");
            if (t < _criteriaTypes.length - 1) sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
