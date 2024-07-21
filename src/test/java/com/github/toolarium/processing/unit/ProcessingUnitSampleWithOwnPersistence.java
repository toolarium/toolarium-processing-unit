/*
 * ProcessingUnitSampleWithOwnPersistence.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;


/**
 * Implements a simple processing unit with own persistence
 * 
 * @author patrick
 */
public class ProcessingUnitSampleWithOwnPersistence extends AbstractProcessingUnitPersistenceImpl<ProcessingUnitSampleWithOwnPersistence.SamplePersistence> {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    public static final ParameterDefinition INPUT_FILENAME_PARAMETER = new ParameterDefinitionBuilder().name("inputFilename").isMandatory().description("The filename incl. path to read in a file.").build();
    
    
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
        
        getProcessingPersistence().setCounter(getProcessingPersistence().getCounter() + 1);
        getProcessingPersistence().setText("Counter" + getProcessingPersistence().getCounter());
        
        return processingUnitStatusBuilder.hasNextIfHasUnprocessedUnits().build();
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
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#newPersistenceInstance()
     */
    @Override
    protected SamplePersistence newPersistenceInstance() {
        return new SamplePersistence();
    }
    
    
    /**
     * Define sample an own persistence 
     * 
     * @author patrick
     */
    static class SamplePersistence implements IProcessingUnitPersistence {
        private static final long serialVersionUID = -178680376384580300L;
        private String text;
        private int counter;
        
        
        /**
         * Constructor for SamplePersistence
         */
        SamplePersistence() {
        }
        
        
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

        
        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "SamplePersistence [text=" + text + ", counter=" + counter + "]";
        }
    }
}
