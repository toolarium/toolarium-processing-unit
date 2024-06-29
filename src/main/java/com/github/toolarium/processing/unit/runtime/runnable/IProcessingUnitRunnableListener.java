/*
 * IProcessingUnitRunnableListener.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;


/**
 * Defines the processing unit runnable listener
 * 
 * @author patrick
 */
public interface IProcessingUnitRunnableListener {
    
    /**
     * Notify processing unit action status
     *
     * @param id the unique id of this processing 
     * @param name the name of this processing unit runnable
     * @param processingUnitClass the processing unit class
     * @param processingActionStatus the processing action status
     * @param processingProgress the processing progress 
     * @param runtimeTimeMeasurment the runtime time measurement
     * @param processingUnitContext the processing unit context
     */
    void notifyProcessingUnitState(String id, 
                                   String name, 
                                   String processingUnitClass, 
                                   ProcessingActionStatus processingActionStatus,
                                   IProcessingProgress processingProgress,
                                   IProcessingUnitRuntimeTimeMeasurement runtimeTimeMeasurment,
                                   IProcessingUnitContext processingUnitContext);
}
