/*
 * ParameterRuntimeTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import org.junit.jupiter.api.Test;

/**
 * Test the {@link ParameterRuntime}.
 *  
 * @author patrick
 */
public class ParameterRuntimeTest {

   
    /**
     * Test add parameter definition
     */
    @Test
    public void testAddParameterDefinition() {
        ParameterRuntime parameterRuntime = new ParameterRuntime();
       
        ParameterDefinition numberOfWords = new ParameterDefinitionBuilder().name("numberOfWords").defaultValue(10000).description("The number of words.").build();
        parameterRuntime.addParameterDefinition(numberOfWords);
        assertEquals(numberOfWords, parameterRuntime.getParameterDefinition(numberOfWords.getKey()));
    }
}
