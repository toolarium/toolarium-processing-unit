/*
 * IProcessingUnit.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import java.util.List;


/**
 * Defines the processing unit interface.
 *
 * @author patrick
 */
public interface IProcessingUnit {

    /**
     * Gets the parameter definition list of the processing.
     *
     * @return the parameter definition list or null in case of no parameters
     */
    List<ParameterDefinition> getParameterDefinition();

    
    /**
     * Validate the parameter list consistency of this class with the parameters. Typical it will be called during the initialization.
     * It can be used to verify if the parameter of this processing are well defined.
     *
     * @param parameterList the parameter list.
     * @throws ValidationException This will be throw in case the consistency check failures.
     */
    void validateParameterList(List<Parameter> parameterList) 
        throws ValidationException;
    
    
    /**
     * Initializes the processing unit. This will called as first to initialize the processing unit.
     * 
     * @param parameterList the parameter list to run the processing.
     * @param processingUnitContext the processing context.
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    void initialize(List<Parameter> parameterList, IProcessingUnitContext processingUnitContext)
        throws ValidationException, ProcessingException;

    
    /**
     * Process unit: This method will be called until the {@link IProcessStatus#hasNext} returns false.
     * Important: this method have to process the sequential or in a small block size.
     *
     * @param processingUnitContext the processing unit context.
     * @return the process status
     * @throws ProcessingException In case of any failures occurs.
     */
    IProcessStatus processUnit(IProcessingUnitContext processingUnitContext)
        throws ProcessingException;


    /**
     * This method will be called after a success end.
     */
    void onSuccess();


    /**
     * This method will be called in case on a stop before.
     */
    void onStop();

    
    /**
     * Release resources will be called to release all internal referenced resources after a processing success, error 
     * or by a <code>suspendProcessing</code> (see method below). It will be called after <code>onSuccess</code> 
     * or <code>onStop</code>.
     *
     * @throws ProcessingException Throws this exception in case of releasing failure.
     */
    void releaseResource()
        throws ProcessingException;

    
    /**
     * Suspends the processing: The processing is able to persist its state with the help of the {@link IProcessingPersistence} object.
     * On a resume this instance of the {@link IProcessingPersistence} will be returned (see method below). 
     *
     * @return the processing persistence which contains all information to resume processing later (see resumeProcessing).
     * @throws ProcessingException Throws this exception in case of while suspend the processing any failures occurs.
     */
    IProcessingPersistence suspendProcessing()
        throws ProcessingException;


    /**
     * After suspending a processing unit can be resumed. The parameter list of the initialization is passed as well the {@link IProcessingPersistence}
     * which was returned by the suspendProcessing method.
     *
     * @param parameterList the starting parameters
     * @param processingProgress the last processing progress before suspending.
     * @param processingPersistence the processing persistence to resume after suspending.
     * @param processingUnitContext the processing unit context
     * @throws ProcessingException Throws this exception in case of while resume the processing any failures occurs.
     */
    void resumeProcessing(List<Parameter> parameterList, IProcessingProgress processingProgress, IProcessingPersistence processingPersistence, IProcessingUnitContext processingUnitContext)
        throws ProcessingException;
}
