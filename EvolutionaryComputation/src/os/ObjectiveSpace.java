package os;

import print.PrintUtils;
import space.Range;

/**
 * Custom extension of {@link space.os.ObjectiveSpace}.
 *
 * @author MTomczyk
 */
public class ObjectiveSpace extends space.os.ObjectiveSpace
{
    /**
     * Auxiliary data on vectors that form a current utopia point (each vector was used to form the corresponding i-th utopia value).
     */
    protected double[][] _vectorsFormingUtopiaPoint;

    /**
     * Parameterized constructor.
     *
     * @param os objective space
     */
    public ObjectiveSpace(space.os.ObjectiveSpace os)
    {
        super(os._utopia, os._nadir, os._ranges, os._criteriaTypes);
    }

    /**
     * Parameterized constructor.
     *
     * @param ranges        objective space ranges
     * @param criteriaTypes types (gain/cost) of the criteria
     */
    public ObjectiveSpace(Range[] ranges, boolean[] criteriaTypes)
    {
        super(ranges, criteriaTypes);
    }

    /**
     * Parameterized constructor. Criteria types are set to false.
     *
     * @param utopia utopia point
     * @param nadir  nadir point
     */
    public ObjectiveSpace(double[] utopia, double[] nadir)
    {
        super(utopia, nadir);
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
        super(utopia, nadir, ranges);
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
        super(utopia, nadir, ranges, criteriaTypes);
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
        space.os.ObjectiveSpace os = space.os.ObjectiveSpace.getOSMaximallySpanned(criteriaTypes, inverseUtopiaNadir);
        return new ObjectiveSpace(os._utopia, os._nadir, os._ranges, os._criteriaTypes);
    }

    /**
     * Returns the string representation.
     *
     * @param prec decimal precision used when printing doubles
     * @return string representation
     */
    @Override
    public String getStringRepresentation(int prec)
    {
        StringBuilder sb = new StringBuilder();
        if (_vectorsFormingUtopiaPoint != null)
        {
            sb.append("Vectors building the utopia point:").append(System.lineSeparator());
            for (int i = 0; i < _criteriaTypes.length; i++)
            {
                sb.append(PrintUtils.getVectorOfDoubles(_vectorsFormingUtopiaPoint[i], prec));
                if (i < _criteriaTypes.length - 1) sb.append(System.lineSeparator());
            }
            return super.getStringRepresentation(prec) + System.lineSeparator() + sb;
        } else return super.getStringRepresentation(prec);
    }
}
