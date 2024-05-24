/*
 * IProcessingStatistic.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import java.util.Set;


/**
 * The processing statistic
 *
 * @author patrick
 */
public interface IProcessingStatistic {

    /**
     * Returns the statistic key set
     *
     * @return the key set
     */
    Set<String> keySet();


    /**
     * Gets the statistic value
     *
     * @param key the statistic key
     * @return the value if it was set; otherwise null
     */
    Double get(String key);
}
