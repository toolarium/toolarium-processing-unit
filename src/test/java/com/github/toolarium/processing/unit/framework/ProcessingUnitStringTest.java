/*
 * ProcessingUnitStringTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.framework;


import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Simple process unit
 *
 * @author patrick
 */
public class ProcessingUnitStringTest extends AbstractProcessingUnitImpl {
    /** DATA_FEED */
    public static final String DATA_FEED = "dataFeed";
    private static final ParameterDefinition DATA_FEED_PARAMTER = new ParameterDefinitionBuilder().name(DATA_FEED).isMandatory().maxOccurs(10).emptyValueIsNotAllowed().description("The data feed.").build();
    private LinkedBlockingQueue<String> queue;
    private String result = "";


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    @Override
    protected void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(DATA_FEED_PARAMTER);
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() {
        this.queue = new LinkedBlockingQueue<String>(getParameterRuntime().getParameterValueList(DATA_FEED_PARAMTER).getValueAsStringList());
        return getProcessingUnitProgress().setNumberOfUnitsToProcess(this.queue.size());
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder)
     */
    @Override
    public IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) {

        String val = queue.poll();
        result += "[" + val;
        
        if (val == null || queue.size() <= 0) {
            processingUnitStatusBuilder.hasEnded();
        } else {
            processingUnitStatusBuilder.hasNextIfHasUnprocessedUnits();
        }
        
        if (val.isEmpty()) {
            processingUnitStatusBuilder.warn("Empty data");
            processingUnitStatusBuilder.increaseNumberOfFailedUnits();
        } else {
            processingUnitStatusBuilder.increaseNumberOfSuccessfulUnits();
        }

        IProcessingUnitStatus processingUnitStatus = processingUnitStatusBuilder.build();
        result += "(" + processingUnitStatus + ")]";
        if (processingUnitStatus.hasNext()) {
            result += " | ";
        }

        return processingUnitStatus;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#suspendProcessing()
     */
    @Override
    public IProcessingUnitPersistence suspendProcessing() throws ProcessingException {
        return new ProcessingPersistenceImpl(queue, result);
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#resumeProcessing(com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.IProcessingUnitPersistence)
     */
    @Override
    public void resumeProcessing(IProcessingUnitProgress processingUnitProgress, IProcessingUnitPersistence processingPersistence) throws ProcessingException {
        super.resumeProcessing(processingUnitProgress, processingPersistence);
        this.queue = ((ProcessingPersistenceImpl)processingPersistence).getQueue();
        this.result = ((ProcessingPersistenceImpl)processingPersistence).getResult();
    }


    /**
     * Get the processed result
     *
     * @return the result
     */
    public String getResult() {
        return result;
    }


    /**
     * Implements a simple processing persistence
     *
     * @author patrick
     */
    static class ProcessingPersistenceImpl implements IProcessingUnitPersistence {
        private static final long serialVersionUID = -3769111865952304752L;
        private LinkedBlockingQueue<String> queue;
        private String result;


        /**
         * Constructor
         *
         * @param queue the queue
         * @param result the result
         */
        ProcessingPersistenceImpl(LinkedBlockingQueue<String> queue, String result) {
            this.queue = queue;
            this.result = result;
        }


        /**
         * Gets the result
         *
         * @return the result
         */
        public String getResult() {
            return result;
        }


        /**
         * Get the queue
         *
         * @return the queue
         */
        public LinkedBlockingQueue<String> getQueue() {
            return queue;
        }
    }
}
