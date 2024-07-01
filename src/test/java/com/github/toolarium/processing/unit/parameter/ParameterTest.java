/*
 * ParameterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Implements parameter tests
 *
 * @author patrick
 */
public class ParameterTest {
    
    /**
     * Implements default parameter tests
     */
    @Test
    public void testDefaultParameter() {
        ParameterDefinition def1 = new ParameterDefinitionBuilder().name("myKey").defaultValue("myName").isMandatory().description("The my key description").build();
        assertEquals(def1.getDefaultValue(), "myName");

        ParameterDefinition def2 = new ParameterDefinitionBuilder().name("myKey").defaultValue(new String[] {"myName1", "myName2"}).isMandatory().description("The my key description").build(); 
        assertTrue(Arrays.equals((String[])def2.getDefaultValue(), new String[] {"myName1", "myName2"}));
    }


    /**
     * Test the parameter sort order
     */
    @Test
    public void testParameterSortOrder() {
        String parameterList = "";
        List<ParameterDefinition>  list = new SimpleProcessingUnitStringTest().getParameterDefinition();
        for (ParameterDefinition d : list) {
            parameterList += d.getKey() + " ";
        }

        assertEquals(parameterList, "numberOfUnitsToProcess numberOfFailedUnits endStatus endMessage unitProcessTimeMax unitProcessTimeMin ");
    }


    class SimpleProcessingUnitStringTest extends AbstractProcessingUnitImpl {
        /**
         * Constructor
         */
        SimpleProcessingUnitStringTest() {
            getParameterRuntime().addParameterDefinition(new ParameterDefinitionBuilder().name("numberOfUnitsToProcess").isMandatory().description("").build());
            getParameterRuntime().addParameterDefinition(new ParameterDefinitionBuilder().name("numberOfFailedUnits").isMandatory().description("").build());
            getParameterRuntime().addParameterDefinition(new ParameterDefinitionBuilder().name("endStatus").isMandatory().description("").build());
            getParameterRuntime().addParameterDefinition(new ParameterDefinitionBuilder().name("endMessage").isMandatory().description("").build());
            getParameterRuntime().addParameterDefinition(new ParameterDefinitionBuilder().name("unitProcessTimeMax").isMandatory().description("").build());
            getParameterRuntime().addParameterDefinition(new ParameterDefinitionBuilder().name("unitProcessTimeMin").isMandatory().description("").build());
        }


        /**
         * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
         */
        @Override
        public long estimateNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) throws ProcessingException {
            return 0;
        }


        /**
         * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
         */
        @Override
        public IProcessingUnitStatus processUnit(IProcessingUnitProgress processingProgress, IProcessingUnitContext processingUnitContext) throws ProcessingException {
            return null;
        }
    }
}
