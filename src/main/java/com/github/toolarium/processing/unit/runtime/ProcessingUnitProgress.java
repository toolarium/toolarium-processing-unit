/*
 * ProcessingProgressImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;


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
public class ProcessingUnitProgress implements IProcessingUnitProgress, Serializable {
    private static final long serialVersionUID = -574712395121580837L;
    private long numberOfUnitsToProcess;
    private long numberOfProcessedUnits;
    private long numberOfFailedUnits;
    private ProcessingRuntimeStatus processingRuntimeStatus;
    private ProcessingUnitStatistic processingUnitStatistic;

    
    /**
     * Constructor
     */
    public ProcessingUnitProgress() {
        numberOfUnitsToProcess = 0;
        numberOfProcessedUnits = 0;
        numberOfFailedUnits = 0;
        processingRuntimeStatus = ProcessingRuntimeStatus.SUCCESSFUL;
        processingUnitStatistic = null;
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
            
            if (processingProgress.getProcesingUnitStatistic() != null) {
                this.processingUnitStatistic = new ProcessingUnitStatistic(processingProgress.getProcesingUnitStatistic());
            }
        }
    }

    
    /**
     * Add the processing unit status
     * 
     * @param processingUnitStatus the processing unit status to add
     * @return true to continue processing
     */
    public boolean addProcessingUnitStatus(IProcessingUnitStatus processingUnitStatus) {
        boolean continueProcessing = false;

        if (processingUnitStatus != null) {
            continueProcessing = processingUnitStatus.hasNext();
            
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
            } else {
                continueProcessing = false;
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
            final ProcessingRuntimeStatus processingUnitRuntimeStatus = processingUnitStatus.getProcessingRuntimeStatus();
            if (!processingRuntimeStatus.equals(processingUnitRuntimeStatus)) {
                if (ProcessingRuntimeStatus.ERROR.equals(processingUnitRuntimeStatus)) {
                    processingRuntimeStatus = ProcessingRuntimeStatus.ERROR; 
                } else if (ProcessingRuntimeStatus.WARN.equals(processingUnitRuntimeStatus)) {
                    if (ProcessingRuntimeStatus.SUCCESSFUL.equals(processingRuntimeStatus)) {
                        processingRuntimeStatus = ProcessingRuntimeStatus.WARN; 
                    }
                }
            }
            
            /*
            ProcessingRuntimeStatus processingRuntimeStatus = processingUnitStatus.getProcessingRuntimeStatus();
            if (ProcessingRuntimeStatus.SUCCESSFUL.equals(processingRuntimeStatus)) {
                // NOP
            } else if (ProcessingRuntimeStatus.WARN.equals(processingRuntimeStatus)) {
                if (ProcessingRuntimeStatus.SUCCESSFUL.equals(this.processingRuntimeStatus)) {
                    this.processingRuntimeStatus = processingRuntimeStatus;
                }
            } else if (ProcessingRuntimeStatus.ERROR.equals(processingRuntimeStatus)) {
                if (!ProcessingRuntimeStatus.ERROR.equals(this.processingRuntimeStatus)) {
                    this.processingRuntimeStatus = processingRuntimeStatus;
                }
            } */           
            
            // update statistic
            if (processingUnitStatus != null && processingUnitStatus.getProcessingUnitStatistic() != null) {
                if (this.processingUnitStatistic == null) {
                    this.processingUnitStatistic = new ProcessingUnitStatistic();
                }
                                
                IProcessingUnitStatistic processingUnitStatistic = processingUnitStatus.getProcessingUnitStatistic();
                for (String key : processingUnitStatistic.keySet()) {
                    this.processingUnitStatistic.add(key, processingUnitStatistic.get(key));
                }
            }
            
        }

        return continueProcessing;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getNumberOfUnitsToProcess()
     */
    @Override
    public long getNumberOfUnitsToProcess() {
        return numberOfUnitsToProcess;
    }


    /**
     * Sets the number of units to process in total 
     *
     * @param numberOfUnitsToProcess the number of units to process in total
     * @return this instance
     */
    public ProcessingUnitProgress setNumberOfUnitsToProcess(long numberOfUnitsToProcess) {
        this.numberOfUnitsToProcess = numberOfUnitsToProcess;
        return this;
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
     * Increase the total units
     * @return this instance
     */
    public ProcessingUnitProgress increaseTotalUnits() {
        numberOfUnitsToProcess++;
        return this;
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
     * Increase the number of failed units
     * @return this instance
     */
    public ProcessingUnitProgress increaseNumberOfFailedUnits() {
        numberOfFailedUnits++;
        return this;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitProgress#getProcesingUnitStatistic()
     */
    @Override
    public IProcessingUnitStatistic getProcesingUnitStatistic() {
        return processingUnitStatistic;
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
