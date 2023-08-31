package com.example.tdddemo.classes;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void negativeChecker_positiveNumber_returnFalse() {
        boolean result = SUT.isNegative(1);
        Assert.assertThat(result, is(false));
    }

    @Test
    public void negativeChecker_negativeNumber_returnTrue() {
        boolean result = SUT.isNegative(-1);
        Assert.assertThat(result, is(true));
    }
}