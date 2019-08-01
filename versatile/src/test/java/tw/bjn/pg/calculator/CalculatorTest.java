package tw.bjn.pg.calculator;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class CalculatorTest {
    @Test
    public void positiveTests() {
        Assert.assertThat(new CalculatorImpl().calc("1 + 2"), is(3));
        Assert.assertThat(new CalculatorImpl().calc(" 3 * 2"), is(6));
        Assert.assertThat(new CalculatorImpl().calc("1 + 2 -9 +1"), is(-5));
        Assert.assertThat(new CalculatorImpl().calc("32 * 10"), is(320));
        Assert.assertThat(new CalculatorImpl().calc("35 / 5"), is(7));
        Assert.assertThat(new CalculatorImpl().calc("1 + ( 2 * 3) - 7"), is(0));
        Assert.assertThat(new CalculatorImpl().calc("( 8 / 2 ) * ( 7 + 4 ) - ( 5 - 8 )"), is(47));
        Assert.assertThat(new CalculatorImpl().calc("1 + 2"), is(3));
        Assert.assertThat(new CalculatorImpl().calc("15"), is(15));
        Assert.assertThat(new CalculatorImpl().calc("      3 +      4 "), is(7));
        Assert.assertThat(new CalculatorImpl().calc("9 + 8 + 1 + 2 * 3 * 4"), is(42));
        Assert.assertThat(new CalculatorImpl().calc("-3 + 8"), is(5));
        Assert.assertThat(new CalculatorImpl().calc("-7 * 8"), is(-56));
    }
    @Test(expected = Exception.class)
    public void negativeTest() {
        new CalculatorImpl().calc("$ as22");
    }

    @Test
    public void wrongCalc() {
        // TODO: improve
        // currently, this expression will be expanded like: '0 - 7 * 0 - 8'
        Assert.assertThat(new CalculatorImpl().calc("-7 * -8"), is(-8));
    }
}