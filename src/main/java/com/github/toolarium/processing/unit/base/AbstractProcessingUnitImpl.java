/*
 * AbstractProcessingUnitImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.base;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingPersistence;
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.ParameterRuntime;
import com.github.toolarium.processing.unit.runtime.ProcessStatus;
import com.github.toolarium.processing.unit.runtime.ProcessingProgress;
import java.util.List;


/**
 * Abstract base implementation of the {@link IProcessingUnit}.
 *
 * @author patrick
 */
public abstract class AbstractProcessingUnitImpl implements IProcessingUnit {
    private IParameterRuntime parameterRuntime;
    private ProcessingProgress processingProgress;
    private ProcessStatus processStatus;

    
    /**
     * Constructor
     */
    protected AbstractProcessingUnitImpl() {
        parameterRuntime = new ParameterRuntime();
        processingProgress = new ProcessingProgress();
        processStatus = new ProcessStatus(processingProgress);
    
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
     * @see com.github.toolarium.processing.unit.IProcessingUnit#initialize(java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void initialize(List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) throws ValidationException, ProcessingException {
        getParameterRuntime().setParameterList(parameterList, processingUnitContext);
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#validateParameterList(java.util.List)
     */
    @Override
    public void validateParameterList(List<Parameter> parameterList) throws ValidationException {
        getParameterRuntime().validateParameterList(parameterList);
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public abstract IProcessStatus processUnit(IProcessingUnitContext processingUnitContext) throws ProcessingException;


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#onSuccess()
     */
    @Override
    public void onSuccess() {
        // NOP
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#onStop()
     */
    @Override
    public void onStop() {
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
    public IProcessingPersistence suspendProcessing() throws ProcessingException {
        return null;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#resumeProcessing(java.util.List, com.github.toolarium.processing.unit.IProcessingProgress, 
     * com.github.toolarium.processing.unit.IProcessingPersistence, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void resumeProcessing(List<Parameter> parameterList, IProcessingProgress resumeProcessingProgress, IProcessingPersistence processingPersistence, IProcessingUnitContext processingUnitContext) 
            throws ProcessingException {

        // initialize parameters
        initialize(parameterList, processingUnitContext);

        // initialize processing progress
        processingProgress.init(resumeProcessingProgress);
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
     * Get the process status
     *
     * @return the process status
     */
    protected ProcessStatus getProcessStatus() {
        return processStatus;
    }


    /**
     * Get the process status
     *
     * @return the process status
     */
    protected ProcessingProgress getProcessingProgress() {
        return processingProgress;
    }
}
