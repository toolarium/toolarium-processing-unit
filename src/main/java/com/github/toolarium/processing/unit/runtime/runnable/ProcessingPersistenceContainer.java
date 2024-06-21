/*
 * ProcessingPersistenceImpl.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingPersistence;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.runtime.ProcessStatus;
import com.github.toolarium.processing.unit.runtime.ProcessingProgress;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * Implements a processing persistence container.
 *
 * @author patrick
 */
public class ProcessingPersistenceContainer implements Serializable {
    private static final long serialVersionUID = -7733025343789892026L;
    private String id;
    private String name;
    private Class<? extends IProcessingUnit> processingUnitClass;
    private List<Parameter> parameterList;
    private IProcessingPersistence processingPersistence;
    private ProcessStatus processStatus;
    private IProcessingUnitContext processingUnitContext;
    private ProcessingRuntimeStatus processingRuntimeStatus;
    private List<String> processStatusMessageList;


    /**
     * Constructor
     *
     * @param id the unique id of the processing 
     * @param name the name of the processing
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingPersistence the processing persistence
     * @param processStatus the process status
     * @param processingUnitContext the processing context.
     * @param processingRuntimeStatus the process runtime status
     * @param processStatusMessageList the process status message list
     */
    public ProcessingPersistenceContainer(String id,
                                          String name,
                                          Class<? extends IProcessingUnit> processingUnitClass,
                                          List<Parameter> parameterList,
                                          IProcessingPersistence processingPersistence,
                                          IProcessStatus processStatus,
                                          IProcessingUnitContext processingUnitContext,
                                          ProcessingRuntimeStatus processingRuntimeStatus,
                                          List<String> processStatusMessageList) {
        this.id = id;
        this.name = name;
        this.processingUnitClass = processingUnitClass;
        this.parameterList = parameterList;
        this.processingPersistence = processingPersistence;
        this.processingUnitContext = processingUnitContext;
        this.processingRuntimeStatus = processingRuntimeStatus;
        this.processStatusMessageList = processStatusMessageList;

        if (processStatus != null) {
            ProcessingProgress processingProgress = new ProcessingProgress();
            
            if (processStatus.getProcessingProgress() != null) {
                processingProgress.init(processStatus.getProcessingProgress());
            }
            
            this.processStatus = new ProcessStatus(processingProgress);
            this.processStatus.setHasNext(processStatus.hasNext());
        }
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
    public IProcessingPersistence getProcessingPersistence() {
        return processingPersistence;
    }


    /**
     * Gets the process status
     *
     * @return the process status
     */
    public IProcessStatus getProcessingStatus() {
        return processStatus;
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
     * Convert the object into a byte array
     *
     * @param processingPersistenceContainer the processing persistence container
     * @return the byte array to persist
     * @throws ProcessingException In case the processing container can't be serialized properly 
     */
    public static byte[] toByteArray(ProcessingPersistenceContainer processingPersistenceContainer) throws ProcessingException {
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
    public static ProcessingPersistenceContainer toProcessingPersistenceContainer(byte[] persistedState) throws ProcessingException {
        try {
            ObjectInputStream objInStream = new ObjectInputStream(new ByteArrayInputStream(persistedState));
            ProcessingPersistenceContainer processingPersistenceContainer = (ProcessingPersistenceContainer)objInStream.readObject();
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
        return Objects.hash(id, name, parameterList, processingPersistence, processStatus, processingUnitClass, processingUnitContext);
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
        
        ProcessingPersistenceContainer other = (ProcessingPersistenceContainer) obj;
        return Objects.equals(parameterList, other.parameterList)
                && Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(processingPersistence, other.processingPersistence)
                && Objects.equals(processStatus, other.processStatus)
                && Objects.equals(processingUnitClass, other.processingUnitClass)
                && Objects.equals(processingUnitContext, other.processingUnitContext);
    }
}
