/*
 * IProcessingUnitContext.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import java.util.Set;


/**
 * Defines the processing unit context
 * 
 * @author patrick
 */
public interface IProcessingUnitContext {
    
    /**
     * Set a context key / value pair
     *
     * @param key the key
     * @param value the value
     * @return the previous value or null
     */
    String set(String key, String value);

    
    /**
     * Get a context value of a defined key
     *
     * @param key the key
     * @return the value
     */
    String get(String key);
    
    
    /**
     * Removes a key in the context and return the removed value.
     * In case the key don't exist it returns null.
     *
     * @param key the key
     * @return the value
     */
    String remove(String key);

    
    /**
     * Gets a set of the context keys back.
     *
     * @return the keys
     */
    Set<String> keySet();
    
    
    /**
     * Check if the statistic if empty
     *
     * @return true if it is empty
     */
    boolean isEmpty();

    
    /**
     * Clears the context
     */
    void clear();
}
