/*
 * ParameterValueTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.processing.unit.dto.ParameterValue;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import org.junit.jupiter.api.Test;


/**
 * Test the value list
 *
 * @author patrick
 */
public class ParameterValueTest {
    
    /**
     * Test date conversion
     */
    @Test
    public void testDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

        assertEquals(formatter.format((new ParameterValue(Arrays.asList("30.4.2024 11:03:23.456"))).getValueAsDate()), "30.04.2024 11:03:23.456");
        assertEquals(formatter.format((new ParameterValue(Arrays.asList("30.4.2024 11:03:12"))).getValueAsDate()), "30.04.2024 11:03:12.000");
    }
}
