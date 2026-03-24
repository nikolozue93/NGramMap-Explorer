package ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

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

        for(Integer year : ts.keySet()){
            if(year >= startYear && year <= endYear){
                this.put(year, ts.get(year));
            }
        }
    }

    /**
     *  Returns all years for this time series in ascending order.
     */
    public List<Integer> years() {
        List<Integer> years = new ArrayList<>(this.keySet());
        return years;
    }

    /**
     *  Returns all data for this time series. Must correspond to the
     *  order of years().
     *
     *  as we must correspond to the order of the years()
     *  first we retrieve the list of the keys (years)
     *  and then add value by value with the ordered keys
     */
    public List<Double> data() {
        List<Double> data = new ArrayList<>(this.values());

        return data;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries sum = new TimeSeries();
        if(this.isEmpty() && ts.isEmpty()){
            return sum;
        }

        // copying this TS into the new one
        for(Integer key : this.keySet()){
            sum.put(key, this.get(key));
        }

        // checking for correspondences to be added
        // if both contain the same key, i add their values
        // and then change with the corresponding key

        // if one contains the key and other not
        // then with else, i just put that given key value
        double newValue;
        for(Integer key : ts.keySet()){
            if(sum.containsKey(key)){
                newValue = ts.get(key) + sum.get(key);
                sum.put(key, newValue);
            } else {
                sum.put(key, ts.get(key));
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
        TimeSeries divided = new TimeSeries();
        double div;

        for(Integer key : this.keySet()){
            if(ts.containsKey(key)){
                div = this.get(key) / ts.get(key);
                divided.put(key, div);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return divided;
    }
}
