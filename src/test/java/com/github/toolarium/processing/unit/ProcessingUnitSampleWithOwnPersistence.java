/*
 * ProcessingUnitSampleWithOwnPersistence.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import java.util.List;


/**
 * Implements a simple processing unit with own persistence
 * 
 * @author patrick
 */
public class ProcessingUnitSampleWithOwnPersistence extends AbstractProcessingUnitImpl {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    public static final  ParameterDefinition INPUT_FILENAME_PARAMETER = 
            new ParameterDefinition("inputFilename", ParameterValueType.STRING,
                                    ParameterDefinition.NO_DEFAULT_PARAMETER, ParameterDefinition.NOT_OPTIONAL, 1,
                                    ParameterDefinition.EMPTY_VALUE_NOT_ALLOWED, "The filename incl. path to read in a file.");
    
    // our self defined persistence
    private SamplePersistence persistence;

    
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
        
        // During a processing step status message can be returned, a status SUCCESSFUL, WARN or ERROR can be set
        //getProcessingProgress().setStatusMessage("Warning sample");
        //getProcessingProgress().setProcessingRuntimeStatus(ProcessingRuntimeStatus.WARN);

        // Support of additional statistic:
        //getProcessingProgress().addStatistic("counter", 1d);

        // Increase the number of processed units
        getProcessingProgress().increaseNumberOfProcessedUnits();
        
        // If it was failed you can increase the number of failed units
        //getProcessingProgress().increaseNumberOfFailedUnits();
        
        // It is called as long as getProcessStatus().setHasNext is set to false.
        getProcessStatus().setHasNext(getProcessingProgress().getNumberOfUnprocessedUnits() > 0);
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
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#suspendProcessing()
     */
    @Override
    public IProcessingPersistence suspendProcessing() throws ProcessingException {
        return persistence; // in case of a suspend of the processing the self defined persistence we return.
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#resumeProcessing(java.util.List, com.github.toolarium.processing.unit.IProcessingProgress, 
     *      com.github.toolarium.processing.unit.IProcessingPersistence, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void resumeProcessing(List<Parameter> parameterList, IProcessingProgress resumeProcessingProgress, IProcessingPersistence processingPersistence, IProcessingUnitContext processingUnitContext) throws ProcessingException {        
        super.initialize(parameterList, processingUnitContext);
        
        // this method is called in case the processing is resumed after a suspending and we get back the self defined persistence.
        persistence = (SamplePersistence)processingPersistence;

        // initialize previous state
        getProcessingProgress().init(resumeProcessingProgress);
    }
    
    
    /**
     * Define sample an own persistence 
     * 
     * @author patrick
     */
    class SamplePersistence implements IProcessingPersistence {
        private static final long serialVersionUID = -178680376384580300L;
        private String text;
        private int counter;
        
        /**
         * Get text
         *
         * @return the text
         */
        public String getText() {
            return text;
        }
        
        /**
         * Set text
         *
         * @param text the text
         */
        public void setText(String text) {
            this.text = text;
        }
        
        
        /**
         * Get the counter
         *
         * @return the counter
         */
        public int getCounter() {
            return counter;
        }
        
        
        /**
         * Set the counter
         *
         * @param counter the counter
         */
        public void setCounter(int counter) {
            this.counter = counter;
        }
    }
}
