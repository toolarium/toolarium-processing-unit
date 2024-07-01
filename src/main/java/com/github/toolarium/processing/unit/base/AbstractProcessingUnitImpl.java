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
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.IParameterRuntime;
import com.github.toolarium.processing.unit.runtime.ParameterRuntime;
import java.util.List;


/**
 * Abstract base implementation of the {@link IProcessingUnit}.
 *
 * @author patrick
 */
public abstract class AbstractProcessingUnitImpl implements IProcessingUnit {
    private IParameterRuntime parameterRuntime;
    
    
    /**
     * Constructor
     */
    protected AbstractProcessingUnitImpl() {
        parameterRuntime = new ParameterRuntime();
    
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
        getParameterRuntime().setParameterList(parameterList, processingUnitContext);
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#processUnit(com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public abstract IProcessingUnitStatus processUnit(IProcessingUnitProgress processingProgress, IProcessingUnitContext processingUnitContext) throws ProcessingException;


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
    public IProcessingUnitPersistence suspendProcessing() throws ProcessingException {
        return null;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#resumeProcessing(com.github.toolarium.processing.unit.IProcessingUnitPersistence, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void resumeProcessing(IProcessingUnitPersistence processingPersistence, IProcessingUnitContext processingUnitContext) 
            throws ProcessingException {
    }

    
    /**
     * Estimate the number of units to process. It will be called once in the process of initialization.
     * 
     * @param processingUnitContext the processing context.
     * @return returns the number of units to process
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public abstract long estimateNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) throws ProcessingException;


    /**
     * Get the parameter runtime information.
     *
     * @return the parameter runtime information.
     */
    protected IParameterRuntime getParameterRuntime() {
        return parameterRuntime;
    }
}
