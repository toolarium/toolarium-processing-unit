/*
 * AbstractProcessingUnitRunnable.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable.impl;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import com.github.toolarium.processing.unit.runtime.runnable.ProcessingUnitProxy;
import java.util.List;
import java.util.UUID;


/**
 * Abstract {@link IProcessingUnitRunnable} implementation.
 * 
 * @author patrick
 */
public abstract class AbstractProcessingUnitRunnable implements IProcessingUnitRunnable {
    private String id;
    private final Class<? extends IProcessingUnit> processingUnitClass;
    private final List<Parameter> parameterList;
    private final IProcessingUnitContext processingUnitContext;
    private ProcessingUnitProxy processingUnitProxy;

    
    /**
     * Constructor
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingUnitContext the processing context.
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public AbstractProcessingUnitRunnable(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) {
        this.id = UUID.randomUUID().toString();
        this.processingUnitClass = processingUnitClass;
        this.parameterList = parameterList;
        this.processingUnitContext = processingUnitContext;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getId()
     */
    @Override
    public String getId() {
        return id;
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getProcessStatus()
     */
    @Override
    public IProcessStatus getProcessStatus() {
        IProcessingUnitProxy processingUnitProxy = getProcessingUnitProxy();
        if (processingUnitProxy != null) {
            return processingUnitProxy.getProcessStatus();
        }
        
        return null;
    }


    /**
     * Get the processing progress
     *
     * @return the processing progress
     */
    protected IProcessingProgress getProcessingProgress() {
        IProcessStatus processStatus = getProcessStatus();
        if (processStatus != null) {
            return processStatus.getProcessingProgress();
        }
        
        return null;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getNumberOfUnitsToProcess()
     */
    @Override
    public long getNumberOfUnitsToProcess() {
        IProcessingProgress processingProgress = getProcessingProgress();
        if (processingProgress != null) {
            return processingProgress.getTotalUnits() - processingProgress.getProcessedUnits();
        }
        return 0;
    }

    
    /**
     * Get the parameter list
     *
     * @return the parameter list
     */
    protected  List<Parameter> getParameterList() {
        return parameterList;
    }

    
    /**
     * Get the processing unit context
     *
     * @return the processing unit context
     */
    protected IProcessingUnitContext getProcessingUnitContext() {
        return processingUnitContext;
    }

    
    /**
     * Create processing unit proxy
     * 
     * @return the processing unit proxy
     */
    protected IProcessingUnitProxy createProcessingUnitProxy() {
        processingUnitProxy = ProcessingUnitProxy.init(processingUnitClass, parameterList, processingUnitContext /* new ProcessingUnitContext(processingUnitContext) */);
        return processingUnitProxy;
    }
    
    
    /**
     * Get the processing unit proxy
     *
     * @return the processing unit proxy
     */
    protected ProcessingUnitProxy getProcessingUnitProxy() {
        return processingUnitProxy;
    }

    
    /**
     * Set the processing unit proxy
     *
     * @param processingUnitProxy the processing unit proxy
     */
    protected void setProcessingUnitProxy(ProcessingUnitProxy processingUnitProxy) {
        this.processingUnitProxy = processingUnitProxy;
    }

    
    /**
     * Before process unit method call
     * 
     * @param continueProcessing the continue processing 
     * @return the continue processing
     */
    protected boolean afterProcessUnit(boolean continueProcessing) {
        return continueProcessing;
    }
}
