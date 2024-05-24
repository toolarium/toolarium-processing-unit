/*
 * ProcessingProgressImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;


import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingStatistic;
import com.github.toolarium.processing.unit.dto.ProcessingStatusType;
import java.io.Serializable;
import java.util.Objects;


/**
 * Implements the {@link IProcessingProgress}.
 *
 * @author patrick
 */
public class ProcessingProgress implements IProcessingProgress, Serializable {
    private static final long serialVersionUID = -574712395121580837L;
    private long totalUnits;
    private long processedUnits;
    private long totalFailedUnits;
    private ProcessingStatusType processingStatusType;
    private String processingStatusMessage;
    private ProcessingStatistic processingStatistic;


    /**
     * Constructor
     */
    public ProcessingProgress() {
        totalUnits = 0;
        processedUnits = 0;
        totalFailedUnits = 0;
        processingStatusType = ProcessingStatusType.SUCCESSFUL;
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

        this.totalUnits = processingProgress.getTotalUnits();
        this.processedUnits = processingProgress.getProcessedUnits();
        this.totalFailedUnits = processingProgress.getTotalFailedUnits();
        this.processingStatusType = processingProgress.getProcessingStatusType();
        this.processingStatistic = new ProcessingStatistic(processingProgress.getProcesingStatistic());
    }


    /**
     * 
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getTotalUnits()
     */
    @Override
    public long getTotalUnits() {
        return totalUnits;
    }


    /**
     * Sets the total units
     *
     * @param totalUnits the total units
     */
    public void setTotalUnits(long totalUnits) {
        this.totalUnits = totalUnits;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getProcessedUnits()
     */
    @Override
    public long getProcessedUnits() {
        return processedUnits;
    }


    /**
     * Sets the total processed units
     *
     * @param processedUnits the total processed units
     */
    public void setProcessedUnits(long processedUnits) {
        this.processedUnits = processedUnits;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getTotalFailedUnits()
     */
    @Override
    public long getTotalFailedUnits() {
        return totalFailedUnits;
    }


    /**
     * Sets the total failed units
     *
     * @param totalFailedUnits the total failed units
     */
    public void setTotalFailedUnits(long totalFailedUnits) {
        this.totalFailedUnits = totalFailedUnits;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getProcesingStatistic()
     */
    @Override
    public IProcessingStatistic getProcesingStatistic() {
        return processingStatistic;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingProgress#getProcessingStatusType()
     */
    @Override
    public ProcessingStatusType getProcessingStatusType() {
        return processingStatusType;
    }


    /**
     * Sets the processing status type
     *
     * @param processingStatusType the processing status type
     */
    public void setProcessingStatusType(final ProcessingStatusType processingStatusType) {
        this.processingStatusType = processingStatusType;
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
        totalUnits++;
    }


    /**
     * Increase the total processed units
     */
    public void increaseTotalProcessedUnits() {
        processedUnits++;
    }


    /**
     * Increase the total failed units
     */
    public void increaseTotalFailedUnits() {
        totalFailedUnits++;
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
        return Objects.hash(processedUnits, processingStatistic, processingStatusMessage, processingStatusType, totalFailedUnits, totalUnits);
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
        return processedUnits == other.processedUnits
                && Objects.equals(processingStatistic, other.processingStatistic)
                && Objects.equals(processingStatusMessage, other.processingStatusMessage)
                && processingStatusType == other.processingStatusType && totalFailedUnits == other.totalFailedUnits
                && totalUnits == other.totalUnits;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingProgressImpl [totalUnits=" + totalUnits + ", processedUnits=" + processedUnits
                + ", totalFailedUnits=" + totalFailedUnits + ", processingStatusType=" + processingStatusType
                + ", processingStatusMessage=" + processingStatusMessage + ", processingStatistic="
                + processingStatistic + "]";
    }
}
