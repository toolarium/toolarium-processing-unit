/*
 * ProcessingUnitStringTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.framework;


import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingPersistence;
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.runtime.ProcessStatus;
import java.util.List;
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
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#countNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    protected long countNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) {
        this.queue = new LinkedBlockingQueue<String>(getParameterRuntime().getParameterValueList(DATA_FEED_PARAMTER).getValueAsStringList());
        return this.queue.size();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public IProcessStatus processUnit(IProcessingUnitContext processingUnitContext) {
        ProcessStatus status = super.getProcessStatus();
        String val = queue.poll();
        result += "[" + val;
        
        status.setHasNext(true);
        if (val == null || queue.size() <= 0) {
            status.setHasNext(false);
        } 
        
        if (val.isEmpty()) {
            getProcessingProgress().setProcessingRuntimeStatus(ProcessingRuntimeStatus.WARN);
            getProcessingProgress().setStatusMessage("Empty data");
            getProcessingProgress().increaseNumberOfFailedUnits();
        }
        getProcessingProgress().increaseNumberOfProcessedUnits();

        result += "(" + status + ")]";
        if (status.hasNext()) {
            result += " | ";
        }

        return status;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#suspendProcessing()
     */
    @Override
    public IProcessingPersistence suspendProcessing() throws ProcessingException {
        return new ProcessingPersistenceImpl(queue, result);
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#resumeProcessing(java.util.List, com.github.toolarium.processing.unit.IProcessingProgress, 
     * com.github.toolarium.processing.unit.IProcessingPersistence, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void resumeProcessing(List<Parameter> parameterList, IProcessingProgress resumeProcessingProgress, IProcessingPersistence processingPersistence, IProcessingUnitContext processingUnitContext) throws ProcessingException {
        super.initialize(parameterList, processingUnitContext);
        this.queue = ((ProcessingPersistenceImpl)processingPersistence).getQueue();
        this.result = ((ProcessingPersistenceImpl)processingPersistence).getResult();
        getProcessingProgress().init(resumeProcessingProgress);
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
    static class ProcessingPersistenceImpl implements IProcessingPersistence {
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
