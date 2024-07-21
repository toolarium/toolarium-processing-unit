/*
 * SamplePersistence.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parallelization;

import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import java.util.List;
import java.util.Queue;


/**
 * Defines the parallel processing persistence
 * 
 * @author patrick
 */
public class ParallelProcessingPersistence implements IProcessingUnitPersistence {
    private static final long serialVersionUID = -178680376384580300L;
    private Queue<String> wordBuffer;
    private List<String> wordResultList; 

    
    /**
     * Get the buffer
     *
     * @return the buffer
     */
    public Queue<String> getBuffer() {
        return wordBuffer;
    }

    
    /**
     * Set the buffer
     *
     * @param wordBuffer the buffer
     */
    public void setQueue(Queue<String> wordBuffer) {
        this.wordBuffer = wordBuffer;
    }

    
    /**
     * Get the result word list
     * 
     * @return the result word list
     */
    public List<String> getWordResultList() {
        return wordResultList;
    }

    
    /**
     * Set the result word list
     * 
     * @param wordResultList the word list
     */
    public void setWordResultList(List<String> wordResultList) {
        this.wordResultList = wordResultList;
    }
}

