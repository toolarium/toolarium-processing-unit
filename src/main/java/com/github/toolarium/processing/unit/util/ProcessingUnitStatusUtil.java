/*
 * ProcessingUnitStatusUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.util;

import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;


/**
 * Defines the processing unit status util
 *  
 * @author patrick
 */
public final class ProcessingUnitStatusUtil {

    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ProcessingUnitStatusUtil INSTANCE = new ProcessingUnitStatusUtil();
    }

    
    /**
     * Constructor
     */
    private ProcessingUnitStatusUtil() {
        // NOP
    }
    

    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ProcessingUnitStatusUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Aggregate all processing unit status 
     * 
     * @param processingUnitStatusBuilder the processing status builder
     * @param processingUnitStatus the processing unit status
     * @return the processing status builder
     */
    public ProcessingUnitStatusBuilder aggregateProcessingUnitStatus(ProcessingUnitStatusBuilder processingUnitStatusBuilder, IProcessingUnitStatus processingUnitStatus) {
        if (processingUnitStatus == null || processingUnitStatusBuilder == null) {
            return processingUnitStatusBuilder;
        }
        
        // update figures
        processingUnitStatusBuilder.increaseNumberOfSuccessfulUnits(processingUnitStatus.getNumberOfSuccessfulUnits());
        processingUnitStatusBuilder.increaseNumberOfFailedUnits(processingUnitStatus.getNumberOfFailedUnits());
        processingUnitStatusBuilder.increaseNumberOfUnprocessedUnits(processingUnitStatus.getNumberOfUnprocessedUnits());
        
        // set status
        ProcessingRuntimeStatus processingRuntimeStatus = processingUnitStatus.getProcessingRuntimeStatus();
        if (ProcessingRuntimeStatus.WARN.equals(processingRuntimeStatus)) {
            processingUnitStatusBuilder.hasWarning();
        } else if (ProcessingRuntimeStatus.ERROR.equals(processingRuntimeStatus)) {
            processingUnitStatusBuilder.hasError();
        }

        // add message
        if (processingUnitStatus.getStatusMessage() != null && !processingUnitStatus.getStatusMessage().isBlank()) {
            processingUnitStatusBuilder.message(processingUnitStatus.getStatusMessage());
        }

        // add statistic
        if (processingUnitStatus.getProcessingUnitStatistic() != null && !processingUnitStatus.getProcessingUnitStatistic().isEmpty()) {
            for (String key : processingUnitStatus.getProcessingUnitStatistic().keySet()) {
                processingUnitStatusBuilder.statistic(key, processingUnitStatus.getProcessingUnitStatistic().get(key));
            }
        }

        processingUnitStatusBuilder.hasNext(processingUnitStatusBuilder.hasNext() || processingUnitStatus.hasNext());
        return processingUnitStatusBuilder;
    }
}
