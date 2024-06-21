/*
 * IProcessingUnitRunnableListener.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;


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
     * @param processingUnitContext the processing unit context
     * @param processingProgress the processing progress 
     */
    void notifyProcessingUnitState(String id, 
                                   String name, 
                                   Class<? extends IProcessingUnit> processingUnitClass, 
                                   ProcessingActionStatus processingActionStatus, 
                                   IProcessingUnitContext processingUnitContext, 
                                   IProcessingProgress processingProgress);
}
