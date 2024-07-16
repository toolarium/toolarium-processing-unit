/*
 * ProcessingUnitSample.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;


/**
 * Implements a simple processing unit
 *   
 * @author patrick
 */
public class ProcessingUnitSample extends AbstractProcessingUnitImpl {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    public static final  ParameterDefinition INPUT_FILENAME_PARAMETER = new ParameterDefinitionBuilder().name("inputFilename").isMandatory().description("The filename incl. path to read in a file.").build();

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(INPUT_FILENAME_PARAMETER); // register parameters
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() {
        return getProcessingUnitProgress().setNumberOfUnitsToProcess(10);
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder)
     */
    @Override
    public IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) throws ProcessingException {
        // This is the main part where the processing takes place

        // In case of successful processing
        processingUnitStatusBuilder.processedSuccessful();
        
        // other wise if it was failed
        //processingUnitStatusBuilder.processingUnitFailed();

        // During a processing step status message can be returned, a status SUCCESSFUL, WARN or ERROR. Additional a message can be set
        //processingUnitStatusBuilder.warn("Warning sample");
        //processingUnitStatusBuilder.error("Error sample");
        //processingUnitStatusBuilder.message("Error sample");

        // Support of statistic:
        //processingUnitStatusBuilder.statistic("counter", 1);
        
        return processingUnitStatusBuilder.hasNext().build();
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
