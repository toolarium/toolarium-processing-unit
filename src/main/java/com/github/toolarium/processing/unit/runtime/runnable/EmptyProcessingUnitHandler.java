/*
 * EmptyProcessingUnitHandler.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link IEmptyProcessingUnitHandler}.
 * 
 * @author patrick
 */
public class EmptyProcessingUnitHandler implements IEmptyProcessingUnitHandler {
    private static final long serialVersionUID = 506266867959666390L;
    private static final Logger LOG = LoggerFactory.getLogger(EmptyProcessingUnitHandler.class);
    private Long maxNumberOfEmptyProcessingUnits;
    private Long emptyProceessingUnitSleepTime;
    private long numberOfEmptyProcessingUnitRuns;
    private long duration;
    
    
    /**
     * Constructor for EmptyProcessingUnitHandler
     */
    public EmptyProcessingUnitHandler() {
        maxNumberOfEmptyProcessingUnits = 10L;
        emptyProceessingUnitSleepTime = 500L;
        numberOfEmptyProcessingUnitRuns = 0L;
        duration = 0L;
    }
    
    
    /**
     * Get the maximum number of empty processing units before it aborts. An empty processing unit mean that 
     * there was no progress: no failed nor successful units processed determined by the 
     * {@link IProcessingUnitStatus}. The maximum number indicates after how many empty runs the 
     * processing unit will abort.
     * 
     * @return the max. number of processing unit runs with no progress
     */
    public Long getMaxNumberOfEmptyProcessingUnits() {
        return maxNumberOfEmptyProcessingUnits;
    }

    
    /**
     * Defines the max. number of processing unit runs with no progress (no failed nor successful) before it aborts.
     *
     * @param maxNumberOfEmptyProcessingUnits the max number of processing unit runs with no progress before it aborts
     */
    public void setMaxNumberOfEmptyProcessingUnits(Long maxNumberOfEmptyProcessingUnits) {
        this.maxNumberOfEmptyProcessingUnits = maxNumberOfEmptyProcessingUnits;
    }

    
    /**
     * Get the sleep time of an empty processing unit run after there was no progress (no failed nor successful units).
     * 
     * @return the sleep time in milliseconds
     */
    public Long getSleepTimeAfterEmptyProcessingUnit() {
        return emptyProceessingUnitSleepTime;
    }

    
    /**
     * Defines the sleep time of an empty processing unit after there was no progress (no failed nor successful units).
     *
     * @param emptyProceessingUnitSleepTime the sleep time in milliseconds
     */
    public void setSleepTimeAfterEmptyProcessingUnit(Long emptyProceessingUnitSleepTime) {
        this.emptyProceessingUnitSleepTime = emptyProceessingUnitSleepTime;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IEmptyProcessingUnitHandler#handle(java.lang.String, java.lang.String, long, com.github.toolarium.processing.unit.IProcessingUnitProgress)
     */
    @Override
    public boolean handle(String id, String name, long threadId, IProcessingUnitProgress processingUnitProgress) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(ProcessingUnitUtil.getInstance().toString(id, name, (String)null) + " Detected empty processing unit run (no progress)");
        }
        
        numberOfEmptyProcessingUnitRuns++;
        
        boolean continueProcessing = false;
        if (getMaxNumberOfEmptyProcessingUnits() != null && numberOfEmptyProcessingUnitRuns < getMaxNumberOfEmptyProcessingUnits()) {
            continueProcessing = true;
            
            if (getSleepTimeAfterEmptyProcessingUnit() != null && getSleepTimeAfterEmptyProcessingUnit() > 0) {
                long start = System.currentTimeMillis();
                ThreadUtil.getInstance().sleep(getSleepTimeAfterEmptyProcessingUnit());
                duration += System.currentTimeMillis() - start;
            }
        } else {
            LOG.info(ProcessingUnitUtil.getInstance().toString(id, name, (String)null) + " Detected empty processing unit run (max " + getMaxNumberOfEmptyProcessingUnits() + " reached, aborting)");
            continueProcessing = false;
        }
        
        return continueProcessing;
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IEmptyProcessingUnitHandler#getDuration()
     */
    @Override
    public long getDuration() {
        return duration;
    }
}
