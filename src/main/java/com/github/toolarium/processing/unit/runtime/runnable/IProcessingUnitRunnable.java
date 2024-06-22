/*
 * IProcessingUnitRunnable.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import java.util.List;


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
     * Get the runnable name or null
     *
     * @return the name or null
     */
    String getName();

    
    /**
     * Get the processing action status
     * 
     * @return the processing action status
     */
    ProcessingActionStatus getProcessingActionStatus();

    
    /**
     * Get the process status
     *
     * @return the process status
     */
    IProcessStatus getProcessStatus();
    
    
    /**
     * Get the status message list
     *
     * @return the status message list
     */
    List<String> getStatusMessageList();
}
