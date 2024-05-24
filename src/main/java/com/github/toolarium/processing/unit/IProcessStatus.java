/*
 * IProcessStatus.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

/**
 * Defines the process status.
 *
 * @author patrick
 */
public interface IProcessStatus {
    
    /**
     * Gets the processing progress.
     *
     * @return the processing progress.
     */
    IProcessingProgress getProcessingProgress();


    /**
     * Defines if there is more to process.
     *
     * @return true if it has more element; otherwise it has ended.
     */
    boolean hasNext();
}
