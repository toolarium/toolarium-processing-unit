/*
 * TestProcessingUnitRunner.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.test;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitContext;
import java.io.Serializable;
import java.util.List;


/**
 * Implements a simple processing unit runner. It's made to use in unit tests.
 *
 * @author patrick
 */
public class TestProcessingUnitRunner implements Serializable {
    private static final long serialVersionUID = 7297801281265114031L;
    private TestProcessingUnitRunnable processingUnitRunnable;

    
    /**
     * Constructor
     */
    protected TestProcessingUnitRunner() {
        processingUnitRunnable = null;
    }


    /**
     * Run processing unit
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @return the number of cycles
     * @throws ValidationException In case the processing can not be initilized 
     * @throws ProcessingException In case of an error in a processing
     */
    public long run(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList) throws ValidationException, ProcessingException {
        return runWithSuspendAndResume(processingUnitClass, parameterList, null, null, null);
    }

    
    /**
     * Run processing unit and abort after some cycles
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param numberOfCyclesBeforeStop the number of cycles before stop
     * @return the number of cycles
     * @throws ValidationException In case the processing can not be initilized 
     * @throws ProcessingException In case of an error in a processing
     */
    public long runAndAbort(Class<? extends IProcessingUnit> processingUnitClass,
                            List<Parameter> parameterList,
                            Integer numberOfCyclesBeforeStop) throws ValidationException, ProcessingException {
        processingUnitRunnable = new TestProcessingUnitRunnable(processingUnitClass, parameterList, new ProcessingUnitContext());
        processingUnitRunnable.setNumberOfCyclesBeforeStop(numberOfCyclesBeforeStop);
        processingUnitRunnable.run();
        return processingUnitRunnable.getProcessStatus().getProcessingProgress().getProcessedUnits() + processingUnitRunnable.getProcessStatus().getProcessingProgress().getTotalFailedUnits();
    }


    /**
     * Run processing unit
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param suspendAfterCycles after the number of cycles it will suspended
     * @param suspendSleepTime the sleep time after suspending or null
     * @param maxNumberOfSuspends the max number of suspends
     * @return the number of cycles
     * @throws ValidationException In case the processing can not be initilized 
     * @throws ProcessingException In case of an error in a processing
     */
    public long runWithSuspendAndResume(Class<? extends IProcessingUnit> processingUnitClass,
                                        List<Parameter> parameterList,
                                        Long suspendAfterCycles,
                                        Long suspendSleepTime,
                                        Integer maxNumberOfSuspends) throws ValidationException, ProcessingException {
        processingUnitRunnable = new TestProcessingUnitRunnable(processingUnitClass, parameterList, new ProcessingUnitContext());
        processingUnitRunnable.setSuspendAfterCycles(suspendAfterCycles);
        processingUnitRunnable.setSuspendSleepTime(suspendSleepTime);
        processingUnitRunnable.setMaxNumberOfSuspends(maxNumberOfSuspends);
        processingUnitRunnable.run();
        return processingUnitRunnable.getProcessStatus().getProcessingProgress().getProcessedUnits() + processingUnitRunnable.getProcessStatus().getProcessingProgress().getTotalFailedUnits();
    }


    /**
     * Gets the suspend counter
     *
     * @return the suspend counter
     */
    public int getSuspendCounter() {
        if (processingUnitRunnable != null) {
            return processingUnitRunnable.getSuspendCounter();
        }
        
        return 0;
    }


    /**
     * Gets the last process status
     *
     * @return the last process status
     */
    public IProcessStatus getProcessStatus() {
        if (processingUnitRunnable != null) {
            return processingUnitRunnable.getProcessStatus();
        }
        
        return null;
    }


    /**
     * Get the processing unit.
     * IMPORTANT: Only to verify some results
     *
     * @return the processing unit
     */
    public IProcessingUnit getProcesingUnit() {
        if (processingUnitRunnable != null) {
            return processingUnitRunnable.getProcessingUnit();
        }
        
        return null;
    }
}
