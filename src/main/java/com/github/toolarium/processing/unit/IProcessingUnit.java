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
     * Initializes the processing unit. This will called as first to initialize the processing unit. It is also used
     * when processing is interrupted and resumed. 
     * 
     * @param parameterList the parameter list to run the processing.
     * @param processingUnitContext the processing context.
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    void initialize(List<Parameter> parameterList, IProcessingUnitContext processingUnitContext)
        throws ValidationException, ProcessingException;

        
    /**
     * Estimate the number of units to process. It will be called once after {@link #initialize(List, IProcessingUnitContext)}.
     * It set the the number of units to process in the object {@link IProcessingUnitProgress}. In case there are more elements
     * to process than estimated, the progress will adapted.
     * In case of a {@link #resumeProcessing(IProcessingUnitProgress, IProcessingUnitPersistence)}
     * it will not be called again.
     * 
     * @return returns the number of units to process
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    long estimateNumberOfUnitsToProcess() throws ProcessingException;
    
    
    /**
     * Process unit: This method will be called until the {@link IProcessingUnitStatus#hasNext} returns false.
     * Important: this method have to process the sequential or in a small block size.
     *
     * @return the process unit status
     * @throws ProcessingException In case of any failures occurs.
     */
    IProcessingUnitStatus processUnit()
        throws ProcessingException;
    

    /**
     * This method is called on ending after a successful end (status ending) before the resources are released. 
     */
    void onEnding();


    /**
     * This method is called in case of an abort (status aborting) before the resources are released.
     */
    void onAborting();
    

    /**
     * Release resources will be called to release all internal referenced resources after a processing success, error 
     * or by a <code>suspendProcessing</code> (see method below). It will be called after {@link #onEnding()} 
     * or {@link #onAborting()}.
     *
     * @throws ProcessingException Throws this exception in case of releasing failure.
     */
    void releaseResource()
        throws ProcessingException;

    
    /**
     * Suspends the processing: The processing is able to persist its state with the help of the {@link IProcessingUnitPersistence} object.
     * On a resume this instance of the {@link IProcessingUnitPersistence} will be returned (see method below). 
     * The {@link IProcessingUnitPersistence} don't need to contain any parameters or additional statuses. They are covered by the framework.
     *
     * @return the processing persistence which contains all information to resume processing later (see resumeProcessing).
     * @throws ProcessingException Throws this exception in case of while suspend the processing any failures occurs.
     */
    IProcessingUnitPersistence suspendProcessing()
        throws ProcessingException;


    /**
     * After suspending a processing unit can be resumed. The parameter list of the initialization is passed as well the {@link IProcessingUnitPersistence}
     * which was returned by the suspendProcessing method. The main purpose of this call is to set the processing persistence.
     *
     * @param processingUnitProgress the processing unit progress of the processing unit before suspending.
     * @param processingPersistence the processing persistence to resume after suspending.
     * @throws ProcessingException Throws this exception in case of while resume the processing any failures occurs.
     */
    void resumeProcessing(IProcessingUnitProgress processingUnitProgress, IProcessingUnitPersistence processingPersistence)
        throws ProcessingException;
}
