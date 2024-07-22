/*
 * ParallelProcessingUnitTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parallelization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.runnable.parallelization.ParallelProcessingUnit;
import com.github.toolarium.processing.unit.runtime.runnable.parallelization.ParallelProcessingUnitParameters;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunner;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunnerFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Test the parallel processing unit
 *  
 * @author patrick
 */
public class ParallelProcessingUnitTest {

    /**
     * Test single instance
     */
    @Test
    public void singleInstanceTest() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ParallelProcessingUnitSample.NUMBER_OF_WORDS.getKey(), "128"));
        parameterList.add(new Parameter(ParallelProcessingUnit.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "10"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.LOCK_SIZE.getKey(), "10"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.UNLOCK_TIMEOUT.getKey(), "100L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.STARTUP_PHASED_SLEEP_TIME.getKey(), "10L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "50L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.NO_PROGRESS_PAUSE_TIME.getKey(), "500L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT.getKey(), "4"));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(ParallelProcessingUnitSample.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 128);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), 128);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 128);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.SUCCESSFUL);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[]");
    }


    /**
     * Test single instance
     */
    @Test
    public void multithreadedInstanceTest() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ParallelProcessingUnitSample.NUMBER_OF_WORDS.getKey(), "128"));
        parameterList.add(new Parameter(ParallelProcessingUnitParameters.NUMBER_OF_THREAD_PARAMETER.getKey(), "4"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.LOCK_SIZE.getKey(), "10"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.UNLOCK_TIMEOUT.getKey(), "100L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.STARTUP_PHASED_SLEEP_TIME.getKey(), "10L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "50L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.NO_PROGRESS_PAUSE_TIME.getKey(), "500L"));
        //parameterList.add(new Parameter(ParallelProcessingUnitParameters.MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT.getKey(), "4"));

        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(ParallelProcessingUnitSample.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 128);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), 128);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 128);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.SUCCESSFUL);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[]");
    }
}
