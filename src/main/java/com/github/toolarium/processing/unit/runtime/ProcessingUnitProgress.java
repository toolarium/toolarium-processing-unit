/*
 * ProcessingProgressImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;


import com.github.toolarium.common.util.RoundUtil;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatistic;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import java.io.Serializable;
import java.util.Objects;


/**
 * Implements the {@link IProcessingUnitProgress}.
 *
 * @author patrick
 */
public class ProcessingUnitProgress implements IProcessingUnitUpdateProgress, Serializable {
    private static final long serialVersionUID = -574712395121580837L;
    private volatile long numberOfUnitsToProcess;
    private volatile long numberOfProcessedUnits;
    private volatile long numberOfFailedUnits;
    private volatile ProcessingRuntimeStatus processingRuntimeStatus;
    private volatile ProcessingUnitStatistic processingUnitStatistic;

    
    /**
     * Constructor
     */
    public ProcessingUnitProgress() {
        numberOfUnitsToProcess = 0;
        numberOfProcessedUnits = 0;
        numberOfFailedUnits = 0;
        processingRuntimeStatus = ProcessingRuntimeStatus.SUCCESSFUL;
        processingUnitStatistic = new ProcessingUnitStatistic();
    }

        
    /**
     * Constructor
     * 
     * @param processingProgress the processing progress to initialize
     */
    public ProcessingUnitProgress(final IProcessingUnitProgress processingProgress) {
        this();
        
        if (processingProgress != null) {
            this.numberOfUnitsToProcess = processingProgress.getNumberOfUnitsToProcess();
            this.numberOfProcessedUnits = processingProgress.getNumberOfProcessedUnits();
            this.numberOfFailedUnits = processingProgress.getNumberOfFailedUnits();
            this.processingRuntimeStatus = processingProgress.getProcessingRuntimeStatus();
            
            if (processingProgress.getProcessingUnitStatistic() != null) {
                this.processingUnitStatistic = new ProcessingUnitStatistic(processingProgress.getProcessingUnitStatistic());
            }
        }
    }


    /**
     * Add the processing unit status
     * 
     * @param processingUnitStatus the processing unit status to add
     * @return the number of processed units
     */
    public long addProcessingUnitStatus(IProcessingUnitStatus processingUnitStatus) {
        if (processingUnitStatus == null) {
            return 0;
        }

        long processedUnits = 0;
        final Long successfulUnits = processingUnitStatus.getNumberOfSuccessfulUnits();
        if (successfulUnits != null && successfulUnits.longValue() > 0) {
            processedUnits = successfulUnits.longValue();
        }
        
        final Long failedUnits = processingUnitStatus.getNumberOfFailedUnits();
        if (failedUnits != null && failedUnits.longValue() > 0) {
            numberOfFailedUnits += failedUnits.longValue();
            processedUnits = failedUnits.longValue();
        }

        if (processedUnits > 0) {
            numberOfProcessedUnits += processedUnits;
        }

        if (numberOfProcessedUnits > numberOfUnitsToProcess) {
            numberOfUnitsToProcess = numberOfProcessedUnits;    
        }

        final Long numberOfUnprocessedUnits = processingUnitStatus.getNumberOfUnprocessedUnits();
        if (numberOfUnprocessedUnits != null && numberOfUnprocessedUnits.longValue() > 0) {
            long currentNumberOfUnprocessedUnits = getNumberOfUnprocessedUnits();
            if (currentNumberOfUnprocessedUnits > numberOfUnprocessedUnits.longValue()) { // 10 > 5
                numberOfUnitsToProcess = numberOfUnitsToProcess - (currentNumberOfUnprocessedUnits - numberOfUnprocessedUnits.longValue());
            } else if (currentNumberOfUnprocessedUnits < numberOfUnprocessedUnits.longValue()) { // 5 / 10
                numberOfUnitsToProcess = numberOfUnitsToProcess + (numberOfUnprocessedUnits.longValue() - currentNumberOfUnprocessedUnits);
            }
        }
        
        // update runtime status
        updateProcessingRuntimeStatus(processingUnitStatus.getProcessingRuntimeStatus());
        
        // update statistic
        addProcessingUnitStatistic(processingUnitStatus.getProcessingUnitStatistic());

        return processedUnits;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfUnitsToProcess()
     */
    @Override
    public long getNumberOfUnitsToProcess() {
        return numberOfUnitsToProcess;
    }

    
    /**
     * Increase the number of units to process in total 
     *
     * @param numberOfUnitsToProcess the number of units to process in total
     * @return this instance
     */
    public ProcessingUnitProgress increaseNumberOfUnitsToProcess(long numberOfUnitsToProcess) {
        this.numberOfUnitsToProcess += numberOfUnitsToProcess;
        return this;
    }

    
    /**
     * Increase the number of units to process in total 
     *
     * @param numberOfUnitsToProcess the number of units to process in total
     * @return this instance
     */
    public ProcessingUnitProgress decreaseNumberOfUnitsToProcess(long numberOfUnitsToProcess) {
        this.numberOfUnitsToProcess -= numberOfUnitsToProcess;
        return this;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitUpdateProgress#setNumberOfUnitsToProcess(long)
     */
    @Override
    public long setNumberOfUnitsToProcess(long numberOfUnitsToProcess) {
        this.numberOfUnitsToProcess = numberOfUnitsToProcess;
        return this.numberOfUnitsToProcess;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfUnprocessedUnits()
     */
    @Override
    public long getNumberOfUnprocessedUnits() {
        if (numberOfUnitsToProcess > 0 && numberOfUnitsToProcess > numberOfProcessedUnits) {
            return numberOfUnitsToProcess - numberOfProcessedUnits;
        }
        
        return 0;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfProcessedUnits()
     */
    @Override
    public long getNumberOfProcessedUnits() {
        return numberOfProcessedUnits;
    }

    
    /**
     * Increase the number of processed units
     * @return this instance
     */
    public ProcessingUnitProgress increaseNumberOfProcessedUnits() {        
        numberOfProcessedUnits++;
        return this;
    }

    
    /**
     * Increase the number of processed units
     *
     * @param processedUnits the number of processed units
     * @return this instance
     */
    public ProcessingUnitProgress increaseNumberOfProcessedUnits(long processedUnits) {
        this.numberOfProcessedUnits += processedUnits;
        return this;
    }


    /**
     * Sets the number of processed units
     *
     * @param processedUnits the number of processed units
     * @return this instance
     */
    public ProcessingUnitProgress setNumberOfProcessedUnits(long processedUnits) {
        this.numberOfProcessedUnits = processedUnits;
        return this;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfSuccessfulUnits()
     */
    @Override
    public long getNumberOfSuccessfulUnits() {
        if (numberOfProcessedUnits > 0 && numberOfProcessedUnits > numberOfFailedUnits) {
            return numberOfProcessedUnits - numberOfFailedUnits;
        }
        
        return 0;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfFailedUnits()
     */
    @Override
    public long getNumberOfFailedUnits() {
        return numberOfFailedUnits;
    }

    
    /**
     * Increase the number of failed units
     * 
     * @return this instance
     */
    public ProcessingUnitProgress increaseNumberOfFailedUnits() {
        numberOfFailedUnits++;
        return this;
    }

    
    /**
     * Increase the number of failed units
     *
     * @param numberOfFailedUnits the number of failed units
     * @return this instance
     */
    public ProcessingUnitProgress increaseNumberOfFailedUnits(long numberOfFailedUnits) {
        this.numberOfFailedUnits += numberOfFailedUnits;
        return this;
    }


    /**
     * Sets the number of failed units
     *
     * @param numberOfFailedUnits the number of failed units
     * @return this instance
     */
    public ProcessingUnitProgress setNumberOfFailedUnits(long numberOfFailedUnits) {
        this.numberOfFailedUnits = numberOfFailedUnits;
        return this;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getProcessingRuntimeStatus()
     */
    @Override
    public ProcessingRuntimeStatus getProcessingRuntimeStatus() {
        return processingRuntimeStatus;
    }


    /**
     * Sets the processing status type
     *
     * @param processingRuntimeStatus the processing runtime status
     * @return this instance
     */
    public ProcessingUnitProgress setProcessingRuntimeStatus(final ProcessingRuntimeStatus processingRuntimeStatus) {
        this.processingRuntimeStatus = processingRuntimeStatus;
        return this;
    }

    
    /**
     * Update processing runtime status
     *
     * @param processingRuntimeStatus the processing runtime status
     * @return the instance
     */
    public ProcessingUnitProgress updateProcessingRuntimeStatus(final ProcessingRuntimeStatus processingRuntimeStatus) {
        if (this.processingRuntimeStatus != null && this.processingRuntimeStatus.equals(processingRuntimeStatus)) {
            return this;
        }
        
        synchronized (this) {
            if (this.processingRuntimeStatus == null) {
                this.processingRuntimeStatus = processingRuntimeStatus; 
            } else if (!this.processingRuntimeStatus.equals(processingRuntimeStatus)) {
                if (ProcessingRuntimeStatus.ERROR.equals(processingRuntimeStatus)) {
                    this.processingRuntimeStatus = ProcessingRuntimeStatus.ERROR; 
                } else if (ProcessingRuntimeStatus.WARN.equals(processingRuntimeStatus)) {
                    if (ProcessingRuntimeStatus.SUCCESSFUL.equals(this.processingRuntimeStatus)) {
                        this.processingRuntimeStatus = ProcessingRuntimeStatus.WARN; 
                    }
                }
            }
        }
        
        return this;
    }

    
    /**
     * Increase the total units
     * 
     * @return this instance
     */
    public ProcessingUnitProgress increaseTotalUnits() {
        numberOfUnitsToProcess++;
        return this;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getProcessingUnitStatistic()
     */
    @Override
    public IProcessingUnitStatistic getProcessingUnitStatistic() {
        return processingUnitStatistic;
    }

    
    /**
     * Add a processing unit statistic
     *
     * @param processingUnitStatistic the processing unit statistic to add
     */
    public void addProcessingUnitStatistic(final IProcessingUnitStatistic processingUnitStatistic) {
        if (processingUnitStatistic != null && !processingUnitStatistic.isEmpty()) {
            for (String key : processingUnitStatistic.keySet()) {
                this.processingUnitStatistic.add(key, processingUnitStatistic.get(key));
            }
        }
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getProgress()
     */
    @Override
    public int getProgress() {
        if (getNumberOfUnitsToProcess() == 0) {
            return 0;
        }
        
        return RoundUtil.getInstance().roundToInt(100.0 / getNumberOfUnitsToProcess() * getNumberOfProcessedUnits());
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(processingUnitStatistic, numberOfFailedUnits, numberOfProcessedUnits, numberOfUnitsToProcess, processingRuntimeStatus);
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
        
        ProcessingUnitProgress other = (ProcessingUnitProgress) obj;
        return numberOfFailedUnits == other.numberOfFailedUnits
                && numberOfProcessedUnits == other.numberOfProcessedUnits
                && numberOfUnitsToProcess == other.numberOfUnitsToProcess
                && processingRuntimeStatus == other.processingRuntimeStatus
                && processingUnitStatistic == other.processingUnitStatistic;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingUnitProgress [numberOfUnitsToProcess=" + numberOfUnitsToProcess + ", numberOfProcessedUnits="
                + numberOfProcessedUnits + ", numberOfFailedUnits=" + numberOfFailedUnits + ", processingRuntimeStatus="
                + processingRuntimeStatus + ", processingUnitStatistic=" + processingUnitStatistic + "]";
    }
}
