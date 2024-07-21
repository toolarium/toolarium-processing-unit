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
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunner;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunnerFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author patrick
 */
public class ParallelProcessingUnitTest {

    //@Test
    public void singleThreadTest() {
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ParallelProcessingUnitSample.NUMBER_OF_WORDS.getKey(), "128"));
        parameterList.add(new Parameter(ParallelProcessingUnit.AGGREGATE_STATUS_PAUSE_TIME.getKey(), "10"));
        //parameterList.add(new Parameter(TestProcessingUnit.SLEEP_TIME_BY_A_PROCESSING_PARAMTER.getKey(), "0"));
        //parameterList.add(new Parameter(TestProcessingUnit.END_AS_WARNING_PARAMTER.getKey(), TRUE));
        
        TestProcessingUnitRunner processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        processRunner.run(ParallelProcessingUnitSample.class, parameterList);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfProcessedUnits(), 2);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfSuccessfulUnits(), 2);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnprocessedUnits(), 0);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfUnitsToProcess(), 2);
        assertEquals(processRunner.getProcessingUnitProgress().getNumberOfFailedUnits(), 0);
        assertEquals(processRunner.getProcessingRuntimeStatus(), ProcessingRuntimeStatus.WARN);
        assertEquals(processRunner.getProcessingActionStatus(), ProcessingActionStatus.ENDED);
        assertEquals(processRunner.getStatusMessageList().toString(), "[Test warn message., Test warn message.]");
        
    }
}
