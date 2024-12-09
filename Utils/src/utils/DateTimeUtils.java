package utils;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Provides various utility functions for {@link java.time.LocalDateTime}.
 *
 * @author MTomczyk
 */

public class DateTimeUtils
{
    /**
     * Returns the duration between two timestamps (in seconds).
     *
     * @param startTime starting timestamp
     * @param stopTime  stopping timestamp
     * @return delta time (in seconds)
     */
    public static long getDeltaTimeInSeconds(LocalDateTime startTime, LocalDateTime stopTime)
    {
        return Duration.between(startTime, stopTime).toSeconds();
    }

    /**
     * Returns the duration between two timestamps (in seconds).
     *
     * @param startTime starting timestamp
     * @param stopTime  stopping timestamp
     * @return delta time (in seconds)
     */
    public static long getDeltaTimeInMilliseconds(LocalDateTime startTime, LocalDateTime stopTime)
    {
        return Duration.between(startTime, stopTime).toMillis();
    }
}
