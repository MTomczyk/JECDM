package problem.moo;

import search.BiSection;

/**
 * For performing auxiliary side-calculations.
 *
 * @author MTomczyk
 */
class AuxSearcher
{
    /**
     * Runs the side-calculations.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // ZDT3 Search
        System.out.println("ZDT3 Pareto bounds search");
        {
            for (double[] b : new double[][]{
                    {0.182d, 0.184d, 0.6696523565498150},
                    {0.4d, 0.41d, 0.2421610854767790},
                    {0.61d, 0.62d, -0.1242184447485860d},
                    {0.82d, 0.83d, -0.4582633256726060}
            })
            {
                double x = BiSection.run(b[0], b[1],
                        x1 -> 1.0d - Math.sqrt(x1) - x1 * Math.sin(10.0d * Math.PI * x1) - b[2],
                        (iteration, evaluation) -> {
                            if (iteration > 1000000) return true;
                            return Double.compare(Math.abs(evaluation), 1.0E-16) < 0;
                        }, new BiSection.IMidRule()
                        {
                        });
                System.out.println(x);
            }
        }

    }
}
