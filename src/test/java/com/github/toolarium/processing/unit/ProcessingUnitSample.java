/*
 * ProcessingUnitSample.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;


/**
 * Implements a simple processing unit
 *   
 * @author patrick
 */
public class ProcessingUnitSample extends AbstractProcessingUnitImpl {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    private static final  ParameterDefinition INPUT_FILENAME_PARAMETER = 
            new ParameterDefinition("inputFilename", ParameterValueType.STRING,
                                    ParameterDefinition.NO_DEFAULT_PARAMETER, ParameterDefinition.NOT_OPTIONAL, 1,
                                    ParameterDefinition.EMPTY_VALUE_NOT_ALLOWED, "The filename incl. path to read in a file.");

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(INPUT_FILENAME_PARAMETER); // register parameters
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#countNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    protected long countNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) {
        // check how many entries we have to process, e.g. counting database records to process
        // it will be called just once, the first time before start processing
        // this number will be set in getProcessingProgress().setNumberOfUnitsToProcess(...) 
        return 10;
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public IProcessStatus processUnit(IProcessingUnitContext processingUnitContext) throws ProcessingException {
        
        // This is the main part where the processing takes place
        
        // During a processing step status message can be returned
        getProcessingProgress().setStatusMessage("");
        
        // A status SUCCESSFUL, WARN or ERROR can be set
        getProcessingProgress().setProcessingRuntimeStatus(ProcessingRuntimeStatus.WARN);
        
        // It is called as long as getProcessStatus().setHasNext is set to false.
        getProcessStatus().setHasNext(true);
        return getProcessStatus();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#releaseResource()
     */
    /* In case we have to release any resources
    @Override
    public void releaseResource() throws ProcessingException {
    }
    */
}
