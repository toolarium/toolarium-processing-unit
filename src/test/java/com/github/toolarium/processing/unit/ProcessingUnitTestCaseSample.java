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
import com.github.toolarium.processing.unit.runtime.ProcessingUnitContext;
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

        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(MyDataProcessingUnit.class, parameterList), TOTAL_UNITS);

        assertEquals(processRunner.getSuspendCounter(), 0);
        assertNotNull(processRunner.getProcessingUnitProgress());
        assertFalse((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnAbortingStatus());
        assertTrue((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnEndingStatus());
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessingUnitProgress().getProcessingUnitStatistic(), "ProcessingUnitStatistic [counter=3.0, PROCEEDING=130.0, SHA-1=26.0, SHA-256=26.0]");
        assertNotNull(processRunner.getStatusMessageList());
        assertNotNull(processRunner.getTimeMeasurement().getStartTimestamp());
        assertNotNull(processRunner.getTimeMeasurement().getStopTimestamp());
        assertTrue(processRunner.getTimeMeasurement().getDuration() >= 0);        
    }


    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitWithOnwContext() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter("keyNames", "name1", "name2"));

        IProcessingUnitContext processingUnitContext = new ProcessingUnitContext();
        processingUnitContext.set("myInputKey", "1234");
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.processingUnitContext(processingUnitContext).run(MyDataProcessingUnit.class, parameterList), TOTAL_UNITS);

        assertEquals(processRunner.getSuspendCounter(), 0);
        assertNotNull(processRunner.getProcessingUnitProgress());
        assertFalse((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnAbortingStatus());
        assertTrue((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnEndingStatus());
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessingUnitProgress().getProcessingUnitStatistic(), "ProcessingUnitStatistic [counter=3.0, PROCEEDING=130.0, SHA-1=26.0, SHA-256=26.0]");
        assertNotNull(processRunner.getStatusMessageList());
        assertNotNull(processRunner.getTimeMeasurement().getStartTimestamp());
        assertNotNull(processRunner.getTimeMeasurement().getStopTimestamp());
        assertTrue(processRunner.getTimeMeasurement().getDuration() >= 0);        
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

        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(MyDataProcessingUnit.class, parameterList), TOTAL_UNITS);

        assertEquals(processRunner.getSuspendCounter(), 0);
        assertNotNull(processRunner.getProcessingUnitProgress());
        assertFalse(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnAbortingStatus());
        assertTrue(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnEndingStatus());
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessingUnitProgress().getProcessingUnitStatistic(), "ProcessingUnitStatistic [counter=22.0, MD5=26.0, PROCEEDING=130.0, SHA-1=26.0]");
    }


    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitWithSuspendAndResume() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        long randomSuspendIdx = new Random().nextInt(10 - 5 + 1) + 1;
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithSuspendAndResume(MyDataProcessingUnit.class, parameterList, randomSuspendIdx, 100L, 3), TOTAL_UNITS);

        //assertTrue(processRunner.getSuspendCounter() > (TOTAL_UNITS / randomSuspendIdx - 2));
        //assertTrue(processRunner.getSuspendCounter() < (TOTAL_UNITS / randomSuspendIdx) + 1);
        assertNotNull(processRunner.getProcessingUnitProgress());
        assertFalse((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnAbortingStatus());
        assertTrue((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnEndingStatus());
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessingUnitProgress().getProcessingUnitStatistic(), "ProcessingUnitStatistic [counter=3.0, PROCEEDING=130.0, SHA-1=26.0, SHA-256=26.0]");
        
        assertNotNull(processRunner.getStatusMessageList());
        assertNotNull(processRunner.getTimeMeasurement().getStartTimestamp());
        assertNotNull(processRunner.getTimeMeasurement().getStopTimestamp());
        assertTrue(processRunner.getTimeMeasurement().getDuration() > 0);        
    }


    /**
     * Simple test case with correct abort
     */
    @Test
    public void testProcessingUnitAbort() {
        List<Parameter> parameterList = new ArrayList<Parameter>();

        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runAndAbort(MyDataProcessingUnit.class, parameterList, 3), 3);

        //assertTrue(processRunner.getSuspendCounter() > (TOTAL_UNITS / randomSuspendIdx - 2));
        //assertTrue(processRunner.getSuspendCounter() < (TOTAL_UNITS / randomSuspendIdx) + 1);
        assertNotNull(processRunner.getProcessingUnitProgress());
        assertTrue((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnAbortingStatus());
        assertFalse((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnEndingStatus());
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 3);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessingUnitProgress().getProcessingUnitStatistic(), "ProcessingUnitStatistic [PROCEEDING=15.0, SHA-1=3.0, SHA-256=3.0]");
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitWithThrottling() {
        int totalUnits = 100;
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(MyDataProcessingUnitConstants.NUMBER_OF_TESTDATA_RECORDS.getKey(), "" + totalUnits));

        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithThrottling(MyDataProcessingUnit.class, parameterList, 10L), totalUnits);
        
        int avg = RoundUtil.getInstance().roundToInt(processRunner.getProcessingUnitThrottling().getBandwidthStatisticCounter().getAverage());
        assertTrue(avg >= 12 && avg <= 40, "" + processRunner.getProcessingUnitThrottling().getBandwidthStatisticCounter());
        avg = RoundUtil.getInstance().roundToInt(processRunner.getProcessingUnitThrottling().getSleepStatisticCounter().getAverage());
        assertTrue(avg >= 80 && avg <= 90, "" + processRunner.getProcessingUnitThrottling().getSleepStatisticCounter());

        assertNotNull(processRunner.getProcessingUnitProgress());
        assertFalse((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnAbortingStatus());
        assertTrue((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnEndingStatus());
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), totalUnits);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), totalUnits);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessingUnitProgress().getProcessingUnitStatistic(), "ProcessingUnitStatistic [counter=3.0, PROCEEDING=500.0, SHA-1=100.0, SHA-256=100.0]");
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitWithThrottlingWithSuspendAndResume() {
        int totalUnits = 100;
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(MyDataProcessingUnitConstants.NUMBER_OF_TESTDATA_RECORDS.getKey(), "" + totalUnits));

        long randomSuspendIdx = new Random().nextInt(10 - 5 + 1) + 1;
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithSuspendAndResume(MyDataProcessingUnit.class, parameterList, randomSuspendIdx, 100L, 5, 10L), totalUnits);
        
        assertNotNull(processRunner.getProcessingUnitThrottling());
        int avg = RoundUtil.getInstance().roundToInt(processRunner.getProcessingUnitThrottling().getBandwidthStatisticCounter().getAverage());
        assertTrue(avg >= 12 && avg <= 25, "" + processRunner.getProcessingUnitThrottling().getBandwidthStatisticCounter());
        avg = RoundUtil.getInstance().roundToInt(processRunner.getProcessingUnitThrottling().getSleepStatisticCounter().getAverage());
        assertTrue(avg >= 60 && avg <= 90, "" + processRunner.getProcessingUnitThrottling().getSleepStatisticCounter());

        assertNotNull(processRunner.getProcessingUnitProgress());
        assertFalse((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnAbortingStatus());
        assertTrue((((MyDataProcessingUnit)processRunner.getProcesingUnit())).getOnEndingStatus());
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), totalUnits);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), totalUnits);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessingUnitProgress().getProcessingUnitStatistic(), "ProcessingUnitStatistic [counter=3.0, PROCEEDING=500.0, SHA-1=100.0, SHA-256=100.0]");
    }

    
    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitSampleWithOwnPersistenceWithSuspendAndResume() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ProcessingUnitSampleWithOwnPersistence.INPUT_FILENAME_PARAMETER.getKey(), "file"));

        long randomSuspendIdx = new Random().nextInt(10 - 5 + 1) + 1;
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithSuspendAndResume(ProcessingUnitSampleWithOwnPersistence.class, parameterList, randomSuspendIdx, 3L, 5, 10L), 10);
        
        assertNotNull(processRunner.getProcessingUnitProgress());
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 10);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 10);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
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
            TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
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
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertTrue(processRunner.run(ProcessingUnitSample.class, parameterList) > 1);
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
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(ProcessingUnitSampleWithOwnPersistence.class, parameterList), 10);
    }
}
