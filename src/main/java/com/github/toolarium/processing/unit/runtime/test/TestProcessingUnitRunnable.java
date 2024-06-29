/*
 * TestProcessingUnitRunnable.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.test;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnableListener;
import com.github.toolarium.processing.unit.runtime.runnable.impl.ProcessingUnitRunnable;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements a {@link IProcessingUnitRunnable} for unit test cases
 * @author patrick
 */
public class TestProcessingUnitRunnable extends ProcessingUnitRunnable {
    private static final Logger LOG = LoggerFactory.getLogger(TestProcessingUnitRunnable.class);
    private Long suspendAfterCycles;
    private Integer numberOfCyclesBeforeStop;

    
    /**
     * Constructor for TestProcessingUnitRunnable
     * 
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingUnitContext the processing unit context
     */
    protected TestProcessingUnitRunnable(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) {
        super(null, null, processingUnitClass, parameterList, processingUnitContext, new LogProcessingUnitRunnableListener());
        this.suspendAfterCycles = null;
        this.numberOfCyclesBeforeStop = null;
    }

    
    /**
     * Constructor for TestProcessingUnitRunnable
     * 
     * @param suspendedState the suspended state
     */
    protected TestProcessingUnitRunnable(byte[] suspendedState) {
        super(suspendedState, new LogProcessingUnitRunnableListener());
        this.suspendAfterCycles = null;
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
     * Set the number of cycles before stop
     *
     * @param numberOfCyclesBeforeStop the number of cycles before stop
     */
    public void setNumberOfCyclesBeforeStop(Integer numberOfCyclesBeforeStop) {
        this.numberOfCyclesBeforeStop = numberOfCyclesBeforeStop;
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
     * @see com.github.toolarium.processing.unit.runtime.runnable.impl.AbstractProcessingUnitRunnable#getParameterList()
     */
    @Override
    public List<Parameter> getParameterList() {
        if (getProcessingUnitProxy() != null) {
            return getProcessingUnitProxy().getParameterList();
        }
        
        return null;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.impl.ProcessingUnitRunnable#getProcessingUnitThrottling()
     */
    @Override
    public IBandwidthThrottling getProcessingUnitThrottling() {
        return super.getProcessingUnitThrottling();
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.impl.AbstractProcessingUnitRunnable#afterProcessUnit(boolean)
     */
    @Override
    protected boolean afterProcessUnit(boolean continueProcessing) {
        if (suspendAfterCycles != null && suspendAfterCycles > 1 && (getProcessingUnitProxy().getProcessStatus().getProcessingProgress().getNumberOfProcessedUnits() % suspendAfterCycles == 0) /*&& suspendCounter < maxNumberOfSuspends*/) {
            suspendProcessing();
        }
        
        if (numberOfCyclesBeforeStop != null && numberOfCyclesBeforeStop > 0 && numberOfCyclesBeforeStop <= getProcessingUnitProxy().getProcessStatus().getProcessingProgress().getNumberOfProcessedUnits()) {
            return false;
        }
        
        return continueProcessing;
    }
    
    
    /**
     * Implement a log processing unit runnable listener
     * 
     * @author patrick
     */
    static class LogProcessingUnitRunnableListener implements IProcessingUnitRunnableListener {

        /**
         * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnableListener#notifyProcessingUnitState(java.lang.String, java.lang.String, java.lang.String, 
         *      com.github.toolarium.processing.unit.dto.ProcessingActionStatus, com.github.toolarium.processing.unit.IProcessingProgress, com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement, 
         *      com.github.toolarium.processing.unit.IProcessingUnitContext)
         */
        @Override
        public void notifyProcessingUnitState(final String id, 
                                              final String name, 
                                              final String processingUnitClass,
                                              final ProcessingActionStatus processingActionStatus, 
                                              final IProcessingProgress processingProgress,
                                              final IProcessingUnitRuntimeTimeMeasurement runtimeTimeMeasurment,
                                              final IProcessingUnitContext processingUnitContext) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(ProcessingUnitUtil.getInstance().prepare(id, name, processingUnitClass) + " status: " + processingActionStatus);
            }
        }
    }
}
