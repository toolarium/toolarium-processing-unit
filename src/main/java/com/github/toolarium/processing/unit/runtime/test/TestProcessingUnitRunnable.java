/*
 * TestProcessingUnitRunnable.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.test;

import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import com.github.toolarium.processing.unit.runtime.runnable.ProcessingUnitProxy;
import com.github.toolarium.processing.unit.runtime.runnable.impl.ProcessingUnitRunnableImpl;
import java.util.List;


/**
 * Implements a {@link IProcessingUnitRunnable} for unit test cases
 * @author patrick
 */
public class TestProcessingUnitRunnable extends ProcessingUnitRunnableImpl {
    private Long suspendAfterCycles;
    private Long suspendSleepTime;
    private Integer maxNumberOfSuspends;
    private volatile int suspendCounter;
    private Integer numberOfCyclesBeforeStop;
    
    
    /**
     * Constructor for TestProcessingUnitRunnable
     * 
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingUnitContext the processing unit context
     */
    protected TestProcessingUnitRunnable(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) {
        super(processingUnitClass, parameterList, processingUnitContext);
        this.suspendAfterCycles = null;
        this.suspendSleepTime = null;
        this.maxNumberOfSuspends = 0;
        this.suspendCounter = 0;
        this.numberOfCyclesBeforeStop = null;
    }

    
    /**
     * Set the number of cycles after the number of cycles it will suspended
     * 
     * @param suspendAfterCycles the number of cycles it will suspended
     */
    public void setSuspendAfterCycles(Long suspendAfterCycles) {
        this.suspendAfterCycles = suspendAfterCycles;
    }

    
    /**
     * Set the suspend sleep time
     *
     * @param suspendSleepTime the suspend sleep time
     */
    public void setSuspendSleepTime(Long suspendSleepTime) {
        this.suspendSleepTime = suspendSleepTime;
    }

    
    /**
     * Set the max number of suspends
     * 
     * @param maxNumberOfSuspends the max number of suspends
     */
    public void setMaxNumberOfSuspends(Integer maxNumberOfSuspends) {
        this.maxNumberOfSuspends = maxNumberOfSuspends;
    }

    
    /**
     * Set the number of cycles before stop
     *
     * @param numberOfCyclesBeforeStop the number of cycles before stop
     */
    public void setNumberOfCyclesBeforeStop(Integer numberOfCyclesBeforeStop) {
        this.numberOfCyclesBeforeStop = numberOfCyclesBeforeStop;
    }


    /**
     * Gets the suspend counter
     *
     * @return the suspend counter
     */
    public int getSuspendCounter() {
        return suspendCounter;
    }

    
    /**
     * Get the processing unit
     *
     * @return the processing unit
     */
    public IProcessingUnit getProcessingUnit() {
        if (getProcessingUnitProxy() != null) {
            return getProcessingUnitProxy().getProcessingUnit();
        }
        
        return null;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.impl.ProcessingUnitRunnableImpl#afterProcessUnit(boolean)
     */
    @Override
    protected boolean afterProcessUnit(boolean continueProcessing) {
        if (suspendAfterCycles != null && suspendAfterCycles > 1 && (getProcessingUnitProxy().getProcessStatus().getProcessingProgress().getProcessedUnits() % suspendAfterCycles == 0) && suspendCounter < maxNumberOfSuspends) {
            // persist...
            final byte[] persisted = getProcessingUnitProxy().suspendProcessing();
            setProcessingUnitProxy(null);
            suspendCounter++;

            // sleep..
            ThreadUtil.getInstance().sleep(suspendSleepTime);

            // resume
            setProcessingUnitProxy(ProcessingUnitProxy.resume(persisted));
        }

        if (numberOfCyclesBeforeStop != null && numberOfCyclesBeforeStop > 0 && numberOfCyclesBeforeStop <= getProcessingUnitProxy().getProcessStatus().getProcessingProgress().getProcessedUnits()) {
            return false;
        }
        
        return continueProcessing;
    }
}
