/*
 * ProcessingFrameworkTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.framework;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.test.ProcessingUnitRunnerFactory;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunner;
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

        TestProcessingUnitRunner processRunner = ProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(ProcessingUnitStringTest.class, parameterList), 4);

        assertEquals(((ProcessingUnitStringTest)processRunner.getProcesingUnit()).getResult(),
                 "[a(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=1, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[b(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=2, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[c(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=3, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[d(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=4, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=false])]");
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

        TestProcessingUnitRunner processRunner = ProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithSuspendAndResume(ProcessingUnitStringTest.class, parameterList, randomSuspendIdx, 100L, 3), 4);

        assertEquals(((ProcessingUnitStringTest)processRunner.getProcesingUnit()).getResult(),
                 "[a(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=1, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[b(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=2, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[c(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=3, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[d(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=4, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=false])]");
    }
    

    
    /**
     * Simple test case with warnings
     */
    @Test
    public void testDataProcessingUnitWarnings1() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ProcessingUnitStringTest.DATA_FEED, "a", "b", "", "d"));

        TestProcessingUnitRunner processRunner = ProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(ProcessingUnitStringTest.class, parameterList), 4);
        
        assertEquals(((ProcessingUnitStringTest)processRunner.getProcesingUnit()).getResult(),
                 "[a(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=1, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[b(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=2, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=2, totalFailedUnits=1, processingStatusType=WARN, processingStatusMessage=Empty data, processingStatistic=[{}]], hasNext=true])] | "
               + "[d(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=3, totalFailedUnits=1, processingStatusType=WARN, processingStatusMessage=Empty data, processingStatistic=[{}]], hasNext=false])]");
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

        TestProcessingUnitRunner processRunner = ProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithSuspendAndResume(ProcessingUnitStringTest.class, parameterList, randomSuspendIdx, 100L, 3), 4);

        assertEquals(((ProcessingUnitStringTest)processRunner.getProcesingUnit()).getResult(),
                 "[a(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=1, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[b(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=2, totalFailedUnits=0, processingStatusType=SUCCESSFUL, processingStatusMessage=null, processingStatistic=[{}]], hasNext=true])] | "
               + "[(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=2, totalFailedUnits=1, processingStatusType=WARN, processingStatusMessage=Empty data, processingStatistic=[{}]], hasNext=true])] | "
               + "[d(ProcessStatusImpl [processingProgress=ProcessingProgressImpl [totalUnits=4, processedUnits=3, totalFailedUnits=1, processingStatusType=WARN, processingStatusMessage=Empty data, processingStatistic=[{}]], hasNext=false])]");
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
    }
}
