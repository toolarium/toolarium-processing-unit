/*
 * ProcessingUnitStatistic.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;

import com.github.toolarium.common.statistic.StatisticCounter;
import com.github.toolarium.common.util.RoundUtil;
import com.github.toolarium.processing.unit.IProcessingUnitStatistic;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Implements {@link IProcessingUnitStatistic}.
 *
 * @author patrick
 */
public class ProcessingUnitStatistic implements IProcessingUnitStatistic, Serializable {
    private static final long serialVersionUID = -5135299892419950527L;
    private Map<String, StatisticCounter> statisticData;


    /**
     * Constructor
     */
    public ProcessingUnitStatistic() {
        statisticData = new ConcurrentHashMap<String, StatisticCounter>();
    }


    /**
     * Constructor
     * 
     * @param processingStatistic the processing statistic
     */
    public ProcessingUnitStatistic(IProcessingUnitStatistic processingStatistic) {
        statisticData = new ConcurrentHashMap<String, StatisticCounter>();
        
        if (processingStatistic != null) {
            for (String key : processingStatistic.keySet()) {
                put(key, processingStatistic.get(key).clone());
            }
        }
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatistic#keySet()
     */
    @Override
    public Set<String> keySet() {
        SortedSet<String> sortedKeys = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        sortedKeys.addAll(statisticData.keySet());
        return sortedKeys;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatistic#hasKey(java.lang.String)
     */
    @Override
    public boolean hasKey(String key) {
        return statisticData.containsKey(key);
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatistic#get(java.lang.String)
     */
    @Override
    public StatisticCounter get(String key) {
        return statisticData.get(key);
    }

    
    /**
     * Get or add a statistic counter
     *
     * @param key the key
     * @return the statistic counter
     * @throws IllegalArgumentException In case of an invalid key
     */
    public StatisticCounter getOrAdd(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Invalid key!");
        }

        if (hasKey(key)) {
            return statisticData.get(key);
        }
        
        synchronized (statisticData) {
            StatisticCounter statisticCounter = statisticData.get(key);
            if (statisticCounter == null) {
                statisticCounter = new StatisticCounter();
                statisticData.put(key, statisticCounter);
            }

            return statisticCounter;
        }
    }

    
    /**
     * Add a value to the statistic counter
     *
     * @param key the key / name of the statistic
     * @param value the value to add
     * @return this instance
     */
    public ProcessingUnitStatistic add(String key, Long value) {
        if (value == null) {
            return this;
        }
        
        getOrAdd(key).add(value);
        return this;
    }


    /**
     * Add a value to the statistic counter
     *
     * @param key the key / name of the statistic
     * @param value the value to add
     * @return this instance
     */
    public ProcessingUnitStatistic add(String key, Double value) {
        if (value == null) {
            return this;
        }
        
        getOrAdd(key).add(value);
        return this;
    }

    
    /**
     * Add a statistic counter
     *
     * @param key the key
     * @param statisticCounterToAdd the statistic counter to add
     * @return this instance
     */
    public ProcessingUnitStatistic add(String key, final StatisticCounter statisticCounterToAdd) {
        if (statisticCounterToAdd == null) {
            return this;
        }

        getOrAdd(key).add(statisticCounterToAdd);
        return this;
    }


    /**
     * Put a statistic value
     *
     * @param key the key
     * @param value the statistic value
     * @return the previous set statistic value; otherwise null
     */
    public StatisticCounter put(String key, final StatisticCounter value) {
        return statisticData.put(key, value);
    }


    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitStatistic#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return statisticData.isEmpty();
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
        
        ProcessingUnitStatistic other = (ProcessingUnitStatistic) obj;
        return Objects.equals(statisticData, other.statisticData);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("ProcessingUnitStatistic ");
        result.append("[");
        
        if (!isEmpty()) {
            boolean addSeparator = false;
            for (String key : keySet()) {
                if (addSeparator) {
                    result.append(", ");
                } else {
                    addSeparator = true;
                }
    
                result.append(key);
                result.append("=");
                result.append(RoundUtil.getInstance().round(statisticData.get(key).getSum(), 2));
            }
        }
        
        result.append("]");
        return result.toString();
    }
}
