package runner;

import ea.EA;
import visualization.IVisualization;

/**
 * Default implementation of the {@link IRunner} interface.
 *
 * @author MTomczyk
 */

public class Runner extends AbstractRunner implements IRunner
{
    /**
     * Params container.
     */
    public static class Params extends AbstractRunner.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param ea evolutionary algorithm
         */
        public Params(EA ea)
        {
            this(new EA[]{ea}, 1);
        }


        /**
         * Parameterized constructor.
         *
         * @param ea                 evolutionary algorithm
         * @param steadyStateRepeats the number of steady-state iterations (the same for each evolutionary algorithm)
         */
        public Params(EA ea, int steadyStateRepeats)
        {
            this(new EA[]{ea}, steadyStateRepeats);
        }

        /**
         * Parameterized constructor.
         *
         * @param ea            evolutionary algorithm
         * @param visualization optional visualization
         */
        public Params(EA ea, IVisualization visualization)
        {
            this(new EA[]{ea}, 1, visualization);
        }

        /**
         * Parameterized constructor.
         *
         * @param eas evolutionary algorithms
         */
        public Params(EA[] eas)
        {
            this(eas, 1);
        }


        /**
         * Parameterized constructor.
         *
         * @param eas           evolutionary algorithms
         * @param visualization optional visualization
         */
        public Params(EA[] eas, IVisualization visualization)
        {
            this(eas, 1, visualization);
        }


        /**
         * Parameterized constructor.
         *
         * @param eas                evolutionary algorithms
         * @param steadyStateRepeats the number of steady-state iterations (the same for each evolutionary algorithm)
         */
        public Params(EA[] eas, int steadyStateRepeats)
        {
            this(eas, steadyStateRepeats, null);
        }


        /**
         * Parameterized constructor.
         *
         * @param ea                 evolutionary algorithm
         * @param steadyStateRepeats the number of steady-state iterations (the same for each evolutionary algorithm)
         * @param visualization      optional visualization (plots)
         */
        public Params(EA ea, int steadyStateRepeats, IVisualization visualization)
        {
            this(new EA[]{ea}, steadyStateRepeats, visualization);
        }

        /**
         * Parameterized constructor.
         *
         * @param eas                evolutionary algorithms
         * @param steadyStateRepeats the number of steady-state iterations (the same for each evolutionary algorithm)
         * @param visualization      optional visualization (plots)
         */
        public Params(EA[] eas, int steadyStateRepeats, IVisualization visualization)
        {
            super(eas, steadyStateRepeats, visualization);
        }

        /**
         * Parameterized constructor.
         *
         * @param eas                evolutionary algorithms
         * @param steadyStateRepeats the number of steady-state iterations; each element represents the number of
         *                           steady-state repeats for a different EA (1:1 mapping); hence, the array length
         *                           should equal the number of EAs
         * @param visualization      optional visualization (plots)
         */
        public Params(EA[] eas, int[] steadyStateRepeats, IVisualization visualization)
        {
            super(eas, steadyStateRepeats, visualization);
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param p params contained
     */
    public Runner(Params p)
    {
        super(p);
    }
}
