/*
 * ProcessingUnitStatusBuilder.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.common.statistic.StatisticCounter;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitProgress;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitStatus;


/**
 * Defines the processing unit status builder
 *  
 * @author patrick
 */
public class ProcessingUnitStatusBuilder {
    private ProcessingUnitProgress processingUnitProgress;
    private ProcessingUnitStatus processingUnitStatus;
    
    
    /**
     * Constructor for ProcessingUnitStatusBuilder
     */
    public ProcessingUnitStatusBuilder() {
        this.processingUnitProgress = new ProcessingUnitProgress();
        this.processingUnitStatus = new ProcessingUnitStatus();
    }

    
    /**
     * Constructor for ProcessingUnitStatusBuilder
     *
     * @param processingUnitProgress the processing unit progress
     */
    public ProcessingUnitStatusBuilder(ProcessingUnitProgress processingUnitProgress) {
        this.processingUnitProgress = processingUnitProgress;
        this.processingUnitStatus = new ProcessingUnitStatus();
    }

    
    /**
     * Get the current hasNext attribute value back.
     *
     * @return true if it has more element; otherwise it has ended.
     */
    public boolean hasNext() {
        return this.processingUnitStatus.hasNext();
    }

    
    /**
     * Defines if there are more unit tor process.
     *
     * @param hasNext true if there are more units to process; otherwise false
     * @return this instance
     */
    public ProcessingUnitStatusBuilder hasNext(boolean hasNext) {
        this.processingUnitStatus.setHasNext(hasNext);
        return this;
    }

    
    /**
     * Mark there are more units in case there are still unprocessed units
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder hasNextIfHasUnprocessedUnits() {
        this.processingUnitStatus.setHasNext(processingUnitProgress.getNumberOfUnprocessedUnits() > 1);
        return this;
    }

    
    /**
     * Mark there are more units to process
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder hasMoreUnitsToProcess() {
        this.processingUnitStatus.setHasNext(true);
        return this;
    }

    
    /**
     * Mark there are no more units to process
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder hasEnded() {
        this.processingUnitStatus.setHasNext(false);
        return this;
    }

    
    /**
     * Increase the number of successful processed units. 
     *
     * @return this instance
     */
    public ProcessingUnitStatusBuilder increaseNumberOfSuccessfulUnits() {
        processingUnitStatus.increaseNumberOfSuccessfulUnits();
        return this;
    }

    
    /**
     * Increase the number of successful processed units. 
     * 
     * @param numberOfSuccessfulUnits the number of successful units to add
     * @return this instance
     */
    public ProcessingUnitStatusBuilder increaseNumberOfSuccessfulUnits(Long numberOfSuccessfulUnits) {
        processingUnitStatus.increaseNumberOfSuccessfulUnits(numberOfSuccessfulUnits);
        return this;
    }

    
    /**
     * Set the number of successful processed  units. 
     *
     * @param numberOfSuccessfulUnits the number of successful processed units to set
     * @return this instance
     */
    public ProcessingUnitStatusBuilder setNumberOfSuccessfulUnits(Long numberOfSuccessfulUnits) {
        if (numberOfSuccessfulUnits != null && numberOfSuccessfulUnits > 0) {
            processingUnitStatus.setNumberOfSuccessfulUnits(numberOfSuccessfulUnits);
        }
        
        return this;
    }

    
    /**
     * Increase the number of failed processed units. 
     * 
     * @return this instance
     */
    public ProcessingUnitStatusBuilder increaseNumberOfFailedUnits() {
        processingUnitStatus.increaseNumberOfFailedUnits();
        return this;
    }

    
    /**
     * Increase the number of failed processed units. 
     * 
     * @param numberOfFailedUnits the number of failed units to add
     * @return this instance
     */
    public ProcessingUnitStatusBuilder increaseNumberOfFailedUnits(Long numberOfFailedUnits) {
        processingUnitStatus.increaseNumberOfFailedUnits(numberOfFailedUnits);
        return this;
    }

    
    /**
     * Set the number of failed processed units. 
     *
     * @param numberOfFailedUnits the number of failed processed units to set
     * @return this instance
     */
    public ProcessingUnitStatusBuilder setNumberOfFailedUnits(Long numberOfFailedUnits) {
        if (numberOfFailedUnits != null && numberOfFailedUnits > 0) {
            processingUnitStatus.setNumberOfFailedUnits(numberOfFailedUnits);
        }
        return this;
    }

    
    /**
     * Increase the number of unprocessed units. 
     * 
     * @return this instance
     */
    public ProcessingUnitStatusBuilder increaseNumberOfUnprocessedUnits() {
        processingUnitStatus.increaseNumberOfUnprocessedUnits();
        return this;
    }

    
    /**
     * Increase the number of unprocessed units. 
     * 
     * @param numberOfUnprocessedUnits the number of unprocessed units to add
     * @return this instance
     */
    public ProcessingUnitStatusBuilder increaseNumberOfUnprocessedUnits(Long numberOfUnprocessedUnits) {
        processingUnitStatus.increaseNumberOfUnprocessedUnits(numberOfUnprocessedUnits);
        return this;
    }

    
    /**
     * Set optionally the number of unprocessed units 
     *
     * @param numberOfUnprocessedUnits the number of unprocessed units
     * @return this instance
     */
    public ProcessingUnitStatusBuilder setNumberOfUnprocessedUnits(Long numberOfUnprocessedUnits) {
        if (numberOfUnprocessedUnits != null && numberOfUnprocessedUnits >= 0) {
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
     * Add a statistic value 
     *
     * @param key the statistic key
     * @param statisticCounter the statistic counter to add
     * @return this instance
     */
    public ProcessingUnitStatusBuilder statistic(String key, StatisticCounter statisticCounter) {
        processingUnitStatus.addStatistic(key, statisticCounter);
        return this;
    }


    /**
     * Build the processing unit status
     *
     * @return the processing unit status
     */
    public IProcessingUnitStatus build() {
        processingUnitProgress.addProcessingUnitStatus(processingUnitStatus);
        return processingUnitStatus;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return processingUnitStatus.toString() + "\n" + processingUnitProgress.toString();
    }
}
