package com.example.tdddemo.classes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringDuplicatorTest {

    StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringDuplicator();
    }

    @Test
    public void duplicate_smallString_returnedConcatenatedValue() {
        String arg = SUT.concatenateString("abc");
        Assert.assertThat(arg,is("abcabc"));
    }
}