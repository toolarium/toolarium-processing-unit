/*
 * ProcessingUnitUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.util;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import com.github.toolarium.processing.unit.runtime.runnable.ProcessingUnitPersistenceContainer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Processing unit util
 * 
 * @author patrick
 */
public final class ProcessingUnitUtil {
    private Map<String, String> shortenClassReferenceMap;
    private ProcessingUnitProgressFormatter processingUnitProgressFormatter;

    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ProcessingUnitUtil INSTANCE = new ProcessingUnitUtil();
    }

    
    /**
     * Constructor
     */
    private ProcessingUnitUtil() {
        shortenClassReferenceMap = new ConcurrentHashMap<String, String>();
        processingUnitProgressFormatter = new ProcessingUnitProgressFormatter(" - ");
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ProcessingUnitUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Prepare processing log message
     * 
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @return the prepared string
     */
    public String toString(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) {
        if (processingUnitClass == null) {
            final String processingUnitClassName = null;
            return toString(id, name, processingUnitClassName);
        }
        
        return toString(id, name, processingUnitClass.getName());
    }
    
    
    /**
     * Prepare processing log message
     * 
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @return the prepared string
     */
    public String toString(String id, String name, String processingUnitClass) {
        StringBuilder processing = new StringBuilder().append("Processing ");
        if (name != null && !name.isBlank()) {
            processing.append("[").append(name).append("]").append(" - ");
        }
        processing.append("ID:").append(id);
        
        if (processingUnitClass != null) {
            processing.append(" ").append(shortenClassReferenceAsString(processingUnitClass));
        }
        return processing.toString();
    }

    
    /**
     * Format process unit progress
     *
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param processingProgress the progressing unit progress
     * @param processingActionStatus the action status
     * @param processingRuntimeStatus the runtime status
     * @return the formatted message
     */
    public String toString(String id, 
                           String name, 
                           String processingUnitClass,
                           IProcessingUnitProgress processingProgress, 
                           ProcessingActionStatus processingActionStatus,
                           ProcessingRuntimeStatus processingRuntimeStatus) {
        return toString(id, name, processingUnitClass, null, null, processingProgress, processingActionStatus, processingRuntimeStatus, null, null, null, null);
    }

    
    /**
     * Format process unit progress
     *
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param processingProgress the progressing unit progress
     * @param processingActionStatus the action status
     * @param processingRuntimeStatus the runtime status
     * @param messages the messages
     * @return the formatted message
     */
    public String toString(String id, 
                           String name, 
                           String processingUnitClass,
                           IProcessingUnitProgress processingProgress, 
                           ProcessingActionStatus processingActionStatus,
                           ProcessingRuntimeStatus processingRuntimeStatus,
                           List<String> messages) {
        return toString(id, name, processingUnitClass, null, null, processingProgress, processingActionStatus, processingRuntimeStatus, messages, null, null, null);
    }

    
    /**
     * Format process unit progress
     *
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param processingProgress the progressing unit progress
     * @param processingActionStatus the action status
     * @param processingRuntimeStatus the runtime status
     * @param messages the messages
     * @param timeMeasurement the time measurement
     * @param processingUnitThrottling the processing unit throttling
     * @return the formatted message
     */
    public String toString(String id, 
                           String name, 
                           String processingUnitClass,
                           IProcessingUnitProgress processingProgress, 
                           ProcessingActionStatus processingActionStatus,
                           ProcessingRuntimeStatus processingRuntimeStatus,
                           List<String> messages,
                           IProcessingUnitRuntimeTimeMeasurement timeMeasurement, 
                           IBandwidthThrottling processingUnitThrottling) {
        return toString(id, name, processingUnitClass, null, null, processingProgress, processingActionStatus, processingRuntimeStatus, messages,timeMeasurement, processingUnitThrottling);
    }

    
    /**
     * Format process unit progress
     *
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param parameters the parameters
     * @param processingUnitContext the processing unit context
     * @param processingProgress the progressing unit progress
     * @param processingActionStatus the action status
     * @param processingRuntimeStatus the runtime status
     * @param messages the messages
     * @param timeMeasurement the time measurement
     * @param processingUnitThrottling the processing unit throttling
     * @return the formatted message
     */
    public String toString(String id, // CHECKSTYLE IGNORE THIS LINE
                           String name, 
                           String processingUnitClass,
                           List<Parameter> parameters,
                           IProcessingUnitContext processingUnitContext,
                           IProcessingUnitProgress processingProgress, 
                           ProcessingActionStatus processingActionStatus,
                           ProcessingRuntimeStatus processingRuntimeStatus,
                           List<String> messages,
                           IProcessingUnitRuntimeTimeMeasurement timeMeasurement, 
                           IBandwidthThrottling processingUnitThrottling) {
        return toString(id, name, processingUnitClass, parameters, processingUnitContext, processingProgress, processingActionStatus, processingRuntimeStatus, messages,timeMeasurement, processingUnitThrottling, null);
    }

    
    /**
     * Format process unit progress
     *
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param parameters the parameters
     * @param processingUnitContext the processing unit context
     * @param processingProgress the progressing unit progress
     * @param processingActionStatus the action status
     * @param processingRuntimeStatus the runtime status
     * @param messages the messages
     * @param timeMeasurement the time measurement
     * @param processingUnitThrottling the processing unit throttling
     * @param processingPersistence the processing persistence
     * @return the formatted message
     */
    public String toString(String id, // CHECKSTYLE IGNORE THIS LINE
                           String name, 
                           String processingUnitClass,
                           List<Parameter> parameters,
                           IProcessingUnitContext processingUnitContext,
                           IProcessingUnitProgress processingProgress, 
                           ProcessingActionStatus processingActionStatus,
                           ProcessingRuntimeStatus processingRuntimeStatus,
                           List<String> messages,
                           IProcessingUnitRuntimeTimeMeasurement timeMeasurement, 
                           IBandwidthThrottling processingUnitThrottling,
                           IProcessingUnitPersistence processingPersistence) {
        return processingUnitProgressFormatter.toString(id, 
                                                        name, 
                                                        processingUnitClass,
                                                        parameters,
                                                        processingUnitContext,
                                                        processingProgress, 
                                                        processingActionStatus, 
                                                        processingRuntimeStatus, 
                                                        messages,
                                                        timeMeasurement, 
                                                        processingUnitThrottling,
                                                        processingPersistence);
    }

    
    /**
     * Get the persisted state as string 
     *
     * @param persistedState the persisted state
     * @return the string re-presenation
     */
    public String toString(byte[] persistedState) {
        ProcessingUnitPersistenceContainer processingPersistenceContainer = ProcessingUnitPersistenceContainer.toProcessingPersistenceContainer(persistedState);
        if (processingPersistenceContainer != null) {
            return processingPersistenceContainer.toString();
        }
        
        return "n/a";
    }
     
    
    /**
     * Shorten the class reference as string
     *
     * @param processingUnitClass the class
     * @return the shorten classname as string
     */
    private String shortenClassReferenceAsString(String processingUnitClass) {
        if (processingUnitClass == null) {
            return "";
        }
        
        String name = shortenClassReferenceMap.get(processingUnitClass);
        if (name != null) {
            return name;
        }
        
        StringBuilder builder = new StringBuilder().append("[");
        final String[] split = processingUnitClass.split("\\.");
        if (split.length > 1) {
            for (int i = 0; i < split.length - 1; i++) {
                builder.append(split[i].substring(0, 1)).append(".");
            }
        }
        
        name = builder.append(split[split.length - 1]).append("]").toString();
        shortenClassReferenceMap.put(processingUnitClass, name);
        return name;
    }
}
