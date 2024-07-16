/*
 * ProcessingUnitStatus.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;


import com.github.toolarium.common.statistic.StatisticCounter;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import java.io.Serializable;
import java.util.Objects;


/**
 * Implements the {@link IProcessingUnitStatus}.
 *
 * @author patrick
 */
public class ProcessingUnitStatus implements IProcessingUnitStatus, Serializable {
    private static final long serialVersionUID = 5412972635270682831L;
    private boolean hasNext;
    private Long numberOfSuccessfulUnits;
    private Long numberOfFailedUnits;
    private Long numberOfUnprocessedUnits;
    private ProcessingRuntimeStatus processingRuntimeStatus;
    private String statusMessage;
    private ProcessingUnitStatistic processingUnitStatistic;


    /**
     * Constructor
     */
    public ProcessingUnitStatus() {
        hasNext = false;
        numberOfSuccessfulUnits = null;
        numberOfFailedUnits = null;
        numberOfUnprocessedUnits = null;
        processingRuntimeStatus = ProcessingRuntimeStatus.SUCCESSFUL;
        statusMessage = null;
        processingUnitStatistic = null;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatus#hasNext()
     */
    @Override
    public boolean hasNext() {
        return hasNext;
    }

    
    /**
     * Mark there are more units to process
     *
     * @return this instance
     */
    public ProcessingUnitStatus hasMoreUnitsToProcess() {
        return setHasNext(true);
    }

    
    /**
     * Set has next
     *
     * @param hasNext true if it has more to process.
     * @return this instance
     */
    public ProcessingUnitStatus setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatus#getNumberOfSuccessfulUnits()
     */
    @Override
    public Long getNumberOfSuccessfulUnits() {
        return numberOfSuccessfulUnits;
    }
    
    
    /**
     * Set the number of successful units
     *
     * @param numberOfSuccessfulUnits the number of successful units
     * @return this instance
     */
    public ProcessingUnitStatus setNumberOfSuccessfulUnits(Long numberOfSuccessfulUnits) {
        this.numberOfSuccessfulUnits = numberOfSuccessfulUnits;
        return this;
    }

    
    /**
     * Increase the number of successful units
     * 
     * @return this instance
     */
    public ProcessingUnitStatus increaseNumberOfSuccessfulUnits() {
        if (numberOfSuccessfulUnits == null) {
            numberOfSuccessfulUnits = 0L;
        } 
        
        numberOfSuccessfulUnits++;
        return this;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatus#getNumberOfFailedUnits()
     */
    @Override
    public Long getNumberOfFailedUnits() {
        return numberOfFailedUnits;
    }

    
    /**
     * Set the number of failed units
     *
     * @param numberOfFailedUnits the number of failed units
     * @return this instance
     */
    public ProcessingUnitStatus setNumberOfFailedUnits(Long numberOfFailedUnits) {
        this.numberOfFailedUnits = numberOfFailedUnits;
        return this;
    }

    
    /**
     * Increase the number of failed units
     * 
     * @return this instance
     */
    public ProcessingUnitStatus increaseNumberOfFailedUnits() {
        if (numberOfFailedUnits == null) {
            numberOfFailedUnits = 0L;
        }
        
        numberOfFailedUnits++;
        return this;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatus#getNumberOfFailedUnits()
     */
    @Override
    public Long getNumberOfUnprocessedUnits() {
        return numberOfUnprocessedUnits;
    }

    
    /**
     * Set the number of unprocessed units
     *
     * @param numberOfUnprocessedUnits the number of unprocessed units
     * @return this instance
     */
    public ProcessingUnitStatus setNumberOfUnprocessedUnits(Long numberOfUnprocessedUnits) {
        this.numberOfUnprocessedUnits = numberOfUnprocessedUnits;
        return this;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatus#getProcessingRuntimeStatus()
     */
    @Override
    public ProcessingRuntimeStatus getProcessingRuntimeStatus() {
        return processingRuntimeStatus;
    }

    
    /**
     * Set the processing runtime statis
     *
     * @param processingRuntimeStatus the processing runtime statis
     * @return this instance
     */
    public ProcessingUnitStatus setProcessingRuntimeStatus(ProcessingRuntimeStatus processingRuntimeStatus) {
        this.processingRuntimeStatus = processingRuntimeStatus;
        return this;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatus#getStatusMessage()
     */
    @Override
    public String getStatusMessage() {
        return statusMessage;
    }
    
    
    /**
     * Sets the status message
     *
     * @param statusMessage the status message
     * @return this instance
     */
    public ProcessingUnitStatus setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatus#getProcessingUnitStatistic()
     */
    @Override
    public ProcessingUnitStatistic getProcessingUnitStatistic() {
        return processingUnitStatistic;
    }
    
    
    /**
     * Add statistic
     *
     * @param key the key / name of the statistic
     * @param value the value to add
     */
    public void addStatistic(String key, Long value) {
        if (key == null || key.isBlank() || value == null) {
            return;
        }
        
        getStatisticCounter(key).add(value);
    }

    
    /**
     * Add statistic
     *
     * @param key the key / name of the statistic
     * @param value the value to add
     */
    public void addStatistic(String key, Double value) {
        if (key == null || key.isBlank() || value == null) {
            return;
        }
        
        getStatisticCounter(key).add(value);
    }

    
    /**
     * Add statistic
     *
     * @param key the key / name of the statistic
     * @param statisticCounter statistic counter
     */
    public void addStatistic(String key, StatisticCounter statisticCounter) {
        if (key == null || key.isBlank() || statisticCounter == null) {
            return;
        }
        
        getStatisticCounter(key).add(statisticCounter);
    }

    
    /**
     * Get statistic counter
     *
     * @param key the key / name of the statistic
     * @return the statistic counter
     */
    public StatisticCounter getStatisticCounter(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        
        if (processingUnitStatistic == null) {
            processingUnitStatistic = new ProcessingUnitStatistic();
        }
        
        return processingUnitStatistic.getOrAdd(key);
    }

    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(hasNext, numberOfFailedUnits, numberOfSuccessfulUnits, numberOfUnprocessedUnits,
                processingRuntimeStatus, processingUnitStatistic, statusMessage);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        ProcessingUnitStatus other = (ProcessingUnitStatus) obj;
        return hasNext == other.hasNext && Objects.equals(numberOfFailedUnits, other.numberOfFailedUnits)
                && Objects.equals(numberOfSuccessfulUnits, other.numberOfSuccessfulUnits)
                && Objects.equals(numberOfUnprocessedUnits, other.numberOfUnprocessedUnits)
                && processingRuntimeStatus == other.processingRuntimeStatus
                && Objects.equals(processingUnitStatistic, other.processingUnitStatistic)
                && Objects.equals(statusMessage, other.statusMessage);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingUnitStatus [hasNext=" + hasNext + ", numberOfSuccessfulUnits=" + numberOfSuccessfulUnits
                + ", numberOfFailedUnits=" + numberOfFailedUnits + ", numberOfUnprocessedUnits="
                + numberOfUnprocessedUnits + ", processingRuntimeStatus=" + processingRuntimeStatus + ", statusMessage="
                + statusMessage + ", processingUnitStatistic=" + processingUnitStatistic + "]";
    }
}
