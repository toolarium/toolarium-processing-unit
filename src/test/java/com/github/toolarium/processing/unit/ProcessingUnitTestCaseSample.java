/*
 * ProcessingUnitTestCaseSample.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.common.util.RoundUtil;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.mydata.MyDataProcessingUnit;
import com.github.toolarium.processing.unit.mydata.MyDataProcessingUnitConstants;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunner;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunnerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;


/**
 * Implements a simple test
 *
 * @author patrick
 */
public final class ProcessingUnitTestCaseSample {
    private static final int TOTAL_UNITS = 26;


    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnit() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter("keyNames", "name1", "name2"));

        TestProcessingUnitRunner<MyDataProcessingUnit> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(MyDataProcessingUnit.class, parameterList), TOTAL_UNITS);

        assertEquals(processRunner.getSuspendCounter(), 0);
        assertNotNull(processRunner.getProcessStatus());
        assertFalse((processRunner.getProcesingUnit()).getOnStopStatus());
        assertTrue((processRunner.getProcesingUnit()).getOnSuccessStatus());
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessStatus().getProcessingProgress().getProcesingStatistic(), "[{counter=3.0, PROCEEDING=130.0, SHA-1=26.0, SHA-256=26.0}]");
        assertNotNull(processRunner.getStatusMessageList());
        assertNotNull(processRunner.getStartTimestamp());
        assertNotNull(processRunner.getStopTimestamp());
        assertTrue(processRunner.getDuration() > 0);        
    }


    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitDefaultValue() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter("counter", "22"));
        parameterList.add(new Parameter("defaultValueTest"));
        parameterList.add(new Parameter("hashNames", "MD5", "SHA-1"));

        TestProcessingUnitRunner<MyDataProcessingUnit> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(MyDataProcessingUnit.class, parameterList), TOTAL_UNITS);

        assertEquals(processRunner.getSuspendCounter(), 0);
        assertNotNull(processRunner.getProcessStatus());
        assertFalse(processRunner.getProcesingUnit().getOnStopStatus());
        assertTrue(processRunner.getProcesingUnit().getOnSuccessStatus());
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessStatus().getProcessingProgress().getProcesingStatistic(), "[{counter=22.0, MD5=26.0, PROCEEDING=130.0, SHA-1=26.0}]");
    }


    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitWithSuspendAndResume() {
        List<Parameter> parameterList = new ArrayList<Parameter>();

        Random random = new Random();
        long randomSuspendIdx = random.nextInt(10 - 5 + 1) + 1;
        
        TestProcessingUnitRunner<MyDataProcessingUnit> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithSuspendAndResume(MyDataProcessingUnit.class, parameterList, randomSuspendIdx, 100L, 3), TOTAL_UNITS);

        //assertTrue(processRunner.getSuspendCounter() > (TOTAL_UNITS / randomSuspendIdx - 2));
        //assertTrue(processRunner.getSuspendCounter() < (TOTAL_UNITS / randomSuspendIdx) + 1);
        assertNotNull(processRunner.getProcessStatus());
        assertFalse((processRunner.getProcesingUnit()).getOnStopStatus());
        assertTrue((processRunner.getProcesingUnit()).getOnSuccessStatus());
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessStatus().getProcessingProgress().getProcesingStatistic(), "[{counter=3.0, PROCEEDING=130.0, SHA-1=26.0, SHA-256=26.0}]");
        
        assertNotNull(processRunner.getStatusMessageList());
        assertNotNull(processRunner.getStartTimestamp());
        assertNotNull(processRunner.getStopTimestamp());
        assertTrue(processRunner.getDuration() > 0);        
    }


    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitOnStop() {
        List<Parameter> parameterList = new ArrayList<Parameter>();

        TestProcessingUnitRunner<MyDataProcessingUnit> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runAndAbort(MyDataProcessingUnit.class, parameterList, 3), 3);

        //assertTrue(processRunner.getSuspendCounter() > (TOTAL_UNITS / randomSuspendIdx - 2));
        //assertTrue(processRunner.getSuspendCounter() < (TOTAL_UNITS / randomSuspendIdx) + 1);
        assertNotNull(processRunner.getProcessStatus());
        assertTrue((processRunner.getProcesingUnit()).getOnStopStatus());
        assertFalse((processRunner.getProcesingUnit()).getOnSuccessStatus());
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfProcessedUnits(), 3);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessStatus().getProcessingProgress().getProcesingStatistic(), "[{PROCEEDING=15.0, SHA-1=3.0, SHA-256=3.0}]");
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitWithTrhottling() {
        int totalUnits = 100;
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(MyDataProcessingUnitConstants.NUMBER_OF_TESTDATA_RECORDS.getKey(), "" + totalUnits));

        TestProcessingUnitRunner<MyDataProcessingUnit> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(MyDataProcessingUnit.class, parameterList), 100);
        
        processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithThrottling(MyDataProcessingUnit.class, parameterList, 10L), totalUnits);
        
        int avg = RoundUtil.getInstance().roundToInt(processRunner.getProcessingUnitThrottling().getBandwidthStatisticCounter().getAverage());
        assertTrue(avg >= 12 && avg <= 20, "" + processRunner.getProcessingUnitThrottling().getBandwidthStatisticCounter());
        avg = RoundUtil.getInstance().roundToInt(processRunner.getProcessingUnitThrottling().getSleepStatisticCounter().getAverage());
        assertTrue(avg >= 80 && avg <= 85, "" + processRunner.getProcessingUnitThrottling().getSleepStatisticCounter());

        assertNotNull(processRunner.getProcessStatus());
        assertFalse((processRunner.getProcesingUnit()).getOnStopStatus());
        assertTrue((processRunner.getProcesingUnit()).getOnSuccessStatus());
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfUnitsToProcess(), totalUnits);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfProcessedUnits(), totalUnits);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessStatus().getProcessingProgress().getProcesingStatistic(), "[{counter=3.0, PROCEEDING=500.0, SHA-1=100.0, SHA-256=100.0}]");
    }


    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitValidation() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter("keyNames", "name1", "name2"));
        parameterList.add(new Parameter(MyDataProcessingUnitConstants.COUNTER_PARAMETER.getKey(), "-1"));
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            TestProcessingUnitRunner<MyDataProcessingUnit> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
            processRunner.run(MyDataProcessingUnit.class, parameterList);
        });

        assertTrue(exception.getMessage().contains("Invalid parameter value of parameter [counter]"));
    }
    

    /**
     * Test the ProcessingUnitSample
     */
    @Test
    public void testProcessingUnitSample() {
        // set the input parameter
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "myFilename"));
        
        // Get a process runner and run the unit test
        TestProcessingUnitRunner<ProcessingUnitSample> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(ProcessingUnitSample.class, parameterList), 10);
    }


    /**
     * Test the ProcessingUnitSample
     */
    @Test
    public void testProcessingUnitSampleWithOwnPersistence() {
        // set the input parameter
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ProcessingUnitSampleWithOwnPersistence.INPUT_FILENAME_PARAMETER.getKey(), "myFilename"));
        
        // Get a process runner and run the unit test
        TestProcessingUnitRunner<ProcessingUnitSampleWithOwnPersistence> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(ProcessingUnitSampleWithOwnPersistence.class, parameterList), 10);
    }
}
