package com.example.tdddemo.classes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PositiveNumberValidatorTest {

    PositiveNumberValidator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new PositiveNumberValidator();
    }

    @Test
    public void positiveChecker_negativeNumber_returnFalse() {
        boolean result = SUT.isPositive(-1);
        Assert.assertThat(result, is(false));
    }

    @Test
    public void positiveChecker_positiveNumber_returnTrue() {
        boolean result = SUT.isPositive(1);
        Assert.assertThat(result, is(true));
    }
}