/*
 * IEmptyProcessingUnitHandler.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import java.io.Serializable;


/**
 * Defines the interface of tzhe empty processing unit handler. This will be called in case of an empty processing unit 
 * run (no failed nor successful processed units) 
 *  
 * @author patrick
 */
public interface IEmptyProcessingUnitHandler extends Serializable {
    
    /**
     * Reset empty processing unit handler. It will be called after a non-empty processing occured. 
     * 
     * @param id the id 
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param threadId the thread id
     * @param processingUnitProgress the processing unit progress
     */
    void reset(String id, String name, Class<? extends IProcessingUnit> processingUnitClass, long threadId, IProcessingUnitProgress processingUnitProgress);
    
    
    /**
     * Handle empty processing of a procesing unit
     *
     * @param id the id 
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param threadId the thread id
     * @param processingUnitProgress the processing unit progress
     * @return true to continue or false to abort processing
     */
    boolean handleEmptyProcessing(String id, String name, Class<? extends IProcessingUnit> processingUnitClass, long threadId, IProcessingUnitProgress processingUnitProgress);
    
    
    /**
     * Get the duration of this handler. This is to calculate exact duration of a processing. 
     *
     * @return the duration of this handler has taken
     */
    long getDuration();
}
