/*
 * AbstractProcessingUnitPersistenceImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.base;

import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.exception.ProcessingException;


/**
 * Abstract base implementation of the {@link IProcessingUnit} which supports own persistence class
 *
 * @author patrick
 */
public abstract class AbstractProcessingUnitPersistenceImpl<T extends IProcessingUnitPersistence> extends AbstractProcessingUnitImpl {
    private T persistence;
    

    /**
     * Get a new persistence instance
     *
     * @return the persistence instance
     */
    protected abstract T newPersistenceInstance();
    
    
    /**
     * Returns the processing persistence instance
     *
     * @return the processing persistence instance
     */
    protected T getProcessingPersistence() {
        if (persistence == null) {
            persistence = newPersistenceInstance();
        }
        return persistence;
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#suspendProcessing()
     */
    @Override
    public IProcessingUnitPersistence suspendProcessing() throws ProcessingException {
        return persistence; // in case of a suspend of the processing the self defined persistence we return.
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#resumeProcessing(com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.IProcessingUnitPersistence)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void resumeProcessing(IProcessingUnitProgress processingUnitProgress, IProcessingUnitPersistence processingPersistence) throws ProcessingException {
        super.resumeProcessing(processingUnitProgress, processingPersistence);
        
        // set the processing persistence
        persistence = (T)processingPersistence;
    }
}
