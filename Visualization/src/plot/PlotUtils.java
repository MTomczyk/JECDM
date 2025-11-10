package plot;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Provides various functionalities.
 *
 * @author MTomczyk
 */
public class PlotUtils
{
    /**
     * Auxiliary method for instantiating a decimal format object. It is derived from Locale.getDefault() and
     * the decimal separator is set to '.'.
     *
     * @param decimalPlaces represents the number of decimal places (at least 0, if zero, the number is to be displayed
     *                      as integer, i.e., without the separator)
     * @return a suitably configured decimal format object
     */
    public static DecimalFormat getDecimalFormat(int decimalPlaces)
    {
        return getDecimalFormat('.', decimalPlaces);
    }

    /**
     * Auxiliary method for instantiating a decimal format object. It is derived from Locale.getDefault().
     *
     * @param separator     decimal separator character
     * @param decimalPlaces represents the number of decimal places (at least 0, if zero, the number is to be displayed
     *                      as integer, i.e., without the separator)
     * @return a suitably configured decimal format object
     */
    public static DecimalFormat getDecimalFormat(Character separator, int decimalPlaces)
    {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(separator);
        if (decimalPlaces < 1)
        {
            DecimalFormat df = new DecimalFormat("0", otherSymbols);
            df.setDecimalSeparatorAlwaysShown(false);
            return df;
        }
        return new DecimalFormat("0." + "0".repeat(decimalPlaces), otherSymbols);
    }
}
