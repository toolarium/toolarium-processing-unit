/*
 * ProcessingUnitUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.util;

import com.github.toolarium.processing.unit.IProcessingUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Processing unit util
 * 
 * @author patrick
 */
public final class ProcessingUnitUtil {
    private Map<Class<? extends IProcessingUnit>, String> shortenClassReferenceMap;

    
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
        shortenClassReferenceMap = new ConcurrentHashMap<Class<? extends IProcessingUnit>, String>();
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
    public String preapre(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) {
        StringBuilder processing = new StringBuilder().append("Processing ");
        if (name != null && !name.isBlank()) {
            processing.append("[").append(name).append("]").append(" - ");
        }
        processing.append("ID:").append(id).append(" ");
        return processing.append(shortenClassReferenceAsString(processingUnitClass)).toString();
    }
    
    
    /**
     * Shorten the class reference as string
     *
     * @param processingUnitClass the class
     * @return the shorten classname as string
     */
    private String shortenClassReferenceAsString(Class<? extends IProcessingUnit> processingUnitClass) {
        if (processingUnitClass == null) {
            return "";
        }
        
        String name = shortenClassReferenceMap.get(processingUnitClass);
        if (name != null) {
            return name;
        }
        
        StringBuilder builder = new StringBuilder().append("[");
        final String[] split = processingUnitClass.getName().split("\\.");
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
