/*
 * ProcessingUnitStatusBuilder.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitStatus;


/**
 * Defines the processing unit status builder
 *  
 * @author patrick
 */
public class ProcessingUnitStatusBuilder {
    private ProcessingUnitStatus processingUnitStatus;
    
    
    /**
     * Constructor for ProcessingUnitStatusBuilder
     */
    public ProcessingUnitStatusBuilder() {
        processingUnitStatus = new ProcessingUnitStatus();
    }

    
    /**
     * Mark there are more units to process
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder hasNext() {
        processingUnitStatus.setHasNext(true);
        return this;
    }

    
    /**
     * Verify if there are unprocessed processing units
     *
     * @param processingUnitProgress the processing unit progress
     * @return this instance
     */
    public ProcessingUnitStatusBuilder hasNext(IProcessingUnitProgress processingUnitProgress) {
        processingUnitStatus.setHasNext(processingUnitProgress.getNumberOfUnprocessedUnits() > 1);
        return this;
    }

    
    /**
     * Mark there are no more units to process
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder hasEnded() {
        processingUnitStatus.setHasNext(false);
        return this;
    }

    
    /**
     * Mark a successful processed processing unit. 
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder processedSuccessful() {
        return numberOfSuccessfulUnits(1);
    }

    
    /**
     * Set the number of failed units. 
     *
     * @param numberOfSuccessfulUnits the number of successful units
     * @return this instance
     */
    public ProcessingUnitStatusBuilder numberOfSuccessfulUnits(long numberOfSuccessfulUnits) {
        if (numberOfSuccessfulUnits > 0) {
            processingUnitStatus.setNumberOfSuccessfulUnits(numberOfSuccessfulUnits);
        }
        
        return this;
    }

    
    /**
     * Mark a failed processed processing unit. 
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder processingUnitFailed() {
        return numberOfFailedUnits(1);
    }
    
    
    /**
     * Set the number of failed units. 
     *
     * @param numberOfFailedUnits the number of failed units
     * @return this instance
     */
    public ProcessingUnitStatusBuilder numberOfFailedUnits(long numberOfFailedUnits) {
        if (numberOfFailedUnits > 0) {
            processingUnitStatus.setNumberOfFailedUnits(numberOfFailedUnits);
        }
        return this;
    }

    
    /**
     * Set optionally the number of unprocessed units 
     *
     * @param numberOfUnprocessedUnits the number of unprocessed units
     * @return this instance
     */
    public ProcessingUnitStatusBuilder numberOfUnprocessedUnits(long numberOfUnprocessedUnits) {
        if (numberOfUnprocessedUnits >= 0) {
            processingUnitStatus.setNumberOfUnprocessedUnits(numberOfUnprocessedUnits);
        }
        
        return this;
    }

    
    /**
     * Processing has warning. 
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder hasWarning() {
        processingUnitStatus.setProcessingRuntimeStatus(ProcessingRuntimeStatus.WARN);
        return this;
    }

    
    /**
     * Processing has warning. 
     *
     * @param message the message
     * @return this instance
     */
    public ProcessingUnitStatusBuilder warn(String message) {
        return hasWarning().message(message);
    }

    
    /**
     * Processing has warning. 
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder hasError() {
        processingUnitStatus.setProcessingRuntimeStatus(ProcessingRuntimeStatus.ERROR);
        return this;
    }

    
    /**
     * Processing has warning. 
     *
     * @param message the message
     * @return this instance
     */
    public ProcessingUnitStatusBuilder error(String message) {
        return hasError().message(message);
    }

    
    /**
     * Processing has warning. 
     *
     * @param message the message
     * @return this instance
     */
    public ProcessingUnitStatusBuilder message(String message) {
        processingUnitStatus.setStatusMessage(message);
        return this;
    }

    
    /**
     * Add a statistic value 
     *
     * @param key the statistic key
     * @param value the value to add
     * @return this instance
     */
    public ProcessingUnitStatusBuilder statistic(String key, Long value) {
        processingUnitStatus.addStatistic(key, value);
        return this;
    }

    
    /**
     * Add a statistic value 
     *
     * @param key the statistic key
     * @param value the value to add
     * @return this instance
     */
    public ProcessingUnitStatusBuilder statistic(String key, Double value) {
        processingUnitStatus.addStatistic(key, value);
        return this;
    }


    /**
     * Build the processing unit status
     *
     * @return the processing unit status
     */
    public IProcessingUnitStatus build() {
        return processingUnitStatus;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return processingUnitStatus.toString();
    }
}
