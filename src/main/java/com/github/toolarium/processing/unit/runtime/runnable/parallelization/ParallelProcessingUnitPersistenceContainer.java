/*
 * ParallelProcessingUnitPersistenceContainer.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable.parallelization;

import com.github.toolarium.common.object.IObjectLockManager;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import java.util.ArrayList;
import java.util.List;


/**
 * Defines the parallel processing unit persistence container
 * 
 * @author patrick
 */
public class ParallelProcessingUnitPersistenceContainer implements IProcessingUnitPersistence {
    private static final long serialVersionUID = 9136017612972853636L;
    private final List<IProcessingUnitPersistence> processingUnitPersistenceList;
    private IObjectLockManager objectLockManager;
    private IProcessingUnitStatus suspendProcessingUnitStatus;

    
    /**
     * Constructor for ParallelProcessingUnitPersistenceContainer
     */
    public ParallelProcessingUnitPersistenceContainer() {
        this.processingUnitPersistenceList = new ArrayList<IProcessingUnitPersistence>();
        this.objectLockManager = null;
        this.suspendProcessingUnitStatus = null;
    }
    
    
    /**
     * Add a processing unit persistence and its processing unit status
     * 
     * @param processingUnitPersistence the processing unit persistence
     */
    public void addProcessingUnitPersistence(IProcessingUnitPersistence processingUnitPersistence) {
        processingUnitPersistenceList.add(processingUnitPersistence);
    }

    
    /**
     * Get the processing unit persistence
     *
     * @return the processing unit persistence
     */
    public List<IProcessingUnitPersistence> getProcessingUnitPersistenceList() {
        return processingUnitPersistenceList;
    }


    /**
     * Get the object lock manager
     *
     * @return the object lock manager
     */
    public IObjectLockManager getObjectLockManager() {
        return objectLockManager;
    }


    /**
     * Get the object lock manager
     *
     * @param objectLockManager the object lock manager
     */
    public void setObjectLockManager(IObjectLockManager objectLockManager) {
        this.objectLockManager = objectLockManager;
    }


    /**
     * Get the suspend processing unit status
     *
     * @return the suspend processing unit status
     */
    public IProcessingUnitStatus getSuspendProcessingUnitStatus() {
        return suspendProcessingUnitStatus;
    }


    /**
     * Set the suspend processing unit status
     *
     * @param suspendProcessingUnitStatus the suspend processing unit status
     */
    public void setSuspendProcessingUnitStatus(IProcessingUnitStatus suspendProcessingUnitStatus) {
        this.suspendProcessingUnitStatus = suspendProcessingUnitStatus;
    }
}
