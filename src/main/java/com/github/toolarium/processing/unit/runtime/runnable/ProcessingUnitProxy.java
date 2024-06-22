/*
 * ProcessingUnitProxy.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingPersistence;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link IProcessingUnitProxy}.
 * 
 * @author patrick
 */
public final class ProcessingUnitProxy implements IProcessingUnitProxy {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingUnitProxy.class);
    private String id;
    private String name;
    private Class<? extends IProcessingUnit> processingUnitClass;
    private IProcessingUnit processingUnit;
    private List<Parameter> parameterList;
    private IProcessStatus lastProcessStatus;
    private IProcessingUnitContext processingUnitContext;
    private ProcessingRuntimeStatus processingRuntimeStatus;
    private List<String> processStatusMessageList;
    private Instant startTimestamp;
    private Instant lastStartTimestamp;
    private long duration;


    /**
     * Constructor
     * 
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnitClass the processing unit class
     * @param processingUnit the processing unit
     * @param parameterList the parameter list
     * @param lastProcessStatus the last process status or null
     * @param processingUnitContext the processing unit context
     * @param processingRuntimeStatus the processing runtime status
     * @param processStatusMessageList the processing status message list
     * @param startTimestamp the start time stamp
     * @param duration the spent duration in milliseconds
     */
    private ProcessingUnitProxy(final String id, // CHECKSTYLE IGNORE THIS LINE
                                final String name,
                                final Class<? extends IProcessingUnit> processingUnitClass,
                                final IProcessingUnit processingUnit, 
                                final List<Parameter> parameterList, 
                                final IProcessStatus lastProcessStatus,
                                final IProcessingUnitContext processingUnitContext,
                                final ProcessingRuntimeStatus processingRuntimeStatus,
                                final List<String> processStatusMessageList,
                                final Instant startTimestamp,
                                final long duration) {
        this.id = id;
        this.name = name;
        this.processingUnitClass = processingUnitClass;
        this.processingUnit = processingUnit;
        this.parameterList = parameterList;
        this.lastProcessStatus = lastProcessStatus;
        this.processingUnitContext = processingUnitContext;
        this.processingRuntimeStatus = processingRuntimeStatus;
        this.processStatusMessageList = processStatusMessageList;
        this.startTimestamp = startTimestamp;
        this.lastStartTimestamp = startTimestamp;
        this.duration = duration;
        
        if (this.duration < 0) {
            this.duration = 0;
        }
        
        if (this.duration > 0) {
            this.lastStartTimestamp = Instant.now();
        } 
    }

    
    /**
     * Initialize a {@link IProcessingUnit}:
     * <li>Initialize the processing unit class
     * <li>Validate the input parameters against the parameter definition
     * <li>Calls on the {@link IProcessingUnit} instance initialize method
     *
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingUnitContext the processing unit context
     * @return the {@link IProcessingUnitProxy}
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public static ProcessingUnitProxy init(String id, String name, Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) 
            throws ValidationException, ProcessingException {
        IProcessingUnit processingUnit = null;
        
        final String processing = ProcessingUnitUtil.getInstance().preapre(id, name, processingUnitClass);
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initialize processing unit class " + processing);
            }
            processingUnit = createProcessingUnitInstance(processingUnitClass);
            final Instant startTimestamp = Instant.now();
            
            // get parameter definition
            if (LOG.isDebugEnabled()) {
                LOG.debug("Get parameter definition of processing unit instance " + processing);
            }
            processingUnit.getParameterDefinition();

            // check consistency
            if (LOG.isDebugEnabled()) {
                LOG.debug("Validate parameter list of processing unit instance " + processing);
            }
            processingUnit.validateParameterList(parameterList);

            // initialize
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initialize the processing unit instance " + processing + " with parameter list [" + parameterList + "]");
            }
            processingUnit.initialize(parameterList, processingUnitContext);
            
            LOG.info("Successful initialized processing unit instance " + processing);
            return new ProcessingUnitProxy(id, name, processingUnitClass, processingUnit, parameterList, null, processingUnitContext, ProcessingRuntimeStatus.SUCCESSFUL, new ArrayList<String>(), startTimestamp, 0);
        } catch (RuntimeException e) {
            if (processingUnit != null) {
                try {
                    // release resource
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Release resource of processing unit instance " + processing, e);
                    }
                    processingUnit.releaseResource();
                    processingUnit = null;
                } catch (Exception ex) {
                    LOG.warn("Could not release resource from processing unit instance " + processing + ": " + ex.getMessage(), ex);
                }                
            }
            
            throw e;
        }
    }


    /**
     * Resume a {@link IProcessingUnit}:
     * <li>Initialize the processing unit class
     * <li>Validate the input parameters against the parameter definition
     * <li>Calls on the {@link IProcessingUnit} instance resumeProcessing method
     *
     *
     * @param persisted the persisted processing unit to resume
     * @return the {@link IProcessingUnitProxy}
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public static ProcessingUnitProxy resume(byte[] persisted) throws ValidationException, ProcessingException {
        Class<? extends IProcessingUnit> processingUnitClass = null;
        IProcessingUnit processingUnit = null;
        
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Resume processing unit instance...");
            }
            
            ProcessingPersistenceContainer resumeProcessingPersistence = ProcessingPersistenceContainer.toProcessingPersistenceContainer(persisted);
            if (resumeProcessingPersistence == null || resumeProcessingPersistence.getProcessingUnitClass() == null) {
                throw new ValidationException("Could not recover processing unit instance!");
            }
            
            final String processing = ProcessingUnitUtil.getInstance().preapre(resumeProcessingPersistence.getId(), resumeProcessingPersistence.getName(), resumeProcessingPersistence.getProcessingUnitClass());
            processingUnitClass = resumeProcessingPersistence.getProcessingUnitClass();

            // create the process unit instance
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initialize processing unit class " + processing + "...");
            }
            processingUnit = createProcessingUnitInstance(processingUnitClass);

            // check consistency
            if (LOG.isDebugEnabled()) {
                LOG.debug("Validate parameter list of processing unit instance " + processing);
            }
            processingUnit.validateParameterList(resumeProcessingPersistence.getParameterList());
            final IProcessStatus lastProcessStatus = resumeProcessingPersistence.getProcessingStatus();
            IProcessingUnitContext processingUnitContext = resumeProcessingPersistence.getProcessingUnitContext();
            
            // resume processing
            if (LOG.isDebugEnabled()) {
                LOG.debug("Resume the processing unit instance " + processing + " with parameter list [" + resumeProcessingPersistence.getParameterList() + "]");
            }
            processingUnit.resumeProcessing(resumeProcessingPersistence.getParameterList(), resumeProcessingPersistence.getProcessingStatus().getProcessingProgress(), resumeProcessingPersistence.getProcessingPersistence(), processingUnitContext);
            LOG.info("Successful resumed processing unit instance " + processing);
            return new ProcessingUnitProxy(resumeProcessingPersistence.getId(), 
                                           resumeProcessingPersistence.getName(),
                                           resumeProcessingPersistence.getProcessingUnitClass(), 
                                           processingUnit, 
                                           resumeProcessingPersistence.getParameterList(), 
                                           lastProcessStatus, 
                                           processingUnitContext,
                                           resumeProcessingPersistence.getProcessingRuntimeStatus(),
                                           resumeProcessingPersistence.getProcessingStatusMessageList(),
                                           resumeProcessingPersistence.getStartTimestamp(),
                                           resumeProcessingPersistence.getDuration());
        } catch (RuntimeException e) {
            if (processingUnit != null) {
                try {
                    // release resource
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Release resource of processing unit instance [" + processingUnitClass + "].");
                    }
                    processingUnit.releaseResource();
                    processingUnit = null;
                } catch (Exception ex) {
                    LOG.warn("Could not release resource from processing unit instance [" + processingUnitClass + "]: " + ex.getMessage(), ex);
                }                
            }
            
            throw e;
        }
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy#getParameterDefinition()
     */
    @Override
    public List<ParameterDefinition> getParameterDefinition() {
        return getProcessingUnit().getParameterDefinition();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy#processUnit()
     */
    @Override
    public boolean processUnit() {
        boolean continueProcessing = false;
        try {
            // process
            lastProcessStatus = getProcessingUnit().processUnit(processingUnitContext);
            
            // update the status
            updateProcessingStatusType(lastProcessStatus);
            updateProcessingStatusMessageList(lastProcessStatus);
            
            // check the progress
            continueProcessing = lastProcessStatus != null && lastProcessStatus.hasNext();
        } catch (ValidationException ve) {
            continueProcessing = !ve.abortProcessing();
            processStatusMessageList.add(prepare(ve.getMessage(), "Exception occured " + ve.getClass()  + "!"));
            
            if (continueProcessing) {
                processingRuntimeStatus = ProcessingRuntimeStatus.WARN;
            } else {
                processingRuntimeStatus = ProcessingRuntimeStatus.ERROR;
            }
        } catch (ProcessingException pe) {
            continueProcessing = !pe.abortProcessing();
            processStatusMessageList.add(prepare(pe.getMessage(), "Exception occured " + pe.getClass()  + "!"));
            
            if (continueProcessing) {
                processingRuntimeStatus = ProcessingRuntimeStatus.WARN;
            } else {
                processingRuntimeStatus = ProcessingRuntimeStatus.ERROR;
            }
        } catch (RuntimeException e) {
            continueProcessing = false;
            processStatusMessageList.add(prepare(e.getMessage(), "Exception occured " + e.getClass()  + "!"));
            processingRuntimeStatus = ProcessingRuntimeStatus.ERROR;
        }
        
        return continueProcessing;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy#suspendProcessing()
     */
    @Override
    public byte[] suspendProcessing() throws ProcessingException {
        try {
            // create persistence container
            if (LOG.isDebugEnabled()) {
                LOG.debug("Suspend processing unit instance [" + processingUnitClass + "]...");
            }
            IProcessingPersistence processingPersistence = getProcessingUnit().suspendProcessing();
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Create processing persistence container of processing unit instance [" + processingUnitClass + "]...");
            }
            
            final ProcessingPersistenceContainer suspendProcessingPersistence = 
                    new ProcessingPersistenceContainer(id,
                                                       name,
                                                       getProcessingUnitClass(), 
                                                       getParameterList(), 
                                                       processingPersistence, 
                                                       lastProcessStatus, 
                                                       processingUnitContext,
                                                       processingRuntimeStatus,
                                                       processStatusMessageList,
                                                       startTimestamp,
                                                       getDuration());
    
            // persist...
            return ProcessingPersistenceContainer.toByteArray(suspendProcessingPersistence);
        } catch (RuntimeException e) {
            throw e;
        } finally {
            if (processingUnit != null) {
                try {
                    // release resource
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Release resource of processing unit instance [" + processingUnitClass + "].");
                    }
                    processingUnit.releaseResource();
                    processingUnit = null;
                } catch (Exception ex) {
                    LOG.warn("Could not release resource from processing unit instance [" + processingUnitClass + "]: " + ex.getMessage(), ex);
                }                
            }
            
            processingUnitClass = null;
            lastProcessStatus = null;
        }
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy#releaseResource()
     */
    @Override
    public void releaseResource() throws ProcessingException {
        try { 
            getProcessingUnit().releaseResource();
        } catch (ProcessingException e) {
            // NOP
        }
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy#getProcessStatus()
     */
    @Override
    public IProcessStatus getProcessStatus() {
        return lastProcessStatus;
    }

    
    /**
     * Get the processing runtime status
     * 
     * @return the processing runtime status
     */
    public ProcessingRuntimeStatus getProcessingRuntimeStatus() {
        return processingRuntimeStatus;
    }
    
    
    /**
     * Get the status message list
     *
     * @return the status message list
     */
    public List<String> getStatusMessageList() {
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
     * @return the duration in milliseconds
     */
    public long getDuration() {
        return duration + Instant.now().toEpochMilli() - lastStartTimestamp.toEpochMilli();
    }
    
    
    /**
     * Get the processing unit class.
     *
     * @return the processing unit class
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public Class<? extends IProcessingUnit> getProcessingUnitClass() {
        if (processingUnitClass == null) {
            throw new ProcessingException("Processing unit class is not initialized!", true);
        }
        
        return processingUnitClass;
    }


    /**
     * Get the processing unit.
     *
     * @return the processing unit
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public IProcessingUnit getProcessingUnit() {
        if (processingUnit == null) {
            throw new ProcessingException("Processing unit is not initialized!", true);
        }
        
        return processingUnit;
    }

    
    /**
     * Get the processing parameter list.
     *
     * @return the processing parameter list
     */
    public List<Parameter> getParameterList() {
        return parameterList;
    }
    

    /**
     * Get the processing unit context
     *
     * @return the processing unit context
     */
    public IProcessingUnitContext getProcessingUnitContext() {
        return processingUnitContext;
    }

    
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ProcessingUnitUtil.getInstance().preapre(id, name, processingUnitClass);
    }

   
    /**
     * Create the processing unit implementation
     *
     * @param processingUnitClass the class
     * @return the instance
     * @throws ValidationException If the instance of the processing unit cannot be initialized correctly 
     */
    private static IProcessingUnit createProcessingUnitInstance(Class<? extends IProcessingUnit> processingUnitClass) throws ValidationException {
        try {
            return processingUnitClass.getDeclaredConstructor().newInstance();
        } catch (Exception t) {
            throw new ValidationException("Could not initialize " + processingUnitClass.getName() + ": " + t.getMessage(), t);
        }
    }


    /**
     * Update the processing status type
     *
     * @param lastProcessStatus the last process status
     */
    private void updateProcessingStatusType(IProcessStatus lastProcessStatus) {
        if (lastProcessStatus == null || lastProcessStatus.getProcessingProgress() == null) {
            return;
        }
        
        ProcessingRuntimeStatus processingRuntimeStatus = lastProcessStatus.getProcessingProgress().getProcessingRuntimeStatus();
        if (ProcessingRuntimeStatus.SUCCESSFUL.equals(processingRuntimeStatus)) {
            // NOP
        } else if (ProcessingRuntimeStatus.WARN.equals(processingRuntimeStatus)) {
            if (ProcessingRuntimeStatus.SUCCESSFUL.equals(this.processingRuntimeStatus)) {
                this.processingRuntimeStatus = processingRuntimeStatus;
            }
        } else if (ProcessingRuntimeStatus.ERROR.equals(processingRuntimeStatus)) {
            if (!ProcessingRuntimeStatus.ERROR.equals(this.processingRuntimeStatus)) {
                this.processingRuntimeStatus = processingRuntimeStatus;
            }
        }
    }

    
    /**
     * Update the processing status message list
     *
     * @param lastProcessStatus the last process status
     */
    private void updateProcessingStatusMessageList(IProcessStatus lastProcessStatus) {
        if (lastProcessStatus == null || lastProcessStatus.getProcessingProgress() == null) {
            return;
        }
        
        final String statusMessage = lastProcessStatus.getProcessingProgress().getProcessingStatusMessage();
        if (statusMessage != null && !statusMessage.isBlank()) {
            processStatusMessageList.add(statusMessage.trim());
        }
    }
    
    
    /**
     * Prepare string
     *
     * @param message the message
     * @param defaultMessage the default message
     * @return the message
     */
    private String prepare(String message, String defaultMessage) {
        if (message == null || message.isBlank()) {
            return defaultMessage;
        }
        
        return message;
    }
}
