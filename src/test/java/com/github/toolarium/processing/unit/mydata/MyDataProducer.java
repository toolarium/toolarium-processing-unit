/*
 * MyDataProducer.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.mydata;

import com.github.toolarium.common.util.RandomGenerator;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Simple data producer.
 *
 * @author patrick
 */
final class MyDataProducer implements IProcessingUnitPersistence {
    private static final long serialVersionUID = -7861222316001789141L;
    private Queue<String> queue;


    /**
     * Constructor
     */
    MyDataProducer() {
        queue = null;
    }


    /**
     * Initialize data producer
     * 
     * @param num the number of elements
     */
    public void init(int num) {
        List<String> dataList = new ArrayList<String>();
        
        for (int i = 0; i < num; i++) {
            char c = RandomGenerator.validLowerCaseLetterCharacters[i % RandomGenerator.validLowerCaseLetterCharacters.length];
            dataList.add("" + c);
        }
        
        this.queue = new LinkedBlockingQueue<String>(dataList);
    }


    /**
     * Close the data producer
     */
    public void close() {
        this.queue = null;
    }


    /**
     * Get data
     *
     * @return the data
     */
    public String getData() {
        return this.queue.poll();
    }


    /**
     * Check if there is more data
     *
     * @return the data
     */
    public boolean hasMoreData() {
        return this.queue.size() > 0;
    }


    /**
     * Gets the size
     *
     * @return the size
     */
    public int getSize() {
        return this.queue.size();
    }
}
