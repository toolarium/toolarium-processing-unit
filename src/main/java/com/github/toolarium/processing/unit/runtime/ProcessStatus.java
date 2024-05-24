/*
 * ProcessStatus.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;


import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingProgress;
import java.io.Serializable;
import java.util.Objects;


/**
 * Implements the {@link IProcessStatus}.
 *
 * @author patrick
 */
public class ProcessStatus implements IProcessStatus, Serializable {
    private static final long serialVersionUID = 5412972635270682831L;
    private IProcessingProgress processingProgress;
    private boolean hasNext;


    /**
     * Constructor
     *
     * @param processingProgress the processing progress
     */
    public ProcessStatus(IProcessingProgress processingProgress) {
        this.processingProgress = processingProgress;
        this.hasNext = false;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessStatus#getProcessingProgress()
     */
    @Override
    public IProcessingProgress getProcessingProgress() {
        return processingProgress;
    }


    /**
     * Sets the processingProgress
     *
     * @param processingProgress The processing progress to set
     */
    public void setProcessingProgress(IProcessingProgress processingProgress) {
        this.processingProgress = processingProgress;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessStatus#hasNext()
     */
    @Override
    public boolean hasNext() {
        return hasNext;
    }


    /**
     * Set has next
     *
     * @param hasNext true if it has more to process.
     */
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(hasNext, processingProgress);
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
        
        ProcessStatus other = (ProcessStatus) obj;
        return hasNext == other.hasNext && Objects.equals(processingProgress, other.processingProgress);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessStatusImpl [processingProgress=" + processingProgress + ", hasNext=" + hasNext + "]";
    }
}
