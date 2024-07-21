/*
 * IProcessingUnitObjectLockManagerSupport.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parallelization;


import com.github.toolarium.common.object.IObjectLockManager;


/**
 * Defines the processing unit object lock manager support.
 * 
 * @author patrick
 */
public interface IProcessingUnitObjectLockManagerSupport {
    
    /**
     * Sets the object lock manager
     *
     * @param objectLockManager the object lock manager
     */
    void setObjectLockManager(IObjectLockManager objectLockManager);
}
