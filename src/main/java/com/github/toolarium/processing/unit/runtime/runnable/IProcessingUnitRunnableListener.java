/*
 * IProcessingUnitRunnableListener.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
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
     * @param processingUnitProgress the processing unit progress 
     * @param runtimeTimeMeasurment the runtime time measurement
     * @param processingUnitContext the processing unit context
     */
    void notifyProcessingUnitState(String id, 
                                   String name, 
                                   String processingUnitClass, 
                                   ProcessingActionStatus processingActionStatus,
                                   IProcessingUnitProgress processingUnitProgress,
                                   IProcessingUnitRuntimeTimeMeasurement runtimeTimeMeasurment,
                                   IProcessingUnitContext processingUnitContext);
}
