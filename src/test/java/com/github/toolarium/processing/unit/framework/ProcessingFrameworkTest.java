/*
 * ProcessingFrameworkTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.framework;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunner;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunnerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;


/**
 * Tests the {@link com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunner} framework.
 *
 * @author patrick
 */
public class ProcessingFrameworkTest {
    private static final String HEADER = "ProcessStatus [processingProgress=ProcessingProgressImpl "; 
    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";
    private static final String VALUE3 = "value3";
    private static final String PARAM1 = "param1";
    private static final String PARAM2 = "param2";
    private static final String PARAM3 = "param3";

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnit1() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ProcessingUnitStringTest.DATA_FEED, "a", "b", "c", "d"));

        TestProcessingUnitRunner<ProcessingUnitStringTest> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(ProcessingUnitStringTest.class, parameterList), 4);

        assertEquals((processRunner.getProcesingUnit()).getResult(),
                 "[a(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=1, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[b(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=2, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[c(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=3, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[d(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=4, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=false])]");
        
        assertNotNull(processRunner.getId());
        assertNull(processRunner.getName());
        assertEquals(ProcessingActionStatus.ENDED, processRunner.getProcessingActionStatus());
        assertFalse(processRunner.getProcessStatus().hasNext());
        assertEquals(ProcessingRuntimeStatus.SUCCESSFUL, processRunner.getProcessingRuntimeStatus());
        assertEquals("[]", "" + processRunner.getStatusMessageList());
        assertTrue(processRunner.getTimeMeasurement().getStartTimestamp().toEpochMilli() <= processRunner.getTimeMeasurement().getStartTimestamp().toEpochMilli());
        assertTrue(processRunner.getTimeMeasurement().getDuration() > 0);
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnit2() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ProcessingUnitStringTest.DATA_FEED, "a", "b", "c", "d"));

        Random random = new Random();
        long randomSuspendIdx = random.nextInt(10 - 5 + 1) + 1;

        TestProcessingUnitRunner<ProcessingUnitStringTest> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithSuspendAndResume(ProcessingUnitStringTest.class, parameterList, randomSuspendIdx, 100L, 3), 4);

        assertEquals((processRunner.getProcesingUnit()).getResult(),
                 "[a(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=1, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[b(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=2, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[c(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=3, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[d(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=4, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=false])]");
    }
    

    
    /**
     * Simple test case with warnings
     */
    @Test
    public void testDataProcessingUnitWarnings1() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ProcessingUnitStringTest.DATA_FEED, "a", "b", "", "d"));

        TestProcessingUnitRunner<ProcessingUnitStringTest> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(ProcessingUnitStringTest.class, parameterList), 4);
        
        assertEquals((processRunner.getProcesingUnit()).getResult(),
                 "[a(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=1, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[b(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=2, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=3, numberOfFailedUnits=1, processingRuntimeStatus=WARN, processingStatusMessage=Empty data, processingStatistic=[{}]], hasNext=true])] | "
               + "[d(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=4, numberOfFailedUnits=1, processingRuntimeStatus=WARN, processingStatusMessage=null, processingStatistic=[{}]], hasNext=false])]");
    }

    
    /**
     * Simple test case with warnings
     */
    @Test
    public void testDataProcessingUnitWarnings2() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ProcessingUnitStringTest.DATA_FEED, "a", "b", "", "d"));

        Random random = new Random();
        long randomSuspendIdx = random.nextInt(10 - 5 + 1) + 1;

        TestProcessingUnitRunner<ProcessingUnitStringTest> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithSuspendAndResume(ProcessingUnitStringTest.class, parameterList, randomSuspendIdx, 100L, 3), 4);

        assertEquals((processRunner.getProcesingUnit()).getResult(),
                 "[a(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=1, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[b(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=2, numberOfFailedUnits=0, processingRuntimeStatus=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=3, numberOfFailedUnits=1, processingRuntimeStatus=WARN, processingStatusMessage=Empty data, processingStatistic=[{}]], hasNext=true])] | "
               + "[d(" + HEADER + "[numberOfUnitsToProcess=4, numberOfProcessedUnits=4, numberOfFailedUnits=1, processingRuntimeStatus=WARN, processingStatusMessage=null, processingStatistic=[{}]], hasNext=false])]");
    }
    
    
    /**
     * Test missing mandatory parameters
     */
    @Test
    public void testMissingMandatoryParameters() {
        List<ParameterDefinition> list1 = new ArrayList<ParameterDefinition>();
        list1.add(new ParameterDefinition(PARAM1, ParameterValueType.STRING, ParameterDefinition.NOT_OPTIONAL, "The first parameter."));
        list1.add(new ParameterDefinition(PARAM2, ParameterValueType.STRING, ParameterDefinition.NOT_OPTIONAL, "The second parameter."));
        list1.add(new ParameterDefinition(PARAM3, ParameterValueType.STRING, ParameterDefinition.OPTIONAL, "The third parameter."));
        // no asserts needed because expected exceptions in case of not set mandatory parameters
        new ProcessingUnitParamterTest(list1).validateParameterList(Arrays.asList(new Parameter(PARAM1, VALUE1), new Parameter(PARAM2, VALUE2)));
        new ProcessingUnitParamterTest(list1).validateParameterList(Arrays.asList(new Parameter(PARAM1, VALUE1), new Parameter(PARAM2, VALUE2), new Parameter(PARAM3, VALUE3)));

        // missing first parameter
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            new ProcessingUnitParamterTest(list1).validateParameterList(Arrays.asList(new Parameter(PARAM2, VALUE2), new Parameter(PARAM3, VALUE3)));
        });
        assertEquals("The parameters are inconsistent: \n-missing mandatory parameter(s): [param1]! ", exception.getMessage());
    }


    class ProcessingUnitParamterTest extends AbstractProcessingUnitImpl {
        
        /**
         * Constructor for ProcessingUnitParamterTest
         *
         * @param parameterDefinitionList the parameter definition list
         */
        ProcessingUnitParamterTest(List<ParameterDefinition> parameterDefinitionList) {
            for (ParameterDefinition paramDef : parameterDefinitionList) {
                getParameterRuntime().addParameterDefinition(paramDef);
            }
        }


        /**
         * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
         */
        @Override
        public IProcessStatus processUnit(IProcessingUnitContext processingUnitContext) {
            return null;
        }


        /**
         * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#countNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
         */
        @Override
        protected long countNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) throws ProcessingException {
            return 0;
        }
    }
}
