package executor;

import utils.Log;

/**
 * Provides various common fields/functions for all-level executors.
 *
 * @author MTomczyk
 */
class AbstractExecutor
{
    /**
     * Provides means for logging (mainly printing messages to the console).
     */
    protected final Log _log;

    /**
     * Indent used when constructing logs.
     */
    protected final int _indent;

    /**
     * Parameterized constructor.
     *
     * @param log    provides means for logging (mainly printing messages to the console)
     * @param indent indent used when constructing logs
     */
    protected AbstractExecutor(Log log, int indent)
    {
        _log = log;
        _indent = indent;
    }
}
