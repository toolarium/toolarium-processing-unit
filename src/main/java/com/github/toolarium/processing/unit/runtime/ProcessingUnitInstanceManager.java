/*
 * ProcessingUnitInstanceManagerImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;

import com.github.toolarium.common.util.ClassInstanceUtil;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.runnable.parallelization.ParallelProcessingUnit;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link IProcessingUnitInstanceManager}.
 *  
 * @author patrick
 */
public class ProcessingUnitInstanceManager implements IProcessingUnitInstanceManager {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingUnitInstanceManager.class);

    /**
     * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitInstanceManager#createProcessingUnitInstance(java.lang.String, java.lang.String, java.lang.Class)
     */
    @Override
    public IProcessingUnit createProcessingUnitInstance(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
        if (processingUnitClass == null) {
            return null;
        }
        
        final String processing = ProcessingUnitUtil.getInstance().toString(id, name, processingUnitClass);
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(processing + " Initialize new processing unit instance [" + processingUnitClass + "].");
            }
            return ClassInstanceUtil.getInstance().newInstance(processingUnitClass);
        } catch (Exception t) {
            throw new ValidationException("Could not initialize " + processing + ": " + t.getMessage(), t);
        }
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitInstanceManager#createParallelProcessingUnitInstance(java.lang.String, java.lang.String, java.lang.Class)
     */
    @Override
    public IProcessingUnit createParallelProcessingUnitInstance(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
        if (processingUnitClass == null) {
            return null;
        }
        
        final String processing = ProcessingUnitUtil.getInstance().toString(id, name, processingUnitClass);
        try {
            IProcessingUnit processingUnit;
            
            if (ProcessingUnitUtil.getInstance().isParallelProcessingUnit(processingUnitClass)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(processing + " Initialize new parallel processing unit instance.");
                }
                processingUnit = new ParallelProcessingUnit(id, name, processingUnitClass);
            } else {
                processingUnit = createProcessingUnitInstance(id, name, processingUnitClass);
            }

            return processingUnit;
        } catch (Exception t) {
            throw new ValidationException("Could not initialize " + processing + ": " + t.getMessage(), t);
        }
    }
    

    /**
     * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitInstanceManager#releaseResource(java.lang.String, java.lang.String, com.github.toolarium.processing.unit.IProcessingUnit)
     */
    @Override
    public void releaseResource(String id, String name, IProcessingUnit processingUnit) {
        if (processingUnit == null) {
            return;
        }
        
        final String processing = ProcessingUnitUtil.getInstance().toString(id, name, processingUnit.getClass().getName());
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(processing + " Release resource of processing unit instance.");
            }
            processingUnit.releaseResource();
        } catch (Exception ex) {
            LOG.warn("Could not release resource from " + processing + ": " + ex.getMessage(), ex);
        }                
    }
}
