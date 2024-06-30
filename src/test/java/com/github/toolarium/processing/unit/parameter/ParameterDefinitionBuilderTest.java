/*
 * ParameterDefinitionBuilderTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.dto.ParameterValueType;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import org.junit.jupiter.api.Test;


/**
 * Test the parameter definition builder
 * 
 * @author patrick
 */
public class ParameterDefinitionBuilderTest {
   
    /**
     * Test the guess value type
     */
    @Test
    public void testGuessValueType() {
        assertEquals(ParameterValueType.STRING, ParameterDefinitionBuilder.guessValueType(null, null));
        assertEquals(ParameterValueType.STRING, ParameterDefinitionBuilder.guessValueType(null, ""));
        assertEquals(ParameterValueType.CHAR, ParameterDefinitionBuilder.guessValueType(null, Character.valueOf('A')));
        assertEquals(ParameterValueType.BOOLEAN, ParameterDefinitionBuilder.guessValueType(null, true));
        assertEquals(ParameterValueType.BOOLEAN, ParameterDefinitionBuilder.guessValueType(null, false));
        assertEquals(ParameterValueType.BOOLEAN, ParameterDefinitionBuilder.guessValueType(null, Boolean.TRUE));
        assertEquals(ParameterValueType.BOOLEAN, ParameterDefinitionBuilder.guessValueType(null, Boolean.FALSE));
        assertEquals(ParameterValueType.SHORT, ParameterDefinitionBuilder.guessValueType(null, Short.valueOf("2")));
        assertEquals(ParameterValueType.INTEGER, ParameterDefinitionBuilder.guessValueType(null, 23));
        assertEquals(ParameterValueType.INTEGER, ParameterDefinitionBuilder.guessValueType(null, Integer.valueOf(23)));
        assertEquals(ParameterValueType.LONG, ParameterDefinitionBuilder.guessValueType(null, 23L));
        assertEquals(ParameterValueType.LONG, ParameterDefinitionBuilder.guessValueType(null, Long.valueOf(23L)));
        assertEquals(ParameterValueType.FLOAT, ParameterDefinitionBuilder.guessValueType(null, 1.2f));
        assertEquals(ParameterValueType.FLOAT, ParameterDefinitionBuilder.guessValueType(null, Float.valueOf(2.3f)));
        assertEquals(ParameterValueType.DOUBLE, ParameterDefinitionBuilder.guessValueType(null, 1.2));
        assertEquals(ParameterValueType.DOUBLE, ParameterDefinitionBuilder.guessValueType(null, 1.2d));
        assertEquals(ParameterValueType.DOUBLE, ParameterDefinitionBuilder.guessValueType(null, Double.valueOf(2.3d)));
        assertEquals(ParameterValueType.DATE, ParameterDefinitionBuilder.guessValueType(null, LocalDate.now()));
        assertEquals(ParameterValueType.DATE, ParameterDefinitionBuilder.guessValueType(null, LocalDate.parse("2024-06-30")));
        assertEquals(ParameterValueType.TIME, ParameterDefinitionBuilder.guessValueType(null, LocalTime.now()));
        assertEquals(ParameterValueType.TIME, ParameterDefinitionBuilder.guessValueType(null, LocalTime.parse("23:59:59.999")));
        assertEquals(ParameterValueType.DATETIME, ParameterDefinitionBuilder.guessValueType(null, new Date()));
        assertEquals(ParameterValueType.DATETIME, ParameterDefinitionBuilder.guessValueType(null, Instant.now()));
        assertEquals(ParameterValueType.DATETIME, ParameterDefinitionBuilder.guessValueType(null, Instant.parse("2021-03-15T08:59:22.123Z")));
    }
}
