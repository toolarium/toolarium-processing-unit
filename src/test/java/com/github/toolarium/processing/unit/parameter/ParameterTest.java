/*
 * ParameterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;
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
        ParameterDefinition def1 = new ParameterDefinition("myKey", ParameterValueType.STRING, "myName", 1, "The my key description");
        assertEquals(def1.getDefaultValue(), "myName");

        ParameterDefinition def2 = new ParameterDefinition("myKey", ParameterValueType.STRING, new String[] {"myName1", "myName2"}, 1, "The my key description");
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
            getParameterRuntime().addParameterDefinition(new ParameterDefinition("numberOfUnitsToProcess", 1, ""));
            getParameterRuntime().addParameterDefinition(new ParameterDefinition("numberOfFailedUnits", 1, ""));
            getParameterRuntime().addParameterDefinition(new ParameterDefinition("endStatus", 1, ""));
            getParameterRuntime().addParameterDefinition(new ParameterDefinition("endMessage", 1, ""));
            getParameterRuntime().addParameterDefinition(new ParameterDefinition("unitProcessTimeMax", 1, ""));
            getParameterRuntime().addParameterDefinition(new ParameterDefinition("unitProcessTimeMin", 1, ""));
        }


        /**
         * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#countNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
         */
        @Override
        protected long countNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) throws ProcessingException {
            return 0;
        }


        /**
         * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
         */
        @Override
        public IProcessStatus processUnit(IProcessingUnitContext processingUnitContext) throws ProcessingException {
            return null;
        }
    }
}
