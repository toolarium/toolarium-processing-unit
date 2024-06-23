/*
 * IProcessingUnitProxy.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingPersistence;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import java.util.List;


/**
 * Defines the {@link IProcessingUnit} proxy. It manages the full handling for startup, possible suspends/resumes and ending
 * even the validation. 
 * 
 * @author patrick
 */
public interface IProcessingUnitProxy {
    
    
    /**
     * Get the runnable id
     *
     * @return the runnable id
     */
    String getId();

    
    /**
     * Get the runnable name or null
     *
     * @return the name or null
     */
    String getName();

    
    /**
     * Gets the parameter definition list of the processing.
     *
     * @return the parameter definition list or null in case of no parameters
     */
    List<ParameterDefinition> getParameterDefinition();
    
    
    /**
     * Gets the last process status
     *
     * @return the last process status
     */
    IProcessStatus getProcessStatus();

    
    /**
     * Get the max number of processing unit calls per seconds
     *
     * @return The max number of processing unit calls per seconds
     */
    Long getMaxNumberOfProcessingUnitCallsPerSecond();

    
    /**
     * Process unit: This method will be called until the {@link IProcessStatus#hasNext} returns false
     * or an exception occurs: RuntimeException will abort; ValidationException or ProcessingException depends
     * on the value returned by the method abortProcessing.
     * Important: this method have to process the sequential or in a small block size.
     *
     * @return true to continue the processing; otherwise false
     */
    boolean processUnit();


    /**
     * Suspends the processing: The processing is able to persist its state with the help of the {@link IProcessingPersistence} object.
     * On a resume this instance of the {@link IProcessingPersistence} will be returned (see method below). 
     *
     * @return the processing persistence which contains all information to resume processing later (see resumeProcessing).
     * @throws ProcessingException Throws this exception in case of while suspend the processing any failures occurs.
     */
    byte[] suspendProcessing()
        throws ProcessingException;


    /**
     * Release resources will be called to release all internal referenced resources after a processing success, error 
     * or by a <code>suspendProcessing</code> (see method below). It will be called after <code>onSuccess</code> 
     * or <code>onStop</code>.
     *
     * @throws ProcessingException Throws this exception in case of releasing failure.
     */
    void releaseResource()
        throws ProcessingException;
}
