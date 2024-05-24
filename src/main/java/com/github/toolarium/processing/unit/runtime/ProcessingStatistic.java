/*
 * ProcessingStatisticImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;

import com.github.toolarium.processing.unit.IProcessingStatistic;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Implements {@link IProcessingStatistic}.
 *
 * @author patrick
 */
public class ProcessingStatistic implements IProcessingStatistic, Serializable {
    private static final long serialVersionUID = -5135299892419950527L;
    private Map<String, Double> statisticData;


    /**
     * Constructor
     */
    public ProcessingStatistic() {
        statisticData = new ConcurrentHashMap<String, Double>();
    }


    /**
     * Constructor
     * 
     * @param processingStatistic the processing statistic
     */
    public ProcessingStatistic(IProcessingStatistic processingStatistic) {
        statisticData = new ConcurrentHashMap<String, Double>();
        for (String key : processingStatistic.keySet()) {
            put(key, processingStatistic.get(key));
        }
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingStatistic#keySet()
     */
    @Override
    public Set<String> keySet() {
        SortedSet<String> sortedKeys = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        sortedKeys.addAll(statisticData.keySet());
        return sortedKeys;
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingStatistic#get(java.lang.String)
     */
    @Override
    public Double get(String key) {
        return statisticData.get(key);
    }


    /**
     * Put a statistic value
     *
     * @param key the key
     * @param value the statistic value
     * @return the previous set statistic value; otherwise null
     */
    public Double put(String key, Double value) {
        return statisticData.put(key, value);
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(statisticData);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;           
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        ProcessingStatistic other = (ProcessingStatistic) obj;
        return Objects.equals(statisticData, other.statisticData);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[{");
        boolean addSeparator = false;
        for (String key : keySet()) {
            if (addSeparator) {
                result.append(", ");
            } else {
                addSeparator = true;
            }

            result.append(key);
            result.append("=");
            result.append(statisticData.get(key));
        }

        result.append("}]");

        return result.toString();
    }
}
