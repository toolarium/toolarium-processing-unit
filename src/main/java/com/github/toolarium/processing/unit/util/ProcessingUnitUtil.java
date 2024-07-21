/*
 * ProcessingUnitUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.util;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.common.util.ClassInstanceUtil;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.parallelization.IParallelProcessingUnit;
import com.github.toolarium.processing.unit.parallelization.IProcessingUnitObjectLockManagerSupport;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitInstanceManager;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitInstanceManager;
import com.github.toolarium.processing.unit.runtime.runnable.EmptyProcessingUnitHandler;
import com.github.toolarium.processing.unit.runtime.runnable.IEmptyProcessingUnitHandler;
import com.github.toolarium.processing.unit.runtime.runnable.ProcessingUnitPersistenceContainer;
import com.github.toolarium.processing.unit.runtime.runnable.parallelization.ParallelProcessingUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Processing unit util
 * 
 * @author patrick
 */
public final class ProcessingUnitUtil {
    private IProcessingUnitInstanceManager processingUnitInstanceManager;
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
        processingUnitInstanceManager = new ProcessingUnitInstanceManager();
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
     * Set the processing unit instance manager
     *
     * @param processingUnitInstanceManager the processing unit instance manager
     */
    public void setProcessingUnitInstanceManager(IProcessingUnitInstanceManager processingUnitInstanceManager) {
        this.processingUnitInstanceManager = processingUnitInstanceManager;
    }
    
    
    /**
     * Check if the processing unit class is available
     *
     * @param processingUnitClassname the class name
     * @return true if it the processing unit is available
     */
    public boolean isProcessingUnitAvailable(String processingUnitClassname) {
        return ClassInstanceUtil.getInstance().isClassAvailable(processingUnitClassname);
    }

    
    /**
     * Check if the processing unit class is available
     *
     * @param processingUnitClassname the class name
     * @return true if it the processing unit is available
     */
    public boolean isProcessingUnit(String processingUnitClassname) {
        return isProcessingUnitAvailable(processingUnitClassname) && ClassInstanceUtil.getInstance().implementsInterface(processingUnitClassname, IProcessingUnit.class);
    }

    
    /**
     * Check if the class is a parallel processing unit
     *
     * @param processingUnitClassname the class name
     * @return true if it is a parallel processing unit
     * @throws ClassNotFoundException In case the class could not be found
     */
    public boolean isParallelProcessingUnit(String processingUnitClassname) throws ClassNotFoundException {
        return isProcessingUnit(processingUnitClassname) && ClassInstanceUtil.getInstance().implementsInterface(processingUnitClassname, IParallelProcessingUnit.class);
    }

    
    /**
     * Check if the class is a parallel processing unit
     *
     * @param processingUnitClass the class 
     * @return true if it is a parallel processing unit
     */
    public boolean isParallelProcessingUnit(Class<? extends IProcessingUnit> processingUnitClass) {
        return IParallelProcessingUnit.class.isAssignableFrom(processingUnitClass);
    }

    
    /**
     * Check if the class is a parallel processing unit
     *
     * @param processingUnit the processing unit instance 
     * @return true if it is a parallel processing unit
     */
    public boolean isParallelProcessingUnit(IProcessingUnit processingUnit) {
        return processingUnit instanceof ParallelProcessingUnit;
    }

    
    /**
     * Check if the instance has object lock manager support
     *
     * @param processingUnit the processing unit 
     * @return true if it has support of unit object lock manager
     */
    public boolean hasProcessingUnitObjectLockManagerSupport(IProcessingUnit processingUnit) {
        return processingUnit instanceof IProcessingUnitObjectLockManagerSupport;
    }

    
    /**
     * Create the processing unit implementation
     *
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnitClass the class
     * @return the instance
     * @throws ValidationException If the instance of the processing unit cannot be initialized correctly 
     */
    public IProcessingUnit createProcessingUnitInstance(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
        try {
            IProcessingUnit processingUnit;
            
            if (isParallelProcessingUnit(processingUnitClass)) {
                processingUnit = createParallelProcessingUnitInstance(id, name, processingUnitClass);
            } else {
                processingUnit = createSingleProcessingUnitInstance(id, name, processingUnitClass);
            }

            return processingUnit;
        } catch (Exception t) {
            throw new ValidationException("Could not initialize " + processingUnitClass.getName() + ": " + t.getMessage(), t);
        }
    }

    
    /**
     * Create the processing unit implementation
     *
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnitClass the class
     * @return the instance
     * @throws ValidationException If the instance of the processing unit cannot be initialized correctly 
     */
    public IProcessingUnit createSingleProcessingUnitInstance(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
        try {
            return processingUnitInstanceManager.createProcessingUnitInstance(id, name, processingUnitClass);
        } catch (Exception t) {
            throw new ValidationException("Could not initialize " + processingUnitClass.getName() + ": " + t.getMessage(), t);
        }
    }

    
    /**
     * Create the processing unit implementation
     *
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnitClass the class
     * @return the instance
     * @throws ValidationException If the instance of the processing unit cannot be initialized correctly 
     */
    public IProcessingUnit createParallelProcessingUnitInstance(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
        try {
            if (isParallelProcessingUnit(processingUnitClass)) {
                return processingUnitInstanceManager.createParallelProcessingUnitInstance(id, name, processingUnitClass);
            } else {
                return createSingleProcessingUnitInstance(id, name, processingUnitClass);
            }
        } catch (Exception t) {
            throw new ValidationException("Could not initialize " + processingUnitClass.getName() + ": " + t.getMessage(), t);
        }
    }


    /**
     * Release resource 
     *
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnit the processing unit
     */
    public void releaseResource(String id, String name, IProcessingUnit processingUnit) {
        if (processingUnit == null) {
            return;
        }
        
        processingUnitInstanceManager.releaseResource(id, name, processingUnit);
    }
    

    /**
     * Get the empty processing unit handler
     *
     * @param processingUnit the processing unit
     * @return the empty processing unit handler
     */
    public IEmptyProcessingUnitHandler getEmptyProcessingUnitHandler(IProcessingUnit processingUnit) {
        IEmptyProcessingUnitHandler emptyProcessingUnitHandler;
        if (ProcessingUnitUtil.getInstance().isParallelProcessingUnit(processingUnit)) {
            emptyProcessingUnitHandler = ((ParallelProcessingUnit)processingUnit).getEmptyProcessingUnitHandler();
        } else {
            emptyProcessingUnitHandler = new EmptyProcessingUnitHandler();
        }
        
        return emptyProcessingUnitHandler;
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
