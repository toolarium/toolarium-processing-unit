/*
 * ParallelProcessingUnitSample.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parallelization;

import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Implements a parallel processing unit
 *  
 * @author patrick
 */
public class ParallelProcessingUnitSample extends AbstractProcessingUnitPersistenceImpl<ParallelProcessingPersistence> implements IParallelProcessingUnit, IProcessingUnitObjectLockManagerSupport {
    /** NUMBER_OF_WORDS: the number of words. */
    public static final ParameterDefinition NUMBER_OF_WORDS = new ParameterDefinitionBuilder().name("numberOfWords").defaultValue(10000).description("The number of words.").build();
    private Queue<String> wordBuffer;
    private List<String> wordResultList;
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        System.err.println("2===>"+getParameterRuntime().hashCode());

        getParameterRuntime().addParameterDefinition(NUMBER_OF_WORDS); // register parameters
        System.err.println("2===>"+getParameterRuntime().getParameterValueList(NUMBER_OF_WORDS));
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initialize(java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void initialize(List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) throws ValidationException, ProcessingException {
        super.initialize(parameterList, processingUnitContext);
        wordResultList = new ArrayList<String>();
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() {
        // simulate data source: it's in a static way so multiple threads see the same -> this method is only called ones fron the first thread
        TextSource.getInstance().createText(getParameterRuntime().getParameterValueList(NUMBER_OF_WORDS).getValueAsLong());
        
        return getProcessingUnitProgress().setNumberOfUnitsToProcess(getParameterRuntime().getParameterValueList(NUMBER_OF_WORDS).getValueAsLong());
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder)
     */
    @Override
    public IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) throws ProcessingException {
        int blockSize = 10;
        
        if (wordBuffer == null || wordBuffer.isEmpty()) {
            wordBuffer = new LinkedBlockingQueue<String>(getObjectLockManager().lock(TextSource.getInstance().getWords(blockSize)));
            TextSource.getInstance().incrementPosition(wordBuffer.size()); // only mark
        }

        if (!wordBuffer.isEmpty()) {
            String text = wordBuffer.poll();
            if (text != null && !text.isBlank()) {
                wordResultList.add(text);
                
                // mark one as processed
                TextSource.getInstance().incrementPosition();
                processingUnitStatusBuilder.processedSuccessful();
                processingUnitStatusBuilder.statistic("processedWord", 1L);
            } else {
                processingUnitStatusBuilder.warn("Empty text dound!");
                processingUnitStatusBuilder.processingUnitFailed();
            }
            
            processingUnitStatusBuilder.hasNext(!wordBuffer.isEmpty() || TextSource.getInstance().hasMoreWords());            
        } else {
            processingUnitStatusBuilder.warn("Found no text!");
            processingUnitStatusBuilder.processingUnitFailed();
            processingUnitStatusBuilder.hasNext(false);
        }
        
        return processingUnitStatusBuilder.build();
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#suspendProcessing()
     */
    @Override
    public IProcessingUnitPersistence suspendProcessing() throws ProcessingException {
        if (wordBuffer != null && !wordBuffer.isEmpty()) {
            getProcessingPersistence().setQueue(wordBuffer);
        }

        getProcessingPersistence().setWordResultList(wordResultList);
        return super.suspendProcessing();
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#resumeProcessing(com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.IProcessingUnitPersistence)
     */
    @Override
    public void resumeProcessing(IProcessingUnitProgress processingUnitProgress, IProcessingUnitPersistence processingPersistence) throws ProcessingException {
        super.resumeProcessing(processingUnitProgress, processingPersistence);
        
        wordBuffer = getProcessingPersistence().getBuffer();
        wordResultList = getProcessingPersistence().getWordResultList();
        removePersistenceInstance();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#releaseResource()
     */
    @Override
    public void releaseResource() throws ProcessingException {
        wordBuffer = null;
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#newPersistenceInstance()
     */
    @Override
    protected ParallelProcessingPersistence newPersistenceInstance() {
        return new ParallelProcessingPersistence();
    }
}
