package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Provides some auxiliary functions related to string processing.
 *
 * @author MTomczyk
 */
public class StringUtils
{


    /**
     * Auxiliary method for creating an indent string (prefix for lines)
     *
     * @param indent the number of spaces to be put in front of each line to be printed
     * @return indent string
     */
    public static String getIndent(int indent)
    {
        char[] ind = new char[indent];
        Arrays.fill(ind, ' ');
        return new String(ind);
    }

    /**
     * Transforms strings stored in a list into an array.
     *
     * @param strings input list
     * @return array of strings
     */
    public static String[] getArrayFromList(LinkedList<String> strings)
    {
        String[] r = new String[strings.size()];
        int idx = 0;
        for (String s : strings) r[idx++] = s;
        return r;
    }


    /**
     * Constructs and returns a timestamp viewed as a string
     *
     * @param timestamp input timestamp
     * @return output timestamp (string); or "not provided" if the input is null
     */
    public static String getTimestamp(LocalDateTime timestamp)
    {
        if (timestamp == null) return "not provided";
        return "[" + timestamp.getDayOfMonth() + "." + timestamp.getMonthValue() + "." + timestamp.getYear() +
                " " + timestamp.getHour() + ":" + timestamp.getMinute() + ":" + timestamp.getSecond() + ":" +
                (timestamp.getNano() / 1000000) + "]";
    }

    /**
     * Constructs a delta time from two timestamps: [days, hours, minutes, seconds, milliseconds].
     *
     * @param startTime starting timestamp
     * @param stopTime  ending timestamp
     * @return delta time viewed as a string
     */
    public static String getDeltaTime(LocalDateTime startTime, LocalDateTime stopTime)
    {
        long millis = Duration.between(startTime, stopTime).toMillis();
        long days = millis / (24 * 60 * 60 * 1000);
        millis -= days * (24 * 60 * 60 * 1000);
        long hours = millis / (60 * 60 * 1000);
        millis -= hours * (60 * 60 * 1000);
        long minutes = millis / (60 * 1000);
        millis -= minutes * (60 * 1000);
        long seconds = millis / ( 1000);
        millis -= seconds * (1000);
        return "[days = " + days + ", hours = " + hours + ", minutes = " + minutes + ", seconds = " + seconds + ", millis = " + millis + "]";
    }
}
