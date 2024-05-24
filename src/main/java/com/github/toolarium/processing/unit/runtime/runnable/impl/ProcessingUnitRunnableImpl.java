/*
 * ProcessingUnitThread.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable.impl;

import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import java.util.List;


/**
 * Implements a {@link IProcessingUnitRunnable}. 
 * 
 * @author patrick
 */
public class ProcessingUnitRunnableImpl extends AbstractProcessingUnitRunnable {
    
    /**
     * Constructor
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingUnitContext the processing context.
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public ProcessingUnitRunnableImpl(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) {
        super(processingUnitClass, parameterList, processingUnitContext);
    }
    

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            createProcessingUnitProxy();
            if (getProcessingUnitProxy() == null) {
                throw new ProcessingException("Could not initialize processing!", true);
            }

            boolean continueProcessing;
            do {
                continueProcessing = getProcessingUnitProxy().processUnit();
                continueProcessing = afterProcessUnit(continueProcessing);
                
                // TODO: handling throttling
            } while (continueProcessing);

            // in case continue processing is marked as false but there are still unit to process open, there we have to stop
            if (!continueProcessing && getNumberOfUnitsToProcess() > 0) {
                getProcessingUnitProxy().getProcessingUnit().onStop();
            } else {
                getProcessingUnitProxy().getProcessingUnit().onSuccess();
            }
        } finally {
            // release
            if (getProcessingUnitProxy() != null) {
                getProcessingUnitProxy().releaseResource();
            }
        }
    }
}
