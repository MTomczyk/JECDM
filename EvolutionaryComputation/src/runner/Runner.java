package runner;

import ea.IEA;
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
        public Params(IEA ea)
        {
            this(new IEA[]{ea}, 1);
        }


        /**
         * Parameterized constructor.
         *
         * @param ea                 evolutionary algorithm
         * @param steadyStateRepeats the number of steady-state iterations (the same for each evolutionary algorithm)
         */
        public Params(IEA ea, int steadyStateRepeats)
        {
            this(new IEA[]{ea}, steadyStateRepeats);
        }

        /**
         * Parameterized constructor.
         *
         * @param ea            evolutionary algorithm
         * @param visualization optional visualization
         */
        public Params(IEA ea, IVisualization visualization)
        {
            this(new IEA[]{ea}, 1, visualization);
        }

        /**
         * Parameterized constructor.
         *
         * @param eas evolutionary algorithms
         */
        public Params(IEA[] eas)
        {
            this(eas, 1);
        }


        /**
         * Parameterized constructor.
         *
         * @param eas           evolutionary algorithms
         * @param visualization optional visualization
         */
        public Params(IEA[] eas, IVisualization visualization)
        {
            this(eas, 1, visualization);
        }


        /**
         * Parameterized constructor.
         *
         * @param eas                evolutionary algorithms
         * @param steadyStateRepeats the number of steady-state iterations (the same for each evolutionary algorithm)
         */
        public Params(IEA[] eas, int steadyStateRepeats)
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
        public Params(IEA ea, int steadyStateRepeats, IVisualization visualization)
        {
            this(new IEA[]{ea}, steadyStateRepeats, visualization);
        }

        /**
         * Parameterized constructor.
         *
         * @param eas                evolutionary algorithms
         * @param steadyStateRepeats the number of steady-state iterations (the same for each evolutionary algorithm)
         * @param visualization      optional visualization (plots)
         */
        public Params(IEA[] eas, int steadyStateRepeats, IVisualization visualization)
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
        public Params(IEA[] eas, int[] steadyStateRepeats, IVisualization visualization)
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

    /**
     * Parameterized constructor. Assumes that the evolutionary process concerns a single generational (not
     * steady-state) evolutionary algorithm.
     *
     * @param ea evolutionary algorithm to be run
     */
    public Runner(IEA ea)
    {
        super(new Params(ea));
    }

    /**
     * Parameterized constructor. Assumes that the evolutionary process concerns a single generational (not
     * steady-state) evolutionary algorithm.
     *
     * @param ea  evolutionary algorithm to be run
     * @param ssr the number of steady state repeats for each generation
     */
    public Runner(IEA ea, int ssr)
    {
        super(new Params(ea, ssr));
    }
}
