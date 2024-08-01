/*
 * ParallelProcessingUnitTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parallelization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.common.util.RoundUtil;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.framework.TextProducer;
import com.github.toolarium.processing.unit.framework.TextProducer.StringList;
import com.github.toolarium.processing.unit.runtime.runnable.parallelization.ParallelProcessingUnit;
import com.github.toolarium.processing.unit.runtime.runnable.parallelization.ParallelProcessingUnitParameters;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunner;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunnerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;


/**
 * Test the parallel processing unit
 *  
 * @author patrick
 */
public class ParallelProcessingUnitTest {
    private static final String TWO = "2";
    private static final int TOTAL_UNITS = 128;

    
    /**
     * Test parallel processing with just a single thread
     */
    @Test
    public void testMultithreadedProcessingUnitWithSingleInstanceTest() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ParallelProcessingUnitSample.NUMBER_OF_WORDS.getKey(), "" + TOTAL_UNITS));
        parameterList.add(new Parameter(ParallelProcessingUnit.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "10"));
        parameterList.add(new Parameter(ParallelProcessingUnitSample.ADD_RESULT_TO_CONTEXT.getKey(), "true"));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.LOCK_SIZE.getKey(), "10"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.UNLOCK_TIMEOUT.getKey(), "100L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.STARTUP_PHASED_SLEEP_TIME.getKey(), "10L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "50L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.NO_PROGRESS_PAUSE_TIME.getKey(), "500L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT.getKey(), "4"));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(ParallelProcessingUnitSample.class, parameterList), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.SUCCESSFUL);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[]");
        
        // compare result
        assertNotNull(processRunner.getProcessingUnitContext());
        assertNotNull(processRunner.getProcessingUnitContext().get(ParallelProcessingUnitSample.RESULT));
        StringList processedContent = TextProducer.getInstance().toStringList(processRunner.getProcessingUnitContext().get(ParallelProcessingUnitSample.RESULT));
        assertEquals(TOTAL_UNITS, processedContent.size());
    }


    /**
     * Test single instance
     */
    @Test
    public void testMultithreadedProcessingUnit() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ParallelProcessingUnitSample.NUMBER_OF_WORDS.getKey(), "" + TOTAL_UNITS));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.NUMBER_OF_THREAD_PARAMETER.getKey(), TWO));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.LOCK_SIZE.getKey(), "4"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.UNLOCK_TIMEOUT.getKey(), "100L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.STARTUP_PHASED_SLEEP_TIME.getKey(), "10L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "50L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.NO_PROGRESS_PAUSE_TIME.getKey(), "500L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT.getKey(), "4"));

        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(ParallelProcessingUnitSample.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.SUCCESSFUL);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[]");
    }


    /**
     * Test single instance
     */
    @Test
    public void testMultithreadedProcessingUnitWithSuspendAndResume() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ParallelProcessingUnitSample.NUMBER_OF_WORDS.getKey(), "" + TOTAL_UNITS));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.NUMBER_OF_THREAD_PARAMETER.getKey(), TWO));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.LOCK_SIZE.getKey(), TWO));
        //parameterList.add(new Parameter(ParallelProcessingUnitSample.ADD_RESULT_TO_CONTEXT.getKey(), "true"));

        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.UNLOCK_TIMEOUT.getKey(), "100L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.STARTUP_PHASED_SLEEP_TIME.getKey(), "10L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "50L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.NO_PROGRESS_PAUSE_TIME.getKey(), "500L"));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT.getKey(), "10"));

        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        long randomSuspendIdx = new Random().nextInt(20 - 10 + 1) + 1;
        assertEquals(processRunner.runWithSuspendAndResume(ParallelProcessingUnitSample.class, parameterList, randomSuspendIdx, 100L, 3), TOTAL_UNITS);
        assertNotNull(processRunner.getStatusMessageList());
        assertNotNull(processRunner.getTimeMeasurement().getStartTimestamp());
        assertNotNull(processRunner.getTimeMeasurement().getStopTimestamp());
        assertTrue(processRunner.getTimeMeasurement().getDuration() > 0);        
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.SUCCESSFUL);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[]");
        
        assertNotNull(((ParallelProcessingUnit)processRunner.getProcesingUnit()).getObjectLockManager());
        assertNotNull(((ParallelProcessingUnit)processRunner.getProcesingUnit()).getObjectLockManager().getLockStatistic());
        assertNotNull(((ParallelProcessingUnit)processRunner.getProcesingUnit()).getObjectLockManager().getUnlockStatistic()); // 
        assertNotNull(((ParallelProcessingUnit)processRunner.getProcesingUnit()).getObjectLockManager().getIgnoreLockStatistic()); // already locked statistic
    }


    /**
     * Simple test case with correct abort
     */
    @Test
    public void testMultithreadedProcessingUnitAbort() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ParallelProcessingUnitSample.NUMBER_OF_WORDS.getKey(), "128"));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.NUMBER_OF_THREAD_PARAMETER.getKey(), TWO));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.LOCK_SIZE.getKey(), TWO));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.UNLOCK_TIMEOUT.getKey(), "100L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.STARTUP_PHASED_SLEEP_TIME.getKey(), "10L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "50L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.NO_PROGRESS_PAUSE_TIME.getKey(), "500L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT.getKey(), "4"));

        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        long num = processRunner.runAndAbort(ParallelProcessingUnitSample.class, parameterList, 10);
        assertTrue(num >= 10);
        assertNotNull(processRunner.getStatusMessageList());
        assertNotNull(processRunner.getTimeMeasurement().getStartTimestamp());
        assertNull(processRunner.getTimeMeasurement().getStopTimestamp());
        assertTrue(processRunner.getTimeMeasurement().getDuration() > 0);        
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), num);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), num);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), TOTAL_UNITS - num);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.SUCCESSFUL);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ABORTED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[]");
        assertNull(processRunner.getProcessingUnitThrottling());
    }
    
    
    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitWithThrottling() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ParallelProcessingUnitSample.NUMBER_OF_WORDS.getKey(), "" + TOTAL_UNITS));
        parameterList.add(new Parameter(ParallelProcessingUnit.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "10"));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.LOCK_SIZE.getKey(), "10"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.UNLOCK_TIMEOUT.getKey(), "100L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.STARTUP_PHASED_SLEEP_TIME.getKey(), "10L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "50L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.NO_PROGRESS_PAUSE_TIME.getKey(), "500L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT.getKey(), "4"));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithThrottling(ParallelProcessingUnitSample.class, parameterList, 2L), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.SUCCESSFUL);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[]");

        assertNotNull(processRunner.getProcessingUnitThrottling());
        int avg = RoundUtil.getInstance().roundToInt(processRunner.getProcessingUnitThrottling().getBandwidthStatisticCounter().getAverage());
        assertTrue(avg >= 2 && avg <= 10, "" + processRunner.getProcessingUnitThrottling().getBandwidthStatisticCounter());
        avg = RoundUtil.getInstance().roundToInt(processRunner.getProcessingUnitThrottling().getSleepStatisticCounter().getAverage());
        assertTrue(avg >= 40 && avg <= 100, "" + processRunner.getProcessingUnitThrottling().getSleepStatisticCounter());
    }
}
