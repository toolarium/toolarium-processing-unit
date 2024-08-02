/*
 * AbstractProcessingUnitRunnable.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable.impl;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.common.formatter.TimeDifferenceFormatter;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import com.github.toolarium.processing.unit.runtime.runnable.IEmptyProcessingUnitHandler;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnableListener;
import com.github.toolarium.processing.unit.runtime.runnable.ProcessingUnitProxy;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract {@link IProcessingUnitRunnable} implementation.
 * 
 * @author patrick
 */
public abstract class AbstractProcessingUnitRunnable implements IProcessingUnitRunnable {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractProcessingUnitRunnable.class);
    private final String id;
    private final String name;
    private final Class<? extends IProcessingUnit> processingUnitClass;
    private final List<Parameter> parameterList;
    private IProcessingUnitContext processingUnitContext;
    private ProcessingUnitProxy processingUnitProxy;
    private ProcessingActionStatus processingActionStatus;
    private IProcessingUnitRunnableListener processingUnitRunnableListener;
    private Instant stopTimestamp;
    private Long duration;
    private TimeDifferenceFormatter timeDifferenceFormatter; 
    private IEmptyProcessingUnitHandler emptyProcessingUnitHandler;
    private Long maxNumberOfProcessingUnitCallsPerSecond;
    private volatile int lastProgressInPercentage;

    
    /**
     * Constructor
     *
     * @param id the unique id of this processing 
     * @param name the name of this processing unit runnable
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingUnitContext the processing context.
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public AbstractProcessingUnitRunnable(final String id, 
                                          final String name, 
                                          final Class<? extends IProcessingUnit> processingUnitClass,
                                          final List<Parameter> parameterList, 
                                          final IProcessingUnitContext processingUnitContext) {
        if (id != null && !id.isBlank()) {
            this.id = id;
        } else {
            this.id = UUID.randomUUID().toString();
        }

        this.name = name;
        this.processingUnitClass = processingUnitClass;
        this.parameterList = parameterList;
        this.processingUnitContext = processingUnitContext;
        this.processingUnitRunnableListener = null;
        this.stopTimestamp = null;
        this.duration = null;
        this.timeDifferenceFormatter = new TimeDifferenceFormatter();
        this.emptyProcessingUnitHandler = null;
        this.maxNumberOfProcessingUnitCallsPerSecond = null;
        this.lastProgressInPercentage = 0;
    }

    
    /**
     * Constructor
     *
     * @param suspendedState the suspended state
     * @param processingUnitRunnableListener the processing unit runnable listener
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public AbstractProcessingUnitRunnable(final byte[] suspendedState, IProcessingUnitRunnableListener processingUnitRunnableListener) {

        // initialize the processingUnitProxy
        setProcessingUnitProxy(ProcessingUnitProxy.resume(suspendedState));
        
        this.id = processingUnitProxy.getId();
        this.name = processingUnitProxy.getName();
        this.processingUnitClass = processingUnitProxy.getProcessingUnitClass();
        this.parameterList = processingUnitProxy.getParameterList();
        this.processingUnitContext = processingUnitProxy.getProcessingUnitContext();
        this.stopTimestamp = null;
        this.duration = null;
        this.timeDifferenceFormatter = new TimeDifferenceFormatter();
        this.emptyProcessingUnitHandler = processingUnitProxy.getEmptyProcessingUnitHandler();
        this.lastProgressInPercentage = 0;
        
        if (processingUnitProxy.getProcessingUnitProgress() != null) {
            this.lastProgressInPercentage = processingUnitProxy.getProcessingUnitProgress().getProgress();
        }

        // resume
        setProcessingUnitRunnableListener(processingUnitRunnableListener);
        setProcessingActionStatus(ProcessingActionStatus.RESUMING);
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getProcessingActionStatus()
     */
    @Override
    public ProcessingActionStatus getProcessingActionStatus() {
        return processingActionStatus;
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#releaseResource()
     */
    @Override
    public void releaseResource() throws ProcessingException {
        if (getProcessingUnitProxy() != null) {
            getProcessingUnitProxy().releaseResource();
        }

        processingUnitContext = null;
        processingUnitRunnableListener = null;
        emptyProcessingUnitHandler = null;
    }

    
    /**
     * Sets the processing action status
     *
     * @param processingActionStatus the processing action status
     */
    protected void setProcessingActionStatus(ProcessingActionStatus processingActionStatus) {
        ProcessingActionStatus previousProcessingActionStatus = this.processingActionStatus;
        this.processingActionStatus = processingActionStatus;
        
        if (ProcessingActionStatus.ENDED.equals(processingActionStatus) || ProcessingActionStatus.ENDED.equals(processingActionStatus)) {
            duration = getTimeMeasurement().getDuration();
            stopTimestamp = Instant.now();
            
            if (getEmptyProcessingUnitHandler() != null) {
                long emptyProcessingUnitHandlerDuration = getEmptyProcessingUnitHandler().getDuration();
                if (emptyProcessingUnitHandlerDuration > 0) {
                    duration -= emptyProcessingUnitHandlerDuration;
                }
            }
            
            if (duration < 0) {
                duration = 0L;
            }
        }
        
        notifyProcessingUnitState(previousProcessingActionStatus, processingActionStatus);
    }

    
    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getProcessingUnitProgress()
     */
    @Override
    public IProcessingUnitProgress getProcessingUnitProgress() {
        if (processingUnitProxy != null) {
            return processingUnitProxy.getProcessingUnitProgress();
        }
        
        return null;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getProcessingRuntimeStatus()
     */
    @Override
    public ProcessingRuntimeStatus getProcessingRuntimeStatus() {
        if (processingUnitProxy != null) {
            return processingUnitProxy.getProcessingRuntimeStatus();
        }
        
        return null;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getStatusMessageList()
     */
    @Override
    public List<String> getStatusMessageList() {
        if (processingUnitProxy != null) {
            return processingUnitProxy.getStatusMessageList();
        }
        
        return null;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getTimeMeasurement()
     */
    @Override
    public IProcessingUnitRuntimeTimeMeasurement getTimeMeasurement() {
        return new IProcessingUnitRuntimeTimeMeasurement() {

            /**
             * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement#getStartTimestamp()
             */
            @Override
            public Instant getStartTimestamp() {
                if (processingUnitProxy != null) {
                    return processingUnitProxy.getStartTimestamp();
                }
                
                return null;
            }

            
            /**
             * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement#getStopTimestamp()
             */
            @Override
            public Instant getStopTimestamp() {
                return stopTimestamp;
            }
            

            /**
             * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement#getDuration()
             */
            @Override
            public long getDuration() {
                if (duration != null) {
                    return duration;
                }
                
                if (processingUnitProxy != null) {
                    return processingUnitProxy.getDuration();
                }
                
                return 0;
            }


            /**
             * 
             * @see com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement#getDurationAsString()
             */
            @Override
            public String getDurationAsString() {
                return getTimeDifferenceFormatter().formatAsString(getDuration());
            }
            
            
            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public String toString() {
                return getDurationAsString();
            }
        };
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable#getParameterList()
     */
    @Override
    public  List<Parameter> getParameterList() {
        return parameterList;
    }   
    
    
    /** 
     * Get the empty processing unit handler
     *
     * @return the empty processing unit handler
     */
    public IEmptyProcessingUnitHandler getEmptyProcessingUnitHandler() {
        if (processingUnitProxy != null) {
            return processingUnitProxy.getEmptyProcessingUnitHandler();
        }
        
        return emptyProcessingUnitHandler;
    }

    
    /**
     * Set the empty processing unit handler
     *
     * @param emptyProcessingUnitHandler the empty processing unit handler
     */
    public void setEmptyProcessingUnitHandler(IEmptyProcessingUnitHandler emptyProcessingUnitHandler) {
        if (processingUnitProxy != null) {
            processingUnitProxy.setEmptyProcessingUnitHandler(emptyProcessingUnitHandler);
        } else {
            this.emptyProcessingUnitHandler = emptyProcessingUnitHandler;
        }
    }

    
    /**
     * Defines the max calls per second to throttle the processing unit
     *
     * @param maxNumberOfProcessingUnitCallsPerSecond the max number of processing units per second
     */
    public void setMaxNumberOfProcessingUnitCallsPerSecond(Long maxNumberOfProcessingUnitCallsPerSecond) {
        if (maxNumberOfProcessingUnitCallsPerSecond == null || maxNumberOfProcessingUnitCallsPerSecond.longValue() <= 0) {
            this.maxNumberOfProcessingUnitCallsPerSecond = null;
        } else {
            this.maxNumberOfProcessingUnitCallsPerSecond = maxNumberOfProcessingUnitCallsPerSecond;
        }
    }

    
    /**
     * Get the max calls per second to throttle the processing unit
     *
     * @return the max number of processing units per second
     */
    public Long getMaxNumberOfProcessingUnitCallsPerSecond() {
        return maxNumberOfProcessingUnitCallsPerSecond;
    }

    
    /**
     * Get the processing unit context
     *
     * @return the processing unit context
     */
    protected IProcessingUnitContext getProcessingUnitContext() {
        return processingUnitContext;
    }

    
    /**
     * Create processing unit proxy
     * 
     * @return the processing unit proxy
     */
    protected ProcessingUnitProxy createProcessingUnitProxy() {
        processingUnitProxy = ProcessingUnitProxy.init(id, name, processingUnitClass, parameterList, processingUnitContext /* new ProcessingUnitContext(processingUnitContext) */);
        
        if (emptyProcessingUnitHandler != null) {
            processingUnitProxy.setEmptyProcessingUnitHandler(emptyProcessingUnitHandler);
        }
        
        return processingUnitProxy;
    }
    
    
    /**
     * Get the processing unit proxy
     *
     * @return the processing unit proxy
     */
    protected ProcessingUnitProxy getProcessingUnitProxy() {
        return processingUnitProxy;
    }

    
    /**
     * Set the processing unit proxy
     *
     * @param processingUnitProxy the processing unit proxy
     */
    protected void setProcessingUnitProxy(ProcessingUnitProxy processingUnitProxy) {
        this.processingUnitProxy = processingUnitProxy;
    }

    
    /**
     * Before process unit method call
     * 
     * @param continueProcessing the continue processing 
     * @return the continue processing
     */
    protected boolean afterProcessUnit(boolean continueProcessing) {
        return continueProcessing;
    }

    
    /**
     * Set the processing unit runnable listener
     *
     * @param processingUnitRunnableListener the processing unit runnable listener
     */
    protected void setProcessingUnitRunnableListener(final IProcessingUnitRunnableListener processingUnitRunnableListener) {
        this.processingUnitRunnableListener = processingUnitRunnableListener;
    }

    
    /**
     * Notify processing unit action status
     *
     * @param bandwidthThrottling the bandwidth throttling 
     */
    protected void notifyProcessingUnitProgress(IBandwidthThrottling bandwidthThrottling) {
        if (getProcessingUnitProgress() == null || lastProgressInPercentage == getProcessingUnitProgress().getProgress()) {
            return;
        }
        
        try {
            String className = "";
            if (processingUnitClass != null) {
                className = processingUnitClass.getName();
            }
            processingUnitRunnableListener.notifyProcessingUnitProgress(getId(), 
                                                                        getName(), 
                                                                        className, 
                                                                        getParameterList(), 
                                                                        getProcessingUnitContext(),
                                                                        getProcessingUnitProgress(),
                                                                        getProcessingActionStatus(),
                                                                        getProcessingRuntimeStatus(),
                                                                        getStatusMessageList(),
                                                                        getTimeMeasurement(),
                                                                        bandwidthThrottling,
                                                                        lastProgressInPercentage);
            lastProgressInPercentage = getProcessingUnitProgress().getProgress();
        } catch (RuntimeException e) {
            LOG.warn("Could not notify the processing unit progress to the processing unit runnable listener: " + e.getMessage(), e);
        }
    }

    
    /**
     * Notify processing unit action status
     *
     * @param previousProcessingActionStatus the previous processing action status
     * @param processingActionStatus the processing action status
     */
    protected void notifyProcessingUnitState(final ProcessingActionStatus previousProcessingActionStatus, final ProcessingActionStatus processingActionStatus) {
        if (processingUnitRunnableListener == null) {
            return;
        }
        
        try {
            String className = "";
            if (processingUnitClass != null) {
                className = processingUnitClass.getName();
            }
            processingUnitRunnableListener.notifyProcessingUnitState(getId(), getName(), className, previousProcessingActionStatus, processingActionStatus, getProcessingUnitProgress(), getTimeMeasurement(), getProcessingUnitContext());
        } catch (RuntimeException e) {
            LOG.warn("Could not notify the processing unit state to the processing unit runnable listener: " + e.getMessage(), e);
        }
    }
    
    
    /**
     * Get the time difference formatter
     *
     * @return the time difference formatter
     */
    protected TimeDifferenceFormatter getTimeDifferenceFormatter() {
        return timeDifferenceFormatter;
    }
    
    
    /**
     * Get the processing class
     *
     * @return the processing class
     */
    protected Class<? extends IProcessingUnit> getProcessingUnitClass() {
        return processingUnitClass;
    } 
}
