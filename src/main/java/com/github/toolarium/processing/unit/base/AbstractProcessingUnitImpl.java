/*
 * AbstractProcessingUnitImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.base;

import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.IParameterRuntime;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitUpdateProgress;
import com.github.toolarium.processing.unit.runtime.ParameterRuntime;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitProgress;
import java.util.List;


/**
 * Abstract base implementation of the {@link IProcessingUnit}.
 *
 * @author patrick
 */
public abstract class AbstractProcessingUnitImpl implements IProcessingUnit {
    private IParameterRuntime parameterRuntime;
    private IProcessingUnitContext processingUnitContext;
    private ProcessingUnitProgress processingUnitProgress;
    
    
    /**
     * Constructor
     */
    protected AbstractProcessingUnitImpl() {
        parameterRuntime = new ParameterRuntime();
        processingUnitContext = null;
        processingUnitProgress = new ProcessingUnitProgress();
        
        // intialize the parameter definition
        initializeParameterDefinition();
    }

    
    /**
     * Initialize the parameter definition: This method is used to register the parameter definition of this
     * processing unit. The method <code>addParameterDefinition</code> can be used for convenience.
     * 
     * <p>In case of inheritance the <code>super.initializeParameterDefinition()</code> must be called before or 
     * after the
     */
    protected void initializeParameterDefinition() {
        // NOP
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#getParameterDefinition()
     */
    @Override
    public List<ParameterDefinition> getParameterDefinition() {
        return getParameterRuntime().getParameterDefinition();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#validateParameterList(java.util.List)
     */
    @Override
    public void validateParameterList(List<Parameter> parameterList) throws ValidationException {
        getParameterRuntime().validateParameterList(parameterList);
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#initialize(java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void initialize(List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) throws ValidationException, ProcessingException {
        this.processingUnitContext = processingUnitContext;
        getParameterRuntime().setParameterList(parameterList, processingUnitContext);
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() throws ProcessingException {
        return getProcessingUnitProgress().getNumberOfUnitsToProcess();
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#processUnit()
     */
    @Override
    public IProcessingUnitStatus processUnit() throws ProcessingException {
        return processUnit(new ProcessingUnitStatusBuilder(processingUnitProgress));
    }

    
    /**
     * Process unit: This method will be called until the {@link IProcessingUnitStatus#hasNext} returns false.
     * Important: this method have to process the sequential or in a small block size.
     *
     * @param processingUnitStatusBuilder the processing unit status builder
     * @return the process unit status
     * @throws ProcessingException In case of any failures occurs.
     */
    public abstract IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) throws ProcessingException;


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#onEnding()
     */
    @Override
    public void onEnding() {
        // NOP
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#onAborting()
     */
    @Override
    public void onAborting() {
        // NOP
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#releaseResource()
     */
    @Override
    public void releaseResource() throws ProcessingException {
        // NOP
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#suspendProcessing()
     */
    @Override
    public IProcessingUnitPersistence suspendProcessing() throws ProcessingException {
        return null;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#resumeProcessing(com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.IProcessingUnitPersistence)
     */
    @Override
    public void resumeProcessing(IProcessingUnitProgress processingUnitProgress, IProcessingUnitPersistence processingPersistence) throws ProcessingException {
        this.processingUnitProgress = new ProcessingUnitProgress(processingUnitProgress);
    }

    
    /**
     * Get the parameter runtime information.
     *
     * @return the parameter runtime information.
     */
    protected IParameterRuntime getParameterRuntime() {
        return parameterRuntime;
    }
    
    
    /**
     * Get the process context 
     *
     * @return the process context
     */
    protected IProcessingUnitContext getProcessingUnitContext() {
        return processingUnitContext;
    }


    /**
     * Get the processing unit progress 
     *
     * @return the processing unit progress
     */
    protected IProcessingUnitUpdateProgress getProcessingUnitProgress() {
        return processingUnitProgress;
    }
}
