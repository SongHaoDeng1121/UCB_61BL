package ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /**
     * If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here.
     */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    private TimeSeries sourceTs;
    private int startYear;
    private int endYear;
    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */





    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        if (startYear > endYear) {
            throw new IllegalArgumentException("startYear must ≤ endYear");
        }
        if (startYear < MIN_YEAR || endYear > MAX_YEAR) {
            throw new IllegalArgumentException("Year out of allowed range");
        }
        this.sourceTs = ts;
        this.startYear = startYear;
        this.endYear = endYear;

        for (Map.Entry<Integer, Double> entry : ts.entrySet()) {
            int year = entry.getKey();
            if (year >= startYear && year <= endYear) {
                this.put(year, entry.getValue());
            }
        }

    }

    public TimeSeries(TimeSeries ts) {
        super();
        for (Map.Entry<Integer, Double> entry : ts.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns all years for this time series in ascending order.
     */
    public List<Integer> years() {
        return new ArrayList<>(this.keySet());
    }

    /**
     * Returns all data for this time series. Must correspond to the
     * order of years().
     */
    public List<Double> data() {
        return new ArrayList<>(this.values());
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     * <p>
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries sum = new TimeSeries();

        for (Map.Entry<Integer, Double> entry : this.entrySet()) {
            sum.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Integer, Double> entry : ts.entrySet()) {
            int year = entry.getKey();
            double value = entry.getValue();

            if (sum.containsKey(year)) {
                sum.put(year, sum.get(year) + value);
            } else {
                sum.put(year, value);
            }
        }

        return sum;
    }


    /**
         * Returns the quotient of the value for each year this TimeSeries divided by the
         * value for the same year in TS. Should return a new TimeSeries (does not modify this
         * TimeSeries).
         *
         * If TS is missing a year that exists in this TimeSeries, throw an
         * IllegalArgumentException.
         * If TS has a year that is not in this TimeSeries, ignore it.
         */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries result = new TimeSeries();

        for (Map.Entry<Integer, Double> entry : this.entrySet()) {
            int year = entry.getKey();

            if (!ts.containsKey(year)) {
                throw new IllegalArgumentException("Missing year in ts: " + year);
            }

            double thisVal = entry.getValue();
            double tsVal = ts.get(year);
            result.put(year, thisVal / tsVal);
        }

        return result;
    }
}


