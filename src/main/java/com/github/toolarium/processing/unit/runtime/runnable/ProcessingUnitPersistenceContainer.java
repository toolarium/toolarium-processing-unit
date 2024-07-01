/*
 * ProcessingUnitPersistenceContainer.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitProgress;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;


/**
 * Implements a processing persistence container.
 *
 * @author patrick
 */
public class ProcessingUnitPersistenceContainer implements Serializable {
    private static final long serialVersionUID = -7733025343789892026L;
    private String id;
    private String name;
    private Class<? extends IProcessingUnit> processingUnitClass;
    private List<Parameter> parameterList;
    private IProcessingUnitPersistence processingPersistence;
    private IProcessingUnitProgress processingUnitProgress;
    private IProcessingUnitContext processingUnitContext;
    private ProcessingRuntimeStatus processingRuntimeStatus;
    private List<String> processStatusMessageList;
    private Instant startTimestamp;
    private long duration;
    private Long maxNumberOfProcessingUnitCallsPerSecond;
    
    
    /**
     * Constructor
     *
     * @param id the unique id of the processing 
     * @param name the name of the processing
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingPersistence the processing persistence
     * @param processingUnitProgress the processing unit progress
     * @param processingUnitContext the processing context.
     * @param processingRuntimeStatus the process runtime status
     * @param processStatusMessageList the process status message list
     * @param startTimestamp the start time stamp
     * @param duration the actual duration in milliseconds
     * @param maxNumberOfProcessingUnitCallsPerSecond the max number of processing unit calls per seconds
     */
    public ProcessingUnitPersistenceContainer(String id, // CHECKSTYLE IGNORE THIS LINE
                                              String name,
                                              Class<? extends IProcessingUnit> processingUnitClass,
                                              List<Parameter> parameterList,
                                              IProcessingUnitPersistence processingPersistence,
                                              IProcessingUnitProgress processingUnitProgress,
                                              IProcessingUnitContext processingUnitContext,
                                              ProcessingRuntimeStatus processingRuntimeStatus,
                                              List<String> processStatusMessageList,
                                              Instant startTimestamp,
                                              long duration,
                                              Long maxNumberOfProcessingUnitCallsPerSecond) {
        this.id = id;
        this.name = name;
        this.processingUnitClass = processingUnitClass;
        this.parameterList = parameterList;
        this.processingPersistence = processingPersistence;
        this.processingUnitProgress = new ProcessingUnitProgress(processingUnitProgress);
        this.processingUnitContext = processingUnitContext;
        this.processingRuntimeStatus = processingRuntimeStatus;
        this.processStatusMessageList = processStatusMessageList;
        this.startTimestamp = startTimestamp;
        this.duration = duration;
        this.maxNumberOfProcessingUnitCallsPerSecond = maxNumberOfProcessingUnitCallsPerSecond;
    }

    
    /**
     * Get the unique processing id
     *
     * @return the processing id
     */
    public String getId() {
        return id;
    }

    
    /**
     * Get the processing name
     *
     * @return the processing name
     */
    public String getName() {
        return name;
    }


    /**
     * Get the processing unit class
     *
     * @return the processing unit class
     */
    public Class<? extends IProcessingUnit> getProcessingUnitClass() {
        return processingUnitClass;
    }


    /**
     * Gets the parameter list
     *
     * @return the parameter list
     */
    public List<Parameter> getParameterList() {
        return parameterList;
    }


    /**
     * Gets the processing information
     *
     * @return the processing information
     */
    public IProcessingUnitPersistence getProcessingPersistence() {
        return processingPersistence;
    }


    /**
     * Gets the processing unit progress
     *
     * @return the processing unit progress
     */
    public IProcessingUnitProgress getProcessingUnitProgress() {
        return processingUnitProgress;
    }


    /**
     * Gets the process unit context
     *
     * @return the process unit context
     */
    public IProcessingUnitContext getProcessingUnitContext() {
        return processingUnitContext;
    }

    
    /**
     * Gets the process runtime status
     *
     * @return the process runtime status
     */
    public ProcessingRuntimeStatus getProcessingRuntimeStatus() {
        return processingRuntimeStatus;
    }

    
    /**
     * Gets the process status message list
     *
     * @return the process status message list
     */
    public List<String> getProcessingStatusMessageList() {
        return processStatusMessageList;
    }

    
    /**
     * Get the start time stamp
     *
     * @return the start time stamp
     */
    public Instant getStartTimestamp() {
        return startTimestamp;
    }
    
    
    /**
     * Get the duration in milliseconds
     *
     * @return the duration
     */
    public long getDuration() {
        return duration;
    }
    
    
    /**
     * Get the max number of processing unit calls per seconds
     *
     * @return the max number of processing unit calls per seconds
     */
    public Long getMaxNumberOfProcessingUnitCallsPerSecond() {
        return maxNumberOfProcessingUnitCallsPerSecond;
    }
    
    
    /**
     * Convert the object into a byte array
     *
     * @param processingPersistenceContainer the processing persistence container
     * @return the byte array to persist
     * @throws ProcessingException In case the processing container can't be serialized properly 
     */
    public static byte[] toByteArray(ProcessingUnitPersistenceContainer processingPersistenceContainer) throws ProcessingException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(outputStream);
            objOutStream.writeObject(processingPersistenceContainer);
            objOutStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (RuntimeException | IOException e) {
            throw new ProcessingException("Could not serialize processing persistence conatiner [" + processingPersistenceContainer.getClass() + "]: " + e.getMessage(), e, true);
        }
    }

    
    /**
     * Convert the object into a byte array
     *
     * @param persistedState the persisted state
     * @return the object representation
     * @throws ProcessingException In case the processing container can't be de-serialized properly 
     */
    public static ProcessingUnitPersistenceContainer toProcessingPersistenceContainer(byte[] persistedState) throws ProcessingException {
        try {
            ObjectInputStream objInStream = new ObjectInputStream(new ByteArrayInputStream(persistedState));
            ProcessingUnitPersistenceContainer processingPersistenceContainer = (ProcessingUnitPersistenceContainer)objInStream.readObject();
            objInStream.close();
            return processingPersistenceContainer;
        } catch (RuntimeException | ClassNotFoundException | IOException e) {
            throw new ProcessingException("Could not de-serialize processing persistence conatiner: " + e.getMessage(), e, true);
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(duration, id, name, parameterList, processingUnitProgress, processStatusMessageList,
                processingPersistence, processingRuntimeStatus, processingUnitClass, processingUnitContext,
                startTimestamp, maxNumberOfProcessingUnitCallsPerSecond);
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
        
        ProcessingUnitPersistenceContainer other = (ProcessingUnitPersistenceContainer) obj;
        return duration == other.duration && Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(parameterList, other.parameterList)
                && Objects.equals(processingUnitProgress, other.processingUnitProgress)
                && Objects.equals(processStatusMessageList, other.processStatusMessageList)
                && Objects.equals(processingPersistence, other.processingPersistence)
                && processingRuntimeStatus == other.processingRuntimeStatus
                && Objects.equals(processingUnitClass, other.processingUnitClass)
                && Objects.equals(processingUnitContext, other.processingUnitContext)
                && Objects.equals(startTimestamp, other.startTimestamp)
                && Objects.equals(maxNumberOfProcessingUnitCallsPerSecond, other.maxNumberOfProcessingUnitCallsPerSecond);
    }
}
