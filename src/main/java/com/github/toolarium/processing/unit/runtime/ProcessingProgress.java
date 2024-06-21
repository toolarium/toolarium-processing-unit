/*
 * ProcessingProgressImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;


import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingStatistic;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import java.io.Serializable;
import java.util.Objects;


/**
 * Implements the {@link IProcessingProgress}.
 *
 * @author patrick
 */
public class ProcessingProgress implements IProcessingProgress, Serializable {
    private static final long serialVersionUID = -574712395121580837L;
    private long numberOfUnitsToProcess;
    private long numberOfProcessedUnits;
    private long numberOfFailedUnits;
    private ProcessingRuntimeStatus processingRuntimeStatus;
    private String processingStatusMessage;
    private ProcessingStatistic processingStatistic;


    /**
     * Constructor
     */
    public ProcessingProgress() {
        numberOfUnitsToProcess = -1;
        numberOfProcessedUnits = 0;
        numberOfFailedUnits = 0;
        processingRuntimeStatus = ProcessingRuntimeStatus.SUCCESSFUL;
        processingStatistic = new ProcessingStatistic();
    }


    /**
     * Initialize a previous state
     *
     * @param processingProgress the processing progress to initialize
     */
    public void init(final IProcessingProgress processingProgress) {
        if (processingProgress == null) {
            return;
        }

        this.numberOfUnitsToProcess = processingProgress.getNumberOfUnitsToProcess();
        this.numberOfProcessedUnits = processingProgress.getNumberOfProcessedUnits();
        this.numberOfFailedUnits = processingProgress.getNumberOfFailedUnits();
        this.processingRuntimeStatus = processingProgress.getProcessingRuntimeStatus();
        this.processingStatistic = new ProcessingStatistic(processingProgress.getProcesingStatistic());
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getNumberOfUnitsToProcess()
     */
    @Override
    public long getNumberOfUnitsToProcess() {
        return numberOfUnitsToProcess;
    }


    /**
     * Sets the number of units to process in total 
     *
     * @param numberOfUnitsToProcess the number of units to process in total
     */
    public void setNumberOfUnitsToProcess(long numberOfUnitsToProcess) {
        this.numberOfUnitsToProcess = numberOfUnitsToProcess;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getNumberOfUnprocessedUnits()
     */
    @Override
    public long getNumberOfUnprocessedUnits() {
        if (numberOfUnitsToProcess > 0 && numberOfUnitsToProcess > numberOfProcessedUnits) {
            return numberOfUnitsToProcess - numberOfProcessedUnits;
        }
        
        return 0;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getNumberOfProcessedUnits()
     */
    @Override
    public long getNumberOfProcessedUnits() {
        return numberOfProcessedUnits;
    }
    

    /**
     * Sets the number of processed units
     *
     * @param processedUnits the number of processed units
     */
    public void setNumberOfProcessedUnits(long processedUnits) {
        this.numberOfProcessedUnits = processedUnits;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getNumberOfSuccessfulUnits()
     */
    @Override
    public long getNumberOfSuccessfulUnits() {
        if (numberOfProcessedUnits > 0 && numberOfProcessedUnits > numberOfFailedUnits) {
            return numberOfProcessedUnits - numberOfFailedUnits;
        }
        
        return 0;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getNumberOfFailedUnits()
     */
    @Override
    public long getNumberOfFailedUnits() {
        return numberOfFailedUnits;
    }


    /**
     * Sets the number of failed units
     *
     * @param numberOfFailedUnits the number of failed units
     */
    public void setNumberOfFailedUnits(long numberOfFailedUnits) {
        this.numberOfFailedUnits = numberOfFailedUnits;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getProcesingStatistic()
     */
    @Override
    public IProcessingStatistic getProcesingStatistic() {
        return processingStatistic;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getProcessingRuntimeStatus()
     */
    @Override
    public ProcessingRuntimeStatus getProcessingRuntimeStatus() {
        return processingRuntimeStatus;
    }


    /**
     * Sets the processing status type
     *
     * @param processingRuntimeStatus the processing runtime status
     */
    public void setProcessingRuntimeStatus(final ProcessingRuntimeStatus processingRuntimeStatus) {
        this.processingRuntimeStatus = processingRuntimeStatus;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getProcessingStatusMessage()
     */
    @Override
    public String getProcessingStatusMessage() {
        return processingStatusMessage;
    }


    /**
     * Sets the processing status message
     *
     * @param processingStatusMessage the processing status message
     */
    public void setStatusMessage(String processingStatusMessage) {
        this.processingStatusMessage = processingStatusMessage;
    }


    /**
     * Increase the total units
     */
    public void increaseTotalUnits() {
        numberOfUnitsToProcess++;
    }


    /**
     * Increase the number of processed units
     */
    public void increaseNumberOfProcessedUnits() {
        numberOfProcessedUnits++;
    }


    /**
     * Increase the number of failed units
     */
    public void increaseNumberOfFailedUnits() {
        numberOfFailedUnits++;
    }


    /**
     * Adds a statistic value
     *
     * @param key the key
     * @param value the value
     * @throws  IllegalArgumentException In case of invalid key 
     */
    public void addStatistic(String key, Double value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Invalid statistic key!");
        }

        if (value == null) {
            return;
        }

        Double v = processingStatistic.get(key);
        if (v == null) {
            v = value;
        } else {
            v = Double.sum(v.doubleValue(), value.doubleValue());
        }

        processingStatistic.put(key, v);
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(numberOfProcessedUnits, processingStatistic, processingStatusMessage, processingRuntimeStatus, numberOfFailedUnits, numberOfUnitsToProcess);
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
        
        ProcessingProgress other = (ProcessingProgress) obj;
        return numberOfProcessedUnits == other.numberOfProcessedUnits
                && Objects.equals(processingStatistic, other.processingStatistic)
                && Objects.equals(processingStatusMessage, other.processingStatusMessage)
                && processingRuntimeStatus == other.processingRuntimeStatus 
                && numberOfFailedUnits == other.numberOfFailedUnits
                && numberOfUnitsToProcess == other.numberOfUnitsToProcess;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingProgressImpl [numberOfUnitsToProcess=" + numberOfUnitsToProcess + ", numberOfProcessedUnits=" + numberOfProcessedUnits
                + ", numberOfFailedUnits=" + numberOfFailedUnits + ", processingRuntimeStatus=" + processingRuntimeStatus
                + ", processingStatusMessage=" + processingStatusMessage + ", processingStatistic="
                + processingStatistic + "]";
    }
}
