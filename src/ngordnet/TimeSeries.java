package ngordnet;

import java.util.TreeMap;
import java.util.Collection;
import java.util.NavigableSet;
import java.util.ArrayList;
import java.util.TreeSet;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {
    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
        super();
    }

    /**
     * Returns the years in which this time series is valid. Doesn't really need
     * to be a NavigableSet. This is a private method and you don't have to
     * implement it if you don't want to.
     */
    private NavigableSet<Integer> validYears(int startYear, int endYear) {
        if (startYear > endYear) {
            throw new IllegalArgumentException();
        }
        return navigableKeySet().subSet(startYear, true, endYear, true);
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR. inclusive
     * of both end points.
     */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        super();
        for (int i : ts.validYears(startYear, endYear)) {
            put(i, ts.get(i));
        }
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        super(ts);
    }

    /**
     * Returns the quotient of this time series divided by the relevant value in
     * ts. If ts is missing a key in this time series, return an
     * IllegalArgumentException.
     */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        if (!ts.keySet().containsAll(keySet())) {
            throw new IllegalArgumentException();
        }
        TreeSet<Integer> allYears = new TreeSet<>(keySet());
        allYears.addAll(ts.keySet());
        TimeSeries<Double> returnedTs = new TimeSeries<>();
        for (int year : allYears) {
            double value;
            if (!containsKey(year)) {
                value = 0;
            } else {
                value = get(year).doubleValue() / ts.get(year).doubleValue();
            }
            returnedTs.put(year, value);
        }
        return returnedTs;
    }

    /**
     * Returns the sum of this time series with the given ts. The result is a a
     * Double time series (for simplicity).
     */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        // Treat missing years as having value of 0
        TreeSet<Integer> allYears = new TreeSet<>(keySet());
        allYears.addAll(ts.keySet());
        // allYears.addAll(ts.keySet());
        TimeSeries<Double> returnedTs = new TimeSeries<>();
        for (int year : allYears) {
            double value;
            if (!containsKey(year)) {
                value = ts.get(year).doubleValue();
            } else if (!ts.containsKey(year)) {
                value = get(year).doubleValue();
            } else {
                value = get(year).doubleValue() + ts.get(year).doubleValue();
            }
            returnedTs.put(year, value);
        }
        return returnedTs;
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        // be aware that Collection<? extends Number> is not a
        // Collection<Number>
        return new TreeSet<Number>(keySet());
    }

    /**
     * Returns all data for this time series. Must be in the same order as
     * years().
     */
    public Collection<Number> data() {
        // must be in the same order as years, must allow duplicates
        return new ArrayList<Number>(values());
    }
}