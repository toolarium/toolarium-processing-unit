/*
 * IProcessingUnitUpdateProgress.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;

import com.github.toolarium.processing.unit.IProcessingUnitProgress;

/**
 * Defines the processing unit update progress.
 * 
 * @author patrick
 */
public interface IProcessingUnitUpdateProgress extends IProcessingUnitProgress {
    
    /**
     * Sets the number of units to process in total 
     *
     * @param numberOfUnitsToProcess the number of units to process in total
     * @return the number of set units to process
     */
    long setNumberOfUnitsToProcess(long numberOfUnitsToProcess);

}
