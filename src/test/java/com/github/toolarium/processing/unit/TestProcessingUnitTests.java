/*
 * TestProcessingUnitTests.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnit;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunner;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunnerFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link TestProcessingUnit}.
 *  
 * @author patrick
 */
public class TestProcessingUnitTests {
    private static final String ZERO = "0";
    private static final String TRUE = "true";


    /**
     * Simple test case with correct
     */
    @Test
    public void testNormalRun() {
        int number = 20;
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), "" + number));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(TestProcessingUnit.class, parameterList), number);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), number);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), number);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), number);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testThrowRuntimeExceptionInValidation() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.THROW_RUNTIME_EXCEPTION_IN_VALIDATION_PARAMTER.getKey(), TRUE));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
            processRunner.run(TestProcessingUnit.class, parameterList);
        });
        
        assertEquals("Test runtime exception in validation.", exception.getMessage());
    }
    
    
    /**
     * Simple test case with correct
     */
    @Test
    public void testThrowValidationExceptionInValidation() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.THROW_VALIDATION_EXCEPTION_IN_VALIDATION_PARAMTER.getKey(), TRUE));
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
            processRunner.run(TestProcessingUnit.class, parameterList);
        });
        
        assertEquals("Test validation exception in validation.", exception.getMessage());
    }
    
    
    /**
     * Simple test case with correct
     */
    @Test
    public void testThrowRuntimeExceptionByFirstCall() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.THROW_RUNTIME_EXCEPTION_PARAMTER.getKey(), TRUE));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(TestProcessingUnit.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.ERROR);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ABORTED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[Test runtime exception in processing.]");
    }


    /**
     * Simple test case with correct
     */
    @Test
    public void testThrowRuntimeExceptionBySecondCall() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), "1"));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.THROW_RUNTIME_EXCEPTION_PARAMTER.getKey(), TRUE));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(TestProcessingUnit.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 1);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 1);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.ERROR);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ABORTED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[Test runtime exception in processing.]");
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testThrowRuntimeException() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), "100"));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.THROW_RUNTIME_EXCEPTION_PARAMTER.getKey(), TRUE));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(TestProcessingUnit.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 10);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), 10);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 90);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 100);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.ERROR);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ABORTED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[Test runtime exception in processing.]");
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testThrowProcessingException() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), "100"));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.THROW_PROCESSING_EXCEPTION_PARAMTER.getKey(), TRUE));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(TestProcessingUnit.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 100);
        assertTrue(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits() > 40);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 100);
        assertTrue(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits() > 5);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.WARN);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertTrue(processRunner.getStatusMessageList().toString().startsWith("[Test process exception in processing."));
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testThrowProcessingExceptionAndAbort() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), "100"));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.THROW_PROCESSING_EXCEPTION_AND_ABORT_PARAMTER.getKey(), TRUE));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(TestProcessingUnit.class, parameterList);
        assertTrue(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits() > 5);
        assertTrue(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits() > 5);
        assertTrue(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits() > 10);
        assertTrue(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits() > 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.ERROR);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ABORTED);
        assertTrue(processRunner.getStatusMessageList().toString().startsWith("[Test process exception in processing."));
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testEndWithWarning() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), "2"));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.END_AS_WARNING_PARAMTER.getKey(), TRUE));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(TestProcessingUnit.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 2);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), 2);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 2);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.WARN);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[Test warn message., Test warn message.]");
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testEndWithError() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(TestProcessingUnit.NUMBER_OF_UNITS_TO_PROCESS_PARAMETER.getKey(), "2"));
        parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), ZERO));
        parameterList.add(new Parameter(TestProcessingUnit.END_AS_ERROR_PARAMTER.getKey(), TRUE));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(TestProcessingUnit.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 2);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), 2);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 2);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.ERROR);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[Test error message., Test error message.]");
    }

    
    /*
         PERCENTAGE_NUMBER_OF_UNITS_TO_FAIL_PARAMTER
         STATISIIC_KEYS_PARAMTER
         STATISIIC_MAX_NUMBER_PARAMTER 
     */
}
