/*
 * IProcessingUnitInstanceManager.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;


import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.exception.ValidationException;


/**
 * Defines the interface to create new {@link IProcessingUnit} instances.
 * 
 * @author patrick
 */
public interface IProcessingUnitInstanceManager {
    
    /**
     * Create the processing unit implementation
     *
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnitClass the class
     * @return the instance
     * @throws ValidationException If the instance of the processing unit cannot be initialized correctly 
     */
    IProcessingUnit createProcessingUnitInstance(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException;


    /**
     * Create the parallel processing unit implementation
     *
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnitClass the class
     * @return the instance
     * @throws ValidationException If the instance of the processing unit cannot be initialized correctly 
     */
    IProcessingUnit createParallelProcessingUnitInstance(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException;


    /**
     * Release resource 
     *
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnit the processing unit
     */
    void releaseResource(String id, String name, IProcessingUnit processingUnit);
}
