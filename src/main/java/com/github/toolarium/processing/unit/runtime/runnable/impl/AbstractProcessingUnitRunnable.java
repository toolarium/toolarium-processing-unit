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
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnableListener;
import com.github.toolarium.processing.unit.runtime.runnable.ProcessingUnitProxy;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract {@link IProcessingUnitRunnable} implementation.
 * 
 * @author patrick
 */
public abstract class AbstractProcessingUnitRunnable implements IProcessingUnitRunnable {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractProcessingUnitRunnable.class);
    private final String id;
    private final String name;
    private final Class<? extends IProcessingUnit> processingUnitClass;
    private final List<Parameter> parameterList;
    private final IProcessingUnitContext processingUnitContext;
    private ProcessingUnitProxy processingUnitProxy;
    private ProcessingActionStatus processingActionStatus;
    private IProcessingUnitRunnableListener processingUnitRunnableListener;

    
    /**
     * Constructor
     *
     * @param id the unique id of this processing 
     * @param name the name of this processing unit runnable
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingUnitContext the processing context.
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public AbstractProcessingUnitRunnable(final String id, 
                                          final String name, 
                                          final Class<? extends IProcessingUnit> processingUnitClass,
                                          final List<Parameter> parameterList, 
                                          final IProcessingUnitContext processingUnitContext) {
        if (id != null && !id.isBlank()) {
            this.id = id;
        } else {
            this.id = UUID.randomUUID().toString();
        }

        this.name = name;
        this.processingUnitClass = processingUnitClass;
        this.parameterList = parameterList;
        this.processingUnitContext = processingUnitContext;
        this.processingUnitRunnableListener = null;
    }

    
    /**
     * Constructor
     *
     * @param suspendedState the suspended state
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public AbstractProcessingUnitRunnable(final byte[] suspendedState) {
        ProcessingUnitProxy processingUnitProxy = ProcessingUnitProxy.resume(suspendedState);
        setProcessingUnitProxy(processingUnitProxy);
        
        this.id = processingUnitProxy.getId();
        this.name = processingUnitProxy.getName();
        this.processingUnitClass = processingUnitProxy.getProcessingUnitClass();
        this.parameterList = processingUnitProxy.getParameterList();
        this.processingUnitContext = processingUnitProxy.getProcessingUnitContext();
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getProcessingActionStatus()
     */
    @Override
    public ProcessingActionStatus getProcessingActionStatus() {
        return processingActionStatus;
    }
    
    
    /**
     * Sets the processing action status
     *
     * @param processingActionStatus the processing action status
     */
    protected void setProcessingActionStatus(ProcessingActionStatus processingActionStatus) {
        this.processingActionStatus = processingActionStatus;
        notifyProcessingUnitState(processingActionStatus);
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
        processingUnitProxy = ProcessingUnitProxy.init(id, name, processingUnitClass, parameterList, processingUnitContext /* new ProcessingUnitContext(processingUnitContext) */);
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

    
    /**
     * Set the processing unit runnable listener
     *
     * @param processingUnitRunnableListener the processing unit runnable listener
     */
    protected void setProcessingUnitRunnableListener(final IProcessingUnitRunnableListener processingUnitRunnableListener) {
        this.processingUnitRunnableListener = processingUnitRunnableListener;
    }

    
    /**
     * Notify processing unit action status
     *
     * @param processingActionStatus the processing action status
     */
    protected void notifyProcessingUnitState(final ProcessingActionStatus processingActionStatus) {
        if (processingUnitRunnableListener == null) {
            return;
        }
        
        try {
            processingUnitRunnableListener.notifyProcessingUnitState(getId(), getName(), processingUnitClass, processingActionStatus, getProcessingUnitContext(), getProcessingProgress());
        } catch (RuntimeException e) {
            LOG.warn("Could not notify the processing unit state to the processing unit runnable listener: " + e.getMessage(), e);
        }
    }
}
