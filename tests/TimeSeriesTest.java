import ngrams.TimeSeries;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Unit Tests for the TimeSeries class.
 *  @author Josh Hug
 */
public class TimeSeriesTest {
    @Test
    public void testFromSpec() {
        TimeSeries catPopulation = new TimeSeries();
        catPopulation.put(1991, 0.0);
        catPopulation.put(1992, 100.0);
        catPopulation.put(1994, 200.0);

        TimeSeries dogPopulation = new TimeSeries();
        dogPopulation.put(1994, 400.0);
        dogPopulation.put(1995, 500.0);

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);
        // expected: 1991: 0,
        //           1992: 100
        //           1994: 600
        //           1995: 500

        List<Integer> expectedYears = new ArrayList<>
                (Arrays.asList(1991, 1992, 1994, 1995));

        assertThat(totalPopulation.years()).isEqualTo(expectedYears);

        List<Double> expectedTotal = new ArrayList<>
                (Arrays.asList(0.0, 100.0, 600.0, 500.0));

        for (int i = 0; i < expectedTotal.size(); i += 1) {
            assertThat(totalPopulation.data().get(i)).isWithin(1E-10).of(expectedTotal.get(i));
        }
    }

    @Test
    public void testEmptyBasic() {
        TimeSeries catPopulation = new TimeSeries();
        TimeSeries dogPopulation = new TimeSeries();

        assertThat(catPopulation.years()).isEmpty();
        assertThat(catPopulation.data()).isEmpty();

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);

        assertThat(totalPopulation.years()).isEmpty();
        assertThat(totalPopulation.data()).isEmpty();
    }

    @Test
    public void testYears(){
        TimeSeries catPopulation = new TimeSeries();
        catPopulation.put(1991, 0.0);
        catPopulation.put(1992, 100.0);
        catPopulation.put(1994, 300.0);
        catPopulation.put(1989, 200.0);

        List<Integer> expectedYears = new ArrayList<>
                (Arrays.asList(1989, 1991, 1992, 1994));

        assertThat(catPopulation.years()).isEqualTo(expectedYears);
    }

    @Test
    public void testData(){
        TimeSeries catPopulation = new TimeSeries();
        catPopulation.put(1991, 0.0);
        catPopulation.put(1992, 100.0);
        catPopulation.put(1994, 300.0);
        catPopulation.put(1989, 200.0);
        catPopulation.put(2000, 50.0);

        List<Double> expectedData = new ArrayList<>
                (Arrays.asList(200.0, 0.0, 100.0, 300.0, 50.0));

        assertThat(catPopulation.data()).isEqualTo(expectedData);
    }

    /**
     * Tests the constructor that creates a sub-history of a TimeSeries.
     */
    @Test
    public void testConstructorSubset() {
        TimeSeries fullHistory = new TimeSeries();
        fullHistory.put(2000, 10.0);
        fullHistory.put(2001, 20.0);
        fullHistory.put(2002, 30.0);
        fullHistory.put(2003, 40.0);
        fullHistory.put(2004, 50.0);

        // Create a new TimeSeries containing only the data from 2001 to 2003
        TimeSeries partialHistory = new TimeSeries(fullHistory, 2001, 2003);

        List<Integer> expectedYears = Arrays.asList(2001, 2002, 2003);
        assertThat(partialHistory.years()).isEqualTo(expectedYears);

        List<Double> expectedData = Arrays.asList(20.0, 30.0, 40.0);
        assertThat(partialHistory.data()).isEqualTo(expectedData);

        // Also, ensure the original TimeSeries was not modified.
        assertThat(fullHistory.years()).hasSize(5);
    }

    /**
     * Tests the `plus` method with two non-overlapping TimeSeries.
     */
    @Test
    public void testPlusNonOverlapping() {
        TimeSeries ts1 = new TimeSeries();
        ts1.put(1990, 10.0);
        ts1.put(1991, 20.0);

        TimeSeries ts2 = new TimeSeries();
        ts2.put(2000, 30.0);
        ts2.put(2001, 40.0);

        TimeSeries sum = ts1.plus(ts2);

        List<Integer> expectedYears = Arrays.asList(1990, 1991, 2000, 2001);
        assertThat(sum.years()).isEqualTo(expectedYears);

        List<Double> expectedData = Arrays.asList(10.0, 20.0, 30.0, 40.0);
        assertThat(sum.data()).isEqualTo(expectedData);
    }

    /**
     * Tests adding a non-empty TimeSeries to an empty one.
     */
    @Test
    public void testPlusWithEmpty() {
        TimeSeries ts1 = new TimeSeries();
        ts1.put(1990, 10.0);
        ts1.put(1991, 20.0);

        TimeSeries emptyTs = new TimeSeries();

        // Test adding an empty series to a populated one
        TimeSeries sum1 = ts1.plus(emptyTs);
        assertThat(sum1.years()).isEqualTo(ts1.years());
        assertThat(sum1.data()).isEqualTo(ts1.data());

        // Test adding a populated series to an empty one
        TimeSeries sum2 = emptyTs.plus(ts1);
        assertThat(sum2.years()).isEqualTo(ts1.years());
        assertThat(sum2.data()).isEqualTo(ts1.data());
    }

    /**
     * Tests the basic functionality of the dividedBy method.
     */
    @Test
    public void testDividedByBasic() {
        TimeSeries dividend = new TimeSeries();
        dividend.put(2000, 100.0);
        dividend.put(2001, 500.0);
        dividend.put(2002, 800.0);

        TimeSeries divisor = new TimeSeries();
        divisor.put(2000, 10.0);
        divisor.put(2001, 25.0);
        divisor.put(2002, 100.0);

        TimeSeries quotient = dividend.dividedBy(divisor);

        List<Integer> expectedYears = Arrays.asList(2000, 2001, 2002);
        assertThat(quotient.years()).isEqualTo(expectedYears);

        assertThat(quotient.get(2000)).isWithin(1E-10).of(10.0);
        assertThat(quotient.get(2001)).isWithin(1E-10).of(20.0);
        assertThat(quotient.get(2002)).isWithin(1E-10).of(8.0);
    }

    /**
     * Tests that dividedBy ignores years that are in the divisor but not the dividend.
     */
    @Test
    public void testDividedByIgnoresExtraDivisorYears() {
        TimeSeries dividend = new TimeSeries();
        dividend.put(2000, 100.0);
        dividend.put(2001, 500.0);

        TimeSeries divisor = new TimeSeries();
        divisor.put(2000, 10.0);
        divisor.put(2001, 25.0);
        divisor.put(2002, 100.0); // Extra year

        TimeSeries quotient = dividend.dividedBy(divisor);

        List<Integer> expectedYears = Arrays.asList(2000, 2001);
        assertThat(quotient.years()).isEqualTo(expectedYears);
        assertThat(quotient.data()).hasSize(2);
    }

    /**
     * Tests that dividedBy throws an IllegalArgumentException if the divisor is
     * missing a year that the dividend has.
     */
    @Test
    public void testDividedByThrowsExceptionOnMissingYear() {
        TimeSeries dividend = new TimeSeries();
        dividend.put(2000, 100.0);
        dividend.put(2001, 500.0); // This year is missing in the divisor

        TimeSeries divisor = new TimeSeries();
        divisor.put(2000, 10.0);

        // This should throw an exception because 2001 is not in the divisor.
        assertThrows(IllegalArgumentException.class, () -> {
            dividend.dividedBy(divisor);
        });
    }

    /**
     * Tests division by zero, which should result in Infinity.
     */
    @Test
    public void testDividedByZero() {
        TimeSeries dividend = new TimeSeries();
        dividend.put(2000, 100.0);

        TimeSeries divisor = new TimeSeries();
        divisor.put(2000, 0.0);

        TimeSeries quotient = dividend.dividedBy(divisor);
        assertThat(quotient.get(2000)).isPositiveInfinity();
    }
}