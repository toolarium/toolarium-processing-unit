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
import java.util.ArrayList;
import java.util.List;
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
    private List<String> statusMessageList;
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
        statusMessageList = null;
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
     * Set the number of successful processed units
     *
     * @param numberOfSuccessfulUnits the number of successful processed units
     * @return this instance
     */
    public ProcessingUnitStatus setNumberOfSuccessfulUnits(Long numberOfSuccessfulUnits) {
        this.numberOfSuccessfulUnits = numberOfSuccessfulUnits;
        return this;
    }

    
    /**
     * Increase the number of successful processed units
     * 
     * @return this instance
     */
    public ProcessingUnitStatus increaseNumberOfSuccessfulUnits() {
        return increaseNumberOfSuccessfulUnits(1L);
    }

    
    /**
     * Increase the number of successful processed units
     * 
     * @param numberOfSuccessfulUnits the number of successful units to add
     * @return this instance
     */
    public ProcessingUnitStatus increaseNumberOfSuccessfulUnits(Long numberOfSuccessfulUnits) {
        if (numberOfSuccessfulUnits == null) {
            return this;
        } 
        
        if (this.numberOfSuccessfulUnits == null) {
            this.numberOfSuccessfulUnits = numberOfSuccessfulUnits;
        } else {
            this.numberOfSuccessfulUnits += numberOfSuccessfulUnits;
        }
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
        return increaseNumberOfFailedUnits(1L);
    }

    
    /**
     * Increase the number of failed units
     * 
     * @param numberOfFailedUnits the number of failed units to add
     * @return this instance
     */
    public ProcessingUnitStatus increaseNumberOfFailedUnits(Long numberOfFailedUnits) {
        if (numberOfFailedUnits == null) {
            return this;
        } 
        
        if (this.numberOfFailedUnits == null) {
            this.numberOfFailedUnits = numberOfFailedUnits;
        } else {
            this.numberOfFailedUnits += numberOfFailedUnits;
        }
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
     * Increase the number of unprocessed units
     * 
     * @return this instance
     */
    public ProcessingUnitStatus increaseNumberOfUnprocessedUnits() {
        return increaseNumberOfUnprocessedUnits(1L);
    }

    
    /**
     * Increase the number of unprocessed units
     * 
     * @param numberOfUnprocessedUnits the number of unprocessed units to add
     * @return this instance
     */
    public ProcessingUnitStatus increaseNumberOfUnprocessedUnits(Long numberOfUnprocessedUnits) {
        if (numberOfUnprocessedUnits == null) {
            return this;
        } 
        
        if (this.numberOfUnprocessedUnits == null) {
            this.numberOfUnprocessedUnits = numberOfUnprocessedUnits;
        } else {
            this.numberOfUnprocessedUnits += numberOfUnprocessedUnits;
        }
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
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatus#getStatusMessageList()
     */
    @Override
    public List<String> getStatusMessageList() {
        return statusMessageList;
    }
    
    
    /**
     * Sets the status message list
     *
     * @param statusMessageList the status message list
     * @return this instance
     */
    public ProcessingUnitStatus setStatusMessageList(List<String> statusMessageList) {
        this.statusMessageList = statusMessageList;
        return this;
    }

    
    /**
     * Sets the status message list
     *
     * @param statusMessage the status message
     * @return this instance
     */
    public ProcessingUnitStatus addStatusMessage(String statusMessage) {
        if (statusMessage != null && !statusMessage.isBlank()) {
            if (statusMessageList == null) {
                statusMessageList = new ArrayList<String>();
            }
            this.statusMessageList.add(statusMessage.trim());
        }
        
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
     * @return the statistic counter
     */
    public StatisticCounter addStatistic(String key, Long value) {
        if (key == null || key.isBlank() || value == null) {
            return null;
        }
        
        StatisticCounter statisticCounter = getStatisticCounter(key);
        statisticCounter.add(value);
        return statisticCounter;
    }

    
    /**
     * Add statistic
     *
     * @param key the key / name of the statistic
     * @param value the value to add
     * @return the statistic counter
     */
    public StatisticCounter addStatistic(String key, Double value) {
        if (key == null || key.isBlank() || value == null) {
            return null;
        }
        
        StatisticCounter statisticCounter = getStatisticCounter(key);
        statisticCounter.add(value);
        return statisticCounter;
    }

    
    /**
     * Add statistic
     *
     * @param key the key / name of the statistic
     * @param inputStatisticCounter statistic counter
     * @return the statistic counter
     */
    public StatisticCounter addStatistic(String key, StatisticCounter inputStatisticCounter) {
        if (key == null || key.isBlank() || inputStatisticCounter == null) {
            return null;
        }

        StatisticCounter statisticCounter = getStatisticCounter(key);
        statisticCounter.add(inputStatisticCounter);
        return statisticCounter;
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
                processingRuntimeStatus, processingUnitStatistic, statusMessageList);
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
                && Objects.equals(statusMessageList, other.statusMessageList);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingUnitStatus [hasNext=" + hasNext + ", numberOfSuccessfulUnits=" + numberOfSuccessfulUnits
                + ", numberOfFailedUnits=" + numberOfFailedUnits + ", numberOfUnprocessedUnits="
                + numberOfUnprocessedUnits + ", processingRuntimeStatus=" + processingRuntimeStatus + ", statusMessageList="
                + statusMessageList + ", processingUnitStatistic=" + processingUnitStatistic + "]";
    }
}
