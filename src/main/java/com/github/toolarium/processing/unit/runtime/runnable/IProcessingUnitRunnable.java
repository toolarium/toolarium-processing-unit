/*
 * IProcessingUnitRunnable.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import java.util.List;


/**
 * Defines the runnable processing unit. It holds beside the processing unit class the parameters and its context.
 * 
 * @author patrick
 */
public interface IProcessingUnitRunnable {
    
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
     * Get the processing parameter list.
     *
     * @return the processing parameter list
     */
    List<Parameter> getParameterList();

    
    /**
     * Get the processing action status
     * 
     * @return the processing action status
     */
    ProcessingActionStatus getProcessingActionStatus();

    
    /**
     * Get the processing unit progress
     *
     * @return the process unit progress
     */
    IProcessingUnitProgress getProcessingUnitProgress();

    
    /**
     * Get the processing runtime status
     * 
     * @return the processing runtime status
     */
    ProcessingRuntimeStatus getProcessingRuntimeStatus();

    
    /**
     * Get the status message list
     *
     * @return the status message list
     */
    List<String> getStatusMessageList();

    
    /**
     * Get the time measurement
     *
     * @return the time measurement
     */
    IProcessingUnitRuntimeTimeMeasurement getTimeMeasurement();


    /**
     * Release resources will be called to release all internal referenced resources after a processing success, warn or error.
     *
     * @throws ProcessingException Throws this exception in case of releasing failure.
     */
    void releaseResource() 
        throws ProcessingException;
}
