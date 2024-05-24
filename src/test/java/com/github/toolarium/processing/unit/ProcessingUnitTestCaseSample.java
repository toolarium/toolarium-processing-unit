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

import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.mydata.MyDataProcessingUnit;
import com.github.toolarium.processing.unit.mydata.MyDataProcessingUnitConstants;
import com.github.toolarium.processing.unit.runtime.test.ProcessingUnitRunnerFactory;
import com.github.toolarium.processing.unit.runtime.test.TestProcessingUnitRunner;
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

        TestProcessingUnitRunner processRunner = ProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(MyDataProcessingUnit.class, parameterList), TOTAL_UNITS);

        assertEquals(processRunner.getSuspendCounter(), 0);
        assertNotNull(processRunner.getProcessStatus());
        assertFalse(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnStopStatus());
        assertTrue(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnSuccessStatus());
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getTotalUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getTotalFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessStatus().getProcessingProgress().getProcesingStatistic(), "[{counter=3.0, PROCEEDING=130.0, SHA-1=26.0, SHA-256=26.0}]");
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

        TestProcessingUnitRunner processRunner = ProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(MyDataProcessingUnit.class, parameterList), TOTAL_UNITS);

        assertEquals(processRunner.getSuspendCounter(), 0);
        assertNotNull(processRunner.getProcessStatus());
        assertFalse(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnStopStatus());
        assertTrue(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnSuccessStatus());
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getTotalUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getTotalFailedUnits(), 0);
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
        TestProcessingUnitRunner processRunner = ProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runWithSuspendAndResume(MyDataProcessingUnit.class, parameterList, randomSuspendIdx, 100L, 3), TOTAL_UNITS);

        //assertTrue(processRunner.getSuspendCounter() > (TOTAL_UNITS / randomSuspendIdx - 2));
        //assertTrue(processRunner.getSuspendCounter() < (TOTAL_UNITS / randomSuspendIdx) + 1);
        assertNotNull(processRunner.getProcessStatus());
        assertFalse(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnStopStatus());
        assertTrue(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnSuccessStatus());
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getTotalUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getProcessedUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getTotalFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessStatus().getProcessingProgress().getProcesingStatistic(), "[{counter=3.0, PROCEEDING=130.0, SHA-1=26.0, SHA-256=26.0}]");
    }


    /**
     * Simple test case with correct
     */
    @Test
    public void testProcessingUnitOnStop() {
        List<Parameter> parameterList = new ArrayList<Parameter>();

        TestProcessingUnitRunner processRunner = ProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.runAndAbort(MyDataProcessingUnit.class, parameterList, 3), 3);

        //assertTrue(processRunner.getSuspendCounter() > (TOTAL_UNITS / randomSuspendIdx - 2));
        //assertTrue(processRunner.getSuspendCounter() < (TOTAL_UNITS / randomSuspendIdx) + 1);
        assertNotNull(processRunner.getProcessStatus());
        assertTrue(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnStopStatus());
        assertFalse(((MyDataProcessingUnit)processRunner.getProcesingUnit()).getOnSuccessStatus());
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getTotalUnits(), TOTAL_UNITS);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getProcessedUnits(), 3);
        assertEquals(processRunner.getProcessStatus().getProcessingProgress().getTotalFailedUnits(), 0);
        assertEquals("" + processRunner.getProcessStatus().getProcessingProgress().getProcesingStatistic(), "[{PROCEEDING=15.0, SHA-1=3.0, SHA-256=3.0}]");
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
            TestProcessingUnitRunner processRunner = ProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
            processRunner.run(MyDataProcessingUnit.class, parameterList);
        });

        assertTrue(exception.getMessage().contains("Invalid parameter value of parameter [counter]"));
    }
}
