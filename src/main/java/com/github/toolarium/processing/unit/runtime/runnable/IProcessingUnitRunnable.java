/*
 * IProcessingUnitRunnable.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessStatus;


/**
 * Defines the runnable processing unit. It holds beside the processing unit class the parameters and its context.
 * 
 * @author patrick
 */
public interface IProcessingUnitRunnable extends Runnable {
    
    /**
     * Get the runnable id
     *
     * @return the runnable id
     */
    String getId();
    
    
    /**
     * Get the process status
     *
     * @return the process status
     */
    IProcessStatus getProcessStatus();

    
    /**
     * Get number of units to process 
     * 
     * @return the number of units to process
     */
    long getNumberOfUnitsToProcess();
}
